package ru.itis.acoa_2023_03_01.task_3_finder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Task_3_Finder {

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("Введите последовательность чисел через пробел:");
        List<Integer> list = Arrays.stream(scanner.nextLine().split(" "))
                .map(Integer::parseInt)
                .toList();
        System.out.print("Введите порядковый номер требуемого числа (начиная с 1): ");
        int r = scanner.nextInt();
        System.out.println("Ответ: " + find(list, r));
    }

    private static int find(List<Integer> array, int need) {
        int randomValue = array.get((int) Math.floor(Math.random() * array.size()));
        List<Integer> less = new ArrayList<>();
        List<Integer> eq = new ArrayList<>();
        List<Integer> greater = new ArrayList<>();
        for (int i : array) {
            if (i < randomValue) less.add(i);
            else if (i == randomValue) eq.add(i);
            else greater.add(i);
        }
        if (less.size() >= need) return find(less, need);
        else if (less.size() + eq.size() >= need) return randomValue;
        else return find(greater, need - (less.size() + eq.size()));
    }
}
