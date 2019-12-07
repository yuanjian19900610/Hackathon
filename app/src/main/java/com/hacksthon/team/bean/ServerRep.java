package com.hacksthon.team.bean;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 服务端发送给客户端的数据格式
 */
public class ServerRep implements Serializable {

    public Integer cmdType;//命令指令
    public String info;//描述信息
    public BigDecimal amount;//支付金额
    public String orderNo;//订单号

    @Override
    public String toString() {
        return "ServerRep{" +
                "cmdType=" + cmdType +
                ", info='" + info + '\'' +
                ", amount=" + amount +
                ", orderNo='" + orderNo + '\'' +
                '}';
    }
}
