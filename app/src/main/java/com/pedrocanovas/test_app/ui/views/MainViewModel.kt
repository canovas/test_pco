package com.pedrocanovas.test_app.ui.views

import android.app.Application
import android.os.AsyncTask
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.pedrocanovas.test_app.database.AppDatabase
import com.pedrocanovas.test_app.models.AccountItem
import com.pedrocanovas.test_app.models.CategoryItem
import com.pedrocanovas.test_app.models.TransactionItem
import com.pedrocanovas.test_app.repository.CategoryRepository
import com.pedrocanovas.test_app.repository.TransactionRepository


class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val transactionRepository: TransactionRepository
    private val categoryRepository: CategoryRepository
    var filterByAccountId = MutableLiveData<Int>()
    val transactionsItems: LiveData<List<TransactionItem>>
    val categoryItems: LiveData<List<CategoryItem>>

    val transactionByAccount: LiveData<List<TransactionItem>>
        get() = Transformations.switchMap(filterByAccountId) { account ->
            val allTransactionItem = transactionRepository.getAllTransaction()
            val transactions =
                    Transformations.switchMap(allTransactionItem) { transactionList ->
                        val filteredTransaction = MutableLiveData<List<TransactionItem>>()
                        val filteredList = transactionList.filter { tr -> tr.accountId == account }
                        filteredTransaction.value = filteredList
                        filteredTransaction
            }
            transactions
        }

    init {
        val transactionDao = AppDatabase.getDatabase(application).transactionDao()
        val categoryDao = AppDatabase.getDatabase(application).categoryDao()

        transactionRepository = TransactionRepository(transactionDao)
        categoryRepository = CategoryRepository(categoryDao)

        transactionsItems = transactionRepository.getAllTransaction()
        categoryItems = categoryRepository.categoryItems
    }

    val acountsLive: MutableLiveData<List<AccountItem>> by lazy {
        MutableLiveData<List<AccountItem>>()
    }

    val sumByAccount: MutableLiveData<Double> by lazy {
        MutableLiveData<Double>()
    }


    fun getTransactionsByAccountId(accountId:Int){
        //transactions.postValue(transactionRepository.getTransactionByAccountId(accountId)
    }



    fun prepareMenu() {
        AsyncTask.execute {
            val accounts = AppDatabase.getDatabase(getApplication()).accountDao().all
            acountsLive.postValue(accounts)
        }
    }

    fun getSumTransaction(accountId: Int?) {
        AsyncTask.execute {
            val total = accountId?.let {
                AppDatabase.getDatabase(getApplication()).transactionDao().sumByAccountId(it)
            }
            sumByAccount.postValue(total)
        }
    }

    // repository op
    fun insertTransaction(transaction: TransactionItem) {
        transactionRepository.setTransaction(transaction)
    }

    fun deleteTransaction(transaction: TransactionItem) {
        transactionRepository.removeTransaction(transaction)
    }
}
