package com.jeeva.sms;

import org.junit.Test;
import org.w3c.dom.Node;

import java.util.LinkedList;
import java.util.ListIterator;
import java.util.function.Predicate;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class NodeDeleteUnitTest {

    @Test
    public void addition_isCorrect() {
        LinkedList<Integer> linkedList = createValues();

        printValues(linkedList);

        removeNodeGreaterThanX(linkedList, 9);

        printValues(linkedList);
    }

    private void removeNodeGreaterThanX(LinkedList<Integer> linkedList, int x) {
        linkedList.removeIf(integer -> x < integer);
    }

    private LinkedList<Integer> createValues() {
        LinkedList<Integer> linkedList = new LinkedList<>();

        linkedList.add(10);
        linkedList.add(8);
        linkedList.add(4);
        linkedList.add(11);
        linkedList.add(9);

        return linkedList;
    }

    private void printValues(LinkedList<Integer> linkedList) {
        if(null != linkedList) {
            if(linkedList.size() > 0) {
                System.out.print(linkedList.peekFirst());
            }

            if(linkedList.size() > 1) {
                ListIterator<Integer> iterator = linkedList.listIterator(1);
                while (iterator.hasNext()) {
                    System.out.print("<=>" + iterator.next());
                }
            }
        }

        System.out.println();
    }
}