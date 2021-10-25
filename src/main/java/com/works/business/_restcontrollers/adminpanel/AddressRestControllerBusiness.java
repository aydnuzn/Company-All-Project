package com.works.business._restcontrollers.adminpanel;

import com.works.entities.Address;
import com.works.entities.constant.address.City;
import com.works.entities.constant.address.District;
import com.works.entities.security.User;
import com.works.models._redis.AddressSession;
import com.works.properties.AddressInterlayer;
import com.works.repositories._jpa.AddressRepository;
import com.works.repositories._jpa.CityRepository;
import com.works.repositories._jpa.DistrictRepository;
import com.works.repositories._jpa.UserRepository;
import com.works.repositories._redis.AddressSessionRepository;
import com.works.utils.REnum;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class AddressRestControllerBusiness {
    final AddressRepository addressRepository;
    final AddressSessionRepository addressSessionRepository;
    final UserRepository userRepository;
    final CityRepository cityRepository;
    final DistrictRepository districtRepository;

    public AddressRestControllerBusiness(AddressRepository addressRepository, AddressSessionRepository addressSessionRepository, UserRepository userRepository, CityRepository cityRepository, DistrictRepository districtRepository) {
        this.addressRepository = addressRepository;
        this.addressSessionRepository = addressSessionRepository;
        this.userRepository = userRepository;
        this.cityRepository = cityRepository;
        this.districtRepository = districtRepository;
    }

    public Map<REnum, Object> addressList(String stIndex) {
        Map<REnum, Object> hm = new LinkedHashMap<>();
        Integer index = 0;
        try {
            index = Integer.parseInt(stIndex);
            Optional<User> optCustomer = userRepository.findById(index);
            if (!optCustomer.isPresent()) {
                hm.put(REnum.STATUS, false);
                hm.put(REnum.MESSAGE, "Kullanıcı Bulunamadı.");
                return hm;
            } else {
                if (optCustomer.get().getRoles().get(0).getRo_id() != 3) {//Müşteri değil
                    hm.put(REnum.STATUS, false);
                    hm.put(REnum.MESSAGE, "Sadece müşterilerin adresleri vardır.");
                    return hm;
                }
            }
        } catch (Exception e) {
            hm.put(REnum.STATUS, false);
            hm.put(REnum.MESSAGE, "String deger girilmiş. İşlem basarisiz. - stIndex tam sayı olmalıdır.");
            return hm;
        }
        hm.put(REnum.STATUS, true);
        hm.put(REnum.MESSAGE, stIndex + " numaralı müşteriye adresler başarılı.");
        List<AddressSession> ls = addressSessionRepository.findByCustomerid(stIndex);
        hm.put(REnum.RESULT, ls);
        hm.put(REnum.COUNT, ls.size());
        return hm;
    }

    public Map<REnum, Object> addressDelete(String stIndex) {
        Map<REnum, Object> hm = new LinkedHashMap<>();
        try {
            Optional<Address> optional = addressRepository.findById(Integer.valueOf(stIndex));
            if (optional.isPresent()) {
                addressRepository.deleteById(Integer.valueOf(stIndex));
                addressSessionRepository.deleteById(stIndex);
                hm.put(REnum.STATUS, true);
                hm.put(REnum.MESSAGE, "İşlem Başarılı");
                return hm;
            } else {
                hm.put(REnum.STATUS, false);
                hm.put(REnum.MESSAGE, "İşlem Başarısız. Bu numaraya sahip adres yok.");
                return hm;
            }
        } catch (Exception e) {
            hm.put(REnum.STATUS, false);
            hm.put(REnum.MESSAGE, "İşlem Başarısız. Numara tam sayı olmalıdır.");
            return hm;
        }
    }

    public Map<REnum, Object> add(AddressInterlayer addressInterlayer, BindingResult bindingResult, String stIndex) {
        Map<REnum, Object> hm = new LinkedHashMap<>();
        if (!bindingResult.hasErrors()) {
            Integer index = 0;
            try {
                index = Integer.parseInt(stIndex);
            } catch (Exception e) {
                hm.put(REnum.STATUS, false);
                hm.put(REnum.MESSAGE, "String deger girilmiş. İşlem basarisiz. - stIndex tam sayı olmalıdır.");
                return hm;
            }

            Optional<User> optCustomer = userRepository.findById(index);
            if (!optCustomer.isPresent()) {
                hm.put(REnum.STATUS, false);
                hm.put(REnum.MESSAGE, "Kullanıcı bulunamadı.");
                return hm;
            }

            Optional<City> optCity = cityRepository.findById(addressInterlayer.getCity_id());
            if (!optCity.isPresent()) {
                hm.put(REnum.STATUS, false);
                hm.put(REnum.MESSAGE, "İl bulunamadı.");
                return hm;
            }

            Optional<District> optDistrict = districtRepository.findById(addressInterlayer.getDistrict_id());
            if (!optDistrict.isPresent()) {
                hm.put(REnum.STATUS, false);
                hm.put(REnum.MESSAGE, "İlçe bulunamadı.");
                return hm;
            }

            Address address = new Address();
            address.setCustomer(optCustomer.get());
            address.setCity(optCity.get());
            address.setDistrict(optDistrict.get());
            address.setAddress_detail(addressInterlayer.getAddress_detail());
            address = addressRepository.save(address);

            AddressSession addressSession = new AddressSession();
            addressSession.setId(String.valueOf(address.getId()));
            addressSession.setCustomerid(String.valueOf(address.getCustomer().getId()));
            addressSession.setCity_name(address.getCity().getCity_name());
            addressSession.setDistrict_name(address.getDistrict().getDistrict_name());
            addressSession.setAddress_detail(address.getAddress_detail());
            addressSessionRepository.save(addressSession);

            hm.put(REnum.STATUS, true);
            hm.put(REnum.MESSAGE, "Adres Ekleme Başarılı.");
            return hm;
        } else {
            hm.put(REnum.STATUS, false);
            hm.put(REnum.MESSAGE, "String deger girilmiş. İşlem basarisiz");
        }
        return hm;
    }


}
