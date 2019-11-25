package com.example.bookticks;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.example.Database.DatabaseManager;
import com.example.adapter.CustomAdapter;
import com.example.adapter.GridItemView;
import com.example.model.GridItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class SeatChart extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private CustomAdapter customAdapter;
    private DatabaseManager databaseManager;
    private ArrayList<GridItem> gridItems = new ArrayList<>();
    @BindView(R.id.seats)
    GridView gridView;
    @BindView(R.id.confirm)
    Button button;
    private String mParam1;


    public SeatChart() {

    }


    public static SeatChart newInstance(String param1) {
        SeatChart fragment = new SeatChart();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            Log.d("TRAIN NAME", mParam1);
        }
        databaseManager = new DatabaseManager(getActivity());
        databaseManager.open();
        char initial = mParam1.charAt(0);
        for (int i = 1; i <= 40; i++) {
            gridItems.add(new GridItem(String.valueOf(initial) + i));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_seat_chart, container, false);
        ButterKnife.bind(this, view);
        customAdapter = new CustomAdapter(getContext(), R.layout.grid_item, gridItems);
        String str = "";
        Cursor cursor = databaseManager.fetch();
        if (cursor != null && cursor.getCount() != 0) {
            boolean flag = false;
            do {
                if (cursor.getString(1).equals(mParam1)) {
                    str = cursor.getString(2);
                    flag = true;
                    if(flag) {
                        Type type = new TypeToken<ArrayList<String>>() {
                        }.getType();
                        Gson gson = new Gson();
                        ArrayList<String> finalOutputString = gson.fromJson(str, type);
                        for (int i = 0; i < finalOutputString.size(); i++) {
                            String sub = finalOutputString.get(i).substring(1);
                            customAdapter.selectedPositions.add(Integer.parseInt(sub) - 1);
                        }
                        flag = false;
                    }

                }
            } while (cursor.moveToNext());
            cursor.close();
        }


        gridView.setAdapter(customAdapter);
        final int index = customAdapter.selectedPositions.size();
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                int selectedIndex = customAdapter.selectedPositions.indexOf(position);

                if (selectedIndex > -1) {
                    customAdapter.selectedPositions.remove(selectedIndex);
                    ((GridItemView) view).display(false);


                } else {
                    customAdapter.selectedPositions.add(position);
                    ((GridItemView) view).display(true);

                }
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((customAdapter.selectedPositions).isEmpty()) {
                    Toast.makeText(getActivity(), "Select any seats!",
                            Toast.LENGTH_LONG).show();
                } else {

                    if(customAdapter.selectedPositions.size() == index)
                    {
                        Toast.makeText(getActivity(), "Select any seats!",
                                Toast.LENGTH_LONG).show();
                    }
                    else {
                        ArrayList<String> list = new ArrayList<>();
                        for (int i = index; i < customAdapter.selectedPositions.size(); i++) {
                            list.add(gridItems.get(customAdapter.selectedPositions.get(i)).getSeatNo());
                        }
                        handleConfirm(list);
                    }
                }

            }
        });
        return view;
    }

    public void handleConfirm(final ArrayList<String> list) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Confirm");
        builder.setMessage("Are you sure?");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                Gson gson = new Gson();
                String string = gson.toJson(list);
                long id = databaseManager.insert(mParam1, string);
                Log.d("db-id", String.valueOf(id));
                dialog.dismiss();
                Toast.makeText(getActivity(), "Booking successful",
                        Toast.LENGTH_LONG).show();
                assert getFragmentManager() != null;
                getFragmentManager().popBackStack();
            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                // Do nothing
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }


}
