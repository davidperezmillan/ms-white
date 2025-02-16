package com.davidperezmillan.ms_black.infrastructure.bbdd.models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "`parameter`", uniqueConstraints = {@UniqueConstraint(columnNames = {"type", "key"})})
public class Parameter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String type;
    @Column(name = "`key`")
    private String key;

    private String value;

}
