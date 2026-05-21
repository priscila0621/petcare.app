package ni.edu.uam.petcare.data

import ni.edu.uam.petcare.model.EstadoServicio
import ni.edu.uam.petcare.model.Mascota
import ni.edu.uam.petcare.model.RegistroServicio
import ni.edu.uam.petcare.model.TipoServicio

object MascotaData {

    fun obtenerMascotasIniciales(): List<Mascota> {
        return listOf(
            Mascota(
                id = 1,
                nombre = "Luna",
                edad = "3 años",
                tipo = "Perro",
                raza = "Golden Retriever",
                peso = "24 kg",
                fotoUri = null,
                historial = listOf(
                    RegistroServicio(
                        id = 1,
                        tipoServicio = TipoServicio.BANIO,
                        fecha = "20/05/2026",
                        estado = EstadoServicio.REALIZADO,
                        notas = "Baño completo y limpieza de orejas."
                    ),
                    RegistroServicio(
                        id = 2,
                        tipoServicio = TipoServicio.VACUNA,
                        fecha = "21/05/2026",
                        estado = EstadoServicio.PENDIENTE,
                        notas = "Vacuna anual pendiente."
                    )
                )
            ),
            Mascota(
                id = 2,
                nombre = "Michi",
                edad = "2 años",
                tipo = "Gato",
                raza = "Siamés",
                peso = "5 kg",
                fotoUri = null,
                historial = listOf(
                    RegistroServicio(
                        id = 3,
                        tipoServicio = TipoServicio.CONSULTA,
                        fecha = "18/05/2026",
                        estado = EstadoServicio.REALIZADO,
                        notas = "Consulta general. Estado saludable."
                    )
                )
            ),
            Mascota(
                id = 3,
                nombre = "Rocky",
                edad = "5 años",
                tipo = "Perro",
                raza = "Bulldog",
                peso = "18 kg",
                fotoUri = null,
                historial = listOf(
                    RegistroServicio(
                        id = 4,
                        tipoServicio = TipoServicio.CORTE_PELO,
                        fecha = "15/05/2026",
                        estado = EstadoServicio.CANCELADO,
                        notas = "El dueño canceló la cita."
                    )
                )
            )
        )
    }
}