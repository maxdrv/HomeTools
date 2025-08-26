package apps.cards

import apps.cards.parser.CardParser
import util.TDir
import kotlin.io.path.name

class ObsidianCardExtractor(val parser: CardParser) {

    private val cardPattern = "\n?\n"

    fun extractCards(dir: TDir): List<Card> {
        return dir.list()
            .map { file -> file to String(file.read()) }
            .filter { (_, content) -> content.contains(cardPattern) }
            .flatMap { (file, content) ->
                val topic = dir.path.name
                try {
                    parser.parse(content)
                        .map { cardContent -> Card(topic, cardContent.question, cardContent.answer) }
                } catch (ex: Exception) {
                    throw RuntimeException("error processing file ${file.path}", ex)
                }
            }
    }

}
