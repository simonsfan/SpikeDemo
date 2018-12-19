package com.spike.demo.mapper;


import com.spike.demo.bean.ProOrder;
import org.apache.ibatis.annotations.Mapper;
@Mapper
public interface ProOrderMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ProOrder record);

    int insertSelective(ProOrder record);

    ProOrder selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ProOrder record);

    int updateByPrimaryKey(ProOrder record);
}
