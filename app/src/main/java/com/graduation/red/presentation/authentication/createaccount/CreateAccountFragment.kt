package com.graduation.red.presentation.authentication.createaccount

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import com.graduation.red.R
import com.graduation.red.base.BaseFragment
import com.graduation.red.databinding.FragmentCreateAccountBinding

class CreateAccountFragment : BaseFragment<FragmentCreateAccountBinding>(), AdapterView.OnItemSelectedListener {
    override val layoutRes: Int
        get() = R.layout.fragment_create_account

    override fun initUI(savedInstanceState: Bundle?) {
        observeSpinner()
        binding.bloodTypeSpinner.setSelection(0)
        handleBloodTypesSpinner()
    }

    private fun observeSpinner() {
        binding.bloodTypeSpinner.onItemSelectedListener = this
    }

    private fun handleBloodTypesSpinner() {
        val spinner: Spinner = binding.bloodTypeSpinner
        ArrayAdapter.createFromResource(
            this.requireContext(),
            R.array.blood_types,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        Toast.makeText(this.requireContext(), p0?.getItemAtPosition(p2).toString(), Toast.LENGTH_SHORT).show()
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {

    }

}