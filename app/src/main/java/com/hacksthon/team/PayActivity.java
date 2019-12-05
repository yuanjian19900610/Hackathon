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
import com.hacksthon.team.interfaces.KayouPayListener;
import com.hacksthon.team.interfaces.SocketListener;
import com.hacksthon.team.manager.SocketManager;
import com.hacksthon.team.utils.Constants;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.Socket;

public class PayActivity extends AppCompatActivity implements KayouPayListener {

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
        startSocket("");
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
        requestBean.setThirdPartyTransOrderNum("12312312");
        requestBean.setThirdPartyInfo("2324234");
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


    private void startSocket(final String ipAddrss) {
        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    Socket socket = new Socket(ipAddrss, Constants.PORT);
                    mSocketManager = SocketManager.getInstance(socket);
                    mSocketManager.setEnable(true);
                    mSocketManager.receiveData();
                    mSocketManager.setSocketListener(new SocketListener() {
                        @Override
                        public void receiveData(final String data) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    tvPayInfo.setText(data);
                                }
                            });
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }
}
