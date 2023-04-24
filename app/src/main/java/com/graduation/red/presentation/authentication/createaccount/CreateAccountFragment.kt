package com.graduation.red.presentation.authentication.createaccount

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.graduation.red.R
import com.graduation.red.base.BaseFragment
import com.graduation.red.databinding.FragmentCreateAccountBinding
import kotlinx.coroutines.launch


class CreateAccountFragment : BaseFragment<FragmentCreateAccountBinding>(), OnItemSelectedListener {

    override val layoutRes: Int
        get() = R.layout.fragment_create_account

    private val args: CreateAccountFragmentArgs by navArgs()
    private var bloodType = ""
    private val db = Firebase.firestore

    override fun initUI(savedInstanceState: Bundle?) {
        observeSpinner()
        handleBloodTypesSpinner()
        binding.bloodTypeSpinner.setSelection(0)
        onClickRegister()
    }

    private fun onClickRegister() {
        binding.btnRegister.setOnClickListener {
            if (checkDataValid()) {
                val user = CreateAccountModel(
                    id = args.phoneNumber,
                    firstName = binding.tvName.text.toString(),
                    lastName = binding.tvLastName.text.toString(),
                    password = binding.tvPassword.text.toString(),
                    gender = getGender(),
                    bloodType = bloodType
                )
                saveData(user)
            }
        }
    }

    private fun saveData(user: CreateAccountModel) {
        viewLifecycleOwner.lifecycleScope.launch {
            showLoading()
            db.collection("Users").document(args.phoneNumber)
                .set(user).addOnSuccessListener {
                    hideLoading()
                    createToast("DocumentSnapshot added successfully")
                }.addOnFailureListener {
                    hideLoading()
                    createToast("failed -> $it")
                }
        }
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
        bloodType = p0?.getItemAtPosition(p2).toString()
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
    }

}