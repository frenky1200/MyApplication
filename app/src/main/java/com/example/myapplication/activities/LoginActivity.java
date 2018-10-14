package com.example.myapplication.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import android.widget.EditText;

import com.example.myapplication.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ru.profit_group.scorocode_sdk.Callbacks.CallbackLoginUser;
import ru.profit_group.scorocode_sdk.Callbacks.CallbackLogoutUser;
import ru.profit_group.scorocode_sdk.Responses.user.ResponseLogin;
import ru.profit_group.scorocode_sdk.ScorocodeSdk;
import ru.profit_group.scorocode_sdk.scorocode_objects.DocumentInfo;
import ru.profit_group.scorocode_sdk.scorocode_objects.User;

public class LoginActivity extends AppCompatActivity {
    public static final String APPLICATION_ID = "fd10b71b4a1e447380414a66272218f7";
    public static final String CLIENT_KEY = "8c16b9b1162f48558f6ac9f163c17370";
    public static final String FILE_KEY = "063eb7b046ae4400bf380d6bde6287c6";
    private static final String MESSAGE_KEY = "56a4e6e802624f528beadedf3b2191f9";
    private static final String SCRIPT_KEY = "8792445a8f8f43f59cca2fe9f5c99f81";

    @BindView(R.id.mailET)
    EditText mailET;
    @BindView(R.id.passET) EditText passET;
    public static DocumentInfo userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ScorocodeSdk.initWith(APPLICATION_ID, CLIENT_KEY, null, FILE_KEY, MESSAGE_KEY, SCRIPT_KEY, null);
        ButterKnife.bind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences pref = getPreferences(MODE_PRIVATE);
        String login = pref .getString("login","");
        String pass = pref .getString("pass","");
        if(pref.getBoolean("authorization",false)){
            MainActivity.display(LoginActivity.this);
            User user = new User();
            user.login(login, pass, new CallbackLoginUser() {
                @Override
                public void onLoginSucceed(ResponseLogin responseLogin) {
                    userInfo = responseLogin.getResult().getUserInfo();
                    finish();
                }

                @Override
                public void onLoginFailed(String errorCode, String errorMessage) {
                }
            });
        }
    }

    @OnClick(R.id.loginBTN)
    public void LoginClick(){
        User user = new User();
        user.login(mailET.getText().toString(), passET.getText().toString(), new CallbackLoginUser() {
            @Override
            public void onLoginSucceed(ResponseLogin responseLogin) {
                userInfo = responseLogin.getResult().getUserInfo();
                saveUserInfo(mailET.getText().toString(), passET.getText().toString());
                MainActivity.display(LoginActivity.this);
            }

            @Override
            public void onLoginFailed(String errorCode, String errorMessage) {
                //Helper.showToast(getBaseContext(), R.string.error_login);
            }
        });
    }

    private void saveUserInfo(String login, String pass) {
        getPreferences(MODE_PRIVATE).edit().putString("login", login).apply();
        getPreferences(MODE_PRIVATE).edit().putString("pass", pass).apply();
        getPreferences(MODE_PRIVATE).edit().putBoolean("authorization", true).apply();
    }

    public static void logout(final Context context) {
        //LocalPersistence.writeObjectToFile(context, null, LocalPersistence.FILE_USER_INFO);
        new User().logout(new CallbackLogoutUser() {
            @Override
            public void onLogoutSucceed() {

                display(context);
            }

            @Override
            public void onLogoutFailed(String errorCode, String errorMessage) {
                //Helper.showToast(context, R.string.error);
            }
        });
    }

    public static void display(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }
}
