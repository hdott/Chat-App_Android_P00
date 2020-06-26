package com.dotts.core.android.pcy_chat

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.lifecycle.ViewModelProviders
import com.dotts.core.android.pcy_chat.Fragments.GlobalChatFragment
import com.dotts.core.android.pcy_chat.Fragments.OnlineUsersFragment
import com.dotts.core.android.pcy_chat.Models.ConnectedUsersModel
import com.dotts.core.android.pcy_chat.Models.UserModel
import com.dotts.core.android.pcy_chat.ViewModels.ChatHelperViewModel
import com.dotts.core.android.pcy_chat.ViewModels.ChatViewModel
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.google.gson.stream.JsonReader
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_online_users.*
import java.io.InputStreamReader
import java.net.Socket
import java.util.*
import kotlin.collections.ArrayList

private const val REQUEST_LOGIN_PROCESS = 0

class MainActivity : AppCompatActivity() {

    companion object {
        lateinit var activityThis: MainActivity
    }

    private lateinit var onlineUsersFragment: OnlineUsersFragment
    private lateinit var globalChatFragment: GlobalChatFragment

    private lateinit var textView: TextView
    private lateinit var socket: Socket

    private val chatHelperViewModel: ChatHelperViewModel by lazy {
        ViewModelProviders.of(this).get(ChatHelperViewModel::class.java)
    }
    private val chatViewModel: ChatViewModel by lazy {
        ViewModelProviders.of(this).get(ChatViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        activityThis = this

        onlineUsersFragment = OnlineUsersFragment()
        globalChatFragment = GlobalChatFragment()

        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_FrameLayout, onlineUsersFragment)
            commit()
        }

        button_fragmentOnlineUsers.setOnClickListener {
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.fragment_FrameLayout, onlineUsersFragment)
//                addToBackStack(null)
                commit()

                updateListOfOnlineUsers(null)
            }
        }

        button_fragmentGlobalChat.setOnClickListener {
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.fragment_FrameLayout, globalChatFragment)
//                addToBackStack(null)
                commit()
            }
        }


        if (!chatHelperViewModel.appStarted) {
            Log.d("MainActivity", "onCreate: appStarted")
            startLogInProcess()
        }
        chatHelperViewModel.appStarted = true

//        updateListOfOnlineUsers(null)
    }

    fun updateListOfOnlineUsers(view: View?) {
        if (!chatViewModel.connectedUser.username.equals("")) {
            var onlineUsersArray: ArrayList<String?> = arrayListOf()
            for (user in chatViewModel.onlineUsers) {
                onlineUsersArray.add(user.username)
            }
            var adapter = ArrayAdapter<String>(this, R.layout.activity_listview, onlineUsersArray)
            online_users_listView.adapter = adapter
//            online_users_listView.refreshDrawableState()
        }
    }

    override fun onStart() {
        super.onStart()

        if (!chatHelperViewModel.appStarted) {
            setVisible(false)
        } else {
            setVisible(true)
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        if (socket != null) {
            Log.d("MainActivity", "onDestroy: Socket not Null")
            Thread(Runnable {
                var getMsg = ConnectedUsersModel(
                    action = "disconnected",
                    username = chatViewModel.connectedUser.username
                )
                socket.outputStream.write(Gson().toJson(getMsg).toByteArray())
                socket.close()
            }).start()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_CANCELED) {
            finish()
        }

        if (requestCode == REQUEST_LOGIN_PROCESS) {
            chatViewModel.connectedUser =
                UserModel(_id = data?.getStringExtra(EXTRA_AUTHENTICATION_ENDED))
            startServer()
        }
    }

    private fun startServer() {
        Thread(Runnable {
            socket = Socket("192.168.0.143", 3000)
            chatViewModel.connectedUser.action = "connected"
            socket.outputStream.write(Gson().toJson(chatViewModel.connectedUser).toByteArray())

            var element = JsonParser().parse(JsonReader(InputStreamReader(socket.getInputStream())))
            chatViewModel.connectedUser = Gson().fromJson(element, UserModel::class.java)

            this.runOnUiThread(Runnable {
                var msg = "Welcome ${chatViewModel.connectedUser.username}"
                Log.d("MainActivity", "startServer: $msg")
//                text_view_mainactivity.setText(msg)
            })

            while (true) {
                var getMsg = ConnectedUsersModel(
                    action = "getConnectedUsers",
                    username = chatViewModel.connectedUser.username
                )
                socket.outputStream.write(Gson().toJson(getMsg).toByteArray())

                var element2 = JsonParser().parse((JsonReader(InputStreamReader(socket.getInputStream()))))
                var onlineUsersMsg = Gson().fromJson(element2, Array<ConnectedUsersModel>::class.java)
                chatViewModel.onlineUsers = onlineUsersMsg.toMutableList()



                this.runOnUiThread({
                    updateListOfOnlineUsers(null)
                })

                Thread.sleep(1000)
            }

        }).start()
    }

    private fun listenForMsg() {
        Thread(Runnable {
            while (true) {
                var element = JsonParser().parse(JsonReader(InputStreamReader(socket.getInputStream())))
//                var msg = Gson().fromJson(element)
            }
        })
    }



    fun startLogInProcess() {
        val intent = LogInActivity.newIntent(this@MainActivity, null)
        startActivityForResult(intent, REQUEST_LOGIN_PROCESS)
    }
}