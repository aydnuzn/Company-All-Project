package com.works.business._controllers.adminpanel;

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
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.util.Optional;

@Service
public class AddressControllerBusiness {
    final String rvalue = "adminpanel/address/";
    final UserRepository userRepository;
    final CityRepository cityRepository;
    final DistrictRepository districtRepository;

    final AddressRepository addressRepository;
    final AddressSessionRepository addressSessionRepository;

    public AddressControllerBusiness(UserRepository userRepository, CityRepository cityRepository, DistrictRepository districtRepository, AddressRepository addressRepository, AddressSessionRepository addressSessionRepository) {
        this.userRepository = userRepository;
        this.cityRepository = cityRepository;
        this.districtRepository = districtRepository;
        this.addressRepository = addressRepository;
        this.addressSessionRepository = addressSessionRepository;
    }

    public String address(Model model, String stIndex) {
        Integer index = 0;
        try {
            index = Integer.parseInt(stIndex);
            Optional<User> optCustomer = userRepository.findById(index);
            if (!optCustomer.isPresent()) {
                return "error/404";
            } else {
                if (optCustomer.get().getRoles().get(0).getRo_id() != 3) {//Müşteri değil
                    return "error/404";
                }
            }
        } catch (Exception e) {
            return "error/404";
        }
        model.addAttribute("customerIndex", stIndex);
        model.addAttribute("customerName", userRepository.findById(index).get());
        model.addAttribute("addressList", addressSessionRepository.findByCustomerid(stIndex));
        return "adminpanel/address/addressdetail";
    }

    public String addressAdd(Model model, String stIndex) {
        Integer index = 0;
        try {
            index = Integer.parseInt(stIndex);
            Optional<User> optCustomer = userRepository.findById(index);
            if (!optCustomer.isPresent()) {
                return "error/404";
            } else {
                if (optCustomer.get().getRoles().get(0).getRo_id() != 3) {//Müşteri değil
                    return "error/404";
                }
            }
        } catch (Exception e) {
            return "error/404";
        }
        model.addAttribute("customerIndex", stIndex);
        model.addAttribute("addressInterlayer", new AddressInterlayer());
        model.addAttribute("cityList", cityRepository.findAll());
        model.addAttribute("districtList", districtRepository.findByCity_idEquals(1));
        return "adminpanel/address/addressadd";
    }

