package apps.cards

import util.ListUtil
import java.util.function.Supplier
import kotlin.test.Test
import kotlin.test.assertContentEquals

val emptyElementSupplier = Supplier { 0 }

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

    @Test
    fun doIt_1_0() {
        val input = listOf(1, 2, 3, 4, 5)
        val actual = ListUtil.listsOfSize(input, 2, emptyElementSupplier)
        val expected = listOf(listOf(1, 2), listOf(3, 4), listOf(5, 0))
        assertContentEquals(expected, actual)
    }

    @Test
    fun doIt_1_1() {
        val input = listOf(1, 2, 3)
        val actual = ListUtil.listsOfSize(input, 2, emptyElementSupplier)
        val expected = listOf(listOf(1, 2), listOf(3, 0))
        assertContentEquals(expected, actual)
    }

    @Test
    fun doIt_1_2() {
        val input = listOf(1, 2)
        val actual = ListUtil.listsOfSize(input, 2, emptyElementSupplier)
        val expected = listOf(listOf(1, 2))
        assertContentEquals(expected, actual)
    }

    @Test
    fun doIt_1_3() {
        val input = listOf(1)
        val actual = ListUtil.listsOfSize(input, 2, emptyElementSupplier)
        val expected = listOf(listOf(1, 0))
        assertContentEquals(expected, actual)
    }

    @Test
    fun doIt_1_4() {
        val input = listOf<Int>()
        val actual = ListUtil.listsOfSize(input, 2, emptyElementSupplier)
        val expected = listOf<List<Int>>()
        assertContentEquals(expected, actual)
    }

}