package com.seed.data.dao.handler;

import com.seed.base.model.enums.DeviceType;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * DeviceTypeTypeHandler
 *
 * @author <a href="mailto:shouheng2015@gmail.com">Shouheng.W</a>
 * @version 1.0 
 * @date 2020/11/06 08:54
 */
@MappedTypes(value = {DeviceType.class})
public class DeviceTypeTypeHandler extends BaseTypeHandler<DeviceType> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, DeviceType parameter, JdbcType jdbcType) throws SQLException {
        ps.setInt(i, parameter.id);
    }

    @Override
    public DeviceType getNullableResult(ResultSet rs, String columnName) throws SQLException {
        try {
            int id = rs.getInt(columnName);
            return DeviceType.getTypeById(id);
        } catch (Exception ex) {
            return DeviceType.UNKNOWN;
        }
    }

    @Override
    public DeviceType getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        if (rs.wasNull()) {
            return DeviceType.UNKNOWN;
        } else {
            int id = rs.getInt(columnIndex);
            return DeviceType.getTypeById(id);
        }
    }

    @Override
    public DeviceType getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        if (cs.wasNull()) {
            return DeviceType.UNKNOWN;
        } else {
            int id = cs.getInt(columnIndex);
            return DeviceType.getTypeById(id);
        }
    }
}
