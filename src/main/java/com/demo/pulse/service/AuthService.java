package com.demo.pulse.service;
// @author Leu A. Manuel
// @see https://github.com/Leupesquisa

import com.demo.pulse.dto.LoginRequestDTO;
import com.demo.pulse.dto.RegisterRequestDTO;
import com.demo.pulse.dto.ResponseDTO;
import com.demo.pulse.model.User;
import com.demo.pulse.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public ResponseDTO<String> registerUser(RegisterRequestDTO registerRequestDTO) {
        if (userRepository.findByUsername(registerRequestDTO.username()).isPresent()) {
            return new ResponseDTO<>("Username already taken", null);
        }

        String encodedPassword = passwordEncoder.encode(registerRequestDTO.password());
        User newUser = new User(null, registerRequestDTO.username(), encodedPassword);
        userRepository.save(newUser);

        String token = tokenService.generateToken(newUser);
        return new ResponseDTO<>("User registered successfully", token);
    }

    public ResponseDTO<String> authenticateUser(LoginRequestDTO loginRequestDTO) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequestDTO.username(), loginRequestDTO.password())
            );

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            User user = userRepository.findByUsername(userDetails.getUsername())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            String token = tokenService.generateToken(user);

            return new ResponseDTO<>("Authentication successful", token);
        } catch (Exception e) {
            return new ResponseDTO<>("Invalid username or password", null);
        }
    }
}
