package com.works.controllers.adminpanel;

import com.works.entities.survey.Survey;
import com.works.entities.survey.SurveySelection;
import com.works.models._elastic.SurveyElastic;
import com.works.models._redis.SurveySelectionSession;
import com.works.models._redis.SurveySession;
import com.works.repositories._elastic.SurveyElasticRepository;
import com.works.repositories._jpa.SurveyRepository;
import com.works.repositories._jpa.SurveySelectionRepository;
import com.works.repositories._redis.SurveySelectionSessionRepository;
import com.works.repositories._redis.SurveySessionRepository;
import com.works.utils.Util;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/survey")
public class SurveyController {

    final String rvalue = "adminpanel/survey/";

    final SurveyRepository surveyRepository;
    final SurveySessionRepository surveySessionRepository;
    final SurveyElasticRepository surveyElasticRepository;
    final SurveySelectionRepository surveySelectionRepository;
    final SurveySelectionSessionRepository surveySelectionSessionRepository;


    public SurveyController(SurveyRepository surveyRepository, SurveySelectionRepository surveySelectionRepository, SurveySessionRepository surveySessionRepository, SurveyElasticRepository surveyElasticRepository, SurveySelectionSessionRepository surveySelectionSessionRepository) {
        this.surveyRepository = surveyRepository;
        this.surveySelectionRepository = surveySelectionRepository;
        this.surveySessionRepository = surveySessionRepository;
        this.surveyElasticRepository = surveyElasticRepository;
        this.surveySelectionSessionRepository = surveySelectionSessionRepository;
    }

    //Anket Ekleme Sayfası Açılışı
    @GetMapping("")
    public String survey(Model model) {
        model.addAttribute("survey", new Survey());
        model.addAttribute("isError", 0);
        return "adminpanel/survey/surveyadd";
    }

    //Anket Güncelleme Sayfası Açılışı
    @GetMapping("/{stIndex}")
    public String surveyUpdate(@PathVariable String stIndex, Model model) {
        Integer index = 0;
        try {
            index = Integer.parseInt(stIndex);
            Optional<Survey> optSurvey = surveyRepository.findById(index);
            if (optSurvey.isPresent()) {
                model.addAttribute("survey", optSurvey.get());
                model.addAttribute("index", index);
                model.addAttribute("isError", 0);
                return "adminpanel/survey/surveyupdate";
            } else {
                return "error/404";
            }

        } catch (Exception e) {
            return "error/404";
        }
    }

    //Anket Listeleme Sayfası Açılışı
    @GetMapping("/list")
    public String surveyList() {
        return "adminpanel/survey/surveylist";
    }

    //Anket Detayı Sayfası Açılışı
    @GetMapping("/detail/{stIndex}")
    public String surveyDetail(@PathVariable String stIndex, Model model) {
        Integer index = 0;
        try {
            index = Integer.parseInt(stIndex);
            Optional<SurveySession> optSurveySession = surveySessionRepository.findById(stIndex);//Survey
            List<SurveySelection> surveySelectionList = new ArrayList<>();//SelectionList
            try {
                surveySelectionList = surveySelectionRepository.findBySurvey_IdEquals(Integer.valueOf(stIndex));
            } catch (Exception e) {
                System.err.println(e);
            }
            if (optSurveySession.isPresent()) {
                model.addAttribute("survey_title", optSurveySession.get().getSurvey_title());
                model.addAttribute("survey_selections", surveySelectionList);
                model.addAttribute("index", index);
                return "adminpanel/survey/surveydetail";
            } else {
                return "error/404";
            }
        } catch (Exception ex) {
            return "error/404";
        }
    }

    //Anket Seçeneği Ekleme Sayfası Açılışı
    @GetMapping("/selection/{stIndex}")
    public String surveySelection(@PathVariable String stIndex, Model model) {
        Integer index = 0;
        try {
            index = Integer.parseInt(stIndex);
            Optional<Survey> optSurvey = surveyRepository.findById(index);
            if (optSurvey.isPresent()) {
                model.addAttribute("surveySelection", new SurveySelection());
                return "adminpanel/survey/surveyselectionadd";
            } else {
                return "error/404";
            }
        } catch (Exception ex) {
            return "error/404";
        }
    }

