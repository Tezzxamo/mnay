package cn.mnay.controller.global;

import cn.mnay.common.exception.BusinessException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/health")
public class HealthController {

    @GetMapping("/get")
    public String getHealth() {
        return "health";
    }

    @GetMapping("/test")
    public String getTest() {
        throw new BusinessException();
    }

}
