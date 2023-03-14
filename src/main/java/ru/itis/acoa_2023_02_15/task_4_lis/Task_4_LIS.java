package ru.itis.acoa_2023_02_15.task_4_lis;

import java.util.Scanner;
import java.util.Stack;

public class Task_4_LIS {

    private static final Scanner scanner = new Scanner(System.in);
    private static int[] ints;
    private static int[] lasts;
    private static int[] pasts;

    public static void main(String[] args) {
        init();
        process();
    }

    private static void init() {
        System.out.println("Введите числовую последовательность:");
        String[] input = scanner.nextLine().split(" ");
        ints = new int[input.length];
        for (int i = 0; i < ints.length; i++) {
            ints[i] = Integer.parseInt(input[i]);
        }
        lasts = new int[input.length]; // Длина НВП
        pasts = new int[input.length];
    }

    private static void process() {
        for (int i = 0; i < ints.length; i++) {
            lasts[i] = 1;
            pasts[i] = -1;
            for (int j = 0; j < i; j++) {
                if (ints[j] < ints[i]) {
                    if (1 + lasts[j] > lasts[i]) {
                        lasts[i] = 1 + lasts[j];
                        pasts[i] = j;
                    }
                }
            }
        }

        int length = lasts[0], position = 0;
        for (int i = 0; i < ints.length; ++i)
            if (lasts[i] > length) {
                length = lasts[i];
                position = i;
            }
        System.out.printf("Длина НВП: %s;\n", length);

        Stack<Integer> path = new Stack<>();
        while (position != -1) {
            path.push(position);
            position = pasts[position];
        }
        System.out.print("НВП: ");
        while (!path.isEmpty()) {
            System.out.print(ints[path.pop()] + " ");
        }
    }
}
