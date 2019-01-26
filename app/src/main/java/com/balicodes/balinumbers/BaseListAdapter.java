package com.balicodes.balinumbers;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by eka on 7/4/15.
 */
public class BaseListAdapter extends BaseAdapter {

    private Context context;
    private List<JSONObject> items;
    private int layoutId;

    public BaseListAdapter(Context context, List<JSONObject> items, int layoutId) {
        this.context = context;
        this.items = items;
        this.layoutId = layoutId;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private void call(String number) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse(number));
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layoutId, parent, false);
        }
        JSONObject item = items.get(position);

        // Main title
        TextView text1 = (TextView) convertView.findViewById(R.id.text1);
        try {
            text1.setText(item.getString("title"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Area
        String area = null;
        try {
            area = item.getString("area");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Sub title
        TextView text2 = (TextView) convertView.findViewById(R.id.text2);
        String number = null;
        JSONArray numbers = null;
        try {
            numbers = item.getJSONArray("number");
            number = numbers.getString(0);
            String sub = area != null ? area + " - " + number : number;
            text2.setText(sub);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // direct call button
        if (layoutId == R.layout.list_item_direct && number != null) {
            Button callBtn = (Button) convertView.findViewById(R.id.callBtn);

            final String finalNumber = "tel:" + number;
            callBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    call(finalNumber);
                }
            });
        }

        return convertView;
    }
}
