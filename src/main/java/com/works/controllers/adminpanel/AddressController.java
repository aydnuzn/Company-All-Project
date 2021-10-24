package com.works.controllers.adminpanel;

import com.works.business._controllers.adminpanel.AddressControllerBusiness;
import com.works.properties.AddressInterlayer;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/admin/address")
public class AddressController {


    final AddressControllerBusiness business;

    public AddressController(AddressControllerBusiness business) {
        this.business = business;
    }

    //address detay sayfasının açılması
    @GetMapping("/{stIndex}")
    public String address(Model model, @PathVariable String stIndex) {
        return business.address(model, stIndex);
    }

    //Müşterinin adreslerine ekleme yapılması amacıyla açılan sayfa
    @GetMapping("/add/{stIndex}")
    public String addressAdd(Model model, @PathVariable String stIndex) {
        return business.addressAdd(model, stIndex);
    }

    //Müşteriye yeni adres ekleme
    @PostMapping("/add/{stIndex}")
    public String add(@Valid @ModelAttribute("addressInterlayer") AddressInterlayer addressInterlayer, BindingResult bindingResult, Model model, @PathVariable String stIndex) {
        return business.add(addressInterlayer, bindingResult, model, stIndex);
    }


    //Adres Güncelleme Sayfası Açılımı
    //1.index customer'a ait
    //2.index addressId
    @GetMapping("/update/{stIndex}/{stIndeks}")
    public String addressUpdate(Model model, @PathVariable String stIndex, @PathVariable String stIndeks) {
        return business.addressUpdate(model, stIndex, stIndeks);
    }

    //Adres Güncelleme
    //1.index customer'a ait
    //2.index addressId
    @PostMapping("/update/{stIndeks}/{stIndex}")
    public String update(@Valid @ModelAttribute("addressInterlayer") AddressInterlayer addressInterlayer, BindingResult bindingResult, Model model, @PathVariable String stIndeks, @PathVariable String stIndex) {
        return business.update(addressInterlayer, bindingResult, model, stIndeks, stIndex);
    }

    //Adres Silme İşlemi
    //İlk index customer No
    //İkinci index Address No
    @GetMapping("/delete/{stIndex}/{stIndex2}")
    public String addressDelete(@PathVariable String stIndex, @PathVariable String stIndex2) {
        return business.addressDelete(stIndex, stIndex2);
    }
}
