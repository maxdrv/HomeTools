package apps.cards.parser

import apps.cards.CardContent

class FlashCardStyleParser: CardParser {

    override fun parse(text: String): List<CardContent> {
        val content = mutableListOf<CardContent>()

        val lines = text.lines()
        if (lines.size < 3) {
            return listOf()
        }
        var question: String
        var separator: String
        var answer: String

        var i = 0
        val lastIdx = lines.size - 2
        while (i < lastIdx) {
            question = lines[i].trim()
            separator = lines[i + 1].trim()

            val multilineAnswer = mutableListOf<String>()
            var j = i
            while (j < lastIdx) {
                val currentLine = lines[j]
                if (currentLine.isNotBlank()) {
                    multilineAnswer.add(currentLine)
                } else {
                    break
                }
                j++
            }

            answer = multilineAnswer.joinToString(separator = "\n")

            if (question.isNotBlank() && separator == "?" && answer.isNotBlank()) {
                content.add(CardContent(question, answer))
            }

            i++
        }

        return content
    }

    private fun lookupMultiline() {

    }
}