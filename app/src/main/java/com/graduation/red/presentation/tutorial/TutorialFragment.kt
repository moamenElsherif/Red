package com.graduation.red.presentation.tutorial

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.graduation.red.R
import com.graduation.red.base.BaseFragment
import com.graduation.red.databinding.FragmentTutorialBinding

class TutorialFragment : BaseFragment<FragmentTutorialBinding>() {
    override val layoutRes: Int
        get() = R.layout.fragment_tutorial

    private lateinit var title: String
    private var imageResource = 0

    override fun initUI(savedInstanceState: Bundle?) {
        if (arguments != null) {
            title = requireArguments().getString(PARAM_1)!!
            imageResource = requireArguments().getInt(PARAM_2)
        }
        binding.textHeader.text = title
        binding.imageHeader.setImageResource(imageResource)
    }

    companion object {

        private const val PARAM_1 = "param1"
        private const val PARAM_2 = "param2"

        fun newInstance(
            title: String,
            imageResource: Int
        ) =
            TutorialFragment().apply {
                val bundle = Bundle()
                bundle.putString(PARAM_1 , title)
                bundle.putInt(PARAM_2 , imageResource)
                this.arguments = bundle
                return this
            }

        }
    }