package com.bac.bacplatform.http;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bac.bacplatform.BacApplication;
import com.bac.bacplatform.utils.str.StringUtil;
import com.bac.bihupapa.bean.Method;
import com.bac.bihupapa.grpc.AES;
import com.bac.bihupapa.grpc.Base64;
import com.bac.bihupapa.grpc.MD5;
import com.bac.bihupapa.grpc.RSA;
import com.bac.bihupapa.grpc.TaskPostExecute;
import com.dxf.grpc.method.MethodGrpc;
import com.dxf.grpc.method.Request;
import com.dxf.grpc.method.Response;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

/**
 * Created by guke on 2017/11/15.
 */
public class GrpcTask extends AsyncTask<Void, Void, Method> {

 //  正式服
//    private static String mHost = "app5.bac365.com";
//    private static int mPort = 3000;

 //  正式服
    private static String mHost = ApiHost.HOST;
    private static int mPort = ApiHost.RPC_PORT;

//  测试服
//    private static String mHost = "intranet.camel.bac365.com";
//    private static int mPort = 20098;
//    private static String mHost = "192.168.0.85";
//    private static int mPort = 3000;

    private static String ID="123";

    public static final int NO_LOGIN = 1;
    public static final int AES_ENCRYPT_ERROR = 2;
    public static final int AES_DECRYPT_ERROR = 3;
    public static final int ZIP_ZIP_ERROR = 4;
    public static final int ZIP_UNZIP_ERROR = 5;
    public static final int NO_METHOD_NAME = 6;
    public static final int NO_METHOD_NAME_ERROR = 6;
    public static final int QUERY_ERROR = 7;
    public static final int EXEC_ERROR = 8;
    public static final int JSON_ERROR = 9;
    public static final int NO_USER_MD5_KEY = 10;
    public static final int SIGN_ERROR = 11;
    public static final int RSA_ERROR = 12;
    public static final int FILTER_ERROR = 13;

    public static final int TOKEN_ERROR = 14;
    public static final int UNKNOW_ERROR = 44;
    public static final int SESSION_ERROR = 10019999;
    // private String mJSON="{\"listMap\":[{\"cPhone\":\"13338602929\",\"cPassword\":\"123456\"}],\"methodName\":\"LOGIN\"}";
    private static ManagedChannel mChannel =null;
    private static MethodGrpc.MethodBlockingStub stub = null;

    private Method inBean;
    private Toast toast;
    private String msg;
    private TaskPostExecute postExecute;



    private String VER = "1.0";
    private String TOKEN = "";
    private Context context;
    private final String defaultPassword = "abcd1234567890!@";
    private static AES aes = null;
    private static String aesStr = null;
    private static Method loginMethod=null;

    private String certificate = null;

    private static ProgressDialog progressdialog = null;
    static {
    }

    public GrpcTask(Context context, Method inBean, String msg, TaskPostExecute postExecute) {
        this.inBean = inBean;
        this.msg = msg;
        this.postExecute = postExecute;
        this.context=context;
        if(mChannel==null){
            mChannel =getmChannel();
            stub = getStub();
        }
        certificate = StringUtil.decode(BacApplication.getBacApplication(), "certificate");
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (msg == null) {
            return;
        }
        if(context==null){
            return;
        }
        //conrext.msg为空不提示
        progressdialog = new ProgressDialog(context);
        progressdialog.setTitle(msg);
        progressdialog.setMessage("loading...");
        progressdialog.setCancelable(true);
        progressdialog.show();

//        toast = Toast.makeText(context, msg, Toast.LENGTH_LONG);
//        toast.show();
    }

    @Override
    protected Method doInBackground(Void... nothing) {
        try {
            return service(inBean);
        } catch (Exception e) {
            Method bean = new Method();
            bean.setErrorId(1000);
            bean.setMsg(e.getMessage());
            Log.e("GrpcTask1", "doInBackground: ", e);
            return bean;
        }
    }


