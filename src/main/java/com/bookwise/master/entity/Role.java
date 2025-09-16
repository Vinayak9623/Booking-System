package com.bookwise.master.entity;

import com.bookwise.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "roles")
public class Role extends BaseEntity {

    @Column(unique = true)
    @NotNull(message = "Role can not be null" )
    private String UserRole;
    private Boolean isActive;
    @ManyToMany(mappedBy = "roles")
    private List<User> users=new ArrayList<>();
}
