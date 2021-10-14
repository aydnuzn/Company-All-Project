package com.works.configs;

import org.springframework.context.annotation.Configuration;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

@Configuration
public class SessionListener implements HttpSessionListener {

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        System.out.println("sessionCreated Call");
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        System.out.println("sessionDestroyed Call");
    }

}