package com.meta.common;

   private final static Logger LOGGER = LoggerFactory.getLogger(MaskUtil.class);

    /** 脱敏用的替代字符 */
    private static final String SENSITIVE_DATA_MASK_CHAR = "*";

    /** 空字符串 */
    private static final String EMPTY_STRING = "";

    private static final String UNDERLINE = "_";

    /**
     * 对密码进行脱敏
     * <br>
     * 规则：全部脱敏
     *
     * @return 脱敏后的密码
     */
    public static String maskPassword(Object value) {
        if (isBlankStr(value)) {
            return EMPTY_STRING;
        }
        return maskAll(String.valueOf(value));
    }
    
        /**
     * 对手机号码进行脱敏
     * <br>
     * 规则：前3、后4位不脱敏，剩余中间位脱敏
     *
     * @return 脱敏后的手机号码
     */
    public static String maskMobilePhone(Object value) {
        if (isBlankStr(value)) {
            return EMPTY_STRING;
        }

        String mobilePhone = String.valueOf(value);
        return mask(mobilePhone, 3, 4);
    }

    /**
     * 对身份证号码进行脱敏
     * <br>
     * 规则：前5、后2位不脱敏，剩余中间位脱敏
     *
     * @return 脱敏后的身份证号码
     */
    public static String maskIdCardNo(Object value) {
        if (isBlankStr(value)) {
            return EMPTY_STRING;
        }

        String idCardNo = String.valueOf(value);
        return mask(idCardNo, 5, 2);
    }

    /** 判断是否为空字符串 */
    private static boolean isBlankStr(Object value) {
        return value == null || StringUtils.isBlank(String.valueOf(value));
    }

    /** 返回由“*”组成的脱敏后字符串 */
    private static String mask(int length) {
        return StringUtils.repeat(SENSITIVE_DATA_MASK_CHAR, length);
    }

    /** 对全部字符进行脱敏 */
    private static String maskAll(String value) {
        return mask(value.length());
    }

    /**
     * 对字符串进行脱敏，指定前几位与后几位不需要脱敏的字符数。
     * <br>
     * 例如：mask("12345", 1, 2)表示对前1位于后2位不需要脱敏，脱敏结果为"1**45"
     *
     * @param value       要脱敏的字符串
     * @param beforeCount 指定前几位不需要脱敏
     * @param afterCount  指定后几位不需要脱敏
     *
     * @return 脱敏后的字符串
     */
    private static String mask(String value, int beforeCount, int afterCount) {

        int length = value.length();

        if (beforeCount > length || afterCount > length || beforeCount + afterCount > length) {

            LogUtil.error(LOGGER, "MaskUtil.mask({}, {}, {}) fail", value, beforeCount, afterCount);

            return value;
        }

        return value.substring(0, beforeCount) + mask(value.length() - beforeCount - afterCount)
                + value.substring(value.length() - afterCount);
    }

    /**
     * 对银行入账单进行脱敏，银行入账单号,卡号做掩码处理，格式16位卡号_xxx_xxxx_xxxx
     *
     * @param value
     * @return
     */
    public static String maskBankcardFlow(Object value) {
        if (isBlankStr(value)) {
            return EMPTY_STRING;
        }

        String bankcardFlow = String.valueOf(value);

        List<String> bankcardFlowList = Lists.newArrayList(StringUtils.split(bankcardFlow, UNDERLINE));

        if (bankcardFlowList.size() > 1) {
            return mask(bankcardFlowList.get(0), 6, 4) + UNDERLINE + Joiner.on(UNDERLINE).join(
                    bankcardFlowList.subList(1, bankcardFlowList.size()));
        }

        return mask(bankcardFlow, 6, 4);
    }
}
