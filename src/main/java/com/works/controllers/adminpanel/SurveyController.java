package com.works.controllers.adminpanel;

import com.works.business._controllers.adminpanel.SurveyControllerBusiness;
import com.works.entities.survey.Survey;
import com.works.entities.survey.SurveySelection;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/admin/survey")
public class SurveyController {

    final SurveyControllerBusiness business;

    public SurveyController(SurveyControllerBusiness business) {
        this.business = business;
    }

    //Anket Ekleme Sayfası Açılışı
    @GetMapping("")
    public String survey(Model model) {
        return business.survey(model);
    }

    //Anket Güncelleme Sayfası Açılışı
    @GetMapping("/{stIndex}")
    public String surveyUpdate(@PathVariable String stIndex, Model model) {
        return business.surveyUpdate(stIndex, model);
    }

    //Anket Listeleme Sayfası Açılışı
    @GetMapping("/list")
    public String surveyList() {
        return business.surveyList();
    }

    //Anket Detayı Sayfası Açılışı
    @GetMapping("/detail/{stIndex}")
    public String surveyDetail(@PathVariable String stIndex, Model model) {
        return business.surveyDetail(stIndex, model);
    }

    //Anket Seçeneği Ekleme Sayfası Açılışı
    @GetMapping("/selection/{stIndex}")
    public String surveySelection(@PathVariable String stIndex, Model model) {
        return business.surveySelection(stIndex, model);
    }

    //Anket Ekleme İşlemi
    @PostMapping("/add")
    public String surveyAdd(@Valid @ModelAttribute("survey") Survey survey, BindingResult bindingResult, Model model) {
        return business.surveyAdd(survey, bindingResult, model);
    }

    //Anket Güncelleme İşlemi
    @PostMapping("/update/{stIndex}")
    public String surveyUpdate(@Valid @ModelAttribute("survey") Survey survey, BindingResult bindingResult, Model model, @PathVariable String stIndex) {
        return business.surveyUpdate(survey, bindingResult, model, stIndex);
    }

    //Seçenek Ekleme İşlemi
    @PostMapping("/selection/add/{stIndex}")
    public String surveySelectionAdd(@Valid @ModelAttribute("surveySelection") SurveySelection surveySelection, BindingResult bindingResult, Model model, @PathVariable String stIndex) {
        return business.surveySelectionAdd(surveySelection, bindingResult, model, stIndex);
    }

    //Seçenek Silme İşlemi
    //1.parametre seçenek no
    //2.parametre anket no
    @GetMapping("/selection/delete/{stIndex}/{stIndex2}")
    public String surveySelectionDelete(@PathVariable String stIndex, @PathVariable String stIndex2) {
        return business.surveySelectionDelete(stIndex, stIndex2);

    }
}