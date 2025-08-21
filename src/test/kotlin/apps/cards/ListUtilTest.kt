package apps.cards

import util.ListUtil
import kotlin.test.Test
import kotlin.test.assertContentEquals

class ListUtilTest {

    @Test
    fun doIt0() {
        val input = listOf(1, 2, 3, 4, 5)
        val actual = ListUtil.partition(input, 2)
        val expected = listOf(listOf(1, 2), listOf(3, 4), listOf(5))
        assertContentEquals(expected, actual)
    }

    @Test
    fun doIt1() {
        val input = listOf(1, 2, 3)
        val actual = ListUtil.partition(input, 2)
        val expected = listOf(listOf(1, 2), listOf(3))
        assertContentEquals(expected, actual)
    }

    @Test
    fun doIt2() {
        val input = listOf(1, 2)
        val actual = ListUtil.partition(input, 2)
        val expected = listOf(listOf(1, 2))
        assertContentEquals(expected, actual)
    }

    @Test
    fun doIt3() {
        val input = listOf(1)
        val actual = ListUtil.partition(input, 2)
        val expected = listOf(listOf(1))
        assertContentEquals(expected, actual)
    }

    @Test
    fun doIt4() {
        val input = listOf<Int>()
        val actual = ListUtil.partition(input, 2)
        val expected = listOf(listOf<Int>())
        assertContentEquals(expected, actual)
    }


}