package utils;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.cazaea.sweetalert.SweetAlertDialog;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Pattern;

public class Utils {

    public static final String CATEGORIA_DESPESA = "categories_despesa";
    public static final String CATEGORIA_RECEITA = "categories_receita";
    public static final String DATAOFMONTH = "dataOfMOnth";
    public static final String RECEITA = "receita";
    public static final String DESPESA = "despesa";

    public static void changeFragment(int frameLayoutId, @NonNull Fragment fragmentName, FragmentManager fragmentManager) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(frameLayoutId, fragmentName);
        fragmentTransaction.commit();
    }

    public static String totalDate() {
        Calendar calendar = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMMM-yyyy");
        return formatter.format(calendar.getTime());
    }

    public static String convertMoney(Double money) {
        return NumberFormat.getCurrencyInstance(new Locale("en", ""))
                .format(money).substring(1);
    }

    public static String currentDateAndMonth() {
        Calendar calendar = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat formatter = new SimpleDateFormat("M-yyyy");
        return formatter.format(calendar.getTime());
    }

    public static String currentDateAndMonth(Date date) {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        return formatter.format(date);
    }

    public static void alertaNegativa(@NonNull Context context, String titulo, String mensagem) {
        SweetAlertDialog aDialog = new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE);
        aDialog.setTitleText(titulo);
        aDialog.setConfirmText("ok");
        aDialog.setConfirmClickListener(sweetAlertDialog -> aDialog.dismissWithAnimation());
        aDialog.setContentText(mensagem);
        aDialog.show();
    }

    public static void alertaPosetiva(@NonNull Context context, String titulo) {

        new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText(titulo)
                .show();
    }

    public static double isNum(String strNum) {
        String formatado = strNum.replaceAll(",00", "");
        String valor = formatado.replaceAll(Pattern.quote("."), "");
        String valorFinal = valor.replace(",", "");
        return Double.parseDouble(valorFinal);
    }

    public static int currentYear() {
        Calendar calendar = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
        String ano = formatter.format(calendar.getTime());
        return Integer.parseInt(ano);
    }

    public static int currentDay() {
        Calendar calendar = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat formatter = new SimpleDateFormat("dd");
        String ano = formatter.format(calendar.getTime());
        return Integer.parseInt(ano);
    }

    public static String currentMonth() {
        Date data = new Date();
        Locale local = new Locale("pt", "PT");
        DateFormat df = new SimpleDateFormat("MMMM", local);
        return df.format(data);
    }

    public static boolean isValidEmail(String email) {
        return Pattern.compile("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$").matcher(email).matches();

    }
}