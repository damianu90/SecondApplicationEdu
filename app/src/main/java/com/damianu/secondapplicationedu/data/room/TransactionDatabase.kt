package com.damianu.secondapplicationedu.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.damianu.secondapplicationedu.data.model.Transaction

@Database(entities = [Transaction::class], version = 1, exportSchema = false)
abstract class TransactionDatabase: RoomDatabase() {
    abstract fun transactionsDao(): TransacionsDao
}