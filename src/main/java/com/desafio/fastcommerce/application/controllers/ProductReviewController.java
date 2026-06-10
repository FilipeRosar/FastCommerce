package com.desafio.fastcommerce.application.controllers;


import com.desafio.fastcommerce.application.service.ProductReviewService;
import com.desafio.fastcommerce.domain.DTOs.ReviewDTOs.CreateProductReviewDTO;
import com.desafio.fastcommerce.domain.DTOs.ReviewDTOs.ProductReviewResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/product-reviews")
@RequiredArgsConstructor
public class ProductReviewController {
    private final ProductReviewService  productReviewService;

    @PostMapping
    public ResponseEntity<UUID> createReview(@RequestBody @Valid CreateProductReviewDTO dto){
        return ResponseEntity.ok(productReviewService.createReview(dto));
    }
    @GetMapping("/product/{productId}")
    public ResponseEntity<Page<ProductReviewResponseDTO>> getReview(@PathVariable UUID productId,
                                                                    @PageableDefault(size = 10) Pageable pageable){
        return ResponseEntity.ok(productReviewService.getReviews(productId, pageable));
    }
    @DeleteMapping("{reviewId}")
    public ResponseEntity<Void> deleteReview(@PathVariable UUID reviewId){
        productReviewService.deleteReview(reviewId);
        return ResponseEntity.noContent().build();
    }
}
