package com.dotts.core.android.pcy_chat

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import com.dotts.core.android.pcy_chat.Models.UserModel
import com.dotts.core.android.pcy_chat.Utils.HashUtils
import com.dotts.core.android.pcy_chat.ViewModels.ChatHelperViewModel
import com.dotts.core.android.pcy_chat.ViewModels.LogInViewModel
import com.google.gson.Gson
import com.google.gson.JsonParser
import com.google.gson.stream.JsonReader
import java.io.InputStreamReader
import java.net.Socket

private const val EXTRA_LOGIN_PROCESS = "com.dotts.core.pcy_chat.login_process"
const val EXTRA_AUTHENTICATION_ENDED = "com.dotts.core.pcy_chat.login_process_ended"
private const val REQUEST_REGISTER_PROCESS = 0

class LogInActivity : AppCompatActivity() {

    private lateinit var registerButton: Button
    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText

    private val logInViewModel: LogInViewModel by lazy {
        ViewModelProviders.of(this).get(LogInViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)

        usernameEditText = findViewById(R.id.username_editText)
        passwordEditText = findViewById(R.id.password_editText)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_CANCELED) {
            return
        }

        if (requestCode == REQUEST_REGISTER_PROCESS) {
            setExtraResult(UserModel(_id = data?.getStringExtra(EXTRA_REGISTER_ENDED)), true)
        }
    }

    companion object {
        fun newIntent(packageContext: Context, userId: String?) : Intent {
            return Intent(packageContext, LogInActivity::class.java).apply {
                putExtra(EXTRA_LOGIN_PROCESS, userId)
            }
        }
    }

    fun startRegisterProcess(view: View) {
        val intent = RegisterActivity.newIntent(this, null)
        startActivityForResult(intent, REQUEST_REGISTER_PROCESS)
    }

    fun authenticateUser(view: View) {
        // create model
        var username = usernameEditText.getText().toString()
        var password = passwordEditText.getText().toString()
        var user = UserModel(
            "logIn",
            null,
            username,
            "Horia",
            password = HashUtils.sha256(password)
        )

        //connect server and authenticate
        connectToServerAndAuthenticate(user, this).execute()
    }

    private fun setExtraResult(user: UserModel, authenticated: Boolean) {
        var isAuthenticated = authenticated

        val data = Intent().apply {
            putExtra(EXTRA_AUTHENTICATION_ENDED, user._id)
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

    class connectToServerAndAuthenticate(user: UserModel, activity: LogInActivity) : AsyncTask<Void, Void, String>() {
        var user = user
        var user2: UserModel = UserModel()
        var activity = activity

        override fun doInBackground(vararg params: Void?): String? {
            val client = Socket("79.114.176.127", 3000)
            client.outputStream.write(Gson().toJson(user).toByteArray())

            var element = JsonParser().parse(JsonReader(InputStreamReader(client.getInputStream())))
            user2 = Gson().fromJson(element, UserModel::class.java)


            client.close()

            return null
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)

            Log.d("LogInActivity", "onPostExecute: ${user2.toString()}")
            if (user2.action == "authenticated") {
                activity.setExtraResult(user2, true)
            } else {
                Toast.makeText(
                    activity,
                    "User or password incorrect!",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun saveToFileJSON(user: UserModel) {

    }
}