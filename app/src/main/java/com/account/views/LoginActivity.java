package com.account.views;

import android.content.Intent;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatActivity;

import com.account.R;
import com.google.android.material.textfield.TextInputLayout;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.Objects;

import controllers.UserController;
import models.User;
import utils.Utils;

@EActivity(R.layout.activity_login)
public class LoginActivity extends AppCompatActivity {

    @ViewById(R.id.mTi_email_signIn)
    TextInputLayout mTi_email;

    @ViewById(R.id.mTi_password_signIn)
    TextInputLayout mTi_password;

    @Click(R.id.mTv_forgot_password)
    void forgotPassword() {

        startActivity(new Intent(this, RecoverActivity_.class));
    }

    @Click(R.id.mTv_newUser_signUp)
    void openRegisterUser() {
        startActivity(new Intent(this, UserRegisterActivity_.class));
    }

    @Click(R.id.mTv_startSession_signIn)
    void startSession() {

        User user = new User();
        UserController controller = new UserController(user, this);
        if (isValidForm()) {
            user.setName(String.valueOf(Objects.requireNonNull(mTi_email.getEditText()).getText()));
            user.setPassword(String.valueOf(Objects.requireNonNull(mTi_password.getEditText()).getText()));
            controller.login();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        SharedPreferences preferences = getSharedPreferences("user.Preferences", 0);
        String uid = preferences.getString("userid", null);

        if (uid != null) {
            startActivity(new Intent(this, MainActivity_.class));
            finish();
        }
    }

    boolean isValidForm() {
        boolean isvalid = false;
        boolean valid = Utils.isValidEmail(String.valueOf(Objects.requireNonNull(mTi_email.getEditText()).getText()));

        if (!valid) {
            mTi_email.requestFocus();
            mTi_email.setError("Introduza um email válido");
            mTi_email.setErrorEnabled(true);
        } else if (String.valueOf(Objects.requireNonNull(mTi_password.getEditText()).getText()).isEmpty() &&
                String.valueOf(mTi_password.getEditText().getText()).length() < 7) {
            mTi_password.requestFocus();
            mTi_password.setError("A sua senha deve ter mais de 7 caracteres");
            mTi_password.setErrorEnabled(true);
            mTi_email.setErrorEnabled(false);
        } else {
            mTi_email.setErrorEnabled(false);
            mTi_password.setErrorEnabled(false);
            isvalid = true;
        }
        return isvalid;
    }
}