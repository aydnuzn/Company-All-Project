package com.works.services;

import com.works.entities.Logger;
import com.works.entities.security.Role;
import com.works.entities.security.User;
import com.works.repositories._jpa.LoggerRepository;
import com.works.repositories._jpa.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService extends SimpleUrlLogoutSuccessHandler implements UserDetailsService, LogoutSuccessHandler {
    final UserRepository uRepo;
    final UserRepository userRepository;
    final LoggerRepository loggerRepository;

    public UserService(UserRepository uRepo, UserRepository userRepository, LoggerRepository loggerRepository) {
        this.uRepo = uRepo;
        this.userRepository = userRepository;
        this.loggerRepository = loggerRepository;
    }

    // security login
    @Override
    public UserDetails loadUserByUsername(String email) {
        UserDetails userDetails = null;
        Optional<User> oUser = uRepo.findByEmailEquals(email);
        if (oUser.isPresent()) {
            User us = oUser.get();
            userDetails = new org.springframework.security.core.userdetails.User(
                    us.getEmail(),
                    us.getPassword(),
                    us.isEnabled(),
                    us.isTokenExpired(),
                    true,
                    true,
                    getAuthorities(us.getRoles()));
        } else {
            throw new UsernameNotFoundException("Kullanıcı adı yada şifre hatalı");
        }
        return userDetails;
    }

    private List<GrantedAuthority> getAuthorities(List<Role> roles) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (Role role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.getRo_name()));
        }
        return authorities;
    }

    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        System.out.println("onLogoutSuccess Call ");
        httpServletResponse.sendRedirect(httpServletRequest.getContextPath() + "/homepanel/login/login");
    }

    public void info(HttpServletRequest req) throws IOException {
        Logger logger = new Logger();
        String email = "";
        try {
            Authentication aut = SecurityContextHolder.getContext().getAuthentication();
            email = aut.getName(); // username
        } catch (Exception e) {

        }

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes()).getRequest();
        String ip = request.getRemoteAddr();
        //System.out.println("ip " + ip);

        String session = req.getSession().getId();
        //System.out.println("session :" + session);

        Optional<User> user = userRepository.findByEmailEquals(email);
        if (user.isPresent()) {
            logger.setLname(user.get().getName());
            logger.setLsurname(user.get().getSurname());
            String roles = "";
            for (Role item : user.get().getRoles()) {
                roles += item.getRo_name() + ", ";
            }
            if (roles.length() > 0) {
                roles = roles.substring(0, roles.length() - 2);
            }
            logger.setLroles(roles);
        }

        logger.setLemail(email);
        logger.setLsessionId(session);
        logger.setLIp(ip);

        logger.setLUrl(req.getRequestURI());
        logger.setLDate(new Date());

        loggerRepository.save(logger);

    }

}
