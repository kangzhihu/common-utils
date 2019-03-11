package com.meta.enums;

import java.util.function.Function;
import com.meta.common.MaskUtil;
public enum SensitiveTypeEnum {

    /** 密码，全部脱敏 */
    PASSWORD(MaskUtil::maskPassword),
    /** 手机号码 */
    MOBILE_PHONE(MaskUtil::maskMobilePhone),

    /** 身份证号码 */
    ID_CARD_NO(MaskUtil::maskIdCardNo),

    ;

    /** 脱敏方法 */
    public final Function<Object, String> mask;

    /**
     * 构造方法
     *
     * @param mask
     */
    SensitiveTypeEnum(Function<Object, String> mask) {
        this.mask = mask;
    }
}
