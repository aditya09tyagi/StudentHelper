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
import com.example.projectsetup.ui.admin.AdminHomeActivity
import com.example.projectsetup.ui.base.BaseActivity
import com.example.projectsetup.ui.home.HomeActivity
import com.example.projectsetup.ui.loader.ProgressModal
import com.example.projectsetup.ui.selection.SelectionActivity
import com.example.projectsetup.ui.user_details.UserDetailsActivity
import com.example.projectsetup.util.Constants
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

    private var userType = Constants.USER_TYPE_STUDENT

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
            Constants.USER_TYPE_STUDENT,
            getDrawable(R.drawable.ic_student)!!,
            getString(R.string.student_label)
        )
        loginTypeList.add(loginModel)
        loginModel = LoginModel(
            Constants.USER_TYPE_FACULTY,
            getDrawable(R.drawable.ic_teacher)!!,
            getString(R.string.teacher_label)
        )
        loginTypeList.add(loginModel)
        loginModel =
            LoginModel(
                Constants.USER_TYPE_ADMIN,
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
                        when (it.userType) {
                            Constants.USER_TYPE_STUDENT -> {
                                if (it.age == 0) {
                                    sharedPreferenceUtil.putInt(Constants.EXTRA_USER_TYPE,Constants.USER_TYPE_STUDENT)
                                    startActivity(
                                        UserDetailsActivity.newIntent(
                                            this,
                                            it.id,
                                            it.name,
                                            userType
                                        )
                                    )
                                    overridePendingTransition(
                                        R.anim.slide_in_right,
                                        R.anim.slide_out_left
                                    )
                                    finishAffinity()
                                } else {
                                    startActivity(SelectionActivity.newIntent(this, it.id))
                                    overridePendingTransition(
                                        R.anim.slide_in_right,
                                        R.anim.slide_out_left
                                    )
                                    finishAffinity()
                                }
                            }
                            Constants.USER_TYPE_FACULTY -> {
                                sharedPreferenceUtil.putInt(Constants.EXTRA_USER_TYPE,Constants.USER_TYPE_FACULTY)
                                if (it.age == 0) {
                                    startActivity(
                                        UserDetailsActivity.newIntent(
                                            this,
                                            it.id,
                                            it.name,
                                            userType
                                        )
                                    )
                                    overridePendingTransition(
                                        R.anim.slide_in_right,
                                        R.anim.slide_out_left
                                    )
                                    finishAffinity()
                                } else {
                                    startActivity(SelectionActivity.newIntent(this, it.id))
                                    overridePendingTransition(
                                        R.anim.slide_in_right,
                                        R.anim.slide_out_left
                                    )
                                    finishAffinity()
                                }
                            }
                            Constants.USER_TYPE_ADMIN -> {
                                sharedPreferenceUtil.putInt(Constants.EXTRA_USER_TYPE,Constants.USER_TYPE_ADMIN)
                                startActivity(AdminHomeActivity.newIntent(this))
                                finishAffinity()
                            }
                        }
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
                        loginViewModel.handleGoogleSignInResult(googleSignInTask, userType)
                    }
                }
            }
        }
    }

    override fun onItemClicked(position: Int, loginModel: LoginModel) {
        selectedItemId = loginModel.id
        loginAdapter.notifyDataSetChanged()
        userType = loginModel.id
    }

}