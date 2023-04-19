package ru.itis.acoa_2023_03_22.task_2_trapezoids;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import java.util.Stack;

public class Task_2_Trapezoids {

    private static class Trapezoid {
        int index;
        int i1;
        int i2;
        int i3;
        int first;
        int last;

        public Trapezoid(int index, int i1, int i2, int i3) {
            this.index = index + 1;
            this.i1 = i1;
            this.i2 = i2;
            this.i3 = i3;
            this.first = i1;
            this.last = i2 - i3;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Trapezoid trapezoid = (Trapezoid) o;
            return i1 == trapezoid.i1 && i2 == trapezoid.i2 && i3 == trapezoid.i3;
        }

        @Override
        public int hashCode() {
            return Objects.hash(i1, i2, i3);
        }
    }

    private static class Vertex {
        int value;
        Map<Vertex, LinkedList<Trapezoid>> from;
        Map<Vertex, LinkedList<Trapezoid>> to;

        public Vertex(int value) {
            this.value = value;
            this.from = new HashMap<>();
            this.to = new HashMap<>();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Vertex vertex = (Vertex) o;
            return value == vertex.value;
        }

        @Override
        public int hashCode() {
            return Objects.hash(value);
        }
    }

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("Введите количество трапеций:");
        int N = Integer.parseInt(scanner.nextLine());
        System.out.println("Введите значения трапеций:");
        List<String> trapezoids = new ArrayList<>();
        for (int i = 0; i < N; i++) {
            trapezoids.add(scanner.nextLine());
        }

        Map<Integer, Vertex> vertexes = initVertexes(N, trapezoids);
        try {
            checkEuler(vertexes);
            Stack<Integer> integers = startWith(vertexes.get(0), vertexes);
            vertexes = initVertexes(N, trapezoids);
            while (integers.size() != 1) {
                Integer from = integers.pop();
                Integer to = integers.peek();
                Trapezoid trapezoid = vertexes.get(from).to.get(vertexes.get(to)).removeLast();
                System.out.printf("%s (%s %s %s)%n", trapezoid.index, trapezoid.i1, trapezoid.i2, trapezoid.i3);
            }
        } catch (IllegalArgumentException iae) {
            System.err.println("Без отходов не получится :(");
//            System.err.println(iae.getMessage());
            System.exit(0);
        }
    }

    private static Map<Integer, Vertex> initVertexes(int N, List<String> trapezoids) {
        Map<Integer, Vertex> vertexes = new HashMap<>();
        for (int i = 0; i < N; i++) {
            String[] input = trapezoids.get(i).split(" ");
            Trapezoid trapezoid = new Trapezoid(i, Integer.parseInt(input[0]), Integer.parseInt(input[1]), Integer.parseInt(input[2]));
            if (!vertexes.containsKey(trapezoid.first)) {
                vertexes.put(trapezoid.first, new Vertex(trapezoid.first));
            }
            if (!vertexes.containsKey(trapezoid.last)) {
                vertexes.put(trapezoid.last, new Vertex(trapezoid.last));
            }
            if (!vertexes.get(trapezoid.first).to.containsKey(vertexes.get(trapezoid.last))) {
                vertexes.get(trapezoid.first).to.put(vertexes.get(trapezoid.last), new LinkedList<>());
            }
            vertexes.get(trapezoid.first).to.get(vertexes.get(trapezoid.last)).add(trapezoid);
            if (!vertexes.get(trapezoid.last).from.containsKey(vertexes.get(trapezoid.first))) {
                vertexes.get(trapezoid.last).from.put(vertexes.get(trapezoid.first), new LinkedList<>());
            }
            vertexes.get(trapezoid.last).from.get(vertexes.get(trapezoid.first)).add(trapezoid);
        }
        return vertexes;
    }

    private static void checkEuler(Map<Integer, Vertex> vertexes) {
        for (Vertex vertex : vertexes.values()) {
            if (vertex.to.values().stream().mapToInt(LinkedList::size).sum()
                    > vertex.from.values().stream().mapToInt(LinkedList::size).sum()) {
                throw new IllegalArgumentException(
                        String.format("Придется начинать с %s", vertex.value)
                );
            } else if (vertex.to.values().stream().mapToInt(LinkedList::size).sum()
                    < vertex.from.values().stream().mapToInt(LinkedList::size).sum()) {
                throw new IllegalArgumentException(
                        String.format("Придется заканчивать на %s", vertex.value)
                );
            }
        }
    }

    private static Stack<Integer> startWith(Vertex start, Map<Integer, Vertex> vertexes) {
        Stack<Integer> result = new Stack<>();
        Stack<Vertex> stack = new Stack<>();
        stack.push(start);
        while (!stack.isEmpty()) {
            Vertex vertex = stack.peek();
            if (vertex.to.size() == 0) {
                if (result.size() == 0 && vertex.value != 0) {
                    throw new IllegalArgumentException(
                            String.format("Придется заканчивать на %s", vertex.value)
                    );
                }
                result.push(vertex.value);
                stack.pop();
            } else {
                Trapezoid any = vertex.to.values().stream().findFirst().get().getLast();
                vertex.to.get(vertexes.get(any.last)).remove(any);
                if (vertex.to.get(vertexes.get(any.last)).size() == 0) {
                    vertex.to.remove(vertexes.get(any.last));
                }
                stack.push(vertexes.get(any.last));
            }
        }
        if (result.peek() != 0) {
            throw new IllegalArgumentException(
                    String.format("Придется начинать с %s", result.peek())
            );
        }
        return result;
    }
}
