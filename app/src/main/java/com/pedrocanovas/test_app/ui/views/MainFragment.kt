package com.pedrocanovas.test_app.ui.views

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.TextUtils
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.os.ConfigurationCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.jpvs0101.currencyfy.Currencyfy
import com.pedrocanovas.test_app.R
import com.pedrocanovas.test_app.models.AccountItem
import com.pedrocanovas.test_app.models.CategoryItem
import com.pedrocanovas.test_app.models.TransactionItem
import com.pedrocanovas.test_app.ui.adapters.RecycledViewOnClick
import com.pedrocanovas.test_app.ui.adapters.TransactionRecyclerAdapter
import com.pedrocanovas.test_app.utils.IOnBackPressed
import kotlinx.android.synthetic.main.add_transaction_layout.*
import kotlinx.android.synthetic.main.main_fragment.*
import java.util.*


class MainFragment() : Fragment(), RecycledViewOnClick, View.OnClickListener, IOnBackPressed {

    private var accountSelected: Int? = null
    var accounts: List<AccountItem>? = null
    companion object{
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel
    private lateinit var adapter: TransactionRecyclerAdapter


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)


        val currentLocale = ConfigurationCompat.getLocales(resources.configuration)[0]
        adapter = TransactionRecyclerAdapter(this, currentLocale)
        rvTransactions.adapter = adapter
        rvTransactions.layoutManager = LinearLayoutManager(context)

        addButton.setOnClickListener {
            linearAddTransaction.visibility = View.VISIBLE
            addButton.visibility = View.GONE
        }

        viewModel.prepareMenu()

        prepareObservers()

        prepareAddView()

    }

    private fun prepareAddView() {
        saveButton.setOnClickListener {
            if ((!TextUtils.isEmpty(textName.text)) &&  (!TextUtils.isEmpty(textAmount.text))) {
                val accountAux: AccountItem = spinnerAccount.selectedItem as AccountItem
                val categoryAux: CategoryItem = spinnerCategory.selectedItem as CategoryItem
                var amount: Double = textAmount.text.toString().toDouble()

                if(!categoryAux.income){
                    amount*=-1
                }

                val tr = TransactionItem(textName.text.toString(), System.currentTimeMillis(), categoryAux.id, categoryAux.name, accountAux.id, accountAux.name, amount, categoryAux.income)
                viewModel.insertTransaction(tr)

                linearAddTransaction.visibility = View.GONE
                addButton.visibility = View.VISIBLE
                resetForm()
            }
            else{
                Toast.makeText(context, R.string.fill_fields, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun resetForm() {

        textName.text.clear()
        textAmount.text.clear()
        spinnerAccount.setSelection(0)
        spinnerCategory.setSelection(0)
    }


    @SuppressLint("SetTextI18n")
    private fun prepareObservers() {

        // All transactions:
        /*viewModel.transactionsItems.observe(viewLifecycleOwner, Observer { items ->
            items?.let { adapter.setTransactions(it) }
        })*/

        viewModel.categoryItems.observe(viewLifecycleOwner, Observer { items ->
            val arrayadapter =
                context?.let { ArrayAdapter(it, android.R.layout.simple_spinner_item, items) }
            spinnerCategory.adapter = arrayadapter
        })

        // prepare Menu
        val accountSizeObserver = Observer<List<AccountItem>> { accounts ->
            this.accounts = accounts
            for (i in accounts.indices) {
                val buttonMenu = Button(context)
                buttonMenu.id = accounts[i].id
                buttonMenu.text = accounts[i].name

                val forButtonParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                buttonMenu.layoutParams = forButtonParams

                val dm = DisplayMetrics()
                activity!!.window.windowManager.defaultDisplay.getMetrics(dm)
                val width = dm.widthPixels

                buttonMenu.layoutParams.width = width/accounts.size;

                buttonMenu.gravity = Gravity.CENTER
                buttonMenu.textAlignment = View.TEXT_ALIGNMENT_CENTER
                linearLayoutMenu.addView(buttonMenu)
                buttonMenu.setOnClickListener(this)
            }

            val arrayadapter =
                context?.let { ArrayAdapter(it, android.R.layout.simple_spinner_item, accounts) }
            spinnerAccount.adapter = arrayadapter
        }

        val transactionObserver = Observer<List<TransactionItem>> { transactions ->
            // text legend visibility
            emptyDataText.text= getString(R.string.empty_data)
            if(transactions.isNotEmpty()){
                emptyDataText.visibility= View.GONE
            }else{
                emptyDataText.visibility= View.VISIBLE
            }
            linearAddTransaction.visibility = View.GONE
            addButton.visibility = View.VISIBLE

            adapter.setTransactions(transactions)
            viewModel.getSumTransaction(accountSelected)

        }

        val sumTransactionObserver = Observer<Double> { totalValue ->
            if(totalValue!=0.0) {
                val currentLocale = ConfigurationCompat.getLocales(resources.configuration)[0]
                val amount: String = Currencyfy.currencyfy(
                    Locale("en", currentLocale.language),
                    totalValue
                )
                textViewTotal.text = getString(R.string.total_amount)+amount
            }else{
                textViewTotal.text=""
            }

        }

        viewModel.sumByAccount.observe(viewLifecycleOwner, sumTransactionObserver)
        viewModel.acountsLive.observe(viewLifecycleOwner, accountSizeObserver)
        viewModel.transactionByAccount.observe(viewLifecycleOwner, transactionObserver)
    }


    // recycler view listener
    override fun onClick(view: View?, i: Int) {
        viewModel.deleteTransaction(adapter.getTransactionByPosition(i))
    }

    // menu listener
    override fun onClick(view: View?) {
        println("account selected: "+view?.id)
        accountSelected=view?.id
        viewModel.filterByAccountId.postValue(accountSelected)
        viewModel.getSumTransaction(accountSelected)
    }

    override fun onBackPressed(): Boolean {
        if (linearAddTransaction.visibility == View.VISIBLE) {
            linearAddTransaction.visibility = View.GONE
            addButton.visibility = View.VISIBLE
            return false
        } else {
            return true
        }
    }
}