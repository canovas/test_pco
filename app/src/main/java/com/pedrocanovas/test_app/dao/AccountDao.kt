package com.pedrocanovas.test_app.dao

import androidx.room.*
import com.pedrocanovas.test_app.models.AccountItem

@Dao
interface AccountDao {
    @get:Query("SELECT * FROM AccountItem")
    val all: List<AccountItem>

    @Insert
    fun insert(account: AccountItem?)

    @Insert
    fun insertAll(accounts: Array<AccountItem>)

    @Delete
    fun delete(account: AccountItem?)

    @Update
    fun update(account: AccountItem?)
}