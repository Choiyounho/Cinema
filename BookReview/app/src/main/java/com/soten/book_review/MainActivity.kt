package com.soten.book_review

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.soten.book_review.api.ApiKey
import com.soten.book_review.api.BookService
import com.soten.book_review.model.BestSellerDTO
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://book.interpark.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val bookService = retrofit.create(BookService::class.java)

        bookService.getBestSellerBooks(ApiKey.BOOK)
            .enqueue(object : Callback<BestSellerDTO> {
                override fun onResponse(
                    call: Call<BestSellerDTO>,
                    response: Response<BestSellerDTO>
                ) {
                    if (response.isSuccessful.not()) {
                        Log.e(TAG, "NOT SUCCESS")
                        return
                    }

                    response.body()?.let {
                        Log.d(TAG, it.toString())

                        it.books.forEach { book ->
                            Log.d(TAG, book.toString())
                        }
                    }
                }

                override fun onFailure(call: Call<BestSellerDTO>, t: Throwable) {
                    Log.e(TAG, t.toString())
                }

            })
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}

