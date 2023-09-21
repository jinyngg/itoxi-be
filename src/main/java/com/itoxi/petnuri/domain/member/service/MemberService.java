package com.itoxi.petnuri.domain.member.service;

import com.itoxi.petnuri.domain.member.dto.request.PetSaveReq;
import com.itoxi.petnuri.domain.member.dto.request.ProfileUpdateReq;
import com.itoxi.petnuri.domain.member.dto.response.MyPageResp;
import com.itoxi.petnuri.domain.member.dto.response.ProfileUpdateResp;
import com.itoxi.petnuri.domain.member.entity.Member;
import com.itoxi.petnuri.domain.member.entity.Pet;
import com.itoxi.petnuri.domain.member.repository.PetRepository;
import com.itoxi.petnuri.global.s3.service.AmazonS3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final AmazonS3Service amazonS3Service;
    private final PetRepository petRepository;

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

}
