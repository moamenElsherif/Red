package com.graduation.red.presentation.tutorial

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.graduation.red.R

class TutorialAdapter(
    fragmentActivity: FragmentActivity,
    private val context: Context
) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int {
        return 4
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> TutorialFragment.newInstance(
                context.resources.getString(R.string.tutorial1),
                R.drawable.tutorial_2
            )

            1 -> TutorialFragment.newInstance(
                context.resources.getString(R.string.tutorial2),
                R.drawable.tutorial_2
            )

            2 -> TutorialFragment.newInstance(
                context.resources.getString(R.string.tutorial3),
                R.drawable.tutorial_2
            )
            else -> {
                TutorialFragment.newInstance(
                    context.resources.getString(R.string.tutorial4),
                    R.drawable.tutorial_2
                )
            }
        }
    }

}