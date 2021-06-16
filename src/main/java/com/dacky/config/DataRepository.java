package com.dacky.config;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ParameterizedPreparedStatementSetter;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.dacky.entity.Temp;

@Transactional
@Repository
public class DataRepository {
	@Autowired
	@Qualifier("jdbcTemplate1")
	private JdbcTemplate jdbcTemplate1;
	@Autowired
	@Qualifier("jdbcTemplate2")
	private JdbcTemplate jdbcTemplate2;

	@Autowired
	@Qualifier("datasource1")
	private DataSource datasource1;

	public List getAllTempData() {
		List temps = jdbcTemplate1.query(Constants.SELECT_QUERY_TEMP,
				(rs, rowNum) -> new Temp(rs.getLong("id"), rs.getLong("id_column_value"),
						rs.getString("id_column_name"), rs.getString("query_type"), rs.getString("table_name"),
						rs.getTimestamp("last_modify")));
		return temps;
	}
	public boolean delete(List<Temp> temps) {
		jdbcTemplate1.batchUpdate(Constants.DELETE_QUERY_TEMP, temps, Constants.COUNT_QUERY,
				new ParameterizedPreparedStatementSetter<Temp>() {
					@Override
					public void setValues(PreparedStatement ps, Temp temp) throws SQLException {
						ps.setLong(1, temp.getId());
					}
				});
		return true;
	}

	public ResultSet find(Temp temp) throws SQLException {
		String selectQuery1 = "SELECT * FROM ";
		String selectQuery2 = " WHERE ";
		String selectQuery3 = " = ";
		Statement statement = datasource1.getConnection().createStatement();
		return statement.executeQuery(selectQuery1 + temp.getTableName() + selectQuery2 + temp.getIdColumnName()
				+ selectQuery3 + temp.getIdColumnValue());
	}
	public void executeQuery(String[] queries) {
		jdbcTemplate2.batchUpdate(queries);
	}
}
