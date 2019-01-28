package com.balicodes.balinumbers;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

import com.balicodes.balinumbers.models.Contact;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by eka on 7/3/15.
 */
public class SectionFragment extends Fragment implements AdapterView.OnItemClickListener {

    private static Logger LOG = Logger.getLogger(SectionFragment.class.getName());
    private Bundle args;

    protected List<Contact> items = new ArrayList<Contact>();
    private BaseListAdapter listAdapter;
    private LinearLayout hotlineContainer, addContainer;
    private ListView listView;
    private String hotlineNumber;
    private TextView hotlineText;
    private Button hotlineBtn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        args = getArguments();

        listAdapter = new BaseListAdapter(getActivity(), items, getListItemLayoutId());
    }

    public void call(String number) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse(number));
        if (intent.resolveActivity(requireActivity().getPackageManager()) != null) {
            requireActivity().startActivity(intent);
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

        if (args.getString("hotline") != null) {
            String num = args.getString("hotline");
            String hotlineTxt = args.getString("hotlineText") + " - " + num;
            hotlineText.setText(hotlineTxt);
            hotlineNumber = "tel:" + num;
            hotlineContainer.setVisibility(View.VISIBLE);
        }

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String listPath = "contacts/" + args.getString("id") + "/list";

        // Load section list
        db.collection(listPath)
                .orderBy("created_at")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            items.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                items.add(new Contact(document));
                            }
                            listAdapter.notifyDataSetChanged();
                        } else {
                            LOG.warning("Error getting collections: " + task.getException());
                        }
                    }
                });

        return view;
    }

    public int getLayoutId() {
        return R.layout.list;
    }

    public boolean isDirectCallAction() {
        return args.getBoolean("directCallBtn");
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Contact contact = items.get(position);

        // Inflate the popup content layout
        View detailPopup = getActivity().getLayoutInflater().inflate(R.layout.list_item_detail, null, false);
        TextView descTxt = (TextView) detailPopup.findViewById(R.id.descTxt);
        TextView addressTxtLabel = (TextView) detailPopup.findViewById(R.id.addressTxtLabel);
        TextView addressTxt = (TextView) detailPopup.findViewById(R.id.addressTxt);
        Button phone1Btn = (Button) detailPopup.findViewById(R.id.phone1Btn);
        Button phone2Btn = (Button) detailPopup.findViewById(R.id.phone2Btn);

        if (contact.getDesc() != null) {
            descTxt.setText(contact.getDesc());
        } else {
            descTxt.setVisibility(View.GONE);
        }

        if (contact.getAddress() != null) {
            addressTxt.setText(contact.getAddress());
        } else {
            addressTxtLabel.setVisibility(View.GONE);
            addressTxt.setVisibility(View.GONE);
        }

        if (contact.getNumbers() != null) {
            List<String> numbers = contact.getNumbers();

            if (numbers.size() > 1) {
                final String phone1 = numbers.get(0);
                final String phone2 = numbers.get(1);

                String txt1 = getText(R.string.call) + " " + phone1;
                String txt2 = getText(R.string.call) + " " + phone2;
                phone1Btn.setText(txt1);
                phone2Btn.setText(txt2);
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
            } else {
                phone2Btn.setVisibility(View.GONE);
                final String phone1 = numbers.get(0);
                String txt2 = getText(R.string.call) + " " + phone1;
                phone1Btn.setText(txt2);
                phone1Btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        call("tel:" + phone1);
                    }
                });
            }
        } else {
            phone1Btn.setVisibility(View.GONE);
            phone2Btn.setVisibility(View.GONE);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(contact.getTitle());
        builder.setView(detailPopup);
        builder.create();
        builder.show();
    }
}
