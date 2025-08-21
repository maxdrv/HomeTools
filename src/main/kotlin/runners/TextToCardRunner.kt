package runners

import util.TFile
import java.nio.file.Paths

fun main() {
    val path = "/Users/derevnin-ma/IdeaProjectsHome/HomeTools/src/main/resources/apps/text-to-card/input.txt"
    val file = TFile(Paths.get(path))

    val lines = file.lines()
    val result = lines.filter { it.isNotBlank() }.joinToString("\n\n") { mapToCard(it) }

    print(result)
}

private fun mapToCard(text: String): String {
    val tokens = text.split("-").map { it.trim() }
    val question = tokens[0]
    val answer = tokens[1]

    val card = """
        $question
        ?
        $answer
    """.trimIndent()

    return card
}