package com.bookwise.master.entity;

import com.bookwise.common.BaseEntity;
import jakarta.persistence.*;
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
@Table(name = "resources")
public class Resource extends BaseEntity {

    @Column(nullable = false)
    private String name;
    private String type;
    private String description;
    private Integer capacity;
    private boolean active = true;
    @OneToMany(mappedBy = "resource", cascade = CascadeType.ALL)
    private List<Reservation> reservations = new ArrayList<>();
}
