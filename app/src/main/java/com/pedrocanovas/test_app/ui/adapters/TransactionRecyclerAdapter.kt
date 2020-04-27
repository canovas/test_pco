package com.pedrocanovas.test_app.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.jpvs0101.currencyfy.Currencyfy.currencyfy
import com.pedrocanovas.test_app.R
import com.pedrocanovas.test_app.models.TransactionItem
import java.util.*


class TransactionRecyclerAdapter(
    val listener: RecycledViewOnClick,
    val currentLocale: Locale
): RecyclerView.Adapter<TransactionRecyclerAdapter.TransactionViewHolder>() {


    private var transactionItems = emptyList<TransactionItem>()
    private lateinit var context: Context

    inner class TransactionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameItemView: TextView = itemView.findViewById(R.id.textView)
        val typeItemView: TextView = itemView.findViewById(R.id.textViewType)
        val amountItemView: TextView = itemView.findViewById(R.id.textViewAmount)
        val removeTransactionButton: Button = itemView.findViewById(R.id.removeTransactionButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        context = parent.context
        val inflater: LayoutInflater = LayoutInflater.from(context)
        val itemView = inflater.inflate(R.layout.item_row, parent, false)
        return TransactionViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val current = transactionItems[position]
        holder.nameItemView.text = current.name
        holder.typeItemView.text = current.typeName

        val amount: String = currencyfy(Locale("en", currentLocale.language), current.amount)
        holder.amountItemView.text = amount

        if(!current.income){
            holder.amountItemView.setTextColor(ContextCompat.getColor(context, R.color.colorRed))
        }else{
            holder.amountItemView.setTextColor(ContextCompat.getColor(context, R.color.colorGreen))
        }

        holder.removeTransactionButton.setOnClickListener { v -> listener.onClick(v, position) }
    }

    internal fun setTransactions(items: List<TransactionItem>) {
        this.transactionItems = items
        notifyDataSetChanged()
    }
    fun getTransactionByPosition(i: Int): TransactionItem {
        return transactionItems[i]
    }
    override fun getItemCount() = transactionItems.size

}