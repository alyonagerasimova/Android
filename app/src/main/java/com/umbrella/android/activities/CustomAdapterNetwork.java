package com.umbrella.android.activities;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.umbrella.android.R;
import com.umbrella.android.data.neuralNetwork.network.Network;

import java.util.List;

public class CustomAdapterNetwork extends BaseAdapter {

    private List<Network> listData;
    private LayoutInflater layoutInflater;
    private Context context;

    public CustomAdapterNetwork(Context aContext, ListAdapter adapter) {
        this.context = aContext;
        this.listData = listData;
        layoutInflater = LayoutInflater.from(aContext);
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        CustomAdapterImages.ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.list_item_layout, null);
            holder = new CustomAdapterImages.ViewHolder();
            holder.flagView = (ImageView) convertView.findViewById(R.id.imageView_flag);
            holder.countryNameView = (TextView) convertView.findViewById(R.id.textView_countryName);
            convertView.setTag(holder);
        } else {
            holder = (CustomAdapterImages.ViewHolder) convertView.getTag();
        }

        Network network = this.listData.get(position);
        holder.countryNameView.setText(network.getNameNetwork());

        return convertView;
    }

    // Find Image ID corresponding to the name of the image (in the directory mipmap).
    public int getMipmapResIdByName(String resName) {
        String pkgName = context.getPackageName();
        // Return 0 if not found.
        int resID = context.getResources().getIdentifier(resName, "mipmap", pkgName);
        Log.i("CustomListView", "Res Name: " + resName + "==> Res ID = " + resID);
        return resID;
    }

    static class ViewHolder {
        ImageView flagView;
        TextView countryNameView;
    }

}
