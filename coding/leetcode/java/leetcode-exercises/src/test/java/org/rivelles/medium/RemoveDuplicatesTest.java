package org.rivelles.medium;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class RemoveDuplicatesTest {
    RemoveDuplicates underTest = new RemoveDuplicates();
    @Test
    void test() {
        int[] input = new int[]{0,0,1,1,1,1,2,3,3};
        var result = underTest.removeDuplicates(input);

        assertThat(result).isEqualTo(7);
    }
}