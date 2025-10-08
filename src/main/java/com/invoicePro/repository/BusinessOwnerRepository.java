package com.invoicePro.repository;

import com.invoicePro.entity.BusinessOwner;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface BusinessOwnerRepository extends JpaRepository<BusinessOwner, Long> {
    Optional<BusinessOwner> findByEmailIdAndStatus(String emailId, String active);

    Optional<BusinessOwner> findByEmailId(String email);
}
