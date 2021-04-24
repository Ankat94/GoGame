package com.example.gogame

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gogame.databinding.HeaderBinding
import com.example.gogame.model.User
import com.example.shaadi.room.UserRepository
import kotlinx.coroutines.handleCoroutineException

class HeaderAdapter(var header: Int, val context: Context): RecyclerView.Adapter<HeaderAdapter.HeaderHolder>() {

    var users = ArrayList<User>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeaderHolder {
        return HeaderHolder(
                DataBindingUtil.inflate(LayoutInflater.from(parent.context),
                        R.layout.header, parent, false))
    }

    override fun onBindViewHolder(holder: HeaderHolder, position: Int) {
        if (header > 1) {
            holder.headerBinding.textView2.visibility = View.VISIBLE
            if (position == 0) {
                holder.headerBinding.textView2.setText("Favourite")
                users = UserRepository(context).getFavorites() as ArrayList<User>
            }
            else {
                holder.headerBinding.textView2.setText("All Conatacts")
                users = UserRepository(context).getAllUsers() as ArrayList<User>
            }
        }
        else {
            holder.headerBinding.textView2.visibility = View.GONE
            users = UserRepository(context).getAllUsers() as ArrayList<User>
        }

        holder.headerBinding.recycle.layoutManager = LinearLayoutManager(context)
        val adapter = UserAdapter(users,context)
        holder.headerBinding.recycle.adapter = adapter
    }

    override fun getItemCount(): Int = header

    class HeaderHolder(val headerBinding: HeaderBinding): RecyclerView.ViewHolder(headerBinding.root)
}