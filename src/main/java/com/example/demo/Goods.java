package com.example.demo;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class Goods {
    private long id;
    private String name;
    private String content;
    private String dep_city;
    private LocalDate dep_date;
    private String arr_city;
    private LocalDate arr_date;

    protected Goods() {

    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDep_city() {
        return dep_city;
    }

    public void setDep_city(String dep_city) {
        this.dep_city = dep_city;
    }

    public LocalDate getDep_date() {
        return dep_date;
    }

    public void setDep_date(LocalDate dep_date) {
        this.dep_date = dep_date;
    }

    public String getArr_city() {
        return arr_city;
    }

    public void setArr_city(String arr_city) {
        this.arr_city = arr_city;
    }

    public LocalDate getArr_date() {
        return arr_date;
    }

    public void setArr_date(LocalDate arr_date) {
        this.arr_date = arr_date;
    }

    @Override
    public String toString() {
        return "Goods{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", content='" + content + '\'' +
                ", dep_city='" + dep_city + '\'' +
                ", dep_date=" + dep_date +
                ", arr_city='" + arr_city + '\'' +
                ", arr_date=" + arr_date +
                '}';
    }
}
