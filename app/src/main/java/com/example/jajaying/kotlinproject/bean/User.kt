package com.example.jajaying.kotlinproject.bean

/**
 * Created by wangyao3 on 2018/8/30.
 */

data class User(
    var data: Data,
    var status: Int,
    var message: String
)

data class Data(
    var yesterday: Yesterday,
    var city: String,
    var aqi: String,
    var forecast: List<Forecast>,
    var ganmao: String,
    var wendu: String
)

data class Yesterday(
    var date: String,
    var high: String,
    var fx: String,
    var low: String,
    var fl: String,
    var type: String
)

data class Forecast(
    var date: String,
    var high: String,
    var fengli: String,
    var low: String,
    var fengxiang: String,
    var type: String
)