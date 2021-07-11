package com.soten.imagesearching

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.soten.imagesearching.data.Repository
import com.soten.imagesearching.databinding.ActivityMainBinding
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val scope = MainScope()

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fetchRandomPhotos()
    }

    private fun fetchRandomPhotos(query: String? = null) = scope.launch {
        Repository.getRandomPhotos(query)?.let { photos ->
            photos
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }
}