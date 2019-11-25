package com.example.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.bookticks.R;
import com.example.model.GridItem;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GridItemView extends FrameLayout {
    @BindView(R.id.seat_no)
    TextView textView;
    @BindView(R.id.seat_layout)
    RelativeLayout relativeLayout;
    public GridItemView(Context context) {
        super(context);
        View view = LayoutInflater.from(context).inflate(R.layout.grid_item, this);
        ButterKnife.bind(this, view);

    }
    public void display(GridItem item, boolean isSelected) {
        textView.setText(item.getSeatNo());
        display(isSelected);
    }
    public void display(boolean isSelected) {
        if(isSelected)
        relativeLayout.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
        else
        relativeLayout.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
    }
}
