package com.ai.ecs.ecm.mall.wap.modules.o2o;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by think on 2017/9/21.
 */
@Controller
@RequestMapping("o2oSpeedSurvey")
public class O2oSpeedSurveyController {

    @RequestMapping("toSpeedSurveyIndex")
    public String toSpeedSurveyIndex(Model model){
        return "web/broadband/o2o/speedSurvey/speedSurveyIndex";
    }
}
