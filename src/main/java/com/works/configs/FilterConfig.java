package com.works.configs;

import com.works.entities.Company;
import com.works.entities.security.User;
import com.works.repositories._jpa.UserRepository;
import com.works.services.UserService;
import com.works.utils.Util;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.annotation.PreDestroy;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Configuration
public class FilterConfig implements Filter {

    final UserRepository userRepository;
    final UserService userService;

    public FilterConfig(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
        System.out.println("The System Started!");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse res = (HttpServletResponse) servletResponse;

        req.setCharacterEncoding("utf-8");
        res.setCharacterEncoding("utf-8");
        userService.info(req);
        getCompany();
        filterChain.doFilter(req, res);
    }

    public void getCompany() {
        try {
            Optional<User> optUser = userRepository.findByEmailEquals(SecurityContextHolder.getContext().getAuthentication().getName());
            if (optUser.isPresent()) {
                Util.theCompany = optUser.get().getCompany();
            } else {
                Util.theCompany = new Company();
            }
        } catch (Exception e) {
            Util.theCompany = new Company();
            System.out.println("Swagger Run");
        }

    }

    @PreDestroy
    public void destroy() {
        System.out.println("The System Shut Down!");
    }


}
