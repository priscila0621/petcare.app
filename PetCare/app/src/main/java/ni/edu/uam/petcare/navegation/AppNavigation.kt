package ni.edu.uam.petcare.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ni.edu.uam.petcare.data.MascotaData
import ni.edu.uam.petcare.model.Mascota
import ni.edu.uam.petcare.screens.HomeScreen
import ni.edu.uam.petcare.screens.RegistroMascotaScreen
import ni.edu.uam.petcare.screens.DetalleMascotaScreen
import ni.edu.uam.petcare.screens.ServiciosScreen
import ni.edu.uam.petcare.screens.EditarMascotaScreen

@Composable
fun AppNavigation() {

    val navController = rememberNavController()

    val mascotas = remember {
        mutableStateListOf<Mascota>().apply {
            addAll(MascotaData.obtenerMascotasIniciales())
        }
    }

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            HomeScreen(
                navController = navController,
                mascotas = mascotas
            )
        }

        composable("registroMascota") {
            RegistroMascotaScreen(
                navController = navController,
                onGuardarMascota = { nuevaMascota ->
                    val nuevoId = (mascotas.maxOfOrNull { it.id } ?: 0) + 1

                    mascotas.add(
                        nuevaMascota.copy(
                            id = nuevoId
                        )
                    )
                }
            )
        }

        composable("editarMascota/{mascotaId}") { backStackEntry ->

            val mascotaId = backStackEntry.arguments
                ?.getString("mascotaId")
                ?.toIntOrNull()

            val mascotaSeleccionada = mascotas.find { mascota ->
                mascota.id == mascotaId
            }

            EditarMascotaScreen(
                navController = navController,
                mascota = mascotaSeleccionada,
                onActualizarMascota = { mascotaActualizada ->

                    val indiceMascota = mascotas.indexOfFirst { mascota ->
                        mascota.id == mascotaActualizada.id
                    }

                    if (indiceMascota != -1) {
                        mascotas[indiceMascota] = mascotaActualizada
                    }
                }
            )
        }

        composable("detalleMascota/{mascotaId}") { backStackEntry ->

            val mascotaId = backStackEntry.arguments
                ?.getString("mascotaId")
                ?.toIntOrNull()

            val mascotaSeleccionada = mascotas.find { mascota ->
                mascota.id == mascotaId
            }

            DetalleMascotaScreen(
                navController = navController,
                mascota = mascotaSeleccionada,
                onEliminarMascota = { mascota ->
                    mascotas.remove(mascota)
                }
            )
        }

        composable("servicios/{mascotaId}") { backStackEntry ->

            val mascotaId = backStackEntry.arguments
                ?.getString("mascotaId")
                ?.toIntOrNull()

            val mascotaSeleccionada = mascotas.find { mascota ->
                mascota.id == mascotaId
            }

            ServiciosScreen(
                navController = navController,
                mascota = mascotaSeleccionada,
                onAgregarServicio = { nuevoServicio ->

                    val indiceMascota = mascotas.indexOfFirst { mascota ->
                        mascota.id == mascotaId
                    }

                    if (indiceMascota != -1) {
                        val mascotaActual = mascotas[indiceMascota]

                        val nuevoIdServicio =
                            (mascotaActual.historial.maxOfOrNull { servicio -> servicio.id } ?: 0) + 1

                        val servicioConId = nuevoServicio.copy(
                            id = nuevoIdServicio
                        )

                        mascotas[indiceMascota] = mascotaActual.copy(
                            historial = mascotaActual.historial + servicioConId
                        )
                    }
                }
            )
        }
    }
}