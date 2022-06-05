package com.testmasuk.testmasuktdi.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.testmasuk.testmasuktdi.model.entity.Buku;

public interface BukuRepository extends CrudRepository<Buku, BigDecimal> {

    List<Buku> findAll();
    
}
