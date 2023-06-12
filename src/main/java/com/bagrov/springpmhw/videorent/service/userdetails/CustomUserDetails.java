package com.bagrov.springpmhw.videorent.service.userdetails;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class CustomUserDetails implements UserDetails {

    private final Integer id;
    private final String username;
    private final String password;
    private final Collection<? extends GrantedAuthority> authorities;
    private final Boolean accountNotExpired;
    private final Boolean accountNonLocked;
    private final Boolean credentialsNonExpired;
    private final Boolean enabled;

    public CustomUserDetails(Integer id, String username, String password, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.authorities = authorities;
        this.accountNotExpired = true;
        this.accountNonLocked = true;
        this.credentialsNonExpired = true;
        this.enabled = true;
    }

    public Integer getId() {
        return id;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountNotExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public String toString() {
        ObjectMapper mapper = new ObjectMapper();
        try{
            return mapper.writeValueAsString(getFieldsToInclude());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return super.toString();
    }

    private Object getFieldsToInclude() {
        Map<String, Object> fields = new HashMap<>();
        fields.put("id", id);
        fields.put("username", username);
        fields.put("password", password);
        fields.put("role", authorities);
        return fields;
    }
}
