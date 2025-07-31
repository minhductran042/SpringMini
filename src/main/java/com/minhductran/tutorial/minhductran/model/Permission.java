package com.minhductran.tutorial.minhductran.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "permissions")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Permission extends AbstractEntity{

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "publish", nullable = false)
    private Integer publish;

    @ManyToMany(mappedBy = "permissions")
    @JsonBackReference
    private Set<UserCatalogue> userCatalogues = new HashSet<>();

}
