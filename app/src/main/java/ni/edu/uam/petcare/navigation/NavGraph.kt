package ni.edu.uam.petcare.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ni.edu.uam.petcare.ui.screens.HomeScreen
import ni.edu.uam.petcare.ui.screens.PetsScreen
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
            PetsScreen()
        }
    }
}