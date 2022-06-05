package com.testmasuk.testmasuktdi.dao;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.maryanto.dimas.plugins.web.commons.ui.datatables.DataTablesRequest;
import com.maryanto.dimas.plugins.web.commons.ui.datatables.OrderingByColumns;
import com.testmasuk.testmasuktdi.model.entity.Buku;

import com.testmasuk.testmasuktdi.repository.BukuRepository;
import com.testmasuk.testmasuktdi.utils.LimitOffsetPageable;
import com.testmasuk.testmasuktdi.utils.QueryComparator;

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

    public List<Buku> datatables(DataTablesRequest<Buku> params) throws DataAccessException {
        MapSqlParameterSource map = new MapSqlParameterSource();
        OrderingByColumns serviceColumn = new OrderingByColumns("i_id", "nama_buku");

        //language=MYSQL-SQL
        String baseQuery = "select ROW_NUMBER() over (%s) as no,\n" +
                "    i_id           as id, \n" +
                "    nama_buku        as namaBuku, \n" +
                "from tbl_buku where 1 = 1 ";
        BukuDao.BukuQueryComparator queryComparator = new BukuDao.BukuQueryComparator(
                String.format(baseQuery,
                        serviceColumn.orderBy(
                                params.getColDir(),
                                params.getColOrder()
                        )
                ),
                map);
        StringBuilder query = queryComparator.getQuery(params.getValue());
        map = queryComparator.getParameters();

        LimitOffsetPageable limitOffset = new LimitOffsetPageable(map);
        map = limitOffset.parameter(params.getStart(), params.getLength());
        String finalQuery = limitOffset.query(query.toString(), "no");

        if (log.isDebugEnabled())
            log.debug("query : {}", finalQuery);

        List<Buku> list = this.jdbcTemplate.query(finalQuery, map, new BeanPropertyRowMapper<>(Buku.class));
        return list;
    }

    public Long datatables(Buku value) {
        //language=MYSQL-SQL
        String baseQuery = "select count(*) as row_count from tbl_buku where 1=1 ";
        MapSqlParameterSource map = new MapSqlParameterSource();

        BukuDao.BukuQueryComparator queryComparator = new BukuDao.BukuQueryComparator(baseQuery, map);
        StringBuilder query = queryComparator.getQuery(value);
        map = queryComparator.getParameters();

        return this.jdbcTemplate.queryForObject(
                query.toString(), map,
                (resultSet, i) -> resultSet.getLong("row_count")
        );
    }

    private class BukuQueryComparator implements QueryComparator<Buku> {

        private StringBuilder builder;
        private MapSqlParameterSource parameterSource;

        public BukuQueryComparator(String baseQuery, MapSqlParameterSource parameterSource) {
            this.builder = new StringBuilder(baseQuery);
            this.parameterSource = parameterSource;
        }

        @Override
        public StringBuilder getQuery(Buku value) {
            if (value.getNamaBuku() != null) {
                this.builder.append(" and lower(nama_buku) like lower(:nama_buku) ");
                this.parameterSource.addValue("nama_buku", "%" + value.getNamaBuku() + "%");
            }
            return this.builder;
        }

        @Override
        public MapSqlParameterSource getParameters() {
            return this.parameterSource;
        }
    }
}
