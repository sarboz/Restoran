package com.restoran.Fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.news.restoran.R;
import com.restoran.Interfase.IProduct;
import com.restoran.Interfase.PodCatalog;
import com.restoran.Models.Catecories;
import com.restoran.adapter.CategoriesRecyclerAdapter;
import com.restoran.adapter.PodCategoryRecyclerAdapter;
import com.restoran.adapter.RecyclerTouchListener;
import com.restoran.adapter.RetrofitClient;
import com.restoran.Interfase.Api;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PodCatalogFragment extends Fragment implements Callback<Catecories> {
    private static final String ARG_PARAM = "param1";

    public PodCatalogFragment() {
    }

    RecyclerView rv_pogcatalog;
    List<Catecories.Cat> catecories;
    static PodCatalog podCatalogClick;

    public static PodCatalogFragment newInstance(String param1, PodCatalog add) {
        PodCatalogFragment fragment = new PodCatalogFragment();
        Bundle args = new Bundle();
        podCatalogClick=add;
        args.putString(ARG_PARAM, param1);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_category_group, container, false);
        rv_pogcatalog = v.findViewById(R.id.rv_category);
        rv_pogcatalog.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        String id = getArguments().getString(ARG_PARAM);

        Api api = RetrofitClient.getApiWithCache(getActivity());

        Call<Catecories> call = api.getPodCatecories(id);
        call.enqueue(this);

        return v;
    }


    @Override
    public void onResponse(Call<Catecories> call, Response<Catecories> response) {
        catecories = response.body().getCat();
        rv_pogcatalog.setAdapter(new PodCategoryRecyclerAdapter(catecories));

        rv_pogcatalog.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), rv_pogcatalog, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Catecories.Cat c = catecories.get(position);
                Toast.makeText(getActivity(), c.getName(), Toast.LENGTH_SHORT).show();
                podCatalogClick.OnPodCatCall(c.getId(),0);
            }
        }));
    }

    @Override
    public void onFailure(Call<Catecories> call, Throwable t) {
    }
}
