package com.example.demo;

import java.security.Principal;
import java.util.List;
import java.util.Objects;

import com.example.demo.User;
import com.example.demo.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AppController {

    @Autowired
    private GoodsService service;
    private UserService userService;
    public AppController(UserService userService) {
        this.userService = userService;
    }
    @RequestMapping("/")
    public String viewHomePage(Model model, @Param("keyword") String keyword, Principal principal, SecurityContextHolderAwareRequestWrapper request) {
        if (principal != null && principal.getName() != null) {
            User user = userService.findUserByEmail(principal.getName());
            if (request.isUserInRole("ROLE_ADMIN"))
            {
                model.addAttribute("admin", true);
            }
            model.addAttribute("username", user.getName());
        }
        List<Goods> listGoods = service.listAll(keyword);
        model.addAttribute("listGoods", listGoods);
        model.addAttribute("keyword", keyword);
        return "index";
    }

    @RequestMapping("/new")
    public String showNewGoodsForm(Model model) {
        Goods goods = new Goods();
        model.addAttribute("goods", goods);
        return "new_goods";
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String saveGoods(@ModelAttribute("goods") Goods goods, Principal principal) {
        User user = userService.findUserByEmail(principal.getName());
        goods.setUser_id(user.getId());
        service.save(goods);
        return "redirect:/";
    }

    @RequestMapping("/edit/{id}")
    public ModelAndView showEditGoodsForm(@PathVariable(name = "id") Long id, SecurityContextHolderAwareRequestWrapper request, Principal principal) {
        ModelAndView mav = new ModelAndView();
        Goods goods = service.get(id);
        mav.addObject("goods", goods);
        User user = userService.findUserByEmail(principal.getName());
        if (request.isUserInRole("ROLE_ADMIN") || Objects.equals(user.getId(), goods.getUser_id()))
        {
            mav.setViewName("edit_goods");
        }
        else {
            mav.setViewName("error403");
        }
        return mav;
    }

    @RequestMapping("/delete/{id}")
    public String deleteGoods(@PathVariable(name = "id") Long id , SecurityContextHolderAwareRequestWrapper request, Principal principal) {
        User user = userService.findUserByEmail(principal.getName());
        if (request.isUserInRole("ROLE_ADMIN") || Objects.equals(user.getId(), service.get(id).getUser_id()))
        {
            service.delete(id);
            return "redirect:/";
        }
        else {
            return "error403";
        }

    }

}
