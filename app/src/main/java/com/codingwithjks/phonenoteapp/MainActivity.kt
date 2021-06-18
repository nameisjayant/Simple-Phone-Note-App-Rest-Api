package com.codingwithjks.phonenoteapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.codingwithjks.phonenoteapp.data.adapter.PhoneAdapter
import com.codingwithjks.phonenoteapp.data.util.ApiState
import com.codingwithjks.phonenoteapp.data.util.Listener
import com.codingwithjks.phonenoteapp.data.util.showMsg
import com.codingwithjks.phonenoteapp.databinding.ActivityMainBinding
import com.codingwithjks.phonenoteapp.databinding.OpenDialogBinding
import com.codingwithjks.phonenoteapp.ui.PhoneViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), Listener {
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private val phoneViewModel: PhoneViewModel by viewModels()
    private lateinit var phoneAdapter: PhoneAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initRecyclerview()
        getPhone()
        setPhone()
    }

    private fun setPhone() {
        binding.apply {
            save.setOnClickListener {
                if (!TextUtils.isEmpty(name.text.toString()) && !TextUtils.isEmpty(phoneNo.text.toString())) {
                    lifecycleScope.launchWhenStarted {
                        phoneViewModel.setPhone(
                            name.text.toString(),
                            phoneNo.text.toString().toLong()
                        ).catch { e ->
                            showMsg("${e.message}")
                        }.collect { data ->
                            Log.d("main", "$data")
                            showMsg("data added successfully..")
                            getPhone()
                        }
                    }
                } else {
                    showMsg("please fill all the fields..")
                }
            }
        }
    }

    private fun getPhone() {
        phoneViewModel.getPhone()
        lifecycleScope.launchWhenStarted {
            binding.apply {
                phoneViewModel.phoneApiState.collect {
                    when (it) {
                        is ApiState.Success -> {
                            recyclerview.isVisible = true
                            progressBar.isVisible = false
                            phoneAdapter.submitList(it.data)
                        }
                        is ApiState.Failure -> {
                            recyclerview.isVisible = false
                            progressBar.isVisible = false
                            Log.d("main", "getPhone: ")
                        }
                        is ApiState.Loading -> {
                            recyclerview.isVisible = false
                            progressBar.isVisible = true
                        }
                        is ApiState.Empty -> {

                        }
                    }
                }
            }
        }
    }

    private fun initRecyclerview() {
        phoneAdapter = PhoneAdapter(this)
        binding.apply {
            recyclerview.apply {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(this@MainActivity)
                adapter = phoneAdapter
            }
        }
    }

    override fun deleteOnClick(position: Int, userId: Int) {
        lifecycleScope.launchWhenStarted {
            phoneViewModel.deletePhone(userId)
                .catch { e ->
                    Log.d("main", "${e.message}")
                }.collect {
                    showMsg("deleted successfully...")
                    getPhone()
                }
        }
    }

    override fun updateOnClick(position: Int, userId: Int, name_1: String, phoneNo_1: Long) {
        val alertDialog = AlertDialog.Builder(this)
        val binding = OpenDialogBinding.inflate(LayoutInflater.from(this))
        val dialog = alertDialog.create()
        dialog.setView(binding.root)
        binding.apply {
            name.setText(name_1)
            phoneNo.setText("$phoneNo_1")
            save.setOnClickListener {
                if (!TextUtils.isEmpty(name.text.toString()) && !TextUtils.isEmpty(phoneNo.text.toString())) {
                    lifecycleScope.launchWhenStarted {
                        phoneViewModel.updatePhone(
                            userId, name.text.toString(),
                            phoneNo.text.toString().toLong()
                        )
                            .catch { e ->
                                Log.d("main", "${e.message}")
                            }.collect {
                                showMsg("update successfully...")
                                getPhone()
                            }
                        dialog.dismiss()
                    }
                } else {
                    showMsg("please fill all the field..")
                }
            }
        }
        dialog.show()
    }
}