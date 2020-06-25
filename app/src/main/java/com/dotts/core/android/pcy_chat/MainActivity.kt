package com.dotts.core.android.pcy_chat

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import com.dotts.core.android.pcy_chat.ViewModels.ChatHelperViewModel

private const val REQUEST_LOGIN_PROCESS = 0

class MainActivity : AppCompatActivity() {

    private val chatHelperViewModel: ChatHelperViewModel by lazy {
        ViewModelProviders.of(this).get(ChatHelperViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (!chatHelperViewModel.appStarted) {
            startLogInProcess()
        }
        chatHelperViewModel.appStarted = true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_CANCELED){
            finish()
        }
    }

    fun startLogInProcess() {
        val intent = LogInActivity.newIntent(this@MainActivity, null)
        startActivityForResult(intent, REQUEST_LOGIN_PROCESS)
    }
}