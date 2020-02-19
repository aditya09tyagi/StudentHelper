package com.example.projectsetup.ui.login

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.projectsetup.R
import com.example.projectsetup.data.models.LoginModel

class LoginAdapter : RecyclerView.Adapter<LoginViewHolder>(), LoginViewHolder.OnItemClickListener {

    private var selectedItemId = 0
    private lateinit var onItemClickListener: OnItemClickListener
    private var loginTypeList: ArrayList<LoginModel> = ArrayList()

    fun initLoginTypeList(loginTypeList:ArrayList<LoginModel>){
        this.loginTypeList = loginTypeList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LoginViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.cell_login_card, parent, false)
        return LoginViewHolder(view)
    }

    override fun getItemCount(): Int {
        if (loginTypeList.isNotEmpty())
            return loginTypeList.size
        return 0
    }

    override fun onBindViewHolder(holder: LoginViewHolder, position: Int) {
        if (loginTypeList.isNotEmpty()){
            holder.setItem(loginTypeList[position],selectedItemId)
            holder.setOnItemClickListener(this)
        }
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    interface OnItemClickListener {
        fun onItemClicked(position: Int,loginModel: LoginModel)
    }

    override fun onItemClicked(position: Int, loginModel: LoginModel) {
        this.selectedItemId = loginModel.id
        onItemClickListener.onItemClicked(position,loginModel)
    }
}