package com.bashoo.onlineshop.activities

import android.os.Bundle
import android.text.TextUtils
import android.view.WindowManager
import com.bashoo.onlineshop.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : ShowSnackBarActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        @Suppress("DEPRECATION")
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_register)

        setBackButtonActionBar()

        register_Button.setOnClickListener { registerUser() }

    }

    private fun setBackButtonActionBar() {
        setSupportActionBar(register_toolbar)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back_button)
        }

        register_toolbar.setNavigationOnClickListener { onBackPressed() }

    }

    private fun validateRegisterActivityDetails(): Boolean {
        return when {
            TextUtils.isEmpty(register_firstNameEditText.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.error_msg_first_name), true)
                false
            }

            TextUtils.isEmpty(
                register_lastNameEditText.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.error_msg_last_name), true)
                false
            }

            TextUtils.isEmpty(register_emailEditTextView.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.error_msg_email), true)
                false
            }

            TextUtils.isEmpty(register_passwordEditTextView.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.error_msg_password), true)
                false
            }

            TextUtils.isEmpty(
                register_confrimPassEditTextView.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.error_msg_confirm_password), true)
                false
            }

            register_passwordEditTextView.text.toString()
                .trim { it <= ' ' } != register_confrimPassEditTextView.text.toString()
                .trim() { it <= ' ' } -> {
                showErrorSnackBar(resources.getString(R.string.error_msg_mismatch_pass), true)
                false
            }

            !register_checkBoxTAC.isChecked -> {
                showErrorSnackBar(resources.getString(R.string.error_msg_terms_and_condition), true)
                false
            }
            else -> {
//                showErrorSnackBar(resources.getString(R.string.register_successfully), false)
                true
            }
        }
    }

    private fun registerUser() {

        // checking the validation if it is true then user will register on the firebase...
        if (validateRegisterActivityDetails()) {

            showProgressBar("Please wait...")

            val email = register_emailEditTextView.text.toString().trim { it <= ' ' }
            val password = register_passwordEditTextView.text.toString().trim() { it <= ' ' }

            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(
                    OnCompleteListener { task ->

                        hideProgressBar()

                        if (task.isSuccessful) {
                            val firebaseUser: FirebaseUser = task.result!!.user!!

                            FirebaseAuth.getInstance().signOut()
                            finish()

                            showErrorSnackBar("Your are registered successfully.", false)
                        } else {
                            showErrorSnackBar(task.exception!!.message.toString(), true)
                        }
                    })

        }
    }

}