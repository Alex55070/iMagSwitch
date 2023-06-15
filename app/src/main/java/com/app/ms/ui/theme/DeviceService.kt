package com.app.ms.ui.theme
import com.app.ms.ElecUpdate
import retrofit2.Call
import retrofit2.http.*

interface DeviceService {
    @PUT("elec/{id}")
    //fun updateDeviceState(@Path("id") deviceId: Int, @Body request: DeviceStateRequest): Call<Unit>
    //fun updateDeviceState(@Path("id") id: Int, @Body request: DeviceStateRequest): Call<Unit>
    //fun updateDeviceState(@Path("id") deviceId: Int, @Field("isOn") isOn: Int): Call<Unit>
    fun updateElec(@Path("id") id: Int, @Body update: ElecUpdate): Call<Unit>

}
