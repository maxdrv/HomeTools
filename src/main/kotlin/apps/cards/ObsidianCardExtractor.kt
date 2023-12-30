package apps.cards

import util.TDir
import util.TFile
import kotlin.io.path.name

class ObsidianCardExtractor {

    private val cardPattern = "\n?\n"

    private data class CardContent(val question: String, val answer: String)

    fun extractCards(dir: TDir): List<Card> {
        return dir.list()
            .map { file -> file to String(file.read()) }
            .filter { (_, content) -> content.contains(cardPattern) }
            .flatMap { (file, content) ->
                val group = createGroupName(dir, file)
                val topic = file.name()
                try {
                    parse(content)
                        .map { cardContent -> Card(group, topic, cardContent.question, cardContent.answer) }
                } catch (ex: Exception) {
                    throw RuntimeException("error processing file ${file.path}", ex)
                }
            }
    }

    private fun createGroupName(dir: TDir, file: TFile): String {
        val inodes = mutableListOf<String>()
        if (file.path.parent == null) {
            return ""
        }
        var curPath = file.path.parent
        while (curPath != dir.path) {
            inodes.add(curPath.name)
            curPath = curPath.parent ?: break
        }
        if (curPath.parent != null) {
            inodes.add(curPath.name)
        }
        return inodes.reversed().joinToString(" - ")
    }

    private fun parse(content: String): List<CardContent> {
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
