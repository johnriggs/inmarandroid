package com.johnriggsdev.inmarandroid.main

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.johnriggsdev.inmarandroid.R
import com.johnriggsdev.inmarandroid.model.Currency
import kotlinx.android.synthetic.main.row_main_rv.view.*

class MainAdapter(private var currencies : MutableList<Currency>, private val clickListener: ClickListener, private val context : Context) : RecyclerView.Adapter<MainAdapter.MainViewHolder>() {

    companion object {
        private final val USD = "USD"
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.row_main_rv, parent, false)
        return MainViewHolder(view)
    }

    override fun getItemCount(): Int {
        return currencies.size
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val currency = currencies.get(position)
        val quote = currency.quotes.get(USD)

        holder.view.row_parent.setOnClickListener(View.OnClickListener {
            clickListener.onItemClicked(currency)
        })

        when (position % 3) {
            0 -> holder.view.currency_symbol.setBackgroundColor(context.resources.getColor(R.color.inmar_blue))
            1 -> holder.view.currency_symbol.setBackgroundColor(context.resources.getColor(R.color.inmar_green))
            2 -> holder.view.currency_symbol.setBackgroundColor(context.resources.getColor(R.color.inmar_orange))
        }

        holder.view.currency_symbol.text = currency.symbol
        holder.view.name_value.text = currency.name
        holder.view.price_value.text = context.resources.getString(R.string.dollar_amount, quote?.price ?: 0.00)
        holder.view.market_cap_value.text = context.resources.getString(R.string.dollar_amount, quote?.marketCap ?: 0.00)
        holder.view.volume_value.text = context.resources.getString(R.string.dollar_amount, quote?.volume24h ?: 0.00)
    }

    class MainViewHolder(val view : View) : RecyclerView.ViewHolder(view)

    interface ClickListener {
        fun onItemClicked(currency: Currency)
    }
}