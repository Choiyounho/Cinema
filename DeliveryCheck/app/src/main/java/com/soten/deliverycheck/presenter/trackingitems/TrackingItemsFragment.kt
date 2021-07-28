package com.soten.deliverycheck.presenter.trackingitems

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.soten.deliverycheck.R
import com.soten.deliverycheck.data.entity.TrackingInformation
import com.soten.deliverycheck.data.entity.TrackingItem
import com.soten.deliverycheck.databinding.FragmentTrackingItemsBinding
import org.koin.android.scope.ScopeFragment

class TrackingItemsFragment : ScopeFragment(), TrackingItemsContract.View {

    override val presenter: TrackingItemsContract.Presenter by inject()

    private var _binding: FragmentTrackingItemsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentTrackingItemsBinding.inflate(inflater)
        .also { _binding = it }
        .root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        bindViews()
        presenter.onViewCreated()
    }

    private fun initViews() {
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = TrackingItemsAdapter()
        }
    }

    private fun bindViews() = with(binding) {
        refreshLayout.setOnRefreshListener {
            presenter.refresh()
        }
        addTrackingItemButton.setOnClickListener {
            findNavController().navigate(R.id.to_addTrackingItemFragment)
        }
        addTrackingItemFloatingActionButton.setOnClickListener { _ ->
            findNavController().navigate(R.id.to_addTrackingItemFragment)
        }
        (binding.recyclerView.adapter as? TrackingItemsAdapter)?.onClickItemListener = { item, information ->
            findNavController()
                .navigate(TrackingItemsFragmentDirections.toTrackingHistoryFragment(item, information))
        }
    }

    override fun showLoadingIndicator() {
        binding.progressBar.visibility = View.VISIBLE
    }

    override fun hideLoadingIndicator() {
        binding.progressBar.visibility = View.GONE
        binding.refreshLayout.isRefreshing = false
    }

    override fun showNoDataDescription() {
        binding.refreshLayout.visibility = View.INVISIBLE
        binding.noDataContainer.visibility = View.VISIBLE
    }

    override fun showTrackingItemInformation(trackingItemInformation: List<Pair<TrackingItem, TrackingInformation>>) {
        binding.refreshLayout.visibility = View.VISIBLE
        binding.noDataContainer.visibility = View.GONE
        (binding.recyclerView.adapter as TrackingItemsAdapter).apply {
            this.data = trackingItemInformation
            notifyDataSetChanged()
        }
    }

}