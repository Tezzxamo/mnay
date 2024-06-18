package cn.mnay.common.constant;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Constants {

    /**
     * 数据库名称
     */
    public static final String MYSQL_SCHEMA = "mnay";

    /**
     * <table cellspacing=8 cellpadding=0>
     *  <tr style="background-color: rgb(255, 255, 255);">
     *      <th>【auth】模块表名</th>
     *      <th>说明</th>
     *  </tr>
     *  <tr style="background-color: rgb(1, 255, 255);"> <td>AUTH_SCHEMA</td> <td>postgresql 使用的 schema</td> </tr>
     *  <tr style="background-color: rgb(1, 255, 255);"> <td>AUTH_MEMBER</td> <td>成员表</td> </tr>
     *  <tr style="background-color: rgb(1, 255, 255);"> <td>AUTH_ROLE</td> <td>角色表</td> </tr>
     *  <tr style="background-color: rgb(1, 255, 255);"> <td>AUTH_MEMBER_ROLE</td> <td>成员-角色表</td> </tr>
     *  <tr style="background-color: rgb(1, 255, 255);"> <td>AUTH_DEPARTMENT</td> <td>部门表</td> </tr>
     *  <tr style="background-color: rgb(1, 255, 255);"> <td>AUTH_MEMBER_DEPARTMENT</td> <td>成员-部门表</td> </tr>
     *  <tr style="background-color: rgb(1, 255, 255);"> <td>AUTH_COMPANY</td> <td>组织表</td> </tr>
     *  <tr style="background-color: rgb(1, 255, 255);"> <td>AUTH_PERMISSION</td> <td>权限表</td> </tr>
     *  <tr style="background-color: rgb(1, 255, 255);"> <td>AUTH_ROLE_PERMISSION</td> <td>角色-权限表</td> </tr>
     *  <tr style="background-color: rgb(1, 255, 255);"> <td>AUTH_MENU</td> <td>菜单表</td> </tr>
     * </table>
     */
    public static final String AUTH_SCHEMA = "auth";
    public static final String AUTH_MEMBER = "auth_member";
    public static final String AUTH_ROLE = "auth_role";
    public static final String AUTH_MEMBER_ROLE = "auth_member_role";
    public static final String AUTH_DEPARTMENT = "auth_department";
    public static final String AUTH_MEMBER_DEPARTMENT = "auth_member_department";
    public static final String AUTH_COMPANY = "auth_company";
    public static final String AUTH_PERMISSION = "auth_permission";
    public static final String AUTH_ROLE_PERMISSION = "auth_role_permission";
    public static final String AUTH_MENU = "auth_menu";

    /**
     * auth部分index
     */
    public static final String INDEX_MEMBER_ROLE = "auth_idx_member_role__member_id_role_id";
    public static final String INDEX_ROLE_PERMISSION = "auth_idx_role_permission_id_role_id";
    public static final String INDEX_MEMBER_DEPARTMENT = "auth_idx_member_department_id_department_id";

    /**
     * <table cellspacing=8 cellpadding=0>
     *     <tr style="background-color: rgb(255, 255, 255);">
     *         <th>【oss】模块表名</th>
     *         <th>说明</th>
     *     </tr>
     *     <tr style="background-color: rgb(1, 255, 255);"> <td>MNAY_FILE</td> <td>文件表</td> </tr>
     * </table>
     */
    public static final String MNAY_FILE = "mnay_file";


}
