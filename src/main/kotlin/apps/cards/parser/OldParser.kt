package apps.cards.parser

import apps.cards.CardContent

class OldParser: CardParser {

    private val cardPattern = "\n?\n"

    override fun parse(text: String): List<CardContent> {
        val cardsAsString = text.split("\n\n").filter { it.isNotBlank() }
        return cardsAsString
            .map { stringToCard(it) }
            .map { CardContent(it.question, it.answer) }
    }

    private fun stringToCard(content: String): CardContent {
        try {
            val split = content.split(cardPattern)
            val question = split[0].trim()
            val answerCandidate = split[1]
            val noTrailingMeta = removeTrailingMeta(answerCandidate)
            return CardContent(question, noTrailingMeta.trim())
        } catch (ex: Exception) {
            throw RuntimeException("error in $content", ex)
        }
    }

    private fun removeTrailingMeta(answer: String): String {
        if (!answer.contains("<!--")) {
            return answer
        }
        val metaIndex = answer.indexOf("<!--")
        return answer.removeRange(metaIndex, answer.length)
    }
}