package com.shujia.scalademo;


public class Student {
    private String name;
    private int age;
    private String clazz;

    public Student(String name, int age, String clazz) {
        this.name = name;
        this.age = age;
        this.clazz = clazz;
    }

    public Student() {
    }

    @Override
    public String toString() {
        return "Student{" +
                "name='" + name + '\'' +
                ", age='" + age + '\'' +
                ", clazz='" + clazz + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }
}
