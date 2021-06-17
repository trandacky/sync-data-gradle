package com.dacky.service;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dacky.repository.Constants;
import com.dacky.repository.DataRepository;
import com.dacky.entity.Temp;

/**
 * this class to running service sync data
 */
@Service
@EnableScheduling
public class AppService {
    @Autowired
    private DataRepository dataRepository;

    /**
     * if failed in query then use Transactional to rollback
     * set time in Scheduled fixedRate in there is 0.5s
     */
    @Scheduled(fixedRate = 500)
    @Transactional
    public void run() throws SQLException {
        List<Temp> temps = dataRepository.getAllTempDB1();
        int size = temps.size();
        String queries[] = new String[size];
        for (int i = 0; i < size; i++) {
            switch (temps.get(i).getQueryType()) {
                case Constants.QUERY_INSERT: {
                    System.out.println("insert *************");
                    ResultSet resultSet = dataRepository.findDattaDB1(temps.get(i));
                    resultSet.next();
                    queries[i] = insertQuery(resultSet);
                }
                break;
                case Constants.QUERY_UPDATE: {
                    System.out.println("update *************");
                    ResultSet resultSet = dataRepository.findDattaDB1(temps.get(i));
                    resultSet.next();
                    queries[i] = updateQuery(resultSet);
                }
                break;
                case Constants.QUERY_DELETE:
                    System.out.println("delete *************");
                    queries[i] = deleteQuery(temps.get(i));
                    break;
                default:
                    System.out.println("invalid *************");
                    break;
            }
        }
        dataRepository.deleteTempDB1(temps);
        if (size > 0) {
            dataRepository.executeQueryDB2(queries);
        }

    }
    /**
     * FillData to set data in query
     * */
    /**
     * INSERT INTO product SET id = ... , value = ...
     */
    private String insertQuery(ResultSet resultSet) throws SQLException {
        String query = "INSERT INTO ";
        ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
        query = query + resultSetMetaData.getTableName(1);
        int columnCount = resultSetMetaData.getColumnCount();
        query = query + " SET ";
        for (int i = 1; i <= columnCount; i++) {
            query = query + resultSetMetaData.getColumnName(i) + " = " + FillData.getValueInsert(resultSet, i) + ",";
        }
        query = query.substring(0, query.length() - 1);
        return query;
    }

    /**
     * UPDATE product SET value = ... where id= ...
     */
    private String updateQuery(ResultSet resultSet) throws SQLException {
        String query = "UPDATE ";
        ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
        query = query + resultSetMetaData.getTableName(1) + " SET ";
        int columnCount = resultSetMetaData.getColumnCount();
        for (int i = 2; i <= columnCount; i++) {
            query = query + resultSetMetaData.getColumnName(i) + " = " + FillData.getValueInsert(resultSet, i) + ",";
        }
        query = query.substring(0, query.length() - 1);
        query = query + " WHERE " + resultSetMetaData.getColumnName(1) + " = " + resultSet.getLong(1);
        return query;
    }

    /**
     * DELETE FROM product WHERE id = ...
     */
    private String deleteQuery(Temp temp) {
        return "DELETE FROM " + temp.getTableName() + " WHERE " + temp.getIdColumnName() + " = "
                + temp.getIdColumnValue();
    }

}
