package com.itoxi.petnuri.domain.member.service;

import com.itoxi.petnuri.domain.dailychallenge.entity.DailyChallenge;
import com.itoxi.petnuri.domain.dailychallenge.repository.DailyChallengeRepository;
import com.itoxi.petnuri.domain.eventChallenge.entity.RewardChallenge;
import com.itoxi.petnuri.domain.eventChallenge.repository.RewardChallengeRepository;
import com.itoxi.petnuri.domain.member.dto.request.PetProfileReq;
import com.itoxi.petnuri.domain.member.dto.request.PetSaveReq;
import com.itoxi.petnuri.domain.member.dto.request.ProfileUpdateReq;
import com.itoxi.petnuri.domain.member.dto.response.MainResp;
import com.itoxi.petnuri.domain.member.dto.response.MyPageResp;
import com.itoxi.petnuri.domain.member.dto.response.ProfileUpdateResp;
import com.itoxi.petnuri.domain.member.entity.Member;
import com.itoxi.petnuri.domain.member.entity.Pet;
import com.itoxi.petnuri.domain.member.repository.MemberRepository;
import com.itoxi.petnuri.domain.member.repository.PetRepository;
import com.itoxi.petnuri.domain.petTalk.entity.PetTalk;
import com.itoxi.petnuri.domain.petTalk.entity.PetTalkPhoto;
import com.itoxi.petnuri.domain.petTalk.repository.PetTalkPhotoJpaRepository;
import com.itoxi.petnuri.domain.petTalk.repository.PetTalkReplyRepository;
import com.itoxi.petnuri.domain.petTalk.repository.PetTalkRepository;
import com.itoxi.petnuri.domain.petTalk.type.PetGender;
import com.itoxi.petnuri.global.common.exception.Exception404;
import com.itoxi.petnuri.global.common.exception.type.ErrorCode;
import com.itoxi.petnuri.global.redis.RedisService;
import com.itoxi.petnuri.global.s3.service.AmazonS3Service;
import com.itoxi.petnuri.global.security.auth.PrincipalDetails;
import com.itoxi.petnuri.global.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.itoxi.petnuri.global.common.exception.type.ErrorCode.PET_GENDER_NOT_FOUND;
import static com.itoxi.petnuri.global.common.exception.type.ErrorCode.PET_NOT_FOUND;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {
    private final AmazonS3Service amazonS3Service;
    private final PetRepository petRepository;
    private final MemberRepository memberRepository;
    private final PetTalkRepository petTalkRepository;
    private final PetTalkReplyRepository petTalkReplyRepository;
    private final PetTalkPhotoJpaRepository petTalkPhotoJpaRepository;
    private final RewardChallengeRepository rewardChallengeRepository;
    private final DailyChallengeRepository dailyChallengeRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisService redisService;

    @Transactional(readOnly = true)
    public MyPageResp getMyPage(Member member) {
        return new MyPageResp(member);
    }

    @Transactional
    public ProfileUpdateResp updateProfile(Member member, ProfileUpdateReq request, MultipartFile file) {
        String originImageUrl = member.getProfileImageUrl();
        if (file != null) {
            // 사진이 들어왔을 경우
            originImageUrl = amazonS3Service.uploadProfileImage(file);
        }
        String changeNickname = request.getNickname();
        member.updateProfile(changeNickname, originImageUrl);
        memberRepository.save(member);

        return new ProfileUpdateResp(changeNickname, originImageUrl);
    }

    @Transactional
    public void savePet(Member member, PetSaveReq petSaveReq) {

        try{
            petRepository.save(Pet.builder()
                    .member(member)
                    .species(petSaveReq.getSpecies())
                    .petName(petSaveReq.getPetName())
                    .breed(petSaveReq.getBreed())
                    .petGender(PetGender.stringToPetGender(petSaveReq.getPetGender()))
                    .petAge(petSaveReq.getPetAge())
                    .isSelected(true).build());
        }catch (Exception e){
            throw new Exception404(PET_GENDER_NOT_FOUND);
        }

    }

    @Transactional
    public void savePet(Member member, PetProfileReq petProfileReq, MultipartFile image) {

        String originUrl = null;
        List<Pet> petList = petRepository.findAllByMember(member);

        if (!image.isEmpty()) {
            originUrl = amazonS3Service.uploadPetProfileImage(image);
        }

        //첫 등록이면 자동으로 대표 프로필로 지정
        if(petList.isEmpty()) {
            petRepository.save(Pet.builder()
                    .member(member)
                    .petName(petProfileReq.getPetName())
                    .petGender(PetGender.stringToPetGender(petProfileReq.getPetGender()))
                    .petAge(petProfileReq.getPetAge())
                    .isSelected(true)
                    .image(originUrl).build());
            return;
        }

        //이미 대표 프로필이 있는데 대표 프로필 지정을 눌렀을 때(isSelected가 true일 때)
        if (petProfileReq.getIsSelected()) {
            checkIsSelected(petList);
            petRepository.save(createPetProfile(member, petProfileReq, originUrl));

            return;
        }

        petRepository.save(createPetProfile(member, petProfileReq, originUrl));
    }

    @Transactional
    public void updatePet(Member member, PetProfileReq petProfileReq, MultipartFile image){
        String originUrl = null;
        List<Pet> petList = petRepository.findAll();
        Pet pet = petRepository.findById(petProfileReq.getPetId())
                .orElseThrow(() -> new Exception404(PET_NOT_FOUND));

        if (!image.isEmpty()) {
            originUrl = amazonS3Service.uploadPetProfileImage(image);
        }


        if(petProfileReq.getIsSelected()){
            checkIsSelected(petList);
            pet.updatePet(member, petProfileReq, originUrl);
            petRepository.save(pet);
            return;
        }

        pet.updatePet(member, petProfileReq, originUrl);
        petRepository.save(pet);
    }

    @Transactional
    public void withdraw(Member member, String accessToken) {
        try {
            petTalkReplyRepository.deleteByWriter(member);
            List<PetTalk> petTalkList = petTalkRepository.findAllByWriter(member);

            for (PetTalk petTalk : petTalkList) {
                petTalkReplyRepository.deleteAllByPetTalk(petTalk);
            }

            petTalkRepository.deleteAll(petTalkList);
            memberRepository.delete(member);

            invalidatedToken(accessToken);
        } catch (Exception e) {
            log.error(e.getMessage());
            System.out.println(ErrorCode.FAIL_WITHDRAW);
        }
    }

    private void invalidatedToken(String accessToken) {
        Long expiration = jwtTokenProvider.getExpiration(accessToken);
        String email = jwtTokenProvider.getEmail(accessToken);

        redisService.addBlacklist(accessToken, email, expiration);
    }

    public void logout(String accessToken) {
        accessToken = jwtTokenProvider.resolveToken(accessToken);
        invalidatedToken(accessToken);
    }

    public void checkIsSelected(List<Pet> petList) {

        //펫 리스트에 대표로 지정된 프로필이 있다면 false로 바꿔줌
        for (Pet pet : petList) {
            if (pet.getIsSelected()) {
                pet.updateIsSelected(false);
                petRepository.save(pet);
            }
        }
    }

    public Pet createPetProfile(Member member, PetProfileReq petProfileReq, String originUrl){


        return Pet.builder()
                .member(member)
                .petName(petProfileReq.getPetName())
                .petGender(PetGender.stringToPetGender(petProfileReq.getPetGender()))
                .petAge(petProfileReq.getPetAge())
                .isSelected(petProfileReq.getIsSelected())
                .image(originUrl).build();
    }

    @Transactional(readOnly = true)
    public MainResp getMain(Authentication authentication) {
        Member member = getAuthenticatedMember(authentication);
        List<MainResp.PetDTO> petDTOList = mapPetsToDTO(member);

        List<MainResp.RewardChallengeDTO> rewardChallengeDTOList = mapRewardChallengesToDTO();
        MainResp.DailyChallengeDTO dailyChallengeDTO = getRandomDailyChallenge();
        MainResp.ChallengeDTO challengeDTO = new MainResp.ChallengeDTO(rewardChallengeDTOList, dailyChallengeDTO);

        List<MainResp.PetTalkDTO> petTalkDTOList = getRandomPetTalks();
        MainResp.MainContentDTO mainContentDTO = new MainResp.MainContentDTO(petDTOList, challengeDTO, petTalkDTOList);

        return new MainResp(mainContentDTO);
    }

    private Member getAuthenticatedMember(Authentication authentication) {
        if (authentication != null) {
            PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
            return principalDetails.getMember();
        }

        return null;
    }

    private List<MainResp.PetDTO> mapPetsToDTO(Member member) {
        List<MainResp.PetDTO> petDTOList = new ArrayList<>();
        if (member != null) {
            List<Pet> petList = petRepository.findAllByMember(member);
            petDTOList = petList.stream()
                    .map(MainResp.PetDTO::new)
                    .collect(Collectors.toList());
        }

        return petDTOList;
    }

    private List<MainResp.RewardChallengeDTO> mapRewardChallengesToDTO() {
        List<RewardChallenge> rewardChallenges = rewardChallengeRepository.findTop2ByOrderByRewardChallengersDesc(PageRequest.of(0, 2));
        return rewardChallenges.stream()
                .map(MainResp.RewardChallengeDTO::new)
                .collect(Collectors.toList());
    }

    private MainResp.DailyChallengeDTO getRandomDailyChallenge() {
        long totalNum = dailyChallengeRepository.count();
        int randomNum = (int) (Math.random() * totalNum);
        Page<DailyChallenge> dailyChallengePage = dailyChallengeRepository.findAll(PageRequest.of(randomNum, 1));

        if (dailyChallengePage.hasContent()) {
            DailyChallenge dailyChallenge = dailyChallengePage.getContent().get(0);
            return new MainResp.DailyChallengeDTO(dailyChallenge);
        }

        return null;
    }

    private List<MainResp.PetTalkDTO> getRandomPetTalks() {
        LocalDateTime oneWeekAgo = LocalDateTime.now().minusWeeks(1);
        List<PetTalk> petTalkList = petTalkRepository.findTopPetTalksOrderByRanking(oneWeekAgo, PageRequest.of(0, 15));

        Collections.shuffle(petTalkList);
        int numToSelect = Math.min(4, petTalkList.size());
        List<PetTalk> randomPetTalk = petTalkList.subList(0, numToSelect);

        return randomPetTalk.stream()
                .map(petTalk -> {
                    PetTalkPhoto petTalkPhoto = petTalkPhotoJpaRepository.findTop1ByPetTalkOrderByIdAsc(petTalk).orElse(null);
                    String thumbnail = (petTalkPhoto != null) ? petTalkPhoto.getUrl() : null;
                    return new MainResp.PetTalkDTO(petTalk, thumbnail);
                })
                .collect(Collectors.toList());
    }
}
