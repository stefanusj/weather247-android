package com.solusi247.weather247.module.presenter

import android.content.Context
import com.solusi247.weather247.Weather247
import com.solusi247.weather247.module.view.MainView
import com.solusi247.weather247.service.ApiService
import com.solusi247.weather247.utils.Constant
import com.solusi247.weather247.utils.Message
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MainPresenter(val view: MainView) {

    val apiService: ApiService
    val context: Context

    init {
        apiService = ApiService.create()
        context = Weather247.context
    }

    fun loadWeather() {
        view.showLoading()
        apiService.getAllWeather()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(
                        { result ->
                            try {
                                if (!result.error) {
                                    view.playAnimationWeatherToday()
                                    // Result successfull
                                    view.onWeatherToday(result.data[0])

                                    view.onLastWeather(result.data)
                                } else {
                                    // Connection success but error in result
                                    Message.showToast(context, result.message, Message.ERROR)
                                }
                            } catch (e: Exception) {
                                e.printStackTrace()
                                Message.showToast(context, Constant.RESULT_ERROR, Message.ERROR)
                            } finally {
                                view.hideLoading()
                            }
                        },
                        { error ->
                            error.printStackTrace()
                            Message.showToast(context, Constant.PROBLEM_SERVER, Message.ERROR)
                            view.hideLoading()
                        }
                )
    }

}