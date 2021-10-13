package com.works.controllers.adminpanel;

import com.works.entities.survey.Survey;
import com.works.entities.survey.SurveySelection;
import com.works.models._elastic.Survey_;
import com.works.models._redis.SurveySession;
import com.works.repositories._elastic.SurveyElasticRepository;
import com.works.repositories._jpa.SurveyRepository;
import com.works.repositories._jpa.SurveySelectionRepository;
import com.works.repositories._redis.SurveySessionRepository;
import com.works.utils.Util;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/admin/survey")
public class SurveyController {

    final String rvalue = "adminpanel/survey/";

    final SurveyRepository surveyRepository;
    final SurveySessionRepository surveySessionRepository;
    final SurveyElasticRepository surveyElasticRepository;
    final SurveySelectionRepository surveySelectionRepository;


    public SurveyController(SurveyRepository surveyRepository, SurveySelectionRepository surveySelectionRepository, SurveySessionRepository surveySessionRepository, SurveyElasticRepository surveyElasticRepository) {
        this.surveyRepository = surveyRepository;
        this.surveySelectionRepository = surveySelectionRepository;
        this.surveySessionRepository = surveySessionRepository;
        this.surveyElasticRepository = surveyElasticRepository;
    }

    @GetMapping("")
    public String survey(Model model) {
        model.addAttribute("survey", new Survey());
        model.addAttribute("isError", 0);
        return "adminpanel/survey/surveyadd";
    }


    @GetMapping("/list")
    public String surveyList() {
        surveySessionRepository.findAll();
        return "adminpanel/survey/surveylist";
    }

    @PostMapping("/add")
    public String surveyAdd(@Valid @ModelAttribute("survey") Survey survey, BindingResult bindingResult, Model model) {
        if (!bindingResult.hasErrors()) {
            try {
                String surveyId = String.valueOf(surveyRepository.save(survey).getId());
                SurveySession surveySession = new SurveySession();
                surveySession.setSurvey_title(survey.getSurvey_title());
                surveySession.setId(surveyId);
                surveySessionRepository.save(surveySession);
                Survey_ survey_ = new Survey_();
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


    @PostMapping("/selection") // {index} --> seçilen ankete şeçenek eklenmeli
    public String surveySelectionAdd(/*@PathVariable String index,*/ @Valid @ModelAttribute("surveySelection") SurveySelection surveySelection, BindingResult bindingResult, Model model) {
        if (!bindingResult.hasErrors()) {
            try {
                // int survey_number = Integer.parseInt(index);
                surveySelectionRepository.save(surveySelection);
            } catch (Exception ex) {
                System.err.println("Error: " + ex);
                model.addAttribute("isError", 1);
                return rvalue + "surveyoptionadd";
            }
        } else {
            System.err.println(Util.errors(bindingResult));
            model.addAttribute("isError", 0);
            return rvalue + "surveyoptionadd";

        }

        return "redirect:/admin/survey";
    }

    @GetMapping("/detail")
    public String surveyDetail() {
        return rvalue + "surveydetail";
    }


}


