package com.example.myapplication.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle

import androidx.appcompat.app.AppCompatActivity

import com.example.myapplication.R

import kotlinx.android.synthetic.main.activity_login.*
import ru.profit_group.scorocode_sdk.Callbacks.CallbackLoginUser
import ru.profit_group.scorocode_sdk.Callbacks.CallbackLogoutUser
import ru.profit_group.scorocode_sdk.Responses.user.ResponseLogin
import ru.profit_group.scorocode_sdk.ScorocodeSdk
import ru.profit_group.scorocode_sdk.scorocode_objects.DocumentInfo
import ru.profit_group.scorocode_sdk.scorocode_objects.User

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        ScorocodeSdk.initWith(APPLICATION_ID, CLIENT_KEY, null, FILE_KEY, MESSAGE_KEY, SCRIPT_KEY, null)
        loginBTN.setOnClickListener { loginClick() }
    }

    override fun onResume() {
        super.onResume()
        val pref = getPreferences(Context.MODE_PRIVATE)
        val login = pref.getString("login", "")
        val pass = pref.getString("pass", "")
        if (pref.getBoolean("authorization", false)) {
            MainActivity.display(this@LoginActivity)
            val user = User()
            user.login(login, pass, object : CallbackLoginUser {
                override fun onLoginSucceed(responseLogin: ResponseLogin) {
                    userInfo = responseLogin.result.userInfo
                    finish()
                }

                override fun onLoginFailed(errorCode: String, errorMessage: String) {}
            })
        }
    }

    private fun loginClick() {
        val user = User()
        user.login(mailET!!.text.toString(), passET!!.text.toString(), object : CallbackLoginUser {
            override fun onLoginSucceed(responseLogin: ResponseLogin) {
                userInfo = responseLogin.result.userInfo
                saveUserInfo(mailET!!.text.toString(), passET!!.text.toString())
                MainActivity.display(this@LoginActivity)
            }

            override fun onLoginFailed(errorCode: String, errorMessage: String) {
                //Helper.showToast(getBaseContext(), R.string.error_login);
            }
        })
    }

    private fun saveUserInfo(login: String, pass: String) {
        getPreferences(Context.MODE_PRIVATE).run {
            edit().putString("login", login).apply()
            edit().putString("pass", pass).apply()
            edit().putBoolean("authorization", true).apply()
        }
    }

    companion object {
        const val APPLICATION_ID = "fd10b71b4a1e447380414a66272218f7"
        const val CLIENT_KEY = "8c16b9b1162f48558f6ac9f163c17370"
        const val FILE_KEY = "063eb7b046ae4400bf380d6bde6287c6"
        private const val MESSAGE_KEY = "56a4e6e802624f528beadedf3b2191f9"
        private const val SCRIPT_KEY = "8792445a8f8f43f59cca2fe9f5c99f81"
        lateinit var userInfo: DocumentInfo

        fun logout(context: Context) {
            //LocalPersistence.writeObjectToFile(context, null, LocalPersistence.FILE_USER_INFO);
            User().logout(object : CallbackLogoutUser {
                override fun onLogoutSucceed() {

                    display(context)
                }

                override fun onLogoutFailed(errorCode: String, errorMessage: String) {
                    //Helper.showToast(context, R.string.error);
                }
            })
        }

        fun display(context: Context) {
            val intent = Intent(context, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            context.startActivity(intent)
        }
    }
}
