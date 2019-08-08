package utils;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

public class Preferences {

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    public Preferences(Context context) {
        String NOME_ARQUIVO = "user.Preferences";
        int MODE = 0;
        preferences = context.getSharedPreferences(NOME_ARQUIVO, MODE);
        editor = preferences.edit();
        editor.apply();
    }

    public void delete_data(@NonNull Context ctx) {
        String NOME_ARQUIVO = "user.Preferences";
        int MODE = 0;
        preferences = ctx.getSharedPreferences(NOME_ARQUIVO, MODE);
        editor = preferences.edit();
        editor.clear();
        editor.apply();
    }

    public void saveUser(String userID, String username, String email) {
        String KEY = "userid";
        String NAME = "username";
        String EMAIL = "userEmail";
        editor.putString(KEY, userID);
        editor.putString(NAME, username);
        editor.putString(EMAIL, email);
        editor.commit();
    }

    public void savePosetive(@NonNull Context context, Float saldo) {
        String NOME_ARQUIVO = "posetive.Preferences";
        int MODE = 0;
        preferences = context.getSharedPreferences(NOME_ARQUIVO, MODE);
        editor = preferences.edit();
        editor.clear();
        editor.apply();
        String KEY = "saldoMensal";
        editor.putFloat(KEY, saldo);
        editor.commit();
    }

    public void saveNegative(@NonNull Context context, Float saldo) {
        String NOME_ARQUIVO = "negative.Preferences";
        int MODE = 0;
        preferences = context.getSharedPreferences(NOME_ARQUIVO, MODE);
        editor = preferences.edit();
        editor.clear();
        editor.apply();
        String KEY = "saldoMensal";
        editor.putFloat(KEY, saldo);
        editor.commit();
    }

    public void receita(@NonNull Context context, Float saldo) {
        String NOME_ARQUIVO = "receita.Preferences";
        int MODE = 0;
        preferences = context.getSharedPreferences(NOME_ARQUIVO, MODE);
        editor = preferences.edit();
        editor.clear();
        editor.apply();
        String KEY = "saldo";
        editor.putFloat(KEY, saldo);
        editor.commit();
    }

    public void despesa(@NonNull Context context, Float saldo) {
        String NOME_ARQUIVO = "despesa.Preferences";
        int MODE = 0;
        preferences = context.getSharedPreferences(NOME_ARQUIVO, MODE);
        editor = preferences.edit();
        editor.clear();
        editor.apply();
        String KEY = "saldo";
        editor.putFloat(KEY, saldo);
        editor.commit();
    }

}
