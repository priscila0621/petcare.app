package ni.edu.uam.petcare.navigation

sealed class Routes(val route: String) {
    object Splash : Routes("splash")
    object Home : Routes("home")
    object Pets : Routes("pets")
    object AddPet : Routes("add_pet")
    object Details : Routes("details")
}