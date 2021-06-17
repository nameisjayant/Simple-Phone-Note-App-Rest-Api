package com.codingwithjks.phonenoteapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codingwithjks.phonenoteapp.data.repository.PhoneRepository
import com.codingwithjks.phonenoteapp.data.util.ApiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PhoneViewModel
@Inject
constructor(private val phoneRepository: PhoneRepository) : ViewModel() {

    private val _phoneApiState: MutableStateFlow<ApiState> = MutableStateFlow(ApiState.Empty)
    val phoneApiState: StateFlow<ApiState> = _phoneApiState

    fun getPhone() = viewModelScope.launch {
        phoneRepository.getPhone()
            .onStart {
                _phoneApiState.value = ApiState.Loading
            }.catch { e ->
                _phoneApiState.value = ApiState.Failure(e)
            }.collect { data ->
                _phoneApiState.value = ApiState.Success(data)
            }
    }

    fun setPhone(
        name: String,
        phoneNo: Long
    ) = phoneRepository.setPhone(name, phoneNo)

    fun deletePhone(
        userId:Int
    ) = phoneRepository.deletePhone(userId)

    fun updatePhone(
        userId: Int,
        name: String,
        phoneNo: Long
    ) = phoneRepository.updatePhone(userId,name,phoneNo)

}