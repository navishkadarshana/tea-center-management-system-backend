package com.praneeth.teaCenterManagement.config.security;


import com.praneeth.teaCenterManagement.config.security.custom.CustomOauthException;
import com.praneeth.teaCenterManagement.config.security.custom.CustomTokenEnhancer;
import com.praneeth.teaCenterManagement.service.authService.UserService;
import com.praneeth.teaCenterManagement.utilities.IPAddressHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;


import static org.springframework.http.HttpStatus.UNAUTHORIZED;





@Configuration
@EnableAuthorizationServer
@RequiredArgsConstructor
@Log4j2
public class AuthServerConfiguration extends AuthorizationServerConfigurerAdapter {

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final UserService userDetailsService;
    private final CustomTokenEnhancer customTokenEnhancer;
    private final Environment environment;

    private final HttpServletRequest request;

    private final IPAddressHandler ipAddressHandler;



    @Override
    public void configure(ClientDetailsServiceConfigurer configurer) throws Exception {

        configurer
                .inMemory()

                // USER
                .withClient(SecurityConstants.USER_ID)
                .secret(passwordEncoder.encode(""))
                .authorizedGrantTypes(SecurityConstants.GRANT_TYPE_PASSWORD, SecurityConstants.AUTHORIZATION_CODE, SecurityConstants.REFRESH_TOKEN, SecurityConstants.IMPLICIT)
                .scopes(SecurityConstants.SCOPE_READ, SecurityConstants.SCOPE_WRITE, SecurityConstants.TRUST)
                .accessTokenValiditySeconds(SecurityConstants.ADMIN_ACCESS_TOKEN_VALIDITY_SECONDS)
                .refreshTokenValiditySeconds(SecurityConstants.ADMIN_REFRESH_TOKEN_VALIDITY_SECONDS)

                // ADMIN
                .and()
                .withClient(SecurityConstants.ADMIN_ID)
                .secret(passwordEncoder.encode(""))
                .authorizedGrantTypes(SecurityConstants.GRANT_TYPE_PASSWORD, SecurityConstants.AUTHORIZATION_CODE, SecurityConstants.REFRESH_TOKEN, SecurityConstants.IMPLICIT)
                .scopes(SecurityConstants.SCOPE_READ, SecurityConstants.SCOPE_WRITE, SecurityConstants.TRUST)
                .accessTokenValiditySeconds(SecurityConstants.ADMIN_ACCESS_TOKEN_VALIDITY_SECONDS)
                .refreshTokenValiditySeconds(SecurityConstants.ADMIN_REFRESH_TOKEN_VALIDITY_SECONDS);
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        endpoints
                .tokenStore(tokenStore())
                .tokenEnhancer(tokenEnhancer())
                .authenticationManager(authenticationManager)
                .accessTokenConverter(accessTokenConverter())
                .userDetailsService(userDetailsService)
                .prefix(environment.getRequiredProperty("spring.mvc.servlet.path"))
                .exceptionTranslator(exception -> {
                    if (exception instanceof InvalidGrantException){
                        String username = request.getParameter("username");
                        Object details = SecurityContextHolder.getContext().getAuthentication().getDetails();
                        log.error("Authentication Failure IP Address : {}, username : {}" , ((WebAuthenticationDetails) details).getRemoteAddress(), username);
                        return ResponseEntity.status(UNAUTHORIZED).body(userDetailsService.loginFailed(username));
                    } else
                        return ResponseEntity.status(UNAUTHORIZED).body(new CustomOauthException(exception.getMessage()));
                });
    }

    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setSigningKey(SecurityConstants.TOKEN_SIGN_IN_KEY);
        return converter;
    }

    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(accessTokenConverter());
    }

    @Bean
    public TokenEnhancer tokenEnhancer() {
        TokenEnhancerChain enhancerChain = new TokenEnhancerChain();
        enhancerChain.setTokenEnhancers(Arrays.asList(customTokenEnhancer, accessTokenConverter()));
        return enhancerChain;
    }
}
