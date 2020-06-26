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
import com.dotts.core.android.pcy_chat.Models.UserModel
import com.dotts.core.android.pcy_chat.Utils.HashUtils
import com.google.gson.Gson
import com.google.gson.JsonParser
import com.google.gson.stream.JsonReader
import java.io.InputStreamReader
import java.net.Socket

private const val EXTRA_REGISTER_PROCESS = "com.dotts.core.pcy_chat.register_process"
const val EXTRA_REGISTER_ENDED = "com.dotts.core.pcy_chat.register_process_ended"

class RegisterActivity : AppCompatActivity() {

    lateinit var returnButton: Button
    lateinit var usernameEditText: EditText
    lateinit var fullNameEditText: EditText
    lateinit var passwordEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        returnButton = findViewById(R.id.return_button)
        usernameEditText = findViewById(R.id.usernameRegister_editText)
        fullNameEditText = findViewById(R.id.fullNameRegister_editText)
        passwordEditText = findViewById(R.id.passwordRegister_editText)

        returnButton.setOnClickListener { view: View ->
            setExtraResult(UserModel(), false)
        }
    }

    companion object {
        fun newIntent(packageContext: Context, userId: String?) : Intent {
            return Intent(packageContext, RegisterActivity::class.java).apply {
                putExtra(EXTRA_REGISTER_PROCESS, userId)
            }
        }
    }

    fun registerUser(view: View) {
        // create model
        var user = UserModel(
            "register",
            null,
            username = usernameEditText.getText().toString(),
            fullName = fullNameEditText.getText().toString(),
            password = HashUtils.sha256(passwordEditText.getText().toString())
        )

        // connect to server and register user
        connectToServerAndRegister(user, this).execute()
    }

    private fun setExtraResult(user: UserModel, authenticated: Boolean) {
        var isAuthenticated = authenticated

        val data = Intent().apply {
            putExtra(EXTRA_REGISTER_ENDED, user._id)
        }
        val result = when{
            isAuthenticated -> Activity.RESULT_OK
            else -> Activity.RESULT_CANCELED
        }
        setResult(result, data)

        if (isAuthenticated) {
            finish()
        } else {
            finish()
        }
    }

    class connectToServerAndRegister(user: UserModel, activity: RegisterActivity) : AsyncTask<Void, Void, String>() {
        var user = user;
        var user2: UserModel = UserModel()
        var activity = activity

        override fun doInBackground(vararg params: Void?): String? {
            val client = Socket("192.168.0.143", 3000)
            client.outputStream.write(Gson().toJson(user).toByteArray())

            var element = JsonParser().parse(JsonReader(InputStreamReader(client.getInputStream())))
            user2 = Gson().fromJson(element, UserModel::class.java)


            client.close()

            return null
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)

            Log.d("RegisterActivity", "onPostExecute: ${user2.toString()}")
            if (user2.action == "registered") {
//                user2.action = "authenticated"
                activity.setExtraResult((user2), true)
            } else {
                Toast.makeText(
                    activity,
                    "Username taken\n or invalid data!",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}