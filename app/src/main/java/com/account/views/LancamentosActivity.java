package com.account.views;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.account.R;
import com.account.views.fragments.DespesasFragment;
import com.account.views.fragments.ReceitasFragment;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.parse.ParseException;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.text.NumberFormat;
import java.util.Objects;

import broadcast.NetworkCheack;
import controllers.MonthController;
import controllers.ParseListenerEquilibrio;


@EActivity(R.layout.activity_lancamentos)

public class LancamentosActivity extends AppCompatActivity {

    @ViewById(R.id.mToolbar_lancamentos)
    MaterialToolbar materialToolbar;

    @ViewById(R.id.tabs)
    TabLayout tabLayout;

    @ViewById(R.id.semi_transparent)
    FloatingActionsMenu actionsMenu;

    @ViewById(R.id.mT_money_lancamento)
    TextView currentMoney;

    @ViewById(R.id.viewpager)
    ViewPager viewPager;
    private String data;
    private NetworkCheack networkCheack;
    private boolean backPressToExit = false;

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

    @AfterViews
    void init() {

        networkCheack = new NetworkCheack();

        viewPager.addOnPageChangeListener((new TabLayout.TabLayoutOnPageChangeListener(tabLayout)));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));
        tabLayout.setupWithViewPager(viewPager);
        // init the bottom sheet behavior
        materialToolbar.setTitle(getResources().getString(R.string.lancamentos));
        setSupportActionBar(materialToolbar);
        Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(R.drawable.ic_arrow_back);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        materialToolbar.setNavigationOnClickListener(v -> startActivity(new Intent(this, MainActivity_.class)));

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
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
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
        Snackbar.make(findViewById(R.id.activity_lancamentos), getString(R.string.sair_snackbar), Snackbar.LENGTH_SHORT).show();
        new Handler().postDelayed(() -> backPressToExit = false, 5000);

    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onStart() {
        super.onStart();

        if (networkCheack.isOnline(getBaseContext())) {
            setupViewPager(viewPager);

            MonthController controller = new MonthController(this);
            controller.situacaoMensalCorrente(new ParseListenerEquilibrio() {
                @Override
                public void onDataRetrievedMoney(Double receita, Double despesa, Double equilibrio) {
                    currentMoney.setText(NumberFormat.getCurrencyInstance()
                            .format(equilibrio).replace("€", ""));
                }

                @Override
                public void onDataRetrieveFail(ParseException e) {

                }
            });
        } else {
            Snackbar.make(findViewById(R.id.activity_lancamentos), "Connecta-te a internet", Snackbar.LENGTH_LONG).show();
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new DespesasFragment(), "DESPESAS");
        adapter.addFrag(new ReceitasFragment(), "RECEITAS");
        viewPager.setAdapter(adapter);
    }

    public String data(String data) {
        return this.data = data;
    }

    public String getData() {
        return this.data;
    }

}
