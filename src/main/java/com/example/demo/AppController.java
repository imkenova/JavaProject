package com.example.demo;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AppController {

    @Autowired
    private GoodsService service;

    @RequestMapping("/")
    public String viewHomePage(Model model, @Param("keyword") String keyword) {
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
    public String saveGoods(@ModelAttribute("goods") Goods goods) {
        service.save(goods);
        return "redirect:/";
    }

    @RequestMapping("/edit/{id}")
    public ModelAndView showEditGoodsForm(@PathVariable(name = "id") Long id) {
        ModelAndView mav = new ModelAndView("edit_goods");
        Goods goods = service.get(id);
        mav.addObject("goods", goods);
        return mav;
    }

    @RequestMapping("/delete/{id}")
    public String deleteGoods(@PathVariable(name = "id") Long id) {
        service.delete(id);
        return "redirect:/";
    }

}
