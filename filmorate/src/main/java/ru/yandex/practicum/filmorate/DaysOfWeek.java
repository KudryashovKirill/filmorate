package ru.yandex.practicum.filmorate;

public enum DaysOfWeek {
    MONDAY {
        @Override
        public void myMethod() {
            System.out.println("Hello Monday");
        }
    },
    TUESDAY {
        @Override
        public void myMethod() {
            System.out.println("Hello TUESDAY");
        }
    },
    WEDNESDAY {
        @Override
        public void myMethod() {
            System.out.println("Hello WEDNESDAY");
        }
    };
    public abstract void myMethod();
}
