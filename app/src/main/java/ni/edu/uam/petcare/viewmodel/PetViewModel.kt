package ni.edu.uam.petcare.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import ni.edu.uam.petcare.model.Pet

class PetViewModel : ViewModel() {

    private var nextId = 3

    var pets = mutableStateListOf(
        Pet(1, "Max", "Perro", "Labrador", 3),
        Pet(2, "Milo", "Gato", "Persa", 2)
    )
        private set

    fun addPet(
        name: String,
        type: String,
        breed: String,
        age: Int
    ) {

        pets.add(
            Pet(
                id = nextId++,
                name = name,
                type = type,
                breed = breed,
                age = age
            )
        )
    }

    fun deletePet(id: Int) {

        pets.removeIf {
            it.id == id
        }
    }

    fun updatePet(updatedPet: Pet) {

        val index = pets.indexOfFirst {
            it.id == updatedPet.id
        }

        if (index != -1) {
            pets[index] = updatedPet
        }
    }

    fun getPetById(id: Int): Pet? {

        return pets.find {
            it.id == id
        }
    }
}