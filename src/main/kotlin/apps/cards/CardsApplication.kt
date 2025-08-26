package apps.cards

import apps.CardsConfiguration
import apps.cards.parser.CardParser
import apps.cards.parser.FlashCardStyleParser
import apps.cards.parser.OldParser
import apps.cards.parser.ParserType
import util.TDir
import java.math.BigInteger
import java.nio.file.Paths
import kotlin.io.path.name

class CardsApplication(private val config: CardsConfiguration) {

    fun run() {
        val dest = TDir(Paths.get(config.destPath))
        val lookupDirs = config.lookupPaths.map { dirPath: String -> TDir(Paths.get(dirPath)) }
        val cardExtractor = ObsidianCardExtractor(parser(config.parser))
        val publisher = OnDiskPdfCardPublisher(dest, CardsPdfPrinter(config.cardUIType))

        lookupDirs
            .map { dir -> dir to CardList(cardExtractor.extractCards(dir)) }
            .forEach { (dir, cards) -> publisher.publish(dir.path.name, orderCards(cards)) }
    }

    private fun parser(parserType: ParserType): CardParser {
        return when (parserType) {
            ParserType.FLASH_CARD_STYLE -> FlashCardStyleParser()
            ParserType.OLD -> OldParser()
        }
    }

    private fun orderCards(cards: CardList): CardList {
        val sorted = cards.content
            .sortedBy { extractLeadingNumber(it.question) }
            .toList()
        return CardList(sorted)
    }

    private fun extractLeadingNumber(topic: String): BigInteger {
        val matcher = "(^\\d+)".toPattern().matcher(topic.trimStart())
        if (matcher.find()) {
            return matcher.group().toBigInteger()
        }
        return BigInteger.ZERO;
    }

}