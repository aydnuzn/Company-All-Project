package com.works.controllers.adminpanel;


import com.works.business._controllers.adminpanel.ProductControllerBusiness;
import com.works.entities.categories.ProductCategory;
import com.works.properties.ProductCategoryInterlayer;
import com.works.properties.ProductInterlayer;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/admin/product")
public class ProductController {

    final ProductControllerBusiness business;

    public ProductController(ProductControllerBusiness business) {
        this.business = business;
    }

    @GetMapping("")
    public String product(Model model) {
        return business.product(model);
    }

    @GetMapping("/list")
    public String productList() {
        return business.productList();
    }

    @Transactional
    @PostMapping("/add")
    public String productAdd(@Valid @ModelAttribute("productInterlayer") ProductInterlayer productInterlayer, BindingResult bindingResult, Model model, @RequestPart(value = "pr_image_file", required = false) MultipartFile[] pr_image_file) {
        return business.productAdd(productInterlayer, bindingResult, model, pr_image_file);
    }

    @GetMapping("/{stIndex}")
    public String productItem(@PathVariable String stIndex, Model model) {
        return business.productItem(stIndex, model);
    }

    @GetMapping("/categorylist")
    @ResponseBody
    public List<String> getPaySearchList() {
        return business.getPaySearchList();
    }

    @Transactional
    @PostMapping("/update/{stIndex}")
    public String productUpdate(@Valid @ModelAttribute("productInterlayer") ProductInterlayer productInterlayer, BindingResult bindingResult, @PathVariable String stIndex, Model model) {
        return business.productUpdate(productInterlayer, bindingResult, stIndex, model);
    }


    // Image show
    @GetMapping("/images/{stIndex}")
    public String productImages(@PathVariable String stIndex, Model model) {
        return business.productImages(stIndex, model);
    }


    //Image Upload
    @PostMapping("/imageupload")
    public String productImageUpload(@RequestPart(value = "pr_image_file") MultipartFile[] pr_image_file) {
        return business.productImageUpload(pr_image_file);
    }

    // delete image item
    @GetMapping("/deleteimage/{stIndex}")
    public String deleteImage(@PathVariable String stIndex) {
        return business.deleteImage(stIndex);
    }
    //***************************************************************************************

    @GetMapping("/category")
    public String productCategory(Model model) {
        return business.productCategory(model);
    }

    @GetMapping("/category/list")
    public String productCategoryList() {
        return business.productCategoryList();
    }

    @PostMapping("/category/add")
    public String prCategoryAdd(@Valid @ModelAttribute("productCategoryInterlayer") ProductCategoryInterlayer productCategoryInterlayer, BindingResult bindingResult, Model model) {
        return business.prCategoryAdd(productCategoryInterlayer, bindingResult, model);
    }

    @GetMapping("/category/{stIndex}")
    public String productCategoryItem(@PathVariable String stIndex, Model model) {
        return business.productCategoryItem(stIndex, model);
    }

    @PostMapping("/category/update/{stIndex}")
    public String productCategoryUpdate(@Valid @ModelAttribute("productCategory") ProductCategory productCategory, BindingResult bindingResult, @PathVariable String stIndex, Model model) {
        return business.productCategoryUpdate(productCategory, bindingResult, stIndex, model);
    }

}
