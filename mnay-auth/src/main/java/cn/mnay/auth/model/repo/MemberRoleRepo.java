package cn.mnay.auth.model.repo;

import cn.mnay.auth.model.dbo.middle.MemberRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRoleRepo extends JpaRepository<MemberRole, String> {

    /**
     * 通过角色Id获取MemberRole
     *
     * @param roleId   角色Id
     * @param memberId 成员id
     * @return MemberRole
     */
    Optional<MemberRole> findByMemberIdAndRoleId(String memberId, String roleId);

    /**
     * 通过memberId查询所有角色id
     *
     * @param memberId 成员id
     * @return MemberRole的集合
     */
    List<MemberRole> findByMemberId(String memberId);

    /**
     * 根据memberId和RoleId删除关联关系
     *
     * @param memberId 成员id
     * @param roleId   角色id
     */
    void deleteByMemberIdAndRoleId(String memberId, String roleId);

}
