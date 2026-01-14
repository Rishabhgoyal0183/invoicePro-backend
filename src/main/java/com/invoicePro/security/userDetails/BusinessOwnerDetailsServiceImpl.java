package com.invoicePro.security.userDetails;

import com.invoicePro.entity.BusinessOwner;
import com.invoicePro.repository.BusinessOwnerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BusinessOwnerDetailsServiceImpl implements UserDetailsService {

    private final BusinessOwnerRepository businessOwnerRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String emailId) throws UsernameNotFoundException {

        BusinessOwner businessOwner = businessOwnerRepository.findByEmailIdAndStatus(emailId, "ACTIVE")
                .orElseThrow(() -> new UsernameNotFoundException("User not found with phone no. : " + emailId));

        return BusinessOwnerDetails.build(businessOwner);

    }

}

