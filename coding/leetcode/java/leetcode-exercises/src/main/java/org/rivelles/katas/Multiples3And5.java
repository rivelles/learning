package org.rivelles.katas;

import java.util.stream.IntStream;

public class Multiples3And5 {
    public int solution(int number) {
        return IntStream.range(3, number).filter(this::multipleBy3Or5).sum();
    }

    private boolean multipleBy3Or5(int value) {
        return value % 3 == 0 || value % 5 == 0;
    }
}
