package com.bignerdranch.android.test

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.google.gson.Gson
import com.google.gson.JsonParser
import com.google.gson.stream.JsonReader
import java.io.InputStreamReader
import java.net.Socket

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


    }

    fun doSth(view: View) {
        var task = someTask()
        task.execute()
    }
}


class someTask() : AsyncTask<Void, Void, String>() {

    var data: String = ""

    data class Student (
            var name: String? = null,
            var address: String? = null) {
    }
    val student = Student("Alex", "Rome") // instance
    val jsonString = Gson().toJson(student)
    var studentFromServer = Student()

    override fun doInBackground(vararg params: Void?): String? {
        val client = Socket("79.117.239.87", 3000)
//        client.outputStream.write("Hello".toByteArray())
        client.outputStream.write(jsonString.toByteArray())
        var element = JsonParser().parse(JsonReader(InputStreamReader(client.getInputStream())))
        studentFromServer = Gson().fromJson(element, Student::class.java)
        client.close()

        return null
    }

    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)

//        Log.d("MAINACTIVITY", "doInBackground: $data")
        Log.d("MAINACTIVITY", "onPostExecute: ${studentFromServer.name}")
        Log.d("MAINACTIVITY", "onPostExecute: ${studentFromServer.address}")
    }
}