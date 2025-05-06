package com.example.tasteofkorea.service;

import com.example.tasteofkorea.dto.JoinRequestDto;
import com.example.tasteofkorea.dto.SignupRequestDto;
import com.example.tasteofkorea.dto.UpdateInfoRequestDto;
import com.example.tasteofkorea.dto.UserInfoResponseDto;
import com.example.tasteofkorea.entity.User;
import com.example.tasteofkorea.entity.UserRoleEnum;
import com.example.tasteofkorea.repository.UserRepository;
import com.example.tasteofkorea.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;  // 추가

    // 회원가입
    public void signup(SignupRequestDto requestDto) {
        String encodedPassword = passwordEncoder.encode(requestDto.getPassword());


        // User 객체를 생성하고 저장
        User user = new User(
                requestDto.getUsername(),
                encodedPassword,  // 여기에 인코딩된 비밀번호 저장
                requestDto.getName(),
                requestDto.getEmail(),
                requestDto.getIntroduce(),
                requestDto.getRole(),  // 이미 Enum 타입이므로 변환 불필요
                null // 초기에는 토큰이 없으므로 null
        );
        userRepository.save(user);
    }

    // 회원 정보 조회
    public Optional<User> getUserInfo(UserDetailsImpl userDetails) {
        return userRepository.findById(userDetails.getUser().getId());
    }

    // 회원 정보 수정
    public UserInfoResponseDto updateUserInfo(UpdateInfoRequestDto requestDto, UserDetailsImpl userDetails) {
        // 정보 수정 로직 구현
        User user = userRepository.findById(userDetails.getUser().getId()).orElseThrow();
        user.setIntroduce(requestDto.getIntroduce());
        userRepository.save(user);

        return new UserInfoResponseDto(user.getUsername(), user.getEmail(), user.getIntroduce());
    }

    // 회원 탈퇴
    public void withdrawal(JoinRequestDto requestDto, UserDetailsImpl userDetails) {
        // 탈퇴 로직 구현
        User user = userRepository.findById(userDetails.getUser().getId()).orElseThrow();
        userRepository.delete(user);
    }

    // 로그아웃
    public void logout(UserDetailsImpl userDetails) {
        // 로그아웃 처리 로직 (예: 클라이언트가 JWT 삭제하게 하면 됨)
    }
}
