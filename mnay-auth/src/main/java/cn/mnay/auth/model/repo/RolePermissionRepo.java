package cn.mnay.auth.model.repo;

import cn.mnay.auth.model.dbo.middle.RolePermission;
import cn.mnay.common.constant.Constants;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RolePermissionRepo extends JpaRepository<RolePermission, String> {

    /**
     * 通过角色id查询RolePermission的集合
     *
     * @param roleIds 角色ids
     * @return RolePermission的集合
     */
    List<RolePermission> findDistinctByRoleIdIn(List<String> roleIds);

    /**
     * 删除指定权限类型在RolePermission表中的关联关系
     * (sql注意点:permission_type在此处不能是PermissionTypeEnum，而是在数据库存储的值的字符串格式)
     *
     * @param roleId 角色id
     * @param type   权限类型
     */
    @Modifying
    @Query(value = "delete arp from " + Constants.AUTH_ROLE_PERMISSION + " arp                                      " +
            " left join " + Constants.AUTH_PERMISSION + " ap on arp.permission_id = ap.id and ap.permission_type=?2 " +
            " where arp.role_id =?1 and ap.permission_type=?2 ", nativeQuery = true)
    void deleteByRoleIdAndPermissionType(String roleId, String type);

    /**
     * 如果指定角色拥有权限则返回true
     *
     * @param roleId 角色id
     * @return true->存在
     */
    boolean existsByRoleId(String roleId);

}
