package com.kakao.mobility.ui

import androidx.annotation.StringRes

sealed class ViewStatus(val index: Int) {
    object Loading : ViewStatus(0)
    object Contents : ViewStatus(1)
    class ErrorFinish(@StringRes val resourceId: Int) : ViewStatus(-1)
}