package com.itoxi.petnuri.domain.eventChallenge.dto.response;

import lombok.Getter;

import java.util.List;

@Getter
public class GetRewardChallengeProductResp {
    private final List<ProductDTO> challengeProducts;

    public GetRewardChallengeProductResp(List<ProductDTO> challengeProducts) {
        this.challengeProducts = challengeProducts;
    }

    @Getter
    public static class ProductDTO {
        private final Long id;
        private final String name;
        private final Long quantity;

        public ProductDTO(Long id, String name, Long quantity) {
            this.id = id;
            this.name = name;
            this.quantity = quantity;
        }
    }
}
