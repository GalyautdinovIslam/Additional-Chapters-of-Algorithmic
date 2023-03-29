package ru.itis.acoa_2023_03_01.task_1_integral;

import java.util.Scanner;

public class Task_1_Integral {

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.print("Введите размерность интеграла: ");
        int N = scanner.nextInt();
        System.out.print("Введите количество испытаний: ");
        int t = scanner.nextInt();
        int all = 0;
        int success = 0;
        while (all++ < t) {
            double sum = 0.0;
            double first = Math.random();
            sum += first;
            for (int i = 1; i < N; i++) {
                sum += Math.random();
            }
            double check = Math.random();
            if ((first / sum) > check) success++;
        }
        System.out.println((double) success / all);
    }
}
