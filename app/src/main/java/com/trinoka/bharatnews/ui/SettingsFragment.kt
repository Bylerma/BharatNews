package com.trinoka.bharatnews.ui

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import com.trinoka.bharatnews.R
import com.trinoka.bharatnews.databinding.FragmentSettingsBinding
import com.trinoka.bharatnews.utils.PreferenceManager
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.json.JSONObject

class SettingsFragment : Fragment(R.layout.fragment_settings), PaymentResultListener {

    private lateinit var binding: FragmentSettingsBinding
    private val PAYMENT_PAGE_URL = "https://rzp.io/rzp/Z70hsu57"
    private lateinit var preferenceManager: PreferenceManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSettingsBinding.bind(view)
        preferenceManager = PreferenceManager(requireContext())

        // Pre-load Razorpay SDK
        Checkout.preload(requireContext())

        setupUI()
        loadPreferences()
    }

    private fun setupUI() {
        binding.switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
            lifecycleScope.launch {
                preferenceManager.setDarkMode(isChecked)
                AppCompatDelegate.setDefaultNightMode(
                    if (isChecked) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
                )
            }
        }

        binding.rgLanguage.setOnCheckedChangeListener { _, checkedId ->
            val languageCode = if (checkedId == R.id.rbHindi) "hi" else "en"
            lifecycleScope.launch {
                preferenceManager.setLanguage(languageCode)
                Toast.makeText(context, "Restart app to apply language change", Toast.LENGTH_SHORT).show()
            }
        }

        binding.sliderTextSize.addOnChangeListener { _, value, _ ->
            lifecycleScope.launch {
                preferenceManager.setTextSize(value)
            }
        }

        binding.switchNotifications.setOnCheckedChangeListener { _, isChecked ->
            lifecycleScope.launch {
                preferenceManager.setNotifications(isChecked)
            }
        }

        binding.btnSubscribe.setOnClickListener {
            openPaymentPage(PAYMENT_PAGE_URL)
        }

        binding.btnClearCache.setOnClickListener {
            Toast.makeText(context, "Cache cleared successfully", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadPreferences() {
        lifecycleScope.launch {
            binding.switchDarkMode.isChecked = preferenceManager.darkModeFlow.first()
            val language = preferenceManager.languageFlow.first()
            if (language == "hi") binding.rbHindi.isChecked = true else binding.rbEnglish.isChecked = true
            binding.sliderTextSize.value = preferenceManager.textSizeFlow.first()
            binding.switchNotifications.isChecked = preferenceManager.notificationsFlow.first()
        }
    }

    private fun openPaymentPage(url: String) {
        val colorPrimary = ContextCompat.getColor(requireContext(), R.color.primary)
        val defaultColors = CustomTabColorSchemeParams.Builder()
            .setToolbarColor(colorPrimary)
            .build()

        val customTabsIntent = CustomTabsIntent.Builder()
            .setShowTitle(true)
            .setDefaultColorSchemeParams(defaultColors)
            .build()
        
        try {
            customTabsIntent.launchUrl(requireContext(), Uri.parse(url))
        } catch (e: Exception) {
            Toast.makeText(context, "Could not open payment page", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onPaymentSuccess(razorpayPaymentId: String?) {
        Toast.makeText(context, "Payment Successful: $razorpayPaymentId", Toast.LENGTH_LONG).show()
    }

    override fun onPaymentError(code: Int, response: String?) {
        Toast.makeText(context, "Payment Failed: $response", Toast.LENGTH_LONG).show()
    }
}