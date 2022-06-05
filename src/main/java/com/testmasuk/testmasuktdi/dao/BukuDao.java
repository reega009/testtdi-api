package com.testmasuk.testmasuktdi.dao;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import com.tabeldata.sipening.api.utils.QueryComparator;

import com.maryanto.dimas.plugins.web.commons.ui.datatables.DataTablesRequest;
import com.maryanto.dimas.plugins.web.commons.ui.datatables.OrderingByColumns;
import com.testmasuk.testmasuktdi.model.entity.Buku;

import com.testmasuk.testmasuktdi.repository.BukuRepository;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class BukuDao {
    
    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    private BukuRepository bukuRepository;

    public List<Buku> findAll(){
        return bukuRepository.findAll();
    }

    public Optional<Buku> findById(BigDecimal id){
        return bukuRepository.findById(id);
    }

    public Buku save(Buku value){
        return bukuRepository.save(value);
    }

    public void removeById(BigDecimal id){
        bukuRepository.deleteById(id);
    }

    public List<Buku> datatables(DataTablesRequest<Buku> params) {
        String baseQuery = "SELECT i_id, id_provinsi, n_kota, i_pgun_rekam, d_pgun_rekam, i_pgun_ubah, d_pgun_ubah \n" +
                "FROM trkota where 1 = 1 \n";
        MapSqlParameterSource mapSqlParameters = new MapSqlParameterSource();
        DataTablesMapping dt = new DataTablesMapping(baseQuery, mapSqlParameters);
        mapSqlParameters = dt.getParameters();
        StringBuilder query = dt.getQuery(params.getValue());

        OrderingByColumns columns = new OrderingByColumns("i_id", "n_kota");
        String orderBy = columns.orderBy(params.getColDir(), params.getColOrder());
        query.append(orderBy);

        if (params.getLength() >= 0) {
            query.append(" limit :limit offset :offset ");
            mapSqlParameters.addValue("limit", params.getLength());
            mapSqlParameters.addValue("offset", params.getStart());
        }

        return jdbcTemplate.query(query.toString(), mapSqlParameters, new BeanPropertyRowMapper<>(Buku.class));
    }

    public Long datatables(Buku value) {
        String baseQuery = "select count(*) as rows\n" +
                "from trkota\n" +
                "where 1 = 1 ";
        MapSqlParameterSource mapSqlParameters = new MapSqlParameterSource();
        DataTablesMapping dt = new DataTablesMapping(baseQuery, mapSqlParameters);
        mapSqlParameters = dt.getParameters();
        StringBuilder query = dt.getQuery(value);

        return jdbcTemplate.queryForObject(query.toString(), mapSqlParameters, (resultSet, i) -> resultSet.getLong(1));
    }

    private class DataTablesMapping implements QueryComparator<Buku> {

        private final MapSqlParameterSource parameter;
        private final StringBuilder builder;

        public DataTablesMapping(String query, MapSqlParameterSource parameterSource) {
            this.parameter = parameterSource;
            this.builder = new StringBuilder(query);
        }

        @Override
        public StringBuilder getQuery(Buku value) {

            if (value.getNamaBuku() != null) {
                this.builder.append(" and lower(n_kota) like lower(:n_kota) ");
                this.parameter.addValue("n_kota", "%" + value.getNamaBuku() + "%");
            }
            return this.builder;
        }

        @Override
        public MapSqlParameterSource getParameters() {
            return this.parameter;
        }
    }

}
