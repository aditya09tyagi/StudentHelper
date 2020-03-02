package com.example.projectsetup.ui.generic_rv

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.projectsetup.R

class GenericDataAdapter:RecyclerView.Adapter<GenericDataViewHolder>(),
    GenericDataViewHolder.OnItemDeletedListener {

    private lateinit var onItemDeletedListener: OnItemDeletedListener
    private var genericDataList:ArrayList<String> = ArrayList()

    fun updateGenericDataList(genericData:String){
        genericDataList.add(genericData)
        notifyDataSetChanged()
    }

    fun onRemoveItem(position: Int){
        genericDataList.removeAt(position)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenericDataViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.cell_generic,parent,false)
        return GenericDataViewHolder(
            view
        )
    }

    override fun getItemCount(): Int {
        if (genericDataList.isNotEmpty())
            return genericDataList.size
        return 0
    }

    override fun onBindViewHolder(holder: GenericDataViewHolder, position: Int) {
        if (genericDataList.isNotEmpty()){
            holder.setGenericData(genericDataList[position])
            holder.setOnItemDeletedListener(this)
        }
    }

    fun setOnItemDeletedListener(onItemDeletedListener: OnItemDeletedListener){
        this.onItemDeletedListener = onItemDeletedListener
    }

    interface OnItemDeletedListener {
        fun onItemDeleted(position: Int)
    }

    override fun onItemDeleted(position: Int) {
        if(::onItemDeletedListener.isInitialized){
            onItemDeletedListener.onItemDeleted(position)
        }
    }
}