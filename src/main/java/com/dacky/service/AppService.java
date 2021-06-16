package com.dacky.service;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dacky.config.Constants;
import com.dacky.config.DataRepository;
import com.dacky.config.FillData;
import com.dacky.entity.Temp;

@Service
public class AppService {

	@Autowired
	private DataRepository dataRepository;

	@Scheduled(fixedRate = 500)
	@Transactional
	public void run() throws SQLException {
		List<Temp> temps = dataRepository.getAllTempData();
		int size = temps.size();
		String queries[] = new String[size];
		for (int i = 0; i < size; i++) {
			switch (temps.get(i).getQueryType()) {
			case Constants.QUERY_INSERT: {
				System.out.println("insert *************");
				ResultSet resultSet = dataRepository.find(temps.get(i));
				resultSet.next();
				queries[i] = insertQuery(resultSet);
			}
				break;
			case Constants.QUERY_UPDATE: {
				System.out.println("update *************");
				ResultSet resultSet = dataRepository.find(temps.get(i));
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
		dataRepository.delete(temps);
		if (size > 0) {
			dataRepository.executeQuery(queries);
		}
		
	}

	private String insertQuery(ResultSet resultSet) throws SQLException {
		String query = "INSERT INTO ";
		ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
		query = query + resultSetMetaData.getTableName(1) + " (";
		int columnCount = resultSetMetaData.getColumnCount();
		for (int i = 1; i <= columnCount; i++) {
			query = query + resultSetMetaData.getColumnName(i) + ",";

		}
		query = query.substring(0, query.length() - 1);
		query = query + ") VALUES (";
		for (int i = 1; i <= columnCount; i++) {
			query = query + FillData.getValueInsert(resultSet, i) + ",";
		}
		query = query.substring(0, query.length() - 1);
		query = query + ")";
		return query;
	}

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

	private String deleteQuery(Temp temp) {
		return "DELETE FROM " + temp.getTableName() + " WHERE " + temp.getIdColumnName() + " = "
				+ temp.getIdColumnValue();
	}

}
