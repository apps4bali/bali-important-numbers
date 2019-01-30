package com.apps4bali.nomorpenting;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.apps4bali.nomorpenting.models.Contact;

import java.util.List;

/**
 * Created by eka on 7/4/15.
 */
public class BaseListAdapter extends BaseAdapter {

    private Context context;
    private List<Contact> items;
    private int layoutId;

    BaseListAdapter(Context context, List<Contact> items, int layoutId) {
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
        Contact item = items.get(position);

        // Main title
        TextView text1 = (TextView) convertView.findViewById(R.id.text1);
        text1.setText(item.getTitle());

        // Area
        String area = item.getArea();

        // Sub title
        TextView text2 = (TextView) convertView.findViewById(R.id.text2);
        List<String> numbers = item.getNumbers();

        String txt2 = area != null ? area + " - " + numbers.get(0) : numbers.get(0);
        text2.setText(txt2);

        // direct call button
        if (layoutId == R.layout.list_item_direct) {
            Button callBtn = (Button) convertView.findViewById(R.id.callBtn);

            final String finalNumber = "tel:" + numbers.get(0);
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
