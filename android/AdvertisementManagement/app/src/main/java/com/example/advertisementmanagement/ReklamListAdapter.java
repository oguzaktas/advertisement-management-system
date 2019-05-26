package com.example.advertisementmanagement;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Yazilim Laboratuvari II Proje 3
 * @author Oguz Aktas & Mert Var
 */
public class ReklamListAdapter extends ArrayAdapter<Reklam> {

    private static final String TAG = "ReklamListAdapter";

    private Context mContext;
    private int mResource;

    public ReklamListAdapter(Context context, int resource, ArrayList<Reklam> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String firmaAdi = getItem(position).getFirmaAdi();
        String kategori = getItem(position).getKategori();
        String kampanyaIcerik = getItem(position).getKampanyaIcerik();
        String kampanyaSuresi = getItem(position).getKampanyaSuresi();

        Reklam reklam = new Reklam(firmaAdi, kategori, kampanyaIcerik, kampanyaSuresi);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView tvFirmaAdi = (TextView) convertView.findViewById(R.id.firmaAdi);
        TextView tvKategori = (TextView) convertView.findViewById(R.id.kategori);
        TextView tvKampanyaIcerik = (TextView) convertView.findViewById(R.id.kampanyaIcerik);
        TextView tvKampanyaSuresi = (TextView) convertView.findViewById(R.id.kampanyaSuresi);

        tvFirmaAdi.setText(firmaAdi);
        tvKategori.setText(kategori);
        tvKampanyaIcerik.setText(kampanyaIcerik);
        tvKampanyaSuresi.setText("Kampanya suresi: " + kampanyaSuresi);

        return convertView;
    }
}
