package ni.edu.uam.petcare.screens

import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import ni.edu.uam.petcare.components.PetCareBackButton
import ni.edu.uam.petcare.components.PetCareCard
import ni.edu.uam.petcare.components.PetCarePrimaryButton
import ni.edu.uam.petcare.components.PetCareSectionTitle
import ni.edu.uam.petcare.components.PetCareTextField
import ni.edu.uam.petcare.components.PetCareTopBar
import ni.edu.uam.petcare.model.Mascota

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditarMascotaScreen(
    navController: NavHostController,
    mascota: Mascota?,
    onActualizarMascota: (Mascota) -> Unit
) {
    if (mascota == null) {
        Scaffold(
            topBar = {
                PetCareTopBar(
                    titulo = "Editar mascota",
                    navigationIcon = {
                        PetCareBackButton(
                            icono = Icons.Default.ArrowBack,
                            onClick = {
                                navController.popBackStack()
                            }
                        )
                    }
                )
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No se encontró la mascota",
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }

        return
    }

    val edadSeparada = separarEdad(mascota.edad)
    val pesoSinUnidad = mascota.peso.filter { it.isDigit() }

    var nombre by rememberSaveable { mutableStateOf(mascota.nombre) }
    var edadNumero by rememberSaveable { mutableStateOf(edadSeparada.first) }
    var unidadEdad by rememberSaveable { mutableStateOf(edadSeparada.second) }
    var tipo by rememberSaveable { mutableStateOf(mascota.tipo) }
    var raza by rememberSaveable { mutableStateOf(mascota.raza) }
    var peso by rememberSaveable { mutableStateOf(pesoSinUnidad) }
    var fotoUri by rememberSaveable { mutableStateOf(mascota.fotoUri) }

    var mostrarError by rememberSaveable { mutableStateOf(false) }
    var mensajeError by rememberSaveable { mutableStateOf("") }
    var mostrarDialogoCancelar by rememberSaveable { mutableStateOf(false) }

    val tiposMascota = listOf("Perro", "Gato", "Conejo", "Ave", "Hámster", "Otro")
    val unidadesEdad = listOf("Semanas", "Meses", "Años")
    val razasDisponibles = obtenerRazasPorTipo(tipo)

    val hayCambiosSinGuardar =
        nombre != mascota.nombre ||
                "$edadNumero $unidadEdad" != mascota.edad ||
                tipo != mascota.tipo ||
                raza != mascota.raza ||
                "$peso kg" != mascota.peso ||
                fotoUri != mascota.fotoUri

    val launcherImagen = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        fotoUri = uri?.toString()
    }

    BackHandler {
        if (hayCambiosSinGuardar) {
            mostrarDialogoCancelar = true
        } else {
            navController.popBackStack()
        }
    }

    if (mostrarDialogoCancelar) {
        ConfirmarSalidaDialog(
            titulo = "Cancelar edición",
            mensaje = "Si sales sin guardar, los cambios realizados se perderán. ¿Deseas cancelar la edición?",
            onConfirmarSalida = {
                mostrarDialogoCancelar = false
                navController.popBackStack()
            },
            onContinuarEditando = {
                mostrarDialogoCancelar = false
            }
        )
    }

    Scaffold(
        topBar = {
            PetCareTopBar(
                titulo = "Editar mascota",
                navigationIcon = {
                    PetCareBackButton(
                        icono = Icons.Default.ArrowBack,
                        onClick = {
                            if (hayCambiosSinGuardar) {
                                mostrarDialogoCancelar = true
                            } else {
                                navController.popBackStack()
                            }
                        }
                    )
                }
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            PetCareSectionTitle(
                titulo = "Actualizar información",
                subtitulo = "Modifica los datos de ${mascota.nombre}. La foto es opcional."
            )

            PetCareCard {
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer)
                        .clickable {
                            launcherImagen.launch("image/*")
                        }
                        .align(Alignment.CenterHorizontally),
                    contentAlignment = Alignment.Center
                ) {
                    if (fotoUri != null) {
                        AsyncImage(
                            model = fotoUri,
                            contentDescription = "Foto de la mascota",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.AddAPhoto,
                                contentDescription = "Agregar foto",
                                tint = MaterialTheme.colorScheme.onPrimaryContainer
                            )

                            Text(
                                text = "Agregar foto",
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }

                PetCareTextField(
                    valor = nombre,
                    onValorCambio = { nuevoValor ->
                        nombre = formatearNombre(nuevoValor, 25)
                    },
                    etiqueta = "Nombre",
                    placeholder = "Ejemplo: Luna"
                )

                PetCareTextField(
                    valor = edadNumero,
                    onValorCambio = { nuevoValor ->
                        edadNumero = nuevoValor.filter { it.isDigit() }.take(2)
                    },
                    etiqueta = "Edad",
                    placeholder = "Ejemplo: 3"
                )

                CampoListaDesplegable(
                    etiqueta = "Unidad de edad",
                    valorSeleccionado = unidadEdad,
                    opciones = unidadesEdad,
                    onSeleccionar = {
                        unidadEdad = it
                    }
                )

                CampoListaDesplegable(
                    etiqueta = "Tipo de mascota",
                    valorSeleccionado = tipo,
                    opciones = tiposMascota,
                    onSeleccionar = { nuevoTipo ->
                        tipo = nuevoTipo
                        raza = obtenerRazasPorTipo(nuevoTipo).first()
                    }
                )

                CampoListaDesplegable(
                    etiqueta = "Raza",
                    valorSeleccionado = raza,
                    opciones = razasDisponibles,
                    onSeleccionar = {
                        raza = it
                    }
                )

                PetCareTextField(
                    valor = peso,
                    onValorCambio = { nuevoValor ->
                        peso = nuevoValor.filter { it.isDigit() }.take(3)
                    },
                    etiqueta = "Peso",
                    placeholder = "Ejemplo: 12"
                )

                if (mostrarError) {
                    Text(
                        text = mensajeError,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                PetCarePrimaryButton(
                    texto = "Guardar cambios",
                    icono = Icons.Default.Save,
                    onClick = {
                        val validacion = validarDatosMascota(
                            nombre = nombre,
                            edadNumero = edadNumero,
                            peso = peso
                        )

                        if (validacion != null) {
                            mostrarError = true
                            mensajeError = validacion
                        } else {
                            val mascotaActualizada = mascota.copy(
                                nombre = nombre.trim(),
                                edad = "${edadNumero.trim()} $unidadEdad",
                                tipo = tipo,
                                raza = raza,
                                peso = "${peso.trim()} kg",
                                fotoUri = fotoUri
                            )

                            onActualizarMascota(mascotaActualizada)
                            navController.popBackStack()
                        }
                    }
                )
            }
        }
    }
}

fun separarEdad(edad: String): Pair<String, String> {
    val numero = edad.filter { it.isDigit() }

    val unidad = when {
        edad.contains("semana", ignoreCase = true) -> "Semanas"
        edad.contains("mes", ignoreCase = true) -> "Meses"
        edad.contains("año", ignoreCase = true) -> "Años"
        edad.contains("anos", ignoreCase = true) -> "Años"
        else -> "Años"
    }

    return Pair(numero, unidad)
}