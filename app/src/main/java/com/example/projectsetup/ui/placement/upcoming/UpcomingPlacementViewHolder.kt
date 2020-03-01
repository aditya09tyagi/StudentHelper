package com.example.projectsetup.ui.placement.upcoming

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.projectsetup.R
import com.example.projectsetup.data.models.Upcoming
import com.example.projectsetup.util.DateTimeUtils
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.cell_upcoming_placement.view.*
import org.threeten.bp.format.TextStyle
import java.util.*

class UpcomingPlacementViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private lateinit var onUpcomingItemClickListener: OnUpcomingItemClickListener

    fun setUpcomingCompany(picasso: Picasso, upcoming: Upcoming) {
        itemView.tvCompanyName.text = upcoming.company.companyName
        itemView.tvCompanyDescription.text = upcoming.description
        itemView.tvJobProfile.text = upcoming.title
        itemView.tvDriveLocation.text = upcoming.place
        itemView.tvVisitDate.text = getStartDateString(upcoming.visit_date)
        setItemClickListener(itemView,upcoming)
        setRegisterViews(upcoming.isRegistered)

    }

    private fun setItemClickListener(itemView: View,upcoming: Upcoming){
        itemView.setOnClickListener {
            if (::onUpcomingItemClickListener.isInitialized)
                onUpcomingItemClickListener.onItemClickListener(adapterPosition,upcoming.company.id)
        }

        itemView.registerButton.setOnClickListener {
            if (::onUpcomingItemClickListener.isInitialized)
                onUpcomingItemClickListener.onRegisterNowClickedListener(adapterPosition,upcoming.jobId)
        }
    }

    private fun setRegisterViews(isRegistered: Boolean) {
        if (isRegistered) {
            itemView.registerButton.visibility = View.GONE
            itemView.registeredTv.visibility = View.VISIBLE
        } else {
            itemView.registerButton.visibility = View.VISIBLE
            itemView.registeredTv.visibility = View.GONE
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

    fun setOnUpcomingItemClickListener(onUpcomingItemClickListener: OnUpcomingItemClickListener){
        this.onUpcomingItemClickListener = onUpcomingItemClickListener
    }

    interface OnUpcomingItemClickListener {
        fun onRegisterNowClickedListener(position: Int, jobId: String)
        fun onItemClickListener(position: Int,companyId: String)
    }
}