package com.sandy.project.service.impl;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.sandy.project.domain.AppUser;
import com.sandy.project.dto.UserDetailResponseDTO;
import com.sandy.project.exception.ResourceNotFoundException;
import com.sandy.project.repository.AppUserRepository;
import com.sandy.project.service.AppUserService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@Service
public class AppUserServiceImpl implements AppUserService {
    
    private AppUserRepository appUserRepository;
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Mencoba login dengan username: {}", username);
        
        try {
            AppUser user = appUserRepository.findByUsername(username)
                    .orElseThrow(() -> new ResourceNotFoundException("invalid.username"));
            
            log.info("User ditemukan: {}", username);
            log.info("User memiliki {} roles", user.getRoles().size());
            user.getRoles().forEach(role -> log.info("Role: {}", role.getName()));
            
            return user;
        } catch (Exception e) {
            log.error("Error saat mencari user: {}", e.getMessage());
            throw e;
        }
    }
    
    @Override
    public UserDetailResponseDTO findUserDetail() {
        SecurityContext ctx = SecurityContextHolder.getContext();
        UserDetailResponseDTO dto = new UserDetailResponseDTO();
        String username = ctx.getAuthentication().getName();
        dto.setUsername(username);
        return dto;
    }
}