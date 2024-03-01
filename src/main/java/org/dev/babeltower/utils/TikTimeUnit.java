package org.dev.babeltower.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TikTimeUnit {
    TIKS(TikTimeUnit.TIK_SCALE),
    SECONDS(TikTimeUnit.SECOND_SCALE),
    MINUTES(TikTimeUnit.MINUTE_SCALE);
    private static final long TIK_SCALE = 1L;
    private static final long SECOND_SCALE = 20L;
    private static final long MINUTE_SCALE = SECOND_SCALE * 60L;

    private long tikCount;

    public long toTik(long time) {
        return time * this.tikCount;
    }
}
