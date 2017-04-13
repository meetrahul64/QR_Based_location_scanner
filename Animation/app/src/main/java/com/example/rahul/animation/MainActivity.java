package com.example.rahul.animation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvanim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btnbutton =(Button)findViewById(R.id.btnbutton);
        tvanim = (TextView) findViewById(R.id.tvanim);
        btnbutton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        tvanim.animate()
                .scaleX(1.5f)
                .scaleY(1.5f)
                .setDuration(1000);
        tvanim.animate()
                .rotationX(360f)
                .alpha(1)
                .setInterpolator(new AnticipateOvershootInterpolator())
                .setDuration(1500)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        tvanim.animate()
                                .scaleX(0.5f)
                                .scaleY(0.5f)
                                .setDuration(1000);
                    }
                });
    }
}
