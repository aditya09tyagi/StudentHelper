package com.example.projectsetup.ui.placement.past

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.projectsetup.R
import com.example.projectsetup.data.models.Past
import com.example.projectsetup.util.DateTimeUtils
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.cell_past_placement.view.*
import org.threeten.bp.format.TextStyle
import java.util.*

class PastPlacementViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private lateinit var onItemClickListener: OnItemClickListener

    fun setPastCompany(picasso: Picasso, past: Past) {
        itemView.tvCompanyName.text = past.company.companyName
        itemView.tvCompanyDescription.text = past.companyDescription
        itemView.tvJobProfile.text = past.companyTitle
        itemView.tvDriveLocation.text = past.driveLocation
        itemView.tvVisitDate.text = getStartDateString(past.visitDate)

        itemView.setOnClickListener {
            if(::onItemClickListener.isInitialized)
                onItemClickListener.onItemClick(adapterPosition,past.company.id)
        }
    }

    private fun getStartDateString(startDate: String): String {
        val date = DateTimeUtils.getLocalDateFromString(startDate)
        val time = DateTimeUtils.getDateInTimeOnlyFormatFromUtcDateTime(startDate)
        return String.format(
            itemView.context.getString(R.string.class_date_format),
            time,
            date.dayOfMonth,
            date.month.getDisplayName(TextStyle.SHORT, Locale.UK)
        )
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener){
        this.onItemClickListener = onItemClickListener
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int, companyId: String)
    }
}