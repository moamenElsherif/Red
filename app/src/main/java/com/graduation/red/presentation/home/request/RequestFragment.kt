package com.graduation.red.presentation.home.request

import android.app.Activity
import android.content.Intent
import android.location.Geocoder
import android.net.Uri
import android.os.Bundle
import android.telecom.TelecomManager.EXTRA_LOCATION
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.graduation.red.R
import com.graduation.red.base.BaseFragment
import com.graduation.red.base.pref.MyPrefs
import com.graduation.red.databinding.FragmentRequestBinding
import com.graduation.red.presentation.Constants
import com.graduation.red.presentation.Constants.Companion.LOCATION_LAT_LNG
import com.graduation.red.presentation.authentication.createaccount.CreateAccountModel
import com.graduation.red.presentation.enums.ForWhoEnum
import com.graduation.red.presentation.enums.GenderEnum
import com.graduation.red.presentation.map.MapActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.*
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
        binding.openMaps.setOnClickListener {
            val intent = Intent(this.requireContext() , MapActivity::class.java)
            startActivityForResult(intent, LOCATION_LAT_LNG)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == LOCATION_LAT_LNG) {
            if (resultCode == Activity.RESULT_OK) {
                try {
                    val result = data?.getStringExtra("result")
                    val selectedLatLng = Gson().fromJson(result, LatLng::class.java)
                    updateUiWithSelectedLocation(selectedLatLng)
                } catch (ex: Exception) {
                    LatLng(0.0 , 0.0)
                }
            } else {
                // Handle the case where the user canceled the operation
            }
        }
    }

    private fun updateUiWithSelectedLocation(selectedLatLng: LatLng?) {
        requestsModel.locationLng = selectedLatLng?.longitude
        requestsModel.locationLat = selectedLatLng?.latitude
        val geocoder = Geocoder(this.requireContext(), Locale.getDefault())
        val addresses = geocoder.getFromLocation(
            selectedLatLng?.latitude!!,
            selectedLatLng.longitude, 1)
        if (addresses?.isNotEmpty() == true) {
            val address = addresses[0]
            val addressName = address.getAddressLine(0)
            requestsModel.locationAddress = addressName
            binding.getLocationOnMap.text = addressName
        }
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
            createToast("invalid date")
            return false
        }
        if (binding.etMonth.text.toString()
                .isEmpty() || binding.etMonth.text.toString().length != 2
        ) {
            createToast("invalid date")
            return false
        }
        if (binding.etYear.text.toString()
                .isEmpty() || binding.etYear.text.toString().length != 4
        ) {
            createToast("invalid date")
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
        if (requestsModel.locationLat == null){
            createToast("select location on map")
            return false
        }
        if (requestsModel.locationLng == null){
            createToast("select location on map")
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
            requestId = generateRandomId(8),
            userId = userModel.id,
            firstName = getFirstName(),
            lastName = getLastName(),
            date = getDate(),
            messageToDonor = binding.etMessage.text.toString(),
            verifiedPhoneNumber = binding.etPhoneNumber.text.toString(),
            donationAddress = binding.etDonationAddress.text.toString(),
            gender = getGender(),
            verifiedDonorsId = listOf()
        )
    }

    fun generateRandomId(length: Int): String {
        val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        val random = Random()
        return (1..length)
            .map { random.nextInt(charPool.size) }
            .map(charPool::get)
            .joinToString("")
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
            db.collection(Constants.RequestDocument).document(requestsModel.requestId)
                .set(requestsModel).addOnSuccessListener {
                    hideLoading()
                    createToast("Request Successfully created")
                    Log.e("generatedId" , requestsModel.requestId)
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