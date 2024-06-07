package org.rivelles.katas;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class HumanReadableTimeTest {
    @Test
    void makeReadable_whenMaxValue_shouldReturnValue() {
        var input = 359999;
        var expected = "99:59:59";

        assertThat(HumanReadableTime.makeReadable(input)).isEqualTo(expected);
    }

    @Test
    void makeReadable_failing() {
        var input = 5;
        var expected = "00:00:05";

        assertThat(HumanReadableTime.makeReadable(input)).isEqualTo(expected);
    }
}