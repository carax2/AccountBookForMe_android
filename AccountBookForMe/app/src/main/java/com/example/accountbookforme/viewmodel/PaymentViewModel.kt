package com.example.accountbookforme.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.accountbookforme.model.Filter
import com.example.accountbookforme.repository.PaymentRepository
import com.example.accountbookforme.util.RestUtil
import kotlinx.coroutines.launch

class PaymentViewModel : ViewModel() {

    private val paymentRepository: PaymentRepository =
        RestUtil.retrofit.create(PaymentRepository::class.java)

    // 店舗一覧
    var paymentList: MutableLiveData<List<Filter>> = MutableLiveData()

    init {
        getPaymentList()
    }

    private fun getPaymentList() {

        viewModelScope.launch {
            try {
                val request = paymentRepository.getAll()
                if (request.isSuccessful) {
                    paymentList.value = request.body()
                } else {
                    Log.e("paymentList", "Not successful: $request")
                }
            } catch (e: Exception) {
                Log.e("paymentList", "Something is wrong: $e")
            }
        }
    }

    // IDから名称を取得
    fun getNameById(id: Long): String? {

        val payment = paymentList.value?.find { payment ->
            payment.id == id
        }
        return payment?.name ?: "Invalid payment method"
    }
}