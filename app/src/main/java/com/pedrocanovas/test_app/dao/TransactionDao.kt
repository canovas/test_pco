package com.pedrocanovas.test_app.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.pedrocanovas.test_app.models.TransactionItem


@Dao
interface TransactionDao {
    @Query("SELECT * FROM TransactionItem")
    fun getAll(): LiveData<List<TransactionItem>>

    @Query("SELECT * FROM TransactionItem WHERE accountId = :accountId")
    fun getAllByAccountId(accountId: Int): LiveData<List<TransactionItem>>

    @Query("SELECT SUM(amount) FROM TransactionItem WHERE accountId = :accountId")
    fun sumByAccountId(accountId: Int): Double


    @Insert
    fun insert(transaction: TransactionItem?)

    @Insert
    fun insertAll(transactions: Array<TransactionItem>)

    @Delete
    fun delete(transaction: TransactionItem?)

    @Update
    fun update(transaction: TransactionItem?)
}