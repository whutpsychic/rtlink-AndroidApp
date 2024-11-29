package com.rtlink.androidapp.webIO

class CallbackKeys {


    companion object {

        private const val COMMON_KEY: String = "Callback"

        // modalTips()
        const val MODAL_TIPS: String = "modalTips$COMMON_KEY"

        // modalConfirm()
        const val MODAL_CONFIRM: String = "modalConfirm$COMMON_KEY"

        // modalProgress()
        const val MODAL_PROGRESS: String = "modalProgress$COMMON_KEY"

        // readLocal()
        const val READ_LOCAL: String = "readLocal$COMMON_KEY"

        // scan()
        const val SCAN: String = "scan$COMMON_KEY"

        // networkType
        const val NETWORK_TYPE: String = "checkNetworkType$COMMON_KEY"
    }
}