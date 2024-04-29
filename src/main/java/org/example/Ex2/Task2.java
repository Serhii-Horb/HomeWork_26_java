package org.example.Ex2;

import java.util.concurrent.CountDownLatch;

/* 2*. Вы едете на экскурсии. Каждый человек, при входе в автобус, называет свою фамилию.
Экскурсовод ставит у себя в блокноте птичку и если количество людей по списку совпадает
автобус, уезжает на экскурсию. Сымитируйте данный процесс работы.
Какой синхронизатор с библиотеки concurrent Вы бы использовали для данного процесса? */
public class Task2 {
    public static void main(String[] args) {
        CountDownLatch countDownLatch = new CountDownLatch(5);

        String[] personNames = new String[]{"Вася", "Валера", "Анатолий", "Светлана", "Андрей"};

        for (int i = 0; i < personNames.length; i++) {
            new Thread(new Person(personNames[i], countDownLatch)).start();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        System.out.println("Все на своих местах! Автобус уехал на экскурсию");
    }
}

class Person implements Runnable {
    private final String personName;
    private final CountDownLatch countDownLatch;

    public Person(String personName, CountDownLatch countDownLatch) {
        this.personName = personName;
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void run() {
        System.out.println("Меня зовут " + personName+ "," + " я готов ехать на экскурсию!");

        countDownLatch.countDown();

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}