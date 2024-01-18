package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

import java.lang.Math;


@Controller
public class BlogController {

    @Autowired
    private BlogService service;

    @Autowired
    private PostRepository repo;
    private UserService userService;
    public BlogController(UserService userService) {
        this.userService = userService;
    }
    @RequestMapping ("/blog")
    public String blogMain(Model model, Principal principal) {
        Iterable<Post> posts = service.listAll();
        if (principal != null && principal.getName() != null) {
            User user = userService.findUserByEmail(principal.getName());
            model.addAttribute("username", user.getName());
        }
        for (Post post:posts) {
            int spaces = 0;
            for (int i = 0; i < post.getContent().length(); i++) {
                if (post.getContent().charAt(i) == ' ') spaces ++;
                if (spaces == 30) {
                    post.setContent(post.getContent().substring(0, i) + " ...");
                    break;
                }
            }
        }
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
    public String savePost(@ModelAttribute("post") Post post, Principal principal) {
        User user = userService.findUserByEmail(principal.getName());
        post.setUser_id(user.getId());
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
    public ModelAndView showEditPostForm(@PathVariable(name = "id") Long id , SecurityContextHolderAwareRequestWrapper request, Principal principal) {
        ModelAndView mav = new ModelAndView();
        Post post = service.get(id);
        User user = userService.findUserByEmail(principal.getName());
        if (request.isUserInRole("ROLE_ADMIN") || Objects.equals(user.getId(), post.getUser_id()))
        {
            mav.setViewName("post_edit");
        }
        else {
            mav.setViewName("error403");
        }
        mav.addObject("post", post);
        return mav;
    }

    @RequestMapping("/blog/{id}/remove")
    public String deletePost(@PathVariable(name = "id") Long id , SecurityContextHolderAwareRequestWrapper request, Principal principal) {
        User user = userService.findUserByEmail(principal.getName());
        if (request.isUserInRole("ROLE_ADMIN") || Objects.equals(user.getId(), service.get(id).getUser_id()))
        {
            service.delete(id);
            return "redirect:/blog";
        }
        else {
            return "error403";
        }
    }



}

