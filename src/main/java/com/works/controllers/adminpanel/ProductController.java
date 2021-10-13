package com.works.controllers.adminpanel;

import com.works.entities.Product;
import com.works.entities.categories.ProductCategory;
import com.works.entities.images.ProductImage;
import com.works.properties.ProductCategoryInterlayer;
import com.works.properties.ProductInterlayer;
import com.works.repositories._jpa.ProductCategoryRepository;
import com.works.repositories._jpa.ProductImageRepository;
import com.works.repositories._jpa.ProductRepository;
import com.works.utils.Util;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/admin/product")
public class ProductController {

    final String rvalue = "adminpanel/product/";

    final ProductCategoryRepository productCategoryRepository;
    final ProductRepository productRepository;
    final ProductImageRepository productImageRepository;

    public ProductController(ProductCategoryRepository productCategoryRepository, ProductRepository productRepository, ProductImageRepository productImageRepository) {
        this.productCategoryRepository = productCategoryRepository;
        this.productRepository = productRepository;
        this.productImageRepository = productImageRepository;
    }

    @GetMapping("")
    public String product(Model model) {
        model.addAttribute("productInterlayer", new ProductInterlayer());
        model.addAttribute("ls", productCategoryRepository.getAllMainCategory());
        return "adminpanel/product/productadd";
    }

    @GetMapping("/list")
    public String productList() {
        return rvalue + "productlist";
    }

