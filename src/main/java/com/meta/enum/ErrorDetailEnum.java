package com.meta.enum;

/**
 * 错误明细码枚举
 *
 * <p>明细错误码会用在标准错误码中，code对应于标准错误码14~16位
 * <p>而errorLevel对应于标准错误码的第4位
 * <p>在标准错误码的位置如下：
 *      <table border="1">
 *          <tr>
 *              <td align="center">说明</td>
 *              <td align="center" colspan=2>固定<br>标识</td>
 *              <td align="center">规<br>范<br>版<br>本</td>
 *              <td align="center">错<br>误<br>级<br>别</td>
 *              <td align="center">错<br>误<br>类<br>型</td>
 *              <td align="center" colspan=4>错误场景（全局唯一）</td>
 *              <td align="center" colspan=2>系统标识</td>
 *              <td align="center" colspan=2>系统用例标识</td>
 *              <td align="center" colspan=3>错误<br>编码</td>
 *          </tr>
 *          <tr>
 *              <td align="center">位置</td>
 *              <td align="center">1</td>
 *              <td align="center">2</td>
 *              <td align="center">3</td>
 *              <td align="center" bgcolor="yellow">4</td>
 *              <td align="center">5</td>
 *              <td align="center">6</td>
 *              <td align="center">7</td>
 *              <td align="center">8</td>
 *              <td align="center">9</td>
 *              <td align="center">10</td>
 *              <td align="center">11</td>
 *              <td align="center">12</td>
 *              <td align="center">13</td>
 *              <td align="center" bgcolor="red">14</td>
 *              <td align="center" bgcolor="red">15</td>
 *              <td align="center" bgcolor="red">16</td>
 *          </tr>
 *          <tr>
 *              <td align="center">示例</td>
 *              <td align="center">S</td>
 *              <td align="center">E</td>
 *              <td align="center">1</td>
 *              <td align="center">1</td>
 *              <td align="center">0</td>
 *
 *              <td align="center"></td>
 *              <td align="center"></td>
 *              <td align="center"></td>
 *              <td align="center"></td>
 *
 *              <td align="center">1</td>
 *              <td align="center">0</td>
 *              <td align="center">0</td>
 *              <td align="center">0</td>
 *              <td align="center">1</td>
 *              <td align="center">2</td>
 *              <td align="center">7</td>
 *          </tr>
 *      </table>
 *
 *  <p>错误明细码的CODE取值空间如下：
 *  <ul>
 *      <li>公共类错误码[00-09]
 *  </ul>
 *
 * 第1~2位：固定标识:"SE"，表示ServiceError；
 * 第 3 位：版本号，固定为"1"；
 * 第 4 位：错误级别，固定为"1：INFO、3：WARN、5：ERROR、7：FATAL"；
 * 第 5 位：错误类型，固定为"0：系统错误、1：业务错误、2：第三方错误"；
 * 第6~13位：事件标识
 * 第6~9位：具体事业部前缀，由公司统一分配;
 * 第10~11位： 业务标识，用于对当前号段做进一步的细分;
 * 第12~13位：事件码，由业务/系统owner自定义；
  * 第14~16位，业务自定义错误码，共3位，由业务/系统owner自定义；
 */
public enum ErrorDetailEnum {

    //========================================================================//
    //                        [000-099] 公共类错误码
    //========================================================================//

    /** 其它未知异常 */
    UNKNOWN_EXCEPTION("000", ErrorLevels.ERROR, "其它未知异常"),

    /** 远程调用异常 */
    REMOTE_EXCEPTION("001", ErrorLevels.ERROR, "远程调用异常"),

    /** 数据库错误 */
    DATABASE_ERROR("002", ErrorLevels.ERROR, "数据库错误"),

    /** 唯一性表约束冲突 */
    MUTEX_ERROR("003", ErrorLevels.ERROR, "唯一性表约束冲突"),

    /** 请求参数非法（参数校验类） */
    ILLEGAL_PARAMETER("004", ErrorLevels.WARN, "请求参数非法"),

    /** 配置错误 */
    CONFIG_ERROR("005", ErrorLevels.ERROR, "配置错误"),

    /** 资源分配错误 */
    ALLOCATION_ERROR("006", ErrorLevels.ERROR, "资源分配错误"),

    /** 数据已超时 */
    OUT_OF_DATE("007", ErrorLevels.WARN, "数据已过期"),

    /** 切流标识设置不正确 */
    SWITCH_FLAG_ERROR("008", ErrorLevels.ERROR, "切流标识设置不正确"),

    /** 不支持的操作 */
    UNSUPPORTED_OPERATION("009", ErrorLevels.ERROR, "不支持的操作"),
    
    ;

    /** 枚举编码  */
    private final String code;

    /** 错误级别 */
    private final String errorLevvel;

    /** 描述说明 */
    private final String description;

    /**
     * 私有构造函数。
     *
     * @param code 错误详情代码
     * @param errorLevvel 错误级别
     * @param description 错误描述说明
     */
    ErrorDetailEnum(String code, String errorLevvel, String description) {
        this.code = code;
        this.errorLevvel = errorLevvel;
        this.description = description;
    }

    /**
     * 根据错误详情代码获取错误详情枚举
     *
     * @param code 错误详情代码
     * @return code对应的错误详情枚举，如果找不到则返回UNKNOWN_EXCEPTION
     */
    public static ErrorDetailEnum getByCode(String code) {
        // 按照code查找枚举
        for (ErrorDetailEnum detailCode : values()) {
            if (StringUtils.equals(detailCode.getCode(), code)) {
                return detailCode;
            }
        }

        // 如果找不到，则返回LoanErrorDetailEnum
        return ErrorDetailEnum.UNKNOWN_EXCEPTION;
    }

    /**
     * 根据三代错误码获取错误详情枚举
     *
     * @param errorCode 三代错误码 AE15133010110000
     * @return errorCode对应的错误详情枚举，如果非本平台生成的错误码返回null
     */
    public static ErrorDetailEnum getByErrorCode(String errorCode) {

        if (errorCode == null || errorCode.length() != 16) {
            return null;
        }

        String globalScenarioCode = errorCode.substring(5, 9);
        String systemScenarioCode = errorCode.substring(9, 11);
        if (!ErrorScenarioEnum.GLOBAL_SCENARIO_CODE.equals(globalScenarioCode)
            || !ErrorScenarioEnum.SYSTEM_SCENARIO_CODE.equals(systemScenarioCode)) {
            return null;
        }
        String code = errorCode.substring(13, 16);
        // 按照code查找枚举
        for (ErrorDetailEnum detailCode : values()) {
            if (StringUtils.equals(detailCode.getCode(), code)) {
                return detailCode;
            }
        }

        // 如果找不到，则返回null
        return null;
    }

    /**
     * Getter method for property <tt>code</tt>.
     *
     * @return property value of code
     */
    public String getCode() {
        return code;
    }

    /**
     * Getter method for property <tt>errorLevvel</tt>.
     *
     * @return property value of errorLevvel
     */
    public String getErrorLevel() {
        return errorLevvel;
    }

    /**
     * Getter method for property <tt>description</tt>.
     *
     * @return property value of description
     */
    public String getDescription() {
        return description;
    }

}
    
