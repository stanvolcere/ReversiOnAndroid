package com.stanvolcere.reversigame;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.stanvolcere.reversigame.ConnectFour.ui.ConnectFourActivity;
import com.stanvolcere.reversigame.ui.ReversiActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainPageActivity extends AppCompatActivity {

    @BindView(R.id.reversiButton) Button reversiButton;
    @BindView(R.id.buttonCheckers) Button checkersButton;
    @BindView(R.id.buttonConnectFour) Button connectFourButton;
    @BindView(R.id.titleLabel) TextView titleLabel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        ButterKnife.bind(this);

    }

    @OnClick(R.id.reversiButton)
    public void goToReversi(View view){
        Intent intent = new Intent(this, ReversiActivity.class);
        startActivity(intent);
    }


    @OnClick(R.id.buttonConnectFour)
    public void goToConnectFour(View view){
        Intent intent = new Intent(this, ConnectFourActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.buttonCheckers)
    public void goToCheckers(View view){

    }

}
