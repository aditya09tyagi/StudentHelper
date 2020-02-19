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

    fun setUpcomingCompany(picasso: Picasso,upcoming: Upcoming){
        itemView.tvCompanyName.text = upcoming.company
        itemView.tvCompanyDescription.text = upcoming.description
        itemView.tvJobProfile.text = upcoming.title
        itemView.tvDriveLocation.text = upcoming.place
        itemView.tvVisitDate.text = getStartDateString(upcoming.visit_date)
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
}