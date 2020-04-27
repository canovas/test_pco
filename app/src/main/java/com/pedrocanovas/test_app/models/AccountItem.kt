package com.pedrocanovas.test_app.models

import android.content.Context
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.pedrocanovas.test_app.R

@Entity
class AccountItem(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var name: String
): FinancialItem() {

    companion object {
        fun populateData(context: Context): Array<AccountItem> {
            return arrayOf(
                AccountItem(1, context.getString(R.string.data_cash)),
                AccountItem(2, context.getString(R.string.data_credit_card)),
                AccountItem(3, context.getString(R.string.data_bank_account))
            )
        }
    }

    override fun toString(): String = name

}