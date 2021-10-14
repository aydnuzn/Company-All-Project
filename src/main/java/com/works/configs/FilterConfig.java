package com.works.configs;

import com.works.entities.security.User;
import com.works.repositories._jpa.UserRepository;
import com.works.utils.Util;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Configuration
public class FilterConfig implements Filter {

    //final HttpServletRequest httpServletRequest;
    //final HttpServletResponse httpServletResponse;
    final UserRepository userRepository;

    public FilterConfig(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse res = (HttpServletResponse) servletResponse;

        req.setCharacterEncoding("utf-8");
        res.setCharacterEncoding("utf-8");
		getCompany();
        filterChain.doFilter(req, res);
    }

    // Proje kapanıp acıldıgı zaman Company null oluyor.
    public void getCompany() {
        Optional<User> optUser = userRepository.findByEmailEquals(SecurityContextHolder.getContext().getAuthentication().getName());
        if (optUser.isPresent()) {
            Util.theCompany = optUser.get().getCompany();
        }
    }

    // Proje ilk çalıştıgında 1 kez tetiklenir
    @PostConstruct
    private void postConstruct() {
        
    }

    // Proje kapanırken tetikleniyor
    @PreDestroy
    public void destroy() {
        System.out.println("Callback triggered - @PreDestroy.");
    }





}
