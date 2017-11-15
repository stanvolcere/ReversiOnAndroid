package com.stanvolcere.reversigame;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.EditText;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toast.makeText(this, "Hi there, welcome to the Reversi Prototype" , Toast.LENGTH_SHORT).show();

        final GridView gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(new ImageAdapter(this));


        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {


                ImageView tile = (ImageView) gridview.getAdapter().getView(position,v,parent);

                if(tile.getDrawable().getConstantState().equals(getResources().getDrawable(R.drawable.blackchip).getConstantState())){
                    Toast.makeText(MainActivity.this, "this is a black chip at - " + position,
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "yeah this is not a black chip at - " + position,
                            Toast.LENGTH_SHORT).show();
                    tile.setImageResource(R.drawable.blackchip);
                }




            }
        });


    }


}
