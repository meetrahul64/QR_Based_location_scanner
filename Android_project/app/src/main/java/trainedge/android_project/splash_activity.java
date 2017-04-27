package trainedge.android_project;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import trainedge.LoginActivity;

import static android.R.attr.animation;

public class splash_activity extends AppCompatActivity {


    private ImageView logo;
    private TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_activity);
        logo = (ImageView) findViewById(R.id.iv_map);
        text = (TextView) findViewById(R.id.tv_ls);
        logo.animate().scaleX(1.5f).scaleY(1.5f).setDuration(1000).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);

                Intent i = new Intent(splash_activity.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        });

    }
}