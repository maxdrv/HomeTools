package apps.cards.parser

import apps.cards.CardContent
import kotlin.test.Test
import kotlin.test.assertContentEquals

class FlashCardStyleParserTest {

    @Test
    fun doIt_1() {
        val content = """
            q1
            ?
            a1
            
            q2
            ?
            a2
        """.trimIndent()

        val parser = FlashCardStyleParser()

        val actual = parser.parse(content)

        val expected = listOf(
            CardContent("q1", "a1"),
            CardContent("q2", "a2")
        )
        assertContentEquals(expected, actual)
    }

    @Test
    fun doIt_2_withExtraWhitespaces() {
        val content = """
            q1   
            ?   
            a1
               
            q2   
            ?  
            a2  
        """.trimIndent()

        val parser = FlashCardStyleParser()

        val actual = parser.parse(content)

        val expected = listOf(
            CardContent("q1", "a1"),
            CardContent("q2", "a2")
        )
        assertContentEquals(expected, actual)
    }

    @Test
    fun `skipping empty lines`() {
        val content = """
            
            
            
            q1
            ?
            a1
            
            
            
            q2
            ?
            a2
            
            
        """.trimIndent()

        val parser = FlashCardStyleParser()

        val actual = parser.parse(content)

        val expected = listOf(
            CardContent("q1", "a1"),
            CardContent("q2", "a2")
        )
        assertContentEquals(expected, actual)
    }

    @Test
    fun `multiline answer`() {
        val content = """
            q1
            ?
            a1
            
            q2
            ?
            a2-1
            a2-2
        """.trimIndent()

        val parser = FlashCardStyleParser()

        val actual = parser.parse(content)

        val expected = listOf(
            CardContent("q1", "a1"),
            CardContent("q2", "a2-1\na2-2")
        )
        assertContentEquals(expected, actual)
    }

    @Test
    fun `obsidian meta doesn't break anything`() {
        val content = """
            q1
            ?
            a1
            
            q2
            ?
            a2
            <!--SR:!2024-01-02,5,250-->
            
            q3
            ?
            a3
        """.trimIndent()

        val parser = FlashCardStyleParser()

        val actual = parser.parse(content)

        val expected = listOf(
            CardContent("q1", "a1"),
            CardContent("q2", "a2"),
            CardContent("q3", "a3"),
        )
        assertContentEquals(expected, actual)
    }
}