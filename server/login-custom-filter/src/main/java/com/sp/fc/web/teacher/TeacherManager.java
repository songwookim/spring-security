package com.sp.fc.web.teacher;


import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Set;

// 통행증 발급 Authentication provider
@Component
public class TeacherManager implements AuthenticationProvider, InitializingBean {
    private HashMap<String, Teacher> teacherDB = new HashMap<>();

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;
        if(teacherDB.containsKey(token.getName())) {
            Teacher teacher = teacherDB.get(token.getName());
            return TeacherAuthenticationToken.builder()
                    .principal(teacher)
                    .credentials(null)
                    .details(teacher.getUsername())
                    .authenticated(true)
                    .build();
        }
        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication == UsernamePasswordAuthenticationToken.class;
    }
    @Override
    public void afterPropertiesSet() throws Exception {
        Set.of(
                new Teacher("teacher1", "선생님1", Set.of(new SimpleGrantedAuthority("ROLE_TEACHER"))),
                new Teacher("teacher2", "선생님2", Set.of(new SimpleGrantedAuthority("ROLE_TEACHER")))
        ).forEach(s ->
                teacherDB.put(s.getId(), s)
        );
    }
}
