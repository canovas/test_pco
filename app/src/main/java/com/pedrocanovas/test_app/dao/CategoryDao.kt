package com.pedrocanovas.test_app.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.pedrocanovas.test_app.models.CategoryItem

@Dao
interface CategoryDao {
    @Query("SELECT * FROM CategoryItem")
    fun getAll(): LiveData<List<CategoryItem>>

    @Insert
    fun insert(expense: CategoryItem?)

    @Insert
    fun insertAll(expenses: Array<CategoryItem>)

    @Delete
    fun delete(expense: CategoryItem?)

}