package com.dacky.entity;

import java.sql.Timestamp;

public class Temp {
    private Long id;
    private String idColumnName;
    private Long idColumnValue;
    private String queryType;
    private String tableName;
    private Timestamp lastModify;

    public Temp(Long id,Long idColumnValue, String idColumnName,  String queryType, String tableName, Timestamp lastModify) {
        this.id = id;
        this.idColumnName = idColumnName;
        this.idColumnValue = idColumnValue;
        this.queryType = queryType;
        this.tableName = tableName;
        this.lastModify = lastModify;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdColumnName() {
        return idColumnName;
    }

    public void setIdColumnName(String idColumnName) {
        this.idColumnName = idColumnName;
    }

    public Long getIdColumnValue() {
        return idColumnValue;
    }

    public void setIdColumnValue(Long idColumnValue) {
        this.idColumnValue = idColumnValue;
    }

    public String getQueryType() {
        return queryType;
    }

    public void setQueryType(String queryType) {
        this.queryType = queryType;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public Timestamp getLastModify() {
        return lastModify;
    }

    public void setLastModify(Timestamp lastModify) {
        this.lastModify = lastModify;
    }
}
