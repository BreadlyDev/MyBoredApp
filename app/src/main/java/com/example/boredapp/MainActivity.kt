package com.example.boredapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.boredapp.api.ApiInterface
import com.example.boredapp.models.Activity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Locale

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        fetchActivity()

        findViewById<Button>(R.id.btn_get_task).setOnClickListener {
            fetchActivity()
        }
    }

    private fun fetchActivity() {
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://www.boredapi.com/api/")
            .build().create(ApiInterface::class.java)

        val response = retrofit.getActivity()

        response.enqueue(object: Callback<Activity> {
            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call<Activity>, response: Response<Activity>) {
                val responseBody = response.body()

                if(response.isSuccessful && responseBody != null) {
                    findViewById<TextView>(R.id.txt_activity).text = responseBody.activity
                    findViewById<TextView>(R.id.txt_accessibility).text = responseBody.accessibility.toString()
                    findViewById<TextView>(R.id.txt_participants).text = responseBody.participants.toString()
                    findViewById<TextView>(R.id.txt_type).text = responseBody.type
                        .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
                    findViewById<TextView>(R.id.txt_price).text = "${responseBody.price} $"
                }
            }

            override fun onFailure(call: Call<Activity>, t: Throwable) {
            }
        })
    }
}