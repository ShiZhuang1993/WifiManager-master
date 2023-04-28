package com.iwdael.wifimanager.example;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;

import com.iwdael.wifimanager.IWifi;
import com.iwdael.wifimanager.IWifiManager;
import com.iwdael.wifimanager.OnWifiChangeListener;
import com.iwdael.wifimanager.OnWifiConnectListener;
import com.iwdael.wifimanager.State;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class WifiActivity extends AppCompatActivity {

    private IWifiManager manager;
    private MyAdapter adapter;
    static final String TAG_FRAG = "InputWiFiPasswordDialog";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mianactivity);
        RecyclerView recyclerView = findViewById(R.id.my_list_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        manager = WifiManager.create(this);
        manager.setOnWifiConnectListener(new OnWifiConnectListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onConnectChanged(boolean status) {
                manager.scanWifi();
            }
        });
        manager.setOnWifiStateChangeListener(state -> {
            //监听状态
            if (state.equals(State.DISABLED)) {
            } else if (state.equals(State.ENABLED)) {
            }
        });
        manager.setOnWifiChangeListener(new OnWifiChangeListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onWifiChanged(List<IWifi> wifis) {
                //Log.e("===========", wifis.toString());
                if (adapter == null) {
                    adapter = new MyAdapter(wifis);
                    recyclerView.setAdapter(adapter);
                } else {
                    adapter.notifyDataSetChanged();
                }
                adapter.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        IWifi iWifi = wifis.get(position);
                        if (iWifi.isConnected()) {
                            showDelDialog(iWifi);
                            adapter.notifyDataSetChanged();
                            return;
                        }
                        if (TextUtils.isEmpty(iWifi.encryption())) {
                            manager.connectOpenWifi(iWifi);
                            return;
                        }
                        if (iWifi.isSaved()) {
                            manager.connectSavedWifi(iWifi);
                        } else {
                            InputWiFiPasswordDialog dialog = new InputWiFiPasswordDialog();
                            Bundle bundle = new Bundle();
                            bundle.putParcelable("info", iWifi);
                            dialog.setArguments(bundle);
                            dialog.show(getSupportFragmentManager(), TAG_FRAG);
                        }
                    }
                });
            }
        });
    }

    /**
     * 删除wifi
     */
    void showDelDialog(final IWifi info) {
        RemoveDialog.showTipDialog(this, "确定要删除" + "\"" + info.name() + "\"？",
                "删除后需要重新输入密码",
                v -> manager.removeWifi(info)
        );
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (manager != null) {
            manager.destroy();
        }
    }
}
