package com.meta.error;

/**
 * 错误级别常量池。
 * 
 * <p>对应于标准错误码的第4位：
 *      <table border="1">
 *          <tr>
 *           <td><b>位置</b></td><td>1</td><td>2</td><td>3</td><td bgcolor="green">4</td><td>5</td><td>6</td><td>7</td><td>8</td><td>9</td><td>10</td><td>11</td><td>12</td>
 *          </tr>
 *          <tr>
 *           <td><b>示例</b></td><td>A</td><td>E</td><td>0</td><td>1</td><td>0</td><td>1</td><td>0</td><td>1</td><td>1</td><td>0</td><td>2</td><td>7</td>
 *          </tr>
 *          <tr>
 *           <td><b>说明</b></td><td colspan=2>固定<br>标识</td><td>规<br>范<br>版<br>本</td><td>错<br>误<br>级<br>别</td><td>错<br>误<br>类<br>型</td><td colspan=4>错误场景</td><td colspan=3>错误编<br>码</td>
 *          </tr>
 *          </table>
 * 
 * @author jiapeng.li
 * @author anorld.zhangm
 * @version $Id: ErrorTypes.java, v 0.1 2012-5-22 下午12:26:53 jiapeng.li Exp $
 */
public interface ErrorLevels {

    /** INFO级别 */
    public static final String INFO  = "1";

    /** WARN级别 */
    public static final String WARN  = "3";

    /** ERROR级别 */
    public static final String ERROR = "5";

    /** FATAL级别 */
    public static final String FATAL = "7";
}
