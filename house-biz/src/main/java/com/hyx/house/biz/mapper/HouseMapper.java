package com.hyx.house.biz.mapper;

import com.hyx.house.common.model.*;
import com.hyx.house.common.page.PageParams;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface HouseMapper {

    public List<House> selectPageHouses(@Param("house") House house, @Param("pageParams") PageParams pageParams);

    public Long selectPageCount(@Param("house") House query);

    public List<Community> selectCommunity(Community community);

    public int insertUserMsg(UserMsg userMsg);

    public HouseUser selectSaleHouseUser(Long houseId);

    public int insert(House house);

    public HouseUser selectHouseUser(@Param("userId") Long userId, @Param("id") Long houseId, @Param("type") Integer integer);

    public int insertHouseUser(HouseUser houseUser);

    public int updateHouse(House updateHouse);

    public int deleteHouseUser(@Param("id") Long id, @Param("userId") Long userId, @Param("type") Integer type);
}
