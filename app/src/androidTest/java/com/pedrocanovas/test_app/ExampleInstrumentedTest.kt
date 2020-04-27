package com.pedrocanovas.test_app

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.pedrocanovas.test_app.dao.TransactionDao
import com.pedrocanovas.test_app.database.AppDatabase
import com.pedrocanovas.test_app.models.TransactionItem
import com.pedrocanovas.test_app.repository.TransactionRepository

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before

@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {

    private lateinit var transactionsDao: TransactionDao
    private lateinit var transactionRepository: TransactionRepository

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        transactionsDao = AppDatabase.getDatabase(context).transactionDao()
        transactionRepository = TransactionRepository(transactionsDao)
    }

    @Test
    fun should_Insert_Transactions() {
        val tr1 = TransactionItem("transaction 1", System.currentTimeMillis(), 1,"type1",1,"account1",100.2, true)
        transactionRepository.setTransaction(tr1)
        val accountTest = transactionRepository.getTransactionByAccountId(1)
        assertNotNull(accountTest)
    }

}
