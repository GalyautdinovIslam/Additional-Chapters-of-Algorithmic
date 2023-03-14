package ru.itis.acoa_2023_02_15.task_2_glass_balls;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Task_2_Glass_Balls {

    private static final Scanner scanner = new Scanner(System.in);
    private static int n; // Количество этажей
    private static int k; // Количество стеклянных шаров
    private static final List<List<Integer>> data = new ArrayList<>();

    public static void main(String[] args) {
        initData();
        handleData();
        printTable();
        process();
    }

    private static void initData() {
        System.out.print("Введите 2 числа через пробел: количество этажей и количество шаров: ");
        String[] input;
        input = scanner.nextLine().split(" ");
        if (input.length != 2) {
            System.out.println("Некорректный ввод!");
            System.exit(1);
        }
        n = Integer.parseInt(input[0]);
        k = Integer.parseInt(input[1]);
        if (n + 1 <= Math.pow(2, k) && (int) Math.ceil(Math.log(n + 1) / Math.log(2)) < k) {
            k = (int) Math.ceil(Math.log(n + 1) / Math.log(2));
            System.out.printf("""
                    Количество шаров превышает минимальное количество шаров для бинарного поиска.
                    Лишние шары будут отброшены. Оставшееся количество шаров %s.
                    """, k);
        }
        // Инициализируем список data списками, где первым элементом лежат единицы
        // Эти единицы означают, что мы можем с единственной попытки узнать нужный этаж в одноэтажном здании
        for (int i = 0; i < k; i++) {
            List<Integer> list = new ArrayList<>();
            list.add(1);
            data.add(list);
        }
    }

    private static void handleData() {
        boolean flag = true;
        while (flag) {
            iterTime(); // Обрабатываем этажи для всех количеств шаров при добавлении ещё одной попытки
            flag = checkTime(); // Достигли ли мы нужного этажа?
        }
    }

    private static void iterTime() {
        // Берем список, для попыток при единственном шаре и добавляем увеличенное на единицу прошлое значение
        data.get(0).add(
                data.get(0).get(
                        data.get(0).size() - 1
                ) + 1
        );
        for (int i = 1; i < k; i++) {
            // Минус два, чтобы взять предпоследнее значение
            // Минус один, чтобы взять последнее значение, так как новое значение мы не положили
            int floor = 1
                    +
                    data.get(i - 1).get(
                            data.get(i - 1).size() - 2
                    )
                    +
                    data.get(i).get(
                            data.get(i).size() - 1
                    );
            // Добавляем последнее значение
            data.get(i).add(floor);
        }
    }

    private static boolean checkTime() {
        return n > data.get(k - 1).get(
                data.get(k - 1).size() - 1
        );
    }

    private static void printTable() {
        System.out.printf("Требуемое количество попыток: %s\n", data.get(0).get(data.get(0).size() - 1));
        int spaces = (int) (Math.floor(Math.max(Math.log10(Math.pow(2, k)), Math.log10(k)) + 1));
        for (int j = data.get(0).size() - 1; j >= 0; j--) {
            for (int i = 0; i < k; i++) {
                System.out.printf(String.format("%%%ss ", spaces), data.get(i).get(j));
            }
            System.out.println();
        }
    }

    private static void process() {
        int row = data.get(0).size() - 1; // количество попыток - 1
        int column = getStartProcessingColumn(); // количество шаров - 1
        int underground = 0; // этаж, который гарантированно сохраняет наш шар в целостности
        int broker = n + 1; // этаж, который гарантированно разбивает наш шар

        int[] iterationsData = iteration(row, column, underground, broker);
        underground = iterationsData[0];
        broker = iterationsData[1];

        finish(underground, broker);
    }

    private static int getStartProcessingColumn() {
        int column;
        for (column = 0; column < k; column++) {
            if (data.get(column).get(data.get(0).size() - 1) >= n) {
                break;
            }
        }
        System.out.printf("""
                Можно справиться за меньшее количество стеклянных шаров.
                Лишние шары будут отброшены. Оставшееся количество шаров %s.
                """, column + 1);
        return column;
    }

    private static int[] iteration(int row, int column, int underground, int broker) {
        int floor; // итерируемый этаж
        while (column != 0 && row != 0) {
            floor = data.get(column - 1).get(row - 1) + 1;
            System.out.printf("Попробуйте сбросить с этажа %s\n", Math.min(n, underground + floor));
            System.out.print("Шар разбился? ");
            String answer = scanner.nextLine();
            if (answer.equals("Да")) {
                broker = Math.min(n, underground + floor);
                row--;
                column--;
                if (broker - 1 == underground) {
                    break;
                }
            } else if (answer.equals("Нет")) {
                underground = Math.min(n, underground + floor);
                row--;
                if (underground >= n) {
                    underground = n;
                    break;
                }
            } else {
                System.out.println("Некорректный ответ. Пожалуйста, используйте \"Да\" или \"Нет\".");
            }
        }
        return new int[]{underground, broker};
    }

    private static void finish(int underground, int broker) {
        if (underground == n) {
            System.out.printf("%s этажей недостаточно, чтобы шарики разбивались.\n", n);
            System.exit(0);
        } else if (broker - 1 == underground) {
            finishWithBroker(broker);
        } else {
            while (broker - 1 != underground) {
                System.out.printf("Попробуйте сбросить с этажа %s\n", underground + 1);
                System.out.print("Шар разбился? ");
                String answer = scanner.nextLine();
                if (answer.equals("Да")) {
                    finishWithBroker(underground + 1);
                } else if (answer.equals("Нет")) {
                    underground++;
                } else {
                    System.out.println("Некорректный ответ. Пожалуйста, используйте \"Да\" или \"Нет\".");
                }
            }
            finishWithBroker(broker);
        }
    }

    private static void finishWithBroker(int broker) {
        System.out.printf("Шары начинают биться с этажа %s.\n", broker);
        System.exit(0);
    }
}