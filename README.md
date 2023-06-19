# Kotlin-SpringBoot-GraphQL

The usage of Kotlin in building GraphQL with Spring Boot offers developers a powerful and efficient approach to creating modern, robust, and scalable APIs. Kotlin, a statically typed programming language, combines concise syntax with strong type inference and interoperability with Java, making it an excellent choice for Spring Boot applications.

When it comes to implementing GraphQL, Kotlin provides several advantages. Firstly, Kotlin's null safety feature helps prevent common null pointer exceptions, reducing the likelihood of runtime errors and enhancing the overall reliability of the application. This is particularly crucial in GraphQL, where nullable fields are a common aspect of the schema.

Another benefit of using Kotlin for GraphQL development is its seamless integration with Spring Boot. Kotlin works flawlessly with the Spring ecosystem, leveraging the power of Spring Boot's auto-configuration and dependency injection capabilities. This integration simplifies the setup and configuration process, allowing developers to focus on writing concise and expressive code.

Kotlin's concise syntax and expressive nature contribute to the readability and maintainability of GraphQL APIs. With features like extension functions and smart casts, developers can write clean and concise code, reducing boilerplate and improving the overall developer experience. This leads to faster development cycles and easier collaboration within development teams.

Furthermore, Kotlin's functional programming capabilities align well with the principles of GraphQL. Kotlin provides robust support for functional programming constructs such as higher-order functions, lambda expressions, and immutable data structures. These features enable developers to write composable and declarative code when defining resolvers and manipulating data within the GraphQL schema.

In terms of performance, Kotlin performs on par with Java, ensuring that GraphQL APIs built with Kotlin and Spring Boot can handle high volumes of requests and provide optimal response times. Kotlin's ability to leverage existing Java libraries and frameworks also means that developers can utilize the rich ecosystem of tools and resources available for Spring Boot development.

# Let’s create a basic Spring Boot application with Kotlin:

Firstly, I highly recommend installing the GraphQL plugin in IntelliJ. This will make it easier to write GraphQL code. 😃

In this document, we will demonstrate how to use Kotlin and Spring Boot to create a basic CRUD application that stores and retrieves animal information. Our focus will be on understanding how to use GraphQL.

