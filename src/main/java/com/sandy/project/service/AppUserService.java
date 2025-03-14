package com.sandy.project.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.sandy.project.dto.UserDetailResponseDTO;

public interface AppUserService extends UserDetailsService {

    public UserDetailResponseDTO findUserDetail();
}
