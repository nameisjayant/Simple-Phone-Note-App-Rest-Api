package com.codingwithjks.phonenoteapp.data.util

import com.codingwithjks.phonenoteapp.data.Phone

sealed class ApiState{
    class Success(val data:List<Phone>) : ApiState()
    class Failure(val msg:Throwable) : ApiState()
    object Loading : ApiState()
    object Empty : ApiState()
}
