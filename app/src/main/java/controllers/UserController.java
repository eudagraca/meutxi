package controllers;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;

import com.account.views.MainActivity_;
import com.cazaea.sweetalert.SweetAlertDialog;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import models.User;
import utils.Preferences;
import utils.Utils;

public class UserController {

    private User user;
    private Context context;
    private ProgressDialog progressDialog;


    public UserController(User user, Context context) {
        this.user = user;
        this.context = context;

        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Aguarde por favor");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
    }

    public void createUser() {
        progressDialog.show();
        user.getImage().saveInBackground((SaveCallback) e -> {
            if (e == null) {
                ParseUser parseUser = new ParseUser();
                parseUser.setEmail(user.getEmail().trim());
                parseUser.setUsername(user.getName());
                parseUser.setPassword(user.getPassword());
                parseUser.put("photo", user.getImage());

                parseUser.signUpInBackground(ex -> {
                    if (ex == null) {
                        progressDialog.dismiss();
                        alert("Usuário criado!");
                    } else {
                        progressDialog.dismiss();
                        new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText(ex.toString())
                                .setCancelText("entendi")
                                .setConfirmClickListener(SweetAlertDialog::dismissWithAnimation)
                                .show();
                    }
                });
            }
        });
    }

    public void login() {
        progressDialog.show();

        ParseUser.logInInBackground(user.getName(), user.getPassword(), (parseUser, e) -> {
            if (parseUser != null) {
                user.setName(parseUser.getUsername());
                user.setEmail(parseUser.getEmail());
                if (e == null) {
                    loginSuccess();
                    progressDialog.dismiss();
                    Preferences pref = new Preferences(context);
                    pref.saveUser(parseUser.getObjectId(), parseUser.getUsername(), parseUser.getEmail());

                } else {
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                    Utils.alertaNegativa(context, "Erro", e.getMessage());
                }
            } else {
                progressDialog.dismiss();
                if (e != null) {
                    Utils.alertaNegativa(context, "Erro", e.getMessage());
                }
                ParseUser.logOut();
            }
        });
    }

    public void updateObject() {
        progressDialog.show();
        ParseUser parseUser = ParseUser.getCurrentUser();

        if (user.getImage() != null) {
            user.getImage().saveInBackground((SaveCallback) e -> {
                if (e == null) {
                    parseUser.put("username", user.getName());
                    parseUser.put("photo", user.getImage());
                    parseUser.saveInBackground(ex -> {
                        progressDialog.dismiss();
                        alert("Perfil actualizado!");
                    });
                } else {
                    progressDialog.dismiss();
                    new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText(e.toString())
                            .setCancelText("entendi")
                            .setConfirmClickListener(SweetAlertDialog::dismissWithAnimation)
                            .show();
                }
            });
        } else {
            parseUser.put("username", user.getName());
            parseUser.saveInBackground(ex -> {
                if (ex == null) {
                    progressDialog.dismiss();
                    alert("Perfil actualizado!");
                } else {
                    progressDialog.dismiss();
                    alert("Não foi possível actualizar ");
                }
            });
        }
    }

    private void alert(String title) {
        new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText(title)
                .setConfirmText("OK")
                .setConfirmClickListener(v -> loginSuccess())
                .show();
    }

    private void loginSuccess() {
        Intent intent = new Intent(context, MainActivity_.class);
        context.startActivity(intent);
    }

}
