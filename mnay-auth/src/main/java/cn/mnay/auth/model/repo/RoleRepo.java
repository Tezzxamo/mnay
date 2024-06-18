package cn.mnay.auth.model.repo;

import cn.mnay.auth.model.dbo.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoleRepo extends JpaRepository<Role, String> {

    /**
     * 是否存在相同角色
     *
     * @param companyId    组织id
     * @param departmentId 部门id
     * @param roleName     角色名称
     * @return true->存在
     */
    boolean existsByCompanyIdAndDepartmentIdAndRoleName(String companyId, String departmentId, String roleName);

    /**
     * 通过companyId,departmentId查询管理员
     *
     * @param companyId    组织id
     * @param departmentId 部门id
     * @return Role
     */
    Optional<Role> findByCompanyIdAndDepartmentIdAndAdminIsTrue(String companyId, String departmentId);

    /**
     * 通过companyId,departmentId查询
     *
     * @param companyId    组织id
     * @param departmentId 部门id
     * @return 角色列表
     */
    List<Role> queryByCompanyIdAndDepartmentId(String companyId, String departmentId);

}
