package apps

import kotlinx.serialization.Serializable

@Serializable
data class Configuration(val cards: CardsConfiguration)

@Serializable
data class CardsConfiguration(val lookupPaths: List<String>, val destPath: String)

val emptyConfig = Configuration(CardsConfiguration(listOf(), ""))