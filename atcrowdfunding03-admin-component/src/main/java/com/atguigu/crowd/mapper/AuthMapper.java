package com.atguigu.crowd.mapper;

import com.atguigu.crowd.entity.Auth;
import com.atguigu.crowd.entity.AuthExample;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface AuthMapper {
    int countByExample(AuthExample example);

    int deleteByExample(AuthExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Auth record);

    int insertSelective(Auth record);

    List<Auth> selectByExample(AuthExample example);

    Auth selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Auth record, @Param("example") AuthExample example);

    int updateByExample(@Param("record") Auth record, @Param("example") AuthExample example);

    int updateByPrimaryKeySelective(Auth record);

    int updateByPrimaryKey(Auth record);

    List<Integer> selectAuthIdByRoleId(Integer roleId);

    void insertRoleRelationship(@Param("id") Integer roleId, @Param("list") List<Integer> list);

    void deleteRelationShip(Integer roleId);

    List<String> selectAuthByAdminId(Integer adminId);
}