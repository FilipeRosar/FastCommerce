package com.desafio.fastcommerce.domain.repository;


import com.desafio.fastcommerce.domain.entities.Products;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductsRepository extends JpaRepository<Products, UUID>, JpaSpecificationExecutor<Products> {
    Page<Products> findByActiveTrue(Pageable pageable);
    Optional<Products> findByIdAndActiveTrue(UUID id);
}
