package org.rivelles.medium;

import org.junit.jupiter.api.Test;
import org.rivelles.libs.ListNode;

import static org.junit.jupiter.api.Assertions.*;

class AddTwoNumbersTest {
    AddTwoNumbers underTest = new AddTwoNumbers();

    @Test
    void addTwoNumbers_WhenInputsAreNull_shouldReturnNull() {
        ListNode input1 = null;
        ListNode input2 = null;
        ListNode expected = null;

        var response = underTest.addTwoNumbers(input1, input2);

        assertEquals(expected, response);
    }

    @Test
    void addTwoNumbers_whenOneOfTheInputsIsNull_shouldReturnOtherInput() {
        ListNode input1 = null;
        ListNode input2 = new ListNode(1);
        ListNode expected = new ListNode(1);

        var response = underTest.addTwoNumbers(input1, input2);

        assertEquals(expected, response);
    }

    @Test
    void addTwoNumbers_WhenInputsHaveSizeOne_shouldReturnCorrectResponse() {
        ListNode input1 = new ListNode(1);
        ListNode input2 = new ListNode(1);
        ListNode expected = new ListNode(2);

        var response = underTest.addTwoNumbers(input1, input2);

        assertEquals(expected, response);
    }

    @Test
    void addTwoNumbers_WhenInputsHaveDecimalSize_shouldReturnCorrectResponse() {
        ListNode input1 = new ListNode(1, new ListNode(2));
        ListNode input2 = new ListNode(1, new ListNode(2));
        ListNode expected = new ListNode(2, new ListNode(4));

        var response = underTest.addTwoNumbers(input1, input2);

        assertEquals(expected, response);
    }

    @Test
    void addTwoNumbers_WhenInputsHaveDecimalSizeAndOverflow_shouldReturnCorrectResponse() {
        ListNode input1 = new ListNode(8, new ListNode(2));
        ListNode input2 = new ListNode(8, new ListNode(2));
        ListNode expected = new ListNode(6, new ListNode(5));

        var response = underTest.addTwoNumbers(input1, input2);

        assertEquals(expected, response);
    }

    @Test
    void addTwoNumbers_WhenInputsHaveDifferentSizes_shouldReturnCorrectResponse() {
        ListNode input1 = new ListNode(1, new ListNode(2, new ListNode(3)));
        ListNode input2 = new ListNode(1);
        ListNode expected = new ListNode(2, new ListNode(2, new ListNode(3)));

        var response = underTest.addTwoNumbers(input1, input2);

        assertEquals(expected, response);
    }

    @Test
    void addTwoNumbers_WhenInputsHaveDifferentSizesAndOverflow_shouldReturnCorrectResponse() {
        ListNode input1 = new ListNode(8, new ListNode(2));
        ListNode input2 = new ListNode(8);
        ListNode expected = new ListNode(6, new ListNode(3));

        var response = underTest.addTwoNumbers(input1, input2);

        assertEquals(expected, response);
    }

    @Test
    void addTwoNumbers_WhenInputsOverflowInAllMembers_shouldReturnCorrectResponse() {
        ListNode input1 = new ListNode(8, new ListNode(8));
        ListNode input2 = new ListNode(8, new ListNode(8));
        ListNode expected = new ListNode(6, new ListNode(7, new ListNode(1)));

        var response = underTest.addTwoNumbers(input1, input2);

        assertEquals(expected, response);
    }

    @Test
    void addTwoNumbers_WhenInputsOverflowAndSizesAreDifferent_shouldReturnCorrectResponse() {
        ListNode input1 = new ListNode(8, new ListNode(8, new ListNode(9, new ListNode(9))));
        ListNode input2 = new ListNode(8, new ListNode(8));
        ListNode expected = new ListNode(6, new ListNode(7, new ListNode(0, new ListNode(0, new ListNode(1)))));

        var response = underTest.addTwoNumbers(input1, input2);

        assertEquals(expected, response);
    }
}