    @Transactional
    @PostMapping("/add")
    public String productAdd(@Valid @ModelAttribute("productInterlayer") ProductInterlayer productInterlayer, BindingResult bindingResult, Model model, @RequestPart(value = "pr_image_file", required = false) MultipartFile pr_image_file) {
        Product product = new Product();
        if(!bindingResult.hasErrors()){
            List<ProductCategory> productCategoryList = new ArrayList<>();
            for (int i = 0; i < productInterlayer.getPr_categories().size(); i++){
                Optional<ProductCategory> optProductCategory = productCategoryRepository.findById(productInterlayer.getPr_categories().get(i));
                if(optProductCategory.isPresent()){
                    productCategoryList.add(optProductCategory.get());
                }
            }
            try{
                product.setPr_categories(productCategoryList);
                product.setCompany(null);
                product.setImages(null);

                product.setPr_name(productInterlayer.getPr_name());
                product.setPr_brief_description(productInterlayer.getPr_brief_description());
                product.setPr_description(productInterlayer.getPr_description());
                product.setPr_price(productInterlayer.getPr_price());
                product.setPr_type(productInterlayer.getPr_type());
                product.setPr_campaign(productInterlayer.getPr_campaign());
                product.setPr_campaign_name(productInterlayer.getPr_campaign_name());
                product.setPr_campaign_description(productInterlayer.getPr_campaign_description());
                product.setPr_address(productInterlayer.getPr_address());
                product.setPr_latitude(productInterlayer.getPr_latitude());
                product.setPr_longitude(productInterlayer.getPr_longitude());
                product = productRepository.save(product);

                // Urunun resimlerinin tutulacagi Klasör olusturulacak
                File theDir = new File(Util.UPLOAD_DIR + "products/" + product.getId());
                if (!theDir.exists()) {
                    theDir.mkdirs();
                }

                String fileName = StringUtils.cleanPath(pr_image_file.getOriginalFilename());
                String ext = "";
                try {//File kısmı validation'da kontrol edilmediği için resim yüklenmemesi durumu kontrolü
                    int length = fileName.lastIndexOf(".");
                    ext = fileName.substring(length, fileName.length());
                    String uui = UUID.randomUUID().toString();
                    fileName = uui + ext;

                    Path path = Paths.get(Util.UPLOAD_DIR + "products/" + product.getId() + "/" + fileName);
                    Files.copy(pr_image_file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                } catch (Exception e) {
                    //File yuklenmediyse, onceden ayarlanmis bir resim bu klasöre yüklenebilir.
                    //...
                }
                ProductImage productImage = new ProductImage();
                productImage.setProduct(product);
                productImage.setImage_url(fileName);
                productImageRepository.save(productImage);

            }catch(Exception ex){
                System.err.println("ProductAdd Error : " + ex);
                return rvalue + "productadd";
            }
            return "redirect:/admin/product";
        }else{
            System.err.println(Util.errors(bindingResult));
        }
        model.addAttribute("ls", productCategoryRepository.getAllMainCategory());
        return rvalue + "productadd";
    }

    @GetMapping("/category")
    public String productCategory(Model model){
        model.addAttribute("productCategoryInterlayer", new ProductCategoryInterlayer());
        model.addAttribute("ls", productCategoryRepository.getAllMainCategory());
        model.addAttribute("isError", false);
        model.addAttribute("isError2", false);
        return "adminpanel/product/prcategoryadd";
    }

    @PostMapping("/category/add")
    public String prCategoryAdd(@Valid @ModelAttribute("productCategoryInterlayer") ProductCategoryInterlayer productCategoryInterlayer, BindingResult bindingResult, Model model) {
        if(!bindingResult.hasErrors()) {
            ProductCategory productCategory = new ProductCategory();
            int selectedCategory = productCategoryInterlayer.getPr_category();
            if (selectedCategory == -1 || selectedCategory == 0) {
                // Secim yapilmamis - ana kategori eklenecek
                try {
                    productCategory.setProductCategories(null);
                    productCategory.setCompany(null);
                    productCategory.setPr_title(productCategoryInterlayer.getPr_title());
                    productCategoryRepository.save(productCategory);
                } catch (DataIntegrityViolationException ex) {
                    System.err.println("Aynı isimde kategori mevcut");
                    model.addAttribute("isError", true);
                    model.addAttribute("isError2", false);
                    model.addAttribute("ls",productCategoryRepository.getAllMainCategory());
                    return rvalue + "prcategoryadd";
                }
                return "redirect:/admin/product/category";
            } else {
                // Secim yapilmis - alt kategori secildiyse ekleme yapilmayacak
                Optional<ProductCategory> optProductCategory = productCategoryRepository.findById(selectedCategory);
                if(optProductCategory.isPresent()){
                    Boolean isControl = false;
                    try{
                        isControl = optProductCategory.get().getProductCategories().equals(null) ? true: false;
                    }catch(Exception ex){
                        // ana kategori secildiyse buraya gelecek
                        isControl = true;
                    }
                    if(isControl){
                        // ana kategori - isleme devam et
                        try {
                            productCategory.setCompany(null);
                            productCategory.setProductCategories(optProductCategory.get());
                            productCategory.setPr_title(productCategoryInterlayer.getPr_title());
                            productCategoryRepository.save(productCategory);
                        } catch (DataIntegrityViolationException ex) {
                            System.err.println("Aynı isimde kategori mevcut");
                            model.addAttribute("isError", true);
                            model.addAttribute("isError2", false);
                            model.addAttribute("ls",productCategoryRepository.getAllMainCategory());
                            return rvalue + "prcategoryadd";
                        }
                        return "redirect:/admin/product/category";
                    }else{
                        // alt kategori secilmis - ekleme yapilmasin
                        System.err.println("Alt kategoriye ekleme yapilamaz");
                        model.addAttribute("isError2", true);
                        model.addAttribute("isError", false);
                        model.addAttribute("ls", productCategoryRepository.getAllMainCategory());
                        return rvalue + "prcategoryadd";
                    }
                }else{
                    System.err.println("Kategori Mevcut Değil");
                }
            }
        }else{
            System.err.println(Util.errors(bindingResult));
        }
        model.addAttribute("ls", productCategoryRepository.getAllMainCategory());
        model.addAttribute("isError", false);
        model.addAttribute("isError2", false);
        return rvalue + "prcategoryadd";
    }

}
