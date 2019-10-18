package com.restoran;

import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.news.restoran.R;
import com.restoran.Chat.Client;
import com.restoran.Fragments.CategoryGroupFragment;
import com.restoran.Fragments.FastProductsFragment;
import com.restoran.Fragments.PodCatalogFragment;
import com.restoran.Fragments.ProductsFragment;
import com.restoran.Interfase.Api;
import com.restoran.Interfase.IProduct;
import com.restoran.Interfase.PodCatalog;
import com.restoran.Models.Orders;
import com.restoran.Models.Products;
import com.restoran.adapter.OrderRecyclerAdapter;
import com.restoran.adapter.RecyclerTouchListener;
import com.restoran.adapter.RetrofitClient;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryActivity extends AppCompatActivity implements PodCatalog, IProduct, Callback<Orders>, BottomNavigationView.OnNavigationItemSelectedListener {
    FragmentTransaction transaction;
    CategoryGroupFragment newFragment;
    RecyclerView rv_product;
    OrderRecyclerAdapter orderAdapter;
    List<Orders.Order> orderList = new ArrayList<>();
    DecimalFormat df = new DecimalFormat("#.###");
    List<Orders.Order> komb = new ArrayList<>();
    String id_afisant, id_stol, kol_gost, id_zal;
    Button btn_komb;
    static boolean status = false;
    String id_zakaz = null;
    String kombo_name = "";
    TextView kombo_textView;
    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        Toolbar toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);

        pref = PreferenceManager.getDefaultSharedPreferences(this);
        kombo_textView = (TextView) findViewById(R.id.text_kombo);

        id_afisant = getIntent().getExtras().get("id_afitsant").toString();
        id_stol = getIntent().getExtras().get("stolId").toString();
        kol_gost = getIntent().getExtras().get("kol_gost").toString();
        id_zal = getIntent().getExtras().get("id_zal").toString();

        btn_komb = (Button) findViewById(R.id.komb);
        rv_product = (RecyclerView) findViewById(R.id.rv_product);

        rv_product.setLayoutManager(new GridLayoutManager(this, 1));
        orderAdapter = new OrderRecyclerAdapter(orderList);
        rv_product.setAdapter(orderAdapter);
        newFragment = CategoryGroupFragment.newInstance(this);

        df.setRoundingMode(RoundingMode.CEILING);

        Intent i = getIntent();
        String title = i.getExtras().get("zal").toString().concat(" | " + i.getExtras().getString("stolName"));
        setTitle(title);

        rv_product.addOnItemTouchListener(new RecyclerTouchListener(this, rv_product, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, final int position) {
                final AlertDialog.Builder d = new AlertDialog.Builder(CategoryActivity.this);
                LayoutInflater inflater = getLayoutInflater();
                final View convertView = inflater.inflate(R.layout.number_picker, null);
                d.setView(convertView);
                final NumberPicker np = (NumberPicker) convertView.findViewById(R.id.picker);
                np.setMaxValue(100);
                np.setMinValue(0);
                np.setValue(orderList.get(position).getKol());
                d.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (pref.getBoolean("uroven", false) && orderList.get(position).getPrint() == 1) {
                            changeKol(np, position);
                        } else if (orderList.get(position).getPrint() == 0) {
                            changeKol(np, position);
                        } else {
                            Toast.makeText(getApplication(), "Нет доступа", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
                d.setNegativeButton("Собой", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (orderList.get(position).getVariant().equals("Нет")) {
                            orderList.get(position).setVariant("Да");
                            orderAdapter.notifyDataSetChanged();
                        } else if (orderList.get(position).getVariant().equals("Да")) {
                            orderList.get(position).setVariant("Нет");
                            orderAdapter.notifyDataSetChanged();
                        }
                    }
                });
                final AlertDialog ad = d.show();
            }
        }));

        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        if (kol_gost.equals("0")) {
            Api api = RetrofitClient.getApi();
            Call<Orders> call = api.getzakaz(id_stol);
            call.enqueue(this);
        }
    }

    private void changeKol(NumberPicker np, int position) {
        int kol = orderList.get(position).getKol();
        int s = np.getValue();
        if (s == kol)
            return;
        if (s == 0 && orderList.get(position).getStatus().equals("new")) {
            orderList.remove(position);
            orderAdapter.notifyDataSetChanged();
            rv_product.scrollToPosition(orderList.size() - 1);
            return;
        } else if (s == 0 && orderList.get(position).getStatus().equals("old")) {
            orderList.get(position).setStatus("del");
        }
        double price = 0, narh = 0;
        price = Double.parseDouble(orderList.get(position).getPrice().replace(",", "."));
        narh = (price / kol) * s;
        if (orderList.get(position).getStatus().equals("old")) {
            orderList.get(position).setStatus("upd");
        }
        orderList.get(position).setPrice(df.format(narh));
        orderList.get(position).setKol(s);
        orderAdapter.notifyDataSetChanged();
        rv_product.scrollToPosition(orderList.size() - 1);
    }

    @Override
    protected void onResume() {
        super.onResume();
        categoryFragment();
    }

    // метод барои нишон додани котегориаҳо
    public void categoryFragment() {
        try {
            transaction = getFragmentManager().beginTransaction();
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            transaction.replace(R.id.fragmetCategory, newFragment);
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // Call back метод барои нишондодани фрагменти подкаталог агар категория подкаталог дошта бошад
    @Override
    public void OnPodCatCall(String id, int kol_pod) {
        try {


            transaction = getFragmentManager().beginTransaction();
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            if (kol_pod > 0) {
                PodCatalogFragment pFragment = PodCatalogFragment.newInstance(id, this);
                transaction.replace(R.id.fragmetCategory, pFragment);
                transaction.addToBackStack("PodCatalogFragment");
            } else {
                ProductsFragment productsFragment = ProductsFragment.newInstance(id, this);
                transaction.replace(R.id.fragmetCategory, productsFragment);
                transaction.addToBackStack("ProductsFragment");
            }
            transaction.commit();
        } catch (Exception e) {
        }
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            try {
                getFragmentManager().popBackStack();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            super.onBackPressed();
        }
    }

    /**
     * метод барои иловаи закази наве ки дорои част аст
     */
    @Override
    public void OnSelectProduct(Products.Product p, Products.Product.Chast ch) {

        if (status) {
            for (int i = 0; i < komb.size(); i++) {
                if (komb.get(i).getKombo().size() == 0 && komb.get(i).getIdProduct().equals(p.getId())
                        && komb.get(i).getType().equals(p.getType())
                        && komb.get(i).getIdChast().equals(ch.getId())) {

                    komb.get(i).setKol(komb.get(i).getKol() + 1);
                    komb.get(i).setPrice(String.valueOf(Double.parseDouble(komb.get(i).getPrice().replace(",", ".")) + Double.parseDouble(ch.getPrice().replace(",", "."))));
                    kombo_name = "";
                    for (int j = 0; j < komb.size(); j++) {
                        kombo_name += " " + komb.get(j).getName() + " x " + komb.get(j).getKol() + ",";
                        kombo_textView.setText(kombo_name);
                    }
                    return;
                }
            }

            Orders.Order o = new Orders.Order();
            o.setIdProduct(p.getId());
            o.setName(p.getName() + " " + ch.getName());
            o.setKol(1);
            o.setVariant("Нет");
            o.setStatus("new");
            o.setType(p.getType());
            o.setPrice(ch.getPrice());
            o.setIdChast(ch.getId());
            komb.add(o);
            kombo_name = "";
            for (int j = 0; j < komb.size(); j++) {
                kombo_name += " " + komb.get(j).getName() + " x " + komb.get(j).getKol() + ",";
                kombo_textView.setText(kombo_name);
            }
            return;
        }

        for (int i = 0; i < orderList.size(); i++) {
            if (orderList.get(i).getStatus().equals("new") && orderList.get(i).getKombo().size() == 0 && orderList.get(i).getIdProduct().equals(p.getId())
                    && orderList.get(i).getType().equals(p.getType())
                    && orderList.get(i).getIdChast().equals(ch.getId())) {

                orderList.get(i).setKol(orderList.get(i).getKol() + 1);
                orderList.get(i).setPrice(String.valueOf(Double.parseDouble(orderList.get(i).getPrice()) + Double.parseDouble(ch.getPrice())));
                orderAdapter.notifyDataSetChanged();
                rv_product.scrollToPosition(orderList.size() - 1);
                return;
            }
        }

        Orders.Order o = new Orders.Order();
        o.setIdProduct(p.getId());
        o.setName(p.getName() + " " + ch.getName());
        o.setKol(1);
        o.setNarh(ch.getPrice());
        o.setStatus("new");
        o.setVariant("Нет");
        o.setType(p.getType());
        o.setPrice(ch.getPrice());
        o.setIdChast(ch.getId());
        orderList.add(o);
        orderAdapter.notifyDataSetChanged();
        rv_product.scrollToPosition(orderList.size() - 1);
    }

    /**
     * метод дар вақти иловаи хуроки нав кор карда аз fragmet -и product  ҷеғ зада мешавад
     */
    @Override
    public void OnSelectProduct(Products.Product p) {

        if (status) {
            for (int i = 0; i < komb.size(); i++) {

                if (komb.get(i).getIdProduct().equals(p.getId()) && p.getName().equals(komb.get(i).getName()) &&
                        p.getType().equals(komb.get(i).getType())) {
                    komb.get(i).setKol(komb.get(i).getKol() + 1);
                    double narh = Double.parseDouble(komb.get(i).getPrice()) + Double.parseDouble(p.getPrice());
                    komb.get(i).setPrice(String.valueOf(narh));

                    kombo_name = "";
                    for (int j = 0; j < komb.size(); j++) {
                        kombo_name += " " + komb.get(j).getName() + " x " + komb.get(j).getKol() + ",";
                        kombo_textView.setText(kombo_name);
                    }
                    return;
                }
            }
            kombo_name += " " + p.getName() + " x " + 1 + ",";
            kombo_textView.setText(kombo_name);
            Orders.Order o = new Orders.Order();
            o.setIdProduct(p.getId());
            o.setName(p.getName());
            o.setStatus("new");
            o.setKol(1);
            o.setVariant("Нет");
            o.setType(p.getType());
            o.setPrice(p.getPrice());
            komb.add(o);
            kombo_name = "";
            for (int i = 0; i < komb.size(); i++) {
                kombo_name += " " + komb.get(i).getName() + " x " + komb.get(i).getKol() + ",";
                kombo_textView.setText(kombo_name);
            }
            return;
        }
        for (int i = 0; i < orderList.size(); i++) {
            if (orderList.get(i).getStatus().equals("new") && orderList.get(i).getKombo().size() == 0 && orderList.get(i).getIdProduct().equals(p.getId()) && orderList.get(i).getType().equals(p.getType())) {
                orderList.get(i).setKol(orderList.get(i).getKol() + 1);
                orderList.get(i).setPrice(String.valueOf(Double.parseDouble(orderList.get(i).getPrice()) + Double.parseDouble(p.getPrice())));
                orderAdapter.notifyDataSetChanged();
                rv_product.scrollToPosition(orderList.size() - 1);
                return;
            }
        }

        Orders.Order o = new Orders.Order();
        o.setIdProduct(p.getId());
        o.setName(p.getName());
        o.setKol(1);
        o.setNarh(p.getPrice());
        o.setStatus("new");
        o.setVariant("Нет");
        o.setType(p.getType());
        o.setPrice(p.getPrice());
        orderList.add(o);
        orderAdapter.notifyDataSetChanged();
        rv_product.scrollToPosition(orderList.size() - 1);
    }

    /**
     * метод барои иловаи заказе ки дорои грамовка аст
     */
    @Override
    public void OnSelectProduct(Products.Product p, int gram) {
        if (status) {

            for (int i = 0; i < komb.size(); i++) {
                if (komb.get(i).getKombo().size() == 0 && komb.get(i).getIdProduct().equals(p.getId())
                        && komb.get(i).getType().equals(p.getType())
                        && komb.get(i).getGramm() == gram) {
                    komb.get(i).setKol(komb.get(i).getKol() + 1);
                    double sum = (Double.parseDouble(p.getPrice().replace(",", ".")) / 100) * gram;
                    double narh = Double.parseDouble(komb.get(i).getPrice().replace(",", ".")) + sum;
                    komb.get(i).setPrice(df.format(narh));
                    kombo_name = "";
                    for (int j = 0; j < komb.size(); j++) {
                        kombo_name += " " + komb.get(j).getName() + " " + " x " + komb.get(j).getKol();
                        kombo_textView.setText(kombo_name);
                    }
                    return;
                }
            }
            Orders.Order o = new Orders.Order();
            o.setIdProduct(p.getId());
            o.setName(p.getName() + " " + gram + " gr");
            o.setKol(1);
            o.setVariant("Нет");
            o.setStatus("new");
            o.setType(p.getType());
            o.setGramm(gram);
            double narh = (Double.parseDouble(p.getPrice().replace(",", ".")) / 100) * gram;
            o.setPrice(df.format(narh));
            komb.add(o);
            kombo_name = "";
            for (int j = 0; j < komb.size(); j++) {
                kombo_name += " " + komb.get(j).getName() + " x " + komb.get(j).getKol() + ",";
                kombo_textView.setText(kombo_name);
            }
            return;
        }

        for (int i = 0; i < orderList.size(); i++) {
            if (orderList.get(i).getStatus().equals("new") && orderList.get(i).getKombo().size() == 0 && orderList.get(i).getIdProduct().equals(p.getId())
                    && orderList.get(i).getType().equals(p.getType())
                    && orderList.get(i).getGramm() == gram) {
                orderList.get(i).setKol(orderList.get(i).getKol() + 1);
                double sum = (Double.parseDouble(p.getPrice().replace(",", ".")) / 100) * gram;
                double narh = Double.parseDouble(orderList.get(i).getPrice().replace(",", ".")) + sum;
                orderList.get(i).setPrice(df.format(narh));
                orderAdapter.notifyDataSetChanged();
                rv_product.scrollToPosition(orderList.size() - 1);
                return;
            }
        }

        Orders.Order o = new Orders.Order();
        o.setIdProduct(p.getId());
        o.setName(p.getName() + " " + gram + " gr");
        o.setKol(1);
        o.setVariant("Нет");
        o.setType(p.getType());
        o.setNarh(p.getPrice());
        o.setGramm(gram);
        double narh = (Double.parseDouble(p.getPrice().replace(",", ".")) / 100) * gram;
        o.setStatus("new");
        o.setPrice(df.format(narh));
        orderList.add(o);
        orderAdapter.notifyDataSetChanged();
        rv_product.scrollToPosition(orderList.size() - 1);
    }

    /**
     * метод барои комбинатсия
     */
    public void btn_kombClick(View view) {
        Button b = (Button) view;
        if (!status) {
            kombo_textView.setText("");
            komb = new ArrayList<>();
            b.setText("готово");
            b.setBackgroundResource(android.R.color.holo_green_dark);
            status = true;
        } else {
            kombo_name = "";
            kombo_textView.setText("");
            double summ = 0;
            for (int i = 0; i < komb.size(); i++) {
                summ += Double.parseDouble(komb.get(i).getPrice().replace(",", "."));
            }
            Orders.Order or = new Orders.Order();
            or.setKombo(komb);
            or.setKol(1);
            or.setVariant("Нет");
            or.setStatus("new");
            or.setPrice(String.valueOf(df.format(summ)));
            String name = "";
            for (int i = 0; i < komb.size(); i++) {
                name += komb.get(i).getName() + " x " + komb.get(i).getKol() + "| ";
            }
            or.setName(name);

            if (komb.size() > 0) {
                orderList.add(or);
            }
            b.setText("Комб");
            b.setBackgroundResource(android.R.color.darker_gray);
            status = false;
        }
    }

    /**
     * метод дар барои гирифтани заказҳо аз сервер барои столи интихобшуда
     */
    @Override
    public void onResponse(Call<Orders> call, Response<Orders> response) {
        id_zakaz = response.body().getId_zakaz();
        orderList.addAll(response.body().getOrderList());
        orderAdapter.notifyDataSetChanged();
        rv_product.scrollToPosition(orderList.size() - 1);

    }

    @Override
    public void onFailure(Call<Orders> call, Throwable t) {
    }

    public void btn_fastPrroducts(View view) {
        transaction = getFragmentManager().beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);

        FastProductsFragment fastproductsFragment = FastProductsFragment.newInstance(this);
        transaction.replace(R.id.fragmetCategory, fastproductsFragment);
        transaction.addToBackStack("fastProductsFragment");
        transaction.commit();

    }

    public void btn_all(View view) {
        getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        transaction = getFragmentManager().beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.replace(R.id.fragmetCategory, newFragment);
        transaction.addToBackStack("All");
        transaction.commit();
    }

    boolean sech = false;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_back:
                onBackPressed();
                break;
            case R.id.action_dial:
                if (status)
                    btn_komb.performClick();
                sech = true;
                zakaz();
                break;
            case R.id.action_mail:
                if (status)
                    btn_komb.performClick();
                zakaz();
                break;
        }
        return false;
    }

    private void zakaz() {
        Api api = RetrofitClient.getApi();


        Orders o = new Orders();
        o.setId_zakaz(id_zakaz);
        o.setOrderList(orderList);
        o.setId_afisant(id_afisant);
        o.setId_zal(id_zal);
        o.setIdStol(id_stol);
        o.setKol_gost(kol_gost);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (new Gson().toJson(o)));
        if (id_zakaz == null) {
            api.addZakaz(body).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {

                        if (MainActivity.conStatus) {
                            Toast.makeText(getApplicationContext(), "ok Add", Toast.LENGTH_SHORT).show();
                            Client client = Client.getInstance();
                            client.Send("update");
                            id_zakaz = response.body().string();
                            if (sech) {
                                client.Send("sech:" + id_zakaz);
                            }
                            finish();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Нет Подключение", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "error: ", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            api.updateZakaz(body).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (MainActivity.conStatus) {
                        Toast.makeText(getApplicationContext(), "ok Update", Toast.LENGTH_SHORT).show();
                        if (sech) {
                            Client client = Client.getInstance();
                            client.Send("sech:" + id_zakaz);
                        }
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "Нет Подключение", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "error: ", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
