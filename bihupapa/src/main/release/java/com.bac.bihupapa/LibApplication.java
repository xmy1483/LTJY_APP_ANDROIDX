package com.bac.bihupapa;

import android.app.Application;

import com.dxf.grpc.method.MethodGrpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

/**
 * Created by wujiazhen on 2017/8/8.
 */

public class LibApplication extends Application {
    private static String mHost="121.43.172.16";
    private static int mPort=3000;
    private static MethodGrpc.MethodBlockingStub stub;
    private static ManagedChannel mChannel;

    public static MethodGrpc.MethodBlockingStub getStub() {
        stub = MethodGrpc.newBlockingStub(mChannel);
        return stub;
    }


    public static ManagedChannel getmChannel() {
        mChannel = ManagedChannelBuilder.forAddress(mHost, mPort)
                .usePlaintext(true)
                .build();
        return mChannel;
    }

}
