package com.sandy.project.security.provider;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.sandy.project.service.AppUserService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@AllArgsConstructor
public class UsernamePasswordAuthProvider implements AuthenticationProvider {
	
	private final AppUserService appUserService;
	
	private final PasswordEncoder passwordEncoder;
	
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String username = (String) authentication.getPrincipal();
		String password = (String) authentication.getCredentials();
		
		log.info("Melakukan autentikasi untuk username: {}", username);
		
		try {
			UserDetails userDetails = appUserService.loadUserByUsername(username);
			log.info("User ditemukan, memeriksa password...");
			
			if(!passwordEncoder.matches(password, userDetails.getPassword())) {
				log.error("Password tidak cocok untuk user: {}", username);
				throw new BadCredentialsException("invalid.username.password");
			}
			
			log.info("Autentikasi berhasil untuk user: {}", username);
			return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
		} catch (Exception e) {
			log.error("Error autentikasi: {}", e.getMessage());
			throw e;
		}
	}
	
	@Override
	public boolean supports(Class<?> authentication) {
		return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
	}
}