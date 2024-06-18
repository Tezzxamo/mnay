package cn.mnay.api.service.auth;

import cn.mnay.api.model.dto.auth.MemberDTO;
import cn.mnay.api.model.request.auth.HttpAuthReq;
import cn.mnay.api.model.request.auth.HttpMemberModifySelfDTO;
import cn.mnay.api.model.request.auth.HttpMemberUpdateReq;
import cn.mnay.api.model.vo.auth.IdNameVO;

import java.util.List;

public interface MemberService {

    /**
     * 通过email判断是否存在
     *
     * @param memberEmail 邮箱
     * @return true->存在
     */
    boolean existsByMemberEmail(String memberEmail);

    /**
     * 根据成员dto信息保存/更新成员
     * 【接口权限需求:部门管理员】
     *
     * @param httpMemberUpdateReq 成员dto信息
     * @return MemberDTO
     */
    MemberDTO updateMember(HttpMemberUpdateReq httpMemberUpdateReq);

    /**
     * 创建member
     *
     * @param httpMemberUpdateReq 用户信息
     * @return 创建好的member信息
     */
    MemberDTO createMember(HttpMemberUpdateReq httpMemberUpdateReq);

    /**
     * 查询member
     *
     * @param memberEmail 邮箱
     * @return 用户信息
     */
    MemberDTO queryByEmail(String memberEmail);

    /**
     * 根据手机号查询
     *
     * @param memberPhone 手机号
     * @return 用户信息
     */
    MemberDTO queryByPhone(String memberPhone);

    /**
     * 获取member
     *
     * @param memberDTO 用户信息
     * @return member信息
     */
    List<MemberDTO> queryByNameLike(MemberDTO memberDTO);

    /**
     * 获取member
     *
     * @param memberName 用户信息
     * @return member信息
     */
    List<MemberDTO> queryByNameLike(String memberName);

    /**
     * 删除member
     *
     * @param memberDTO 用户信息
     * @return true->删除成功
     */
    Boolean deleteMember(MemberDTO memberDTO);

    /**
     * 找出id-name的结合
     *
     * @return IdNameVO
     */
    List<IdNameVO> listIdNameVO();

    /**
     * 更新登录时间
     *
     * @param memberDTO 成员
     */
    void updateLastLoginTime(MemberDTO memberDTO);

    /**
     * 修改自身信息
     * 【接口权限需求:当且仅当时用户自身，才可以调用该接口】
     *
     * @param modifyDTO 修改信息
     * @return 用户信息
     */
    MemberDTO modifySelf(HttpMemberModifySelfDTO modifyDTO);

    /**
     * 锁定用户
     *
     * @param memberId memberId
     * @param status   status
     * @return 用户信息
     */
    MemberDTO changeStatus(String memberId, Boolean status);

    /**
     * 通过id查询用户信息
     *
     * @param memberId 用户id
     * @return 用户信息
     */
    MemberDTO queryById(String memberId);

    /**
     * 重置密码
     * [接口权限需求：无]
     *
     * @param httpAuthReq 重置密码相关信息
     * @return 用户信息
     */
    MemberDTO resetPassword(HttpAuthReq httpAuthReq);

    /**
     * 获取当前登录用户信息
     *
     * @return 当前登录用户信息
     */
    MemberDTO current();

}
