package com.johnriggsdev.inmarandroid.main

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import android.widget.Toast.LENGTH_SHORT
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.johnriggsdev.inmarandroid.R
import com.johnriggsdev.inmarandroid.app.InmarApp
import com.johnriggsdev.inmarandroid.details.DetailsFragment
import com.johnriggsdev.inmarandroid.model.Currency
import com.johnriggsdev.inmarandroid.utils.ConnectionLiveData
import com.johnriggsdev.inmarandroid.utils.Constants.Companion.APP_TAG
import com.johnriggsdev.inmarandroid.utils.Constants.Companion.SORT_24H
import com.johnriggsdev.inmarandroid.utils.Constants.Companion.SORT_DATE
import com.johnriggsdev.inmarandroid.utils.Constants.Companion.SORT_MKC
import com.johnriggsdev.inmarandroid.utils.Constants.Companion.SORT_NAME
import com.johnriggsdev.inmarandroid.utils.Constants.Companion.SORT_PRICE
import com.johnriggsdev.inmarandroid.utils.PreferencesHelper

import kotlinx.android.synthetic.main.activity_main.*
import java.lang.NullPointerException

class MainActivity : AppCompatActivity(), MainAdapter.ClickListener, DetailsFragment.InteractionListener {

    companion object {
        const val FRAGMENT = "fragment"
    }

    private val currencyList = mutableListOf<Currency>()
    private lateinit var viewModel : MainViewModel

    private var detailsFragment : DetailsFragment? = null
    private val fragmentManager = supportFragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(main_toolbar)
        title = ""

        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        viewModel.setPrefs(PreferencesHelper.defaultPrefs(InmarApp.applicationContext()))

        observeViewModel()
        setupListeners()
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        detailsFragment = savedInstanceState.getParcelable(FRAGMENT)
    }

    override fun onResume() {
        super.onResume()

        if (detailsFragment != null){
            transparent_mask.visibility = View.VISIBLE
            details_frame.visibility = View.VISIBLE
        } else {
            transparent_mask.visibility = View.GONE
            details_frame.visibility = View.GONE
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        when(id){
            R.id.sort_market -> {
                viewModel.setSortCategory(SORT_MKC)
                dismissDetails()
            }
            R.id.sort_price -> {
                viewModel.setSortCategory(SORT_PRICE)
                dismissDetails()
            }
            R.id.sort_name -> {
                viewModel.setSortCategory(SORT_NAME)
                dismissDetails()
            }
            R.id.sort_date -> {
                viewModel.setSortCategory(SORT_DATE)
                dismissDetails()
            }
            R.id.sort_24h -> {
                viewModel.setSortCategory(SORT_24H)
                dismissDetails()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupListeners() {
        retry_button.setOnClickListener {
            viewModel.onRefresh()
        }

        transparent_mask.setOnClickListener {
            dismissDetails()
        }

        val connectionLiveData = ConnectionLiveData(this)
        connectionLiveData.observe(this, Observer {
            viewModel.updateConnection(it)
            if (!it) {
                Toast.makeText(this, "No Internet Connection Detected!", LENGTH_SHORT).show()
            }
        })
    }

    private fun observeViewModel() {
        viewModel.getCurrencies().observe(this, Observer{ currencies: Array<Currency> ->
            if (currencies.isNotEmpty()) {
                currencyList.clear()
                currencyList.addAll(currencies)
                setupRecyclerView(currencyList)
            } else {
                Toast.makeText(this, getString(R.string.no_currencies), LENGTH_LONG).show()
                retry_button.visibility = View.VISIBLE
                main_recycler.visibility = View.GONE
            }
        })

        viewModel.getIsProgressing().observe(this, Observer { isProgressing ->
            when(isProgressing){
                true -> {
                    main_progress.visibility = View.VISIBLE
                    retry_button.visibility = View.GONE
                }
                else -> main_progress.visibility = View.GONE
            }
        })

        viewModel.getCurrencyError().observe(this, Observer { errorMessage ->
            if (errorMessage.isNotEmpty()){
                Toast.makeText(this, "Error: $errorMessage", LENGTH_LONG).show()
                retry_button.visibility = View.VISIBLE
            }
        })

        viewModel.getSortType().observe(this, Observer { sortType ->
            var type = ""

            when(sortType){
                SORT_MKC -> type = getString(R.string.menu_market_cap)
                SORT_PRICE -> type = getString(R.string.menu_price)
                SORT_NAME -> type = getString(R.string.menu_name)
                SORT_DATE -> type = getString(R.string.menu_date_added)
                SORT_24H -> type = getString(R.string.menu_24h_volume)
            }

            toolbar_title.text = getString(R.string.top_50_currencies, type)
        })
    }

    private fun setupRecyclerView(currencies : MutableList<Currency>){
        val recycler = main_recycler
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = MainAdapter(currencies, this, this)
        recycler.visibility = View.VISIBLE
    }

    override fun onItemClicked(currency: Currency) {
        transparent_mask.visibility = View.VISIBLE
        details_frame.visibility = View.VISIBLE

        detailsFragment = DetailsFragment.newInstance(currency)
        fragmentManager.inTransaction {
                add(R.id.details_frame, detailsFragment!!)
            }
    }

    override fun onBackPressed() {
        if (detailsFragment != null){
            dismissDetails()
        } else {
            super.onBackPressed()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        detailsFragment?.let{
            outState.putParcelable(FRAGMENT, detailsFragment)
        }
    }

    private fun dismissDetails() {
        transparent_mask.visibility = View.GONE
        details_frame.visibility = View.GONE
        try {
            fragmentManager.beginTransaction().remove(detailsFragment!!).commit()
        } catch (ex: NullPointerException){
            Log.d(APP_TAG, "There was no fragment to dismiss")
        }
        detailsFragment = null
    }

    override fun onUrlClicked(urlString: String) {
        val webpage: Uri = Uri.parse(urlString)
        val intent = Intent(Intent.ACTION_VIEW, webpage)
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        }
    }

    private inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> Unit) {
        val fragmentTransaction = beginTransaction()
        fragmentTransaction.func()
        fragmentTransaction.commit()
    }
}


