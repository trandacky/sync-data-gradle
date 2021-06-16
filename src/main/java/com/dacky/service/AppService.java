package com.dacky.service;


import com.dacky.config.Constants;
import com.dacky.config.DataRepository;
import com.dacky.config.FillData;
import com.dacky.entity.Temp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.jdbc.support.rowset.SqlRowSetMetaData;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class AppService {

    @Autowired
    private DataRepository dataRepository;

    @Scheduled(fixedRate = 50000)
    public void run() throws SQLException {
        List<Temp> temps = dataRepository.getAllTempData();
        List<String> queries = new ArrayList<>();
        int size = temps.size();
        for (Temp temp : temps) {
            switch (temp.getQueryType()) {
                case Constants.QUERY_INSERT: {
                    System.out.println("insert *************");
                    SqlRowSet sqlRowSet = dataRepository.find(temp);
                    sqlRowSet.next();
                    queries.add(insertQuery(sqlRowSet));
                }
                break;
                case Constants.QUERY_UPDATE: {
                    System.out.println("update *************");
                    SqlRowSet sqlRowSet = dataRepository.find(temp);
                    sqlRowSet.next();
                    queries.add(updateQuery(sqlRowSet));
                }
                break;
                case Constants.QUERY_DELETE:
                    System.out.println("delete *************");
                    queries.add(deleteQuery(temp));
                    break;
                default:
                    System.out.println("invalid *************");
                    break;
            }
        }
        dataRepository.executeQuery(queries);
//        dataRepository.delete(temps);
    }

    private String insertQuery(SqlRowSet sqlRowSet) throws SQLException {
        String query = "INSERT INTO";
        String value = "";
        SqlRowSetMetaData sqlRowSetMetaData = sqlRowSet.getMetaData();
        query = query + sqlRowSetMetaData.getTableName(1) + " (";
        int columnCount = sqlRowSetMetaData.getColumnCount();
        for (int i = 1; i <= columnCount; i++) {
            query = query + sqlRowSetMetaData.getColumnName(i) + ", ";

        }
        query = query.substring(0, query.length() - 1);
        query = query + ") VALUES (";
        for (int i = 1; i <= columnCount; i++) {
            query = query + FillData.getValueInsert(sqlRowSet, i) + ",";
        }
        query = query.substring(0, query.length() - 1);
        query = query + ")";
        return query;
    }

    private String updateQuery(SqlRowSet sqlRowSet) throws SQLException {
        String query = "UPDATE ";
        SqlRowSetMetaData sqlRowSetMetaData = sqlRowSet.getMetaData();
        query = query + sqlRowSetMetaData.getTableName(1) + " SET ";
        int columnCount = sqlRowSetMetaData.getColumnCount();
        for (int i = 2; i <= columnCount; i++) {
            query = query + sqlRowSetMetaData.getColumnName(i) + " = " + FillData.getValueInsert(sqlRowSet, i) + ",";
        }
        query = query.substring(0, query.length() - 1);
        query = query + " WHERE " + sqlRowSetMetaData.getColumnName(1) + " = " + sqlRowSet.getLong(1);
        return query;
    }

    private String deleteQuery(Temp temp) {
        return "DELETE FROM " + temp.getTableName() +
                " WHERE " + temp.getIdColumnName() + " = " + temp.getIdColumnValue();
    }

}


