package com.iwdael.wifimanager.example;

import android.graphics.Color;
import android.net.wifi.WifiManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.iwdael.wifimanager.IWifi;
import com.iwdael.wifimanager.Wifi;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private List<IWifi> dataList;
    private ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public MyAdapter(List<IWifi> dataList) {
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_custom_list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setOnClickListener(v -> {
            if (itemClickListener != null) {
                int position = viewHolder.getAdapterPosition();
                itemClickListener.onItemClick(position);
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        IWifi iWifi = dataList.get(position);
        int level = WifiManager.calculateSignalLevel(iWifi.level(), 4) + 1;
        if (!TextUtils.isEmpty(iWifi.encryption())) {
            holder.iv_password.setVisibility(View.VISIBLE);
            holder.iv_password.setImageResource(R.drawable.ic_password);
        } else {
            holder.iv_password.setVisibility(View.GONE);
        }
        if (iWifi.isConnected()) {
            holder.nameView.setTextColor(Color.parseColor("#B03180"));
            holder.iv_connected.setVisibility(View.VISIBLE);
        } else {
            holder.iv_connected.setVisibility(View.GONE);
            holder.nameView.setTextColor(Color.parseColor("#191f25"));
        }

        String state = iWifi.state();
        if (state != null) {
            holder.genderView.setText(state);
        }

        switch (level) {
            case 1:
                holder.iv_wifi.setImageResource(R.drawable.ic_wifi_1);
                break;
            case 2:
                holder.iv_wifi.setImageResource(R.drawable.ic_wifi_2);
                break;
            case 3:
                holder.iv_wifi.setImageResource(R.drawable.ic_wifi_3);
                break;
            default:
                holder.iv_wifi.setImageResource(R.drawable.ic_wifi);
                break;
        }
        holder.nameView.setText(iWifi.name());

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameView;
        TextView genderView;
        ImageView iv_wifi, iv_password, iv_connected;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameView = itemView.findViewById(R.id.name_view);
            genderView = itemView.findViewById(R.id.gender_view);
            iv_wifi = itemView.findViewById(R.id.iv_wifi);
            iv_password = itemView.findViewById(R.id.iv_password);
            iv_connected = itemView.findViewById(R.id.iv_connected);
        }
    }
}
