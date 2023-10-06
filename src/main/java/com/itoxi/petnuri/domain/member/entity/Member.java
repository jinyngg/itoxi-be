package com.itoxi.petnuri.domain.member.entity;

import com.itoxi.petnuri.domain.member.type.MemberRole;
import com.itoxi.petnuri.global.common.BaseTimeEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "member")
public class Member extends BaseTimeEntity {

    @Id
    @Column(name = "member_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "nickname", unique = true, nullable = false)
    private String nickname;

    @Column(name = "profile_image_url")
    private String profileImageUrl;

    @Builder.Default
    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private MemberRole role = MemberRole.USER;

    @Column(name = "referral_code")
    private String referralCode;

    private boolean isAgreed;

    public static Member createMember(String email, String nickname, String referralCode, boolean isAgreed) {
        return Member.builder()
                .email(email)
                .nickname(nickname)
                .referralCode(referralCode)
                .isAgreed(isAgreed)
                .build();
    }

    public void updateProfile(String nickname, String profileImageUrl) {
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
    }
}
