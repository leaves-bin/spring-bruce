package com.bruce.search.common;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.sql.*;

/**
 * 解决mybaits 转换ORACLE clob的
 * 并将此类配置在mybaits的xml文件中
 *
 * @author zyb
 */
public class MyClobTypeHandlerOracle extends BaseTypeHandler<Object> {

    private static final String DEFAULT_CHARSET = "UTF-8";

    @Override
    public Object getNullableResult(ResultSet arg0, String arg1)
            throws SQLException {
        Clob clob = (Clob) arg0.getClob(arg1);
        try {
            //把byte转化成string
            return (clob == null || clob.length() == 0) ? null : clob.getSubString((long) 1, (int) clob.length());
        } catch (Exception e) {
            throw new RuntimeException("Blob Encoding Error!");
        }
    }


    @Override
    public Object getNullableResult(CallableStatement arg0, int arg1)
            throws SQLException {
        Clob clob = (Clob) arg0.getClob(arg1);
        byte[] returnValue = null;
        if (null != clob) {
            returnValue = clob.toString().getBytes();
        }
        try {
            return new String(returnValue, DEFAULT_CHARSET);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Blob Encoding Error!");
        }
    }

    @Override
    public void setNonNullParameter(PreparedStatement arg0, int arg1,
                                    Object arg2, JdbcType arg3) throws SQLException {
        ByteArrayInputStream bis;
        try {
            //把String转化成byte流
            bis = new ByteArrayInputStream(((String) arg2).getBytes(DEFAULT_CHARSET));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Blob Encoding Error!");
        }
        arg0.setBinaryStream(arg1, bis, ((String) arg2).length());
    }


    @Override
    public Object getNullableResult(ResultSet arg0, int arg1)
            throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

}
