package cn.yang.util.core;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Objects;
import java.util.UUID;

/**
 * @author yang 2019/4/16
 */
public class BasicUtil {

    private static final int DEFAULT_OFFSET_HOURS = 8;

    /**
     * 生成uuid
     *
     * @return ${8}-${8}-${8}-${8} 标准形式uuid
     */
    public static String uuid() {
        return UUID.randomUUID().toString();
    }

    /**
     * 生成10位时间戳
     *
     * @param time   生成时间戳的具体时间，null则为当前时间
     * @return 时间戳
     */
    public static Long timestamp(LocalDateTime time) {
        if (Objects.isNull(time)) {
            time = LocalDateTime.now();
        }
        return time.atZone(ZoneId.systemDefault()).toEpochSecond();
    }

    public static Long timestamp() {
        return timestamp(null);
    }

    public static LocalDateTime fromTimestamp(long timestamp) {
        return LocalDateTime.ofEpochSecond(timestamp, 0, ZoneOffset.ofHours(8));
    }

    public static LocalDateTime now(ZoneId zoneId) {
        if (Objects.isNull(zoneId)) {
            zoneId = ZoneId.systemDefault();
        }
        return LocalDateTime.now(zoneId);
    }

    public static LocalDateTime now() {
        return now(null);
    }

}
