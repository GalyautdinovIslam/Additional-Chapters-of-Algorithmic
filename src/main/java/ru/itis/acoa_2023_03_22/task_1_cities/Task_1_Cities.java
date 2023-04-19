package ru.itis.acoa_2023_03_22.task_1_cities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import java.util.Stack;

public class Task_1_Cities {

    private static class City {
        String name;
        Character first;
        Character last;

        public City(String name) {
            this.name = name;
            this.first = name.charAt(0);
            this.last = name.charAt(name.length() - 1);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            City city = (City) o;
            return name.equals(city.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name);
        }
    }

    private static class Vertex {
        Character character;
        Map<Vertex, LinkedList<City>> from;
        Map<Vertex, LinkedList<City>> to;

        public Vertex(Character character) {
            this.character = character;
            this.from = new HashMap<>();
            this.to = new HashMap<>();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Vertex vertex = (Vertex) o;
            return character.equals(vertex.character);
        }

        @Override
        public int hashCode() {
            return Objects.hash(character);
        }
    }

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("Введите количество городов:");
        int N = Integer.parseInt(scanner.nextLine());
        System.out.println("Введите названия городов:");
        List<String> cities = new ArrayList<>();
        for (int i = 0; i < N; i++) {
            cities.add(scanner.nextLine().toUpperCase());
        }

        Map<Character, Vertex> vertexes = initVertexes(N, cities);
        try {
            Vertex[] check = checkEuler(vertexes);
            Stack<Character> characters;
            if (check[0] != null) {
                characters = startWith(check[0], vertexes);
            } else {
                characters = startWith(vertexes.values().stream().findFirst().get(), vertexes);
                //characters = unknownStart(vertexes, N, cities);
            }
            vertexes = initVertexes(N, cities);
            while (characters.size() != 1) {
                Character from = characters.pop();
                Character to = characters.peek();
                City city = vertexes.get(from).to.get(vertexes.get(to)).removeLast();
                System.out.println(city.name);
            }
        } catch (IllegalArgumentException iae) {
            System.err.println("Сыграть в города не получится :(");
            System.err.println(iae.getMessage());
            System.exit(0);
        }
    }

    private static Map<Character, Vertex> initVertexes(int N, List<String> cities) {
        Map<Character, Vertex> vertexes = new HashMap<>();
        for (int i = 0; i < N; i++) {
            String input = cities.get(i);
            City city = new City(input);
            if (!vertexes.containsKey(city.first)) {
                vertexes.put(city.first, new Vertex(city.first));
            }
            if (!vertexes.containsKey(city.last)) {
                vertexes.put(city.last, new Vertex(city.last));
            }
            if (!vertexes.get(city.first).to.containsKey(vertexes.get(city.last))) {
                vertexes.get(city.first).to.put(vertexes.get(city.last), new LinkedList<>());
            }
            vertexes.get(city.first).to.get(vertexes.get(city.last)).add(city);
            if (!vertexes.get(city.last).from.containsKey(vertexes.get(city.first))) {
                vertexes.get(city.last).from.put(vertexes.get(city.first), new LinkedList<>());
            }
            vertexes.get(city.last).from.get(vertexes.get(city.first)).add(city);
        }
        return vertexes;
    }

    private static Vertex[] checkEuler(Map<Character, Vertex> vertexes) {
        Vertex from = null;
        Vertex to = null;
        for (Vertex vertex : vertexes.values()) {
            if (vertex.to.size() > vertex.from.size()) {
                if (vertex.to.size() - vertex.from.size() == 1) {
                    if (from == null) {
                        from = vertex;
                    } else {
                        throw new IllegalArgumentException(
                                String.format("Придется начинать сразу с %s и c %s", from.character, vertex.character)
                        );
                    }
                } else {
                    throw new IllegalArgumentException(
                            String.format("На букву %s придется начинать несколько раз [%s]", vertex.character, vertex.to.size() - vertex.from.size())
                    );
                }
            } else if (vertex.to.size() < vertex.from.size()) {
                if (vertex.from.size() - vertex.to.size() == 1) {
                    if (to == null) {
                        to = vertex;
                    } else {
                        throw new IllegalArgumentException(
                                String.format("Придется заканчивать сразу на %s и на %s", to.character, vertex.character)
                        );
                    }
                } else {
                    throw new IllegalArgumentException(
                            String.format("На букву %s придется заканчивать несколько раз [%s]", vertex.character, vertex.from.size() - vertex.to.size())
                    );
                }
            }
        }
        return new Vertex[]{from, to};
    }

    private static Stack<Character> startWith(Vertex start, Map<Character, Vertex> vertexes) {
        Stack<Character> result = new Stack<>();
        Stack<Vertex> stack = new Stack<>();
        stack.push(start);
        while (!stack.isEmpty()) {
            Vertex vertex = stack.peek();
            if (vertex.to.size() == 0) {
                result.push(vertex.character);
                stack.pop();
            } else {
                City any = vertex.to.values().stream().findFirst().get().getLast();
                vertex.to.get(vertexes.get(any.last)).remove(any);
                if (vertex.to.get(vertexes.get(any.last)).size() == 0) {
                    vertex.to.remove(vertexes.get(any.last));
                }
                stack.push(vertexes.get(any.last));
            }
        }
        return result;
    }

    private static Stack<Character> unknownStart(Map<Character, Vertex> vertexes, int N, List<String> cities) {
        Stack<Character> result = new Stack<>();
        for (Vertex vertex : new HashMap<>(vertexes).values()) {
            Stack<Character> step = startWith(vertex, vertexes);
            if (step.size() > result.size()) {
                result = step;
            }
            vertexes = initVertexes(N, cities);
        }
        return result;
    }
}
