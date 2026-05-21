package ni.edu.uam.petcare.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import ni.edu.uam.petcare.viewmodel.PetViewModel

@Composable
fun PetDetailScreen(
    navController: NavController,
    petId: Int,
    petViewModel: PetViewModel = viewModel()
) {

    val pet = petViewModel.getPetById(petId)

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(text = pet?.name ?: "")
        Text(text = pet?.type ?: "")
        Text(text = pet?.breed ?: "")
        Text(text = "${pet?.age} años")

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                navController.navigate("edit_pet/${petId}")
            }
        ) {
            Text("Editar")
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = {

                pet?.let {
                    petViewModel.deletePet(it.id)
                }

                navController.popBackStack()
            }
        ) {
            Text("Eliminar")
        }
    }
}