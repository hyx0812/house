package com.hyx.house.web.controller;

import com.hyx.house.biz.service.AgencyService;
import com.hyx.house.biz.service.CityService;
import com.hyx.house.biz.service.HouseService;
import com.hyx.house.biz.service.RecommandService;
import com.hyx.house.common.constants.CommonConstants;
import com.hyx.house.common.constants.HouseUserType;
import com.hyx.house.common.model.House;
import com.hyx.house.common.model.HouseUser;
import com.hyx.house.common.model.User;
import com.hyx.house.common.model.UserMsg;
import com.hyx.house.common.page.PageData;
import com.hyx.house.common.page.PageParams;
import com.hyx.house.common.result.ResultMsg;
import com.hyx.house.web.interceptor.UserContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

@Controller
public class HouseController {
    @Resource
    private HouseService houseService;

    @Resource
    private AgencyService agencyService;

    @Resource
    private RecommandService recommandService;

    @Resource
    private CityService cityService;



    /**
     * 1. 实现分页
     * 2. 支持小区搜索、类型搜索
     * 3. 支持排序
     * 4. 支持展示图片、价格、标题、地址等信息
     * @return
     */
    @RequestMapping("/house/list")
    public String houseList(Integer pageSize, Integer pageNum, House query, ModelMap modelMap){
        PageData<House> ps = houseService.queryHouse(query, PageParams.build(pageSize, pageNum));
        List<House> hotHouses = recommandService.getHotHouse(CommonConstants.RECOM_SIZE);
        modelMap.put("recomHouses", hotHouses);
        modelMap.put("ps", ps);
        modelMap.put("vo", query);
        return "house/listing";
    }

    @RequestMapping("/house/toAdd")
    public String toAdd(ModelMap modelMap) {
        modelMap.put("citys", cityService.getAllCitys());
        modelMap.put("communitys", houseService.getAllCommunitys());
        return "/house/add";
    }

    @RequestMapping("/house/add")
    public String doAdd(House house){
        User user = UserContext.getUser();
        house.setState(CommonConstants.HOUSE_STATE_UP);
        houseService.addHouse(house,user);
        return "redirect:/house/ownlist";
    }

    @RequestMapping("house/ownlist")
    public String ownlist(House house,Integer pageNum,Integer pageSize,ModelMap modelMap){
        User user = UserContext.getUser();
        house.setUserId(user.getId());
        house.setBookmarked(false);
        modelMap.put("ps", houseService.queryHouse(house, PageParams.build(pageSize, pageNum)));
        modelMap.put("pageType", "own");
        return "/house/ownlist";
    }


    /**
     * 查询房屋详情
     * 查询关联的经纪人
     * @param id
     * @param modelMap
     * @return
     */
    @RequestMapping("house/detail")
    public String houseDetail(Long id, ModelMap modelMap){
        House house = houseService.queryOneHouse(id);
        HouseUser houseUser = houseService.getHouseUser(id);
        recommandService.increase(id);
        if (houseUser.getUserId() != null && !houseUser.getUserId().equals(0)) {
            modelMap.put("agent", agencyService.getAgentDeail(houseUser.getUserId()));
        }
        List<House> rcHouses =  recommandService.getHotHouse(CommonConstants.RECOM_SIZE);
        modelMap.put("recomHouses", rcHouses);
        modelMap.put("house", house);
        return "/house/detail";
    }

    @RequestMapping("house/leaveMsg")
    public String houseMsg(UserMsg userMsg){
        houseService.addUserMsg(userMsg);
        return "redirect:/house/detail?id=" + userMsg.getHouseId();
    }

    // 评分
    @ResponseBody
    @RequestMapping("house/rating")
    public ResultMsg houseRate(Double rating, Long id){
        houseService.updateRating(id, rating);
        return ResultMsg.successMsg("ok");
    }
//    // 收藏
    @ResponseBody
    @RequestMapping("house/bookmark")
    public ResultMsg bookmark(Long id){
        User user = UserContext.getUser();
        houseService.bindUser2House(id, user.getId(), true);
        return ResultMsg.successMsg("ok");
    }
    // 删除收藏
    @ResponseBody
    @RequestMapping("house/unbookmark")
    public ResultMsg unbookmark(Long id){
        User user = UserContext.getUser();
        houseService.unBndUser2House(id, user.getId(), HouseUserType.BOOKMARK);
        return ResultMsg.successMsg("ok");
    }

    @RequestMapping("house/del")
    public String delsale(Long id, String pageType){
        User user = UserContext.getUser();
        houseService.unBndUser2House(id, user.getId(), pageType.equals("own") ? HouseUserType.SALE : HouseUserType.BOOKMARK);
        return "redirect:/house/ownlist";
    }

    // 收藏列表
    @RequestMapping("house/bookmarked")
    public String bookmarked(House house, Integer pageNum, Integer pageSize, ModelMap modelMap){
        User user = UserContext.getUser();
        house.setBookmarked(true);
        house.setUserId(user.getId());
        modelMap.put("ps", houseService.queryHouse(house, PageParams.build(pageSize, pageNum)));
        modelMap.put("pageType", "book");
        return "/house/ownlist";
    }

}
