package com.works.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/product")
public class ProductController {

    @GetMapping("/list")
    public String productList(){
        return "productlist";
    }

    @GetMapping("/add")
    public String productAdd(){
        return "productadd";
    }

    @GetMapping("/category/add")
    public String prCategoryAdd(){
        return "prcategoryadd";
    }

}
