package util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Supplier;

public class ListUtil {

    public static <T> List<List<T>> partition(List<T> input, int partitionSize) {
        List<List<T>> partitions = new ArrayList<>();
        if (input.size() <= partitionSize) {
            partitions.add(input);
            return partitions;
        }

        for (int i = 0; i < input.size(); i = i + partitionSize) {
            List<T> partition = new ArrayList<>();
            for (int j = 0; j < partitionSize; j++) {
                int next = i + j;
                if (next < input.size()) {
                    partition.add(input.get(next));
                }
            }
            partitions.add(partition);
        }

        return partitions;
    }

    public static <T> List<List<T>> listsOfSize(List<T> input, int size, Supplier<T> emptyElementSupplier) {
        List<List<T>> lists = new ArrayList<>();
        if (input.isEmpty()) {
            return new ArrayList<>();
        }

        Iterator<T> iterator = input.iterator();

        int cnt = 1;
        List<T> list = new ArrayList<>();
        while (iterator.hasNext()) {
            T element = iterator.next();
            list.add(element);

            if (cnt == size) {
                cnt = 1;
                lists.add(list);
                list = new ArrayList<>();
            } else {
                cnt++;
            }
        }
        if (cnt == 1) {
            return lists;
        }

        for (int i = cnt; i < size + 1; i++) {
            T emptyElement = emptyElementSupplier.get();
            list.add(emptyElement);
        }
        lists.add(list);

        return lists;
    }

}
