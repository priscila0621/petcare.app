package ni.edu.uam.petcare.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ni.edu.uam.petcare.ui.screens.AddPetScreen
import ni.edu.uam.petcare.ui.screens.EditPetScreen
import ni.edu.uam.petcare.ui.screens.HomeScreen
import ni.edu.uam.petcare.ui.screens.PetDetailScreen
import ni.edu.uam.petcare.ui.screens.PetsScreen
import ni.edu.uam.petcare.ui.screens.ServiceHistoryScreen
import ni.edu.uam.petcare.ui.screens.SplashScreen

@Composable
fun NavGraph() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.Splash.route
    ) {

        composable(Routes.Splash.route) {
            SplashScreen(navController)
        }

        composable(Routes.Home.route) {
            HomeScreen(navController)
        }

        composable(Routes.Pets.route) {
            PetsScreen(navController)
        }

        composable(Routes.AddPet.route) {
            AddPetScreen(navController)
        }

        composable(Routes.Details.route) { backStackEntry ->

            val petId =
                backStackEntry.arguments
                    ?.getString("petId")
                    ?.toIntOrNull() ?: 0

            PetDetailScreen(
                navController = navController,
                petId = petId
            )
        }

        composable("edit_pet/{petId}") { backStackEntry ->

            val petId =
                backStackEntry.arguments
                    ?.getString("petId")
                    ?.toIntOrNull() ?: 0

            EditPetScreen(
                navController = navController,
                petId = petId
            )
        }

        composable(Routes.Services.route) {
            ServiceHistoryScreen()
        }
    }
}