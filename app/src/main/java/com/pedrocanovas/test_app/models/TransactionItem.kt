package com.pedrocanovas.test_app.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.NO_ACTION
import androidx.room.PrimaryKey

@Entity
data class TransactionItem(
    var name: String,
    var date: Long,
    var typeId: Int,
    var typeName: String,
    var accountId: Int,
    var accountName: String,
    var amount: Double,
    var income: Boolean
): FinancialItem(){
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}