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

        print("返序列化 ->" + parseFrom.getInfo() + "," + parseFrom.getCustomerList() + "," + parseFrom.getResult() + "," + parseFrom.getRepDate());


    }

    public void count(long millis) {
        String[] units = {"天", "小时", "分钟", "秒"};
        StringBuilder sb = new StringBuilder();
        long day = 0, hour = 0, min = 0, sec = 0;
        //天 时 分 秒
        int[] unitLen = {86400000, 3600000, 60000, 1000};
        for (int i = 0; i < 4; i++) {
            if (millis >= unitLen[i]) {
                long mode = millis / unitLen[i];
                millis -= mode * unitLen[i];
                sb.append(mode).append(units[i]);
                if (i == 0) {
                    day = mode;
                } else if (i == 1) {
                    hour = mode;
                } else if (i == 2) {
                    min = mode;
                } else {
                    sec = mode;
                }
            }
        }
        print(sb.toString() + ";" + day + "" + hour + "" + min + "" + sec);
    }

    @Test
    public void testTime() throws Exception {
        count(80807);

    }


    private void print(String msg) {
        System.out.println(msg);
    }
}