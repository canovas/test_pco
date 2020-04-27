package com.pedrocanovas.test_app.repository

import androidx.lifecycle.LiveData
import com.pedrocanovas.test_app.dao.CategoryDao
import com.pedrocanovas.test_app.models.CategoryItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext


class CategoryRepository(private val categoryDao: CategoryDao): CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main
    val categoryItems: LiveData<List<CategoryItem>> = categoryDao.getAll()
}