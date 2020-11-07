package org.johnpc.jwt;

import org.johnpc.jwt.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UserPrinciple implements UserDetails {

    // Using decorator Pattern
    private User user;

    public UserPrinciple(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();

        // Extract list of permissions (permission names)
        this.user.getPermissionsList().forEach(permission->{
            GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(permission);
            authorities.add(grantedAuthority);
        });

        //Extract list of Roles (Roles names)
        this.user.getRoleList().forEach(role -> {
            GrantedAuthority roleAuthority = new SimpleGrantedAuthority("ROLE_"+ role);
            authorities.add(roleAuthority);
        });
        return authorities;
    }

    @Override
    public String getPassword() {
        return this.user.getPassword();
    }

    @Override
    public String getUsername() {
        return this.user.getUsername();
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
        return this.user.getBlocked() == 1;
    }
}
