package com.bowmeow.user.services;

import com.bowmeow.user.User;
import com.bowmeow.user.UserServiceGrpc;
import com.bowmeow.user.entity.UserEntity;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService extends UserServiceGrpc.UserServiceImplBase {
//    private final UserRepository userRepository;

    @Override
    public void signUp( User.SignUpRequest request, StreamObserver<User.SignUpResponse> responseObserver) {
        // 실제 회원가입 로직 구현
        String username = request.getUsername();
        String password = request.getPassword();

        // 예: 사용자 저장
//        UserEntity user = new UserEntity(username, password);
//        userRepository.save(user);

        User.SignUpResponse response = User.SignUpResponse.newBuilder()
                .setMessage("User successfully registered")
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getUser(User.UserRequest request, StreamObserver<User.UserResponse> responseObserver) {
        // 실제 사용자 조회 로직 구현
        String userId = request.getUserId();
//        UserEntity user = userRepository.findById(userId)
//                .orElseThrow(() -> new RuntimeException("User not found"));

        UserEntity user = UserEntity.builder()
                .id( UUID.randomUUID().toString() )
                .username( "김서영" )
                .password( "1234" )
                .email( "seoyeomedev@gmail.com" )
                .build();

        User.UserResponse response = User.UserResponse.newBuilder()
                .setUsername(user.getUsername())
                .setEmail(user.getEmail())
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
