package com.soten.book_review

import androidx.room.Database
import androidx.room.RoomDatabase
import com.soten.book_review.dao.HistoryDao
import com.soten.book_review.model.history.History

@Database(entities = [History::class], version = 1)
abstract class AppDataBase: RoomDatabase() {
    abstract fun historyDao(): HistoryDao
}