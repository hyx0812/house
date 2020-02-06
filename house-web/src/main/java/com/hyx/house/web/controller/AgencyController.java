package com.hyx.house.web.controller;

import com.hyx.house.biz.service.AgencyService;
import com.hyx.house.biz.service.HouseService;
import com.hyx.house.common.model.House;
import com.hyx.house.common.model.User;
import com.hyx.house.common.page.PageData;
import com.hyx.house.common.page.PageParams;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

@Controller
public class AgencyController {

    @Resource
    private AgencyService agencyService;

    @Resource
    private HouseService houseService;

    @RequestMapping("/agency/agentList")
    public String agentList(Integer pageSize, Integer pageNum, ModelMap modelMap){
        PageData<User> ps = agencyService.getAllAgent(PageParams.build(pageSize, pageNum));
        modelMap.put("ps", ps);
        return "/user/agent/agentList";
    }

    @RequestMapping("/agency/agentDetail")
    public String agentDetail(Long id, ModelMap modelMap){
            User user = agencyService.getAgentDeail(id);
        House query = new House();
        query.setUserId(id);
        query.setBookmarked(false);
        PageData<House> bindHouse = houseService.queryHouse(query, PageParams.build(3,1));
        if (bindHouse != null){
            modelMap.put("bindHouse", bindHouse);
        }
        modelMap.put("agent", user);
        return "/user/agent/agentDetail";
    }

}
