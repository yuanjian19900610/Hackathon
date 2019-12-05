package com.hacksthon.team.interfaces;

/**
 * <pre>
 * com.hacksthon.demo.interfaces
 *
 * *-------------------------------------------------------------------*
 *     scott
 *                                    江城子 . 程序员之歌
 *     /\__/\
 *    /`    '\                     十年生死两茫茫，写程序，到天亮。
 *  ≈≈≈ 0  0 ≈≈≈ Hello world!          千行代码，Bug何处藏。
 *    \  --  /                     纵使上线又怎样，朝令改，夕断肠。
 *   /        \                    领导每天新想法，天天改，日日忙。
 *  /          \                       相顾无言，惟有泪千行。
 * |            |                  每晚灯火阑珊处，夜难寐，加班狂。
 *  \  ||  ||  /
 *   \_oo__oo_/≡≡≡≡≡≡≡≡o
 *
 * Created by scott on 2019/12/5.
 *
 * *-------------------------------------------------------------------*
 *  </pre>
 */
public interface KayouPayListener {
    /**
     * @param errorCode
     * @param errorMsg
     * @param responseBean 错误情况下，需要设置几个字段默认值为-1，否则sync那边校验不通过
     */
    void onError(String errorCode, String errorMsg, com.cardinfo.smartpos.service.ResponseBean responseBean);

    void onSuccess(com.cardinfo.smartpos.service.ResponseBean  responseBean);
}
