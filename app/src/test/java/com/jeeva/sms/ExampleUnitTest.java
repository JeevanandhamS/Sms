package com.jeeva.sms;

import org.junit.Test;
import org.reactivestreams.Subscription;

import io.reactivex.Observable;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        doIt();
    }

    private void doIt() {
        Observable someObservable = Observable
                .fromArray(1, 2, 3, 4, 5, 6, 7, 11)
                .filter(prime -> prime % 2 != 0)
                .onErrorResumeNext(Observable.empty())
                .map(number -> String.format("Hello %d", number))
                .doOnComplete(() -> System.out.println("complete"))
                .doOnTerminate(() -> System.out.println("terminate"))
                .doFinally(() -> System.out.println("finally"));

        someObservable.subscribe(
                System.out::println,
                System.out::println,
                () -> System.out.println("Completed!"));
    }
}