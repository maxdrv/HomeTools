package apps

import apps.cards.CardUIType
import kotlinx.serialization.Serializable

@Serializable
data class Configuration(val cards: CardsConfiguration)

@Serializable
data class CardsConfiguration(val lookupPaths: List<String>, val destPath: String, val cardUIType: CardUIType)

val emptyConfig = Configuration(CardsConfiguration(listOf(), "", CardUIType.QUESTION))