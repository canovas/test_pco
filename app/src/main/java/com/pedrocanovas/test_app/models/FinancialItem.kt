package com.pedrocanovas.test_app.models

open class FinancialItem {
    fun identifier() = this::class.java.name.hashCode()
}