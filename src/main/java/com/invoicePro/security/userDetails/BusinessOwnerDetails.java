package com.invoicePro.security.userDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.invoicePro.entity.BusinessOwner;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

@Data
@AllArgsConstructor
public class BusinessOwnerDetails implements UserDetails {
    private static final long serialVersionUID = 1L;

    private final Long id;

    private final String fullName;

    private final String email;

    private final String phoneNumber;

    @JsonIgnore
    private final String password;

    private final Collection<? extends GrantedAuthority> authorities;

    public static BusinessOwnerDetails build(BusinessOwner businessOwner) {

        return new BusinessOwnerDetails(businessOwner.getId(), businessOwner.getFullName(), businessOwner.getEmailId(),
                businessOwner.getPhoneNumber(), businessOwner.getPassword(), Collections.EMPTY_LIST);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }
    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        BusinessOwnerDetails user = (BusinessOwnerDetails) o;
        return Objects.equals(id, user.id);
    }
}

