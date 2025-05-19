package com.basicauth.basicauth;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.basicauth.basicauth.auth.entity.User;
import com.basicauth.basicauth.auth.repository.AuthRepository;

@SpringBootTest
@AutoConfigureMockMvc
class BasicauthApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private AuthRepository authRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@BeforeEach
	void setUp() {
		// Clean up any existing test user
		authRepository.findByUsername("testuser").ifPresent(authRepository::delete);

		// Create test user
		User testUser = new User();
		testUser.setUsername("testuser");
		testUser.setPassword(passwordEncoder.encode("password123"));
		authRepository.save(testUser);
	}

	@Test
	void contextLoads() {
	}

	@Test
	void whenAccessingProtectedEndpointWithoutAuth_thenReturns401() throws Exception {
		mockMvc.perform(get("/orders"))
			.andExpect(status().isUnauthorized());
	}

	@Test
	void whenAccessingAuthEndpointWithoutAuth_thenReturns200() throws Exception {
		mockMvc.perform(post("/auth/register")
			.contentType(MediaType.APPLICATION_JSON)
			.content("{\"username\":\"newuser\",\"password\":\"password123\"}"))
			.andExpect(status().isOk());
	}

	@Test
	void whenAccessingProtectedEndpointWithAuth_thenReturns200() throws Exception {
		mockMvc.perform(get("/orders")
			.with(SecurityMockMvcRequestPostProcessors.httpBasic("testuser", "password123")))
			.andExpect(status().isOk());
	}
}
