package com.fiordelisi.fiordelisiproduct.config;

import com.fiordelisi.fiordelisiproduct.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class FiordelisiProductUserPrincipal implements UserDetails {
    private final transient User user;
    private final boolean havePassword;


    public FiordelisiProductUserPrincipal(User user) {
        this.user = user;
        this.havePassword = user.getPassword() != null && !user.getPassword().isEmpty();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    @Override
    public String getPassword() {
        return user != null ? user.getPassword() : null;
    }

    @Override
    public String getUsername() {
        return user != null ? user.getUsername() : null;
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

    public User getUser() {
        return user;
    }

    public String getId() {
        return user != null ? user.getId() : null;
    }

    public boolean isHavePassword() {
        return havePassword;
    }
}
