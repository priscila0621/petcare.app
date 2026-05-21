package ni.edu.uam.petcare.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ni.edu.uam.petcare.model.Pet
import ni.edu.uam.petcare.navigation.Routes

@Composable
fun PetsScreen(navController: NavController) {

    var pets by remember {
        mutableStateOf(
            listOf(
                Pet(1, "Max", "Perro", "Labrador", 3),
                Pet(2, "Milo", "Gato", "Persa", 2)
            )
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Mascotas")
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(Routes.AddPet.route)
                }
            ) {
                Text("+")
            }
        }
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            items(pets) { pet ->

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            navController.navigate("details/${pet.id}")
                        },
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {

                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {

                        Text(
                            text = pet.name,
                            style = MaterialTheme.typography.titleMedium
                        )

                        Text(text = pet.type)
                        Text(text = pet.breed)
                    }
                }
            }
        }
    }
}