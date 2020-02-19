package com.example.projectsetup.util

import android.content.Context
import android.view.View
import android.widget.Toast
import com.mobsandgeeks.saripaar.Rule
import com.mobsandgeeks.saripaar.Validator
import android.widget.EditText
import com.mobsandgeeks.saripaar.ValidationError
import com.mobsandgeeks.saripaar.rule.NotEmptyRule

abstract class CustomValidationListener (val context: Context): Validator.ValidationListener {

    companion object{

    }

    override fun onValidationFailed(errors: List<ValidationError>) {

        val filteredErrors = ArrayList<ValidationError>()
        filteredErrors.addAll(errors)

        for (error in errors) {
            val view = error.view
            if (view is EditText) {
                if (view.text.toString().trim { it <= ' ' }.isEmpty()) {
                    var isRequired = false
                    for (rule in error.failedRules) {
                        if (rule is NotEmptyRule) {
                            isRequired = true
                        }
                    }

                    if (!isRequired) {
                        filteredErrors.remove(error)
                    }
                }
            }
        }

        if (filteredErrors.size == 0) {
            onValidationSucceeded()
            return
        }

        for (error in filteredErrors) {
            val view = error.getView()
            val message = error.getCollatedErrorMessage(context)

            // Display error messages ;)
            if (view is EditText) {
                (view).error = message
            } else {
                Toast.makeText(context, message, Toast.LENGTH_LONG).show()
            }
        }
    }
}