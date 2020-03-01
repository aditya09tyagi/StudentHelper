package com.example.projectsetup.ui.placement.live

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.projectsetup.R
import com.example.projectsetup.data.models.Live
import com.example.projectsetup.util.DateTimeUtils
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.cell_live_companies.view.*
import org.threeten.bp.format.TextStyle
import java.util.*

class LiveCompanyViewHolder (itemView: View):RecyclerView.ViewHolder(itemView){
    fun setLiveCompany(picasso: Picasso, live: Live) {
        itemView.tvCompanyName.text = live.company.companyName
        itemView.tvCompanyDescription.text = live.companyDescription
        itemView.tvJobProfile.text = live.companyTitle
        itemView.tvDriveLocation.text = live.driveLocation
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