package com.itoxi.petnuri.domain.member.service;

import com.itoxi.petnuri.domain.member.dto.request.PetProfileReq;
import com.itoxi.petnuri.domain.member.dto.request.PetSaveReq;
import com.itoxi.petnuri.domain.member.dto.request.ProfileUpdateReq;
import com.itoxi.petnuri.domain.member.dto.response.MyPageResp;
import com.itoxi.petnuri.domain.member.dto.response.ProfileUpdateResp;
import com.itoxi.petnuri.domain.member.entity.Member;
import com.itoxi.petnuri.domain.member.entity.Pet;
import com.itoxi.petnuri.domain.member.repository.MemberRepository;
import com.itoxi.petnuri.domain.member.repository.PetRepository;
import com.itoxi.petnuri.global.common.exception.CustomException;
import com.itoxi.petnuri.global.common.exception.Exception404;
import com.itoxi.petnuri.global.redis.RedisService;
import com.itoxi.petnuri.global.s3.service.AmazonS3Service;
import com.itoxi.petnuri.global.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.itoxi.petnuri.global.common.exception.type.ErrorCode.PET_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final AmazonS3Service amazonS3Service;
    private final PetRepository petRepository;
    private final MemberRepository memberRepository;
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
        String changeNickname = request.getNickName();
        member.updateProfile(changeNickname, originImageUrl);

        return new ProfileUpdateResp(changeNickname, originImageUrl);
    }


    public void savePet(Member member, PetSaveReq petSaveReq) {

        petRepository.save(Pet.builder()
                .member(member)
                .species(petSaveReq.getSpecies())
                .petName(petSaveReq.getPetName())
                .breed(petSaveReq.getBreed())
                .petGender(petSaveReq.getPetGender())
                .petAge(petSaveReq.getPetAge()).build());
    }

    public void savePet(Member member, PetProfileReq petProfileReq, MultipartFile image) {

        String originUrl = null;
        List<Pet> petList = petRepository.findAll();

        if (!image.isEmpty()) {
            originUrl = amazonS3Service.uploadPetProfileImage(image);
        }

        //첫 등록이면 자동으로 대표 프로필로 지정
        if(petList.isEmpty()) {
            petRepository.save(Pet.builder()
                    .member(member)
                    .petName(petProfileReq.getPetName())
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
        memberRepository.delete(member);
        invalidatedToken(accessToken);
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
                .petAge(petProfileReq.getPetAge())
                .isSelected(petProfileReq.getIsSelected())
                .image(originUrl).build();
    }

}
