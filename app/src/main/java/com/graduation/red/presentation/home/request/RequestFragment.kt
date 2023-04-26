package com.graduation.red.presentation.home.request

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.graduation.red.R
import com.graduation.red.base.BaseFragment
import com.graduation.red.base.pref.MyPrefs
import com.graduation.red.databinding.FragmentRequestBinding
import com.graduation.red.presentation.Constants
import com.graduation.red.presentation.authentication.createaccount.CreateAccountModel
import com.graduation.red.presentation.enums.ForWhoEnum
import com.graduation.red.presentation.enums.GenderEnum
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class RequestFragment : BaseFragment<FragmentRequestBinding>(), AdapterView.OnItemSelectedListener {
    override val layoutRes: Int
        get() = R.layout.fragment_request

    @Inject
    lateinit var myPrefs: MyPrefs
    private lateinit var userModel: CreateAccountModel

    private val forMeLiveData = MutableLiveData<Boolean>()
    private val db = Firebase.firestore

    private val requestsModel = RequestsModel()

    override fun initUI(savedInstanceState: Bundle?) {
        userModel = myPrefs.getUserDetails()
        observeRequestModel()
        handleCreateRequestClick()
        observeForWho()
        observeSpinner()
        handleBloodTypesSpinner()
        binding.bloodTypeSpinner.setSelection(0)
        binding.rbMe.isChecked = true
    }

    private fun checkDataValid(): Boolean {
        if (forMeLiveData.value == false && binding.tvName.text.isEmpty()) {
            createToast("invalid name")
            return false
        }
        if (forMeLiveData.value == false && binding.tvLastName.text.isEmpty()) {
            createToast("invalid name")
            return false
        }
        if (binding.etDay.text.toString().isEmpty() || binding.etDay.text.toString().length != 2) {
            createToast("invalid date1")
            return false
        }
        if (binding.etMonth.text.toString()
                .isEmpty() || binding.etMonth.text.toString().length != 2
        ) {
            createToast("invalid date2")
            return false
        }
        if (binding.etYear.text.toString()
                .isEmpty() || binding.etYear.text.toString().length != 4
        ) {
            createToast("invalid date3")
            return false
        }
        if (binding.etPhoneNumber.text.isEmpty()) {
            createToast("invalid number")
            return false
        }
        if (binding.etDonationAddress.text.isEmpty()) {
            createToast("invalid address")
            return false
        }
        return true
    }

    private fun handleCreateRequestClick() {
        binding.btnCreateRequest.setOnClickListener {
            if (!checkDataValid()) return@setOnClickListener
            saveData(readyRequestModel())
        }
    }

    private fun readyRequestModel(): RequestsModel {
        return requestsModel.copy(
            userId = userModel.id,
            firstName = getFirstName(),
            lastName = getLastName(),
            date = getDate(),
            messageToDonor = binding.etMessage.text.toString(),
            verifiedPhoneNumber = binding.etPhoneNumber.text.toString(),
            donationAddress = binding.etDonationAddress.text.toString(),
            gender = getGender(),
            verifiedDonorsId = listOf("moamen" , "elserif")
        )
    }

    private fun getDate(): String {
        return "${binding.etDay.text}/${binding.etMonth.text}/${binding.etYear.text}"
    }

    private fun getLastName(): String {
        return if (forMeLiveData.value == true) userModel.lastName
        else binding.tvLastName.text.toString()
    }

    private fun getFirstName(): String {
        return if (forMeLiveData.value == true) userModel.firstName
        else binding.tvName.text.toString()
    }

    private fun observeForWho() {
        binding.radioGroup1.setOnCheckedChangeListener { group, checkedId -> // checkedId is the RadioButton selected
            forMeLiveData.value = checkedId == binding.rbMe.id
        }
    }

    private fun getGender(): Int {
        return if (forMeLiveData.value == false) {
            val radioId = binding.radioGroup2.checkedRadioButtonId
            if (radioId == binding.rbMale.id) GenderEnum.MALE.value
            else GenderEnum.FEMALE.value
        } else userModel.gender
    }

    private fun saveData(requestsModel: RequestsModel) {
        viewLifecycleOwner.lifecycleScope.launch {
            showLoading()
            db.collection(Constants.RequestDocument).document()
                .set(requestsModel).addOnSuccessListener {
                    hideLoading()
                    createToast("Request Successfully created")
                }.addOnFailureListener {
                    hideLoading()
                    createToast("failed -> $it")
                }
        }
    }

    private fun observeRequestModel() {
        forMeLiveData.observe(viewLifecycleOwner) {
            handleUi(it)
        }
    }

    private fun handleUi(it: Boolean?) {
        if (it == true) {
            binding.nameView.animate().alpha(0.0f).translationY(0F).duration = 500
            binding.nameView.visibility = View.GONE
            binding.bloodTypeView.visibility = View.GONE
            binding.bloodTypeView.animate().alpha(0.0f).translationY(0F).duration = 500
            binding.genderView.visibility = View.GONE
            binding.genderView.animate().alpha(0.0f).translationY(0F).duration = 500
            requestsModel.forWho = ForWhoEnum.FOR_ME.value
        } else {
            binding.nameView.animate().alpha(1.0f).translationY(0F).duration = 500
            binding.nameView.visibility = View.VISIBLE
            binding.bloodTypeView.visibility = View.VISIBLE
            binding.bloodTypeView.animate().alpha(1.0f).translationY(0F).duration = 500
            binding.genderView.visibility = View.VISIBLE
            binding.genderView.animate().alpha(1.0f).translationY(0F).duration = 500
            requestsModel.forWho = ForWhoEnum.FOR_OTHER.value
        }
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
        requestsModel.requestedBloodType = p0?.getItemAtPosition(p2).toString()
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {

    }

}