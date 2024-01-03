package apps.cards

import util.TDir
import kotlin.io.path.name

class ObsidianCardExtractor {

    private val cardPattern = "\n?\n"

    fun extractCards(dir: TDir): List<Card> {
        var cnt = 1;
        return dir.list()
            .map { file -> file to String(file.read()) }
            .filter { (_, content) -> content.contains(cardPattern) }
            .flatMap { (file, content) ->
                val topic = dir.path.name
                try {
                    parse(content)
                        .map { cardContent -> Card(topic, cnt++, cardContent.question, cardContent.answer) }
                } catch (ex: Exception) {
                    throw RuntimeException("error processing file ${file.path}", ex)
                }
            }
    }

    internal fun parse(content: String): List<CardContent> {
        val cardsAsString = content.split("\n\n").filter { it.isNotBlank() }
        return cardsAsString
            .map { stringToCard(it) }
            .map { CardContent(it.question, it.answer) }
    }

    private fun stringToCard(content: String): CardContent {
        val split = content.split(cardPattern)
        val question = split[0].trim()
        val answerCandidate = split[1]
        val noTrailingMeta = removeTrailingMeta(answerCandidate)
        return CardContent(question, noTrailingMeta.trim())
    }

    private fun removeTrailingMeta(answer: String): String {
        if (!answer.contains("<!--")) {
            return answer
        }
        val metaIndex = answer.indexOf("<!--")
        return answer.removeRange(metaIndex, answer.length)
    }
}
