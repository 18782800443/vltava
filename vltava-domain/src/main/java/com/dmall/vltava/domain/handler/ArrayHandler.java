package com.dmall.vltava.domain.handler;


import com.alibaba.fastjson.JSON;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * @author Rob
 * @date Create in 11:31 上午 2020/9/1
 */
public class ArrayHandler<T> extends BaseTypeHandler<List<T>> {
    private Class<T> clazz;

    public ArrayHandler(Class<T> clazz){
        this.clazz = clazz;
    }
    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, List<T> t, JdbcType jdbcType) throws SQLException {
        preparedStatement.setString(i,JSON.toJSONString(t));
    }

    @Override
    public List<T> getNullableResult(ResultSet resultSet, String s) throws SQLException {
        return JSON.parseArray(resultSet.getString(s),this.clazz);
    }

    @Override
    public List<T> getNullableResult(ResultSet resultSet, int i) throws SQLException {
        return JSON.parseArray(resultSet.getString(i),this.clazz);
    }

    @Override
    public List<T> getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        return JSON.parseArray(callableStatement.getString(i), this.clazz);
    }
}
