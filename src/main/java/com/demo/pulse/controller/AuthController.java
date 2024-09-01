package com.demo.pulse.controller;
// @author Leu A. Manuel
// @see https://github.com/Leupesquisa

import com.demo.pulse.dto.LoginRequestDTO;
import com.demo.pulse.dto.RegisterRequestDTO;
import com.demo.pulse.dto.ResponseDTO;
import com.demo.pulse.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ResponseDTO<String>> login(@RequestBody LoginRequestDTO body) {
        ResponseDTO<String> response = authService.authenticateUser(body);
        if (response.data() != null) {  // Verifica se a autenticação foi bem-sucedida
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseDTO<String>> register(@RequestBody RegisterRequestDTO body) {
        ResponseDTO<String> response = authService.registerUser(body);
        if (response.data() != null) {  // Verifica se o registro foi bem-sucedido
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }
    }
}
