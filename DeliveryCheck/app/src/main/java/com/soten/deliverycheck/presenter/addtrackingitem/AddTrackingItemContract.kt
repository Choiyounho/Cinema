package com.soten.deliverycheck.presenter.addtrackingitem

import com.soten.deliverycheck.data.entity.ShippingCompany
import com.soten.deliverycheck.presenter.BasePresenter
import com.soten.deliverycheck.presenter.BaseView

class AddTrackingItemContract {

    interface View : BaseView<Presenter> {

        fun showShippingCompaniesLoadingIndicator()

        fun hideShippingCompaniesLoadingIndicator()

        fun showRecommendShippingLoadingIndicator()

        fun hideRecommendShippingLoadingIndicator()

        fun showRecommendCompany(company: ShippingCompany)

        fun showSaveTrackingItemIndicator()

        fun hideSaveTrackingItemIndicator()

        fun showCompanies(companies: List<ShippingCompany>)

        fun enableSaveButton()

        fun disableSaveButton()

        fun showErrorToast(message: String)

        fun finish()
    }

    interface Presenter : BasePresenter {

        var invoice: String?
        var shippingCompanies: List<ShippingCompany>?
        var selectedShippingCompany: ShippingCompany?

        fun fetchShippingCompanies()

        fun fetchRecommendShippingCompany()

        fun changeSelectedShippingCompany(companyName: String)

        fun changeShippingInvoice(invoice: String)

        fun saveTrackingItem()
    }
}