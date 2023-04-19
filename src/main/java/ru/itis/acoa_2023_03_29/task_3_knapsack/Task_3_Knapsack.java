package ru.itis.acoa_2023_03_29.task_3_knapsack;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;

public class Task_3_Knapsack {

    private static class Item {
        Integer amount;
        Integer cost;

        public Item(Integer amount, Integer cost) {
            this.amount = amount;
            this.cost = cost;
        }
    }

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.print("Введите количество предметов: ");
        int N = scanner.nextInt();
        System.out.print("Введите размер рюкзака: ");
        int S = scanner.nextInt();
        List<Item> items = new ArrayList<>();
        System.out.println("Введите размер и цену каждого предмета.");
        for (int i = 0; i < N; i++) {
            int amount = scanner.nextInt();
            int cost = scanner.nextInt();
            items.add(new Item(amount, cost));
        }

        int[][] array = new int[N][S];
        for (int i = 0; i < S; i++) {
            array[0][i] = 0;
        }
        for (int i = 0; i < N; i++) {
            array[i][0] = 0;
        }
        for (int i = 1; i < N; i++) {
            for (int j = 1; j < S; j++) {
                if (j >= items.get(i).amount) {
                    array[i][j] = Math.max(array[i - 1][j], array[i - 1][j - items.get(i).amount] + items.get(i).cost);
                } else {
                    array[i][j] = array[i - 1][j];
                }
            }
        }
        Stack<Integer> answer = new Stack<>();
        print(N - 1, S - 1, array, items, answer);
        int cost = 0;
        while (!answer.isEmpty()) {
            int i = answer.pop();
            Item item = items.get(i);
            cost += item.cost;
            System.out.printf("%s ", i + 1);
        }
        System.out.println();
        System.out.printf("Общий вес: %s%n", cost);
    }

    private static void print(int i, int j, int[][] array, List<Item> items, Stack<Integer> answer) {
        if (array[i][j] == 0) {
            return;
        }
        if (array[i - 1][j] == array[i][j]) {
            print(i - 1, j, array, items, answer);
        } else {
            print(i - 1, j - items.get(i).amount, array, items, answer);
            answer.push(i);
        }
    }
}
