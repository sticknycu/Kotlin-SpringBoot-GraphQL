package ro.nicolaemarius.ghergu.graphql.service

import ro.nicolaemarius.ghergu.graphql.model.Animal
import org.springframework.stereotype.Service

@Service
class AnimalResolver {

    private val animals: MutableList<Animal> = mutableListOf()

    fun getAnimals(): List<Animal> {
        return animals
    }

    fun getAnimal(id: String): Animal? {
        return animals.find { it.id == id }
    }

    fun createAnimal(name: String, race: String, type: String): Animal {
        val animal = Animal(
            id = (animals.size + 1).toString(),
            name = name,
            race = race,
            type = type
        )

        animals.add(animal)
        return animal
    }

    fun modifyAnimal(name: String, race: String): Animal {
        val existingAnimal = animals.find { it.name == name }

        deleteAnimal(
            name = existingAnimal!!.name
        )

        return createAnimal(
            name = existingAnimal.name,
            race = race,
            type = existingAnimal.type
        )
    }

    fun deleteAnimal(name: String): List<Animal> {
        animals.removeIf { it.name == name }

        return animals
    }
}