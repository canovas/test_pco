package com.pedrocanovas.test_app.repository

import androidx.lifecycle.LiveData
import com.pedrocanovas.test_app.dao.TransactionDao
import com.pedrocanovas.test_app.models.TransactionItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext


class TransactionRepository(private val transactionDao: TransactionDao): CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    //val transactionItems: LiveData<List<TransactionItem>> = transactionDao.getAll()

    fun getAllTransaction():LiveData<List<TransactionItem>> {
        return transactionDao.getAll()
    }

    fun getSumByAccountId(accountId: Int):Double {
       return transactionDao.sumByAccountId(accountId)
    }

    fun getTransactionByAccountId(accountId: Int):LiveData<List<TransactionItem>> {
        return transactionDao.getAllByAccountId(accountId)
    }

    fun setTransaction(transaction: TransactionItem) {
        launch  { setInternalTransaction(transaction) }
    }

    fun removeTransaction(transaction: TransactionItem) {
        launch  { removeInternalTransaction(transaction) }
    }

    private suspend fun setInternalTransaction(transaction: TransactionItem){
        withContext(Dispatchers.IO){
            transactionDao.insert(transaction)
        }
    }

    private suspend fun removeInternalTransaction(transaction: TransactionItem){
        withContext(Dispatchers.IO){
            transactionDao.delete(transaction)
        }
    }
}