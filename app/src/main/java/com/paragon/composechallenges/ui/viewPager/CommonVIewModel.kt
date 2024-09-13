package com.paragon.composechallenges.ui.viewPager

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow

data class User(val name: String, val email: String, val phone: String)

class CommonVIewModel : ViewModel() {
    val user = MutableStateFlow<User?>(User("", "", ""))
    fun setName(name: String) {
        user.value = user.value?.copy(name = name)
    }

    fun setEmail(email: String) {
        user.value = user.value?.copy(email = email)
    }

    fun setPhone(phone: String) {
        user.value = user.value?.copy(phone = phone)
    }
}