    private Method service(Method mBean) {
        if("LOGIN".equals(mBean.getMethodName())){
            // 做自动登录，不需要验证码
//            loginMethod=mBean;

        }
//        if(commonParam.getmToken()!=null){
//            TOKEN = commonParam.getmToken();
//        }
        if (aes == null) {
            Method m = GET_KEY();
            if (m != null) {
                return m;
            }
        }
        Request message = null;
        Response response = null;
        String json = null;
        Method bacHttpBean = new Method();
        for (int i = 0; i < 3; i++) {
            try {
                json = Base64.encodeToString(aes.encrypt(JSON.toJSONString(mBean).getBytes()), 2);
                String sign = MD5.getKeyedDigest(json, aesStr);
                System.out.println("==>sign= " + sign);
                System.out.println("==>json= " + json);

                json = URLEncoder.encode(json, "UTF-8");
                System.out.println("==>json URLEncoder= " + json);
                message = Request.newBuilder().setID(ID)
                        .setTOKEN(TOKEN)
                        .setAES("Y")
                        .setJSON(json)
                        .setCOUNT(1)
                        .setSIGN(sign).build();
                try {
                    response = stub.service(message);
                } catch (io.grpc.StatusRuntimeException e) {
                    dealGrpcState(e.getStatus());
                    Thread.sleep(1000);
                    continue;
                }
                TOKEN = response.getTOKEN();
                // TODO
//                commonParam.setmToken(TOKEN);

                if (!haveRedo(response)) {
                    break;
                }
            } catch (Exception e) {
                e.printStackTrace();

                bacHttpBean.setErrorId(UNKNOW_ERROR);
                bacHttpBean.setMsg("未知错误:" + getStackTrace(e));
                return bacHttpBean;
            }
        }

        if (response == null) {
            bacHttpBean.setErrorId(1);
            bacHttpBean.setMsg("网络错误，请查看网络是否正常！");
            return bacHttpBean;
        }
        if (response.getERROR() != 0) {
            bacHttpBean.setErrorId(response.getERROR());
            bacHttpBean.setMsg(response.getMSG());
            return bacHttpBean;
        }


        bacHttpBean = checkSign(response.getJSON(), aesStr, response.getSIGN());
        if (bacHttpBean != null) {
            return bacHttpBean;
        }


        try {
            json = new String(aes.decrypt(Base64.decode(response.getJSON())), Charset.forName("UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
            bacHttpBean.setErrorId(UNKNOW_ERROR);
            bacHttpBean.setMsg("解密错误:" + getStackTrace(e));
            return bacHttpBean;
        }
        Method method = JSON.parseObject(json, Method.class);
        return method;
    }

    @Override
    protected void onPostExecute(Method result) {
        //关闭弹框
        if (progressdialog!=null){
            progressdialog.dismiss();

        }

        //toast.cancel();
        postExecute.onPostExecute(result);
    }


    private boolean haveRedo(Response response) {
        int err = response.getERROR();
        if (err == 0) {
            return false;
        }

        System.out.println("==>" + aesStr);

        if (err == SIGN_ERROR || err == AES_DECRYPT_ERROR
                || err == AES_ENCRYPT_ERROR
                || err == NO_LOGIN || err==SESSION_ERROR || err==TOKEN_ERROR
                ) {

            GET_KEY();
            if(loginMethod!=null && certificate !=null && !"".equals(certificate)){
                Map<String,Object> map= loginMethod.getListMap().get(0);
                map.remove("verification_code");
                map.put("certificate",certificate);
            }

            LOGIN();
            return true;
        }
        System.out.println("+++++++++++++++请求返回："+err);

        return false;
    }

    private Method checkSign(String data, String key, String sign_ser) {
//        String sign = null;
//        Method bacHttpBean = new Method();
//        try {
//            sign = MD5.getKeyedDigest(data, key);
//            if (!sign.equals(sign_ser)) {
//                System.out.println("==>SIGN_KEY " + key);
//                System.out.println("==>SIGN_SER " + sign_ser);
//                System.out.println("==>SIGN_APP " + sign);
//                bacHttpBean.setErrorId(1);
//                bacHttpBean.setMsg("签名错误!");
//                return bacHttpBean;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            bacHttpBean.setErrorId(1);
//            bacHttpBean.setMsg("MD5错误!" + getStackTrace(e));
//            return bacHttpBean;
//        }
        return null;
    }


    private Method LOGIN() {
        if (loginMethod == null) {
            return null;
        }
        Request message = null;
        Response response = null;
        String json = null;
        Method mBean = new Method();
        try {
            json = Base64.encodeToString(aes.encrypt(JSON.toJSONString(loginMethod).getBytes()), 2);
            String sign = MD5.getKeyedDigest(json, aesStr);
            System.out.println("==>sign= " + sign);
            System.out.println("==>json= " + json);

            json = URLEncoder.encode(json, "UTF-8");
            System.out.println("==>json URLEncoder= " + json);
            message = Request.newBuilder().setID(ID)
                    .setTOKEN(TOKEN)
                    .setAES("Y")
                    .setJSON(json)
                    .setCOUNT(1)
                    .setSIGN(sign).build();
            response = stub.service(message);
            TOKEN = response.getTOKEN();

            // TODO
//            commonParam.setmToken(TOKEN);

            if(response.getERROR()==0){
                return null;
            }
            mBean.setErrorId(response.getERROR());
            mBean.setMsg(response.getMSG());
            return mBean;
        } catch (Exception e) {
            e.printStackTrace();
            mBean.setErrorId(1);
            mBean.setMsg("Login错误!" + getStackTrace(e));
            return mBean;
        }
    }

    private Method GET_KEY() {
        try {
            Map<String, String> map = RSA.generateKeyPair();
            String publicKey = map.get("publicKey");
            String privateKey = map.get("privateKey");

            Method mBean = new Method();
            mBean.setMethodName("GET_KEY");
            mBean.put("PublicKey", publicKey);
            AES aes = new AES(defaultPassword.getBytes());
            String json = Base64.encodeToString(
                    aes.encrypt(
                            JSON.toJSONString(mBean).getBytes("UTF-8")
                    ), 2
            );
            String sign = MD5.getKeyedDigest(json, defaultPassword);
            json = URLEncoder.encode(json, "UTF-8");
            Request message = Request.newBuilder().setID(ID).setTOKEN(TOKEN).setAES("F").setJSON(json).setSIGN(sign).setCOUNT(1).build();

            Response response = stub.service(message);


            TOKEN = response.getTOKEN();
            // TODO
//            commonParam.setmToken(TOKEN);

            if (response.getERROR() == 0) {
                json = response.getJSON();
                mBean = checkSign(response.getJSON(), defaultPassword, response.getSIGN());
                if (mBean != null) {
                    return mBean;
                }
                json = new String(aes.decrypt(Base64.decode(json)), Charset.forName("UTF-8"));
                System.out.println("==>json " + json);
                Method method = JSON.parseObject(json, Method.class);
                String key = (String) method.getListMap().get(0).get("KEY");
                aesStr = RSA.decrypt(key, privateKey);
                aesStr = new String(Base64.decode(aesStr), Charset.forName("UTF-8"));
                this.aes = new AES(aesStr.getBytes());
                // TODO
//                commonParam.setmPrivateKey(privateKey);

                return null;
            } else {
                Method Bean = new Method();
                Bean.setErrorId(response.getERROR());
                Bean.setMsg(response.getMSG());
                return Bean;
            }
        } catch (io.grpc.StatusRuntimeException e) {
            return dealGrpcState(e.getStatus());
        } catch (Exception e) {
            e.printStackTrace();
            Method Bean = new Method();
            Bean.setErrorId(1);
            Bean.setMsg("网络错误，请查看网络连接");
            return Bean;
        }
    }


    public static String getStackTrace(Exception t) {
        StringWriter sw = new StringWriter();
        sw.append(t.getMessage()).append("\n\t");
        PrintWriter pw = new PrintWriter(sw);
        try {
            t.printStackTrace(pw);
            //return sw.toString().replaceAll("\r\n\t", "<br>");
            return sw.toString();
        } finally {
            pw.close();
        }
    }


    private Method dealGrpcState(io.grpc.Status status) {

        if (io.grpc.Status.INTERNAL.getCode().equals(status.getCode())) {
            Method Bean = new Method();
            Bean.setErrorId(1);
            Bean.setMsg("网络错误，请查看网络连接");
            return Bean;
        }else if(io.grpc.Status.UNAVAILABLE.getCode().equals(status.getCode())){
            mChannel=getmChannel();
            stub=getStub();
            Method Bean = new Method();
            Bean.setErrorId(1);
            Bean.setMsg("网络错误，请查看网络是否正常！");
            return Bean;
        }else{
            Method Bean = new Method();
            Bean.setErrorId(1);
            Bean.setMsg("ERROR===="+status.getCode().name()+" ID："+status.getCode().value());
            return Bean;
        }
    }


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

    public static void setID(String ID) {
        GrpcTask.ID = ID;
    }
}


