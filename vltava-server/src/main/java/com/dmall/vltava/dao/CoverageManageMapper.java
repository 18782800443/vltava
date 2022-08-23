package com.dmall.vltava.dao;

import com.dmall.vltava.domain.coverage.CoverageManage;
import com.dmall.vltava.utils.MyMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component("CoverageManageMapper")
public interface CoverageManageMapper extends MyMapper<CoverageManage> {

    @Select("select count(1) from coverage_manage where yn=1 and task_name=#{coverageTaskName}")
    Integer exist(@Param("coverageTaskName") String coverageTaskName);

    @Select({"<script>",
            "select * from coverage_manage",
            "where 1=1",
            "<when test='appId!=\"\" and appId!=null'>",
            "and app_id=#{appId}",
            "</when>",
            "<when test='taskName!=\"\" and taskName!=null'>",
            "and task_name=#{taskName}",
            "</when>",
            "order by modify_time desc",
            "</script>"})
    List<CoverageManage> filter(@Param("appId") Integer appId, @Param("taskName") String taskName);

    @Select("select id from coverage_manage where yn=1 and task_name=#{taskName}")
    Integer selectIdByTaskName(@Param("taskName") String taskName);

    @Select("select id,task_status,version from coverage_manage where yn=1 and task_status!=0")
    List<CoverageManage> getAllValid();

    @Select("select id,task_status,version from coverage_manage where yn=1 and id in (#{ids})")
    List<CoverageManage> getAllAgentReuqest(@Param("ids") String ids);

    @Select("select id,version,modify_time from coverage_manage where yn=1 and task_status=4")
    List<CoverageManage> getAllInvalid();
}