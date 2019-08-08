package com.account.views;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.account.R;
import com.cazaea.sweetalert.SweetAlertDialog;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Objects;

import controllers.UserController;
import de.hdodenhof.circleimageview.CircleImageView;
import models.User;

@EActivity(R.layout.activity_perfil)
public class PerfilActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_PICKER = 115;
    @ViewById(R.id.profile_image)
    CircleImageView profileImage;

    @ViewById(R.id.emai_update)
    TextInputLayout email_update;

    @ViewById(R.id.name_update)
    TextView name_update;

    @ViewById(R.id.edit_name)
    TextView enable_inputs;

    @ViewById(R.id.name)
    TextInputLayout name;

    @ViewById(R.id.chage_pass)
    TextView change_pass;

    @ViewById(R.id.ll_login_data)
    LinearLayout ll_login_data;

    @ViewById(R.id.mToolbar_perfil)
    MaterialToolbar materialToolbar;

    @ViewById(R.id.mBtn_update)
    MaterialButton mButton;

    private ParseUser parse;
    private ProgressDialog progressDialog;
    private ParseFile file;

    @AfterViews
    void init() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("...");
        progressDialog.setCancelable(false);

        materialToolbar.setTitle(getResources().getString(R.string.meu_perfil));
        setSupportActionBar(materialToolbar);
        Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(R.drawable.ic_arrow_back);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        materialToolbar.setNavigationOnClickListener(v -> startActivity(new Intent(this, MainActivity_.class)));

        parse = ParseUser.getCurrentUser();
        Objects.requireNonNull(email_update.getEditText()).setText(parse.getEmail());
        name_update.setText(parse.getUsername());
        if (parse.getParseFile("photo") != null) {
            Picasso.get().load(Objects.requireNonNull(parse.getParseFile("photo")).getUrl()).into(profileImage);
        }
    }

    @Click(R.id.mBtn_update)
    void update() {
        User user = new User();

        progressDialog.show();

        if (validateInput()) {
            user.setName(String.valueOf(Objects.requireNonNull(name.getEditText()).getText()));
            user.setImage(file);
            user.setUid(parse.getObjectId());
            UserController controller = new UserController(user, this);
            controller.updateObject();
        }
    }

    private boolean validateInput() {
        boolean isvalid = false;
        if (String.valueOf(Objects.requireNonNull(name.getEditText()).getText()).isEmpty()) {
            name.setErrorEnabled(true);
            name.setError("Deves preencher o nome");
            name.requestFocus();
        } else {
            name.setErrorEnabled(false);
            isvalid = true;
        }
        return isvalid;
    }

    @Click(R.id.profile_image)
    void image() {
        profileImage.setOnClickListener(v -> {
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
                profileImage.setImageBitmap(bitmap);
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

    @Click(R.id.logout)
    void logOut() {
        if (parse != null) {
            ParseUser.logOutInBackground(e -> {

                if (e != null) {
                    startActivity(new Intent(getBaseContext(), LoginActivity_.class));
                    finish();
                } else {
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


    @Click(R.id.edit_name)
    void enableInputs() {
        name.setVisibility(View.VISIBLE);
        mButton.setVisibility(View.VISIBLE);
        Objects.requireNonNull(name.getEditText()).setText(parse.getUsername());
    }


    @Click(R.id.chage_pass)
    void callToResetPass() {
        SweetAlertDialog dialog = new SweetAlertDialog(PerfilActivity.this, SweetAlertDialog.WARNING_TYPE);
        dialog.setTitleText("Deseja redifinir a senha?")
                .setCancelText("não")
                .setConfirmText("sim");
        dialog.setConfirmClickListener(sweetAlertDialog -> {

        }).setCancelClickListener(sweetAlertDialog -> dialog.dismissWithAnimation()).show();
    }

    private void alert(String title) {
        new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText(title)
                .setConfirmText("OK")
                .setConfirmClickListener(v -> loginSuccess())
                .show();
    }

    private void loginSuccess() {
        Intent intent = new Intent(PerfilActivity.this, MainActivity_.class);
        startActivity(intent);
    }
}
