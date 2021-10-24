package com.works.business._controllers.adminpanel;

import com.works.entities.Product;
import com.works.entities.categories.ProductCategory;
import com.works.entities.images.ProductImage;
import com.works.models._elastic.ProductCategoryElastic;
import com.works.models._elastic.ProductElastic;
import com.works.models._redis.ProductCategorySession;
import com.works.models._redis.ProductSession;
import com.works.properties.ProductCategoryInterlayer;
import com.works.properties.ProductInterlayer;
import com.works.repositories._elastic.ProductCategoryElasticRepository;
import com.works.repositories._elastic.ProductElasticRepository;
import com.works.repositories._jpa.ProductCategoryRepository;
import com.works.repositories._jpa.ProductImageRepository;
import com.works.repositories._jpa.ProductRepository;
import com.works.repositories._redis.ProductCategorySessionRepository;
import com.works.repositories._redis.ProductSessionRepository;
import com.works.utils.Util;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProductControllerBusiness {
    final String rvalue = "adminpanel/product/";

    final ProductCategoryRepository productCategoryRepository;
    final ProductRepository productRepository;
    final ProductImageRepository productImageRepository;

    final ProductElasticRepository productElasticRepository;
    final ProductSessionRepository productSessionRepository;
    final ProductCategoryElasticRepository productCategoryElasticRepository;
    final ProductCategorySessionRepository productCategorySessionRepository;

    public ProductControllerBusiness(ProductCategoryRepository productCategoryRepository, ProductRepository productRepository, ProductImageRepository productImageRepository, ProductElasticRepository productElasticRepository, ProductSessionRepository productSessionRepository, ProductCategoryElasticRepository productCategoryElasticRepository, ProductCategorySessionRepository productCategorySessionRepository) {
        this.productCategoryRepository = productCategoryRepository;
        this.productRepository = productRepository;
        this.productImageRepository = productImageRepository;
        this.productElasticRepository = productElasticRepository;
        this.productSessionRepository = productSessionRepository;
        this.productCategoryElasticRepository = productCategoryElasticRepository;
        this.productCategorySessionRepository = productCategorySessionRepository;
    }

    public String product(Model model) {
        model.addAttribute("productInterlayer", new ProductInterlayer());
        model.addAttribute("ls", productCategoryRepository.getAllMainCategory());
        return rvalue + "productadd";
    }

    public String productList() {
        return rvalue + "productlist";
    }

    public String productAdd(ProductInterlayer productInterlayer, BindingResult bindingResult, Model model, MultipartFile[] pr_image_file) {
        Product product = new Product();
        if (!bindingResult.hasErrors()) {
            List<ProductCategory> productCategoryList = new ArrayList<>();
            for (int i = 0; i < productInterlayer.getPr_categories().size(); i++) {
                Optional<ProductCategory> optProductCategory = productCategoryRepository.findById(productInterlayer.getPr_categories().get(i));
                if (optProductCategory.isPresent()) {
                    productCategoryList.add(optProductCategory.get());
                }
            }
            try {
                product.setPr_categories(productCategoryList);
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
                String fileName = "";
                for (MultipartFile image_file : pr_image_file) {
                    fileName = StringUtils.cleanPath(image_file.getOriginalFilename());
                    String ext = "";
                    try {//File kısmı validation'da kontrol edilmediği için resim yüklenmemesi durumu kontrolü
                        int length = fileName.lastIndexOf(".");
                        ext = fileName.substring(length, fileName.length());
                        String uui = UUID.randomUUID().toString();
                        fileName = uui + ext;

                        Path path = Paths.get(Util.UPLOAD_DIR + "products/" + product.getId() + "/" + fileName);
                        Files.copy(image_file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                    } catch (Exception e) {
                        //File yuklenmediyse, onceden ayarlanmis bir resim bu klasöre yüklenebilir.
                        fileName = "emptyimage.png";
                        ProductImage productImage = new ProductImage();
                        productImage.setProduct(product);
                        productImage.setImage_url(fileName);
                        productImageRepository.save(productImage);
                    }
                    ProductImage productImage = new ProductImage();
                    productImage.setProduct(product);
                    productImage.setImage_url(fileName);
                    productImageRepository.save(productImage);
                }

                /*----Add Redis Database---- */
                ProductSession productSession = new ProductSession();
                productSession.setId(product.getId().toString());
                productSession.setPr_name(product.getPr_name());
                productSession.setPr_brief_description(product.getPr_brief_description());
                productSession.setPr_price(product.getPr_price().toString());
                productSession.setPr_type(product.getPr_type() == 1 ? "Satılık" : "Kiralık");
                productSession.setPr_image(fileName);
                productSessionRepository.save(productSession);

                /*----Add Elasticsearch Database---- */
                ProductElastic productElastic = new ProductElastic();
                productElastic.setId(product.getId().toString());
                productElastic.setPr_name(product.getPr_name());
                productElastic.setPr_brief_description(product.getPr_brief_description());
                productElastic.setPr_price(product.getPr_price());
                productElastic.setPr_type(product.getPr_type() == 1 ? "Satılık" : "Kiralık");
                productElastic.setPr_image(fileName);
                productElasticRepository.save(productElastic);


            } catch (Exception ex) {
                System.err.println("ProductAdd Error : " + ex);
                return rvalue + "productadd";
            }
            return "redirect:/admin/product";
        } else {
            System.err.println(Util.errors(bindingResult));
        }
        model.addAttribute("ls", productCategoryRepository.getAllMainCategory());
        return rvalue + "productadd";
    }

    List<String> updateCategoryList;

    public String productItem(String stIndex, Model model) {
        try {
            Integer index = Integer.parseInt(stIndex);
            Optional<Product> optionalProduct = productRepository.findById(index);
            updateCategoryList = new ArrayList<>();
            for (ProductCategory prCategory : optionalProduct.get().getPr_categories()) {
                updateCategoryList.add(prCategory.getId().toString());
            }

            ProductInterlayer productInterlayer = new ProductInterlayer();
            productInterlayer.setPr_name(optionalProduct.get().getPr_name());
            productInterlayer.setPr_brief_description(optionalProduct.get().getPr_brief_description());
            productInterlayer.setPr_description(optionalProduct.get().getPr_description());
            productInterlayer.setPr_price(optionalProduct.get().getPr_price());
            productInterlayer.setPr_type(optionalProduct.get().getPr_type());
            productInterlayer.setPr_campaign(optionalProduct.get().getPr_campaign());
            productInterlayer.setPr_campaign_name(optionalProduct.get().getPr_campaign_name());
            productInterlayer.setPr_campaign_description(optionalProduct.get().getPr_campaign_description());
            productInterlayer.setPr_address(optionalProduct.get().getPr_address());
            productInterlayer.setPr_latitude(optionalProduct.get().getPr_latitude());
            productInterlayer.setPr_longitude(optionalProduct.get().getPr_longitude());
            productInterlayer.setPr_categories(null);

            if (optionalProduct.isPresent()) {
                model.addAttribute("index", index);
                model.addAttribute("productInterlayer", productInterlayer);
                model.addAttribute("ls", productCategoryRepository.getAllMainCategory());
                return rvalue + "productupdate";
            } else {
                // ulasilmak istenen duyuru mevcut değil
                return "error/404";
            }
        } catch (Exception ex) {
            // url kısmında rakam girilecek yere string girilmis
            return "error/404";
        }
    }

    public List<String> getPaySearchList() {
        return updateCategoryList;
    }

    public String productUpdate(ProductInterlayer productInterlayer, BindingResult bindingResult, String stIndex, Model model) {
        // Kulanıcı formdaki id yi String yaparsa diye kontrol
        Integer index = 0;
        try {
            index = Integer.parseInt(stIndex);
        } catch (Exception ex) {
            return "error/404";
        }
        Optional<Product> optionalProduct = productRepository.findById(index);
        if (!bindingResult.hasErrors()) {
            // Kullanıcı formdaki id yi değiştirirse, o ürün var mı diye kontrol
            if (optionalProduct.isPresent()) {
                List<ProductCategory> productCategoryList = new ArrayList<>();
                for (int i = 0; i < productInterlayer.getPr_categories().size(); i++) {
                    Optional<ProductCategory> optProductCategory = productCategoryRepository.findById(productInterlayer.getPr_categories().get(i));
                    if (optProductCategory.isPresent()) {
                        productCategoryList.add(optProductCategory.get());
                    }
                }

                Product product = optionalProduct.get();
                product.setPr_categories(productCategoryList);
                // resmi burada degil, resimler kısmında düzenliyoruz.
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
                product = productRepository.saveAndFlush(product);

                /*----Add Redis Database---- */
                ProductSession productSession = productSessionRepository.findById(index.toString()).get();
                productSession.setPr_name(product.getPr_name());
                productSession.setPr_brief_description(product.getPr_brief_description());
                productSession.setPr_price(product.getPr_price().toString());
                productSession.setPr_type(product.getPr_type() == 1 ? "Satılık" : "Kiralık");
                productSessionRepository.deleteById(index.toString());
                productSessionRepository.save(productSession);

                /*----Add Elasticsearch Database---- */
                ProductElastic productElastic = productElasticRepository.findById(index.toString()).get();
                productElastic.setPr_name(product.getPr_name());
                productElastic.setPr_brief_description(product.getPr_brief_description());
                productElastic.setPr_price(product.getPr_price());
                productElastic.setPr_type(product.getPr_type() == 1 ? "Satılık" : "Kiralık");
                productElasticRepository.deleteById(index.toString());
                productElasticRepository.save(productElastic);

                return "redirect:/admin/product/list";
            } else {
                return rvalue + "productupdate";
            }
        } else {
            System.err.println(Util.errors(bindingResult));
            model.addAttribute("index", index);
        }
        model.addAttribute("ls", productCategoryRepository.getAllMainCategory());
        return rvalue + "productupdate";
    }

    //******************************************************************************
    Integer index = 0;

    public String productImages(String stIndex, Model model) {
        try {
            index = Integer.parseInt(stIndex);
            Optional<Product> optProduct = productRepository.findById(index);
            if (optProduct.isPresent()) {
                List<ProductImage> productImageList = productImageRepository.findByProduct_IdEquals(index);
                model.addAttribute("ls", productImageList);
                model.addAttribute("index", index);
            } else {
                System.err.println("Mudahale edilmis. Aranilan id de bir ürün yok");
                return "redirect:/admin/product/list";
            }
        } catch (Exception ex) {
            System.err.println("Mudahale edilmis. String ifade girilmis");
            return "redirect:/admin/product/list";
        }
        if (!errorMessage.equals("")) {
            model.addAttribute("errorMessage", errorMessage);
            errorMessage = "";
        }
        return rvalue + "productimage";
    }

    String errorMessage = "";

    public String productImageUpload(MultipartFile[] pr_image_file) {
        Optional<Product> optProduct = null;
        if (pr_image_file != null && pr_image_file.length > 0) {
            String fileName = "";
            optProduct = productRepository.findById(index);
            if (optProduct.isPresent()) {
                for (MultipartFile image_file : pr_image_file) {
                    fileName = StringUtils.cleanPath(image_file.getOriginalFilename());
                    String ext = "";
                    try {
                        int length = fileName.lastIndexOf(".");
                        ext = fileName.substring(length, fileName.length());
                        String uui = UUID.randomUUID().toString();
                        fileName = uui + ext;

                        Path path = Paths.get(Util.UPLOAD_DIR + "products/" + optProduct.get().getId() + "/" + fileName);
                        Files.copy(image_file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

                        ProductImage productImage = new ProductImage();
                        productImage.setProduct(optProduct.get());
                        productImage.setImage_url(fileName);
                        productImageRepository.save(productImage);

                    } catch (Exception e) {
                    }
                }
                // Rediste kayıtlı olan deger, emptyimage ise o zaman redis ve elastic update edilecek
                String imageFileName = productSessionRepository.findById(index.toString()).get().getPr_image();
                if (imageFileName.equals("emptyimage.png")) {
                    File f = new File(Util.UPLOAD_DIR + "products/" + index); // klasör yolu
                    File[] listOfFiles = f.listFiles(); // klasordeki tum dosyalar

                    // Klasorde resim kalmadiysa default olarak belirtilen resim atanacak
                    String newImageName = "emptyimage.png";
                    if (listOfFiles.length > 0) {
                        //klasordeki tum dosyaların isimleri alindi
                        List<String> imageNames = new ArrayList<>();
                        for (File f1 : listOfFiles) {
                            if (f1.isFile()) {
                                imageNames.add(f1.getName());
                            }
                        }
                        newImageName = imageNames.get(0);

                        /*----Update Redis Database---- */
                        ProductSession productSession = productSessionRepository.findById(index.toString()).get();
                        productSession.setPr_image(newImageName);
                        productSessionRepository.deleteById(index.toString());
                        productSessionRepository.save(productSession);

                        /*----Update Elasticsearch Database---- */
                        ProductElastic productElastic = productElasticRepository.findById(index.toString()).get();
                        productElastic.setPr_image(newImageName);
                        productElasticRepository.deleteById(index.toString());
                        productElasticRepository.save(productElastic);
                    }

                }
            }
        } else {
            errorMessage = "Lütfen resim seçiniz!";
        }
        return "redirect:/admin/product/images/" + index;
    }

    public String deleteImage(String stIndex) {
        try {
            Integer prImageIndex = Integer.parseInt(stIndex);
            Optional<ProductImage> optionalProductImage = productImageRepository.findById(prImageIndex);
            if (optionalProductImage.isPresent()) {
                productImageRepository.deleteById(prImageIndex);
                // file delete
                String deleteFilePath = optionalProductImage.get().getImage_url();
                File file = new File(Util.UPLOAD_DIR + "products/" + index + "/" + deleteFilePath);
                file.delete();

                //************************************************************************************
                // Redis te kayitli olan resmin url'i dosya içinde mevcut mu
                String redisImagePath = productSessionRepository.findById(index.toString()).get().getPr_image();
                file = new File(Util.UPLOAD_DIR + "products/" + index + "/" + redisImagePath);
                boolean exists = file.exists();

                // Silinen deger redis te kayitli olan veri ise veya hiç veri kalmadiysa update yapilacak degilse ellenmeyecek
                if (!exists) {
                    // Redis ve Elasticsearch Update yapilacak
                    File f = new File(Util.UPLOAD_DIR + "products/" + index); // klasör yolu
                    File[] listOfFiles = f.listFiles(); // klasordeki tum dosyalar

                    // Klasorde resim kalmadiysa default olarak belirtilen resim atanacak
                    String newImageName = "emptyimage.png";
                    if (listOfFiles.length > 0) {
                        //klasordeki tum dosyaların isimleri alindi
                        List<String> imageNames = new ArrayList<>();
                        for (File f1 : listOfFiles) {
                            if (f1.isFile()) {
                                imageNames.add(f1.getName());
                            }
                        }
                        newImageName = imageNames.get(0);
                    }

                    /*----Update Redis Database---- */
                    ProductSession productSession = productSessionRepository.findById(index.toString()).get();
                    productSession.setPr_image(newImageName);
                    productSessionRepository.deleteById(index.toString());
                    productSessionRepository.save(productSession);

                    /*----Update Elasticsearch Database---- */
                    ProductElastic productElastic = productElasticRepository.findById(index.toString()).get();
                    productElastic.setPr_image(newImageName);
                    productElasticRepository.deleteById(index.toString());
                    productElasticRepository.save(productElastic);
                }
            }
        } catch (Exception ex) {
            errorMessage = "Silme işlemi sırasında bir hata oluştu!";
        }
        return "redirect:/admin/product/images/" + index;
    }

    //***************************************************************************************
    public String productCategory(Model model) {
        model.addAttribute("productCategoryInterlayer", new ProductCategoryInterlayer());
        model.addAttribute("ls", productCategoryRepository.getAllMainCategory());
        model.addAttribute("isError", false);
        model.addAttribute("isError2", false);
        return rvalue + "prcategoryadd";
    }

    public String productCategoryList() {
        return rvalue + "prcategorylist";
    }

    public String prCategoryAdd(ProductCategoryInterlayer productCategoryInterlayer, BindingResult bindingResult, Model model) {
        if (!bindingResult.hasErrors()) {
            ProductCategorySession productCategorySession = new ProductCategorySession();
            ProductCategoryElastic productCategoryElastic = new ProductCategoryElastic();
            ProductCategory productCategory = new ProductCategory();

            int selectedCategory = productCategoryInterlayer.getPr_category();
            if (selectedCategory == -1 || selectedCategory == 0) {
                // Secim yapilmamis - ana kategori eklenecek
                try {
                    productCategory.setProductCategories(null);
                    productCategory.setPr_title(productCategoryInterlayer.getPr_title());
                    productCategory = productCategoryRepository.save(productCategory);

                    /*----------Add Redis Database-----------*/
                    productCategorySession.setId(productCategory.getId().toString());
                    productCategorySession.setPr_title(productCategory.getPr_title());
                    productCategorySession.setParent_id(null);
                    productCategorySessionRepository.save(productCategorySession);

                    /*--------Add Elasticsearch Database--------*/
                    productCategoryElastic.setId(productCategory.getId().toString());
                    productCategoryElastic.setPr_title(productCategory.getPr_title());
                    productCategoryElastic.setParent_id(null);
                    productCategoryElasticRepository.save(productCategoryElastic);


                } catch (DataIntegrityViolationException ex) {
                    System.err.println("Aynı isimde kategori mevcut");
                    model.addAttribute("isError", true);
                    model.addAttribute("isError2", false);
                    model.addAttribute("ls", productCategoryRepository.getAllMainCategory());
                    return rvalue + "prcategoryadd";
                }
                return "redirect:/admin/product/category";
            } else {
                // Secim yapilmis - alt kategori secildiyse ekleme yapilmayacak
                Optional<ProductCategory> optProductCategory = productCategoryRepository.findById(selectedCategory);
                if (optProductCategory.isPresent()) {
                    Boolean isControl = false;
                    try {
                        isControl = optProductCategory.get().getProductCategories().equals(null) ? true : false;
                    } catch (Exception ex) {
                        // ana kategori secildiyse buraya gelecek
                        isControl = true;
                    }
                    if (isControl) {
                        // ana kategori - alt kategori eklenecek
                        try {
                            productCategory.setProductCategories(optProductCategory.get());
                            productCategory.setPr_title(productCategoryInterlayer.getPr_title());
                            productCategory = productCategoryRepository.save(productCategory);

                            /*----------Add Redis Database-----------*/
                            productCategorySession.setId(productCategory.getId().toString());
                            productCategorySession.setPr_title(productCategory.getPr_title());
                            productCategorySession.setParent_id(productCategory.getProductCategories().getId().toString());
                            productCategorySessionRepository.save(productCategorySession);

                            /*--------Add Elasticsearch Database--------*/
                            productCategoryElastic.setId(productCategory.getId().toString());
                            productCategoryElastic.setPr_title(productCategory.getPr_title());
                            productCategoryElastic.setParent_id(productCategory.getProductCategories().getId().toString());
                            productCategoryElasticRepository.save(productCategoryElastic);

                        } catch (DataIntegrityViolationException ex) {
                            System.err.println("Aynı isimde kategori mevcut");
                            model.addAttribute("isError", true);
                            model.addAttribute("isError2", false);
                            model.addAttribute("ls", productCategoryRepository.getAllMainCategory());
                            return rvalue + "prcategoryadd";
                        }
                        return "redirect:/admin/product/category";
                    } else {
                        // alt kategori secilmis - ekleme yapilmasin
                        System.err.println("Alt kategoriye ekleme yapilamaz");
                        model.addAttribute("isError2", true);
                        model.addAttribute("isError", false);
                        model.addAttribute("ls", productCategoryRepository.getAllMainCategory());
                        return rvalue + "prcategoryadd";
                    }
                } else {
                    System.err.println("Kategori Mevcut Değil");
                }
            }
        } else {
            System.err.println(Util.errors(bindingResult));
        }
        model.addAttribute("ls", productCategoryRepository.getAllMainCategory());
        model.addAttribute("isError", false);
        model.addAttribute("isError2", false);
        return rvalue + "prcategoryadd";
    }

    public String productCategoryItem(String stIndex, Model model) {
        try {
            Integer index = Integer.parseInt(stIndex);
            Optional<ProductCategory> optionalProductCategory = productCategoryRepository.findById(index);
            if (optionalProductCategory.isPresent()) {
                model.addAttribute("index", index);
                model.addAttribute("productCategory", optionalProductCategory.get());
                model.addAttribute("isError", false);
                return rvalue + "prcategoryupdate";
            } else {
                // ulasilmak istenen kategori mevcut değil
                return "error/404";
            }
        } catch (Exception ex) {
            // url kısmında rakam girilecek yere string girilmis
            return "error/404";
        }
    }

    public String productCategoryUpdate(ProductCategory productCategory, BindingResult bindingResult, String stIndex, Model model) {
        // Kulanıcı formdaki id yi String yaparsa diye kontrol
        Integer index = 0;
        try {
            index = Integer.parseInt(stIndex);
        } catch (Exception ex) {
            return "error/404";
        }
        Optional<ProductCategory> optionalProductCategory = productCategoryRepository.findById(index);
        // Kulanıcı formdaki id yi değiştirirse, o kategori var mı diye kontrol
        if (!bindingResult.hasErrors()) {
            if (optionalProductCategory.isPresent()) {
                try {
                    productCategory.setId(optionalProductCategory.get().getId());
                    productCategory.setProductCategories(optionalProductCategory.get().getProductCategories());
                    productCategory = productCategoryRepository.saveAndFlush(productCategory);

                    /*----Update Redis Database---- */
                    ProductCategorySession productCategorySession = new ProductCategorySession();
                    productCategorySession.setId(productCategory.getId().toString());
                    productCategorySession.setPr_title(productCategory.getPr_title());
                    productCategorySession.setParent_id(productCategory.getProductCategories() == null ? null : productCategory.getProductCategories().getId().toString());
                    productCategorySessionRepository.deleteById(stIndex);
                    productCategorySessionRepository.save(productCategorySession);

                    /*----Update Elasticsearch Database---- */
                    ProductCategoryElastic productCategoryElastic = new ProductCategoryElastic();
                    productCategoryElastic.setId(productCategory.getId().toString());
                    productCategoryElastic.setPr_title(productCategory.getPr_title());
                    productCategoryElastic.setParent_id(productCategory.getProductCategories() == null ? null : productCategory.getProductCategories().getId().toString());
                    productCategoryElasticRepository.deleteById(stIndex);
                    productCategoryElasticRepository.save(productCategoryElastic);
                } catch (DataIntegrityViolationException ex) {
                    System.err.println("Aynı isimde kategori mevcut");
                    model.addAttribute("index", index);
                    model.addAttribute("isError", true);
                    model.addAttribute("productCategory", optionalProductCategory.get());
                    return rvalue + "prcategoryupdate";
                }
                return "redirect:/admin/product/category/list";
            } else {
                return rvalue + "prcategoryupdate";
            }
        } else {
            System.err.println(Util.errors(bindingResult));
            model.addAttribute("index", index);
        }
        model.addAttribute("isError", false);
        return rvalue + "prcategoryupdate";
    }

}
