package com.restoran.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;

import com.news.restoran.R;
import com.restoran.CategoryActivity;
import com.restoran.Models.ZalStol;
import com.restoran.adapter.RecyclerTouchListener;
import com.restoran.adapter.StolRecyclerAdapter;

import java.util.List;

public class ZalFragment extends Fragment {


    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String ARG_SECTION_NAME = "section_name";
    private static boolean isfirst = false;

    public ZalFragment() {
    }

    SharedPreferences pref;
    RecyclerView rv_list;
    List<ZalStol.Stol> stol;
    StolRecyclerAdapter stolAdapter;

    public static ZalFragment newInstance(int sectionNumber, String zal, String id_zal, List<ZalStol.Stol> l, String id) {
        ZalFragment fragment = new ZalFragment();
        fragment.stol = l;
        Bundle args = new Bundle();
        args.putString(ARG_SECTION_NAME, zal);
        args.putString("id", id);
        args.putString("id_zal", id_zal);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        rv_list = (RecyclerView) rootView.findViewById(R.id.rv_list);
        pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
       // stol.get(1).setStatus(true);
        rv_list.setLayoutManager(new GridLayoutManager(getContext(), Integer.parseInt(pref.getString("stol_count_in_line", "2"))));
        stolAdapter = new StolRecyclerAdapter(stol);
        rv_list.setAdapter(stolAdapter);
        rv_list.addOnItemTouchListener(new RecyclerTouchListener(getContext(), rv_list, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                ZalStol.Stol s = stol.get(position);
                final Intent i = new Intent(getContext(), CategoryActivity.class);
                i.putExtra("stolId", s.getId());
                i.putExtra("stolName", s.getName());
                i.putExtra("id_afitsant", getArguments().getString("id"));
                i.putExtra("zal", getArguments().getString(ARG_SECTION_NAME));
                i.putExtra("id_zal", getArguments().getString("id_zal"));
                i.putExtra("kol_gost", 0);

                if (s.isStatus()) {
                    final AlertDialog.Builder d = new AlertDialog.Builder(getActivity());
                    LayoutInflater inflater = getLayoutInflater();
                    final View convertView = inflater.inflate(R.layout.number_picker, null);
                    d.setView(convertView);
                    final NumberPicker np = (NumberPicker) convertView.findViewById(R.id.picker);
                    np.setMaxValue(50);
                    np.setMinValue(1);
                    d.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            int s = np.getValue();
                            i.putExtra("kol_gost", s);
                            startActivity(i);
                        }
                    });
                    final AlertDialog ad = d.show();
                } else
                    startActivity(i);
            }
        }));
        return rootView;
    }

}
