package com.minhductran.tutorial.minhductran.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_catalogue")
@NoArgsConstructor
@AllArgsConstructor
public class UserCatalogue extends AbstractEntity{

    @Column(name = "publish", nullable = false)
    private Integer publish;


}
