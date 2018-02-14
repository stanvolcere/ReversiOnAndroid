package com.stanvolcere.reversigame.ConnectFour.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.stanvolcere.reversigame.ConnectFour.ConnectFourImageAdapter;
import com.stanvolcere.reversigame.ImageAdapter;
import com.stanvolcere.reversigame.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ConnectFourActivity extends AppCompatActivity {

    @BindView(R.id.gridview) GridView gridview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect_four);

        ButterKnife.bind(this);

        gridview.setAdapter(new ConnectFourImageAdapter(this));

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Toast.makeText(ConnectFourActivity.this, "" + position,
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}
