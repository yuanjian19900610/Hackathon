package com.hacksthon.team;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.cardinfo.smartpos.TradeController;
import com.cardinfo.smartpos.TransCallback;
import com.cardinfo.smartpos.service.ResponseBean;
import com.hacksthon.team.bean.CmdConstantType;
import com.hacksthon.team.bean.DeviceInfo;
import com.hacksthon.team.bean.ServerRep;
import com.hacksthon.team.interfaces.KayouPayListener;
import com.hacksthon.team.interfaces.SocketListener;
import com.hacksthon.team.manager.SocketManager;
import com.hacksthon.team.utils.Constants;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.net.Socket;

public class PayActivity extends AppCompatActivity implements KayouPayListener {

    public static final String PAY_INFO="payInfo";

    private static final String TAG=PayActivity.class.getSimpleName();
    private TextView tvPayInfo;
    private Button btnSure;
    private SocketManager mSocketManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        tvPayInfo= (TextView) findViewById(R.id.tv_payinfo);
        btnSure= (Button) findViewById(R.id.btn_sure);
        btnSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pay(new BigDecimal(0.01));
            }
        });
//        ServerRep  serverRep = (ServerRep) getIntent().getSerializableExtra(PAY_INFO);
//        StringBuilder sb=new StringBuilder();
//        sb.append("订单号："+serverRep.orderNo+"\n");
//        sb.append("订单金额："+serverRep.amount+"\n");
//        sb.append("说明："+serverRep.info+"\n");
//        tvPayInfo.setText(sb.toString());
//        startSocket();
    }


    private void pay(BigDecimal tradeAmount){
        BigDecimal transAmount = tradeAmount.setScale(2, BigDecimal.ROUND_HALF_UP);
        com.cardinfo.smartpos.service.RequestBean requestBean = new com.cardinfo.smartpos.service.RequestBean();

        requestBean.setTransAmount(transAmount.toPlainString());
        /** 1 交易 2 撤销 3 小票补打  4 退货*/
        requestBean.setBusinessType("1");
        /** 交易方式  1 银行卡 2 扫码。（ 交易、撤销／退货、补打小票 ，参数均传1）*/
        requestBean.setBusinessMode("1");
        /** /打印页数 默认两页 商户存根 持卡人存根，第三联为银联存根*/
        requestBean.setPrintPage("2");
        requestBean.setThirdPartyTransOrderNum(System.currentTimeMillis()+"");
        requestBean.setThirdPartyInfo(System.currentTimeMillis()+"1001");
        /** 是否需要电子签名 默认不需要0 , 1 需要 */
        requestBean.setIsNeedElectronicSignature("1");
        doBusinessKaYou(requestBean,this);
    }

    private  void doBusinessKaYou(final com.cardinfo.smartpos.service.RequestBean  bean, final KayouPayListener listener) {
        TradeController.getInstance().init(this);
        Log.d(TAG, "doBusinessKaYou 卡友请求参数：" + (bean != null ? bean.toString() : null));

        TradeController.getInstance().doBusiness(bean, new TransCallback() {
            @Override
            public void onError(String errorCode, String errorMsg) {
                Log.d(TAG, "doBusinessKaYou 卡友回调错误 error: " + errorCode + " " + errorMsg);
                if (listener != null) {
                    listener.onError(errorCode, errorMsg, null);
                }
                Toast.makeText(getApplicationContext(),"支付失败="+errorMsg,Toast.LENGTH_SHORT).show();
                DeviceInfo info = new DeviceInfo();
                info.deviceMac = "20:59:a0:0e:58:c6";
                info.deviceIp = "192.168.0.114";
                info.deviceInfo = "刷卡成功";
                info.deviceType = 0x01;
                info.cmdType = CmdConstantType.CMD_PAY_SUCCESS;
                SocketManager.getInstance().sendData(info);

            }

            @Override
            public void onSuccess(ResponseBean responseBean) {
                Log.d(TAG, "doBusinessKaYou onSuccess() 卡友回调结果：" + (responseBean != null ? responseBean.toString() : null));
                if (listener != null) {
                    listener.onSuccess(responseBean);
                }
                Toast.makeText(getApplicationContext(),"支付成功",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onError(String errorCode, String errorMsg, ResponseBean responseBean) {

    }

    @Override
    public void onSuccess(ResponseBean responseBean) {

    }

}
