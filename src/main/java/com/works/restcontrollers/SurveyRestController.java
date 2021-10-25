package com.works.restcontrollers;

import com.works.entities.security.User;
import com.works.entities.survey.Survey;
import com.works.entities.survey.SurveySelection;
import com.works.entities.survey.SurveyVote;
import com.works.repositories._elastic.SurveyElasticRepository;
import com.works.repositories._jpa.SurveyRepository;
import com.works.repositories._jpa.SurveySelectionRepository;
import com.works.repositories._jpa.SurveyVoteRepository;
import com.works.repositories._jpa.UserRepository;
import com.works.repositories._redis.SurveySessionRepository;
import com.works.utils.REnum;
import com.works.utils.Util;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/rest/admin/survey")
public class SurveyRestController {

    final SurveyRepository surveyRepository;
    final SurveySessionRepository surveySessionRepository;
    final SurveyElasticRepository surveyElasticRepository;

    final UserRepository userRepository;
    final SurveySelectionRepository surveySelectionRepository;
    final SurveyVoteRepository surveyVoteRepository;

    public SurveyRestController(SurveySessionRepository surveySessionRepository, SurveyRepository surveyRepository, SurveyElasticRepository surveyElasticRepository, UserRepository userRepository, SurveySelectionRepository surveySelectionRepository, SurveyVoteRepository surveyVoteRepository) {
        this.surveySessionRepository = surveySessionRepository;
        this.surveyRepository = surveyRepository;
        this.surveyElasticRepository = surveyElasticRepository;
        this.userRepository = userRepository;
        this.surveySelectionRepository = surveySelectionRepository;
        this.surveyVoteRepository = surveyVoteRepository;
    }

    //******************************* REST API *********************************
    //ELASTIC
    @GetMapping("/list/{stSearchKey}/{stIndex}")
    public Map<REnum, Object> surveyListSearch(@RequestBody @PathVariable String stSearchKey, @PathVariable String stIndex) {
        Map<REnum, Object> hm = new LinkedHashMap<>();
        hm.put(REnum.MESSAGE, "Başarılı");
        hm.put(REnum.STATUS, true);
        hm.put(REnum.RESULT, surveyElasticRepository.findBySurvey_title(stSearchKey + " " + Util.theCompany.getCompany_name(), PageRequest.of(Integer.parseInt(stIndex) - 1, Util.pageSize)));
        int additional = 0;
        Integer size = surveyElasticRepository.findBySurvey_title(stSearchKey + " " + Util.theCompany.getCompany_name()).size();
        // dogrulama icin
        System.out.println("*********" + size + "-->" + stSearchKey + " " + Util.theCompany.getCompany_name());
        if (size % Util.pageSize != 0) {
            additional = 1;
        }
        hm.put(REnum.ERROR, null);
        hm.put(REnum.COUNTOFPAGE, size / Util.pageSize + additional);
        return hm;
    }

    //REDIS
    @GetMapping("/list/{stIndex}")
    public Map<REnum, Object> surveyList(@RequestBody @PathVariable String stIndex) {
        Map<REnum, Object> hm = new LinkedHashMap<>();
        hm.put(REnum.MESSAGE, "Başarılı");
        hm.put(REnum.STATUS, true);
        if (stIndex.equals("0")) {
            hm.put(REnum.RESULT, surveySessionRepository.findByCompanynameEquals(Util.theCompany.getCompany_name(), PageRequest.of(Integer.parseInt(stIndex), Util.pageSize)));
        } else {
            hm.put(REnum.RESULT, surveySessionRepository.findByCompanynameEquals(Util.theCompany.getCompany_name(), PageRequest.of(Integer.parseInt(stIndex) - 1, Util.pageSize)));
        }
        int additional = 0;
        Integer totalSize = surveySessionRepository.findByCompanynameEquals(Util.theCompany.getCompany_name()).size();
        if (totalSize % 10 != 0) {
            additional = 1;
        }
        hm.put(REnum.COUNTOFPAGE, (totalSize / Util.pageSize) + additional);
        return hm;
    }

    @DeleteMapping("/delete/{stIndex}")
    public Map<REnum, Object> surveyDelete(@RequestBody @PathVariable String stIndex) {
        Map<REnum, Object> hm = new LinkedHashMap<>();
        hm.put(REnum.MESSAGE, "Başarılı");
        surveyRepository.deleteById(Integer.valueOf(stIndex));
        surveySessionRepository.deleteById(stIndex);
        surveyElasticRepository.deleteById(stIndex);
        hm.put(REnum.STATUS, true);
        return hm;
    }

