package com.web.SellShoes.config;

import javax.servlet.http.HttpSession;

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
			"/category/getAll",
			"/brand/getAll",
			"/color/getAll",
			"/size/getAll",
			"/product/getProductView",
			"/product/details",
			"/product/get",
			"/product/all-product",
			"/product/index",
			"/response/**",
			"/rate/getRateProductPage",
			"/password/**",
			"/rate/**",	
			"/variant/getVariant",
			
			"/login/checkLogin",
			"/register/**",
			"/userfeedback/**",
			"/feedback/saveUserfeedback",
			"/feedback/userfeedback",
			"/logins/**",
			"/registers/**",
			"/image/**",
			"/upload/**",
			"/plugins/**" ,
			"/adminview/**",
			"/shopview/**",
			
			"/account/**",
	};
	

	 private static final String[] ROLE_USER = {
			 "/cart/**",
			 "/userOrder/**",
			 "/order/user/all-order",
			 "/order/user/getOrderPage",
			 "/order/user/all-order",
			 "/rate/**",
			 "/order/updateStatus",
			 
	 };
	    
	 
   private static final String[] ROLE_ADMIN = {
		   "/order/admin/all",
		   "/promotion/admin",
		   "/promotion/getProductPage",
		   "/order/getOrderPage/**",
           "/order/admin/all",
           "/category/**",
           "/color/**",
           "/feedback/**",
           "/size/**",
           "/variant/**",
           
           "/brand/**"
   };
   
   private static final String[] COMMON_ROLES = {
		   "/promotion/getProductPage",
		   "/order/updateStatus",
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
                .and()
                .authorizeRequests()
                .antMatchers(ROLE_USER).hasAnyAuthority("USER","ADMIN")
                .and()
                .authorizeRequests()
                .antMatchers(ROLE_ADMIN).hasAuthority("ADMIN")
                .and()
                .authorizeRequests()
                .antMatchers(COMMON_ROLES).hasAnyAuthority("USER", "ADMIN")
                .and()
                .authorizeRequests()
                .anyRequest()
                //.permitAll()
                .authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .permitAll()
                .and()
                .logout()
                .logoutUrl("/logout") // Url của trang logout.
                
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                
                //.logoutSuccessUrl("/login")
                .permitAll()
                .logoutSuccessHandler((request, response, authentication) -> {
                    HttpSession session = request.getSession(false);
                    if (session != null) {
                        session.removeAttribute("email");
                        session.removeAttribute("fullName");
                    }
                });
                ;
        // Thêm một lớp Filter kiểm tra jwt
       // http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

    }
	
	
}

