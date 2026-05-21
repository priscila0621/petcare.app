package ni.edu.uam.petcare.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import ni.edu.uam.petcare.model.Pet
import ni.edu.uam.petcare.viewmodel.PetViewModel

@Composable
fun EditPetScreen(
    navController: NavController,
    petId: Int,
    petViewModel: PetViewModel = viewModel()
) {

    val pet = petViewModel.getPetById(petId)

    var name by remember { mutableStateOf(pet?.name ?: "") }
    var type by remember { mutableStateOf(pet?.type ?: "") }
    var breed by remember { mutableStateOf(pet?.breed ?: "") }
    var age by remember { mutableStateOf(pet?.age?.toString() ?: "") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Editar Mascota")
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),

            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            OutlinedTextField(
                value = name,
                onValueChange = {
                    name = it
                },
                label = {
                    Text("Nombre")
                },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = type,
                onValueChange = {
                    type = it
                },
                label = {
                    Text("Tipo")
                },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = breed,
                onValueChange = {
                    breed = it
                },
                label = {
                    Text("Raza")
                },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = age,
                onValueChange = {
                    age = it
                },
                label = {
                    Text("Edad")
                },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {

                    petViewModel.updatePet(
                        Pet(
                            id = petId,
                            name = name,
                            type = type,
                            breed = breed,
                            age = age.toIntOrNull() ?: 0
                        )
                    )

                    navController.popBackStack()
                },

                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Guardar Cambios")
            }
        }
    }
}