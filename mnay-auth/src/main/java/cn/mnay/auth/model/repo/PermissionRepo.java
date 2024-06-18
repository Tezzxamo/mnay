package cn.mnay.auth.model.repo;

import cn.mnay.api.enums.auth.PermissionTypeEnum;
import cn.mnay.api.model.dao.MemberPermissionDAO;
import cn.mnay.auth.model.dbo.Permission;
import cn.mnay.common.constant.Constants;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface PermissionRepo extends JpaRepository<Permission, String> {

    /**
     * 通过权限类型查询权限需求
     *
     * @param type 权限类型
     * @return 该权限类型下的所有权限需求
     */
    List<Permission> findByPermissionType(PermissionTypeEnum type);

    /**
     * 通过permissionName查询
     *
     * @param permissionNameList 权限名称
     * @param type               权限类型
     * @return 权限列表
     */
    List<Permission> queryByPermissionNameInAndPermissionType(List<String> permissionNameList, PermissionTypeEnum type);

    /**
     * 通过权限类型查询权限需求
     *
     * @param ids 权限ids
     * @return 该权限类型下的所有权限需求
     */
    List<Permission> findByIdIn(List<String> ids);

    /**
     * 通过权限id列表、权限类型查询权限需求
     *
     * @param ids  权限ids
     * @param type 权限类型
     * @return 该权限类型下的所有权限需求
     */
    List<Permission> findByIdInAndPermissionType(List<String> ids, PermissionTypeEnum type);

    /**
     * 通过权限名称列表、权限类型查询权限需求
     *
     * @param permissionNames 权限名称列表
     * @param type            权限类型
     * @return 该权限类型下的所有权限需求
     */
    List<Permission> findByPermissionNameInAndPermissionType(Collection<String> permissionNames, PermissionTypeEnum type);

    /**
     * 通过权限名称和权限类型判断是否存在
     *
     * @param permissionName 权限名称
     * @param type           权限类型
     * @return 存在->true
     */
    boolean existsByPermissionNameAndPermissionType(String permissionName, PermissionTypeEnum type);

    /**
     * 通过权限名称列表、权限类型查询权限需求
     *
     * @param permissionName 权限名称
     * @param type           权限类型
     * @return 该权限类型下的所有权限需求
     */
    Optional<Permission> findByPermissionNameAndPermissionType(String permissionName, PermissionTypeEnum type);


    @Query(value = " select m.id as memberId, m.member_name as memberName,r.admin as admin,r.company_id as companyId, r.department_id as departmentId,p.permission_name as permissionName from " + Constants.AUTH_PERMISSION + " p  " +
            "   inner join " + Constants.AUTH_ROLE_PERMISSION + " rp on rp.permission_id = p.id " +
            "   inner join " + Constants.AUTH_ROLE + " r on r.id = rp.role_id                   " +
            "   inner join " + Constants.AUTH_MEMBER_ROLE + " mr on mr.role_id=r.id" +
            "   inner join " + Constants.AUTH_MEMBER + " m on m.id=mr.member_id" +
            "   where p.permission_name in ?1                                 ",
            nativeQuery = true
    )
    List<MemberPermissionDAO> queryMemberPermissionDTO(List<String> permissionNameList);

}
