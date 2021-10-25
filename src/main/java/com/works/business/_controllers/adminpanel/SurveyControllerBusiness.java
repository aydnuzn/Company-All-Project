package com.works.business._controllers.adminpanel;

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
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SurveyControllerBusiness {
    final String rvalue = "adminpanel/survey/";

    final SurveyRepository surveyRepository;
    final SurveySessionRepository surveySessionRepository;
    final SurveyElasticRepository surveyElasticRepository;
    final SurveySelectionRepository surveySelectionRepository;
    final SurveySelectionSessionRepository surveySelectionSessionRepository;

    public SurveyControllerBusiness(SurveyRepository surveyRepository, SurveySessionRepository surveySessionRepository, SurveyElasticRepository surveyElasticRepository, SurveySelectionRepository surveySelectionRepository, SurveySelectionSessionRepository surveySelectionSessionRepository) {
        this.surveyRepository = surveyRepository;
        this.surveySessionRepository = surveySessionRepository;
        this.surveyElasticRepository = surveyElasticRepository;
        this.surveySelectionRepository = surveySelectionRepository;
        this.surveySelectionSessionRepository = surveySelectionSessionRepository;
    }

    public String survey(Model model) {
        model.addAttribute("survey", new Survey());
        model.addAttribute("isError", 0);
        return rvalue + "surveyadd";
    }

    public String surveyUpdate(String stIndex, Model model) {
        Integer index = 0;
        try {
            index = Integer.parseInt(stIndex);
            Optional<Survey> optSurvey = surveyRepository.findById(index);
            if (optSurvey.isPresent()) {
                model.addAttribute("survey", optSurvey.get());
                model.addAttribute("index", index);
                model.addAttribute("isError", 0);
                return rvalue + "surveyupdate";
            } else {
                return "error/404";
            }

        } catch (Exception e) {
            return "error/404";
        }
    }

    public String surveyList() {
        return rvalue + "surveylist";
    }

    public String surveyDetail(String stIndex, Model model) {
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
                return rvalue + "surveydetail";
            } else {
                return "error/404";
            }
        } catch (Exception ex) {
            return "error/404";
        }
    }

    public String surveySelection(String stIndex, Model model) {
        Integer index = 0;
        try {
            index = Integer.parseInt(stIndex);
            Optional<Survey> optSurvey = surveyRepository.findById(index);
            if (optSurvey.isPresent()) {
                model.addAttribute("surveySelection", new SurveySelection());
                return rvalue + "surveyselectionadd";
            } else {
                return "error/404";
            }
        } catch (Exception ex) {
            return "error/404";
        }
    }

    public String surveyAdd(Survey survey, BindingResult bindingResult, Model model) {
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

    public String surveyUpdate(Survey survey, BindingResult bindingResult, Model model, String stIndex) {
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

    public String surveySelectionAdd(SurveySelection surveySelection, BindingResult bindingResult, Model model, String stIndex) {
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
                surveySelection.setSurvey_selection_score(0);
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

    public String surveySelectionDelete(String stIndex, String stIndex2) {
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
