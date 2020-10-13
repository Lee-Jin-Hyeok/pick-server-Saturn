package com.dsm.pick.controller;

import com.dsm.pick.domains.domain.Teacher;
import com.dsm.pick.domains.service.AuthService;
import com.dsm.pick.domains.service.JwtService;
import com.dsm.pick.utils.exception.TokenExpirationException;
import com.dsm.pick.utils.exception.TokenInvalidException;
import com.dsm.pick.utils.form.*;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/auth")
@Api(value = "Auth Controller")
public class AuthController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final AuthService authService;
    private final JwtService jwtService;

    @Autowired
    public AuthController(AuthService authService, JwtService jwtService) {
        this.authService = authService;
        this.jwtService = jwtService;
    }

    @ApiOperation(value = "로그인", notes = "AccessToken 및 RefreshToken 생성")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK!!"),
            @ApiResponse(code = 400, message = "Non Exist ID or Password"),
            @ApiResponse(code = 404, message = "Mismatch ID or Password"),
            @ApiResponse(code = 410, message = "Incorrect Encoding or Algorithm"),
            @ApiResponse(code = 500, message = "500???")
    })
    @PostMapping("/login")
    public LoginResponseForm login(@RequestBody TeacherRequestForm teacherRequestForm) {
        Teacher teacher = new Teacher();
        teacher.setId(teacherRequestForm.getId());
        teacher.setPw(teacherRequestForm.getPw());
        teacher.existIdOrPassword();

        String encodedPassword = authService.encodingPassword(teacher.getPw());
        teacher.setPw(encodedPassword);

        LoginResponseForm result = null;
        if(authService.checkIdAndPassword(teacher)) {
            String teacherId = teacher.getId();

            String accessToken = jwtService.createAccessToken(teacherId);
            String refreshToken = jwtService.createRefreshToken(teacherId);
            String teacherName = authService.getTeacherName(teacherId);

            result = new LoginResponseForm(accessToken, refreshToken, teacherName);
        }

        return result;
    }

    @ApiOperation(value = "정상적인 토큰인지 확인", notes = "정상적인 토큰인지 확인")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK!!"),
            @ApiResponse(code = 401, message = "Token Expiration"),
            @ApiResponse(code = 403, message = "Token Invalid"),
            @ApiResponse(code = 500, message = "500???")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", dataType = "string", required = true, value = "Token")
    })
    @PostMapping("/token")
    public void isUsableToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");

        boolean isValid = jwtService.isValid(token);
        if(!isValid) {
            throw new TokenInvalidException("토큰이 잘못 되었습니다.");
        }

        boolean isNotTimeOut = jwtService.isNotTimeOut(token);
        if(!isNotTimeOut) {
            throw new TokenExpirationException("토큰이 만료 되었습니다.");
        }
    }

    @ApiOperation(value = "엑세스 토큰 만료", notes = "엑세스 토큰 재발급")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK!!"),
            @ApiResponse(code = 403, message = "Token Invalid"),
            @ApiResponse(code = 404, message = "Mismatch Refresh Token"),
            @ApiResponse(code = 500, message = "500???")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", dataType = "string", required = true, value = "Refresh Token")
    })
    @GetMapping("/access-token")
    public AccessTokenReissuanceResponseForm accessTokenReissuance(HttpServletRequest request) {
        String refreshToken = request.getHeader("Authorization");

        boolean isValid = jwtService.isValid(refreshToken);
//        boolean isNotTimeOut = jwtService.isNotTimeOut(refreshToken);

        if(isValid) {
            String teacherId = jwtService.getTeacherId(refreshToken);
            String accessToken = jwtService.createAccessToken(teacherId);
            return new AccessTokenReissuanceResponseForm(accessToken);
        } else {
            throw new TokenInvalidException("토큰이 잘못 되었습니다.");
        }
    }

//    @ApiOperation(value = "로그아웃", notes = "JWT 토큰 제거")
//    @ApiResponses({
//            @ApiResponse(code = 200, message = "NO Return Success"),
//            @ApiResponse(code = 404, message = "NOT User"),
//            @ApiResponse(code = 500, message = "500인데 이거 안 뜰듯")
//    })
//    @ApiImplicitParams({
//            @ApiImplicitParam(paramType = "header", name = "token", dataType = "string", required = true, value = "Access Token")
//    })
//    @DeleteMapping("/access-refresh-token")
//    public void logout(HttpServletRequest request) {
//        authService.logout(request.getHeader("token"));
//    }

//    @ApiOperation(value = "테스트라고 했다 이거 보고 뭐라 하지 마라", notes = "마마 이거 왜 여노?")
//    @PostMapping("/join")
//    public void join(Teacher teacher) {
//        authService.join(teacher);
//    }
}