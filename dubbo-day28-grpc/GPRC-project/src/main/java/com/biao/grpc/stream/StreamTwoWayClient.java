package com.biao.grpc.stream;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * @ClassName StreamTwoWayClient
 * @Description: TODO
 * @Author Biao
 * @Date 2020/4/28
 * @Version V1.0
 **/
public class StreamTwoWayClient {
    public static void main(String[] args) throws InterruptedException {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 8883).usePlaintext().build();
        StreamServiceGrpc.StreamServiceStub asyncStub = StreamServiceGrpc.newStub(channel);

        StreamObserver<Stream.ResponseData> responseData = new StreamObserver<Stream.ResponseData>() {
            @Override
            public void onNext(Stream.ResponseData value) {
                // TODO Auto-generated method stub
                System.out.println(value.getText());
            }

            @Override
            public void onError(Throwable t) {
                // TODO Auto-generated method stub
                t.printStackTrace();
            }

            @Override
            public void onCompleted() {
                // TODO Auto-generated method stub
                // 关闭channel
                channel.shutdown();
            }
        };

        // 客户端流式rpc和服务端流式rpc，可以传入多个对象，同时返回多个响应对象
        StreamObserver<Stream.RequestData> requestData = asyncStub.twoWayStreamFun(responseData);
        long start = System.currentTimeMillis();
        for (int i = 0; i < 10; i++) {
            requestData.onNext(Stream.RequestData.newBuilder().setText("你好" + i).build());
        }
        requestData.onCompleted();
        System.out.println(System.currentTimeMillis() - start + " MS");
        // 由于是异步获得结果，所以sleep 10秒
        Thread.sleep(2000);
    }
}
