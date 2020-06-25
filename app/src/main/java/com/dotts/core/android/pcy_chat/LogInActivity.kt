package com.dotts.core.android.pcy_chat

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast

private const val EXTRA_LOGIN_PROCESS = "com.dotts.core.pcy_chat.login_process"
const val EXTRA_AUTHENTICATION_ENDED = "com.dotts.core.pcy_chat.login_process_ended"

class LogInActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)
    }

    companion object {
        fun newIntent(packageContext: Context, userId: String?) : Intent {
            return Intent(packageContext, LogInActivity::class.java).apply {
                putExtra(EXTRA_LOGIN_PROCESS, userId)
            }
        }
    }

    fun authenticateUser(view: View) {
        //connect server and authenticate


        // to be changed with the actual result
        val isAuthenticated = false

        val userId: String = ""

        // Setup Extra Result
        val data = Intent().apply {
            putExtra(EXTRA_AUTHENTICATION_ENDED, userId)
        }
        val result = when{
            isAuthenticated -> Activity.RESULT_OK
            else -> Activity.RESULT_CANCELED
        }
        setResult(result, data)

        if (isAuthenticated) {
            finish()
        } else {
            //
        }
    }
}