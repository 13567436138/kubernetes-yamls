package com.aosom.aosombase.grpc;

import com.aosom.aosombase.context.GlobalContext;
import com.aosom.aosombase.seata.SeataConfig;

import io.grpc.*;
import io.seata.core.context.RootContext;
import org.springframework.util.StringUtils;

/**
 *  grpc   的使用
 */
public class RpcSeataServerInterceptor  implements ServerInterceptor {
    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> call, Metadata headers, ServerCallHandler<ReqT, RespT> next) {

//        String xid =request.getAuth();
//        if (!StringUtils.isEmpty(xid)) {
//            System.out.println("================这是我替换的Xid"+xid);
//            // template.header(FescarAutoConfiguration.FESCAR_XID, xid);
//            RootContext.bind(xid);
//
//        }

        Metadata.Key<String> xidhead = Metadata.Key.of(SeataConfig.SEATA_XID, Metadata.ASCII_STRING_MARSHALLER);
        String xid = headers.get(xidhead);
        if (!StringUtils.isEmpty(xid)){
            RootContext.bind(xid);
        }

        Metadata.Key<String> uidHead = Metadata.Key.of(GlobalContext.MEMBER_ID, Metadata.ASCII_STRING_MARSHALLER);
        String uid = headers.get(uidHead);
        if (!StringUtils.isEmpty(xid)){

            GlobalContext.saveKey(GlobalContext.MEMBER_ID,uid);
        }

        //服务端写回参数
        ServerCall<ReqT, RespT> serverCall = new ForwardingServerCall.SimpleForwardingServerCall<ReqT, RespT>(call) {
            @Override
            public void sendHeaders(Metadata headers) {
                //移除xid
                String  xidOut=  RootContext.getXID();
                if(!StringUtils.isEmpty(xidOut)){
                    RootContext.unbind();
                }
                //移除所有主键
                GlobalContext.removeAllKey();

                super.sendHeaders(headers);
            }


        };
        return next.startCall(serverCall,headers);
    }


}
