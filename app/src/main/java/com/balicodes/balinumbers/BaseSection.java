package com.balicodes.balinumbers;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by eka on 7/3/15.
 */
public class BaseSection extends Fragment implements AdapterView.OnItemClickListener {

    protected List<JSONObject> items = new ArrayList<JSONObject>();
    private BaseListAdapter listAdapter;
    private LinearLayout hotlineContainer, addContainer;
    private ListView listView;
    private String hotlineNumber;
    private TextView hotlineText;
    private Button hotlineBtn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listAdapter = new BaseListAdapter(getActivity(), items, getListItemLayoutId());
    }

    public void call(String number) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse(number));
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            getActivity().startActivity(intent);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutId(), container, false);
        listView = (ListView) view.findViewById(getListViewLayoutId());
        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(this);
        hotlineContainer = (LinearLayout) view.findViewById(R.id.hotlineContainer);
        hotlineContainer.setVisibility(View.GONE);

        addContainer = (LinearLayout) view.findViewById(R.id.adContainer);
        addContainer.setVisibility(View.GONE);

        hotlineText = (TextView) view.findViewById(R.id.hotlineText);
        hotlineBtn = (Button) view.findViewById(R.id.hotlineBtn);
        hotlineBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                call(hotlineNumber);
            }
        });

        // Load JSON file asynchronously
        new JsonLoader().execute(getJsonFileName());
        return view;
    }

    public String getJsonFileName() {
        return null;
    }

    public int getLayoutId() {
        return R.layout.list;
    }

    public boolean isDirectCallAction() {
        return false;
    }

    public int getListItemLayoutId() {
        if (isDirectCallAction()) {
            return R.layout.list_item_direct;
        } else {
            return R.layout.list_item;
        }
    }

    public int getListViewLayoutId() {
        return R.id.list;
    }

    public String getHotlineText(String number) {
        return "HOTLINE - " + number;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        JSONObject obj = items.get(position);
        String title = null;
        String desc = null;
        String address = null;
        JSONArray phones = null;

        try {
            title = obj.getString("title");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            desc = obj.getString("desc");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            address = obj.getString("address");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            phones = obj.getJSONArray("number");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Inflate the popup content layout
        View detailPopup = getActivity().getLayoutInflater().inflate(R.layout.list_item_detail, null, false);
        TextView descTxt = (TextView) detailPopup.findViewById(R.id.descTxt);
        TextView addressTxt = (TextView) detailPopup.findViewById(R.id.addressTxt);
        Button phone1Btn = (Button) detailPopup.findViewById(R.id.phone1Btn);
        Button phone2Btn = (Button) detailPopup.findViewById(R.id.phone2Btn);

        if (desc != null) {
            descTxt.setText(desc);
        } else {
            descTxt.setVisibility(View.GONE);
        }

        if (address != null) {
            addressTxt.setText(address);
        } else {
            addressTxt.setVisibility(View.GONE);
        }

        if (phones != null) {
            if (phones.length() > 1) {
                try {
                    final String phone1 = phones.getString(0).toString();
                    final String phone2 = phones.getString(1).toString();
                    phone1Btn.setText(getText(R.string.call) + " " + phone1);
                    phone2Btn.setText(getText(R.string.call) + " " + phone2);
                    phone1Btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            call("tel:" + phone1);
                        }
                    });
                    phone2Btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            call("tel:" + phone2);
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                phone2Btn.setVisibility(View.GONE);
                try {
                    final String phone1 = phones.getString(0).toString();
                    phone1Btn.setText(getText(R.string.call) + " " + phone1);
                    phone1Btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            call("tel:" + phone1);
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } else {
            phone1Btn.setVisibility(View.GONE);
            phone2Btn.setVisibility(View.GONE);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title);
        builder.setView(detailPopup);
        builder.create();
        builder.show();
    }

    public JSONObject loadJsonFile(String filename) {
        try {
            InputStream stream = getActivity().getAssets().open(filename);
            int size = stream.available();
            byte[] buffer = new byte[size];
            stream.read(buffer);
            stream.close();
            return new JSONObject(new String(buffer, "UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public class JsonLoader extends AsyncTask<String, JSONObject, JSONObject> {
        @Override
        protected JSONObject doInBackground(String... params) {
            JSONObject obj = loadJsonFile(params[0]);
            return obj;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            items.clear();
            try {
                JSONArray list = jsonObject.getJSONArray("list");
                for (int i = 0; i < list.length(); i++) {
                    JSONObject item = list.getJSONObject(i);
                    items.add(item);
                }
                listAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                String number = jsonObject.getString("hotline");
                hotlineText.setText(getHotlineText(number));
                hotlineNumber = "tel:" + number;
                hotlineContainer.setVisibility(View.VISIBLE);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
