package com.rtlink.androidapp

class GlobalConfig {
    // 静态对象
    companion object {
        // web前端访问地址
//        const val WEB_URL: String = "http://192.168.0.2:8088"
        const val WEB_URL: String = "http://192.168.1.71:8088"

        // web端JS调用原生接口时的对象名字
        const val IO_NAME: String = "Android"

        // RAM名称
        const val RAM_NAME: String = "RTMB"
    }
}