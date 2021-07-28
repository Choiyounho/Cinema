package com.soten.deliverycheck.repository

import com.soten.deliverycheck.data.api.SweetTrackerApi
import com.soten.deliverycheck.data.db.ShippingCompanyDao
import com.soten.deliverycheck.data.entity.ShippingCompany
import com.soten.deliverycheck.preference.PreferenceManager
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class ShippingCompanyRepositoryImpl(
    private val trackerApi: SweetTrackerApi,
    private val shippingCompanyDao: ShippingCompanyDao,
    private val preferenceManager: PreferenceManager,
    private val dispatcher: CoroutineDispatcher
) : ShippingCompanyRepository {

    override suspend fun getShippingCompanies(): List<ShippingCompany> = withContext(dispatcher) {
        // 현재 시간
        val currentTimeMillis = System.currentTimeMillis()
        // 마지막 업데이트 시간
        val lastDatabaseUpdateTimeMillis = preferenceManager.getLong(KEY_LAST_DATABASE_UPDATED_TIME_MILLIS)

        // 마지막 업데이트 시간이 없거나, 저장된 파일의 유효기간이 다 됐을 시 새로 저장
        if (lastDatabaseUpdateTimeMillis == null ||
                CACHE_MAX_AGE_MILLIS < (currentTimeMillis - lastDatabaseUpdateTimeMillis)
        ) {
            val shippingCompanies = trackerApi.getShippingCompanies()
                .body()
                ?.shippingCompanies
                ?: emptyList()

            shippingCompanyDao.insert(shippingCompanies)
            preferenceManager.putLong(KEY_LAST_DATABASE_UPDATED_TIME_MILLIS, currentTimeMillis)
        }

        // 반환
       shippingCompanyDao.getAll()
    }

    override suspend fun getRecommendShippingCompany(invoice: String): ShippingCompany? = withContext(dispatcher) {
        try {
            trackerApi.getRecommendShippingCompanies(invoice)
                .body()
                ?.shippingCompanies
                ?.minByOrNull { it.code.toIntOrNull() ?: Int.MAX_VALUE }
        } catch (e: Exception) {
            null
        }
    }

    companion object {
        private const val KEY_LAST_DATABASE_UPDATED_TIME_MILLIS = "KEY_LAST_DATABASE_UPDATED_TIME_MILLIS"
        private const val CACHE_MAX_AGE_MILLIS = 1000L * 60 * 60 * 24 * 7
    }

}