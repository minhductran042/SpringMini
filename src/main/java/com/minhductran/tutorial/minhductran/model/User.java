package com.minhductran.tutorial.minhductran.model;

import com.minhductran.tutorial.minhductran.utils.UserStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "users")
@Slf4j
public class User extends AbstractEntity implements UserDetails {

    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @Column(name = "email", unique = true)
    private String email; // Thêm trường email để lưu trữ địa chỉ email người dùng

    @Column(name = "password")
    private String password;

    @Column(name = "phone", unique = true, nullable = false)
    private String phone;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    // mappedBy chỉ ra rằng trường này là mối quan hệ ngược lại với trường "user" trong lớp Todo
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true) // lazy: chi tai todo khi user goi getTodoList
    private List<ToDo> todos;

    @Column(name = "user_status")
    @Enumerated(EnumType.STRING)
    private UserStatus status; // Trạng thái của người dùng, có thể là "ACTIVE", "INACTIVE", v.v.

    @Column(name = "logo")
    private String logo; // Thêm trường logo lưu trữ ảnh đại diện của người dùng

    Set<String> roles = new HashSet<>(); // Thêm trường roles để lưu trữ các quyền của người dùng

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserStatus.ACTIVE.equals(status);
    }

    @Override
    public String getUsername() {
        return email; // Quan trọng: return email làm username
    }
}
