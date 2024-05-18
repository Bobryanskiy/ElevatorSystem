package com.github.bobryanskiy.elevatorsystem;

import java.util.Random;

public class ParametersGeneratorThread implements Runnable {
    static int from, to;

    @Override
    public void run() {
        while (true) {
            Random random = new Random();
            int temp = random.nextInt(1, 11);
            if (temp <= 5)
                from = 1;
            else
                from = random.nextInt(2, ElevatorsThread.FLOOR_AMOUNT + 1);
            if (from == 1) {
                to = random.nextInt(2, ElevatorsThread.FLOOR_AMOUNT + 1);
            } else {
                temp = random.nextInt(1, 11);
                if (temp < 8) to = 1;
                else {
                    do {
                        to = random.nextInt(2, ElevatorsThread.FLOOR_AMOUNT + 1);
                    } while (to == from);
                }
            }
            System.out.println(from + " " + to);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
