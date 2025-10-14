package com.invoicePro.repository;

import com.invoicePro.entity.Business;
import com.invoicePro.entity.BusinessOwner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BusinessRepository extends JpaRepository<Business, Long> {

    List<Business> findByOwner(BusinessOwner businessOwner);
}