    public String add(AddressInterlayer addressInterlayer, BindingResult bindingResult, Model model, String stIndex) {
        if (!bindingResult.hasErrors()) {
            Integer index = 0;
            try {
                index = Integer.parseInt(stIndex);
            } catch (Exception e) {
                model.addAttribute("isError", 1);
                model.addAttribute("cityList", cityRepository.findAll());
                model.addAttribute("districtList", districtRepository.findByCity_idEquals(1));
                model.addAttribute("customerIndex", stIndex);
                return rvalue + "addressadd";
            }

            Optional<User> optCustomer = userRepository.findById(index);
            if (!optCustomer.isPresent()) {
                model.addAttribute("isError", 2);
                model.addAttribute("cityList", cityRepository.findAll());
                model.addAttribute("districtList", districtRepository.findByCity_idEquals(1));
                model.addAttribute("customerIndex", stIndex);
                return rvalue + "addressadd";
            }

            Optional<City> optCity = cityRepository.findById(addressInterlayer.getCity_id());
            if (!optCity.isPresent()) {
                model.addAttribute("isError", 3);
                model.addAttribute("cityList", cityRepository.findAll());
                model.addAttribute("districtList", districtRepository.findByCity_idEquals(1));
                model.addAttribute("customerIndex", stIndex);
                return rvalue + "addressadd";
            }

            Optional<District> optDistrict = districtRepository.findById(addressInterlayer.getDistrict_id());
            if (!optDistrict.isPresent()) {
                model.addAttribute("isError", 4);
                model.addAttribute("cityList", cityRepository.findAll());
                model.addAttribute("districtList", districtRepository.findByCity_idEquals(1));
                model.addAttribute("customerIndex", stIndex);
                return rvalue + "addressadd";
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

            return "redirect:/admin/address/" + stIndex;
        } else {
            model.addAttribute("cityList", cityRepository.findAll());
            model.addAttribute("districtList", districtRepository.findByCity_idEquals(1));
            model.addAttribute("customerIndex", stIndex);
            return rvalue + "addressadd";
        }
    }

    public String addressUpdate(Model model, String stIndex, String stIndeks) {
        Integer index = 0;
        Integer index2 = 0;
        try {
            index = Integer.parseInt(stIndex);
            index2 = Integer.valueOf(stIndeks);
            Optional<User> optCustomer = userRepository.findById(index);
            Optional<Address> optAddress = addressRepository.findById(index2);
            if (!optCustomer.isPresent() || !optAddress.isPresent()) {
                return "error/404";
            } else {
                if (optCustomer.get().getRoles().get(0).getRo_id() != 3) {//Müşteri değil
                    return "error/404";
                }
            }
        } catch (Exception e) {
            return "error/404";
        }
        model.addAttribute("customerIndex", stIndex);
        model.addAttribute("addressIndex", stIndeks);
        model.addAttribute("info", addressRepository.findById(Integer.valueOf(stIndeks)).get());
        model.addAttribute("addressInterlayer", new AddressInterlayer());
        model.addAttribute("cityList", cityRepository.findAll());
        model.addAttribute("districtList", districtRepository.findByCity_idEquals(1));
        return "adminpanel/address/addressupdate";
    }

    public String update(AddressInterlayer addressInterlayer, BindingResult bindingResult, Model model, String stIndeks, String stIndex) {
        if (!bindingResult.hasErrors()) {
            Integer index = 0;
            try {
                index = Integer.parseInt(stIndex);
            } catch (Exception e) {
                model.addAttribute("isError", 1);
                model.addAttribute("cityList", cityRepository.findAll());
                model.addAttribute("districtList", districtRepository.findByCity_idEquals(1));
                model.addAttribute("customerIndex", stIndeks);
                model.addAttribute("addressIndex", stIndex);
                return rvalue + "addressupdate";
            }

            Optional<Address> optAddress = addressRepository.findById(index);
            if (!optAddress.isPresent()) {
                model.addAttribute("isError", 2);
                model.addAttribute("cityList", cityRepository.findAll());
                model.addAttribute("districtList", districtRepository.findByCity_idEquals(1));
                model.addAttribute("customerIndex", stIndeks);
                model.addAttribute("addressIndex", stIndex);
                return rvalue + "addressupdate";
            }

            Optional<City> optCity = cityRepository.findById(addressInterlayer.getCity_id());
            if (!optCity.isPresent()) {
                model.addAttribute("isError", 3);
                model.addAttribute("cityList", cityRepository.findAll());
                model.addAttribute("districtList", districtRepository.findByCity_idEquals(1));
                model.addAttribute("customerIndex", stIndeks);
                model.addAttribute("addressIndex", stIndex);
                return rvalue + "addressupdate";
            }

            Optional<District> optDistrict = districtRepository.findById(addressInterlayer.getDistrict_id());
            if (!optDistrict.isPresent()) {
                model.addAttribute("isError", 4);
                model.addAttribute("cityList", cityRepository.findAll());
                model.addAttribute("districtList", districtRepository.findByCity_idEquals(1));
                model.addAttribute("customerIndex", stIndeks);
                model.addAttribute("addressIndex", stIndex);
                return rvalue + "addressupdate";
            }

            Address address = optAddress.get();
            address.setId(index);
            address.setCity(optCity.get());
            address.setDistrict(optDistrict.get());
            address.setAddress_detail(addressInterlayer.getAddress_detail());
            address = addressRepository.saveAndFlush(address);

            addressSessionRepository.delete(addressSessionRepository.findById(stIndex).get());

            AddressSession addressSession = new AddressSession();
            addressSession.setId(String.valueOf(address.getId()));
            addressSession.setCustomerid(String.valueOf(address.getCustomer().getId()));
            addressSession.setCity_name(address.getCity().getCity_name());
            addressSession.setDistrict_name(address.getDistrict().getDistrict_name());
            addressSession.setAddress_detail(address.getAddress_detail());
            addressSessionRepository.save(addressSession);

            return "redirect:/admin/address/" + optAddress.get().getCustomer().getId();
        } else {
            model.addAttribute("cityList", cityRepository.findAll());
            model.addAttribute("districtList", districtRepository.findByCity_idEquals(1));
            model.addAttribute("customerIndex", stIndeks);
            model.addAttribute("addressIndex", stIndex);
            return rvalue + "addressupdate";
        }
    }

    public String addressDelete(String stIndex, String stIndex2) {
        try {
            addressRepository.deleteById(Integer.valueOf(stIndex2.trim()));
            addressSessionRepository.deleteById(stIndex2.trim());
            return "redirect:/admin/address/" + stIndex.trim();
        } catch (Exception e) {
            return "error/404";
        }
    }
}
