package noticeboard.preferences;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.InMemoryOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import lombok.RequiredArgsConstructor;
import noticeboard.security.JwtAuthenticationFilter;
import noticeboard.security.JwtTokenProvider;
import noticeboard.security.oauth.CustomOAuth2UserService;
import noticeboard.security.oauth.OAuth2Provider;

@RequiredArgsConstructor
@EnableWebSecurity
@PropertySource("classpath:application.properties")
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	JwtTokenProvider jwtTokenProvider;
	
	private static String CLIENT_PROPERTY_KEY = "spring.security.oauth2.client.registration.";
    private static List<String> clients = Arrays.asList("google", "kakao");
    @Resource private Environment env;
    @Autowired CustomOAuth2UserService customOAuth2UserService;
    
    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        List<ClientRegistration> registrations = clients.stream()
                .map(c -> getRegistration(c))
                .filter(registration -> registration != null)
                .collect(Collectors.toList());
        return new InMemoryClientRegistrationRepository(registrations);
    }
    
    private ClientRegistration getRegistration(String client) {
        // API Client Id ????????????
        String clientId = env.getProperty(
                CLIENT_PROPERTY_KEY + client + ".client-id");

        // API Client Id ?????? ??????????????? ????????????
        if (clientId == null) {
            return null;
        }

        // API Client Secret ????????????
        String clientSecret = env.getProperty(
                CLIENT_PROPERTY_KEY + client + ".client-secret");

        if (client.equals("google")) {
            return OAuth2Provider.GOOGLE.getBuilder(client)
                    .clientId(clientId)
                    .clientSecret(clientSecret)
                    .build();
        }
        
        if (client.equals("kakao")) {
            return OAuth2Provider.KAKAO.getBuilder(client)
                    .clientId(clientId)
                    .clientSecret(clientSecret)
                    .build();
        }

        return null;
    }
    

	@Bean
	public OAuth2AuthorizedClientService authorizedClientService() {
		return new InMemoryOAuth2AuthorizedClientService(clientRegistrationRepository());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable(). // ???????????? ????????? ?????? ?????? ???????????? ????????? ??????, header??? id, pw??? ?????? token(jwt)??? ?????? ??????. ????????? basic???
		
		// ?????? bearer??? ????????????.
			httpBasic().disable().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Session
				// ????????????
				.and().authorizeRequests()// ????????? ?????? ???????????? ??????
				.antMatchers("/auth/**").authenticated() // ????????? ????????? url
				.antMatchers("/auth/**").hasRole("ADMIN") // url ?????? ??? ????????? Role
				.antMatchers("/auth/**").hasRole("USER") // ??????
				.antMatchers("/**").permitAll()	//permitAll() ?????? ?????? ????????? ?????? .anyRequest() ?????????
				.and().logout() //.logoutSuccessUrl("/refreshToken")
				.deleteCookies("refreshToken")
                .and()
				.oauth2Login()
				.loginPage("/expireAccess")
				.clientRegistrationRepository(clientRegistrationRepository())
				.authorizedClientService(authorizedClientService())
				;
				// ??????????????? ?????? ??? ?????? ????????? ????????? UserService ??????????????? ????????? ??????

		http.addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), // ??????
				UsernamePasswordAuthenticationFilter.class); // JwtAuthenticationFilter???
		// UsernamePasswordAuthenticationFilter ?????? ?????????

	}
	
	@Bean
    public CorsConfigurationSource corsConfigurationSource() {
        final CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://tomcatServer:8080"));
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        configuration.setAllowCredentials(true);
        
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
	
}