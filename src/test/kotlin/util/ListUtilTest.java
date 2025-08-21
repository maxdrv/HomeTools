package util;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.junit.jupiter.api.Test;

class ListUtilTest {

    @Test
    void doItTest() {
        List<Integer> input = new ArrayList<>();
        input.add(1);
        input.add(2);
        input.add(3);

        List<List<Integer>> actual = ListUtil.partition(input, 2);

        List<Integer> list1 = new ArrayList<>();
        list1.add(1);
        list1.add(2);
        List<Integer> list2 = new ArrayList<>();
        list1.add(3);
        List<List<Integer>> expected = new ArrayList<>();
        expected.add(list1);
        expected.add(list2);

        boolean equals = Objects.equals(actual, expected);
        assert equals;
    }

}