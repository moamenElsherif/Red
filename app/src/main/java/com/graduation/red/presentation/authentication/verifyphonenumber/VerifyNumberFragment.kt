package com.graduation.red.presentation.authentication.verifyphonenumber

import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.graduation.red.R
import com.graduation.red.base.BaseFragment
import com.graduation.red.databinding.FragmentVerifyNumberBinding
import com.graduation.red.presentation.authentication.loginwithnumber.LoginWithNumberFragmentDirections
import kotlinx.coroutines.launch

class VerifyNumberFragment : BaseFragment<FragmentVerifyNumberBinding>() {
    override val layoutRes: Int
        get() = R.layout.fragment_verify_number

    val TAG = "VerifyNumberFragment"
    lateinit var auth: FirebaseAuth

    private val args: VerifyNumberFragmentArgs by navArgs()

    override fun initUI(savedInstanceState: Bundle?) {
        auth = FirebaseAuth.getInstance()
        binding.btnSubmit.setOnClickListener {
            handleSentCode()
        }
    }

    private fun handleSentCode() {
        val code = binding.etConfirmCode.text.trim()
        if (code.isNotEmpty()) {
            val credential: PhoneAuthCredential = PhoneAuthProvider.getCredential(
                args.otp, code.toString()
            )
            signInWithPhoneAuthCredential(credential)
        } else Toast.makeText(this.requireContext(), "Enter correct Code", Toast.LENGTH_SHORT)
            .show()

    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        lifecycleScope.launch {
            showLoading()
            auth.signInWithCredential(credential)
                .addOnCompleteListener(this@VerifyNumberFragment.requireActivity()) { task ->
                    if (task.isSuccessful) {
                        hideLoading()
                        findNavController().navigate(
                            VerifyNumberFragmentDirections.actionVerifyNumberFragmentToCreateAccountFragment(
                                args.phoneNumber
                            )
                        )
                    } else {
                        // Sign in failed, display a message and update the UI
                        hideLoading()
                        if (task.exception is FirebaseAuthInvalidCredentialsException) {
                            // The verification code entered was invalid
                            Toast.makeText(this@VerifyNumberFragment.requireContext(), "Invalid OTP", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }
        }

    }
}