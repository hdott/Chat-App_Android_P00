package com.dotts.core.android.pcy_chat.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.lifecycle.ViewModelProviders
import com.dotts.core.android.pcy_chat.MainActivity
import com.dotts.core.android.pcy_chat.R
import com.dotts.core.android.pcy_chat.ViewModels.ChatViewModel
import kotlinx.android.synthetic.main.fragment_online_users.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//private const val ARG_PARAM1 = "param1"
//private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [GlobalChatFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class GlobalChatFragment : Fragment(R.layout.fragment_global_chat) {

    private val chatViewModel: ChatViewModel by lazy {
        ViewModelProviders.of(this).get(ChatViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//
//        if (chatViewModel.onlineUsers != null) {
//            var onlineUsersArray: ArrayList<String?> = arrayListOf()
//            for (user in chatViewModel.onlineUsers) {
//                onlineUsersArray.add(user.username)
//            }
//            var adapter = ArrayAdapter<String>(this, R.layout.activity_listview, onlineUsersArray)
//            online_users_listView.adapter = adapter
//            online_users_listView.refreshDrawableState()
//        }
    }
    // TODO: Rename and change types of parameters
//    private var param1: String? = null
//    private var param2: String? = null

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        arguments?.let {
//            param1 = it.getString(ARG_PARAM1)
//            param2 = it.getString(ARG_PARAM2)
//        }
//    }
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_online_users, container, false)
//    }
//
//    companion object {
//        /**
//         * Use this factory method to create a new instance of
//         * this fragment using the provided parameters.
//         *
//         * @param param1 Parameter 1.
//         * @param param2 Parameter 2.
//         * @return A new instance of fragment OnlineUsersFragment.
//         */
//        // TODO: Rename and change types and number of parameters
//        @JvmStatic
//        fun newInstance(param1: String, param2: String) =
//            OnlineUsersFragment().apply {
//                arguments = Bundle().apply {
//                    putString(ARG_PARAM1, param1)
//                    putString(ARG_PARAM2, param2)
//                }
//            }
//    }
}