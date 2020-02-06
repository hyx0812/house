package com.hyx.house.biz.service;

import com.hyx.house.biz.mapper.AgencyMapper;
import com.hyx.house.common.model.User;
import com.hyx.house.common.page.PageData;
import com.hyx.house.common.page.PageParams;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class AgencyService {

    @Resource
    private AgencyMapper agencyMapper;


    private String imgPrefix="";

    public User getAgentDeail(Long userId) {
        User user = new User();
        user.setId(userId);
        user.setType(2);
        List<User> list = agencyMapper.selectAgent(user, PageParams.build(1,1));
        setImg(list);
        if (!list.isEmpty()){
            return list.get(0);
        }
        return null;
    }

    private void setImg(List<User> list) {
        list.forEach(i ->{
            i.setAvatar(imgPrefix + i.getAvatar());
        });
    }

    public PageData<User> getAllAgent(PageParams pageParams) {
        List<User> agents = agencyMapper.selectAgent(new User(), pageParams);
        setImg(agents);
        Long count = agencyMapper.selectAgentCount(new User());
        return PageData.buildPage(agents, count, pageParams.getPageSize(), pageParams.getPageNum());
    }
}
