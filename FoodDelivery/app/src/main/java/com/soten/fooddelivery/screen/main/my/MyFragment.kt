package com.soten.fooddelivery.screen.main.my

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.soten.fooddelivery.R
import com.soten.fooddelivery.databinding.FragmentMyBinding
import com.soten.fooddelivery.extensions.load
import com.soten.fooddelivery.model.OrderModel
import com.soten.fooddelivery.screen.base.BaseFragment
import com.soten.fooddelivery.util.provider.ResourceProvider
import com.soten.fooddelivery.widget.adapter.ModelRecyclerAdapter
import com.soten.fooddelivery.widget.adapter.listener.AdapterListener
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class MyFragment : BaseFragment<MyViewModel, FragmentMyBinding>() {

    override val viewModel by viewModel<MyViewModel>()

    override fun getViewBinding(): FragmentMyBinding = FragmentMyBinding.inflate(layoutInflater)

    private val gso: GoogleSignInOptions by lazy {
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
    }

    private val gsc by lazy { GoogleSignIn.getClient(requireActivity(), gso) }

    private val auth by lazy { Firebase.auth }

    private val loginLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                try {
                    task.getResult(ApiException::class.java)?.let { account ->
                        Log.e(TAG, "firebaseAuthWithGoogle: ${account.id}")
                        viewModel.saveToken(account.idToken ?: throw Exception())
                    } ?: throw Exception()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

    private val resourceProvider by inject<ResourceProvider>()

    private val adapter by lazy {
        ModelRecyclerAdapter<OrderModel, MyViewModel>(
            listOf(),
            viewModel,
            resourceProvider,
            adapterListener = object : AdapterListener {}
        )
    }


    override fun initViews() = with(binding) {
        loginButton.setOnClickListener {
            signInGoogle()
        }
        logoutButton.setOnClickListener {
            auth.signOut()
            viewModel.signOut()
            Snackbar.make(binding.root, "로그아웃 완료.", Snackbar.LENGTH_SHORT).show()
        }
        orderRecyclerView.adapter = adapter
    }

    private fun signInGoogle() {
        val signIntent = gsc.signInIntent
        Log.d(TAG, "함수 접근")
        loginLauncher.launch(signIntent)
    }

    override fun observeData() = viewModel.myStateLiveData.observe(viewLifecycleOwner) { state ->
        when (state) {
            is MyState.Loading -> handleLoadingState()
            is MyState.Success -> handleSuccessState(state)
            is MyState.Login -> handleLoginState(state)
            is MyState.Error -> handleErrorState(state)
            else -> {
            }
        }
    }

    private fun handleLoadingState() {
        binding.loginRequiredGroup.isGone = true
        binding.profileGroup.isVisible = true
    }

    private fun handleSuccessState(state: MyState.Success) = with(binding) {
        progressBar.isGone = true
        when (state) {
            is MyState.Success.Registered -> handleRegisteredState(state)
            is MyState.Success.NotRegistered -> {
                profileGroup.isGone = true
                loginRequiredGroup.isVisible = true
            }
        }
    }

    private fun handleRegisteredState(state: MyState.Success.Registered) = with(binding) {
        profileGroup.isVisible = true
        loginRequiredGroup.isGone = true
        profileImageView.load(state.profileImageUri.toString(), 60f)
        userNameTextView.text = state.userName

        adapter.submitList(state.orderList)
//        Toast.makeText(requireContext(), state.orderList.toString(), Toast.LENGTH_SHORT).show()
    }

    private fun handleLoginState(state: MyState.Login) {
        binding.progressBar.isVisible = true
        val credential = GoogleAuthProvider.getCredential(state.idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    viewModel.setUserInfo(user)
                } else {
                    auth.signOut()
                    viewModel.setUserInfo(null)
                }
            }
    }

    private fun handleErrorState(state: MyState.Error) {

    }

    companion object {
        fun newInstance() = MyFragment()

        const val TAG = "MyFragment"

        const val RC_SIGN_IN = 1000
    }

}