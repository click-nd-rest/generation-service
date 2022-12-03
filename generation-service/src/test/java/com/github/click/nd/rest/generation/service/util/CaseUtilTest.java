package com.github.click.nd.rest.generation.service.util;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class CaseUtilTest {
    @Test
    public void lowerHyphen() {
        Assertions.assertThat(CaseUtil.toLowerHyphen("pIE_COOL")).isEqualTo("pie-cool");
    }
}
