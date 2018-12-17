package com.hualianzb.sec.logics;


import com.hysd.android.platform_huanuo.base.logic.ILogic;

/**
 * Created by Administrator on 2018/3/27.
 */

public interface IRequest extends ILogic {
    /**
     * 发送验证码
     *
     * @param telzone   区号
     * @param telephone 手机号
     */
    void send(int telzone, String telephone);
}
