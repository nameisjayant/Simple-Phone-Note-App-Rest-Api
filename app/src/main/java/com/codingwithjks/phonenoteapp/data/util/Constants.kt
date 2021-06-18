package com.codingwithjks.phonenoteapp.data.util

import android.content.Context
import android.widget.Toast
import retrofit2.Response

fun Context.showMsg(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}