package com.graduation.red.presentation.authentication.loginwithpassword

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.graduation.red.MainActivity
import com.graduation.red.R
import com.graduation.red.base.BaseFragment
import com.graduation.red.databinding.FragmentLoginWithPasswordBinding
import kotlinx.coroutines.launch


class LoginWithPasswordFragment : BaseFragment<FragmentLoginWithPasswordBinding>() {
    override val layoutRes: Int
        get() = R.layout.fragment_login_with_password

    private val args: LoginWithPasswordFragmentArgs by navArgs()
    private val db = Firebase.firestore

    override fun initUI(savedInstanceState: Bundle?) {
        binding.tvPhoneNumber.text = args.phone
        handleSubmitClick()
    }

    private fun passwordValid(): Boolean {
        if (binding.tvPassword.text.trim().isEmpty()) {
            createToast("enter password")
            return false
        }
        if (binding.tvPassword.text.trim().count() < 6) {
            createToast("password must be more than 6 char")
            return false
        }
        return true
    }

    private fun handleSubmitClick() {
        binding.btnSubmit.setOnClickListener {
            showLoading()
            viewLifecycleOwner.lifecycleScope.launch {
                if (passwordValid()) {
                    db.collection("Users").document(args.phone).get().addOnSuccessListener {
                        hideLoading()
                        if (it != null) {
                            val password = it.data?.get("password")
                            if (password == binding.tvPassword.text.trim()
                                    .toString()
                            ) goToMainActivity()
                            else createToast("incorrect password")
                        }
                    }
                }
            }
        }

    }

    private fun goToMainActivity() {
        startActivity(Intent(this.requireActivity() , MainActivity::class.java))
        this.requireActivity().finish()
    }
}