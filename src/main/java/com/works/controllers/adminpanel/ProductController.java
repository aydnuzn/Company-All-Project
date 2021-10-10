package com.works.controllers.adminpanel;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/product")
public class ProductController {

    final String rvalue = "adminpanel/product/";


    @GetMapping("/list")
    public String productList() {
        return rvalue + "productlist";
    }

    @GetMapping("/add")
    public String productAdd() {
        return rvalue + "productadd";
    }

    @GetMapping("/category/add")
    public String prCategoryAdd() {
        return rvalue + "prcategoryadd";
    }

}
