package cn.mnay.auth.model.repo;

import cn.mnay.api.model.dao.SimpleIdNameDAO;
import cn.mnay.auth.model.dbo.Member;
import cn.mnay.common.constant.Constants;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface MemberRepo extends JpaRepository<Member, String> {

    /**
     * 获取存在的id数量
     *
     * @param idList id列表
     * @return 存在的id的数量
     */
    long countByIdIn(List<String> idList);

    /**
     * 通过邮箱(唯一)判断是否存在
     *
     * @param memberEmail 邮箱(唯一)
     * @return true->存在
     */
    boolean existsByMemberEmail(String memberEmail);

    /**
     * 通过id查询name
     *
     * @param memberId id
     * @return name
     */
    @Query(value = "select member_name from " + Constants.AUTH_MEMBER + " m where m.id = ?1", nativeQuery = true)
    String findMemberNameById(String memberId);

    /**
     * 查询id-name的map
     *
     * @param ids id集合
     * @return list map
     */
    @Query(value = "select member_name as memberName,id as id from " + Constants.AUTH_MEMBER + " m where m.id in ?1", nativeQuery = true)
    List<Map<String, String>> findIdNameMap(List<String> ids);

    /**
     * 通过memberEmail查询member
     *
     * @param memberEmail 邮箱(唯一)
     * @return 成员
     */
    Optional<Member> findByMemberEmail(String memberEmail);

    /**
     * 通过memberPhone查询member
     *
     * @param memberPhone 手机号
     * @return 成员
     */
    Optional<Member> findByMemberPhone(String memberPhone);

    /**
     * 通过memberName查询member
     *
     * @param memberName 成员名称
     * @return 成员
     */
    Optional<Member> findByMemberName(String memberName);

    /**
     * 通过memberName模糊查询
     *
     * @param memberName 成员名称
     * @return 模糊查询结果
     */
    List<Member> queryByMemberNameLike(String memberName);

    /**
     * 通过部门id查询成员
     *
     * @param departmentId 部门id
     * @return 成员集合
     */
    List<Member> findAllByDepartmentId(String departmentId);

    /**
     * 通过部门id查询成员
     *
     * @param departmentIds 部门ids
     * @return 成员集合
     */
    List<Member> findAllByDepartmentIdIn(Collection<String> departmentIds);

    /**
     * 更新登录时间
     *
     * @param lastLoginTime 登录时间
     * @param memberId      成员id
     */
    @Modifying
    @Transactional(rollbackFor = Exception.class)
    @Query(value = "update " + Constants.AUTH_MEMBER + " SET " + Constants.AUTH_MEMBER + ".last_login_time = ?1 where " + Constants.AUTH_MEMBER + ".id = ?2", nativeQuery = true)
    void updateLastLoginTime(LocalDateTime lastLoginTime, String memberId);

    /**
     * 查询所有成员名称
     *
     * @return 成员名称集合
     */
    @Query(value = "select member_name from  " + Constants.AUTH_MEMBER, nativeQuery = true)
    List<String> findAllMemberName();

    /**
     * 找出所有的id-name集合
     *
     * @return id-name集合
     */
    @Query(value = "select id as id, member_name as name from " + Constants.AUTH_MEMBER, nativeQuery = true)
    List<SimpleIdNameDAO> listIdNameVO();


}
