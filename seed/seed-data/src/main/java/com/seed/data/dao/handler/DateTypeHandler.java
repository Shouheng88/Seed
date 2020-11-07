package com.seed.data.dao.handler;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

/**
 * Date type handler. The date will be saved in seconds in database.
 *
 * @author <a href="mailto:shouheng2015@gmail.com">Shouheng.W</a>
 * @version 1.0
 * @date 2020/11/6 21:20
 */
@MappedTypes(value = {Date.class})
public class DateTypeHandler extends BaseTypeHandler<Date> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Date parameter, JdbcType jdbcType) throws SQLException {
        ps.setLong(i, parameter.getTime()/1000);
    }

    @Override
    public Date getNullableResult(ResultSet rs, String columnName) throws SQLException {
        try {
            long millis = rs.getLong(columnName);
            return new Date(millis*1000);
        } catch (Exception ex) {
            return null;
        }
    }

    @Override
    public Date getNullableResult(ResultSet rs, int i) throws SQLException {
        if (rs.wasNull()) {
            return null;
        } else {
            long millis = rs.getLong(i);
            return new Date(millis*1000);
        }
    }

    @Override
    public Date getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        if (cs.wasNull()) {
            return null;
        } else {
            long millis = cs.getLong(columnIndex);
            return new Date(millis*1000);
        }
    }
}
