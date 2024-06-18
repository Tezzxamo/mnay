package cn.mnay.auth.model.repo;

import cn.mnay.api.model.dao.SimpleIdNameDAO;
import cn.mnay.auth.model.dbo.Company;
import cn.mnay.common.constant.Constants;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface CompanyRepo extends JpaRepository<Company, String> {

    /**
     * 判断是否以及存在该组织名称
     *
     * @param companyName 组织名称
     * @return true->存在
     */
    boolean existsByCompanyName(String companyName);

    /**
     * 通过id查询name
     *
     * @param companyId id
     * @return name
     */
    @Query(value = "select ac.company_name from " + Constants.AUTH_COMPANY + " ac where ac.id = ?1", nativeQuery = true)
    String findCompanyNameById(String companyId);

    /**
     * 查询id-name的map
     *
     * @param ids id集合
     * @return list map
     */
    @Query(value = "select ac.company_name as companyName, ac.id as id from " + Constants.AUTH_COMPANY + " ac where ac.id in ?1", nativeQuery = true)
    List<Map<String, String>> findIdNameMap(List<String> ids);

    /**
     * 通过companyName查询Company
     *
     * @param companyName 组织名称
     * @return 组织
     */
    Optional<Company> findByCompanyName(String companyName);

    /**
     * 找出所有的id-name集合
     *
     * @return id-name集合
     */
    @Query(value = "select ac.id as id, ac.company_name as name from " + Constants.AUTH_COMPANY + " ac", nativeQuery = true)
    List<SimpleIdNameDAO> listIdNameVO();

}
