package com.smart.scm2.Config;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.smart.scm2.Entities.User;

public class CustomUserDetails implements UserDetails {

    private User user ;

    public CustomUserDetails(User user2) {
        //TODO Auto-generated constructor stub
        this.user = user2;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // TODO Auto-generated method stub
        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(user.getRole());
        return List.of(simpleGrantedAuthority);
    }

    @Override
    public String getPassword() {
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method 'getPassword'");
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method 'getUsername'");
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method 'isAccountNonExpired'");
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method 'isAccountNonLocked'");
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method 'isCredentialsNonExpired'");
        return true;
    }

    @Override
    public boolean isEnabled() {
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method 'isEnabled'");
        return true;
    }
    
}
