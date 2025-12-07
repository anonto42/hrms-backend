//package com.hrmf.hrms_backend.service;
//
//import com.hrmf.hrms_backend.entity.CustomUserDetails;
//import com.hrmf.hrms_backend.entity.User;
//import com.hrmf.hrms_backend.repository.UserRepository;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//@Slf4j
//@Service
//@RequiredArgsConstructor
//public class CustomUserDetailsService implements UserDetailsService {
//
//    private final UserRepository userRepository;
//
//    @Override
//    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
//        log.debug("Looking up user by email: {}", email);
//        User user = userRepository.findByEmail(email.toLowerCase().trim())
//                .orElseThrow(() -> {
//                    log.error("User not found: {}", email);
//                    return new UsernameNotFoundException("User not found");
//                });
//
//        log.debug("Found user: {}, password field value: {}",
//                user.getEmail(),
//                user.getHashPassword() != null ? "[HAS VALUE]" : "NULL");
//
//        return new CustomUserDetails(user);
//    }
//}