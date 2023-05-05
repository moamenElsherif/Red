package com.graduation.red.presentation.home.donate

import com.graduation.red.presentation.home.request.RequestsModel

interface DonateListener {
    fun clickDetails(item: RequestsModel)
    fun clickDonate(requestId: String)
}