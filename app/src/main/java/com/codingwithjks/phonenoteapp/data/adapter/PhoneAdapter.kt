package com.codingwithjks.phonenoteapp.data.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.codingwithjks.phonenoteapp.data.Phone
import com.codingwithjks.phonenoteapp.data.util.Listener
import com.codingwithjks.phonenoteapp.databinding.EachRowBinding
import javax.inject.Inject

class PhoneAdapter @Inject constructor(private val listener: Listener) : ListAdapter<Phone,PhoneAdapter.PhoneViewHolder>(Diff) {

    inner class PhoneViewHolder(private val binding: EachRowBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(phone: Phone){
            binding.apply {
                name.text = phone.name
                phoneNo.text = phone.phoneNo.toString()
                delete.setOnClickListener {
                    listener.deleteOnClick(adapterPosition,phone.userId)
                }
                root.setOnClickListener {
                    listener.updateOnClick(adapterPosition,phone.userId,phone.name,phone.phoneNo)
                }
            }
        }
    }

    object Diff : DiffUtil.ItemCallback<Phone>(){
        override fun areItemsTheSame(oldItem: Phone, newItem: Phone): Boolean {
            return oldItem.userId == newItem.userId
        }

        override fun areContentsTheSame(oldItem: Phone, newItem: Phone): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhoneViewHolder {
        return PhoneViewHolder(EachRowBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: PhoneViewHolder, position: Int) {
        val phone = getItem(position)
        if(phone != null){
            holder.bind(phone)
        }
    }
}