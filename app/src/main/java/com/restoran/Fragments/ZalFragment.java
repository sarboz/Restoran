package com.restoran.Fragments;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.news.restoran.R;
import com.restoran.CategoryActivity;
import com.restoran.Interfase.Api;
import com.restoran.Models.ZalStol;
import com.restoran.adapter.RecyclerTouchListener;
import com.restoran.adapter.RetrofitClient;
import com.restoran.adapter.StolRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.NOTIFICATION_SERVICE;

public class ZalFragment extends Fragment implements Callback<ZalStol> {

    private static final String ARG_SECTION_NAME = "section_name";

    public ZalFragment() {
    }

    SharedPreferences pref;
    RecyclerView rv_list;
    List<ZalStol.Stol> stol = new ArrayList<>();
    StolRecyclerAdapter stolAdapter;

    public static ZalFragment newInstance(int sectionNumber, String zal, String id_zal, List<ZalStol.Stol> l, String id) {
        ZalFragment fragment = new ZalFragment();
        fragment.stol = l;
        Bundle args = new Bundle();
        args.putString(ARG_SECTION_NAME, zal);
        args.putString("id", id);
        args.putString("id_zal", id_zal);
        args.putInt("pos", sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        rv_list = (RecyclerView) rootView.findViewById(R.id.rv_list);
        pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        rv_list.setLayoutManager(new GridLayoutManager(getContext(), Integer.parseInt(pref.getString("stol_count", "2"))));
        if (v == null) {
            v = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
        }
        Api api = RetrofitClient.getApi();
        Call<ZalStol> call = api.getZals();
        call.enqueue(this);


        rv_list.addOnItemTouchListener(new RecyclerTouchListener(getContext(), rv_list, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                ZalStol.Stol s = stol.get(position);
                final Intent i = rvClick(s);
                if (!s.isStatus()) {
                    final AlertDialog.Builder d = new AlertDialog.Builder(getActivity());
                    LayoutInflater inflater = getLayoutInflater();
                    final View convertView = inflater.inflate(R.layout.number_picker_stol, null);

                    final NumberPicker np = (NumberPicker) convertView.findViewById(R.id.picker);
                    np.setMaxValue(40);
                    np.setMinValue(11);
                    d.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            int s = np.getValue();
                            i.putExtra("kol_gost", s);
                            startActivity(i);
                        }
                    });
                    d.setView(convertView);
                    final AlertDialog ad = d.show();
                    View.OnClickListener listener = new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Button b = (Button) v;
                            i.putExtra("kol_gost", Integer.parseInt(b.getText().toString()));
                            startActivity(i);
                            ad.dismiss();
                        }
                    };
                    convertView.findViewById(R.id.btn1).setOnClickListener(listener);
                    convertView.findViewById(R.id.btn2).setOnClickListener(listener);
                    convertView.findViewById(R.id.btn3).setOnClickListener(listener);
                    convertView.findViewById(R.id.btn4).setOnClickListener(listener);
                    convertView.findViewById(R.id.btn5).setOnClickListener(listener);
                    convertView.findViewById(R.id.btn6).setOnClickListener(listener);
                    convertView.findViewById(R.id.btn7).setOnClickListener(listener);
                    convertView.findViewById(R.id.btn8).setOnClickListener(listener);
                    convertView.findViewById(R.id.btn9).setOnClickListener(listener);
                    convertView.findViewById(R.id.btn10).setOnClickListener(listener);


                } else if (s.getIdAfitsant().equals(pref.getString("id", "0")))
                    startActivity(i);
                else if (pref.getString("svoiZakazi", "").equals("1") && !s.getIdAfitsant().equals(pref.getString("id", "0"))) {
                    Toast.makeText(getContext(), "Нет доступа", Toast.LENGTH_SHORT).show();
                } else if (pref.getString("svoiZakazi", "").equals("0")) {
                    startActivity(i);
                }
            }
        }));
        return rootView;
    }


    private Intent rvClick(ZalStol.Stol s) {
        final Intent i = new Intent(getContext(), CategoryActivity.class);
        i.putExtra("stolId", s.getId());
        i.putExtra("stolName", s.getName());
        i.putExtra("id_afitsant", getArguments().getString("id"));
        i.putExtra("zal", getArguments().getString(ARG_SECTION_NAME));
        i.putExtra("id_zal", getArguments().getString("id_zal"));
        i.putExtra("kol_gost", 0);
        return i;
    }


    private long[] pattern = {0, 400, 800, 600, 400, 800, 100, 500};
    private Vibrator v = null;

    @Override
    public void onResponse(Call<ZalStol> call, Response<ZalStol> response) {
        this.stol = response.body().getZals().get(getArguments().getInt("pos")).getStol();
        stolAdapter = new StolRecyclerAdapter(stol);
        rv_list.setAdapter(stolAdapter);

        for (int i = 0; i < stol.size(); i++) {
            ZalStol.Stol s = stol.get(i);
            if (s.isVizov() && s.getIdAfitsant().equals(pref.getString("id", ""))) {

                PendingIntent resultPendingIntent = PendingIntent.getActivity(getContext(), 0, rvClick(s),
                        PendingIntent.FLAG_UPDATE_CURRENT);
                NotificationCompat.Builder builder =
                        new NotificationCompat.Builder(getContext())
                                .setSmallIcon(R.mipmap.ic_launcher)
                                .setContentTitle("Муштари Шуморо ҷустуҷӯ дорад.")
                                .setContentText("Шуморо дар столи " + s.getName() + " интизоранд.")
                                .setContentIntent(resultPendingIntent)
                                .setAutoCancel(pref.getBoolean("notifiAuto", false))
                                .setSound(Uri.parse(pref.getString("ringtone", "")))
                                .setChannelId("Notifi");

                Notification notification = builder.build();

                NotificationManager notificationManager =
                        (NotificationManager) getActivity().getSystemService(NOTIFICATION_SERVICE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    int importance = NotificationManager.IMPORTANCE_HIGH;
                    NotificationChannel mChannel = new NotificationChannel("Notifi", getResources().getString(R.string.app_name), importance);
                    notificationManager.createNotificationChannel(mChannel);
                }
                notificationManager.notify(Integer.parseInt(s.getId()), notification);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                    v.vibrate(VibrationEffect.createWaveform(pattern, VibrationEffect.DEFAULT_AMPLITUDE));
                else {
                    v.vibrate(pattern, -1);
                }
            }
        }
    }

    @Override
    public void onFailure(Call<ZalStol> call, Throwable t) {

    }

}

