package runners

import apps.CardsConfiguration
import apps.Configuration
import apps.cards.CardUIType
import apps.cards.CardsApplication

fun main() {
    val paths = listOf(
        "/Users/derevnin-ma/Obsidian/ObsidianVault/languages/English/cards",
    )
    val cardsConfig = CardsConfiguration(
        lookupPaths = paths,
        destPath = "/Users/derevnin-ma/Notes",
        cardUIType = CardUIType.TRANSLATION,
    )
    val config = Configuration(cardsConfig)
    CardsApplication(config.cards).run()
}