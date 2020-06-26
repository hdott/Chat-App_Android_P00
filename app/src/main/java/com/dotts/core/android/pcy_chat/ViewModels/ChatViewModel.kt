package com.dotts.core.android.pcy_chat.ViewModels

import androidx.lifecycle.ViewModel
import com.dotts.core.android.pcy_chat.Models.ConnectedUsersModel
import com.dotts.core.android.pcy_chat.Models.UserModel

class ChatViewModel : ViewModel() {
    lateinit var onlineUsers : MutableList<ConnectedUsersModel>
    var connectedUser : UserModel = UserModel(username = "")

}