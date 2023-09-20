package com.itoxi.petnuri.global.security.auth;

import com.itoxi.petnuri.domain.member.entity.Member;
import com.itoxi.petnuri.domain.member.repository.MemberRepository;
import com.itoxi.petnuri.global.common.exception.type.ErrorCode;
import com.itoxi.petnuri.global.common.exception.Exception404;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class PrincipalDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.info("시큐리티 로그인 시도 email: " + email);

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new Exception404(ErrorCode.USER_NOT_FOUND));

        return new PrincipalDetails(member);
    }
}