    // *************************** Mvc-Pageable ***********************************
    //ELASTIC-DataTable
    @GetMapping("/datatable/list/{stSearchKey}")
    public Map<REnum, Object> surveyPageListSearch(HttpServletRequest request, @PathVariable String stSearchKey) {
        Map<String, String[]> allMap = request.getParameterMap();

        Map<REnum, Object> hm = new LinkedHashMap<>();
        hm.put(REnum.MESSAGE, "Başarılı");
        hm.put(REnum.STATUS, true);

        int validPage = Integer.parseInt(allMap.get("start")[0]) == 0 ? 0 : (Integer.parseInt(allMap.get("start")[0])) / Integer.parseInt(allMap.get("length")[0]);
        hm.put(REnum.RESULT, (surveyElasticRepository.findBySurvey_title(stSearchKey + " " + Util.theCompany.getCompany_name(), PageRequest.of(validPage, Integer.parseInt(allMap.get("length")[0])))).getContent());
        Integer totalCount = surveyElasticRepository.findBySurvey_title(stSearchKey + " " + Util.theCompany.getCompany_name()).size();
        System.out.println("TOTAL -->" + totalCount);
        hm.put(REnum.ERROR, null);
        hm.put(REnum.COUNT, totalCount);
        hm.put(REnum.DRAW, Integer.parseInt(allMap.get("draw")[0]));
        return hm;
    }

    //REDIS-DataTable
    @GetMapping("/datatable/list")
    public Map<REnum, Object> surveyPageList(HttpServletRequest request) {
        Map<String, String[]> allMap = request.getParameterMap();
        Map<REnum, Object> hm = new LinkedHashMap<>();
        hm.put(REnum.STATUS, true);
        hm.put(REnum.MESSAGE, "Başarılı");
        int validPage = Integer.parseInt(allMap.get("start")[0]) == 0 ? 0 : (Integer.parseInt(allMap.get("start")[0])) / Integer.parseInt(allMap.get("length")[0]);

        hm.put(REnum.RESULT, surveySessionRepository.findByCompanynameEquals(Util.theCompany.getCompany_name(), PageRequest.of(validPage, Integer.parseInt(allMap.get("length")[0]))));
        int filterCount = surveySessionRepository.findByCompanynameEquals(Util.theCompany.getCompany_name()).size();
        hm.put(REnum.COUNT, filterCount);
        hm.put(REnum.DRAW, Integer.parseInt(allMap.get("draw")[0]));
        return hm;
    }


    //Oy verme
    @GetMapping("/vote/{stCustomer}/{stSurvey}/{stSelection}")
    public Map<REnum, Object> vote(@PathVariable String stCustomer, @PathVariable String stSurvey, @PathVariable String stSelection) {
        Map<REnum, Object> hm = new LinkedHashMap<>();
        Integer customerId;
        Integer surveyId;
        Integer selectionId;
        try {
            customerId = Integer.parseInt(stCustomer);
            surveyId = Integer.parseInt(stSurvey);
            selectionId = Integer.parseInt(stSelection);
        } catch (Exception e) {
            hm.put(REnum.STATUS, false);
            hm.put(REnum.MESSAGE, "Değişkenler tam sayı olmalıdır.");
            return hm;
        }

        Optional<User> optionalUser = userRepository.findById(customerId);
        Optional<Survey> optionalSurvey = surveyRepository.findById(surveyId);
        Optional<SurveySelection> optionalSurveySelection = surveySelectionRepository.findById(selectionId);

        if (!optionalUser.isPresent() || optionalUser.get().getRoles().get(0).getRo_id() != 3 || !optionalSurvey.isPresent() || optionalUser.get().getCompany().getId() != optionalSurvey.get().getCompany().getId() || !optionalSurveySelection.isPresent()) {
            hm.put(REnum.STATUS, false);
            hm.put(REnum.MESSAGE, "Hatalı Bilgi Girildi. Oy kullanılamadı.");
            return hm;
        }

        SurveyVote surveyVote = new SurveyVote();
        surveyVote.setSurveySelection(optionalSurveySelection.get());
        surveyVote.setCustomer(optionalUser.get());
        surveyVoteRepository.save(surveyVote);

        optionalSurveySelection.get().setSurvey_selection_score(optionalSurveySelection.get().getSurvey_selection_score() + 1);
        surveySelectionRepository.saveAndFlush(optionalSurveySelection.get());

        hm.put(REnum.STATUS, true);
        hm.put(REnum.MESSAGE, "İşlem Başarılı");
        hm.put(REnum.RESULT, surveyVote);

        return hm;
    }
}