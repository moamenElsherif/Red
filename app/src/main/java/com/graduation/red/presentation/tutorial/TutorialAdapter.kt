package com.graduation.red.presentation.tutorial

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class TutorialAdapter(
    fragmentActivity: FragmentActivity,
    private val context: Context
) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int {
        return 5
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> TutorialFragment.newInstance(
                context.resources.getString(R.string.have_astore_need_adelivery),
                R.drawable.tutorial_screen1
            )

            1 -> TutorialFragment.newInstance(
                context.resources.getString(R.string.receive_orders_and_track_it),
                R.drawable.tutorial_screen2
            )

            2 -> TutorialFragment.newInstance(
                context.resources.getString(R.string.chat_with_customer_and_courier_by_chat),
                R.drawable.tutorial_screen3
            )

            3 -> TutorialFragment.newInstance(
                context.resources.getString(R.string.customer_service_is_available_seven_days_aweek_to_serve_you),
                R.drawable.tutorial_screen4
            )

            else -> {
                TutorialFragment.newInstance(
                    context.resources.getString(R.string.generate_daily_and_weekly_and_custom_reports),
                    R.drawable.tutorial_screen5
                )
            }
        }
    }

}