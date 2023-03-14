package ru.itis.acoa_2023_02_15.task_1_vaccine;

import java.util.Scanner;
import java.util.Stack;

public class Task_1_Vaccine {

    private static class Pair {
        long time;
        Character laboratory;

        public Pair(long time, Character laboratory) {
            this.time = time;
            this.laboratory = laboratory;
        }
    }

    private static final Scanner scanner = new Scanner(System.in);
    private static int n; // Количество этапов производства
    private static int[] t_A; // Стоимость производства этапа в лаборатории А
    private static int[] t_B; // Стоимость производства этапа в лаборатории B
    private static int[] t_A2B; // Стоимость перевозки полуфабриката после этапа из лаборатории A в лабораторию B
    private static int[] t_B2A; // Стоимость перевозки полуфабриката после этапа из лаборатории B в лабораторию A

    private static Pair[] p_A; // Итоговое минимальное время после каждого этапа в лаборатории А
    private static Pair[] p_B; // Итоговое минимальное время после каждого этапа в лаборатории B

    public static void main(String[] args) {
        initData();
        handleData();
        printResult();
    }

    private static void initData() {
        System.out.print("Введите количество этапов производства вакцины: ");
        n = Integer.parseInt(scanner.nextLine());
        t_A = new int[n];
        t_B = new int[n];
        t_A2B = new int[n - 1];
        t_B2A = new int[n - 1];
        p_A = new Pair[n];
        p_B = new Pair[n];
        System.out.println("Введите стоимость каждого этапа производства вакцины в лаборатории А (через пробел):");
        initArray(t_A, n);
        System.out.println("Введите стоимость каждого этапа производства вакцины в лаборатории B (через пробел):");
        initArray(t_B, n);
        System.out.println("Введите стоимость перевозки после каждого этапа производства вакцины из лаборатории А в лабораторию B (через пробел):");
        initArray(t_A2B, n - 1);
        System.out.println("Введите стоимость перевозки после каждого этапа производства вакцины из лаборатории B в лабораторию A (через пробел):");
        initArray(t_B2A, n - 1);
        p_A[0] = new Pair(t_A[0], null);
        p_B[0] = new Pair(t_B[0], null);
    }

    private static void initArray(int[] array, int size) {
        String[] input;
        input = scanner.nextLine().split(" ");
        if (input.length != size) {
            System.out.printf("Неверное количество этапов. Требуется: %s. Обнаружено: %s.\n", size, input.length);
            System.exit(1);
        }
        for (int i = 0; i < size; i++) {
            array[i] = Integer.parseInt(input[i]);
        }
    }

    private static void handleData() {
        for (int i = 1; i < n; i++) {
            if (p_A[i - 1].time < p_B[i - 1].time + t_B2A[i - 1]) {
                p_A[i] = new Pair(p_A[i - 1].time + t_A[i], 'A');
            } else {
                p_A[i] = new Pair(p_B[i - 1].time + t_B2A[i - 1] + t_A[i], 'B');
            }

            if (p_B[i - 1].time < p_A[i - 1].time + t_A2B[i - 1]) {
                p_B[i] = new Pair(p_B[i - 1].time + t_B[i], 'B');
            } else {
                p_B[i] = new Pair(p_A[i - 1].time + t_A2B[i - 1] + t_B[i], 'A');
            }
        }
    }

    private static void printResult() {
        Stack<Character> path = new Stack<>();
        path.push(p_A[n - 1].time < p_B[n - 1].time ? 'A' : 'B');
        Pair iter_pair = p_A[n - 1].time < p_B[n - 1].time ? p_A[n - 1] : p_B[n - 1];
        for (int i = n - 2; i >= 0; i--) {
            path.push(iter_pair.laboratory);
            iter_pair = iter_pair.laboratory == 'A' ? p_A[i] : p_B[i];
        }
        System.out.printf("Результирующее время: %s.\n", Math.min(p_A[n - 1].time, p_B[n - 1].time));
        printPath(path);
    }

    private static void printPath(Stack<Character> path) {
        System.out.print("Итоговая траектория: ");
        while (!path.empty()) {
            System.out.print(path.pop());
        }
        System.out.println();
    }
}
