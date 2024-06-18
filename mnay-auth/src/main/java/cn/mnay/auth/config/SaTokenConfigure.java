package cn.mnay.auth.config;

import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.jwt.StpLogicJwtForSimple;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpLogic;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.collection.ListUtil;
import cn.mnay.api.model.dto.auth.Auditor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class SaTokenConfigure implements WebMvcConfigurer {

    // Sa-Token 整合 jwt (Simple 简单模式)
    @Bean
    public StpLogic getStpLogicJwt() {
        return new StpLogicJwtForSimple();
    }


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册 Sa-Token 拦截器，定义详细认证规则
        registry.addInterceptor(new SaInterceptor(handler -> {
            SaRouter.match("/**")
                    // 要执行的校验动作，可以写完整的 lambda 表达式
                    .check(r -> {
                        // ①校验是否登录
                        StpUtil.checkLogin();
                        // ②设置当前用户信息
                        Auditor.setCurrentMemberInfo((String) StpUtil.getLoginId());
                    });
            // 根据路由划分模块，不同模块不同鉴权
            // SaRouter.match("/user/**", r -> StpUtil.checkPermission("user"));
        })).addPathPatterns(includePathPatterns()).excludePathPatterns(excludePathPatterns());
    }

    /**
     * @return 需要鉴权的路由
     */
    private List<String> includePathPatterns() {
        return ListUtil.toList("/**");
    }

    /**
     * @return 无须鉴权的路由
     */
    private List<String> excludePathPatterns() {
        return ListUtil.toList(
                // 放行xxx

                // 放行登录
                "/login/password",
                "/login/phone",
                "/login/out",
                // 放行swagger页面、静态资源等
                "/v3/api-docs",
                "/**/*.html",
                "/**/*.css",
                "/**/*.js",
                "/**/api-docs/**",
                "/**/*.png",
                "/js/**",
                "/css/**",
                "/images/*",
                "/fonts/**",
                "/**/*.jpg",
                "/swagger-ui/",
                "/swagger-resources",
                "/api/**"
        );
    }

}
