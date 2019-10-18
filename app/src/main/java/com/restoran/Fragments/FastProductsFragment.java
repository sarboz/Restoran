package com.restoran.Fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.google.gson.Gson;
import com.news.restoran.R;
import com.restoran.Interfase.Api;
import com.restoran.Interfase.IProduct;
import com.restoran.Models.Products;
import com.restoran.adapter.DialogRecyclerAdapter;
import com.restoran.adapter.ProductsRecyclerAdapter;
import com.restoran.adapter.RecyclerTouchListener;
import com.restoran.adapter.RetrofitClient;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FastProductsFragment extends Fragment implements Callback<Products> {
  private static final String ARG_PARAM1 = "param1";

  SharedPreferences pref;

  static IProduct addProduct;

  public static FastProductsFragment newInstance(IProduct add) {
    FastProductsFragment fragment = new FastProductsFragment();
    addProduct = add;
    Bundle args = new Bundle();
    fragment.setArguments(args);
    return fragment;
  }

  RecyclerView rv_products;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View v = inflater.inflate(R.layout.fragment_category_group, container, false);
    rv_products = v.findViewById(R.id.rv_category);
    pref= PreferenceManager.getDefaultSharedPreferences(getActivity());
    rv_products.setLayoutManager(new GridLayoutManager(getActivity(), Integer.parseInt(pref.getString("product_count","2"))));

    Api api = RetrofitClient.getApiWithCache(getActivity());

    Call<Products> call = api.getFastProducts();
    call.enqueue(this);
    return v;
  }

  ArrayList<Products.Product> products = new ArrayList<>();
  Gson g = new Gson();

  @Override
  public void onResponse(Call<Products> call, Response<Products> response) {

    products.addAll(response.body().getProducts());
    rv_products.setAdapter(new ProductsRecyclerAdapter(products));
    rv_products.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), rv_products, new RecyclerTouchListener.ClickListener() {
      @Override
      public void onClick(View view, final int pos) {
        final Products.Product p = products.get(pos);
        if (p.getChast().size() == 0 && p.getVesovoy()==0) {

          addProduct.OnSelectProduct(p);

        } else if (p.getChast().size() >= 1) {
          final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
          LayoutInflater inflater = getActivity().getLayoutInflater();
          final View convertView = inflater.inflate(R.layout.dialog, null);
          alertDialog.setView(convertView);
          final RecyclerView list = convertView.findViewById(R.id.rv_list);

          list.setLayoutManager(new GridLayoutManager(getActivity(), Integer.parseInt(pref.getString("chast_count_in_line","2"))));

          DialogRecyclerAdapter adapter = new DialogRecyclerAdapter(p.getChast());
          list.setAdapter(adapter);
          final AlertDialog ad = alertDialog.show();

          list.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), list, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {

              Products.Product.Chast ch = products.get(pos).getChast().get(position);
              ad.dismiss();
              addProduct.OnSelectProduct(p, ch);

            }
          }));
        }
        else if(p.getVesovoy()>0){
          final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
          LayoutInflater inflater = getActivity().getLayoutInflater();
          final View convertView = inflater.inflate(R.layout.dialog_vesovoy, null);
          alertDialog.setView(convertView);
          final EditText gramm=convertView.findViewById(R.id.gramm);

          gramm.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
              gramm.post(new Runnable() {
                @Override
                public void run() {
                  InputMethodManager inputMethodManager= (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                  inputMethodManager.showSoftInput(gramm, InputMethodManager.SHOW_IMPLICIT);
                }
              });
            }
          });

          alertDialog.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
              int g= Integer.parseInt(gramm.getText().toString());
              Products.Product p = products.get(pos);

              addProduct.OnSelectProduct(p,g);

            }
          });
          final AlertDialog ad = alertDialog.show();
          gramm.requestFocus();
        }
      }
    }));
  }

  @Override
  public void onFailure(Call<Products> call, Throwable t) {

  }

}
