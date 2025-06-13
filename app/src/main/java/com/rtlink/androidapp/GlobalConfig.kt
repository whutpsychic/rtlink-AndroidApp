package com.rtlink.androidapp

class GlobalConfig {
    // 静态对象
    companion object {
        // web前端访问地址(默认)
//        const val WEB_URL: String = "http://www.tencent.com"
        const val WEB_URL: String = "http://192.168.1.33:8088"
//        const val WEB_URL: String = "http://192.168.1.71:8082/mobile"
//        const val WEB_URL: String = "http://192.168.0.2:8088"

        // web端JS调用原生接口时的对象名字
        const val IO_NAME: String = "Android"

        // RAM名称
        const val RAM_NAME: String = "RTMB"

        // 使用离线模式加载本地 .html 文件
        const val OFFLINE_MODE: Boolean = false
    }
}