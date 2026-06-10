package com.desafio.fastcommerce.domain.repository;

import com.desafio.fastcommerce.domain.entities.ProductReview;
import com.desafio.fastcommerce.domain.entities.Products;
import com.desafio.fastcommerce.domain.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductReviewRepository extends JpaRepository<ProductReview, UUID> {
    Optional<ProductReview> findByUserAndProduct(User user, Products product);

    Page<ProductReview> findByProduct(Products product, Pageable pageable);
    long countByProduct(Products product);

    @Query("""
    SELECT AVG(pr.rating)
    FROM ProductReview pr
    WHERE pr.product = :product
    """)
    Double getAverageScore(@Param("product") Products product);
}
