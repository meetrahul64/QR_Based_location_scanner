package trainedge.android_project;

import android.*;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

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
                handlePermission();
            }
        });


    }

    private void handlePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (checkSelfPermission(android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{android.Manifest.permission.CAMERA}, REQUEST_CAMERA_CODE);
            } else {
                startActivity(new Intent(splash_activity.this, LoginActivity.class));
                finish();
            }
        } else {
            startActivity(new Intent(splash_activity.this, LoginActivity.class));
            finish();

        }
    }

    public static final int REQUEST_CAMERA_CODE = 32;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CAMERA_CODE) {
            if (grantResults.length > 0) {
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    handlePermission();
                } else {
                    startActivity(new Intent(splash_activity.this, LoginActivity.class));
                    finish();
                }
            }
        }
    }
}