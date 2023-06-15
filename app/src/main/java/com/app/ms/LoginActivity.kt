package com.app.ms

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

import com.app.ms.ui.theme.AuthenticationService
import com.app.ms.ui.theme.DeviceService
import com.app.ms.ui.theme.DeviceStateRequest

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import androidx.appcompat.app.AppCompatActivity
import com.app.ms.ui.theme.LoginRequest


class LoginActivity : AppCompatActivity() {
    private lateinit var editTextUsername: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var buttonLogin: Button
    private lateinit var textViewWelcome: TextView
    private lateinit var buttonTurnOn: Button
    private lateinit var buttonTurnOff: Button

    private val BASE_URL = "http://192.168.1.65:8888/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        editTextUsername = findViewById(R.id.editTextUsername)
        editTextPassword = findViewById(R.id.editTextPassword)
        buttonLogin = findViewById(R.id.buttonLogin)
        textViewWelcome = findViewById(R.id.textViewWelcome)
        buttonTurnOn = findViewById(R.id.buttonTurnOn)
        buttonTurnOff = findViewById(R.id.buttonTurnOff)

        buttonLogin.setOnClickListener {
            val username = editTextUsername.text.toString()
            val password = editTextPassword.text.toString()
            performLogin(username, password)
        }
    }

    private fun performLogin(username: String, password: String) {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val authService = retrofit.create(AuthenticationService::class.java)

        val loginRequest = LoginRequest.LoginRequest(username, password)

        authService.login(loginRequest).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    if (loginResponse != null) {
                        val user = loginResponse.user
                        //showWelcomeMessage(user.User)
                        showDeviceControls()
                    } else {
                        showError("Invalid response from server")
                    }
                } else {
                    showError("Login failed")
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                showError("Communication error: " + t.message)
            }
        })
    }

    private fun showWelcomeMessage(username: String) {
        textViewWelcome.text = "Welcome, $username!"
        textViewWelcome.visibility = View.VISIBLE
    }

    private fun showDeviceControls() {
        buttonTurnOn.visibility = View.VISIBLE
        buttonTurnOff.visibility = View.VISIBLE

        buttonTurnOn.setOnClickListener {
            sendDeviceState(1)
        }

        buttonTurnOff.setOnClickListener {
            sendDeviceState(0)
        }
    }
/*
    private fun sendDeviceState(state: Int) {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val deviceService = retrofit.create(DeviceService::class.java)

        val deviceStateRequest = DeviceStateRequest(state)

        deviceService.updateDeviceState(1, deviceStateRequest).enqueue(object : Callback<Unit> {
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                if (response.isSuccessful) {
                    showToast("Device state updated")
                } else {
                    showToast("Failed to update device state")
                }
            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                showToast("Communication error: " + t.message)
            }
        })
    }
*/
private fun sendDeviceState(state: Int) {
    val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val deviceService = retrofit.create(DeviceService::class.java)

    val deviceId = 1 // ID del dispositivo que deseas actualizar

    val elecUpdate = ElecUpdate(deviceId, state)

    deviceService.updateElec(deviceId, elecUpdate).enqueue(object : Callback<Unit> {
        override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
            if (response.isSuccessful) {
                showToast("Device state updated")
            } else {
                showToast("Failed to update device state")
            }
        }

        override fun onFailure(call: Call<Unit>, t: Throwable) {
            showToast("Communication error: ${t.message}")
        }
    })
}


    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}

