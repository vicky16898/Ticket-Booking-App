package com.example.adapter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.bookticks.R;
import com.example.model.BookedTickets;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListAdapter extends ArrayAdapter<BookedTickets> {
    ArrayList<BookedTickets> bookedTickets;
    Context context;
    int resource;
    @BindView(R.id.name)
    TextView textView;
    @BindView(R.id.seatsText)
    TextView seatsText;

    public ListAdapter(Context context, int resource, ArrayList<BookedTickets> bookedTickets)
    {
        super(context, resource, bookedTickets);
        this.context = context;
        this.resource =resource;
        this.bookedTickets = bookedTickets;
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View getView(int position,View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(resource, null, false);
        ButterKnife.bind(this, view);
        BookedTickets bk = bookedTickets.get(position);
        textView.setText(bk.getTrainName());
        String listString = String.join(", ",bk.getSeats());
        seatsText.setText(listString);
        return view;
    }
}
