package ni.edu.uam.petcare.screens

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight

@Composable
fun ConfirmarSalidaDialog(
    titulo: String = "Cancelar registro",
    mensaje: String = "Si sales sin guardar, los datos ingresados se perderán. ¿Deseas cancelar?",
    textoConfirmar: String = "Sí, cancelar",
    textoCancelar: String = "Seguir editando",
    onConfirmarSalida: () -> Unit,
    onContinuarEditando: () -> Unit
) {
    AlertDialog(
        onDismissRequest = {
            onContinuarEditando()
        },
        title = {
            Text(
                text = titulo,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        },
        text = {
            Text(
                text = mensaje,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmarSalida()
                }
            ) {
                Text(
                    text = textoConfirmar,
                    color = MaterialTheme.colorScheme.error,
                    fontWeight = FontWeight.SemiBold
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onContinuarEditando()
                }
            ) {
                Text(
                    text = textoCancelar,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.surface,
        titleContentColor = MaterialTheme.colorScheme.onSurface,
        textContentColor = MaterialTheme.colorScheme.onSurfaceVariant
    )
}