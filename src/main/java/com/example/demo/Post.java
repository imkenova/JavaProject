package com.example.demo;

import jakarta.persistence.*;

@Entity
public class Post {
    private Long id;

    private String title;

    private String date_post;

    private String content;

    public Post() {

    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate_post() {
        return date_post;
    }

    public void setDate_post(String date_post) {
        this.date_post = date_post;
    }

    public Post(String title, String content, String date_post) {
        this.title = title;
        this.date_post = date_post;
        this.content = content;
    }
}
