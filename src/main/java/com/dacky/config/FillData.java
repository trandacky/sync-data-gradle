package com.dacky.config;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.Objects;

import javax.xml.bind.DatatypeConverter;

/**
 * this class fill data and return data to set value in query with getValueInsert fuction
 */
public class FillData {
    public static final String SQL_VALUE_NULL = "NULL";
    public static String getValueInsert(ResultSet resultSet, int columnPosition) throws SQLException {
        int dbType = resultSet.getMetaData().getColumnType(columnPosition);
        String value;
        switch (dbType) {
            case Types.BOOLEAN:
            case Types.BIT:
            case Types.DISTINCT:
            case Types.INTEGER:
            case Types.SMALLINT:
            case Types.TINYINT:
                value = sqlNumber(resultSet.getInt(columnPosition));
                break;
            case Types.BIGINT:
            case Types.NUMERIC:
                value = sqlNumber(resultSet.getLong(columnPosition));
                break;
            case Types.DECIMAL:
                value = sqlNumber(resultSet.getBigDecimal(columnPosition));
                break;
            case Types.DOUBLE:
                value = sqlNumber(resultSet.getDouble(columnPosition));
                break;
            case Types.REAL:
            case Types.FLOAT:
                float floatValue = resultSet.getFloat(columnPosition);
                value = sqlNumber(resultSet.wasNull() ? null : floatValue);
                break;
            case Types.BLOB:
            case Types.BINARY:
            case Types.VARBINARY:
                value = sqlBytes(resultSet.getBytes(columnPosition));
                break;
            case Types.TIME:
            case Types.TIME_WITH_TIMEZONE:
                value = sqlTime(resultSet.getTime(columnPosition), getTimeFormat());
                break;
            case Types.TIMESTAMP:
            case Types.TIMESTAMP_WITH_TIMEZONE:
                value = sqlTimestamp(resultSet.getTimestamp(columnPosition), getDateTimeFormat());
                break;
            case Types.DATE:
                value = sqlDate(resultSet.getDate(columnPosition), getDateFormat());
                break;
            default:
                return sqlString(resultSet.getString(columnPosition));
        }
        return value;
    }

    public static String sqlString(String query) {
        if (query == null) {
            return SQL_VALUE_NULL;
        }
        query = query.replaceAll("'", "''");
        return "'" + query + "'";
    }

    public static String sqlFormatDate(java.util.Date date, String format) {
        return String.format("'%s'", formatDate(date, format));
    }

    public static String sqlDate(java.sql.Date date, String format) {
        if (date == null) {
            return SQL_VALUE_NULL;
        }
        return sqlFormatDate(date, format);
    }

    public static String sqlTime(Time time, String format) {
        if (time == null) {
            return SQL_VALUE_NULL;
        }
        return sqlFormatDate(time, format);
    }

    public static String sqlTimestamp(Timestamp timestamp, String format) {
        if (timestamp == null) {
            return SQL_VALUE_NULL;
        }
        return sqlFormatDate(timestamp, format);
    }

    public static String sqlNumber(Number value) {
        if (value == null) {
            return SQL_VALUE_NULL;
        }
        return String.valueOf(value);
    }

    public static String sqlBytes(byte[] bytes) {
        if (Objects.isNull(bytes)) {
            return SQL_VALUE_NULL;
        }
        return DatatypeConverter.printHexBinary(bytes);
    }

    public static String formatDate(java.util.Date date, String pattern) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        return simpleDateFormat.format(date);
    }

    private static String getDateFormat() {
        return "yyyy-MM-dd";
    }


    private static String getTimeFormat() {
        return "HH:mm:ss";
    }


    private static String getDateTimeFormat() {
        return "yyyy-MM-dd HH:mm:ss";
    }
}
