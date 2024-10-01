package com.bowmeow.user.services;

import com.bowmeow.user.User;
import com.bowmeow.user.UserServiceGrpc;
import com.bowmeow.user.entity.UserEntity;
import com.bowmeow.user.repository.UserRepository;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class UserService extends UserServiceGrpc.UserServiceImplBase {
    private final UserRepository userRepository;
    private final RedisTemplate<String, String> redisTemplate;
    private final EmailService emailService;

    @Override
    public void signUp(User.SignUpRequest request, StreamObserver<User.SignUpResponse> responseObserver) {
        String email = request.getEmail();
        String isVerified = redisTemplate.opsForValue().get("verified:" + email);

        if (isVerified != null && isVerified.equals("true")) {
            if (!userRepository.existsByEmail(email)) {
                // 이메일 인증이 완료된 사용자를 회원가입 처리
                UserEntity user = UserEntity.builder()
                        .email(email)
                        .password(request.getPassword())
                        .nickname(request.getNickname())
                        .phoneNumber(request.getPhoneNumber())
                        .address(request.getAddress())
                        .build();
                userRepository.save(user);

                // 회원가입 성공 응답
                User.SignUpResponse response = User.SignUpResponse.newBuilder()
                        .setSuccess(true)
                        .setMessage("회원가입이 성공적으로 완료되었습니다.")
                        .build();
                responseObserver.onNext(response);

                // 회원가입 완료 후 인증 정보 삭제
                redisTemplate.delete("verified:" + email);
            } else {
                // 이미 존재하는 이메일인 경우
                User.SignUpResponse response = User.SignUpResponse.newBuilder()
                        .setSuccess(false)
                        .setMessage("이메일이 이미 존재합니다.")
                        .build();
                responseObserver.onNext(response);
            }
        } else {
            // 이메일 인증이 완료되지 않은 경우
            User.SignUpResponse response = User.SignUpResponse.newBuilder()
                    .setSuccess(false)
                    .setMessage("이메일 인증이 완료되지 않았습니다.")
                    .build();
            responseObserver.onNext(response);
        }
        responseObserver.onCompleted();
    }


    @Override
    public void getUser(User.UserRequest request, StreamObserver<User.UserResponse> responseObserver) {
        // 실제 사용자 조회 로직 구현
        UUID userId = UUID.fromString( request.getUserId() );
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        User.UserResponse response = User.UserResponse.newBuilder()
                .setNickname(user.getNickname())
                .setEmail(user.getEmail())
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void sendVerificationEmail(User.SendVerificationEmailRequest request,
                                      StreamObserver<User.SendVerificationEmailResponse> responseObserver) {
        String email = request.getEmail();
        String token = UUID.randomUUID().toString();

        redisTemplate.opsForValue().set( email, token, 10, TimeUnit.MINUTES );

        String verificationLink = "http://bowmeow.com/verify?email=" + email + "&token=" + token;

        try {
            // 이메일 발송 (실제 발송 로직 구현)
            emailService.sendEmail(email, "이메일 인증", "다음 링크를 클릭하여 인증을 완료하세요: " + verificationLink);

            // 성공 응답 전송
            User.SendVerificationEmailResponse response = User.SendVerificationEmailResponse.newBuilder()
                    .setMessage("인증 이메일이 발송되었습니다.")
                    .build();
            responseObserver.onNext(response);
        } catch (Exception e) {
            // 에러 발생 시
            responseObserver.onError(new RuntimeException("이메일 인증 요청 처리 중 오류가 발생했습니다: " + e.getMessage()));
        } finally {
            responseObserver.onCompleted();
        }
    }

    @Override
    public void verifyEmail(User.VerifyEmailRequest request,
                            StreamObserver<User.VerifyEmailResponse> responseObserver) {
        String email = request.getEmail();
        String token = request.getToken();

        // Redis에서 토큰 조회
        String redisToken = redisTemplate.opsForValue().get(email);

        if (redisToken != null && redisToken.equals(token)) {
            // 인증 완료: Redis에 "인증 완료" 플래그 설정
            redisTemplate.opsForValue().set("verified:" + email, "true", 24, TimeUnit.HOURS); // 24시간 유지

            redisTemplate.delete(email); // 인증이 완료되었으므로 기존 토큰 삭제

            User.VerifyEmailResponse response = User.VerifyEmailResponse.newBuilder()
                    .setMessage("이메일이 성공적으로 인증되었습니다.")
                    .build();
            responseObserver.onNext(response);
        } else {
            User.VerifyEmailResponse response = User.VerifyEmailResponse.newBuilder()
                    .setMessage("유효하지 않은 인증 정보입니다.")
                    .build();
            responseObserver.onNext(response);
        }
        responseObserver.onCompleted();
    }

    @Override
    public void isEmailVerified(User.IsEmailVerifiedRequest request,
                                StreamObserver<User.IsEmailVerifiedResponse> responseObserver) {
//        String email = request.getEmail();
//        boolean isVerified = userRepository.isEmailVerified(email);
//
//        User.IsEmailVerifiedResponse response = User.IsEmailVerifiedResponse.newBuilder()
//                .setVerified(isVerified)
//                .build();
//        responseObserver.onNext(response);
//        responseObserver.onCompleted();
    }
}