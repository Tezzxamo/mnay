package cn.mnay.api.util;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.mnay.api.model.dto.auth.Auditor;
import cn.mnay.api.model.dto.auth.MemberDTO;
import cn.mnay.common.model.dto.auth.MemberInfo;
import cn.mnay.api.service.auth.MemberService;
import cn.mnay.common.enums.error.CodeEnum;
import cn.mnay.common.exception.BusinessException;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.net.InetAddress;
import java.util.Optional;
import java.util.stream.Stream;

@Slf4j
@Component
@Configuration
@ConfigurationProperties(prefix = "mnay.auth")
public class TokenUtil {

    // redisTemplate
    private static RedisTemplate<String, Object> redisTemplate;

    /**
     * 从请求体中获取token并返回
     */
    public static Optional<String> extractTokenString(HttpServletRequest httpServletRequest) {
        String authToken = httpServletRequest.getHeader("Authorization");
        return Optional.ofNullable(authToken).map((token) -> token.startsWith("Bearer ") ? token.substring("Bearer ".length()) : null);
    }

    public static void setCurrentUserInfo(String memberId) {
        if (!StringUtils.isBlank(memberId)) {
            // 得到memberId
            // 通过id找到用户信息存入线程变量
            MemberService memberService = SpringUtil.getBean(MemberService.class);
            MemberDTO memberDTO = memberService.queryById(memberId);
            Auditor.setCurrentMemberInfo(MemberInfo.builder()
                    .memberId(memberId)
                    .memberName(memberDTO.getMemberName())
                    .memberType(memberDTO.getMemberType())
                    .memberEmail(memberDTO.getMemberEmail())
                    .locked(memberDTO.getLocked())
                    .token(StpUtil.getTokenValue())
                    .build());
            Auditor.getCurrentMemberInfo();
        }
        log.error("memberId 为空,无法获取对应用户信息");
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    public static void removeCurrentUserInfo() {
        Auditor.clearCurrentMemberInfo();
    }

    /**
     * 获取本机IP
     */
    public static String getIP() {
        try {
            String IP = Optional.ofNullable((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                    .map((v) -> getClientIP(v.getRequest()))
                    .orElse(InetAddress.getByName(InetAddress.getLocalHost().getHostName()).getHostAddress());
            String ip = "0:0:0:0:0:0:0:1";
            if (StrUtil.equals(ip, IP)) {
                IP = "127.0.0.1";
            }
            return IP;
        } catch (Exception e) {
            log.error("获取本地IP失败：{}", e.getLocalizedMessage());
            throw new BusinessException(CodeEnum.GET_CURRENT_IP_ERROR);
        }
    }

    private static String getClientIP(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        // 处理多个代理的情况，返回第一个非unknown的IP
        return Stream.of(ip.split(","))
                .filter(p -> !"unknown".equalsIgnoreCase(p.trim()))
                .findFirst()
                .orElse(ip);
    }


    @Resource
    public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
        TokenUtil.redisTemplate = redisTemplate;
    }


}
