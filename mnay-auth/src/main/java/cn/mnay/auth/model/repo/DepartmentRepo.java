package cn.mnay.auth.model.repo;

import cn.mnay.api.model.dao.SimpleIdNameDAO;
import cn.mnay.api.model.dao.SingleGroupConcatDAO;
import cn.mnay.auth.model.dbo.Department;
import cn.mnay.common.constant.Constants;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface DepartmentRepo extends JpaRepository<Department, String> {

    /**
     * 通过id查询name
     *
     * @param departmentId id
     * @return name
     */
    @Query(value = "select ad.department_name from " + Constants.AUTH_DEPARTMENT + " ad where ad.id = ?1", nativeQuery = true)
    String findDepartmentNameById(String departmentId);

    /**
     * 查询id-name的map
     *
     * @param ids id集合
     * @return list map
     */
    @Query(value = "select ad.department_name as departmentName, ad.id as id from " + Constants.AUTH_DEPARTMENT + " ad where ad.id in ?1", nativeQuery = true)
    List<Map<String, String>> findIdNameMap(List<String> ids);

    /**
     * 根据组织id查询所有最顶级部门(通过自关联，顶级部门就会查出所有的多级部门)
     *
     * @param companyId 组织id
     * @return 该组织下的所有部门
     */
    List<Department> findAllByParentDepartmentIdIsNullAndCompany_Id(String companyId);

    /**
     * 相同组织id和相同部门名称的部门存在的话，则返回true
     *
     * @param departmentName 部门名称
     * @param companyId      组织id
     * @return true->存在
     */
    boolean existsByDepartmentNameAndCompany_Id(String departmentName, String companyId);

    /**
     * 找出一个组织下的所有部门
     *
     * @param companyId 组织id
     * @return 部门list
     */
    List<Department> findAllByCompany_Id(String companyId);

    /**
     * 获取所有的部门id
     *
     * @return idList
     */
    @Query(value = "select id from " + Constants.AUTH_DEPARTMENT, nativeQuery = true)
    List<String> findAllDepartmentId();

    /**
     * 通过组织id获取所有的部门id
     *
     * @param companyId 组织id
     * @return idList
     */
    @Query(value = "select id from " + Constants.AUTH_DEPARTMENT + " where company_id = ?1", nativeQuery = true)
    List<String> findAllDepartmentIdByCompanyId(String companyId);

    /**
     * 找出所有的父部门Id的连接
     *
     * @param childId 子部门id
     * @return concatString
     */
    @Query(value = "with recursive t as" +
            " (                                                                                             " +
            " select * from " + Constants.AUTH_DEPARTMENT + " where id = ?1                                 " +
            " union all                                                                                     " +
            " select a.* from " + Constants.AUTH_DEPARTMENT + " a join t on a.id = t.parent_department_id   " +
            " )                                                                                             " +
            "select group_concat(t.id order by t.department_level SEPARATOR '_') as concatString from t     ", nativeQuery = true)
    SingleGroupConcatDAO groupConcatParentIdByChildId(String childId);

    /**
     * 找出所有的父部门
     *
     * @param childId 子部门id
     * @return 所有的父部门
     */
    @Query(value = "with recursive t as" +
            " (                                                                                             " +
            " select * from " + Constants.AUTH_DEPARTMENT + " where id = ?1                                 " +
            " union all                                                                                     " +
            " select a.* from " + Constants.AUTH_DEPARTMENT + " a join t on a.id = t.parent_department_id   " +
            " )                                                                                             " +
            "select t.* from t ", nativeQuery = true)
    List<Department> listParentByChildId(String childId);

    /**
     * 找出所有的父部门(只有父部门,且不包含他们本身)
     *
     * @param childIdList 子部门idList
     * @return 所有的父部门(只有父部门, 且不包含他们本身)
     */
    @Query(value = "with recursive t as" +
            " (                                                                                             " +
            " select * from " + Constants.AUTH_DEPARTMENT + " where id in ?1                                " +
            " union all                                                                                     " +
            " select a.* from " + Constants.AUTH_DEPARTMENT + " a join t on a.id = t.parent_department_id   " +
            " )                                                                                             " +
            "select distinct t.* from t " +
            "where t.id not in ?1", nativeQuery = true)
    List<Department> listParentWithoutChildByChildIdList(List<String> childIdList);

    /**
     * 找出所有的子部门+本部门
     *
     * @param parentId 父部门id
     * @return 所有的子部门
     */
    @Query(value = "with recursive t as " +
            " (                                                                                             " +
            " select * from " + Constants.AUTH_DEPARTMENT + " where id = ?1                                 " +
            " union all                                                                                     " +
            " select a.* from " + Constants.AUTH_DEPARTMENT + " a join t on a.parent_department_id = t.id   " +
            " )                                                                                             " +
            "select * from t ", nativeQuery = true)
    List<Department> listChildByParentId(String parentId);

    /**
     * 找出所有的子部门+本部门  的id
     *
     * @param parentId 父部门id
     * @return 所有的子部门+本部门  的id
     */
    @Query(value = "with recursive t as " +
            " (                                                                                             " +
            " select * from " + Constants.AUTH_DEPARTMENT + " where id = ?1                                 " +
            " union all                                                                                     " +
            " select a.* from " + Constants.AUTH_DEPARTMENT + " a join t on a.parent_department_id = t.id   " +
            " )                                                                                             " +
            "select t.id from t ", nativeQuery = true)
    List<String> listChildIdByParentId(String parentId);

    /**
     * 通过idList找出所有组织id
     *
     * @param idList 部门idList
     * @return 组织idList
     */
    @Query(value = " select distinct company_id from " + Constants.AUTH_DEPARTMENT + " where id in ?1 ", nativeQuery = true)
    List<String> findDistinctCompanyById(List<String> idList);

    /**
     * 找出所有的id-name集合
     *
     * @return id-name集合
     */
    @Query(value = "select id as id, department_name as name from " + Constants.AUTH_DEPARTMENT, nativeQuery = true)
    List<SimpleIdNameDAO> listIdNameVO();
}
