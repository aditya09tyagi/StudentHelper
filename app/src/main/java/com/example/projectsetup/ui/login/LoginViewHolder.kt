package com.example.projectsetup.ui.login

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.projectsetup.R
import com.example.projectsetup.data.models.LoginModel
import kotlinx.android.synthetic.main.activity_login.view.tvLoginType
import kotlinx.android.synthetic.main.cell_login_card.view.*

class LoginViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private lateinit var onItemClickListener: OnItemClickListener

    fun setItem(loginModel: LoginModel, selectedItemId: Int) {
        itemView.ivLoginType.background = loginModel.parentBackgroundResource
        itemView.tvLoginType.text = loginModel.loginTypeText

        itemView.setOnClickListener {
            if (::onItemClickListener.isInitialized)
                onItemClickListener.onItemClicked(adapterPosition, loginModel)
        }

        if (loginModel.id == selectedItemId){
            itemView.clDataContainer.background = itemView.context.getDrawable(R.drawable.bg_login_card_selected)
            itemView.tvLoginType.setCompoundDrawablesWithIntrinsicBounds(null,null,itemView.context.getDrawable(R.drawable.ic_filled_tick),null)
        }else{
            itemView.clDataContainer.background = itemView.context.getDrawable(R.drawable.bg_login_card_unselected)
            itemView.tvLoginType.setCompoundDrawablesWithIntrinsicBounds(null,null,null,null)
        }
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    interface OnItemClickListener {
        fun onItemClicked(position: Int, loginModel: LoginModel)
    }
}