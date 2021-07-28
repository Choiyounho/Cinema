package com.soten.deliverycheck.presenter.addtrackingitem

import com.soten.deliverycheck.data.entity.ShippingCompany
import com.soten.deliverycheck.data.entity.TrackingItem
import com.soten.deliverycheck.repository.ShippingCompanyRepository
import com.soten.deliverycheck.repository.TrackingItemRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class AddTrackingItemPresenter(
    private val view: AddTrackingItemContract.View,
    private val shippingCompanyRepository: ShippingCompanyRepository,
    private val trackerRepository: TrackingItemRepository
) : AddTrackingItemContract.Presenter {

    override val scope: CoroutineScope = MainScope()

    override var invoice: String? = null
    override var shippingCompanies: List<ShippingCompany>? = null
    override var selectedShippingCompany: ShippingCompany? = null

    override fun onViewCreated() {
        fetchShippingCompanies()
    }

    override fun onDestroyView() {}

    override fun fetchShippingCompanies() {
        scope.launch {
            view.showShippingCompaniesLoadingIndicator()
            if (shippingCompanies.isNullOrEmpty()) {
                shippingCompanies = shippingCompanyRepository.getShippingCompanies()
            }

            shippingCompanies?.let { view.showCompanies(it) }
            view.hideShippingCompaniesLoadingIndicator()
        }
    }

    override fun fetchRecommendShippingCompany() {
        scope.launch {
            view.showRecommendShippingLoadingIndicator()
            shippingCompanyRepository.getRecommendShippingCompany(invoice!!)?.let { view.showRecommendCompany(it) }
            view.hideRecommendShippingLoadingIndicator()
        }
    }

    override fun changeSelectedShippingCompany(companyName: String) {
        selectedShippingCompany = shippingCompanies?.find { it.name == companyName }
        enableSaveButtonIfAvailable()
    }

    override fun changeShippingInvoice(invoice: String) {
        this.invoice = invoice
        enableSaveButtonIfAvailable()
    }

    override fun saveTrackingItem() {
        scope.launch {
            try {
                view.showSaveTrackingItemIndicator()
                trackerRepository.saveTrackingItem(
                    TrackingItem(
                        invoice!!,
                        selectedShippingCompany!!
                    )
                )
                view.finish()
            } catch (exception: Exception) {
                view.showErrorToast(exception.message ?: "서비스에 문제가 생겨서 운송장을 추가하지 못했어요 😢")
            } finally {
                view.hideSaveTrackingItemIndicator()
            }
        }
    }

    private fun enableSaveButtonIfAvailable() {
        if (!invoice.isNullOrBlank() && selectedShippingCompany != null) {
            view.enableSaveButton()
        } else {
            view.disableSaveButton()
        }
    }
}