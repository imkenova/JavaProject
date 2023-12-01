package com.example.demo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BlogService {

    @Autowired
    private PostRepository repo;

    public List<Post> listAll() {
        return repo.findAll();
    }

    public void save(Post post) {
        repo.save(post);
    }

    public Post get(Long id) {
        return repo.findById(id).get();
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }
}
