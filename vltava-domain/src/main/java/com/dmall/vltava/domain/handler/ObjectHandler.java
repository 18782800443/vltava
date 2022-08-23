package com.dmall.vltava.domain.handler;

import com.alibaba.fastjson.JSON;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Rob
 * @date Create in 11:31 上午 2020/9/1
 */
public class ObjectHandler<T> extends BaseTypeHandler<T> {

    private Class<T> clazz;

    public ObjectHandler(Class<T> clazz){
        this.clazz = clazz;
    }

    private String toJSONString(T t){
        return JSON.toJSONString(t);
    }

    private T toObject(String str){
        return JSON.parseObject(str, this.clazz);
    }

    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, T t, JdbcType jdbcType) throws SQLException {
        preparedStatement.setString(i,toJSONString(t));
    }

    @Override
    public T getNullableResult(ResultSet resultSet, String s) throws SQLException {
        return toObject(resultSet.getString(s));
    }

    @Override
    public T getNullableResult(ResultSet resultSet, int i) throws SQLException {
        return toObject(resultSet.getString(i));
    }

    @Override
    public T getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        return toObject(callableStatement.getString(i));
    }
}
