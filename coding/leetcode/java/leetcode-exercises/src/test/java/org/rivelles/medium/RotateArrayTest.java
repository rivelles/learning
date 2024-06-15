package org.rivelles.medium;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class RotateArrayTest {
    RotateArray rotateArray = new RotateArray();
    @Test
    void test() {
        int[] input = new int[] {1,2,3,4,5,6,7};
        int k = 3;

        int[] expected = new int[] {5,6,7,1,2,3,4};

        rotateArray.rotate(input, k);

        assertThat(input).isEqualTo(expected);
    }

    @Test
    void test2() {
        int[] input = new int[] {-1,-100,3,99};
        int k = 2;

        int[] expected = new int[] {3,99,-1,-100};

        rotateArray.rotate(input, k);

        assertThat(input).isEqualTo(expected);
    }
}