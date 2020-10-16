package com.dsm.pick.domains.service;

import com.dsm.pick.domains.domain.Teacher;
import com.dsm.pick.domains.repository.TeacherRepository;
import com.dsm.pick.utils.exception.IdOrPasswordMismatchException;
import com.dsm.pick.utils.exception.NonExistEncodingOrCryptographicAlgorithmException;
import com.dsm.pick.utils.exception.TeacherNameNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.security.MessageDigest;

@Service
public class AuthService {
    private static final String ALGORITHM = "SHA-512";
    private static final String ENCODING = "UTF-8";

    private TeacherRepository teacherRepository;

    @Autowired
    public AuthService(TeacherRepository teacherRepository) {
        this.teacherRepository = teacherRepository;
    }

    public String encodingPassword(String original) {
        String resultHex = null;
        try {
            MessageDigest digest = MessageDigest.getInstance(ALGORITHM);
            digest.reset();
            digest.update(original.getBytes(ENCODING));
            resultHex = String.format("%0128x", new BigInteger(1, digest.digest()));
        } catch(Exception e) {
            throw new NonExistEncodingOrCryptographicAlgorithmException("현재 인코딩 방식 또는 암호화 알고리즘이 잘못되었습니다.");
        }
        return resultHex;
    }

    public String getTeacherName(String teacherId) {
        return teacherRepository.findById(teacherId)
                .orElseThrow(TeacherNameNotFoundException::new)
                .getName();
    }

//    public String getAccessToken(String id) {
//        return jwtService.createAccessToken(id);
//    }
//
//    public String getRefreshToken(String id) {
//        return jwtService.createRefreshToken(id);
//    }

//    public LocalDateTime getAccessTokenExpiration(String accessToken) {
//        Date expiration = jwtService.getExpiration(accessToken);
//        return LocalDateTime.ofInstant(expiration.toInstant(), ZoneId.of("Asia/Seoul"));
//    }

//    public Teacher getSameRefreshTokenTeacher(String refreshToken) {
//        if(jwtService.isUsableToken(refreshToken)) {
//            Teacher findTeacher = teacherRepository.findByRefreshToken(refreshToken)
//                    .orElseThrow(() -> new RefreshTokenMismatchException());
//
//            String findRefreshToken = findTeacher.getRefreshToken();
//            if(refreshToken.equals(findRefreshToken))
//                return findTeacher;
//            else
//                throw new RefreshTokenMismatchException();
//        } else {
//            throw new TokenExpirationException();
//        }
//    }

    public boolean checkIdAndPassword(Teacher teacher) {
        String userId = teacher.getId();

        Teacher findTeacher = teacherRepository.findById(userId)
                .orElseThrow(() -> new IdOrPasswordMismatchException());

        String userPw = teacher.getPw();
        String findUserPw = findTeacher.getPw();

        if (!(userPw.equals(findUserPw)))
            throw new IdOrPasswordMismatchException();

        return true;
    }

//    public LoginResponseForm formatLoginResponseForm(String accessToken, String refreshToken) {
//        return new LoginResponseForm(accessToken, refreshToken);
//    }
//
//    public AccessTokenReissuanceResponseForm formatAccessTokenReissuanceResponseForm(String accessToken, LocalDateTime accessTokenExpiration) {
//        return new AccessTokenReissuanceResponseForm(accessToken, accessTokenExpiration);
//    }

//    public void logout(String accessToken) {
//        if(jwtService.isUsableToken(accessToken)) {
//            String teacherId = jwtService.getTeacherId(accessToken);
//
//            Teacher findTeacher = teacherRepository.findById(teacherId)
//                    .orElseThrow(() -> new TokenExpirationException());
//
////            String refreshToken = findTeacher.getRefreshToken();
//
////            jwtService.killToken(refreshToken);
////            jwtService.killToken(accessToken);
//
//            findTeacher.setRefreshToken(null);
//        } else {
//            throw new TokenExpirationException();
//        }
//    }

    public void join(Teacher teacher) {
        teacher.setPw(encodingPassword(teacher.getPw()));
        teacherRepository.save(teacher);
    }
}