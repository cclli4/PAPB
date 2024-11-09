package com.example.composeview2.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class TugasViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TugasViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TugasViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
