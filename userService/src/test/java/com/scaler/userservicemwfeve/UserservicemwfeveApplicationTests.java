package com.scaler.userservicemwfeve;

import com.scaler.userservicemwfeve.security.repositories.JpaRegisteredClientRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.test.annotation.Commit;

import java.time.Duration;
import java.util.UUID;

@SpringBootTest
class UserservicemwfeveApplicationTests {
    @Autowired
    private JpaRegisteredClientRepository registeredClientRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Test
    void contextLoads() {
    }

    @Test
    @Commit
    void storeRegisteredClientIntoDB() {
                RegisteredClient oidcClient = RegisteredClient.withId(UUID.randomUUID().toString())
                .clientId("oidc-client")
                .clientSecret(passwordEncoder.encode("secret"))
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                .redirectUri("https://oauth.pstmn.io/v1/callback")
                .postLogoutRedirectUri("https://oauth.pstmn.io/v1/callback")
                .scope(OidcScopes.OPENID)
                .scope(OidcScopes.PROFILE)
                .scope("ADMIN")
                .scope("USER") // Role
                .clientSettings(ClientSettings.builder()
                        .requireAuthorizationConsent(true).build())
                        .tokenSettings(TokenSettings.builder()
                        .accessTokenTimeToLive(Duration.ofSeconds(3600))
                        .refreshTokenTimeToLive(Duration.ofSeconds(2419200))
                                .authorizationCodeTimeToLive(Duration.ofSeconds(3600))
                                .deviceCodeTimeToLive(Duration.ofSeconds(3600)
                                )
                .reuseRefreshTokens(true)
                .build())
                .build();

                registeredClientRepository.save(oidcClient);

    }

}
