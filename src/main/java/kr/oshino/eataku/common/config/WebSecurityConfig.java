package kr.oshino.eataku.common.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Autowired
    private CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;

    @Bean
    public WebSecurityCustomizer configure() {
        return (web) -> web.ignoring()
                .requestMatchers(
                        new AntPathRequestMatcher("/css/**"),
                        new AntPathRequestMatcher("/js/**"),
                        new AntPathRequestMatcher("/images/**"),
                        new AntPathRequestMatcher("/favicon.ico"),
                        new AntPathRequestMatcher("/error")
                );    //static관련 핸들러 메소드에서 필터링 제외시킴.
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(auth -> auth.disable())
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests

                        // 인덱스 페이지 및 정적 파일
                        .requestMatchers("/", "/css/**", "/js/**", "/images/**").permitAll()

                        // 모든 사용자가 접근 가능한 URL
                        .requestMatchers("/search/**","/signUp/checkDupAccount","/signUp/checkDupNickname","/signUp/checkEmailVerifCode","/signUp/sendEmailVerifCode").permitAll()
                        .requestMatchers("/restaurant/**").permitAll()
                        .requestMatchers("/admin/waiting/stream/**").permitAll()

                        // 비로그인 사용자만 접근 가능한 URL
                        .requestMatchers("/login/**", "/signUp/**", "/managerLogin/**", "/managerLogin").anonymous()

                        // 일반사용자(User)만 접근 가능한 URL
                        .requestMatchers("/test/user").hasRole("general")

                        // 관리자(Manager)만 접근 가능한 URL
                        .requestMatchers("/test/admin").hasRole("manager")

                        // 그 외 모든 페이지는 로그인한 모든 사용자 접근가능
                        .anyRequest().authenticated()

                )
                .formLogin(formLogin -> formLogin
                        .loginPage("/login")
                        .loginProcessingUrl("/loginProc")
                        .successHandler(customAuthenticationSuccessHandler)
                )
                .logout((logout) -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true)
                        .permitAll()
                );
//        http
//                .csrf(auth -> auth.disable());
        http
                .sessionManagement((auth) -> auth
                        .maximumSessions(1)
                        .maxSessionsPreventsLogin(false)
                );
        http
                .sessionManagement((auth) -> auth
                        .sessionFixation().changeSessionId()
                );

        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
