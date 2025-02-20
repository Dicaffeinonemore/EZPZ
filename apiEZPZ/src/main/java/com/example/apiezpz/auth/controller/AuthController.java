package com.example.apiezpz.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.apiezpz.auth.dto.LoginRequest;
import com.example.apiezpz.auth.dto.SignUpRequest;
import com.example.apiezpz.auth.dto.Token;
import com.example.apiezpz.auth.dto.TokenRequest;
import com.example.apiezpz.auth.entity.RefreshToken;
import com.example.apiezpz.auth.repository.RefreshTokenRepository;
import com.example.apiezpz.auth.security.JwtTokenProvider;
import com.example.apiezpz.auth.service.AuthService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignUpRequest request) {
        try {
            authService.signup(request);
            return ResponseEntity.ok("회원가입 성공");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            log.info("로그인 요청 데이터: {}", request);
            Token tokenDto = authService.login(request);
            log.info("생성된 토큰 정보: {}", tokenDto);
            
            if (tokenDto == null || tokenDto.getAccessToken() == null) {
                log.error("토큰 생성 실패");
                return ResponseEntity.badRequest().body("토큰 생성 실패");
            }
            
            return ResponseEntity.ok().body(tokenDto);
        } catch (Exception e) {
            log.error("로그인 처리 중 오류 발생: ", e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 토큰 재발급
    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(@RequestBody TokenRequest tokenRequest) {
        try {
            log.info("토큰 재발급 요청 - accessToken: {}", tokenRequest.getAccessToken());
            
            // 1. AccessToken에서 username 추출
            String username = jwtTokenProvider.getUsernameFromToken(tokenRequest.getAccessToken());
            if (username == null) {
                log.error("토큰에서 username 추출 실패");
                return ResponseEntity.badRequest().body("유효하지 않은 토큰입니다.");
            }
            
            log.info("토큰에서 추출한 username: {}", username);
            
            // 2. 저장소에서 username으로 RefreshToken 조회
            RefreshToken refreshToken = refreshTokenRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("로그아웃 된 사용자입니다."));
                
            // 3. RefreshToken 검증
            if (!jwtTokenProvider.validateToken(refreshToken.getToken())) {
                log.error("유효하지 않은 리프레시 토큰");
                return ResponseEntity.badRequest().body("리프레시 토큰이 만료되었습니다. 다시 로그인해주세요.");
            }
            
            // 4. 새로운 토큰 발급
            Token tokenDto = authService.reissue(username);
            log.info("새로운 토큰 발급 완료 - accessToken: {}", tokenDto.getAccessToken());
            
            return ResponseEntity.ok(tokenDto);
        } catch (Exception e) {
            log.error("토큰 재발급 중 오류 발생", e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 로그아웃
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody TokenRequest tokenRequest) {
        try {
            String username = jwtTokenProvider.getUsernameFromToken(tokenRequest.getAccessToken());
            if (username == null) {
                return ResponseEntity.badRequest().body("유효하지 않은 토큰입니다.");
            }
            
            authService.logout(username);
            return ResponseEntity.ok("로그아웃 성공");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}