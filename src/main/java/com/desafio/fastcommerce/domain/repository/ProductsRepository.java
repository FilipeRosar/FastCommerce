package com.desafio.fastcommerce.domain.repository;


import com.desafio.fastcommerce.domain.entities.Products;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductsRepository extends JpaRepository<Products, UUID>, JpaSpecificationExecutor<Products> {
    Page<Products> findByIsActiveTrue(Pageable pageable);

    Optional<Products> findByIdAndIsActiveTrue(UUID id);

    Page<Products> findByNameContainingIgnoreCaseAndIsActiveTrue(
            String name,
            Pageable pageable
    );
    Page<Products> findByCategoryContainingIgnoreCaseAndIsActiveTrue(
            String category,
            Pageable pageable
    );
    Page<Products> findByPriceBetweenAndIsActiveTrue(
            BigDecimal minPrice,
            BigDecimal maxPrice,
            Pageable pageable
    );

}