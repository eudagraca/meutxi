package com.account.views;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.account.R;
import com.google.android.material.textfield.TextInputLayout;
import com.parse.ParseFile;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Objects;

import controllers.UserController;
import de.hdodenhof.circleimageview.CircleImageView;
import models.User;
import utils.Utils;

@EActivity(R.layout.activity_user_register)

public class UserRegisterActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_PICKER = 101;

    @ViewById(R.id.mTi_email_signup)
    TextInputLayout mTi_email;

    @ViewById(R.id.mTi_name_signup)
    TextInputLayout mTi_name;

    @ViewById(R.id.mTi_password_signup)
    TextInputLayout mTi_password;

    @ViewById(R.id.image_view)
    CircleImageView image_view;

    private ParseFile file;

    @Click(R.id.mTv_signUp)
    void save() {
        User user = new User();
        UserController controller = new UserController(user, this);
        if (isValidForm()) {

            String email = String.valueOf(Objects.requireNonNull(mTi_email.getEditText()).getText());
            String name = String.valueOf(Objects.requireNonNull(mTi_name.getEditText()).getText());
            String pass = String.valueOf(Objects.requireNonNull(mTi_password.getEditText()).getText());

            user.setEmail(email);
            user.setPassword(pass);
            user.setName(name);
            user.setImage(file);
            controller.createUser();
        }
    }

    @Click(R.id.ll_iniciar)
    void start() {
        startActivity(new Intent(this, LoginActivity_.class));
        finish();
    }

    @Click(R.id.image_view)
    void image() {

        image_view.setOnClickListener(v -> {
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.setType("image/*");
            startActivityForResult(i, REQUEST_CODE_PICKER);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @NonNull Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PICKER && resultCode == RESULT_OK) {
            Uri imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                image_view.setImageBitmap(bitmap);
                // Convert it to byte
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                // Compress image to lower quality scale 1 - 100
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] image = stream.toByteArray();
                // Create the ParseFile
                file = new ParseFile("androidbegin.png", image);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    boolean isValidForm() {
        boolean isvalid = false;
        boolean valid = Utils.isValidEmail(String.valueOf(Objects.requireNonNull(mTi_email.getEditText()).getText()));
        if (String.valueOf(Objects.requireNonNull(mTi_name.getEditText()).getText()).isEmpty()
                || String.valueOf(mTi_name.getEditText().getText()).length() < 3) {
            mTi_name.setErrorEnabled(true);
            mTi_name.setError("Introduza um nome válido");
            mTi_name.requestFocus();
        } else if (String.valueOf(Objects.requireNonNull(mTi_email.getEditText()).getText()).isEmpty()
                || !valid || mTi_email.getEditText().getText().length() < 7) {
            mTi_email.requestFocus();
            mTi_email.setError("Introduza um email válido");
            mTi_email.setErrorEnabled(true);
            mTi_name.setErrorEnabled(false);
        } else if (String.valueOf(Objects.requireNonNull(mTi_password.getEditText()).getText()).isEmpty() ||
                String.valueOf(mTi_password.getEditText().getText()).length() < 7) {
            mTi_password.requestFocus();
            mTi_password.setError("A sua senha deve ter mais de 7 caracteres");
            mTi_password.setErrorEnabled(true);
            mTi_email.setErrorEnabled(false);
        } else if (file == null) {
            mTi_password.setErrorEnabled(false);
            Toast.makeText(this, "Seleccione uma foto para o perfil", Toast.LENGTH_SHORT).show();
        } else {
            mTi_password.setErrorEnabled(false);
            mTi_name.setErrorEnabled(false);
            mTi_email.setErrorEnabled(false);
            isvalid = true;
        }
        return isvalid;
    }
}