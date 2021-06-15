package com.dacky.entity;

import org.springframework.jdbc.support.rowset.SqlRowSet;

public class Product {
    private Long id;
    private String data;

    public Product(SqlRowSet sqlRowSet) {
        this.id= sqlRowSet.getLong(1);
        this.data= sqlRowSet.getString(2);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
