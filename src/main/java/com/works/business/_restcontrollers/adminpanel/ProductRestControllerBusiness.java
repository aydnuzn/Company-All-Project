package com.works.business._restcontrollers.adminpanel;

import com.works.entities.Product;
import com.works.entities.projections.ProductsOfCategoryInfo;
import com.works.models._redis.ProductSession;
import com.works.repositories._elastic.ProductElasticRepository;
import com.works.repositories._jpa.ProductRepository;
import com.works.repositories._redis.ProductSessionRepository;
import com.works.utils.REnum;
import com.works.utils.Util;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductRestControllerBusiness {
    final ProductRepository productRepository;
    final ProductSessionRepository productSessionRepository;
    final ProductElasticRepository productElasticRepository;

    public ProductRestControllerBusiness(ProductRepository productRepository, ProductSessionRepository productSessionRepository, ProductElasticRepository productElasticRepository) {
        this.productRepository = productRepository;
        this.productSessionRepository = productSessionRepository;
        this.productElasticRepository = productElasticRepository;
    }

    public Map<REnum, Object> productLastTenItem() {
        Map<REnum, Object> hm = new LinkedHashMap<>();
        List<Product> productList = productRepository.getLastTenProductItem(Util.theCompany.getId());
        for (int i = 0; i < productList.size(); i++) {
            //    productList.get(i).setCompany(null);
            for (int j = 0; j < productList.get(i).getPr_categories().size(); j++) {
                productList.get(i).getPr_categories().get(j).setCompany(null);
                productList.get(i).getPr_categories().get(j).setProductCategories(null);
            }
        }
        if (productList.size() > 0) {
            hm.put(REnum.STATUS, true);
            hm.put(REnum.MESSAGE, "İşlem Başarılı");
        } else {
            hm.put(REnum.STATUS, false);
            hm.put(REnum.MESSAGE, "İşlem Başarısız");
        }
        hm.put(REnum.RESULT, productList);
        return hm;
    }

    public Map<REnum, Object> productEntryQuantityList(String stPage, String stIndex) {
        Map<REnum, Object> hm = new LinkedHashMap<>();
        Integer size = productSessionRepository.findByCompanynameEquals(Util.theCompany.getCompany_name()).size();
        if (size > 0) {
            Integer countOfPage = ((int) Math.ceil((double) size / Util.pageSize));
            Integer stPageIndex = Integer.parseInt(stPage);
            if (countOfPage >= stPageIndex) {
                if (stPageIndex > 0) {
                    List<ProductSession> productSessionList = productSessionRepository.findByCompanynameEquals(Util.theCompany.getCompany_name(), PageRequest.of(Integer.parseInt(stPage) - 1, Util.pageSize));
                    Integer index = Integer.parseInt(stIndex);
                    if (index > productSessionList.size()) {
                        hm.put(REnum.STATUS, true);
                        hm.put(REnum.MESSAGE, "Yeterli veri yok. Getirilmek istenen veri adedi-> " + index + " Var olan veri adedi-> " + productSessionList.size());
                        hm.put(REnum.RESULT, productSessionList);
                    } else {
                        List<ProductSession> firstNElementsList = productSessionList.stream().limit(index).collect(Collectors.toList());
                        hm.put(REnum.STATUS, true);
                        hm.put(REnum.MESSAGE, "İşlem Başarılı");
                        hm.put(REnum.COUNT, size);
                        hm.put(REnum.COUNTOFPAGE, countOfPage);
                        hm.put(REnum.RESULT, firstNElementsList);
                    }
                } else {
                    hm.put(REnum.STATUS, false);
                    hm.put(REnum.MESSAGE, "Girilen sayfa 0 dan büyük olmalı");
                    hm.put(REnum.RESULT, null);
                    return hm;
                }
            } else {
                hm.put(REnum.STATUS, false);
                hm.put(REnum.MESSAGE, "Aranilan sayfada veri yoktur.");
                hm.put(REnum.COUNTOFPAGE, countOfPage);
                hm.put(REnum.RESULT, null);
            }

        } else {
            hm.put(REnum.STATUS, false);
            hm.put(REnum.MESSAGE, "Veritabanında kayıt yok");
            hm.put(REnum.RESULT, null);
        }
        return hm;
    }

    public Map<REnum, Object> productListByCategory(String stCategory, String stPage, String stIndex) {
        Map<REnum, Object> hm = new LinkedHashMap<>();
        // Integer size = productRepository.findByPr_categories_IdEqualsAndCompany_IdEquals(Integer.parseInt(stCategory),Util.theCompany.getId()).size();
        List<ProductsOfCategoryInfo> productsOfCategoryInfoList = productRepository.getProductsOfCategory(Integer.parseInt(stCategory), Util.theCompany.getId());
        Integer size = productsOfCategoryInfoList.size();
        if (size > 0) {
            Integer countOfPage = ((int) Math.ceil((double) size / Util.pageSize));
            Integer stPageIndex = Integer.parseInt(stPage);
            if (countOfPage >= stPageIndex) {
                // o sayfadaki toplam veri adedi buunacak
                // girilen deger buna esit veya az ise direk gosterilecek
                // girilen deger daha fazla ise bu kadar veri vardır. Olan veriler getirildi denecek
                //     List<Product> productList = productRepository.findByPrCategories_IdEqualsAndCompany_IdEquals(Integer.parseInt(stCategory),Util.theCompany.getId(), PageRequest.of(Integer.parseInt(stIndex)-1, Util.pageSize));
                List<Product> productList = new ArrayList<>();
                List<ProductsOfCategoryInfo> productsOfCategoryInfoListPageable = productRepository.getProductsOfCategory(Integer.parseInt(stCategory), Util.theCompany.getId(), PageRequest.of(Integer.parseInt(stPage) - 1, Util.pageSize));
                Integer templSize = productRepository.getProductsOfCategory(Integer.parseInt(stCategory), Util.theCompany.getId(), PageRequest.of(Integer.parseInt(stPage) - 1, Util.pageSize)).size();
                for (int i = 0; i < templSize; i++) {
                    int tempIndex = Integer.parseInt(productsOfCategoryInfoListPageable.get(i).getProductId());
                    Product product = new Product();
                    product = productRepository.findById(tempIndex).get();
                    productList.add(product);
                }

                Integer index = Integer.parseInt(stIndex);
                if (index > productList.size()) {
                    hm.put(REnum.STATUS, true);
                    hm.put(REnum.MESSAGE, "Yeterli veri yok. Getirilmek istenen veri adedi-> " + index + " Var olan veri adedi-> " + productList.size());
                    hm.put(REnum.RESULT, productList);
                } else {
                    List<Product> firstNElementsList = productList.stream().limit(index).collect(Collectors.toList());
                    hm.put(REnum.STATUS, true);
                    hm.put(REnum.MESSAGE, "İşlem Başarılı");
                    hm.put(REnum.COUNT, size);
                    hm.put(REnum.COUNTOFPAGE, countOfPage);
                    hm.put(REnum.RESULT, firstNElementsList);
                }
            } else {
                hm.put(REnum.STATUS, false);
                hm.put(REnum.MESSAGE, "Aranilan sayfada veri yoktur.");
                hm.put(REnum.COUNTOFPAGE, countOfPage);
                hm.put(REnum.RESULT, null);
            }

        } else {
            hm.put(REnum.STATUS, false);
            hm.put(REnum.MESSAGE, "Veritabanında kayıt yok");
            hm.put(REnum.RESULT, null);
        }
        return hm;
    }

    public Map<REnum, Object> productListSearch(String stSearchKey, String stPageIndex, String stPageSize) {
        Map<REnum, Object> hm = new LinkedHashMap<>();
        try {
            Integer pageIndex = Integer.parseInt(stPageIndex);
            Integer pageSize = Integer.parseInt(stPageSize);

            Integer size = productElasticRepository.findByPr_name(stSearchKey + " " + Util.theCompany.getCompany_name()).size();
            if (size > 0) {
                hm.put(REnum.STATUS, true);
                hm.put(REnum.MESSAGE, "İşlem Başarılı");
                hm.put(REnum.COUNT, size);
                hm.put(REnum.COUNTOFPAGE, ((int) Math.ceil((double) size / pageSize)));
                hm.put(REnum.RESULT, productElasticRepository.findByPr_name(stSearchKey + " " + Util.theCompany.getCompany_name(), PageRequest.of(pageIndex - 1, pageSize)));
                hm.put(REnum.ERROR, null);
            } else {
                hm.put(REnum.STATUS, false);
                hm.put(REnum.MESSAGE, "Veri Bulunamadı");
                hm.put(REnum.RESULT, null);
            }
        } catch (Exception ex) {
            hm.put(REnum.STATUS, false);
            hm.put(REnum.MESSAGE, "Girilen sayfa ve sayfadaki veri sayısı string olamaz");
            hm.put(REnum.RESULT, null);
        }
        return hm;
    }

    public Map<REnum, Object> productDetail(String stIndex) {
        Map<REnum, Object> hm = new LinkedHashMap<>();
        try {
            Integer index = Integer.parseInt(stIndex);
            if (index > 0) {
                Optional<Product> optionalProduct = productRepository.findByIdEqualsAndCompany_IdEqualsAllIgnoreCase(index, Util.theCompany.getId());
                if (optionalProduct.isPresent()) {
                    //    productList.get(i).setCompany(null);
                    for (int i = 0; i < optionalProduct.get().getPr_categories().size(); i++) {
                        optionalProduct.get().getPr_categories().get(i).setCompany(null);
                        optionalProduct.get().getPr_categories().get(i).setProductCategories(null);
                    }
                    hm.put(REnum.STATUS, true);
                    hm.put(REnum.MESSAGE, "İşlem Başarılı");
                    hm.put(REnum.RESULT, optionalProduct.get());
                } else {
                    hm.put(REnum.STATUS, false);
                    hm.put(REnum.MESSAGE, "Ürün bulunamadi");
                    hm.put(REnum.RESULT, null);
                }
            } else {
                hm.put(REnum.STATUS, false);
                hm.put(REnum.MESSAGE, "Lütfen sayma(pozitif) sayısı giriniz");
                hm.put(REnum.RESULT, null);
            }
        } catch (Exception ex) {
            hm.put(REnum.STATUS, false);
            hm.put(REnum.MESSAGE, "Lütfen rakam giriniz");
            hm.put(REnum.RESULT, null);
        }
        return hm;
    }

    public Map<REnum, Object> productDelete(String stIndex) {
        Map<REnum, Object> hm = new LinkedHashMap<>();
        hm.put(REnum.MESSAGE, "Başarılı");
        productRepository.deleteById(Integer.valueOf(stIndex));
        productSessionRepository.deleteById(stIndex);
        productElasticRepository.deleteById(stIndex);
        hm.put(REnum.STATUS, true);
        return hm;
    }

    public Map<REnum, Object> productPageListSearch(HttpServletRequest request, String stSearchKey) {
        Map<String, String[]> allMap = request.getParameterMap();

        Map<REnum, Object> hm = new LinkedHashMap<>();
        hm.put(REnum.STATUS, true);
        hm.put(REnum.MESSAGE, "Başarılı");

        int validPage = Integer.parseInt(allMap.get("start")[0]) == 0 ? 0 : (Integer.parseInt(allMap.get("start")[0])) / Integer.parseInt(allMap.get("length")[0]);
        hm.put(REnum.RESULT, (productElasticRepository.findByPr_name(stSearchKey + " " + Util.theCompany.getCompany_name(), PageRequest.of(validPage, Integer.parseInt(allMap.get("length")[0])))).getContent());
        Integer totalCount = productElasticRepository.findByPr_name(stSearchKey + " " + Util.theCompany.getCompany_name()).size();
        hm.put(REnum.ERROR, null);
        hm.put(REnum.COUNT, totalCount);
        hm.put(REnum.DRAW, Integer.parseInt(allMap.get("draw")[0]));
        return hm;
    }

    public Map<REnum, Object> productPageList(HttpServletRequest request) {
        Map<String, String[]> allMap = request.getParameterMap();
        Map<REnum, Object> hm = new LinkedHashMap<>();
        hm.put(REnum.STATUS, true);
        hm.put(REnum.MESSAGE, "Başarılı");
        int validPage = Integer.parseInt(allMap.get("start")[0]) == 0 ? 0 : (Integer.parseInt(allMap.get("start")[0])) / Integer.parseInt(allMap.get("length")[0]);
        hm.put(REnum.RESULT, productSessionRepository.findByCompanynameEquals(Util.theCompany.getCompany_name(), PageRequest.of(validPage, Integer.parseInt(allMap.get("length")[0]))));
        int filterCount = productSessionRepository.findByCompanynameEquals(Util.theCompany.getCompany_name()).size();
        hm.put(REnum.COUNT, filterCount);
        hm.put(REnum.DRAW, Integer.parseInt(allMap.get("draw")[0]));
        return hm;
    }
}
