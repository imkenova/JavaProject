package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Optional;


@Controller
public class BlogController {

    @Autowired
    private BlogService service;

    @Autowired
    private PostRepository repo;

    @RequestMapping ("/blog")
    public String blogMain(Model model) {
        Iterable<Post> posts = service.listAll();
        model.addAttribute("posts", posts);
        return "blog-main";
    }

    @RequestMapping("/blog/new")
    public String showNewPostForm(Model model) {
        Post post = new Post();
        model.addAttribute("post", post);
        return "blog-add";
    }

    @RequestMapping(value = "/blog/save", method = RequestMethod.POST)
    public String savePost(@ModelAttribute("post") Post post) {
        service.save(post);
        return "redirect:/blog";
    }


    @RequestMapping("/blog/{id}")
    public String postDetails(@PathVariable(value="id") long id, Model model){
        if (!repo.existsById(id)){
            return "redirect:/blog";
        }
        Optional<Post> post = repo.findById(id);
        ArrayList<Post> res = new ArrayList<>();
        post.ifPresent(res::add);
        model.addAttribute("post", res);
        return "post_details";
    }

    @RequestMapping("/blog/{id}/edit")
    public ModelAndView showEditPostForm(@PathVariable(name = "id") Long id) {
        ModelAndView mav = new ModelAndView("post_edit");
        Post post = service.get(id);
        mav.addObject("post", post);
        return mav;
    }

    @RequestMapping("/blog/{id}/remove")
    public String deletePost(@PathVariable(name = "id") Long id) {
        service.delete(id);
        return "redirect:/blog";
    }



}
