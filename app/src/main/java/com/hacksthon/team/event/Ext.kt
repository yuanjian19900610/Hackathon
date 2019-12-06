package com.hacksthon.team.event


// 定义 gson 扩展
inline fun <reified T> com.google.gson.Gson.parseJson(json : String) : T = fromJson(json, T::class.java)

