package com.pedrocanovas.test_app.models

import android.content.Context
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.pedrocanovas.test_app.R


@Entity
class CategoryItem(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var name: String,
    var income: Boolean
): FinancialItem() {

    companion object {
        fun populateData(context: Context): Array<CategoryItem> {
            return arrayOf(
                CategoryItem(1, context.getString(R.string.data_tax), false),
                CategoryItem(2, context.getString(R.string.data_grocery), false),
                CategoryItem(3, context.getString(R.string.data_entertaiment), false),
                CategoryItem(4, context.getString(R.string.data_gym), false),
                CategoryItem(5, context.getString(R.string.data_health), false),
                CategoryItem(6, context.getString(R.string.data_salary), true),
                CategoryItem(7, context.getString(R.string.data_dividends), true)
            )
        }
    }

    override fun toString(): String = name

}