package com.graduation.red.presentation.authentication.createaccount

import android.os.Bundle
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.navigation.fragment.navArgs
import com.graduation.red.R
import com.graduation.red.base.BaseFragment
import com.graduation.red.databinding.FragmentCreateAccountBinding


class CreateAccountFragment : BaseFragment<FragmentCreateAccountBinding>(), OnItemSelectedListener {
    override val layoutRes: Int
        get() = R.layout.fragment_create_account

    private val args: CreateAccountFragmentArgs by navArgs()

    private val user = CreateAccountModel()

    override fun initUI(savedInstanceState: Bundle?) {
        observeSpinner()
        binding.bloodTypeSpinner.setSelection(0)
        handleBloodTypesSpinner()
        onClickRegister()
    }

    private fun onClickRegister() {
        binding.btnRegister.setOnClickListener {
            if (checkDataValid()){
                addDataToModel()
            }
        }
    }

    private fun addDataToModel() {
        user.id = args.phoneNumber
        user.firstName = binding.tvName.text.toString()
        user.lastName = binding.tvLastName.text.toString()
        user.password = binding.tvPassword.text.toString()
        user.gender = getGender()
    }

    private fun getGender(): Int {
        val radioId = binding.radioGroup2.checkedRadioButtonId
        return if (radioId == binding.rbMale.id) binding.rbMale.tag.toString().toInt()
        else binding.rbFemale.tag.toString().toInt()
    }

    private fun checkDataValid(): Boolean {
        if (binding.tvName.text.isNullOrEmpty()) {
            createToast("invalid name")
            return false
        }
        if (binding.tvLastName.text.isNullOrEmpty()) {
            createToast("invalid name")
            return false
        }
        if (binding.tvPassword.text.isNullOrEmpty()) {
            createToast("invalid password")
            return false
        }
        if (binding.tvConfirmPassword.text.isNullOrEmpty()) {
            createToast("invalid password")
            return false
        }
        if (binding.tvPassword.text != binding.tvConfirmPassword.text) {
            createToast("passwords aren't matching")
            return false
        }
        if (binding.tvNationalCode.text.isNullOrEmpty() || binding.tvNationalCode.text.count() != 11) {
            createToast("invalid national Id")
            return false
        }
        return true
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
        user.bloodType = p0?.getItemAtPosition(p2).toString()
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
    }

}