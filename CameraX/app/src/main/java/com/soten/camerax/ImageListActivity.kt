package com.soten.camerax

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.soten.camerax.adapter.ImageViewPagerAdapter
import com.soten.camerax.databinding.ActivityImageListBinding

class ImageListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityImageListBinding

    private lateinit var adapter: ImageViewPagerAdapter

    private val uriList by lazy<List<Uri>> { intent.getParcelableArrayListExtra(URI_LIST_KEY)!! }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImageListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()
    }

    private fun initViews() {
        setSupportActionBar(binding.toolbar)
        setUpImageList()
    }

    private fun setUpImageList() = with(binding) {
        if (::adapter.isInitialized.not()) {
            adapter = ImageViewPagerAdapter(uriList)
        }
        imageViewPager.adapter = ImageViewPagerAdapter(uriList)
        indicator.setViewPager(imageViewPager)
        imageViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                toolbar.title = getString(R.string.images_page, position + 1, adapter.uriList.size)
            }
        })
    }

    companion object {
        private const val URI_LIST_KEY = "uriList"

        fun newIntent(activity: Activity, uriList: List<Uri>) =
            Intent(activity, ImageListActivity::class.java).apply {
                putExtra(URI_LIST_KEY, ArrayList<Uri>().apply { uriList.forEach { add(it) } })
            }

    }
}