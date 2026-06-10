package com.desafio.fastcommerce.application.service;

import com.desafio.fastcommerce.domain.DTOs.ReviewDTOs.CreateProductReviewDTO;
import com.desafio.fastcommerce.domain.DTOs.ReviewDTOs.ProductReviewResponseDTO;
import com.desafio.fastcommerce.domain.entities.ProductReview;
import com.desafio.fastcommerce.domain.entities.Products;
import com.desafio.fastcommerce.domain.entities.User;
import com.desafio.fastcommerce.domain.repository.OrderRepository;
import com.desafio.fastcommerce.domain.repository.ProductReviewRepository;
import com.desafio.fastcommerce.domain.repository.ProductsRepository;
import com.desafio.fastcommerce.infrastructure.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductReviewService {
    private final ProductReviewRepository productReviewRepository;
    private final ProductsRepository  productsRepository;
    private final OrderRepository orderRepository;
    private final AuthService authService;

    @Transactional
    public UUID createReview(CreateProductReviewDTO dto){
        User user = authService.getAuthenticatedUser();

        Products products = productsRepository.findById(dto.productId())
                .orElseThrow(() -> new CustomException("Produto não encontrado."));
        boolean hasPurchased = orderRepository.existsPurchaseProduct(user.getId(), products.getId());

        if(!hasPurchased){
            throw new CustomException("Você só pode avaliar produtos já comprados.");
        }
        productReviewRepository.findByUserAndProduct(user, products).ifPresent(review ->
                {
                    throw new CustomException("Você já avaliou este produto");
                });
        ProductReview review = new ProductReview();

        review.setUser(user);
        review.setProduct(products);
        review.setRating(dto.score());
        review.setComment(dto.comment());

        productReviewRepository.save(review);
        System.out.println("Atualizando média do produto...");
        updateProductAverage(products);

        return review.getId();
    }
    public Page<ProductReviewResponseDTO> getReviews(UUID productId,Pageable pageable) {
        Products products = productsRepository.findById(productId)
                .orElseThrow(() -> new CustomException("Produto não encontrado."));

        return productReviewRepository.findByProduct(products, pageable).map(this::toDto);
    }
    @Transactional
    public void deleteReview(UUID reviewId) {
        User user = authService.getAuthenticatedUser();

        ProductReview review = productReviewRepository.findById(reviewId)
                .orElseThrow(() -> new CustomException("Avaliação não encontrada."));

        if (!review.getUser().getId().equals(user.getId())) {
            throw new CustomException("Você não pode remover esta avalição.");
        }
        Products products = review.getProduct();
        productReviewRepository.deleteById(reviewId);
        updateProductAverage(products);
    }
    private void updateProductAverage(Products products){
        long totalReview = productReviewRepository.countByProduct(products);

        if (totalReview == 0) {
            products.setAverageRating(5.0);
            products.setTotalRatings(0);
        } else {
            Double average = productReviewRepository.getAverageScore(products);
            products.setAverageRating(average == null ? 5.0 : Math.round(average * 10.0)/10.0);

            products.setTotalRatings((int) totalReview);
        }
        productsRepository.save(products);
    }
    private ProductReviewResponseDTO toDto(ProductReview productReview){
        return new ProductReviewResponseDTO(
          productReview.getId(),
          productReview.getUser().getId(),
          productReview.getUser().getName(),
          productReview.getRating(),
          productReview.getComment(),
          productReview.getCreatedAt()
        );
    }
}
