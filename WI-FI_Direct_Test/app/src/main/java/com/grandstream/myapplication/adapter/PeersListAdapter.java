package com.grandstream.myapplication.adapter;

import android.net.wifi.p2p.WifiP2pDevice;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;

import com.grandstream.myapplication.R;
import com.grandstream.myapplication.listener.HolderClickListener;

import java.util.List;

public class PeersListAdapter extends Adapter<PeersListAdapter.PeersListHolder> {

    private static String TAG = "xiangsun";

    private List<WifiP2pDevice> mDevices;
    private HolderClickListener mHolderClickListener;

    private String[] status = new String[] {
            "CONNECTED",
            "INVITED",
            "FAILED",
            "AVAILABLE",
            "UNAVAILABLE"
    };

    public PeersListAdapter(HolderClickListener holderClickListener, List<WifiP2pDevice> devices) {
        mDevices = devices;
        mHolderClickListener = holderClickListener;
    }

    @NonNull
    @Override
    public PeersListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.peer_info_layout, parent, false);
        Log.d(TAG, "onCreateViewHolder: dklajflak");
        return new PeersListHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PeersListHolder holder, int position) {
        WifiP2pDevice device = mDevices.get(position);
        if(device == null) {
            return;
        }
        Log.d(TAG, "onBindViewHolder: " + device);
        holder.name.setText(device.deviceName);
        holder.addr.setText(device.deviceAddress);
        holder.status.setText(status[device.status]);
        holder.root.setOnClickListener(v -> {
            mHolderClickListener.onClick(mDevices.get(position));
        });
    }

    @Override
    public int getItemCount() {
        return mDevices.size();
    }


    protected class PeersListHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private TextView addr;
        private TextView status;
        private ConstraintLayout root;


        public PeersListHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.peer_name);
            addr = itemView.findViewById(R.id.peer_addr);
            status = itemView.findViewById(R.id.peer_status);
            root = itemView.findViewById(R.id.root);
        }
    }

    public void setData(List<WifiP2pDevice> data) {
        this.mDevices = data;
        notifyDataSetChanged();
    }
}
