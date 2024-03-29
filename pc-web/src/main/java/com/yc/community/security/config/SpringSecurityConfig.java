package com.yc.community.security.config;

import com.yc.community.security.component.AccessDecisionProcessor;
import com.yc.community.security.component.JwtAuthenticationTokenFilter;
import com.yc.community.security.component.RestAuthenticationEntryPoint;
import com.yc.community.security.component.RestfulAccessDeniedHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.vote.UnanimousBased;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.expression.WebExpressionVoter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Arrays;
import java.util.List;


/**
 * <p>
 * SpringSecurity配置文件
 * </p>
 *
 * @author yc001
 * @since 2020-06-30
 */
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;

    /**
     * 自定义访问控制，默认是所有访问都要经过认证。
     *
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests()
                // 放行所有OPTIONS请求
                .antMatchers(HttpMethod.OPTIONS).permitAll()
                // 放行登录方法
                .antMatchers("/api/web/sys/**").permitAll()
                .antMatchers("/websocket/**").permitAll()
                .antMatchers("/chatWebsocket").permitAll()
                // 其他请求都需要认证后才能访问
                .anyRequest().authenticated()
                // 使用自定义的 accessDecisionManager
                .accessDecisionManager(accessDecisionManager())
                .and()
                // 添加未登录与权限不足异常处理器
                .exceptionHandling()
                .accessDeniedHandler(restfulAccessDeniedHandler())
                .authenticationEntryPoint(restAuthenticationEntryPoint())
                .and()
                // 将自定义的JWT过滤器放到过滤链中
                .addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class)
                // 打开Spring Security的跨域
                .cors()
                .and()
                // 关闭CSRF
                .csrf().disable()
                // 关闭Session机制
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Bean
    public RestfulAccessDeniedHandler restfulAccessDeniedHandler() {
        return new RestfulAccessDeniedHandler();
    }

    @Bean
    public RestAuthenticationEntryPoint restAuthenticationEntryPoint() {
        return new RestAuthenticationEntryPoint();
    }

    @Bean
    public AccessDecisionVoter<FilterInvocation> accessDecisionProcessor() {
        return new AccessDecisionProcessor();
    }

    @Bean
    public AccessDecisionManager accessDecisionManager() {
        // 构造一个新的AccessDecisionManager 放入两个投票器
        List<AccessDecisionVoter<?>> decisionVoters = Arrays.asList(new WebExpressionVoter(), accessDecisionProcessor());
        return new UnanimousBased(decisionVoters);
    }

}
