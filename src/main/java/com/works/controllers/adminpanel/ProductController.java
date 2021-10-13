package com.works.controllers.adminpanel;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/product")
public class ProductController {

    final String rvalue = "adminpanel/product/";
	final ProductCategoryRepository productCategoryRepository;
    final ProductRepository productRepository;
	
	public ProductController(ProductCategoryRepository productCategoryRepository, ProductRepository productRepository) {
        this.productCategoryRepository = productCategoryRepository;
        this.productRepository = productRepository;
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

    @GetMapping("/add")
    public String productAdd() {
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
