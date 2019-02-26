package com.pershing.security.config;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import com.pershing.security.JwtAccessHandler;
import com.pershing.security.JwtAuthenticationEntryPoint;
import com.pershing.security.JwtAuthenticationFailureHandler;
import com.pershing.security.JwtAuthenticationFilter;
import com.pershing.security.JwtAuthenticationProvider;
import com.pershing.security.JwtLoginProccessFilter;
import com.pershing.security.JwtSuccessHandler;
import com.pershing.security.JwtUseFormAuthentiactionProvider;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private JwtAuthenticationProvider authenticationProvider;

	@Autowired
	private JwtUseFormAuthentiactionProvider jwtUseFormAuthentiactionProvider;

	@Autowired
	private JwtAuthenticationFailureHandler jwtAuthenticationFailureHandler;

	@Bean
	public AuthenticationManager authenticationManager() {
		return new ProviderManager(Collections.singletonList(authenticationProvider));
	}

	@Bean
	public AuthenticationEntryPoint authenticationEntryPoint() {
		return new JwtAuthenticationEntryPoint();
	}

	@Bean
	public AccessDeniedHandler accessDeniedHandler() {
		return new JwtAccessHandler();
	}

	/**
	 * 主要設定
	 */
	@Override
	protected void configure(HttpSecurity web) throws Exception {

		web.csrf().disable().authorizeRequests().antMatchers("/login/login", "/error").permitAll().anyRequest()
				.authenticated().and()
				
				//.exceptionHandling().authenticationEntryPoint(authenticationEntryPoint()).and()
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().formLogin().loginPage("/login/login").permitAll();

		web.addFilterBefore(loginProccessFilter(), UsernamePasswordAuthenticationFilter.class);
		web.addFilterBefore(authenticationFilter(), UsernamePasswordAuthenticationFilter.class);
		web.headers().cacheControl();

	}

	/**
	 * 不檢查這些資源(html、js、css或是圖片等等)
	 */
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/resources/**", "/static/**", "/css/**", "/js/**", "/images/**");
	}

	/**
	 * 產生登入用過濾器的bean
	 * 
	 * @return
	 */
	@Bean
	public JwtLoginProccessFilter loginProccessFilter() {
		JwtLoginProccessFilter filter = new JwtLoginProccessFilter("/login", authenticationManager());

		// 設置providerManager
		filter.setAuthenticationManager(
				new ProviderManager(Collections.singletonList(jwtUseFormAuthentiactionProvider)));

		filter.setAuthenticationSuccessHandler(new JwtSuccessHandler());
		filter.setAuthenticationFailureHandler(jwtAuthenticationFailureHandler);
		return filter;

	}

	/**
	 * 登入後jwt驗證用過濾器bean
	 * 
	 * @return
	 */
	public JwtAuthenticationFilter authenticationFilter() {

		// 這些路徑的交易要做jwt驗證、可以將這邊的交易清單設定在properties
		List<String> pathsToSkip = Arrays.asList("/login/index", "/ap1/**", "/ap2/**");

		List<RequestMatcher> matchers = pathsToSkip.stream().map(path -> new AntPathRequestMatcher(path))
				.collect(Collectors.toList());

		JwtAuthenticationFilter filter = new JwtAuthenticationFilter(new OrRequestMatcher(matchers));
		filter.setAuthenticationManager(authenticationManager());
		filter.setAuthenticationSuccessHandler(new JwtSuccessHandler());
		filter.setAuthenticationFailureHandler(jwtAuthenticationFailureHandler);
		return filter;
	}
}
