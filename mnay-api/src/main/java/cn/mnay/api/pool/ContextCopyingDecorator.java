package cn.mnay.api.pool;

import cn.mnay.api.model.dto.auth.Auditor;
import cn.mnay.common.model.dto.auth.MemberInfo;
import jakarta.annotation.Nonnull;
import org.springframework.core.task.TaskDecorator;

/**
 * 用于将父线程的某些值传递到子线程中去
 */
public class ContextCopyingDecorator implements TaskDecorator {

    @Nonnull
    @Override
    public Runnable decorate(@Nonnull Runnable runnable) {
        MemberInfo memberInfo = Auditor.getCurrentMemberInfo();

        return () -> {
            try {
                Auditor.setCurrentMemberInfo(memberInfo);
                runnable.run();
            } finally {
                Auditor.clearCurrentMemberInfo();
            }
        };
    }
}
