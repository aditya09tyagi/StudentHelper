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

    fun setPastCompany(picasso: Picasso, past: Past){
        itemView.tvCompanyName.text = past.companyName
        itemView.tvCompanyDescription.text = past.companyDescription
        itemView.tvJobProfile.text = past.companyTitle
        itemView.tvDriveLocation.text = past.driveLocation
        itemView.tvVisitDate.text = getStartDateString(past.visitDate)
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