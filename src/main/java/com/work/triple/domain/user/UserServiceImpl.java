package com.work.triple.domain.user;

import com.work.triple.common.exception.BadRequestException;
import com.work.triple.common.exception.NotFoundException;
import com.work.triple.common.exception.UnAuthorizedException;
//import com.work.triple.config.jwt.JwtTokenProvider;
import com.work.triple.domain.user.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Transactional
@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user =getUser(username);

        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        user.getRoles().forEach(role-> authorities.add(new SimpleGrantedAuthority(role.getName())));
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
    }

    @Override
    public UserDto saveUser(CreateUserDto user) {
        Optional<User> optionalUser = userRepository.findByUsername(user.getUsername());
        if (optionalUser.isPresent()){
            throw new BadRequestException("duplicateUser");
        }
        Role role = roleRepository.save(Role.builder()
                .name("ROLE_USER").build());
        User saveUser = userRepository.save(User.builder()
                .username(user.getUsername())
                .password(passwordEncoder.encode(user.getPassword())) // 패스워드 암호화해서 저장
                .roles(List.of(role)) // 일반 유저 역할을 부여함
                .build());
        return new UserDto(saveUser);
    }

    @Override
    public User getUser(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> {
            throw new NotFoundException("userNotFound");
        });
    }

    @Override
    public RoleDto saveRole(String roleName) {
        Role role= roleRepository.saveAndFlush(Role.builder().name(roleName).build());
        return modelMapper.map(role, RoleDto.class);
    }

}
