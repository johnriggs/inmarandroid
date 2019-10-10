package com.johnriggsdev.inmarandroid.main

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
import com.johnriggsdev.inmarandroid.details.DetailsFragment
import com.johnriggsdev.inmarandroid.model.Currency
import com.johnriggsdev.inmarandroid.utils.ConnectionLiveData

import kotlinx.android.synthetic.main.activity_main.*

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

        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

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

    private fun setupListeners() {
        retry_button.setOnClickListener(View.OnClickListener {
            viewModel.onRefresh()
        })

        transparent_mask.setOnClickListener(View.OnClickListener {
            dismissDetails()
        })

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

    }

    fun setupRecyclerView(currencies : MutableList<Currency>){
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
        fragmentManager.beginTransaction().remove(detailsFragment!!).commit()
        detailsFragment = null
    }

    override fun onUrlClicked(urlString: String) {
        val webpage: Uri = Uri.parse(urlString)
        val intent = Intent(Intent.ACTION_VIEW, webpage)
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        }
    }

    inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> Unit) {
        val fragmentTransaction = beginTransaction()
        fragmentTransaction.func()
        fragmentTransaction.commit()
    }
}


