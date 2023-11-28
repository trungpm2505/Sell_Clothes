package com.web.SellShoes.config;

import javax.servlet.Filter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import com.web.SellShoes.jwt.JwtAuthenticationFilter;
import com.web.SellShoes.serviceImpl.AccountServiceImpl;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	private final AccountServiceImpl userService;
	private final JwtAuthenticationFilter jwtAuthenticationFilter;
	 
	private static final String[] NO_LOG_IN = {
			"/password/**",
			"/rate/**",
			"/orderDetails/**",
			"/order/**",
			"/promotion/**",
			"/variant/**",
			"/color/**",
			"/size/**",
			"/category/**",
			"/brand/**",
			"/product/**",
			"/login/**",
			"/register/**",
			"/image/**",
			"/upload/**",
			"/plugins/**" ,
			"/adminview/**",
			"/shopview/**",
			"/registers/**",
			"/logins/**",
			"/shop/**",
			"/cart/**",
			"/userOrder/**",
	};
	

	 private static final String[] ROLE_USER = {
			
	 };
	    
	 
   private static final String[] ROLE_ADMIN = {

          
           
   };
 
	@Bean(BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        // Get AuthenticationManager bean
        return super.authenticationManagerBean();
    }
	
	//quản lý dữ liệu người dùng
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		 auth.userDetailsService(userService) // Cung cáp userservice cho spring security, kiểm tra quyền người dùng
         .passwordEncoder(passwordEncoder()); // cung cấp password encoder
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		// Password encoder, để Spring Security sử dụng mã hóa mật khẩu người dùng
		return new BCryptPasswordEncoder();
	}
	
    
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(NO_LOG_IN);
    }
	
    //pphân quyền sử dụng và đăng nhập
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf()
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
            .and()
            .cors().and()
            .authorizeRequests()
                .antMatchers(NO_LOG_IN).permitAll()
                .antMatchers(ROLE_USER).hasAuthority("USER") // Thêm role cho các đường dẫn trong ROLE_USER
                .antMatchers(ROLE_ADMIN).hasAuthority("ADMIN") // Thêm role cho các đường dẫn trong ROLE_ADMIN
                .anyRequest().authenticated() // Tất cả các request còn lại yêu cầu xác thực
            .and()
            .formLogin()
                .loginPage("/login")
                .permitAll()
            .and()
            .rememberMe()
            .key("lodaaaaaa") 
            .tokenValiditySeconds(86400)
            .userDetailsService(userService)
            .and()
            .logout()
                .logoutUrl("/logout")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
                .logoutSuccessHandler((request, response, authentication) -> {
                   // HttpSession session = request.getSession(false);
                });
        // Thêm một lớp Filter kiểm tra jwt
        http.addFilterBefore((Filter) jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }

	
	
}

