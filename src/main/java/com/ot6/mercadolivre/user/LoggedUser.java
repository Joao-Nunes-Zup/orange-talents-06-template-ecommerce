package com.ot6.mercadolivre.user;

import com.ot6.mercadolivre.user.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class LoggedUser implements UserDetails {

    private String username;
    private String password;
    private List<Profile> profiles = new ArrayList<Profile>();

    public LoggedUser(String username, String password, List<Profile> profiles) {
        this.username = username;
        this.password = password;
        profiles.forEach(profile -> { this.profiles.add(profile); });
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.profiles;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
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
