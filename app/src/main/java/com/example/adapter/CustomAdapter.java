package com.example.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bookticks.R;
import com.example.model.GridItem;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;



public class CustomAdapter extends BaseAdapter {
    private ArrayList<GridItem> items;

    private Context context;
    public List<Integer> selectedPositions;

    public CustomAdapter(Context context, int resource, ArrayList<GridItem> items) {
        this.items = items;
        this.context = context;
        selectedPositions = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return items.size();
    }


    @Override
    public GridItem getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, @NotNull ViewGroup parent) {

        GridItemView view = (GridItemView) convertView;
        if(view == null)
            view = new GridItemView(context);
        view.display(items.get(position), selectedPositions.contains(position));

        return view;
    }
}
