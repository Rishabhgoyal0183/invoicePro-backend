package com.invoicePro.repository;

import com.invoicePro.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Page<Product> findByBusinessId(long businessId, Pageable pageable);

    Optional<Object> findByNameAndBusinessId(String name, long businessId);

    Optional<Product> findByIdAndBusinessId(long productId, long businessId);

    long countByBusinessId(long businessId);

    long countByBusinessIdAndStockLessThan(long businessId, int quantityThreshold);

    @Query("SELECT COALESCE(SUM(p.salePrice), 0) FROM Product p WHERE p.businessId = :businessId")
    Double calculateTotalInventoryValue(long businessId);
}
