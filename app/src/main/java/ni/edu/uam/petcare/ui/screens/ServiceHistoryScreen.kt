package ni.edu.uam.petcare.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ni.edu.uam.petcare.model.Service
import ni.edu.uam.petcare.ui.components.EmptyState
import ni.edu.uam.petcare.ui.components.ServiceCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServiceHistoryScreen() {

    val services = listOf(
        Service(
            title = "Vacunación",
            description = "Vacuna anual aplicada",
            date = "10/05/2026"
        ),
        Service(
            title = "Baño",
            description = "Baño completo realizado",
            date = "15/05/2026"
        )
    )

    if (services.isEmpty()) {

        EmptyState(
            message = "No hay servicios registrados"
        )

    } else {

        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text("Historial de Servicios")
                    }
                )
            }
        ) { padding ->

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),

                contentPadding = PaddingValues(16.dp),

                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                items(services) { service ->

                    ServiceCard(
                        title = service.title,
                        description = service.description,
                        date = service.date
                    )
                }
            }
        }
    }
}