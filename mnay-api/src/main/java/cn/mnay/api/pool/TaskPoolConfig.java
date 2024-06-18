package cn.mnay.api.pool;

import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Objects;
import java.util.concurrent.ThreadPoolExecutor;

@Data
@EnableAsync
@Configuration
@ConfigurationProperties(prefix = "mnay.async-thread-pool")
@EnableConfigurationProperties(TaskPoolConfig.class)
@ConditionalOnProperty(
        prefix = "mnay.async-thread-pool",
        name = "enable",
        havingValue = "true"
)
public class TaskPoolConfig implements AsyncConfigurer, WebMvcConfigurer {

    /**
     * 是否启动异步线程池，默认false；
     */
    private boolean enable;
    /**
     * 核心线程数,默认：Java虚拟机可用线程数
     */
    private Integer corePoolSize = Runtime.getRuntime().availableProcessors() * 2;  // IO密集型
    /**
     * 线程池最大线程数,默认：Math.max(corePoolSize * 4, 256)
     */
    private Integer maxPoolSize = Math.max(corePoolSize * 4, 256);
    /**
     * 线程队列最大线程数,默认：200
     */
    private Integer queueCapacity = 200;
    /**
     * 线程池中线程最大空闲时间，默认：60，单位：秒
     */
    private Integer keepAliveSeconds = 60;
    /**
     * 核心线程是否允许超时，默认false
     */
    private boolean allowCoreThreadTimeOut;
    /**
     * 用来设置线程池关闭的时候等待所有任务都完成再继续销毁其他的Bean，默认:true（必须设置setAwaitTerminationSeconds）
     */
    private boolean waitForTasksToCompleteOnShutdown = true;// 用来设置线程池关闭的时候等待所有任务都完成再继续销毁其他的Bean
    /**
     * 线程池中任务的等待时间，如果超过这个时候还没有销毁就强制销毁，默认：60秒（必须设置setWaitForTasksToCompleteOnShutdown）
     */
    private int awaitTerminationSeconds = 60;
    /**
     * 自定义线程名前缀，默认："Async-Service-"
     */
    private String threadNamePrefix = "Async-Service-"; // 线程池名前缀

    @Bean("taskExecutor")
    @Override
    public ThreadPoolTaskExecutor getAsyncExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(corePoolSize);
        taskExecutor.setMaxPoolSize(maxPoolSize);
        taskExecutor.setKeepAliveSeconds(keepAliveSeconds);
        taskExecutor.setQueueCapacity(queueCapacity);
        taskExecutor.setThreadNamePrefix(threadNamePrefix);
        taskExecutor.setAllowCoreThreadTimeOut(allowCoreThreadTimeOut);
        taskExecutor.setWaitForTasksToCompleteOnShutdown(waitForTasksToCompleteOnShutdown);
        taskExecutor.setAwaitTerminationSeconds(awaitTerminationSeconds);
        taskExecutor.setTaskDecorator(new ContextCopyingDecorator());
        // 线程池对拒绝任务的处理策略
        taskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 初始化
        taskExecutor.initialize();
        return taskExecutor;
    }


    @Override
    public void configureAsyncSupport(final AsyncSupportConfigurer configurer) {
        configurer.setDefaultTimeout(60 * 1000L);
        configurer.setTaskExecutor(Objects.requireNonNull(getAsyncExecutor()));
    }

}
