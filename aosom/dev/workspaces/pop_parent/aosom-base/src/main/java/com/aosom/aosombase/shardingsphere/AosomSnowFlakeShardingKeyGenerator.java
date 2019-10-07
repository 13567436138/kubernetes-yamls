package com.aosom.aosombase.shardingsphere;


import com.aosom.aosombase.nacos.MoreSiteContainer;
import com.google.common.base.Preconditions;
import org.apache.shardingsphere.core.strategy.keygen.TimeService;
import org.apache.shardingsphere.spi.keygen.ShardingKeyGenerator;

import java.util.Calendar;
import java.util.Properties;

/**
 * 生成分布式id
 */
public class AosomSnowFlakeShardingKeyGenerator  implements ShardingKeyGenerator {

    public static final long EPOCH;

    private static final long SEQUENCE_BITS = 12L;

    private static final long WORKER_ID_BITS = 10L;

    private static final long SEQUENCE_MASK = (1 << SEQUENCE_BITS) - 1;

    private static final long WORKER_ID_LEFT_SHIFT_BITS = SEQUENCE_BITS;

    private static final long TIMESTAMP_LEFT_SHIFT_BITS = WORKER_ID_LEFT_SHIFT_BITS + WORKER_ID_BITS;

    private static final long WORKER_ID_MAX_VALUE = 1L << WORKER_ID_BITS;

    private static final long WORKER_ID = 0;

    private static final int MAX_TOLERATE_TIME_DIFFERENCE_MILLISECONDS = 10;


    private static TimeService timeService = new TimeService();



    private byte sequenceOffset;

    private long sequence;

    private long lastMilliseconds;

    static {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2016, Calendar.NOVEMBER, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        EPOCH = calendar.getTimeInMillis();
    }

    @Override
    public String getType() {
        return "SNOWFLAKE";
    }

    @Override
    public Properties getProperties() {
        return null;
    }

    @Override
    public void setProperties(Properties properties) {

    }

    @Override
    public synchronized Comparable<?> generateKey() {
        long currentMilliseconds = timeService.getCurrentMillis();

            if (waitTolerateTimeDifferenceIfNeed(currentMilliseconds)) {
                currentMilliseconds = timeService.getCurrentMillis();
            }

        if (lastMilliseconds == currentMilliseconds) {
            if (0L == (sequence = (sequence + 1) & SEQUENCE_MASK)) {
                currentMilliseconds = waitUntilNextTime(currentMilliseconds);
            }
        } else {
            vibrateSequenceOffset();
            sequence = sequenceOffset;
        }
        lastMilliseconds = currentMilliseconds;
        return ((currentMilliseconds - EPOCH) << TIMESTAMP_LEFT_SHIFT_BITS) | (getWorkerId() << WORKER_ID_LEFT_SHIFT_BITS) | sequence;
    }


    private boolean waitTolerateTimeDifferenceIfNeed(final long currentMilliseconds)  {
        if (lastMilliseconds <= currentMilliseconds) {
            return false;
        }
        long timeDifferenceMilliseconds = lastMilliseconds - currentMilliseconds;
        Preconditions.checkState(timeDifferenceMilliseconds < getMaxTolerateTimeDifferenceMilliseconds(),
                "Clock is moving backwards, last time is %d milliseconds, current time is %d milliseconds", lastMilliseconds, currentMilliseconds);
        try {
            Thread.sleep(timeDifferenceMilliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return true;
    }

    private long getWorkerId() {
        /*long result = MoreSiteContainer.getWorkId();
        Preconditions.checkArgument(result >= 0L && result < WORKER_ID_MAX_VALUE);
        return result;*/
        return 1;
    }

    private int getMaxTolerateTimeDifferenceMilliseconds() {
        return MAX_TOLERATE_TIME_DIFFERENCE_MILLISECONDS;
    }

    private long waitUntilNextTime(final long lastTime) {
        long result = timeService.getCurrentMillis();
        while (result <= lastTime) {
            result = timeService.getCurrentMillis();
        }
        return result;
    }

    private void vibrateSequenceOffset() {
        sequenceOffset = (byte) (~sequenceOffset & 1);
    }
}
