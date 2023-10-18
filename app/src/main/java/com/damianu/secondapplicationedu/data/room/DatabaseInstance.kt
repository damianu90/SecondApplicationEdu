package com.damianu.secondapplicationedu.data.room

import android.content.Context
import androidx.room.Room

object DatabaseInstance {
    private var instance: TransactionDatabase? = null

    fun getInstance(context: Context): TransactionDatabase {
        if (instance == null) {
            synchronized(TransactionDatabase::class.java) {
                instance = roomBuild(context)
            }
        }

        return instance!!
    }

    private fun roomBuild(context: Context): TransactionDatabase =
        Room.databaseBuilder(context,
            TransactionDatabase::class.java,
            "transactions_database")
            .fallbackToDestructiveMigration()
            .build()
}