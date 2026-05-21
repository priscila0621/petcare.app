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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
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
fun RegistroMascotaScreen(
    navController: NavHostController,
    onGuardarMascota: (Mascota) -> Unit
) {
    var nombre by rememberSaveable { mutableStateOf("") }
    var edadNumero by rememberSaveable { mutableStateOf("") }
    var unidadEdad by rememberSaveable { mutableStateOf("Años") }
    var tipo by rememberSaveable { mutableStateOf("Perro") }
    var raza by rememberSaveable { mutableStateOf("Mestizo / sin raza definida") }
    var peso by rememberSaveable { mutableStateOf("") }
    var fotoUri by rememberSaveable { mutableStateOf<String?>(null) }

    var mostrarError by rememberSaveable { mutableStateOf(false) }
    var mensajeError by rememberSaveable { mutableStateOf("") }
    var mostrarDialogoCancelar by rememberSaveable { mutableStateOf(false) }

    val tiposMascota = listOf("Perro", "Gato", "Conejo", "Ave", "Hámster", "Otro")
    val unidadesEdad = listOf("Semanas", "Meses", "Años")
    val razasDisponibles = obtenerRazasPorTipo(tipo)

    val hayDatosIngresados =
        nombre.isNotBlank() ||
                edadNumero.isNotBlank() ||
                peso.isNotBlank() ||
                fotoUri != null ||
                tipo != "Perro" ||
                raza != "Mestizo / sin raza definida" ||
                unidadEdad != "Años"

    val launcherImagen = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        fotoUri = uri?.toString()
    }

    BackHandler {
        if (hayDatosIngresados) {
            mostrarDialogoCancelar = true
        } else {
            navController.popBackStack()
        }
    }

    if (mostrarDialogoCancelar) {
        ConfirmarSalidaDialog(
            titulo = "Cancelar registro de mascota",
            mensaje = "Si sales sin guardar, la información ingresada de la mascota se perderá. ¿Deseas cancelar el registro?",
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
                titulo = "Registrar mascota",
                navigationIcon = {
                    PetCareBackButton(
                        icono = Icons.Default.ArrowBack,
                        onClick = {
                            if (hayDatosIngresados) {
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
                titulo = "Datos de la mascota",
                subtitulo = "Completa la información básica. La foto es opcional."
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
                            modifier = Modifier.fillMaxSize()
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
                    texto = "Guardar mascota",
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
                            val nuevaMascota = Mascota(
                                id = 0,
                                nombre = nombre.trim(),
                                edad = "${edadNumero.trim()} $unidadEdad",
                                tipo = tipo,
                                raza = raza,
                                peso = "${peso.trim()} kg",
                                fotoUri = fotoUri,
                                historial = emptyList()
                            )

                            onGuardarMascota(nuevaMascota)
                            navController.popBackStack()
                        }
                    }
                )
            }
        }
    }
}

/*
    Mantengo esta función porque también puede estar siendo usada en EditarMascotaScreen.
    Internamente ya usa el nuevo componente PetCareTextField.
*/
@Composable
fun CampoFormulario(
    valor: String,
    onValorCambio: (String) -> Unit,
    etiqueta: String,
    placeholder: String = ""
) {
    PetCareTextField(
        valor = valor,
        onValorCambio = onValorCambio,
        etiqueta = etiqueta,
        placeholder = placeholder
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CampoListaDesplegable(
    etiqueta: String,
    valorSeleccionado: String,
    opciones: List<String>,
    onSeleccionar: (String) -> Unit
) {
    var expandido by rememberSaveable { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expandido,
        onExpandedChange = {
            expandido = !expandido
        }
    ) {
        OutlinedTextField(
            value = valorSeleccionado,
            onValueChange = {},
            readOnly = true,
            label = {
                Text(text = etiqueta)
            },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = expandido
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(MenuAnchorType.PrimaryNotEditable),
            shape = RoundedCornerShape(18.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                unfocusedIndicatorColor = MaterialTheme.colorScheme.outline,
                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                focusedLabelColor = MaterialTheme.colorScheme.primary,
                unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )

        ExposedDropdownMenu(
            expanded = expandido,
            onDismissRequest = {
                expandido = false
            }
        ) {
            opciones.forEach { opcion ->
                DropdownMenuItem(
                    text = {
                        Text(text = opcion)
                    },
                    onClick = {
                        onSeleccionar(opcion)
                        expandido = false
                    }
                )
            }
        }
    }
}

fun formatearNombre(
    texto: String,
    limite: Int
): String {
    val soloLetras = texto
        .filter { caracter ->
            caracter.isLetter() || caracter.isWhitespace()
        }
        .take(limite)

    return soloLetras
        .lowercase()
        .replaceFirstChar { primeraLetra ->
            if (primeraLetra.isLowerCase()) {
                primeraLetra.titlecase()
            } else {
                primeraLetra.toString()
            }
        }
}

fun validarDatosMascota(
    nombre: String,
    edadNumero: String,
    peso: String
): String? {
    return when {
        nombre.isBlank() -> "El nombre de la mascota es obligatorio."
        nombre.length < 2 -> "El nombre debe tener al menos 2 caracteres."
        edadNumero.isBlank() -> "La edad es obligatoria."
        edadNumero.toIntOrNull() == null -> "La edad debe ser un número válido."
        edadNumero.toInt() <= 0 -> "La edad debe ser mayor que cero."
        peso.isBlank() -> "El peso es obligatorio."
        peso.toIntOrNull() == null -> "El peso debe ser un número válido."
        peso.toInt() <= 0 -> "El peso debe ser mayor que cero."
        else -> null
    }
}

fun obtenerRazasPorTipo(tipo: String): List<String> {
    return when (tipo) {
        "Perro" -> listOf(
            "Mestizo / sin raza definida",
            "Golden Retriever",
            "Labrador",
            "Bulldog",
            "Poodle",
            "Chihuahua",
            "Pastor Alemán",
            "Otro"
        )

        "Gato" -> listOf(
            "Mestizo / sin raza definida",
            "Doméstico de pelo corto",
            "Doméstico de pelo largo",
            "Siamés",
            "Persa",
            "Bengalí",
            "Otro"
        )

        "Conejo" -> listOf(
            "Mestizo / sin raza definida",
            "Cabeza de león",
            "Mini Lop",
            "Holandés",
            "Rex",
            "Otro"
        )

        "Ave" -> listOf(
            "No especificada",
            "Periquito",
            "Canario",
            "Loro",
            "Agapornis",
            "Otro"
        )

        "Hámster" -> listOf(
            "No especificada",
            "Sirio",
            "Ruso",
            "Roborovski",
            "Chino",
            "Otro"
        )

        else -> listOf(
            "No especificada",
            "Otro"
        )
    }
}