To create a Kotlin Spring Boot application, we can use [Spring Initializr](https://start.spring.io/). We only need to add the "Spring Web" and "Spring for GraphQL" dependencies.

In the resources folder, there will be a folder named "graphql". This is where we store our GraphQL schemas.

Compared to Java, it is easier to write code using Kotlin. We can use [data classes](https://kotlinlang.org/docs/data-classes.html) to create a DTO class named Animal.

```kotlin
data class Animal(val id: String, val name: String, val race: String, val type: String)
```

> The `AnimalResolver` service class is responsible for handling the business logic related to animals. It includes methods for retrieving a list of all animals, retrieving a single animal by its name, creating a new animal, modifying an existing animal, and deleting an animal:
>

```kotlin
@Service
class AnimalResolver {

    private val animals: MutableList<Animal> = mutableListOf()

    fun getAnimals(): List<Animal> = animals

    fun getAnimal(id: String): Animal? = animals.find { it.id == id }

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
```

> The `AnimalExpose` controller class is responsible for exposing and retrieving the data from the `AnimalResolver` service class. It contains methods for getting a list of all animals, getting a single animal by its ID, creating a new animal, modifying an existing animal, and deleting an animal. The methods use annotations such as `@QueryMapping` and `@MutationMapping` to specify the types of GraphQL queries and mutations that they handle. This controller is an essential component of the GraphQL API, as it provides the interface for clients to interact with the data.
>

```kotlin
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
```

<aside>
ℹ️ <b>A GraphQL schema</b> defines the types of data that can be queried and manipulated, along with the operations that can be performed on them. The schema consists of three main building blocks: <b>object types, queries, and mutations</b>.

**Object types** represent the types of data in the system, such as a User or a Product. Each object type has one or more fields that represent the properties of that type, such as a User's name or email address.

**Queries** define the operations that can be performed to retrieve data from the system. A query is a read-only operation that returns one or more object types.

**Mutations** define the operations that can be performed to modify data in the system. A mutation is a write operation that can create, update, or delete one or more object types.

The schema is defined using the **GraphQL Schema Definition Language (SDL)**, which is a simple syntax for defining the types, queries, and mutations in the system. The schema is then used by the server to validate and execute incoming queries and mutations.

</aside>

<aside>
💡 To use the schema, clients send queries and mutations to the server, which then executes them and returns the results. The GraphiQL UI can be used to test the schema and see the data that is returned by each query and mutation.

</aside>

```graphql
type Query {
    getAnimals: [Animal]
    getAnimal(id: ID!): Animal
}

type Mutation {
    createAnimal(name: String!, race: String!, type: String!): Animal
    modifyAnimal(name: String!, race: String!): Animal
    deleteAnimal(name: String!): [Animal]
}

type Animal {
    id: ID!
    name: String!
    race: String!
    type: String!
}

```

> In the example provided, the GraphQL schema defines the object type Animal and the queries and mutations that can be performed on it. The type Animal has four fields: id, name, race, and type. The queries include getAnimals, which returns a list of Animal objects, and getAnimal, which returns a single Animal object by its id. The mutations include createAnimal, which creates a new Animal object, modifyAnimal, which updates an existing Animal object, and deleteAnimal, which deletes an Animal object by its name.
>

Finally, add the following property to the **`application.properties`** file to enable the GraphiQL UI:

```xml
spring.graphql.graphiql.enabled=true
```

We can also change the GraphQL and GraphiQL endpoint names using the following properties:

- To configure the GraphQL endpoint path: `spring.graphql.path`
- To configure the GraphiQL UI endpoint path: `spring.graphql.graphiql.path`

<aside>
💡 We can access the GraphQL endpoint at <b>`/graphql`</b> and use GraphiQL to test our CRUD operations. The URL to access the UI is: http://localhost:8080/graphiql?path=/graphql

</aside>

In conclusion, every time we create an API, we need a schema to define all the objects, mutations, and queries. In this schema, we defined our object Animal and our methods createAnimal, modifyAnimal, and deleteAnimal. When working with arrays, we use "[Animal]" to tell GraphQL it is an array of objects of type Animal.

To test our endpoints, we can use the following example:

```graphql
mutation persist {
  createAnimal(name: "Fluffy", race: "Abyssinian", type: "Cat") {
    id
    name
    race
    type
  }
}

mutation persist2 {
  createAnimal(name: "Fluflu", race: "Alaskan", type: "Dog") {
    id
    name
    race
    type
  }
}

mutation persist3 {
  modifyAnimal(name: "Fluflu", race: "American Bulldog") {
    id
    name
    race
  }
}

query receive {
  getAnimals {
    id
    name
    race
  }
}
```

# But why GraphQL, why not REST? Maybe both sometimes?

Probably you should wonder: why I should learn GraphQL and spend time on this when we already have REST? 😕

There are numerous reasons to switch to GraphQL, and let's explore some real scenarios to illustrate its advantages. Imagine a situation where you have established a deadline with your client. While the task at hand may not be overly complex, it involves handling a significant amount of repetitive data, which requires rewriting code, methods, and consumes valuable time.

For instance, suppose you have a payment system that requires retrieving customer information such as name, accounts, cards, and vendors. Sometimes, you may need all of this information, while other times you might only need the customer's name and account details, and on other occasions, just the name and card information. In such cases, you would typically need to fetch data repeatedly and recreate a substantial amount of existing code (assuming you are not exposing all fields through a single endpoint, which can lead to complications).

However, with GraphQL, you can streamline this process significantly. By defining a schema that represents the available data fields, you can precisely request only the information you need for a particular scenario. This means you won't have to fetch unnecessary data or duplicate code. Instead, you write your GraphQL schema once, and it handles the dynamic data fetching for you.

This capability allows you to optimize the retrieval of customer information based on your specific requirements. By leveraging GraphQL's flexible querying capabilities, you can tailor your requests to retrieve the precise data fields needed at any given time. As a result, you save development time and effort by avoiding redundant code and minimizing the amount of data transferred between the client and the server.

In summary, with GraphQL, you have the power to define a concise and focused schema that aligns with your data requirements. By using GraphQL's querying capabilities, you can retrieve only the necessary information, eliminating repetitive code rewriting and ensuring optimal data fetching efficiency.

<aside>
💡 <b>Over-fetching and under-fetching are two common pitfalls.</b>

Over-fetching is fetching too much data, meaning there is data in the response you don't use.

Under-fetching is not having enough data with a call to an endpoint, forcing you to call a second endpoint.

In both cases, they are performance issues: you either use more bandwidth than ideal, or you are making more HTTP requests than ideal.

In a perfect world, these problems would never arise; you would have exactly the right endpoints to give exactly the right data to your products.

These problems often appear when you scale and iterate on your products. The data you use on your pages often change, and the cost to maintain a separate endpoint with exactly the right data for each component becomes too much.

So, you end up with a compromise between not having too many endpoints, and having the endpoints fit each component needs best. This will lead to over-fetching in some cases (the endpoint will provide more data than needed for one specific component), and under-fetching in some others (you will need to call a second endpoint).

</aside>

Okay, maybe it sounds great, but it's important to acknowledge that GraphQL has its limitations as well. While GraphQL offers many benefits, it's essential to be aware of potential challenges and limitations that may arise during its implementation. Read about them [here](https://www.ibm.com/docs/en/api-connect/10.0.1.x?topic=api-graphql-limitations).

# Why Kotlin?

Perhaps the initial reason was to catch your attention by exploring something new, not necessarily Java (and don't worry, I won't hold it against you – Java is capable of achieving the same outcomes 😆). Another motivation behind adopting Kotlin is personal preference and the desire to explore new technologies. This is the beauty of technology – it evolves rapidly, presenting different ways to approach tasks. If we set GraphQL aside for a moment, there are numerous other possibilities with Kotlin, such as [coroutines](https://kotlinlang.org/docs/coroutines-overview.html), which can be employed for [non-blocking application development](https://www.baeldung.com/kotlin/spring-boot-kotlin-coroutines), and even venturing into rocket science 🚀.