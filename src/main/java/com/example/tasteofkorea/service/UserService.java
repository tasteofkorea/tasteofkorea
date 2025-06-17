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
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;  // 추가

    // 회원가입
    public void signup(SignupRequestDto requestDto) {
        // 중복 username 검사
        if (userRepository.findByUsername(requestDto.getUsername()).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 사용자명입니다.");
        }

        // 중복 email 검사
        if (userRepository.findByEmail(requestDto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }

        String encodedPassword = passwordEncoder.encode(requestDto.getPassword());

        User user = new User(
                requestDto.getUsername(),
                encodedPassword,
                requestDto.getName(),
                requestDto.getEmail(),
                requestDto.getIntroduce(),
                requestDto.getRole(),
                null
        );

        userRepository.save(user);
    }

    // 회원 정보 수정
    @Transactional
    public UserInfoResponseDto updateUserInfo(UpdateInfoRequestDto requestDto, String username) {
        // 정보 수정 로직 구현
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("유저 이름이 없습니다."));
        user.changeIntroduce(requestDto.getIntroduce());
        userRepository.save(user);

        return new UserInfoResponseDto(user.getId(), user.getUsername(), user.getEmail(), user.getIntroduce());
    }

    // 회원 탈퇴
    @Transactional
    public void withdrawal(String username) {
        // 탈퇴 로직 구현
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("유저 이름이 없습니다."));
        userRepository.delete(user);
    }



    // 로그아웃 처리
    public void logout(JoinRequestDto requestDto, UserDetailsImpl userDetails) {
        // 클라이언트 측에서 토큰을 삭제하도록 안내
        // JWT의 경우, 서버에서 별다른 처리가 필요 없으므로,
        // 이 예시에서는 실제로는 토큰을 클라이언트에서 삭제해야 합니다.

        // 예를 들어, requestDto에서 필요한 정보로 로그아웃 처리 여부 확인 등
        if (userDetails != null) {
            // JWT 토큰을 클라이언트에서 삭제하도록 요청하는 로직
            // JWT 기반 인증에서는 실제로 서버에서 해당 토큰을 블랙리스트 처리하거나 만료시킬 수 없기 때문에
            // 클라이언트가 로컬 저장소에서 토큰을 삭제하도록 유도하는 방법
            System.out.println(userDetails.getUsername() + " 로그아웃 처리");
            // 필요시 requestDto를 활용하여 추가적인 로그아웃 처리를 할 수 있음.
        }
    }

    public User getUserInfoByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new IllegalArgumentException("사용자 정보를 찾을 수 없습니다."));
    }
}
