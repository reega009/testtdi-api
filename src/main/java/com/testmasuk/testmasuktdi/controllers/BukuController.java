package com.testmasuk.testmasuktdi.controllers;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.maryanto.dimas.plugins.web.commons.ui.datatables.DataTablesRequest;
import com.maryanto.dimas.plugins.web.commons.ui.datatables.DataTablesResponse;
import com.testmasuk.testmasuktdi.model.entity.Buku;
import com.testmasuk.testmasuktdi.service.BukuService;
import javax.validation.Valid;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/buku")
@Slf4j
public class BukuController {
    
    @Autowired
    private BukuService service;

    @GetMapping("/{id}")
    public ResponseEntity<Buku> findById(@PathVariable("id") BigDecimal id){
        Optional<Buku> findById = service.findById(id);
        return findById.map((ResponseEntity::ok)).orElseGet(() -> ResponseEntity.noContent().build());
    }

    @GetMapping("/findAll")
    public ResponseEntity<?> findAll(){
        List<Buku> findAll = service.findAll();
        return ResponseEntity.ok(findAll);
    }
    
    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody @Valid Buku value){
        try {
            Buku save = service.save(value);
            return ResponseEntity.ok(save);
        } catch (DataAccessException e) {
            return new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update")
    public ResponseEntity<?> update(@RequestBody @Valid Buku value){
        try {
            Buku save = service.save(value);
            return ResponseEntity.ok(save);
        } catch (DataAccessException e) {
            return new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @DeleteMapping("/delete/{id}")
    public void deleteById(@PathVariable("id") BigDecimal id){
        service.removeById(id);
    }

    @PostMapping("/datatables")
    @ResponseBody
    public DataTablesResponse<Buku> dataTables(
            @RequestParam(required = false, value = "draw", defaultValue = "0") Long draw,
            @RequestParam(required = false, value = "start", defaultValue = "0") Long start,
            @RequestParam(required = false, value = "length", defaultValue = "10") Long length,
            @RequestParam(required = false, value = "order[0][column]", defaultValue = "1") Long iSortCol0,
            @RequestParam(required = false, value = "order[0][dir]", defaultValue = "asc") String sSortDir0,
            @RequestBody(required = false) Buku params) {
        if (params == null) params = new Buku();
        log.info("draw: {}, start: {}, length: {}, type: {}", draw, start, length, params);
        return service.datatables(new DataTablesRequest<>(draw, length, start, sSortDir0, iSortCol0, params));
    }

}
