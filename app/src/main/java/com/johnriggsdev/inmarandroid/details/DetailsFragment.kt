package com.johnriggsdev.inmarandroid.details

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.johnriggsdev.inmarandroid.R
import com.johnriggsdev.inmarandroid.model.Currency
import com.johnriggsdev.inmarandroid.model.CurrencyMetadata
import kotlinx.android.parcel.Parcelize
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_details.*

@Parcelize
class DetailsFragment : Fragment(), Parcelable {

    lateinit var currency: Currency
    var currencyMetadata : CurrencyMetadata? = null

    private lateinit var viewModel : DetailsViewModel
    private lateinit var interactionListener: InteractionListener

    companion object {
        const val WEBSITE = "website"
        const val TECH_DOC = "technical_doc"

        fun newInstance(currency: Currency): DetailsFragment {
            val frag = DetailsFragment()
            frag.currency = currency
            return frag
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        interactionListener = context as InteractionListener
    }

    @Suppress("OverridingDeprecatedMember", "DEPRECATION")
    override fun onAttach(activity: Activity) {
        super.onAttach(activity)

        interactionListener = activity as InteractionListener
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        retainInstance = true
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_details, container, false)
    }

    override fun onStart() {
        super.onStart()

        viewModel = ViewModelProviders.of(this).get(DetailsViewModel::class.java)
        viewModel.initialize(currency)

        observeViewModel()
    }

    override fun onResume() {
        super.onResume()

        setupCurrencyName(currency.name)

        currencyMetadata?.let {
            setupViews(it)
        }

        setupListeners()
    }

    private fun observeViewModel() {
        viewModel.getCurrencyMetadata().observe(this, Observer{ metadata: CurrencyMetadata ->
            this.currencyMetadata = metadata

            this.currencyMetadata?.let{
                setupViews(it)
            }
        })

        viewModel.getIsProgressing().observe(this, Observer { isProgressing ->
            when(isProgressing){
                true -> {
                    metadata_progress.visibility = View.VISIBLE
                    metadata_retry_button.visibility = View.GONE
                }
                else -> metadata_progress.visibility = View.GONE
            }
        })

        viewModel.getMetadataError().observe(this, Observer { errorMessage ->
            if (errorMessage.isNotEmpty()){
                Toast.makeText(context, "Error: $errorMessage", Toast.LENGTH_LONG).show()
                metadata_retry_button.visibility = View.VISIBLE
            }
        })
    }

    private fun setupListeners() {
        metadata_retry_button.setOnClickListener(View.OnClickListener {
            viewModel.onRefresh()
        })

        website_url.setOnClickListener(View.OnClickListener {
            interactionListener.onUrlClicked(website_url.text as String)
        })

        tech_docs_url.setOnClickListener(View.OnClickListener {
            interactionListener.onUrlClicked(tech_docs_url.text as String)
        })
    }

    fun setupCurrencyName(name : String){
        currency_title.text = name
    }

    fun setupViews(metadata: CurrencyMetadata){
        lateinit var website : String
        lateinit var techDocs : String

        try {
            website = metadata.urls.getValue(WEBSITE)[0]
            website_url.isClickable = true
        } catch (ex : IndexOutOfBoundsException){
            website = getString(R.string.no_url_found)
            website_url.isClickable = false
        }

        try {
            techDocs = metadata.urls.getValue(TECH_DOC)[0]
            tech_docs_url.isClickable = true
        } catch (ex : IndexOutOfBoundsException){
            techDocs = getString(R.string.no_url_found)
            tech_docs_url.isClickable = false
        }

        website_url.text = website
        tech_docs_url.text = techDocs
        description_value.text = metadata.description
        Glide.with(context!!).load(metadata.logo).apply(
            RequestOptions()
                .fitCenter().placeholder(R.drawable.inmar_placeholder)).into(currency_logo)

        website_title.visibility = View.VISIBLE
        website_scrollview.visibility = View.VISIBLE
        tech_docs_title.visibility = View.VISIBLE
        tech_docs_scrollview.visibility = View.VISIBLE
        description_title.visibility = View.VISIBLE
        description_value.visibility = View.VISIBLE
    }

    interface InteractionListener {
        fun onUrlClicked(urlString : String)
    }
}