package ru.itis.acoa_2023_03_29.task_2_continuous_knapsack;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Task_2_Continuous_Knapsack {

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
        items.sort((item1, item2) -> {
            double value = ((double) item1.cost) / item1.amount - ((double) item2.cost) / item2.amount;
            if (Math.abs(value) < 1e-4) return 0;
            return value > 0 ? -1 : 1;
        });
        int cost = 0;
        for (int i = 0; i < N; i++) {
            if (S - items.get(i).amount > 0) {
                System.out.printf("Берём предмет [%s %s] в полном объёме.%n", items.get(i).amount, items.get(i).cost);
                cost += items.get(i).cost;
                S -= items.get(i).amount;
            } else {
                System.out.printf("Берём предмет [%s %s] в объёме %S.%n", items.get(i).amount, items.get(i).cost, S);
                cost += ((double) S) / items.get(i).amount * items.get(i).cost;
                break;
            }
        }
        System.out.printf("Итоговая цена %s.%n", cost);
    }
}
