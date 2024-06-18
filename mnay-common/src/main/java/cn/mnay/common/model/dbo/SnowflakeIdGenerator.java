package cn.mnay.common.model.dbo;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.generator.BeforeExecutionGenerator;
import org.hibernate.generator.EventType;

import java.lang.management.ManagementFactory;
import java.util.EnumSet;

import static org.hibernate.generator.EventTypeSets.INSERT_ONLY;

public class SnowflakeIdGenerator implements BeforeExecutionGenerator {

    private static final Snowflake SNOWFLAKE = IdUtil.getSnowflake(getWorkerId(), getDatacenterId());

    public static long getWorkerId() {
        return Math.abs(ManagementFactory.getRuntimeMXBean().getName().hashCode() % 32);
    }

    public static long getDatacenterId() {
        return Math.abs(ManagementFactory.getRuntimeMXBean().getName().hashCode() / 32 % 32);
    }

    public static String nextIdStr() {
        return SNOWFLAKE.nextIdStr();
    }

    public static long nextId() {
        return SNOWFLAKE.nextId();
    }


    @Override
    public Object generate(SharedSessionContractImplementor session, Object owner, Object currentValue, EventType eventType) {
        if (currentValue != null) {
            return currentValue;
        }
        return nextIdStr();
    }

    @Override
    public EnumSet<EventType> getEventTypes() {
        return INSERT_ONLY;
    }
}
