package com.itoxi.petnuri.domain.eventChallenge.service;

import com.itoxi.petnuri.domain.eventChallenge.dto.response.GetRewardChallengeDetailResp;
import com.itoxi.petnuri.domain.eventChallenge.dto.response.GetRewardChallengeProductResp;
import com.itoxi.petnuri.domain.eventChallenge.dto.response.GetRewardChallengeProductResp.ProductDTO;
import com.itoxi.petnuri.domain.eventChallenge.entity.RewardChallenge;
import com.itoxi.petnuri.domain.eventChallenge.repository.RewardChallengeRepository;
import com.itoxi.petnuri.domain.product.entity.ChallengeProduct;
import com.itoxi.petnuri.domain.product.entity.Product;
import com.itoxi.petnuri.domain.product.repository.ChallengeProductRepository;
import com.itoxi.petnuri.global.common.exception.Exception404;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.itoxi.petnuri.domain.product.type.ChallengeProductCategory.REWARD;
import static com.itoxi.petnuri.global.common.exception.type.ErrorCode.NOT_FOUND_DAILY_CHALLENGE_ID;

@Service
@RequiredArgsConstructor
public class RewardChallengeService {
    private final RewardChallengeRepository challengeRepository;
    private final ChallengeProductRepository challengeProductRepository;

    @Transactional(readOnly = true)
    public GetRewardChallengeDetailResp getDetail(Long challengeId) {
        RewardChallenge rewardChallenge = challengeRepository.findById(challengeId)
                .orElseThrow(() -> new Exception404(NOT_FOUND_DAILY_CHALLENGE_ID));

        return new GetRewardChallengeDetailResp(rewardChallenge);
    }

    @Transactional(readOnly = true)
    public GetRewardChallengeProductResp getChallengeProduct(Long challengeId) {
        RewardChallenge rewardChallenge = challengeRepository.findById(challengeId)
                .orElseThrow(() -> new Exception404(NOT_FOUND_DAILY_CHALLENGE_ID));

        List<ChallengeProduct> challengeProducts = challengeProductRepository
                .findAllByRewardChallengeAndCategory(rewardChallenge, REWARD);

        List<ProductDTO> productDTOS = new ArrayList<>();
        for (ChallengeProduct ChallengeProduct : challengeProducts) {
            Product product = ChallengeProduct.getProduct();
            ProductDTO productDTO = new ProductDTO(ChallengeProduct.getId(), product.getName(), product.getQuantity());
            productDTOS.add(productDTO);
        }

        return new GetRewardChallengeProductResp(productDTOS);
    }
}
