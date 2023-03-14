package ru.itis.acoa_2023_02_15.task_3_lcs;

import java.util.Objects;
import java.util.Scanner;

public class Task_3_LCS {

    private static class Pair {
        int f;
        int s;

        public Pair(int f, int s) {
            this.f = f;
            this.s = s;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Pair pair = (Pair) o;
            return f == pair.f && s == pair.s;
        }

        @Override
        public int hashCode() {
            return Objects.hash(f, s);
        }
    }

    private static final Scanner scanner = new Scanner(System.in);
    private static char[] first; // Первая последовательность
    private static char[] second; // Вторая последовательность
    private static int[][] lcs; // НОП для префиксов определенной длины
    private static Pair[][] pairs; // Пары индексов

    public static void main(String[] args) {
        init();
        process();
        printLCS(first.length, second.length);
    }

    private static void init() {
        System.out.println("Введите первую последовательность:");
        first = scanner.nextLine().trim().toCharArray();
        System.out.println("Введите вторую последовательность:");
        second = scanner.nextLine().trim().toCharArray();
        lcs = new int[first.length + 1][second.length + 1];
        pairs = new Pair[first.length + 1][second.length + 1];
    }

    private static void process() {
        for (int i = 1; i < first.length + 1; i++) {
            for (int j = 1; j < second.length + 1; j++) {
                // Если новые буквы совпадают, то длина НОП как без этих букв, но + 1
                if (first[i - 1] == second[j - 1]) {
                    lcs[i][j] = lcs[i - 1][j - 1] + 1;
                    pairs[i][j] = new Pair(i - 1, j - 1);
                } else { // Иначе выбираем наибольшую длину НОП с предыдущих этапов
                    if (lcs[i - 1][j] >= lcs[i][j - 1]) {
                        lcs[i][j] = lcs[i - 1][j];
                        pairs[i][j] = new Pair(i - 1, j);
                    } else {
                        lcs[i][j] = lcs[i][j - 1];
                        pairs[i][j] = new Pair(i, j - 1);
                    }
                }
            }
        }
    }

    private static void printLCS(int f, int s) {
        // Вышли за края слова, останавливаемся
        if (f == 0 || s == 0) {
            return;
        }

        // Если пришли не с соседей, то буква совпала, ее и напечатаем
        if (pairs[f][s].equals(new Pair(f - 1, s - 1))) {
            printLCS(f - 1, s - 1);
            System.out.print(first[f - 1]);
        } else { // Иначе выводим для соседа
            if (pairs[f][s].equals(new Pair(f - 1, s))) {
                printLCS(f - 1, s);
            } else {
                printLCS(f, s - 1);
            }
        }
    }
}
