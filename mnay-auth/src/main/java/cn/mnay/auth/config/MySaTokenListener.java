package cn.mnay.auth.config;

import cn.dev33.satoken.listener.SaTokenListener;
import cn.dev33.satoken.stp.SaLoginModel;
import cn.mnay.api.model.dto.auth.Auditor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 自定义侦听器的实现
 */
@Slf4j
@Component
public class MySaTokenListener implements SaTokenListener {

    @Override
    public void doLogin(String loginType, Object loginId, String tokenValue, SaLoginModel loginModel) {
        // 登录时设置当前登录用户信息
        Auditor.setCurrentMemberInfo((String) loginId);
        log.info("登录事件：{} -- {} -- {}", loginType, loginId, tokenValue);
    }

    @Override
    public void doLogout(String loginType, Object loginId, String tokenValue) {
        // 登出时清除当前登录用户信息
        Auditor.clearCurrentMemberInfo();
        log.info("登出事件：{} -- {}", loginType, loginId);
    }

    @Override
    public void doKickout(String loginType, Object loginId, String tokenValue) {
        log.info("踢下线事件：{} -- {}", loginType, loginId);
    }

    @Override
    public void doReplaced(String loginType, Object loginId, String tokenValue) {

    }

    @Override
    public void doDisable(String loginType, Object loginId, String service, int level, long disableTime) {

    }

    @Override
    public void doUntieDisable(String loginType, Object loginId, String service) {

    }

    @Override
    public void doOpenSafe(String loginType, String tokenValue, String service, long safeTime) {

    }

    @Override
    public void doCloseSafe(String loginType, String tokenValue, String service) {

    }

    @Override
    public void doCreateSession(String id) {

    }

    @Override
    public void doLogoutSession(String id) {

    }

    @Override
    public void doRenewTimeout(String tokenValue, Object loginId, long timeout) {

    }
}
