package com.example.myapplication.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.MyApp.Companion.prefs
import com.example.myapplication.R
import kotlinx.android.synthetic.main.activity_login.*
import ru.profit_group.scorocode_sdk.Callbacks.CallbackLoginUser
import ru.profit_group.scorocode_sdk.Callbacks.CallbackLogoutUser
import ru.profit_group.scorocode_sdk.Responses.user.ResponseLogin
import ru.profit_group.scorocode_sdk.scorocode_objects.DocumentInfo
import ru.profit_group.scorocode_sdk.scorocode_objects.User

class LoginActivity : AppCompatActivity() {

    lateinit var login: String
    lateinit var pass: String

    override fun onBackPressed() {
        setResult(2, intent)
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loginBTN.setOnClickListener {
            pass = passET!!.text.toString()
            login = mailET!!.text.toString()
            loginClick()
        }
    }

    override fun onResume() {
        super.onResume()
        if (prefs.getBoolean("authorization", false)) {
            login = prefs.getString("login", "")!!
            pass = prefs.getString("pass", "")!!
            loginClick()

        }
    }

    private fun loginClick() {
        User().login(login, pass, object : CallbackLoginUser {
            override fun onLoginSucceed(responseLogin: ResponseLogin) {
                userInfo = responseLogin.result.userInfo
                saveUserInfo(login, pass)
                setResult(RESULT_OK, intent)
                finish()
            }

            override fun onLoginFailed(errorCode: String, errorMessage: String) {
                Toast.makeText(this@LoginActivity, errorMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun saveUserInfo(login: String, pass: String) {
        prefs.run {
            edit().putString("login", login).apply()
            edit().putString("pass", pass).apply()
            edit().putBoolean("authorization", true).apply()
        }
    }

    companion object {

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
