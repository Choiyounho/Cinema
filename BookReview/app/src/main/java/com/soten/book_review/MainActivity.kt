package com.soten.book_review

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.MotionEvent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.soten.book_review.adapter.BookAdapter
import com.soten.book_review.adapter.HistoryAdapter
import com.soten.book_review.api.BookService
import com.soten.review.databinding.ActivityMainBinding
import com.soten.book_review.model.book.BestSellersDTO
import com.soten.book_review.model.book.SearchBooksDTO
import com.soten.book_review.model.history.History
import com.soten.review.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: BookAdapter
    private lateinit var historyAdapter: HistoryAdapter

    private lateinit var service: BookService

    private lateinit var db: AppDatabase

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "historyDB"
        ).build()

        adapter = BookAdapter(clickListener = {
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra("bookModel", it)
            startActivity(intent)
        })
        historyAdapter = HistoryAdapter(
            historySearchClickListener = {
                search(it)
            },
            historyDeleteClickListener = {
                deleteSearchKeyword(it)
            })




        getBestSellerList()

        binding.bookRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.bookRecyclerView.adapter = adapter

        binding.searchEditText.setOnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN) {
                search(binding.searchEditText.text.toString())
                return@setOnKeyListener true
            }
            return@setOnKeyListener false

        }

        binding.searchEditText.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                showHistoryView()
            }

            return@setOnTouchListener false
        }


        binding.historyRecyclerView.adapter = historyAdapter
        binding.historyRecyclerView.layoutManager = LinearLayoutManager(this)


    }

    private fun getBestSellerList() {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        service = retrofit.create(BookService::class.java)
        service.getBestSeller(getString(R.string.interParkAPIKey))
            .enqueue(object : Callback<BestSellersDTO> {
                override fun onFailure(call: Call<BestSellersDTO>, t: Throwable) {

                }

                override fun onResponse(
                    call: Call<BestSellersDTO>,
                    response: Response<BestSellersDTO>
                ) {
                    if (response.isSuccessful.not()) {
                        return
                    }

                    response.body()?.let {
                        adapter.submitList(it.books)
                    }
                }

            })
    }

    private fun search(text: String) {
        service.getBooksByName(getString(R.string.interParkAPIKey), text)
            .enqueue(object : Callback<SearchBooksDTO> {
                override fun onFailure(call: Call<SearchBooksDTO>, t: Throwable) {
                    hideHistoryView()
                }

                override fun onResponse(
                    call: Call<SearchBooksDTO>,
                    response: Response<SearchBooksDTO>
                ) {

                    hideHistoryView()
                    saveSearchKeyword(text)

                    if (response.isSuccessful.not()) {
                        return
                    }

                    response.body()?.let {
                        adapter.submitList(it.books)
                    }
                }

            })
    }

    private fun showHistoryView() {
        Thread {
            db.historyDao().getAll().reversed().run {
                runOnUiThread {
                    binding.historyRecyclerView.isVisible = true
                    historyAdapter.submitList(this)
                }
            }

        }.start()

    }

    private fun hideHistoryView() {
        binding.historyRecyclerView.isVisible = false
    }

    private fun saveSearchKeyword(keyword: String) {
        Thread {
            db.historyDao().insertHistory(History(null, keyword))
        }.start()
    }

    private fun deleteSearchKeyword(keyword: String) {
        Thread {
            db.historyDao().delete(keyword)
            showHistoryView()
        }.start()
    }

    var lastTimeBackPressed: Long = 0

    override fun onBackPressed() {
        if (System.currentTimeMillis() - lastTimeBackPressed >= 1500) {
            lastTimeBackPressed = System.currentTimeMillis()
            getBestSellerList()
        } else {
            ActivityCompat.finishAffinity(this)
            System.runFinalization()
            exitProcess(0)
        }
    }


    companion object {
        private const val TAG = "MainActivity"
        private const val BASE_URL = "https://book.interpark.com/"
    }
}