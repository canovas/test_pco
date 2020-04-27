package com.pedrocanovas.test_app.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.pedrocanovas.test_app.dao.AccountDao
import com.pedrocanovas.test_app.dao.CategoryDao
import com.pedrocanovas.test_app.dao.TransactionDao
import com.pedrocanovas.test_app.models.AccountItem
import com.pedrocanovas.test_app.models.CategoryItem
import com.pedrocanovas.test_app.models.TransactionItem
import java.util.concurrent.Executors


@Database(entities = [TransactionItem::class, AccountItem::class, CategoryItem::class], version = 1, exportSchema = false)
public abstract class AppDatabase : RoomDatabase() {

    abstract fun transactionDao(): TransactionDao
    abstract fun accountDao(): AccountDao
    abstract fun categoryDao(): CategoryDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "financial_db"
                )
                    .addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            Executors.newSingleThreadScheduledExecutor()
                                .execute(Runnable {
                                    getDatabase(context).accountDao().insertAll(AccountItem.populateData(context))
                                    getDatabase(context).categoryDao().insertAll(CategoryItem.populateData(context))
                                })
                        }
                    })
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}