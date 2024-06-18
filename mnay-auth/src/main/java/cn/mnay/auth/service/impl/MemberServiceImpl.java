package cn.mnay.auth.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.lang.Assert;
import cn.mnay.api.model.dto.auth.Auditor;
import cn.mnay.api.model.dto.auth.MemberDTO;
import cn.mnay.api.model.request.auth.HttpAuthReq;
import cn.mnay.api.model.request.auth.HttpMemberModifySelfDTO;
import cn.mnay.api.model.request.auth.HttpMemberUpdateReq;
import cn.mnay.api.model.vo.auth.IdNameVO;
import cn.mnay.api.service.auth.MemberService;
import cn.mnay.auth.model.dbo.Member;
import cn.mnay.auth.model.repo.MemberRepo;
import cn.mnay.auth.model.wrapper.MemberWrapper;
import cn.mnay.common.enums.error.CodeEnum;
import cn.mnay.common.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepo memberRepo;


    @Override
    public boolean existsByMemberEmail(String memberEmail) {
        return memberRepo.existsByMemberEmail(memberEmail);
    }

    @Override
    public MemberDTO updateMember(HttpMemberUpdateReq httpMemberUpdateReq) {
        return null;
    }

    @Override
    public MemberDTO createMember(HttpMemberUpdateReq httpMemberUpdateReq) {
        return null;
    }

    @Override
    public MemberDTO queryByEmail(String memberEmail) {
        // 查询
        Member member = memberRepo.findByMemberEmail(memberEmail)
                .orElseThrow(() -> new BusinessException(CodeEnum.USER_GET_ERROR));
        return MemberWrapper.INSTANCE.to(member);
    }

    @Override
    public MemberDTO queryByPhone(String memberPhone) {
        // 查询
        Member member = memberRepo.findByMemberPhone(memberPhone)
                .orElseThrow(() -> new BusinessException(CodeEnum.USER_GET_ERROR));
        return MemberWrapper.INSTANCE.to(member);
    }

    @Override
    public List<MemberDTO> queryByNameLike(MemberDTO memberDTO) {
        return this.queryByNameLike(memberDTO.getMemberName());
    }

    @Override
    public List<MemberDTO> queryByNameLike(String memberName) {
        //
        List<Member> members = memberRepo.queryByMemberNameLike("%" + memberName + "%");
        // 如果查询结果为空直接返回
        if (members.isEmpty()) {
            return ListUtil.toList();
        }
        // 填充后返回
        return MemberWrapper.INSTANCE.to(members);
    }

    @Override
    public Boolean deleteMember(MemberDTO memberDTO) {
        // 断言存在该用户，否则报错
        Assert.isTrue(memberRepo.existsById(memberDTO.getId()), () -> new BusinessException(CodeEnum.USER_GET_ERROR));

        // 开始删除
        memberRepo.deleteById(memberDTO.getId());
        return true;
    }

    @Override
    public List<IdNameVO> listIdNameVO() {
        return memberRepo.listIdNameVO().stream()
                .map(vo -> new IdNameVO().setId(vo.getId()).setName(vo.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public void updateLastLoginTime(MemberDTO memberDTO) {
        memberRepo.updateLastLoginTime(memberDTO.getLastLoginTime(), memberDTO.getId());
    }

    @Override
    public MemberDTO modifySelf(HttpMemberModifySelfDTO modifyDTO) {
        return null;
    }

    @Override
    public MemberDTO changeStatus(String memberId, Boolean status) {
        // 确认用户存在
        Member member = findById(memberId);
        // 设置
        member.setLocked(status);
        // 保存修改并返回
        return MemberWrapper.INSTANCE.to(memberRepo.saveAndFlush(member));
    }

    @Override
    public MemberDTO queryById(String memberId) {
        return MemberWrapper.INSTANCE.to(findById(memberId));
    }

    @Override
    public MemberDTO resetPassword(HttpAuthReq httpAuthReq) {
        Member member = findByMemberEmail(httpAuthReq.getMemberEmail());
        member.setMemberPassword(httpAuthReq.getMemberPassword());
        MemberDTO memberDTO = MemberWrapper.INSTANCE.to(memberRepo.saveAndFlush(member));
        // 重置密码后需要重新登录
        StpUtil.logout();
        return memberDTO;
    }

    @Override
    public MemberDTO current() {
        MemberDTO memberDTO = MemberWrapper.INSTANCE.to(findById(Auditor.getCurrentMemberIdOrThrow()));
        memberDTO.setToken(StpUtil.getTokenValue());
        return memberDTO;
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * 无权限控制，仅供当前service的查询方法
     * 用于获取成员或抛出异常
     */
    private Member findById(String memberId) {
        return memberRepo.findById(memberId).orElseThrow(() -> new BusinessException(CodeEnum.USER_GET_ERROR));
    }

    private Member findByMemberEmail(String memberEmail) {
        return memberRepo.findByMemberEmail(memberEmail).orElseThrow(() -> new BusinessException(CodeEnum.USER_GET_ERROR));
    }

}
