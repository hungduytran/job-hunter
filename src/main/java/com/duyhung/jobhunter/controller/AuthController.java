package com.duyhung.jobhunter.controller;

import com.duyhung.jobhunter.domain.dto.LoginDTO;
import com.duyhung.jobhunter.domain.dto.ResLoginDTO;
import com.duyhung.jobhunter.util.SecurityUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final SecurityUtil securityUtil;

    public AuthController(AuthenticationManagerBuilder authenticationManagerBuilder, SecurityUtil securityUtil) {
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.securityUtil = securityUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<ResLoginDTO> login(@RequestBody LoginDTO loginDTO) {
        //Nạp input gồm username/password vào Security
        UsernamePasswordAuthenticationToken authenticationToken
                = new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword());
//xác thực người dùng => cần viết hàm loadUserByUsername
        Authentication authentication =
                authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        //create a token
        String access_token = this.securityUtil.createToken(authentication);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        ResLoginDTO resLoginDTO = new ResLoginDTO();
        resLoginDTO.setAccesstoken(access_token);
        return ResponseEntity.ok().body(resLoginDTO);
    }
}
