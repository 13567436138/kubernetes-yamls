package com.aosom.aosombase.grpc;

import com.aosom.aosombase.context.GlobalContext;
import com.aosom.aosombase.seata.SeataConfig;
import io.grpc.*;
import io.seata.core.context.RootContext;
import org.springframework.util.StringUtils;

/**
 *    grpc   的配置使用
 *        客户端配置
 *       grpc.client.school.address=static://localhost:9090
 *       grpc.client.school.enableKeepAlive=true
 *       grpc.client.school.keepAliveWithoutCalls=true
 *       grpc.client.school.negotiationType=plaintext
 *     服务器配置
 *     grpc.server.port=9090
 *     grpc.server.address=0.0.0.0
 *
 *
 */

public class RpcSeataClientInterceptor  implements ClientInterceptor {
    @Override
    public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(MethodDescriptor<ReqT, RespT> methodDescriptor, CallOptions callOptions, Channel channel) {


         final ClientCall<ReqT,RespT> clientCall =   channel.newCall(methodDescriptor, callOptions);
        return new ForwardingClientCall<ReqT, RespT>() {
            @Override
            protected ClientCall<ReqT, RespT> delegate() {
                return clientCall;
            }
            @Override
            public void start(Listener<RespT> responseListener, Metadata headers) {
                String xid = RootContext.getXID();
                if (!StringUtils.isEmpty(xid)) {

                    Metadata.Key<String> xidKey = Metadata.Key.of(SeataConfig.SEATA_XID,Metadata.ASCII_STRING_MARSHALLER);
                    headers.put(xidKey,xid);
                }

                String uid= GlobalContext.getKey(GlobalContext.MEMBER_ID);
                if(!StringUtils.isEmpty(uid)){
                    Metadata.Key<String> uidKey = Metadata.Key.of(GlobalContext.MEMBER_ID,Metadata.ASCII_STRING_MARSHALLER);
                    headers.put(uidKey,uid);
                }
                super.start(responseListener, headers);
            }
        };
    }
}
