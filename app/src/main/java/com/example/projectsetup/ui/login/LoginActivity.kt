package com.example.projectsetup.ui.login

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projectsetup.R
import com.example.projectsetup.StudentHelper
import com.example.projectsetup.data.models.LoginModel
import com.example.projectsetup.data.models.Status
import com.example.projectsetup.di.components.DaggerLoginActivityComponent
import com.example.projectsetup.di.modules.activity.LoginActivityModule
import com.example.projectsetup.ui.base.BaseActivity
import com.example.projectsetup.ui.loader.ProgressModal
import com.example.projectsetup.ui.user_details.UserDetailsActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.cell_login_card.*
import javax.inject.Inject

class LoginActivity : BaseActivity(), LoginAdapter.OnItemClickListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var googleSignInClient: GoogleSignInClient

    @Inject
    lateinit var loginAdapter: LoginAdapter

    private lateinit var progressModal: ProgressModal
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var loginModel: LoginModel
    private var selectedItemId: Int = 0
    private var loginTypeList: ArrayList<LoginModel> = ArrayList()

    companion object {
        private const val REQUEST_CODE_GOOGLE_SIGN_IN = 1001

        fun newIntent(context: Context): Intent {
            val intent = Intent(context, LoginActivity::class.java)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        inject()
        initLoginTypeList()
        initialiseLayout()
        setListener()
        observeData()
    }

    private fun inject() {
        val component = DaggerLoginActivityComponent.builder()
            .loginActivityModule(LoginActivityModule(this))
            .studentHelperApplicationComponent(StudentHelper.get(this).studentHelperApplicationComponent())
            .build()

        component.injectLoginActivityComponent(this)

        loginViewModel = ViewModelProvider(this, viewModelFactory).get(LoginViewModel::class.java)

        progressModal = ProgressModal(this)
    }

    private fun initLoginTypeList() {
        loginModel = LoginModel(
            1,
            getDrawable(R.drawable.ic_student)!!,
            getString(R.string.student_label)
        )
        loginTypeList.add(loginModel)
        loginModel = LoginModel(
            2,
            getDrawable(R.drawable.ic_teacher)!!,
            getString(R.string.teacher_label)
        )
        loginTypeList.add(loginModel)
        loginModel =
            LoginModel(
                3,
                getDrawable(R.drawable.ic_admin)!!,
                getString(R.string.admin_label)
            )
        loginTypeList.add(loginModel)
    }

    private fun initialiseLayout() {
        rvLogin.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        rvLogin.adapter = loginAdapter
        if (loginTypeList.isNotEmpty())
            loginAdapter.initLoginTypeList(loginTypeList)
    }

    private fun setListener() {

        loginAdapter.setOnItemClickListener(this)

        googleSignInTv.setOnClickListener {
            if (selectedItemId != 0) {
                btnGoogleSignIn.performClick()
                startActivityForResult(googleSignInClient.signInIntent, REQUEST_CODE_GOOGLE_SIGN_IN)
            } else {
                showErrorSnackBar(getString(R.string.select_account_type), clLoginContainer)
            }
        }
    }

    private fun observeData() {
        loginViewModel.userLiveData.observe(this, Observer {
            when (it.status) {
                Status.LOADING -> {
                    progressModal.show()
                }
                Status.SUCCESS -> {
                    progressModal.dismiss()
                    it.data?.let {
                        startActivity(UserDetailsActivity.newIntent(this,it.id,it.name))
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                    }
                }
                Status.ERROR -> {
                    progressModal.dismiss()
                    it.message?.let { msg ->
                        if (msg.contains("college id"))
                            googleSignInClient.signOut()

                        showErrorSnackBar(msg, clParent)
                    }
                }
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_CODE_GOOGLE_SIGN_IN -> {
                if (resultCode == Activity.RESULT_OK) {
                    data?.let {
                        val googleSignInTask =
                            GoogleSignIn.getSignedInAccountFromIntent(it)
                        loginViewModel.handleGoogleSignInResult(googleSignInTask)
                    }
                }
            }
        }
    }

    override fun onItemClicked(position: Int, loginModel: LoginModel) {
        selectedItemId = loginModel.id
        loginAdapter.notifyDataSetChanged()
    }

}