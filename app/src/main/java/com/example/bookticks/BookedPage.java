package com.example.bookticks;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.example.Database.DatabaseManager;
import com.example.model.BookedTickets;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BookedPage extends AppCompatActivity {
    @BindView(R.id.ticketList)
    ListView listView;
    ArrayList<BookedTickets> bookedTicketsArrayList = new ArrayList<>();
    DatabaseManager databaseManager = new DatabaseManager(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booked_page);
        ButterKnife.bind(this);
        databaseManager.open();
        Gson gson = new Gson();
        Cursor cursor = databaseManager.fetch();
        if(cursor != null && cursor.getCount() != 0)
        {

            do
            {
                String outputarray = cursor.getString(2);
                Type type = new TypeToken<ArrayList<String>>() {}.getType();
                ArrayList<String> finalOutputString = gson.fromJson(outputarray, type);
                bookedTicketsArrayList.add(new BookedTickets(cursor.getInt(0), cursor.getString(1), finalOutputString));
            }while (cursor.moveToNext());
            cursor.close();

        }
        final ListAdapter adapter = new com.example.adapter.ListAdapter(this, R.layout.list_item, bookedTicketsArrayList);
        listView.setAdapter(adapter);

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(BookedPage.this);

                builder.setTitle("Confirm");
                builder.setMessage("Are you sure want to cancel?");

                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        databaseManager.delete(bookedTicketsArrayList.get(position).getId());
                        bookedTicketsArrayList.remove(position);
                        ((com.example.adapter.ListAdapter) adapter).notifyDataSetChanged();

                        dialog.dismiss();
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
                return true;
            }
        });



    }
}
