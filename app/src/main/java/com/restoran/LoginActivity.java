package com.restoran;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;


import com.news.restoran.R;
import com.restoran.Models.Login;
import com.restoran.adapter.RetrofitClient;
import com.restoran.Interfase.Api;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    AutoCompleteTextView loginView;
    EditText pass;
    SharedPreferences spref;
    SharedPreferences.Editor prefEditor;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginView = (AutoCompleteTextView) findViewById(R.id.login);
        pass = (EditText) findViewById(R.id.password);
        spref = PreferenceManager.getDefaultSharedPreferences(this);
        prefEditor = spref.edit();
        loginView.setText(spref.getString("login", ""));
        if (spref.contains("login")) {
            pass.requestFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(pass, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    private boolean Validate(String login, String pass) {
        if (login.isEmpty() || pass.isEmpty() || login.length() < 3)
            return false;
        else
            return true;
    }

    public void login_btn(View view) {
        if (!Validate(loginView.getText().toString(), pass.getText().toString())) {
            Snackbar.make(view, "Validate error", Snackbar.LENGTH_SHORT).show();
        } else {
            Api api = RetrofitClient.getApi();
            Call<Login> call = api.getStr(loginView.getText().toString(), pass.getText().toString());
            call.enqueue(new Callback<Login>() {
                @Override
                public void onResponse(Call<Login> call, Response<Login> response) {
                    Login l = response.body();
                    if (l.getStatus().equals("success")) {
                        prefEditor.putString("login", loginView.getText().toString());
                        prefEditor.putString("fio",l.getFio());
                        prefEditor.putString("id",l.getMessage());

                        prefEditor.commit();
                        Toast.makeText(getApplicationContext(),l.getMessage(),Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplication(), MainActivity.class));
                    } else if (l.getStatus().equals("error")) {
                        Toast.makeText(getApplication(), "error:" + l.getMessage(), Toast.LENGTH_SHORT).show();
                    }


                }

                @Override
                public void onFailure(Call<Login> call, Throwable t) {
                    Toast.makeText(getApplication(), "error:" + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

    }
}
