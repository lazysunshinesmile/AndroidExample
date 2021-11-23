package com.sun.myexamples;

public class Bean {
    int id;
    String name;
    long score;

    @Override
    public String toString() {
        return "Bean{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", score=" + score +
                '}';
    }
}
