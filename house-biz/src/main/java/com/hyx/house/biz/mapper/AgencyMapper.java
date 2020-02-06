package com.hyx.house.biz.mapper;

import com.hyx.house.common.model.User;
import com.hyx.house.common.page.PageParams;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AgencyMapper {
    public List<User> selectAgent(@Param("user") User user, @Param("pageParams") PageParams pageParams);

    public Long selectAgentCount(@Param("user") User user);
}
