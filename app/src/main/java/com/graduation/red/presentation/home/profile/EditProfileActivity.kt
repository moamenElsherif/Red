package com.graduation.red.presentation.home.profile

import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.graduation.red.R
import com.graduation.red.base.BaseActivity
import com.graduation.red.base.pref.MyPrefs
import com.graduation.red.databinding.ActivityEditProfileBinding
import com.graduation.red.presentation.Constants
import com.graduation.red.presentation.authentication.createaccount.CreateAccountModel
import com.graduation.red.presentation.enums.GenderEnum
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class EditProfileActivity : BaseActivity<ActivityEditProfileBinding>() {
    override val layoutRes: Int
        get() = R.layout.activity_edit_profile

    private val db = Firebase.firestore

    @Inject
    lateinit var myPref: MyPrefs

    override fun initUI(savedInstanceState: Bundle?) {
        binding.btnBack.setOnClickListener {
            finish()
        }
        initData(myPref.getUserDetails())
    }

    private fun isSameData(userDetails: CreateAccountModel): Boolean {
        return (
                userDetails.firstName == binding.tvFirstName.text.trim()
                    .toString() && userDetails.lastName == binding.tvLastName.text.trim().toString()
                        && userDetails.gender == getGender()
                )
    }

    private fun initData(userDetails: CreateAccountModel) {
        binding.item = userDetails
        binding.radioGroup2.check(if (userDetails.gender == GenderEnum.MALE.value) binding.rbMale.id else binding.rbFemale.id)
        binding.btnUpdate.setOnClickListener {
            if (isSameData(userDetails)) return@setOnClickListener
            else updateData(userDetails)
        }
    }

    private fun getGender(): Int {
        val radioId = binding.radioGroup2.checkedRadioButtonId
        return if (radioId == binding.rbMale.id) binding.rbMale.tag.toString().toInt()
        else binding.rbFemale.tag.toString().toInt()
    }

    private fun updateData(userDetails: CreateAccountModel) {
        lifecycleScope.launch {
            showLoading()
            if (userDetails.id == binding.tvPhoneNumber.text.trim().toString()) {

                val document =
                    db.collection(Constants.UsersDocument)
                        .document(myPref.getUserDetails().id)
                val updates = hashMapOf<String, Any>(
                    "firstName" to binding.tvFirstName.text.toString(),
                    "lastName" to binding.tvLastName.text.toString(),
                    "gender" to getGender(),
                )
                document.update(updates).addOnSuccessListener {
                    hideLoading()
                    saveToSharedPref(
                        userDetails.copy(
                            firstName = binding.tvFirstName.text.toString(),
                            lastName = binding.tvLastName.text.toString(),
                            gender = getGender()
                        )
                    )
                    finish()
                }.addOnFailureListener {
                    Toast.makeText(this@EditProfileActivity, "failed", Toast.LENGTH_SHORT).show()
                }
            } else {
                val oldId = userDetails.id
                val phoneNumber = "+20" + binding.tvPhoneNumber.text.trim().toString()
                val newUser = userDetails.copy(
                    id = phoneNumber,
                    firstName = binding.tvFirstName.text.trim().toString(),
                    lastName = binding.tvLastName.text.trim().toString(),
                    gender = getGender()
                )
                db.collection(Constants.UsersDocument).document(phoneNumber)
                    .set(newUser).addOnSuccessListener {
                        hideLoading()
                        deleteOldDocument(oldId)
                        saveToSharedPref(newUser)
                        finish()
                    }.addOnFailureListener {
                        hideLoading()
                    }
            }
        }
    }

    private fun deleteOldDocument(oldId: String) {
        db.collection(Constants.UsersDocument).document(oldId).delete()
    }

    private fun saveToSharedPref(user: CreateAccountModel) {
        myPref.clearUserPref()
        myPref.setUserDetails(user)
    }
}