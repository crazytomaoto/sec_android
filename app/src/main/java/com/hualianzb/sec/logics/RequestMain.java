package com.hualianzb.sec.logics;

import android.content.Context;

import com.hualianzb.sec.api.IReturnCallback;
import com.hualianzb.sec.api.request.DynaCommonResult;
import com.hualianzb.sec.api.request.DynaRequest;
import com.hysd.android.platform_huanuo.base.logic.BaseLogic;
import com.hysd.android.platform_huanuo.net.base.ProtocolType;

/**
 * Created by wangtianyun on 2018/3/27.
 * 请求接口的主要实现类
 */

public class RequestMain extends BaseLogic implements IRequest {
    public RequestMain(Context context) {
        super(context);
    }

    @Override
    public void send(int telzone, String telephone) {
        DynaRequest request = new DynaRequest(mcontext, new IReturnCallback<DynaCommonResult>() {
            @Override
            public void onReturn(Object invoker, ProtocolType.ResponseEvent event, DynaCommonResult result) {
                if (ProtocolType.ResponseEvent.isFinish(event)) {
                    if (ProtocolType.ResponseEvent.SUCCESS == event) {
                        String code = result.data.getString("code");
                        String msg = result.data.getString("msg");
//                        if ("success".equals(code)) {
//                            sendMessage(GlobalMessageType.MainRequest.SEND, msg);
//                        } else {
//                            sendMessage(GlobalMessageType.MainRequest.SEND_ERROR, msg);
//                        }
                    }
                }
            }
        });
//        request.setModel(RequestHost.AppUserController.send);
        request.addParam("telzone", telzone);
        request.addParam("telephone", telephone);
        request.exec();
    }
}