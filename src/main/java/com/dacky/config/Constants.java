package com.dacky.config;

public class Constants {
    public static final String QUERY_INSERT = "INSERT";
    public static final String QUERY_UPDATE = "UPDATE";
    public static final String QUERY_DELETE = "DELETE";
    public static final String TABLE_TEMP_NAME = "temp";
    public static final int COUNT_QUERY = 1;

    public static final String SELECT_QUERY_TEMP = "SELECT * FROM " + TABLE_TEMP_NAME +" ORDER BY last_modify ASC LIMIT " + COUNT_QUERY;
    public static final String DELETE_QUERY_TEMP = "DELETE FROM temp WHERE id = ?";


}
