package ru.yandex.practicum.filmorate;

public class Main {
    public static void main(String[] args) {
        DaysOfWeek.MONDAY.myMethod();
        Main main = new Main();
//        main.sum();

        Integer a = (100);
        Integer b = (100);
        System.out.println(a == b);

        String s1 = "Hello";
        String s2 = "Hello";

        try {

        } catch (Error error) {

        }
    }

    public int sum(int... elements) {
        int result = 0;
        for (int i : elements) {
            result += i;
        }
        return result;
    }


//    String name;
//    private static int age;
//
//    {
//        name = "Kirill";
//        System.out.println("Hello");
//    }
//
//    // всегда один раз
//    static {
//        age = 20;
//        System.out.println("Static block");
//    }
//
//    public Main(String name, int age) {
//        this.name = name;
//        this.age = age;
//        System.out.println("Конструктор");
//    }
//
//    public static void main(String[] args) {
//        Main main = new Main("", 20);
//        Main main1 = new Main("Sasha", 30);
//    }

}
