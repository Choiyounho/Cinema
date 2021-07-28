package com.soten.deliverycheck.data.entity

import com.google.gson.annotations.SerializedName

data class ShippingCompanies(
    @SerializedName("Company", alternate = ["Recommend"])
    val shippingCompanies: List<ShippingCompany>? = null
)

/*
* 동일한 응답에 대해 필드명이 같다면 alternate 를 사용
* */
