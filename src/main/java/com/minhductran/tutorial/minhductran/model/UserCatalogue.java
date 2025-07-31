package com.minhductran.tutorial.minhductran.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "user_catalogue")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UserCatalogue extends AbstractEntity{

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "publish", nullable = false)
    private Integer publish;

    @Column(name = "permissions", nullable = false)
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_catalogue_permission",
            joinColumns = @JoinColumn(name = "user_catalogue_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id")
    )
    @JsonManagedReference
    private Set<Permission> permissions = new HashSet<>();

    @Builder.Default
    @ManyToMany(mappedBy = "userCatalogues")
    @JsonBackReference
    private Set<User> users = new HashSet<>();
}