    //Anket Ekleme İşlemi
    @PostMapping("/add")
    public String surveyAdd(@Valid @ModelAttribute("survey") Survey survey, BindingResult bindingResult, Model model) {
        if (!bindingResult.hasErrors()) {
            try {
                String surveyId = String.valueOf(surveyRepository.save(survey).getId());
                SurveySession surveySession = new SurveySession();
                surveySession.setSurvey_title(survey.getSurvey_title());
                surveySession.setId(surveyId);
                surveySessionRepository.save(surveySession);
                SurveyElastic survey_ = new SurveyElastic();
                survey_.setSurvey_title(survey.getSurvey_title());
                survey_.setId(surveyId);
                surveyElasticRepository.save(survey_);
            } catch (DataIntegrityViolationException ex) {
                System.err.println("Error: " + ex);
                model.addAttribute("isError", 1);
                return rvalue + "surveyadd";
            }
        } else {
            System.out.println(Util.errors(bindingResult));
            model.addAttribute("isError", 0);
            return rvalue + "surveyadd";
        }
        return "redirect:/admin/survey";
    }

    //Anket Güncelleme İşlemi
    @PostMapping("/update/{stIndex}")
    public String surveyUpdate(@Valid @ModelAttribute("survey") Survey survey, BindingResult bindingResult, Model model, @PathVariable String stIndex) {
        Integer index = 0;
        try {
            index = Integer.parseInt(stIndex);
        } catch (Exception e) {
            return "error/404";
        }
        if (!bindingResult.hasErrors()) {
            try {
                survey.setId(index);
                surveyRepository.saveAndFlush(survey);
                SurveySession surveySession = new SurveySession();
                surveySession.setSurvey_title(survey.getSurvey_title());
                surveySession.setId(stIndex);
                surveySessionRepository.deleteById(stIndex);
                surveySessionRepository.save(surveySession);
                SurveyElastic survey_ = new SurveyElastic();
                survey_.setSurvey_title(survey.getSurvey_title());
                survey_.setId(stIndex);
                surveyElasticRepository.deleteById(stIndex);
                surveyElasticRepository.save(survey_);
            } catch (DataIntegrityViolationException ex) {
                System.err.println("Error: " + ex);
                model.addAttribute("isError", 1);
                return rvalue + "surveyupdate";
            }
        } else {
            System.out.println(Util.errors(bindingResult));
            model.addAttribute("isError", 0);
            return rvalue + "surveyupdate";
        }
        return "redirect:/admin/survey/" + stIndex;
    }

    //Seçenek Ekleme İşlemi
    @PostMapping("/selection/add/{stIndex}")
    public String surveySelectionAdd(@Valid @ModelAttribute("surveySelection") SurveySelection surveySelection, BindingResult bindingResult, Model model, @PathVariable String stIndex) {
        Integer index = 0;
        try {
            index = Integer.parseInt(stIndex);
        } catch (Exception e) {
            return "error/404";
        }
        if (!bindingResult.hasErrors()) {
            Optional<Survey> optSurvey = surveyRepository.findById(index);
            if (optSurvey.isPresent()) {
                surveySelection.setSurvey(optSurvey.get());
            } else {
                return "error/404";
            }
            try {
                Integer surveyId = surveySelectionRepository.save(surveySelection).getId();
                SurveySelectionSession surveySelectionSession = new SurveySelectionSession();
                surveySelectionSession.setId(String.valueOf(surveyId));//Seçenek numarası
                surveySelectionSession.setSurvey_selection_title(surveySelection.getSurvey_selection_title());
                surveySelectionSession.setSurvey_selection_score(0);//İlk ekleniyor.
                surveySelectionSession.setSurveyid(String.valueOf(optSurvey.get().getId()));//Anket numarası
                surveySelectionSessionRepository.save(surveySelectionSession);
            } catch (Exception ex) {
                System.err.println("Error: " + ex);
                model.addAttribute("isError", 1);
                return rvalue + "surveyselectionadd";
            }
        } else {
            System.err.println(Util.errors(bindingResult));
            model.addAttribute("isError", 0);
            return rvalue + "surveyselectionadd";
        }
        return "redirect:/admin/survey";
    }

    //Seçenek Silme İşlemi
    //1.parametre seçenek no
    //2.parametre anket no
    @GetMapping("/selection/delete/{stIndex}/{stIndex2}")
    public String surveySelectionDelete(@PathVariable String stIndex, @PathVariable String stIndex2) {
        //security'de eklenip, company bilgisi de alındıktan sonra burada;
        //Bu firmaya ait mi silinmeye çalışılan yazılacak.
        //Silinmek istenen anket seçenek no bu ankete mi ait? Eklenecek
        try {
            surveySelectionRepository.deleteById(Integer.valueOf(stIndex));
            surveySelectionSessionRepository.deleteById(stIndex);
            return "redirect:/admin/survey/detail/" + stIndex2.trim();
        } catch (Exception e) {
            return "error/404";
        }

    }
}