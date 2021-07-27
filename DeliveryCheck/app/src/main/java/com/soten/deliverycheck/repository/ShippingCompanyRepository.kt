package com.soten.deliverycheck.repository

import com.soten.deliverycheck.data.entity.ShippingCompany

interface ShippingCompanyRepository {

    suspend fun getShippingCompanies(): List<ShippingCompany>

}