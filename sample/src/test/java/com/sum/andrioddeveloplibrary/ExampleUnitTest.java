package com.sum.andrioddeveloplibrary;

import com.sum.andrioddeveloplibrary.protobuffer.ProtoCustomer;

import org.junit.Test;

import java.util.Arrays;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        new com.sum.andrioddeveloplibrary.java.Test().start2();
    }


    @Test
    public void protoBufferTest() throws Exception {

        print("test start");

        ProtoCustomer.RequestCustomerRsp rsp = ProtoCustomer.RequestCustomerRsp.newBuilder()
                .setErrCode("0")
                .setInfo("同步成功")
                .setInfo("n22")
                .setErrCode(null)
                .setResult(true)
                .build();
        print("序列化 ->" + Arrays.toString(rsp.toByteArray()));

        ProtoCustomer.RequestCustomerRsp parseFrom = ProtoCustomer.RequestCustomerRsp.parseFrom(rsp.toByteArray());

        print("返序列化 ->" + parseFrom.getInfo() + "," + parseFrom.getCustomerList() + "," + parseFrom.getResult()+","+parseFrom.getRepDate());


    }

    private void print(String msg) {
        System.out.println(msg);
    }
}