package com.example.gogame

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.gogame.databinding.UserRowBinding
import com.example.gogame.model.User
import com.example.gogame.utils.AdapterListener

class UserAdapter(var users : List<User>, context: Context): RecyclerView.Adapter<UserAdapter.UserHolder>() {

    val adapterListener : AdapterListener = context as AdapterListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserHolder {
        return UserHolder(
            DataBindingUtil.inflate(LayoutInflater.from(parent.context),
            R.layout.user_row,parent
            ,false))
    }

    override fun onBindViewHolder(holder: UserHolder, position: Int) {
        holder.userRowBinding.user = users.get(position)
        holder.userRowBinding.root.setOnClickListener {
            adapterListener.onStatusChanged(users.get(position).id, position)
        }
    }

    override fun getItemCount(): Int = users.size

    class UserHolder( val userRowBinding: UserRowBinding): RecyclerView.ViewHolder(userRowBinding.root)
}