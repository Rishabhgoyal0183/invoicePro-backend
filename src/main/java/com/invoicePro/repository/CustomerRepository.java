package com.invoicePro.repository;

import com.invoicePro.entity.Customer;
import com.invoicePro.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Page<Customer> findByBusinessIdAndStatusNot(Long businessId, Status status, Pageable pageable);


    Optional<Customer> findByNameAndBusinessId(String name, long businessId);
}
