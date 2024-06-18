package cn.mnay.api.model.vo.auth;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class IdNameVO {

    private String id;
    private String name;

}
