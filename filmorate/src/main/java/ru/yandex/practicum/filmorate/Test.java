package ru.yandex.practicum.filmorate;

import java.util.List;

public class Test<T extends Number & Comparable<T>> {
    private Object[] value;

    public void set(int index, T element) {
        value[index] = element;
    }
}
