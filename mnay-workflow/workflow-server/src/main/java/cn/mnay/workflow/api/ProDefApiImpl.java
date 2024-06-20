package cn.mnay.workflow.api;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "ProDefApiImpl", description = "工作流-流程定义-接口")
public class ProDefApiImpl implements ProDefApi {
}
