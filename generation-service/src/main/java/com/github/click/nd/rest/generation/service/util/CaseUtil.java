package com.github.click.nd.rest.generation.service.util;

import com.google.common.base.CaseFormat;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@UtilityClass
@Slf4j
public class CaseUtil {

    public String toUpperCamel(String string) {
        return CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, string);
    }

    public String toLowerCamel(String string) {
        return CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, string);
    }

    public String toLowerHyphen(String string) {
        string = string.replace(" ", "_");
        return CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_HYPHEN, string);
    }
}
