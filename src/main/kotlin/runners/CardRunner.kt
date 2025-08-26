package runners

import apps.CardsConfiguration
import apps.Configuration
import apps.cards.CardUIType
import apps.cards.CardsApplication
import apps.cards.parser.ParserType

fun main() {
    val paths = listOf(
        "/Users/derevnin-ma/Obsidian/ObsidianVault/languages/English/cards/Elton Jonh",
    )
    val cardsConfig = CardsConfiguration(
        lookupPaths = paths,
        destPath = "/Users/derevnin-ma/Notes",
        cardUIType = CardUIType.TRANSLATION_V2,
        parser = ParserType.OLD,
    )
    val config = Configuration(cardsConfig)
    CardsApplication(config.cards).run()
}