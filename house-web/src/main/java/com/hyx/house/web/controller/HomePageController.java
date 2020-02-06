package com.hyx.house.web.controller;

import com.hyx.house.biz.service.RecommandService;
import com.hyx.house.common.model.House;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.util.List;

@Controller
public class HomePageController {

    @Resource
    private RecommandService recommandService;

    @RequestMapping("index")
    public String index(ModelMap modelMap){
        List<House> houses = recommandService.getLastest();
        modelMap.put("recomHouses", houses);
        return "/homepage/index";
    }

    @RequestMapping("")
    public String home(ModelMap modelMap){
        return "redirect:/index";
    }
}
