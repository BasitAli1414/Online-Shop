package com.bashoo.onlineshop.activities

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import com.bashoo.onlineshop.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : ShowSnackBarActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        @Suppress("DEPRECATION")
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_login)

        login_Button.setOnClickListener(this)
        login_registerTextView.setOnClickListener(this)
        login_forgot_passwordTextView.setOnClickListener(this)


    }

    override fun onClick(view: View?) {

        if (view != null) {
            when (view.id) {

                R.id.login_forgot_passwordTextView -> {
                    Toast.makeText(this, "Working", Toast.LENGTH_LONG).show()
                }
                R.id.login_Button -> {
                    login()
                }
                R.id.login_registerTextView -> {
                    startActivity(Intent(this, RegisterActivity::class.java))
                }
            }
        }
    }

    private fun validateLoginDetails(): Boolean {
        return when {

            TextUtils.isEmpty(login_emailEditTextView.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.error_msg_email), true)
                false
            }
            TextUtils.isEmpty(login_passwordEditTextView.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.error_msg_password), true)
                false
            }
            else -> {
                //showErrorSnackBar("Your details are valid ", false)
                true
            }

        }

    }

    private fun login() {

        showProgressBar("Please wait...")

        if (validateLoginDetails()) {

            val email = login_emailEditTextView.text.toString().trim { it <= ' ' }
            val password = login_passwordEditTextView.text.toString().trim { it <= ' ' }


            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(
                    OnCompleteListener { task ->

                        hideProgressBar()

                        if (task.isSuccessful) {
                            val firebaseUser: FirebaseUser = task.result!!.user!!
                            showErrorSnackBar("Your are successfully login.", false)
                        } else {
                            showErrorSnackBar(task.exception!!.message.toString(), true)
                        }
                    })
        }
    }
}