package com.restoran.Fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.preference.PreferenceManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.news.restoran.R;
import com.restoran.Models.Catecories;
import com.restoran.adapter.CategoriesRecyclerAdapter;
import com.restoran.adapter.RecyclerTouchListener;
import com.restoran.adapter.RetrofitClient;
import com.restoran.Interfase.Api;
import com.restoran.Interfase.PodCatalog;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CategoryGroupFragment extends Fragment implements Callback<Catecories> {

  RecyclerView rv_category;
  List<Catecories.Cat> catecories;
  SharedPreferences pref;
  public CategoryGroupFragment() {
  }

  static PodCatalog mpCatalog;

  public static CategoryGroupFragment newInstance(PodCatalog p) {
    CategoryGroupFragment fragment = new CategoryGroupFragment();
    mpCatalog = p;

    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    pref=PreferenceManager.getDefaultSharedPreferences(getActivity());
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {

    View v = inflater.inflate(R.layout.fragment_category_group, container, false);
    rv_category = v.findViewById(R.id.rv_category);
    rv_category.setLayoutManager(new GridLayoutManager(getActivity(), Integer.parseInt(pref.getString("count_in_line","2"))));

    Api api = RetrofitClient.getApiWithCacheCategory(getActivity());

    Call<Catecories> call = api.getCatecories();
    call.enqueue(this);

    return v;
  }

  // дар ин метод категорияҳо аз сервер гирифта шуда ба RecyclerView  мебарорад дар ҳолати
  // пахши элементҳо ба интерфейси подкаталог id ва ҳолати доштани подкатолог фиристода мешаваад.
  @Override
  public void onResponse(Call<Catecories> call, Response<Catecories> response) {
    catecories = response.body().getCat();
    rv_category.setAdapter(new CategoriesRecyclerAdapter(response.body().getCat()));
    rv_category.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), rv_category, new RecyclerTouchListener.ClickListener() {
      @Override
      public void onClick(View view, int position) {
        Catecories.Cat c = catecories.get(position);
        mpCatalog.OnPodCatCall(c.getId(), c.getCount());
      }

    }));
  }

  @Override
  public void onFailure(Call<Catecories> call, Throwable t) {

  }
}
