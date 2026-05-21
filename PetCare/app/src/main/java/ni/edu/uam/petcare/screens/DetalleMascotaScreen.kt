package ni.edu.uam.petcare.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.PlaylistAdd
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import ni.edu.uam.petcare.components.PetCareBackButton
import ni.edu.uam.petcare.components.PetCareCard
import ni.edu.uam.petcare.components.PetCareDangerButton
import ni.edu.uam.petcare.components.PetCarePrimaryButton
import ni.edu.uam.petcare.components.PetCareSecondaryButton
import ni.edu.uam.petcare.components.PetCareStatusChip
import ni.edu.uam.petcare.components.PetCareTopBar
import ni.edu.uam.petcare.model.EstadoServicio
import ni.edu.uam.petcare.model.Mascota
import androidx.compose.material.icons.filled.Edit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleMascotaScreen(
    navController: NavHostController,
    mascota: Mascota?,
    onEliminarMascota: (Mascota) -> Unit
) {
    var mostrarDialogoEliminar by rememberSaveable {
        mutableStateOf(false)
    }

    if (mostrarDialogoEliminar && mascota != null) {
        AlertDialog(
            onDismissRequest = {
                mostrarDialogoEliminar = false
            },
            title = {
                Text(text = "Eliminar mascota")
            },
            text = {
                Text(
                    text = "¿Estás segura de eliminar el registro de ${mascota.nombre}? Esta acción no se puede deshacer."
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        mostrarDialogoEliminar = false
                        onEliminarMascota(mascota)
                        navController.popBackStack()
                    }
                ) {
                    Text(
                        text = "Sí, eliminar",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        mostrarDialogoEliminar = false
                    }
                ) {
                    Text(text = "Cancelar")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            PetCareTopBar(
                titulo = "Detalle de mascota",
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
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                PetCareCard {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(105.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primaryContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            if (mascota.fotoUri != null) {
                                AsyncImage(
                                    model = mascota.fotoUri,
                                    contentDescription = "Foto de ${mascota.nombre}",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.Default.Pets,
                                    contentDescription = "Mascota",
                                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                    modifier = Modifier.size(54.dp)
                                )
                            }
                        }

                        Text(
                            text = mascota.nombre,
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )

                        Text(
                            text = "${mascota.tipo} • ${mascota.raza}",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        PetCareStatusChip(
                            texto = "${mascota.historial.size} servicios registrados",
                            activo = mascota.historial.isNotEmpty()
                        )
                    }
                }

                PetCareCard {
                    Text(
                        text = "Información general",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    DatoMascota(titulo = "Edad", valor = mascota.edad)
                    DatoMascota(titulo = "Tipo", valor = mascota.tipo)
                    DatoMascota(titulo = "Raza", valor = mascota.raza)
                    DatoMascota(titulo = "Peso", valor = mascota.peso)
                }

                PetCareCard {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.MedicalServices,
                            contentDescription = "Servicios",
                            tint = MaterialTheme.colorScheme.primary
                        )

                        Text(
                            text = " Historial de servicios",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    if (mascota.historial.isEmpty()) {
                        Text(
                            text = "Todavía no tiene servicios registrados.",
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    } else {
                        mascota.historial.take(3).forEach { servicio ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(
                                        text = servicio.tipoServicio.etiqueta,
                                        fontWeight = FontWeight.Medium,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )

                                    Text(
                                        text = "Fecha: ${servicio.fecha}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }

                                PetCareStatusChip(
                                    texto = servicio.estado.etiqueta,
                                    activo = servicio.estado == EstadoServicio.PENDIENTE
                                )
                            }
                        }

                        if (mascota.historial.size > 3) {
                            Text(
                                text = "Hay más servicios registrados. Revisalos en gestionar servicios.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                PetCarePrimaryButton(
                    texto = "Gestionar servicios",
                    icono = Icons.Default.PlaylistAdd,
                    onClick = {
                        navController.navigate("servicios/${mascota.id}")
                    }
                )

                PetCareSecondaryButton(
                    texto = "Editar información",
                    onClick = {
                        navController.navigate("editarMascota/${mascota.id}")
                    }
                )

                PetCareDangerButton(
                    texto = "Eliminar mascota",
                    icono = Icons.Default.Delete,
                    onClick = {
                        mostrarDialogoEliminar = true
                    }
                )
            }
        }
    }
}

@Composable
fun DatoMascota(
    titulo: String,
    valor: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = titulo,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface
        )

        Text(
            text = valor,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}