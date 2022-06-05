package com.testmasuk.testmasuktdi.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.maryanto.dimas.plugins.web.commons.ui.datatables.DataTablesRequest;
import com.maryanto.dimas.plugins.web.commons.ui.datatables.DataTablesResponse;
import com.testmasuk.testmasuktdi.dao.BukuDao;
import com.testmasuk.testmasuktdi.model.entity.Buku;

@Service
@Transactional
public class BukuService {
    
    @Autowired
    private BukuDao dao;

    public List<Buku> findAll(){
        return dao.findAll();
    }

    public Optional<Buku> findById(BigDecimal id){
        return dao.findById(id);
    }

    public Buku save(Buku value){
        return dao.save(value);
    }

    public void removeById(BigDecimal id){
        dao.removeById(id);
    }

    public DataTablesResponse<Buku> datatables(DataTablesRequest<Buku> params) throws DataAccessException {
        List<Buku> list = dao.datatables(params);
        Long rowCount = dao.datatables(params.getValue());
        return new DataTablesResponse<>(list, params.getDraw(), rowCount, rowCount);
    }

}
