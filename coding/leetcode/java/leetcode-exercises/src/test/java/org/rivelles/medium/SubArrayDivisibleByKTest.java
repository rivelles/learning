package org.rivelles.medium;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SubArrayDivisibleByKTest {
    @Test
    void solution_whenArrayHasOnlyOneMultipleElement_shouldReturn1() {
        int[] nums = new int[] {5};
        int k = 5;

        int expected = 1;

        assertThat(SubArrayDivisibleByK.solution(nums, k)).isEqualTo(expected);
    }

    @Test
    void solution_whenArrayHasOnlyOneNotMultipleElement_shouldReturn0() {
        int[] nums = new int[] {6};
        int k = 5;

        int expected = 0;

        assertThat(SubArrayDivisibleByK.solution(nums, k)).isEqualTo(expected);
    }

    @Test
    void solution_whenArrayHasMultipleElements_shouldReturnSolution() {
        int[] nums = new int[] {4,5,0,-2,-3,1};
        int k = 5;

        int expected = 7;

        assertThat(SubArrayDivisibleByK.solution(nums, k)).isEqualTo(expected);
    }

    @Test
    void solution_failing() {
        int[] nums = new int[] {2,-2,2,-4};
        int k = 6;

        int expected = 2;

        assertThat(SubArrayDivisibleByK.solution(nums, k)).isEqualTo(expected);
    }

}