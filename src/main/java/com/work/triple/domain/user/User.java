package com.work.triple.domain.user;

import com.work.triple.domain.BaseTimeEntity;
import com.work.triple.domain.trip.Trip;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;

@Getter @NoArgsConstructor
@Builder @AllArgsConstructor
@Table(name = "user")
@Entity
public class User extends BaseTimeEntity implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "username", unique = true)
    private String username;
    @Column(name = "password")
    private String password;

    @OneToMany(mappedBy = "user")
    private List<Trip> trips = new ArrayList<>();

    @ManyToMany(fetch = FetchType.EAGER)
    private Collection<Role> roles = new ArrayList<>();

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        List<String> roleNames = this.roles.stream().map(Role::getName).collect(Collectors.toList());
        String[] roleNameArray = roleNames.toArray(new String[roleNames.size()]);
        stream(roleNameArray).forEach(role-> authorities.add(new SimpleGrantedAuthority(role)));
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
