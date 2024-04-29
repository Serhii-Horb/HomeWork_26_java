package org.example.Ex1;
/*1. У вас в магазине распродажа. К вам набежало 10 000 клиентов и вы пытаетесь корректно всех обслужить с учетом того,
что одновременно у вас внутри магазина может находиться не более 10 покупателей(согласно карантинным нормам)
и время обслуживания одного покупателя занимает 30 секунд. Сымитируйте данный процесс работы и
подсчитайте за какой период времени вы сможете обслужить данный объем покупателей?
Отдельный покупатель - отдельный поток. Какой синхронизатор с библиотеки concurrent Вы бы использовали? */

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class Task1 {
    public static void main(String[] args) {
        Semaphore semaphore = new Semaphore(10, true);

        long startTime = System.nanoTime();
        for (int i = 0; i < 10000; i++) {
            Thread thread = new Thread(new Client(semaphore));
            thread.start();
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        long endTime = System.nanoTime();

        System.out.println("Мы потратили на обслуживание клиентов - " +
                TimeUnit.NANOSECONDS.toSeconds(endTime - startTime) + " секунд");
    }
}

class Client implements Runnable {
    private final Semaphore semaphore;

    public Client(Semaphore semaphore) {
        this.semaphore = semaphore;
    }

    @Override
    public void run() {
        try {
            semaphore.acquire();
            System.out.println("Клиент " + Thread.currentThread().getName() + " зашёл в магазин");
            Thread.sleep(3000);
            System.out.println("Клиент " + Thread.currentThread().getName() + " вышел из магазина");
            semaphore.release();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
