package com.soten.usedtransaction.ui.mypage

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.soten.usedtransaction.R
import com.soten.usedtransaction.databinding.FragmentMypageBinding

class MyPageFragment : Fragment(R.layout.fragment_mypage) {

    private var binding: FragmentMypageBinding? = null
    private val auth: FirebaseAuth by lazy {
        Firebase.auth
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fragmentMyPageBinding = FragmentMypageBinding.bind(view)
        binding = fragmentMyPageBinding

        fragmentMyPageBinding.signInOutButton.setOnClickListener {
            binding?.let {
                val email = it.emailEditText.text.toString()
                val password = it.passwordEditText.text.toString()

                if (auth.currentUser == null) {
                    auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(requireActivity()) { task -> // requireActivity는 null 체크를 하고 받아 옴
                            if (task.isSuccessful) {
                                successSignIn()
                            } else {
                                Toast.makeText(
                                    context,
                                    "로그인에 실패했습니다. 이메일 또는 비밀번호를 확인해주세요",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                } else {
                    auth.signOut()
                    it.emailEditText.text.clear()
                    it.emailEditText.isEnabled = true
                    it.passwordEditText.text.clear()
                    it.passwordEditText.isEnabled = true

                    it.signInOutButton.text = "로그인"
                    it.signInOutButton.isEnabled = false
                    it.signUpButton.isEnabled = false
                }
            }
        }

        fragmentMyPageBinding.signUpButton.setOnClickListener {
            binding?.let {
                val email = it.emailEditText.text.toString()
                val password = it.passwordEditText.text.toString()

                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(requireActivity()) { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(
                                context,
                                "회원가입에 성공했습니다. 로그인 버튼을 눌러주세요.",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            Toast.makeText(
                                context,
                                "회원가입에 실패했습니다. 이미 가입한 이메일이거나 잘못된 비밀번호입니다.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                    }
            }

        }



        fragmentMyPageBinding.emailEditText.addTextChangedListener {
            binding?.let {
                val enable =
                    it.emailEditText.text.isNotEmpty() && it.passwordEditText.text.isNotEmpty()
                it.signInOutButton.isEnabled = enable
                it.signUpButton.isEnabled = enable
            }
        }

        fragmentMyPageBinding.passwordEditText.addTextChangedListener {
            binding?.let {
                val enable =
                    it.emailEditText.text.isNotEmpty() && it.passwordEditText.text.isNotEmpty()
                it.signInOutButton.isEnabled = enable
                it.signUpButton.isEnabled = enable
            }
        }

    }

    override fun onStart() {
        super.onStart()

        if (auth.currentUser == null) {
            binding?.let {
                it.emailEditText.text.clear()
                it.passwordEditText.text.clear()
                it.emailEditText.isEnabled = true
                it.passwordEditText.isEnabled = true

                it.signInOutButton.text = "로그인"
                it.signInOutButton.isEnabled = false
                it.signUpButton.isEnabled = false
            }

        } else {
            binding?.let {
                it.emailEditText.setText(auth.currentUser?.email)
                it.emailEditText.isEnabled = false
                it.passwordEditText.setText("********")
                it.passwordEditText.isEnabled = false

                it.signInOutButton.text = "로그아웃"
                it.signInOutButton.isEnabled = true
                it.signUpButton.isEnabled = false
            }
        }
    }

    private fun successSignIn() {
        if (auth.currentUser == null) {
            Toast.makeText(context, "로그인에 실패했습니다. 다시 시도해주세요", Toast.LENGTH_SHORT).show()
            return
        }

        binding?.emailEditText?.isEnabled = false
        binding?.passwordEditText?.isEnabled = false
        binding?.signUpButton?.isEnabled = false
        binding?.signInOutButton?.text = "로그아웃"
    }
}
