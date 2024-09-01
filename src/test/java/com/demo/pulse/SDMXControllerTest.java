package com.demo.pulse;

import com.demo.pulse.controller.SDMXController;
import com.demo.pulse.service.TokenService;
import com.demo.pulse.model.User;
import com.demo.pulse.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@WebMvcTest(SDMXController.class)
public class SDMXControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TokenService tokenService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    private String jwtToken;

    @BeforeEach
    public void setUp() {
        // Configure um usuário e um token JWT válido para os testes
        User user = new User("1", "testuser", "password123");

        jwtToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJsb2dpbi1hdXRoLWFwaSIsInN1YiI6InRlc3R1c2VyIiwiZXhwIjoxNzI0NzQ2NjMxfQ.-Rt_sHfn_HXtcFovMmOXMoIfC7xyRY686QAy_lXPh8k";  // Simule um token JWT

        Mockito.when(tokenService.validateToken(jwtToken)).thenReturn(user.getUsername());
        Mockito.when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
    }

    @Test
    public void testGetEconomicDataWithValidToken() throws Exception {
        // Faça a chamada para o endpoint protegido com o token JWT válido
        mockMvc.perform(get("/api/sdmx/data")
                        .header("Authorization", "Bearer " + jwtToken)
                        .param("dataflow", "EXR")
                        .param("key", "D.USD.EUR.SP00.A")
                        .param("startPeriod", "2021-07-01")
                        .param("endPeriod", "2021-07-30")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())  // Verifique se a resposta é 200 OK
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testGetEconomicDataWithoutToken() throws Exception {
        // Faça a chamada para o endpoint protegido sem o token JWT
        mockMvc.perform(get("/api/sdmx/data")
                        .param("dataflow", "EXR")
                        .param("key", "D.USD.EUR.SP00.A")
                        .param("startPeriod", "2021-07-01")
                        .param("endPeriod", "2021-07-30")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());  // Verifique se a resposta é 401 Unauthorized
    }

    @Test
    public void testGetEconomicDataWithInvalidToken() throws Exception {
        // Simule um token JWT inválido
        String invalidToken = "invalid_jwt_tokeneyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJsb2dpbi1hdXRoLWFwaSIsInN1YiI6InRlc3R1c2VyIiwiZXhwIjoxNzI0NzQ2NjMxfQ.-Rt_sHfn_HXtcFovMmOXMoIfC7xyRY686QAy_lXPh8k";
        Mockito.when(tokenService.validateToken(invalidToken)).thenReturn(null);

        mockMvc.perform(get("/api/sdmx/data")
                        .header("Authorization", "Bearer " + invalidToken)
                        .param("dataflow", "EXR")
                        .param("key", "D.USD.EUR.SP00.A")
                        .param("startPeriod", "2021-07-01")
                        .param("endPeriod", "2021-07-30")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());  // Verifique se a resposta é 401 Unauthorized
    }
}

