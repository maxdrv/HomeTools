package util;

import java.util.ArrayList;
import java.util.List;

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

}
