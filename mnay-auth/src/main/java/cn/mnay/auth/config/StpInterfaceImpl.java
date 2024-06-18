package cn.mnay.auth.config;

import cn.dev33.satoken.stp.StpInterface;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 自定义权限、角色列表获取<br/>
 * 缓存关系：【用户id -> 角色id -> 权限码】
 */
@Component
public class StpInterfaceImpl implements StpInterface {



    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        return List.of();
    }

    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        return List.of();
    }
}
