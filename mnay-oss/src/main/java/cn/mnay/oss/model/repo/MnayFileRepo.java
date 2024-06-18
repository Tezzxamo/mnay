package cn.mnay.oss.model.repo;

import cn.mnay.oss.model.dbo.MnayFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MnayFileRepo extends JpaRepository<MnayFile, String> {

    /**
     * 根据文件哈希查询
     *
     * @param fileHash 文件哈希
     * @return {@link MnayFile}
     */
    List<MnayFile> findByFileHash(String fileHash);


}
