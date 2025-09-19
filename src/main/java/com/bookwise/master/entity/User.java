package com.bookwise.master.entity;

import com.bookwise.common.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User extends BaseEntity {

   // @NotNull(message = "name can not be null")
    private String name;
    @NotNull(message = "mobile number can not be null")
    @Column(length = 10)
    private String mobileNumber;
    @Column(unique = true)
    @NotNull(message = "email can not be null")
    private String email;
    @NotNull(message = "password can not be null")
    private String password;
    @ManyToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles=new HashSet<>();

}
