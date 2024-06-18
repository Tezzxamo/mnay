package cn.mnay.oss.model.dbo;


import cn.mnay.common.model.dbo.BaseTimeUserEntity;
import cn.mnay.common.jpa.comment.annotation.ColumnComment;
import cn.mnay.common.jpa.comment.annotation.TableComment;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import static cn.mnay.common.constant.Constants.MYSQL_SCHEMA;

/**
 * <strong>【特性】：</strong><br/>
 * <p>1、储存时：主要储存字段不设置唯一键，用于可以重复保存，用于确保多线程环境下不会报错<br/>
 * 备注：</p>
 * <ul>
 * <li>但是一般不会有多条重复数据(即重复的fileHash)</li>
 * <li>如果没有会上传minio</li>
 * <li>可以对一个文件重复上传到minio中</li>
 * <li>在多线程环境下可能会导致重复记录，如果objectName相同，minio中只存在一份文件，如果不同，则minio中也会有两份文件</li>
 * <li>只会在时间间隔相当小（多线程）的情况下会发生同样fileHash存两个记录的情况</li><br/>
 * </ul>
 * <p>2、查询时：但是为了确保不会报错，查询时根据fileHash查询list，查出任意存在一条即可确定已经存在，则不再重复上传</p>
 */
@Data
@Entity
@Table(name = "mnay_file", schema = MYSQL_SCHEMA)
@DynamicInsert
@DynamicUpdate
@Accessors(chain = true)
@TableComment("文件存储对应表")
@EqualsAndHashCode(callSuper = true)
public class MnayFile extends BaseTimeUserEntity {

    @ColumnComment("文件名")
    private String fileName;
    @ColumnComment("文件哈希")
    private String fileHash;
    @ColumnComment("minio中桶名称")
    private String bucketName;
    @ColumnComment("minio中对象名")
    private String objectName;

}
