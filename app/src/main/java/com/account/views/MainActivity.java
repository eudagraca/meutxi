package com.account.views;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.account.R;
import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;
import com.anychart.enums.Align;
import com.anychart.enums.LegendLayout;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.snackbar.Snackbar;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mikepenz.materialdrawer.util.DrawerImageLoader;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import broadcast.NetworkCheack;
import controllers.MonthController;
import controllers.ParseListenerDespesa;
import controllers.ParseListenerEquilibrio;
import controllers.ParseListenerMoney;
import models.Categorias;
import models.Despesa;

@SuppressLint("Registered")
@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {

    @ViewById(R.id.mToolbar)
    MaterialToolbar mToolbar;

    @ViewById(R.id.ll_graph)
    LinearLayout llGraph;

    @ViewById(R.id.ll_empty)
    LinearLayout llEmpty;

    @ViewById(R.id.tv_money_despesa)
    TextView tv_despesa;

    @ViewById(R.id.tv_money_equilibrio)
    TextView tv_equilibrio;

    @ViewById(R.id.mT_money_view)
    TextView tv_receita;

    @ViewById(R.id.chart)
    AnyChartView anyChart;

    @ViewById(R.id.ll_saldo)
    MaterialCardView card1;

    @ViewById(R.id.cv2)
    MaterialCardView card2;

    @ViewById(R.id.cvWallet)
    MaterialCardView mCardView;

    @ViewById(R.id.money)
    TextView mMoney;

    @ViewById(R.id.ll_empty2)
    LinearLayout mllEmpty;
    @ViewById(R.id.floating_buttom)
    FloatingActionsMenu actionsMenu;
    private ProgressDialog progressDialog;
    private Pie pie;
    @NonNull
    private List<DataEntry> dataEntries = new ArrayList<>();
    private ParseUser parse;
    private NetworkCheack networkCheack;
    private boolean backPressToExit = false;
    private double auxiliaryCash;

    @Click(R.id.fb_despesas)
    void despesas() {
        Intent intent = new Intent(this, RegisterMonthActivity_.class);
        intent.putExtra("situacao", -1);
        startActivity(intent);
        actionsMenu.collapse();
    }

    @Click(R.id.fb_receitas)
    void receitas() {
        Intent intent = new Intent(this, RegisterMonthActivity_.class);
        intent.putExtra("situacao", +1);
        startActivity(intent);
        actionsMenu.collapse();
    }

    @Click(R.id.showValues)
    void showValues() {
        Intent intent = new Intent(this, ReportsActivity.class);
        startActivity(intent);
        finish();
        actionsMenu.collapse();
    }

    @Click(R.id.mostrar)
    void mostrarMais() {
        Intent intent = new Intent(this, LancamentosActivity_.class);
        startActivity(intent);
        finish();
        actionsMenu.collapse();
    }

    @AfterViews
    void init() {
        parse = ParseUser.getCurrentUser();
        progressDialog = new ProgressDialog(MainActivity.this);

        networkCheack = new NetworkCheack();

        pie = AnyChart.pie();
        mToolbar.setTitle("Início");
        setSupportActionBar(mToolbar);

        String photo = Objects.requireNonNull(parse.getParseFile("photo")).getUrl();

        SharedPreferences sharedPref = getSharedPreferences("user.Preferences", 0);
        String email = sharedPref.getString("userEmail", null);

        DrawerImageLoader.init(new DrawerImageLoader.IDrawerImageLoader() {
            @Override
            public void set(ImageView imageView, Uri uri, Drawable placeholder) {
                Picasso.get().load(Objects.requireNonNull(parse.getParseFile("photo")).getUrl()).into(imageView);
            }

            @Override
            public void set(ImageView imageView, Uri uri, Drawable placeholder, String tag) {
                Picasso.get().load(Objects.requireNonNull(parse.getParseFile("photo")).getUrl()).into(imageView);
            }

            @Override
            public void cancel(ImageView imageView) {

            }

            @Nullable
            @Override
            public Drawable placeholder(Context ctx) {
                return null;
            }

            @Nullable
            @Override
            public Drawable placeholder(Context ctx, String tag) {
                return null;
            }
        });

        // Create AccountHeader
        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withProfileImagesVisible(true)
                .withTranslucentStatusBar(true)
                .withHeaderBackground(R.color.colorPrimary)
                .withTextColor(getResources().getColor(R.color.white))
                .withTypeface(Typeface.defaultFromStyle(Typeface.BOLD))
                .addProfiles(new ProfileDrawerItem()
                        .withEmail(email)
                        .withName(parse.getUsername())
                        .withIcon(photo)).withOnAccountHeaderProfileImageListener(new AccountHeader.OnAccountHeaderProfileImageListener() {
                    @Override
                    public boolean onProfileImageClick(View view, IProfile profile, boolean current) {
                        startActivity(new Intent(getBaseContext(), PerfilActivity_.class));
                        return false;
                    }

                    @Override
                    public boolean onProfileImageLongClick(View view, IProfile profile, boolean current) {
                        return false;
                    }
                })
                .withOnAccountHeaderListener((view, profile, currentProfile) -> true)
                .build();

        SecondaryDrawerItem item1 = new SecondaryDrawerItem().withName("Início")
                .withTypeface(Typeface.defaultFromStyle(Typeface.MONOSPACE.getStyle()))
                .withIconColor(getResources().getColor(R.color.colorPrimary))
                .withIcon(GoogleMaterial.Icon.gmd_account_balance).withOnDrawerItemClickListener((view, position, drawerItem) -> {
                    startActivity(new Intent(getBaseContext(), MainActivity_.class));
                    actionsMenu.collapseImmediately();
                    return false;
                });

        SecondaryDrawerItem lancamentos = new SecondaryDrawerItem().withName("Lançamentos")
                .withIconColor(getResources().getColor(R.color.colorPrimary))
                .withTypeface(Typeface.defaultFromStyle(Typeface.SERIF.getStyle()))
                .withIcon(FontAwesome.Icon.faw_money).withOnDrawerItemClickListener((view, position, drawerItem) -> {
                    startActivity(new Intent(getBaseContext(), LancamentosActivity_.class));
                    return false;
                });

        SecondaryDrawerItem balanco = new SecondaryDrawerItem().withName("Balanço Mensal")
                .withIconColor(getResources().getColor(R.color.colorPrimary))
                .withTypeface(Typeface.defaultFromStyle(Typeface.SERIF.getStyle()))
                .withIcon(FontAwesome.Icon.faw_balance_scale).withOnDrawerItemClickListener((view, position, drawerItem) -> {
                    startActivity(new Intent(getBaseContext(), ReportsActivity.class));
                    return false;
                });

        SecondaryDrawerItem about = new SecondaryDrawerItem().withName("About us")
                .withIconColor(getResources().getColor(R.color.colorPrimary))
                .withTypeface(Typeface.defaultFromStyle(Typeface.SERIF.getStyle()))
                .withIcon(FontAwesome.Icon.faw_list_alt).withOnDrawerItemClickListener(
                        (view, position, drawerItem) -> false);

        Drawer result = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(mToolbar)
                .withAccountHeader(headerResult)
                .withActionBarDrawerToggleAnimated(true)
                .withSelectedItem(0)
                .addDrawerItems(item1,
                        lancamentos,
                        balanco,
                        new DividerDrawerItem(),
                        about,
                        new DividerDrawerItem()
                ).withOnDrawerNavigationListener(clickedView -> false).build();
        result.addStickyFooterItem(new SecondaryDrawerItem()
                .withTypeface(Typeface.defaultFromStyle(Typeface.SANS_SERIF.getStyle()))
                .withIconColor(getResources().getColor(R.color.md_red_700))
                .withIcon(FontAwesome.Icon.faw_code)
                .withName("we love code"));

        actionsMenu.setOnFloatingActionsMenuUpdateListener(new FloatingActionsMenu.OnFloatingActionsMenuUpdateListener() {
            @Override
            public void onMenuExpanded() {
                actionsMenu.setBackgroundColor(getResources().getColor(R.color.transp));
            }

            @Override
            public void onMenuCollapsed() {
                actionsMenu.setBackgroundColor(getResources().getColor(R.color.float_transparent));
            }
        });
    }

    @Override
    public void onBackPressed() {

        if (backPressToExit) {
            Intent homeScreenIntent = new Intent(Intent.ACTION_MAIN);
            homeScreenIntent.addCategory(Intent.CATEGORY_HOME);
            homeScreenIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(homeScreenIntent);
        }
        this.backPressToExit = true;
        Snackbar.make(findViewById(R.id.content_main), getString(R.string.sair_snackbar), Snackbar.LENGTH_SHORT).show();
        new Handler().postDelayed(() -> backPressToExit = false, 1000);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (networkCheack.isOnline(getBaseContext())) {

            MonthController controller = new MonthController(this);
            progressDialog.setMessage("Carregando registos");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setCancelable(false);
            progressDialog.show();

            ScrollView scrollView = findViewById(R.id.scroll);

            scrollView.setVisibility(View.GONE);

            controller.myCash(new ParseListenerMoney() {
                @SuppressLint("SetTextI18n")
                @Override
                public void moneyOnWallet(List<Double> mCash) {
                    double finalCah = 0;
                    for (double cash : mCash) {
                        finalCah += cash;
                    }
                    if (finalCah < 0) {
                        mMoney.setTextColor(getResources().getColor(R.color.md_red_500));
                    } else {
                        mMoney.setTextColor(getResources().getColor(R.color.md_green_600));
                    }
                    mMoney.setText(finalCah + "0 MZN");

                    auxiliaryCash = finalCah;
                }

                @Override
                public void onDataRetrieveFailed(ParseException e) {

                }
            });

            controller.situacaoMensalCorrente(new ParseListenerEquilibrio() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onDataRetrievedMoney(Double receita, Double despesa, Double equilibrio) {

                    if ((receita+auxiliaryCash) - despesa > 0) {
                        tv_equilibrio.setTextColor(getResources().getColor(R.color.colorPrimary));
                    } else {
                        tv_equilibrio.setTextColor(getResources().getColor(R.color.md_red_500));
                    }

                    tv_equilibrio.setText(NumberFormat.getCurrencyInstance()
                            .format((receita+auxiliaryCash)-despesa).replace("€", ""));
                    tv_despesa.setText(despesa + "0");
                    tv_receita.setText(receita+auxiliaryCash + "0");
                }

                @Override
                public void onDataRetrieveFail(ParseException e) {
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                }
            });

            dataEntries.clear();
            controller.getAllDespesas(new ParseListenerDespesa() {
                @Override
                public void onDataRetrievedDespesas(List<Despesa> despesas) {
                    if (despesas.size() == 0) {
                        llEmpty.setVisibility(View.VISIBLE);
                        llGraph.setVisibility(View.GONE);
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                    } else {
                        llEmpty.setVisibility(View.GONE);
                        llGraph.setVisibility(View.VISIBLE);

                        List<Despesa> myExpenses = new ArrayList<>();
                        dataEntries.clear();
                        for (Despesa entry : despesas) {
                            Despesa despesa = new Despesa();
                            Categorias categorias = new Categorias();
                            categorias.setName(entry.getCategoria().getName());
                            despesa.setCategoria(categorias);
                            despesa.setValor(entry.getValor());
                            myExpenses.add(despesa);
                            dataEntries.add(new ValueDataEntry(despesa.getCategoria().getName(), despesa.getValor()));
                        }
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        pie.data(dataEntries);
                    }
                    scrollView.setVisibility(View.VISIBLE);
                }

                @Override
                public void onDataRetrieveFail(ParseException e) {

                }
            });

            pie.title(getResources().getString(R.string.mes_corrente));
            pie.labels().position("outside");
            pie.legend()
                    .position("center-bottom")
                    .itemsLayout(LegendLayout.HORIZONTAL)
                    .align(Align.CENTER);
            anyChart.setChart(pie);
        } else {
            Snackbar.make(findViewById(R.id.content_main), "Connecta-te a internet", Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}