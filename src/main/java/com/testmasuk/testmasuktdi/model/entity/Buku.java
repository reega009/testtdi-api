package com.testmasuk.testmasuktdi.model.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_buku")
// @SequenceGenerator(
//         name = "tbl_buku_i_id_seq",
//         sequenceName = "tbl_buku_i_id_seq",
//         allocationSize = 1,
//         initialValue = 2)
public class Buku {

    @Id
    @Column(name = "i_id")
    // @GeneratedValue(generator = "tbl_buku_i_id_seq")
    private BigDecimal id;

    @Column(name = "nama_buku")
    private String namaBuku;
}