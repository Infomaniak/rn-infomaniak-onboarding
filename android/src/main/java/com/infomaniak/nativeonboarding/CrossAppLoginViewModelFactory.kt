package com.infomaniak.nativeonboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class CrossAppLoginViewModelFactory(
    private val applicationId: String,
    private val clientId: String,
) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = CrossAppLoginViewModel(applicationId, clientId) as T
}
