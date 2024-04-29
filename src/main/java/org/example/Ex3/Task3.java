package org.example.Ex3;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicInteger;

/* 3*. Для подъема на смотровую площадку работает лифт, в который одновременно может сесть не более 5 человек.
Создайте группу людей, каждый из которых будет иметь характеристику вес.
Создайте программу-симулятор работы лифта, при разном количестве людей в группе(можно вводить с клавиатуры).

4*. Для подъема на смотровую площадку работает лифт, в который одновременно может сесть не более 5 человек
или он может поднять не более 300 кг. груза. Создайте группу людей, каждый из которых будет иметь характеристику вес.
Создайте программу-симулятор работы лифта, при разном количестве людей в группе(можно вводить с клавиатуры).
Вес человека можно генерировать, но не более 150 кг. :)
*/
public class Task3 {
    public static void main(String[] args) throws InterruptedException {
        AtomicInteger liftCapacity = new AtomicInteger();

        CyclicBarrier barrier = new CyclicBarrier(5, new Elevator(liftCapacity));

        People[] peoples = new People[]{
                new People(liftCapacity, "Вася", 65, barrier),
                new People(liftCapacity, "Вова", 60, barrier),
                new People(liftCapacity, "Коля", 75, barrier),
                new People(liftCapacity, "Коля", 150, barrier),
                new People(liftCapacity, "Толя", 10, barrier),
                new People(liftCapacity, "Олег", 88, barrier),
                new People(liftCapacity, "Галя", 55, barrier),
                new People(liftCapacity, "Тамара", 18, barrier),
                new People(liftCapacity, "Саша", 109, barrier),
                new People(liftCapacity, "Даша", 20, barrier),
                new People(liftCapacity, "Марина", 34, barrier),
        };

        for (int i = 0; i < peoples.length; i++) {
            Thread thread = new Thread(peoples[i]);
            thread.start();
            Thread.sleep(500);
            if (peoples[i].equals(peoples.length - 1)) {
                return;
            }
        }
    }
}

class People implements Runnable {
    AtomicInteger liftCapacity;
    private final String name;
    private final int personWeight;
    private final CyclicBarrier barrier;

    public People(AtomicInteger liftCapacity, String name, int personWeight, CyclicBarrier barrier) {
        this.liftCapacity = liftCapacity;
        this.name = name;
        this.personWeight = personWeight;
        this.barrier = barrier;
    }

    @Override
    public void run() {
        System.out.println(name + " хочет подняться на смотровую площадку на лифте");
        if ((liftCapacity.get() + personWeight) > 300) {
            System.out.println("Ты слишком толстый и никуда не едешь");
        } else {
            System.out.println("Я поместился в лифт");
            liftCapacity.addAndGet(personWeight);
            try {
                barrier.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

class Elevator implements Runnable {
    AtomicInteger liftCapacity;

    public Elevator(AtomicInteger liftCapacity) {
        this.liftCapacity = liftCapacity;
    }

    @Override
    public void run() {
        System.out.println("Лифт полный, мест больше нет! Лифт уехал!");
        liftCapacity.set(0);
    }
}