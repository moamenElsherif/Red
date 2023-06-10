package com.graduation.red.presentation.home.profile.requests_summery

import com.graduation.red.presentation.home.request.RequestsModel

interface RequestSummeryListener {
    fun onItemClick(item: RequestsModel)
}