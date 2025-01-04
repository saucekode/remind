package com.busyhill.remind;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
class RemindApplicationTests {

	@Autowired
	private MockMvc mvc;

	@Autowired
	JwtEncoder jwtEncoder;

	@Test
	void shouldReturnACashCardWhenDataIsSaved() throws Exception {
		this.mvc.perform(get("/cashcards/99"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(99))
				.andExpect(jsonPath("$.owner").value("sarah1"));
	}

	@Test
	@DirtiesContext
	void shouldCreateANewCashCard() throws Exception {
		String location = this.mvc.perform(post("/cashcards")
						.with(csrf())
						.contentType("application/json")
						.content("""
                        {
                            "amount" : 250.00,
                            "owner"  : "sarah1"
                        }
                        """))
				.andExpect(status().isCreated())
				.andExpect(header().exists("Location"))
				.andReturn().getResponse().getHeader("Location");

		this.mvc.perform(get(location))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.amount").value(250.00))
				.andExpect(jsonPath("$.owner").value("sarah1"));
	}


	@Test
	void shouldRequireValidTokens() throws Exception {
		String token = mint((claims) -> claims.audience(List.of("https://wrong")));
		this.mvc.perform(get("/cashcards/100").header(
				"Authorization", "Bearer " + token)
		).andExpect(status().isUnauthorized()
		).andExpect(header().string("WWW-Authenticate", containsString("aud claim is not valid")));
	}

	@Test
	void shouldNotAllowTokensThatAreExpired() throws Exception {
		String token = mint((claims) -> claims
				.issuedAt(Instant.now().minusSeconds(3600))
				.expiresAt(Instant.now().minusSeconds(3599))
		);
		this.mvc.perform(get("/cashcards/100").header("Authorization", "Bearer " + token))
				.andExpect(status().isUnauthorized())
				.andExpect(header().string("WWW-Authenticate", containsString("Jwt expired")));
	}

	private String mint() {
		return mint(consumer -> {});
	}

	private String mint(Consumer<JwtClaimsSet.Builder> consumer) {
		JwtClaimsSet.Builder builder = JwtClaimsSet.builder()
				.issuedAt(Instant.now())
				.expiresAt(Instant.now().plusSeconds(100000))
				.subject("sarah1")
				.issuer("http://localhost:9000")
				.audience(Arrays.asList("cashcard-client"))
				.claim("scp", Arrays.asList("cashcard:read", "cashcard:write"));
		consumer.accept(builder);
		JwtEncoderParameters parameters = JwtEncoderParameters.from(builder.build());
		return this.jwtEncoder.encode(parameters).getTokenValue();
	}


	@TestConfiguration
	static class TestJwtConfiguration {
		@Bean
		JwtEncoder jwtEncoder(@Value("classpath:authz.pub") RSAPublicKey pub,
							  @Value("classpath:authz.pem") RSAPrivateKey pem) {
			RSAKey key = new RSAKey.Builder(pub).privateKey(pem).build();
			return new NimbusJwtEncoder(new ImmutableJWKSet<>(new JWKSet(key)));
		}
	}
}
