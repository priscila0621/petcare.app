package ni.edu.uam.petcare.model

data class Mascota(
    val id: Int,
    val nombre: String,
    val edad: String,
    val tipo: String,
    val raza: String,
    val peso: String,
    val fotoUri: String? = null,
    val historial: List<RegistroServicio> = emptyList()
)

data class RegistroServicio(
    val id: Int,
    val tipoServicio: TipoServicio,
    val fecha: String,
    val estado: EstadoServicio = EstadoServicio.REALIZADO,
    val notas: String = ""
)

enum class TipoServicio(val etiqueta: String) {
    BANIO("Baño"),
    VACUNA("Vacuna"),
    CONSULTA("Consulta"),
    CORTE_PELO("Corte de pelo")
}

enum class EstadoServicio(val etiqueta: String) {
    PENDIENTE("Pendiente"),
    REALIZADO("Realizado"),
    CANCELADO("Cancelado")
}