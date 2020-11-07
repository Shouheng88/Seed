package com.seed.portal.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * User detail service. The administrator username and password were
 * saved in yml configuration as default. You can also load data from
 * other kinds of data sources like MySQL, Redis etc.
 *
 * @author <a href="mailto:shouheng2015@gmail.com">Shouheng.W</a>
 * @version 1.0
 * @date 2020/11/6 17:55
 */
@Slf4j
@Service("userDetailsService")
@Transactional(rollbackFor = Exception.class)
public class UserDetailsServiceImpl implements UserDetailsService {

    @Value("${com.seed.admin.username}")
    private String adminUserName;

    @Value("${com.seed.admin.password}")
    private String adminPassword;

    @Resource
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("username [{}]", username);
        if (!adminUserName.equals(username)) {
            throw new UsernameNotFoundException("User name not found : " + username);
        }
        return new User(username, passwordEncoder.encode(adminPassword),
                AuthorityUtils.commaSeparatedStringToAuthorityList("admin"));
    }

}
