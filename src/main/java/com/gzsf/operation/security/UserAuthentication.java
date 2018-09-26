package com.gzsf.operation.security;

import com.gzsf.operation.bean.LoginInfo;
import com.gzsf.operation.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UserAuthentication implements Authentication {
    private final List<GrantedAuthority> authorities=new ArrayList<>();
    private LoginInfo details;
    private boolean authenticated=false;
    private String name;
    private String credentials;
    private String principal;

    public UserAuthentication(LoginInfo details) {
        setDetails(details);
    }

    public UserAuthentication() {
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public void addAuthority(User.Role role){
        for (int i = 0; i < User.Role.values().length; i++) {
            User.Role item=User.Role.values()[i];
            if (item.ordinal()>=role.ordinal()){
                authorities.add(new SimpleGrantedAuthority(item.name()));
            }
        }
    }

    public void setDetails(LoginInfo details) {
        this.details = details;
        name=details.getUserName();
        authenticated=true;
        addAuthority(details.getUser().getRole());
    }

    @Override
    public Object getCredentials() {
        return credentials;
    }

    @Override
    public Object getDetails() {
        return details;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }

    @Override
    public boolean isAuthenticated() {
        return authenticated;
    }

    @Override
    public void setAuthenticated(boolean b) throws IllegalArgumentException {
        authenticated=b;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setCredentials(String credentials) {
        this.credentials = credentials;
    }

    public void setPrincipal(String principal) {
        this.principal = principal;
    }
}
