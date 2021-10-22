package com.works.restcontrollers;

import com.works.entities.constant.address.District;
import com.works.repositories._jpa.DistrictRepository;
import com.works.utils.REnum;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/rest")
public class ConstantRestController {
    final DistrictRepository districtRepository;

    public ConstantRestController(DistrictRepository districtRepository) {
        this.districtRepository = districtRepository;
    }

    @GetMapping("/getXDistricts/{stIndex}")
    public Map<REnum, Object> getXDistricts(@PathVariable String stIndex) {
        Map<REnum, Object> hm = new LinkedHashMap<>();
        List<District> districtList = new ArrayList<>();
        try {
            int did = Integer.parseInt(stIndex);
            districtList = districtRepository.findByCity_idEquals(did);
            if (districtList.size() > 0) {
                hm.put(REnum.STATUS, true);
                hm.put(REnum.MESSAGE, "İlçeler bulundu.");
            } else {
                hm.put(REnum.STATUS, false);
                hm.put(REnum.MESSAGE, "Veri tabanında" + did + " numaralı İlçe bulunamadı.");
            }
        } catch (Exception ex) {
            hm.put(REnum.STATUS, false);
            hm.put(REnum.MESSAGE, "İlçe bulunamadı. - Integer ifade girilmesi gerekir.");
        }
        hm.put(REnum.COUNT, districtList.size());
        hm.put(REnum.RESULT, districtList);
        return hm;
    }
}
