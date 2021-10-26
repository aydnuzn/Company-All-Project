package com.works.business._restcontrollers.adminpanel;

import com.works.repositories._elastic.AdvertisementElasticRepository;
import com.works.repositories._jpa.AdvertisementRepository;
import com.works.repositories._redis.AdvertisementSessionRepository;
import com.works.utils.REnum;
import com.works.utils.Util;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class AdvertisementRestControllerBusiness {
    final AdvertisementRepository advertisementRepository;
    final AdvertisementSessionRepository advertisementSessionRepository;
    final AdvertisementElasticRepository advertisementElasticRepository;

    public AdvertisementRestControllerBusiness(AdvertisementRepository advertisementRepository, AdvertisementSessionRepository advertisementSessionRepository, AdvertisementElasticRepository advertisementElasticRepository) {
        this.advertisementRepository = advertisementRepository;
        this.advertisementSessionRepository = advertisementSessionRepository;
        this.advertisementElasticRepository = advertisementElasticRepository;
    }

    public Map<REnum, Object> advertisementListSearch(String stSearchKey, String stIndex) {
        Map<REnum, Object> hm = new LinkedHashMap<>();
        hm.put(REnum.MESSAGE, "Başarılı");
        hm.put(REnum.STATUS, true);
        hm.put(REnum.RESULT, advertisementElasticRepository.findByAdv_title(stSearchKey + " " + Util.theCompany.getCompany_name(), PageRequest.of(Integer.parseInt(stIndex) - 1, Util.pageSize)));
        int additional = 0;
        Integer size = advertisementElasticRepository.findByAdv_title(stSearchKey + " " + Util.theCompany.getCompany_name()).size();
        if (size % Util.pageSize != 0) {
            additional = 1;
        }
        hm.put(REnum.ERROR, null);
        hm.put(REnum.COUNTOFPAGE, size / Util.pageSize + additional);
        return hm;
    }

    public Map<REnum, Object> advertisementList(String stIndex) {
        Map<REnum, Object> hm = new LinkedHashMap<>();
        hm.put(REnum.MESSAGE, "Başarılı");
        hm.put(REnum.STATUS, true);
        if (stIndex.equals("0")) {
            hm.put(REnum.RESULT, advertisementSessionRepository.findByCompanynameEquals(Util.theCompany.getCompany_name(), PageRequest.of(Integer.parseInt(stIndex), Util.pageSize)));
        } else {
            hm.put(REnum.RESULT, advertisementSessionRepository.findByCompanynameEquals(Util.theCompany.getCompany_name(), PageRequest.of(Integer.parseInt(stIndex) - 1, Util.pageSize)));
        }
        int additional = 0;
        Integer totalSize = advertisementSessionRepository.findByCompanynameEquals(Util.theCompany.getCompany_name()).size();
        if (totalSize % 10 != 0) {
            additional = 1;
        }
        hm.put(REnum.COUNTOFPAGE, (totalSize / Util.pageSize) + additional);
        return hm;
    }

    public Map<REnum, Object> advertisementDelete(String stIndex) {
        Map<REnum, Object> hm = new LinkedHashMap<>();
        hm.put(REnum.MESSAGE, "Başarılı");
        advertisementRepository.deleteById(Integer.valueOf(stIndex));
        advertisementSessionRepository.deleteById(stIndex);
        advertisementElasticRepository.deleteById(stIndex);
        hm.put(REnum.STATUS, true);
        return hm;
    }

    public Map<REnum, Object> advertisementPageListSearch(HttpServletRequest request, String stSearchKey) {
        Map<String, String[]> allMap = request.getParameterMap();

        Map<REnum, Object> hm = new LinkedHashMap<>();
        hm.put(REnum.MESSAGE, "Başarılı");
        hm.put(REnum.STATUS, true);

        int validPage = Integer.parseInt(allMap.get("start")[0]) == 0 ? 0 : (Integer.parseInt(allMap.get("start")[0])) / Integer.parseInt(allMap.get("length")[0]);
        hm.put(REnum.RESULT, (advertisementElasticRepository.findByAdv_title(stSearchKey + " " + Util.theCompany.getCompany_name(), PageRequest.of(validPage, Integer.parseInt(allMap.get("length")[0])))).getContent());
        Integer totalCount = advertisementElasticRepository.findByAdv_title(stSearchKey + " " + Util.theCompany.getCompany_name()).size();
        System.out.println("TOTAL -->" + totalCount);
        hm.put(REnum.ERROR, null);
        hm.put(REnum.COUNT, totalCount);
        hm.put(REnum.DRAW, Integer.parseInt(allMap.get("draw")[0]));
        return hm;
    }

    public Map<REnum, Object> advertisementPageList(HttpServletRequest request) {
        Map<String, String[]> allMap = request.getParameterMap();
        Map<REnum, Object> hm = new LinkedHashMap<>();
        hm.put(REnum.STATUS, true);
        hm.put(REnum.MESSAGE, "Başarılı");
        int validPage = Integer.parseInt(allMap.get("start")[0]) == 0 ? 0 : (Integer.parseInt(allMap.get("start")[0])) / Integer.parseInt(allMap.get("length")[0]);

        hm.put(REnum.RESULT, advertisementSessionRepository.findByCompanynameEquals(Util.theCompany.getCompany_name(), PageRequest.of(validPage, Integer.parseInt(allMap.get("length")[0]))));
        int filterCount = advertisementSessionRepository.findByCompanynameEquals(Util.theCompany.getCompany_name()).size();
        hm.put(REnum.COUNT, filterCount);
        hm.put(REnum.DRAW, Integer.parseInt(allMap.get("draw")[0]));
        return hm;
    }
}
