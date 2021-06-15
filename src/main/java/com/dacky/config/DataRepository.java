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

import java.sql.PreparedStatement;
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

    public List getAllTempData() {
        List temps = jdbcTemplate1.query(Constants.SELECT_QUERY_TEMP,
                (rs, rowNum) -> new Temp(rs.getLong("id"), rs.getLong("id_column_value"),
                        rs.getString("id_column_name"), rs.getString("query_type"),
                        rs.getString("table_name"), rs.getTimestamp("last_modify")));
        return temps;
    }


    public boolean insertToDBEnd(Temp temp) {
        SqlRowSet sqlRowSet = getDataTableStart(temp);
        sqlRowSet.next();
        List<Product> products = Arrays.asList(new Product(sqlRowSet));
        try {
            jdbcTemplate2.batchUpdate("INSERT INTO product (id, data) VALUES( ?, ?)",
                    products, 2000, new ParameterizedPreparedStatementSetter<Product>() {
                        @Override
                        public void setValues(PreparedStatement ps, Product product) throws SQLException {
                            ps.setLong(1, product.getId());
                            ps.setString(2, product.getData());
                        }
                    });
            return true;
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return false;
    }

    private SqlRowSet getDataTableStart(Temp temp) {
        SqlRowSet sqlRowSet = jdbcTemplate1.queryForRowSet(Constants.SELECT_QUERY_PRODUCT + temp.getIdColumnValue());
        return sqlRowSet;
    }

    public boolean delete(List<Temp> temps) {
        jdbcTemplate1.batchUpdate(Constants.DELETE_QUERY_TEMP,
                temps, 2000, new ParameterizedPreparedStatementSetter<Temp>() {
                    @Override
                    public void setValues(PreparedStatement ps, Temp temp) throws SQLException {
                        ps.setLong(1, temp.getId());
                    }
                });
        return true;
    }
}
