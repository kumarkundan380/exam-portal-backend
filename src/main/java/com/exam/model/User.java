package com.exam.model;

import com.exam.enums.Gender;
import com.exam.enums.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseEntity {

    @Id
    @GeneratedValue
    @Column (name = "user_id")
    private Long userId;

    @Column(name = "name",nullable = false)
    private String name;

    @Column(name = "user_name",unique = true,nullable = false, updatable = false)
    private String userName;

    @Column(name = "password",nullable = false)
    private String password;

    @Column(name = "email",unique = true,nullable = false)
    private String email;

    @Column(name = "phone_number",unique = true,nullable = false)
    private String phoneNumber;

    @Column(name = "user_image")
    private String userImage;

    @Column(name = "gender", nullable = false)
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @ManyToMany(fetch = FetchType.EAGER,cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "user_role",
            joinColumns = {
                    @JoinColumn(name = "user_id", referencedColumnName="user_id")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "role_id", referencedColumnName="role_id")
            }
    )
    private Set<Role> roles = new HashSet<>();

    @Column(name = "is_verified", nullable = false)
    private Boolean isVerified = Boolean.FALSE;

    @Column(name = "account_non_expired", nullable = false)
    private Boolean accountNonExpired = Boolean.TRUE;

    @Column(name = "credentials_non_expired", nullable = false)
    private Boolean credentialsNonExpired = Boolean.TRUE;

    @Column(name = "account_non_locked", nullable = false)
    private Boolean accountNonLocked = Boolean.TRUE;

    @Column(name = "user_status",nullable = false)
    @Enumerated(EnumType.STRING)
    private UserStatus userStatus = UserStatus.ACTIVE;


}
