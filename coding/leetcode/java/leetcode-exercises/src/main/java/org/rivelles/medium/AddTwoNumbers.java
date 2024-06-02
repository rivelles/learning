package org.rivelles.medium;

import org.rivelles.libs.ListNode;

import static java.util.Optional.ofNullable;

public class AddTwoNumbers {
    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        if (l1 == null && l2 == null) return null;
        if (l1 == null) return l2;
        if (l2 == null) return l1;

        ListNode linkedList = new ListNode();
        ListNode firstElement = linkedList;
        var accumulated = false;

        while (l1 != null || l2 != null) {
            var resultValue = sum(l1, l2);
            if (accumulated) resultValue++;
            accumulated = resultValue >= 10;
            if (accumulated) resultValue = resultValue-10;

            linkedList.val = resultValue;

            if (l1 != null) l1 = l1.next;
            if (l2 != null) l2 = l2.next;
            if (l1 != null || l2 != null) {
                linkedList.next = new ListNode();
                linkedList = linkedList.next;
            }
        }
        if (accumulated) {
            linkedList.next = new ListNode(1);
        }
        return firstElement;
    }

    private static int sum(ListNode l1, ListNode l2) {
        return ofNullable(l1).map(listNode -> listNode.val).orElse(0) +
            ofNullable(l2).map(listNode -> listNode.val).orElse(0);
    }
}
