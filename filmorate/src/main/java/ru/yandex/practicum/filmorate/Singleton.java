package ru.yandex.practicum.filmorate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Singleton {
    private static Singleton singleton;

    private Singleton() {
    }

    public static void main(String[] args) {
        List<Integer> list = new ArrayList<>();
        HashMap<Integer, Integer> ndqo = new HashMap<>();
    }

    public static synchronized Singleton getInstance() {
        if (singleton == null) {
            singleton = new Singleton();
        }
        return singleton;
    }


    public boolean isAnogram(String str1, String str2) {
        char[] arrayStr1 = str1.toCharArray();
        char[] arrayStr2 = str2.toCharArray();
        Arrays.sort(arrayStr1);
        Arrays.sort(arrayStr2);
        return Arrays.equals(arrayStr1, arrayStr2);
    }

    public static void myMethod1() {
        singleton.isAnogram("adhfisdhf", "fakhbfihwf");
        synchronized (singleton) {
            System.out.println("mpadfpkmf");
        }
        System.out.println("Первый метод работает");
    }

    public static void myMethod2() {
        singleton.isAnogram("adhfisdhf", "fakhbfihwf");
        synchronized (singleton) {
            System.out.println("mpadfpkmf");
        }
        System.out.println("Второй метод работает");
    }
}
