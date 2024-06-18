package cn.mnay.api.model.dao;

public interface MemberPermissionDAO {
    /**
     * memberId
     *
     * @return memberId
     */
    String getMemberId();

    /**
     * memberName
     *
     * @return memberName
     */
    String getMemberName();

    Boolean getAdmin();

    String getDepartmentId();

    String getCompanyId();

    /**
     * permissionName
     *
     * @return permissionName
     */
    String getPermissionName();
}
