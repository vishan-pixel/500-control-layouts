package com.example.data

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class VipManager(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences("vip_preferences", Context.MODE_PRIVATE)

    private val _isVipUnlocked = MutableStateFlow(prefs.getBoolean(KEY_VIP_UNLOCKED, false))
    val isVipUnlocked: StateFlow<Boolean> = _isVipUnlocked.asStateFlow()

    private val _activePlan = MutableStateFlow(prefs.getString(KEY_ACTIVE_PLAN, "NONE") ?: "NONE")
    val activePlan: StateFlow<String> = _activePlan.asStateFlow()

    private val _paymentQrUri = MutableStateFlow<String?>(prefs.getString(KEY_PAYMENT_QR_URI, null))
    val paymentQrUri: StateFlow<String?> = _paymentQrUri.asStateFlow()

    private val _creatorUpi = MutableStateFlow(prefs.getString(KEY_CREATOR_UPI, "notaltino.pvp@upi") ?: "notaltino.pvp@upi")
    val creatorUpi: StateFlow<String> = _creatorUpi.asStateFlow()

    fun unlockVip(plan: String = PLAN_MONTHLY) {
        prefs.edit()
            .putBoolean(KEY_VIP_UNLOCKED, true)
            .putString(KEY_ACTIVE_PLAN, plan)
            .putLong(KEY_UNLOCKED_AT, System.currentTimeMillis())
            .apply()
        _isVipUnlocked.value = true
        _activePlan.value = plan
    }

    fun lockVip() {
        prefs.edit()
            .putBoolean(KEY_VIP_UNLOCKED, false)
            .putString(KEY_ACTIVE_PLAN, "NONE")
            .apply()
        _isVipUnlocked.value = false
        _activePlan.value = "NONE"
    }

    fun savePaymentQr(uri: String?) {
        prefs.edit().putString(KEY_PAYMENT_QR_URI, uri).apply()
        _paymentQrUri.value = uri
    }

    fun saveCreatorUpi(upi: String) {
        prefs.edit().putString(KEY_CREATOR_UPI, upi.trim()).apply()
        _creatorUpi.value = upi.trim()
    }

    companion object {
        const val PLAN_MONTHLY = "MONTHLY_50" // ₹50 / Month
        const val PLAN_YEARLY = "YEARLY_200"  // ₹200 / Year

        const val PRICE_MONTHLY_RUPEES = 50
        const val PRICE_YEARLY_RUPEES = 200

        private const val KEY_VIP_UNLOCKED = "key_vip_unlocked"
        private const val KEY_ACTIVE_PLAN = "key_active_plan"
        private const val KEY_UNLOCKED_AT = "key_unlocked_at"
        private const val KEY_PAYMENT_QR_URI = "key_payment_qr_uri"
        private const val KEY_CREATOR_UPI = "key_creator_upi"

        @Volatile
        private var instance: VipManager? = null

        fun getInstance(context: Context): VipManager {
            return instance ?: synchronized(this) {
                instance ?: VipManager(context.applicationContext).also { instance = it }
            }
        }
    }
}
