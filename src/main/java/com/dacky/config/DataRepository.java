package com.dacky.config;

import com.dacky.entity.Product;
import com.dacky.entity.Temp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.*;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.jdbc.support.rowset.SqlRowSetMetaData;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import javax.sql.RowSet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;


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
                        rs.getString("id_column_name"), rs.getString("query_type"),
                        rs.getString("table_name"), rs.getTimestamp("last_modify")));
        return temps;
    }
    public boolean delete(List<Temp> temps) {
        String sqlArr[] = new String[6];

        sqlArr[0] = "insert into student values('richard','richard@dev2qa.com')";
        sqlArr[1] = "insert into student values('jerry','jerry@dev2qa.com')";
        sqlArr[2] = "insert into teacher(name, email) values('tom','tom@gmail.com')";
        sqlArr[3] = "update teacher set email = 'hello@gmail.com' where name = 'hello'";
        sqlArr[4] = "insert into teacher(name, email) values('song','song@gmail.com')";
        sqlArr[5] = "insert into teacher(name, email) values('jerry','jerry@gmail.com')";
        List<String> list= Arrays.asList("asdasd","asdasd");
        list.stream().toArray();



        jdbcTemplate1.batchUpdate(Constants.DELETE_QUERY_TEMP,
                temps, Constants.COUNT_QUERY, new ParameterizedPreparedStatementSetter<Temp>() {
                    @Override
                    public void setValues(PreparedStatement ps, Temp temp) throws SQLException {
                        ps.setLong(1, temp.getId());
                    }
                });
        return true;
    }


    public SqlRowSet find(Temp temp) throws SQLException {
        String selectQuery1 = "SELECT * FROM ";
        String selectQuery2 = " WHERE ";
        String selectQuery3 = " = ";
        return jdbcTemplate1.queryForRowSet(selectQuery1 + temp.getTableName() + selectQuery2
                + temp.getIdColumnName() + selectQuery3 + temp.getIdColumnValue());
    }

    public void executeQuery(List<String> queries) {
        jdbcTemplate2.batchUpdate(queries.stream().toArray());
    }
}
