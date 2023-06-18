package ro.nicolaemariusghergu.graphql.controller

import ro.nicolaemariusghergu.graphql.model.Animal
import ro.nicolaemariusghergu.graphql.service.AnimalResolver
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller

@Controller
class AnimalExpose(private var animalResolver: AnimalResolver) {

    @QueryMapping
    fun getAnimals(): List<Animal> = animalResolver.getAnimals()

    @QueryMapping
    fun getAnimal(@Argument id: String): Animal? = animalResolver.getAnimal(id)

    @MutationMapping
    fun createAnimal(@Argument name: String, @Argument race: String, @Argument type: String): Animal =
        animalResolver.createAnimal(
            name = name,
            race = race,
            type = type
        )

    @MutationMapping
    fun modifyAnimal(@Argument name: String, @Argument race: String): Animal =
        animalResolver.modifyAnimal(
            name = name,
            race = race
        )

    @MutationMapping
    fun deleteAnimal(@Argument name: String): List<Animal> = animalResolver.deleteAnimal(name)

}