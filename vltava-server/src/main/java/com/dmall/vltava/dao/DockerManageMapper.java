package com.dmall.vltava.dao;

import com.dmall.vltava.domain.DockerManage;
import com.dmall.vltava.utils.MyMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component("DockerManageMapper")
public interface DockerManageMapper extends MyMapper<DockerManage> {

    @Select("select * from docker_manage where system_unique_name=#{systemName} and zone=#{zone} and build_group=#{group}")
    DockerManage getExist(@Param("systemName") String name, @Param("zone") String zone, @Param("group") String group);

    @Select("select a.id, a.system_unique_name, a.zone, d.ip, d._port from app_manage as a left join docker_manage as d" +
            " on a.system_unique_name = d.system_unique_name and a.zone = d.zone and a.build_group = d.build_group where a.id in" +
            " (select distinct app_id from mock_manage where yn = 1 and task_status != 5)")
    List<DockerManage> getHealth();
}
