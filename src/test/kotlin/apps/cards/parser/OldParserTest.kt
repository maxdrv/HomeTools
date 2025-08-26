package apps.cards.parser

import apps.cards.CardContent
import kotlin.test.Test
import kotlin.test.assertContentEquals

class OldParserTest {

    @Test
    fun `extract cards from obsidian md document`() {
        val content = """
            
            question1
            ?
            answer1
            
            question2
            ?
            answer2
            continues
            
            question3
            ?
            answer3
            <!--SR:!2024-01-02,5,250-->
            
        """.trimIndent()

        val parser = OldParser()
        val actual = parser.parse(content)

        val expected = listOf(
            CardContent("question1", "answer1"),
            CardContent("question2", "answer2\ncontinues"),
            CardContent("question3", "answer3")
        )
        assertContentEquals(expected, actual)
    }

}