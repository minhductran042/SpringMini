package com.minhductran.tutorial.minhductran.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "emails")
@Getter
@Setter
public class Email extends AbstractEntity{
    @Column(name = "text", nullable = false)
    private String text;

    @Column(name = "subject", nullable = false)
    private String subject;

    @Column(name = "to_address", nullable = false)
    private String to;
}