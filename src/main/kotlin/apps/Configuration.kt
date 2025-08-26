package apps

import apps.cards.CardUIType
import apps.cards.parser.ParserType
import kotlinx.serialization.Serializable

@Serializable
data class Configuration(val cards: CardsConfiguration)

@Serializable
data class CardsConfiguration(val lookupPaths: List<String>, val destPath: String, val cardUIType: CardUIType, val parser: ParserType)

val emptyConfig = Configuration(CardsConfiguration(listOf(), "", CardUIType.QUESTION, ParserType.OLD))