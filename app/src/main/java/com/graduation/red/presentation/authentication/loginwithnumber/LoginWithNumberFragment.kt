package com.graduation.red.presentation.authentication.loginwithnumber

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.graduation.red.MainActivity
import com.graduation.red.R
import com.graduation.red.base.BaseFragment
import com.graduation.red.databinding.FragmentLoginWithNumberBinding
import com.graduation.red.presentation.Constants
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class LoginWithNumberFragment : BaseFragment<FragmentLoginWithNumberBinding>(),
    LoginWithNumberListener {
    override val layoutRes: Int
        get() = R.layout.fragment_login_with_number

    private var number: String? = null
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks

    lateinit var auth: FirebaseAuth

    private val db = Firebase.firestore

    lateinit var storedVerificationId: String
    lateinit var resendToken: PhoneAuthProvider.ForceResendingToken

    override fun initUI(savedInstanceState: Bundle?) {
        auth = FirebaseAuth.getInstance()
        handleCallbacks()
        binding.listener = this
    }

    override fun onClickSubmit() {
        checkUserExists()
    }

    private fun checkUserExists() {
        if (binding.tvPhoneNumber.text.trim().isNotEmpty()) {
            lifecycleScope.launch {
                showLoading()
                val collection = db.collection(Constants.UsersDocument)
                collection.whereEqualTo("id", "+20${binding.tvPhoneNumber.text}").get()
                    .addOnSuccessListener { document ->
                        try {
                            hideLoading()
                            if (document != null && document.documents.size > 0) navigateToLoginWithPassword()
                            else sendOtp()
                        } catch (ex: Exception) {
                            Log.e("LoginWithNumber", ex.message.toString())
                        }
                    }.addOnFailureListener {
                        hideLoading()
                        createToast("Error writing document")
                    }
            }
        } else createToast("invalid number")
    }

    private fun navigateToLoginWithPassword() {
        findNavController().navigate(
            LoginWithNumberFragmentDirections.actionLoginWithNumberFragmentToLoginWithPasswordFragment(
                "+20${binding.tvPhoneNumber.text}"
            )
        )
    }

    private fun handleCallbacks() {
        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            // This method is called when the verification is completed
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                startActivity(
                    Intent(
                        this@LoginWithNumberFragment.requireContext(), MainActivity::class.java
                    )
                )
                this@LoginWithNumberFragment.requireActivity().finish()
                Log.d("GFG", "onVerificationCompleted Success")
            }

            // Called when verification is failed add log statement to see the exception
            override fun onVerificationFailed(e: FirebaseException) {
                Log.d("GFG", "onVerificationFailed  $e")
            }

            override fun onCodeSent(
                verificationId: String, token: PhoneAuthProvider.ForceResendingToken
            ) {
                Log.d("GFG", "onCodeSent: $verificationId")
                storedVerificationId = verificationId
                resendToken = token

                findNavController().navigate(
                    LoginWithNumberFragmentDirections.actionLoginWithNumberFragmentToVerifyNumberFragment(
                        number ?: "", storedVerificationId
                    )
                )
            }
        }
    }

    private fun sendOtp() {
        number = binding.tvPhoneNumber.text.trim().toString()

        if (!number.isNullOrEmpty()) {
            number = "+20$number"
            sendVerificationCode(number!!)
        } else {
            Toast.makeText(this.requireContext(), "Enter mobile number", Toast.LENGTH_SHORT).show()
        }
    }

    private fun sendVerificationCode(number: String) {
        val options =
            PhoneAuthOptions.newBuilder(auth).setPhoneNumber(number) // Phone number to verify
                .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                .setActivity(this.requireActivity()) // Activity (for callback binding)
                .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
                .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
        Log.d("GFG", "Auth started")
    }
}