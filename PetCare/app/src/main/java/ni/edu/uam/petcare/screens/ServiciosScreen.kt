package ni.edu.uam.petcare.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import ni.edu.uam.petcare.components.PetCareBackButton
import ni.edu.uam.petcare.components.PetCareCard
import ni.edu.uam.petcare.components.PetCareFilterChip
import ni.edu.uam.petcare.components.PetCarePrimaryButton
import ni.edu.uam.petcare.components.PetCareSectionTitle
import ni.edu.uam.petcare.components.PetCareStatusChip
import ni.edu.uam.petcare.components.PetCareTextField
import ni.edu.uam.petcare.components.PetCareTopBar
import ni.edu.uam.petcare.model.EstadoServicio
import ni.edu.uam.petcare.model.Mascota
import ni.edu.uam.petcare.model.RegistroServicio
import ni.edu.uam.petcare.model.TipoServicio
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun ServiciosScreen(
    navController: NavHostController,
    mascota: Mascota?,
    onAgregarServicio: (RegistroServicio) -> Unit
) {
    var tipoSeleccionado by rememberSaveable {
        mutableStateOf(TipoServicio.BANIO)
    }

    var fecha by rememberSaveable {
        mutableStateOf("")
    }

    var notas by rememberSaveable {
        mutableStateOf("")
    }

    var mostrarError by rememberSaveable {
        mutableStateOf(false)
    }

    var mostrarCalendario by rememberSaveable {
        mutableStateOf(false)
    }

    var mostrarDialogoCancelar by rememberSaveable {
        mutableStateOf(false)
    }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val manana = LocalDate.now().plusDays(1)
    val mananaMillis = localDateToMillis(manana)

    val hayDatosIngresados =
        fecha.isNotBlank() ||
                notas.isNotBlank() ||
                tipoSeleccionado != TipoServicio.BANIO

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = mananaMillis,
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                return utcTimeMillis >= mananaMillis
            }
        }
    )

    BackHandler {
        if (hayDatosIngresados) {
            mostrarDialogoCancelar = true
        } else {
            navController.popBackStack()
        }
    }

    if (mostrarDialogoCancelar) {
        ConfirmarSalidaDialog(
            titulo = "Cancelar registro de servicio",
            mensaje = "Si sales sin guardar, la información del servicio se perderá. ¿Deseas cancelar el registro?",
            onConfirmarSalida = {
                mostrarDialogoCancelar = false
                navController.popBackStack()
            },
            onContinuarEditando = {
                mostrarDialogoCancelar = false
            }
        )
    }

    if (mostrarCalendario) {
        DatePickerDialog(
            onDismissRequest = {
                mostrarCalendario = false
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        val fechaSeleccionada = datePickerState.selectedDateMillis

                        if (fechaSeleccionada != null && fechaSeleccionada >= mananaMillis) {
                            fecha = convertirMillisAFecha(fechaSeleccionada)
                            mostrarError = false
                        }

                        mostrarCalendario = false
                    }
                ) {
                    Text(text = "Aceptar")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        mostrarCalendario = false
                    }
                ) {
                    Text(text = "Cancelar")
                }
            }
        ) {
            DatePicker(
                state = datePickerState
            )
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState
            )
        },
        topBar = {
            PetCareTopBar(
                titulo = "Servicios",
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

        if (mascota == null) {
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
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                PetCareSectionTitle(
                    titulo = "Servicios de ${mascota.nombre}",
                    subtitulo = "Agenda servicios veterinarios desde mañana en adelante."
                )

                PetCareCard {
                    Text(
                        text = "Nuevo servicio",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        TipoServicio.entries.forEach { tipo ->
                            PetCareFilterChip(
                                texto = tipo.etiqueta,
                                seleccionado = tipoSeleccionado == tipo,
                                onClick = {
                                    tipoSeleccionado = tipo
                                }
                            )
                        }
                    }

                    PetCareTextField(
                        valor = fecha,
                        onValorCambio = {},
                        etiqueta = "Fecha del servicio",
                        placeholder = "Selecciona una fecha",
                        soloLectura = true,
                        trailingIcon = {
                            IconButton(
                                onClick = {
                                    mostrarCalendario = true
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CalendarMonth,
                                    contentDescription = "Seleccionar fecha"
                                )
                            }
                        }
                    )

                    PetCareTextField(
                        valor = notas,
                        onValorCambio = {
                            notas = it
                        },
                        etiqueta = "Notas",
                        placeholder = "Ejemplo: Servicio agendado para revisión general",
                        unaLinea = false,
                        minLineas = 2
                    )

                    if (mostrarError) {
                        Text(
                            text = "Por favor selecciona una fecha para el servicio.",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }

                    PetCarePrimaryButton(
                        texto = "Guardar servicio",
                        icono = Icons.Default.Save,
                        onClick = {
                            if (fecha.isBlank()) {
                                mostrarError = true
                            } else {
                                val nuevoServicio = RegistroServicio(
                                    id = 0,
                                    tipoServicio = tipoSeleccionado,
                                    fecha = fecha.trim(),
                                    estado = EstadoServicio.PENDIENTE,
                                    notas = notas.trim()
                                )

                                onAgregarServicio(nuevoServicio)

                                fecha = ""
                                notas = ""
                                tipoSeleccionado = TipoServicio.BANIO
                                mostrarError = false

                                scope.launch {
                                    snackbarHostState.showSnackbar(
                                        message = "Servicio registrado correctamente"
                                    )
                                }
                            }
                        }
                    )
                }

                Text(
                    text = "Historial",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )

                if (mascota.historial.isEmpty()) {
                    PetCareCard {
                        Text(
                            text = "Aún no hay servicios registrados.",
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(mascota.historial) { servicio ->
                            ServicioCard(servicio = servicio)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ServicioCard(
    servicio: RegistroServicio
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 3.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .padding(end = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.MedicalServices,
                    contentDescription = "Servicio",
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = servicio.tipoServicio.etiqueta,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Text(
                    text = "Fecha: ${servicio.fecha}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                if (servicio.notas.isNotBlank()) {
                    Text(
                        text = servicio.notas,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            PetCareStatusChip(
                texto = servicio.estado.etiqueta,
                activo = servicio.estado == EstadoServicio.PENDIENTE
            )
        }
    }
}

fun localDateToMillis(fecha: LocalDate): Long {
    return fecha
        .atStartOfDay(ZoneId.systemDefault())
        .toInstant()
        .toEpochMilli()
}

fun convertirMillisAFecha(millis: Long): String {
    val fecha = Instant
        .ofEpochMilli(millis)
        .atZone(ZoneId.systemDefault())
        .toLocalDate()

    val formato = DateTimeFormatter.ofPattern("dd/MM/yyyy")

    return fecha.format(formato)
}