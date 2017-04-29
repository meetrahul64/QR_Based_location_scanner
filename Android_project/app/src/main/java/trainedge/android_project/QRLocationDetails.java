package trainedge.android_project;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.math.DoubleMath;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class QRLocationDetails extends AppCompatActivity implements View.OnClickListener {

    private TextView tvAddress;
    private TextView tvLat;
    private TextView tvLog;
    private TextView tvMainLink;
    private TextView tvWiki;
    private ImageView ivPhoto1;
    private ImageView ivPhoto2;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrlocation_detaills);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout layout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        loadData();
        tvAddress = (TextView) findViewById(R.id.id_address);
        tvLat = (TextView) findViewById(R.id.id_lat);
        tvLog = (TextView) findViewById(R.id.id_log);
        tvMainLink = (TextView) findViewById(R.id.id_main_link);
        tvWiki = (TextView) findViewById(R.id.id_wiki_link);
        ivPhoto1 = (ImageView) findViewById(R.id.id_pic1);
        ivPhoto2 = (ImageView) findViewById(R.id.id_pic2);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);
    }

    private void loadData() {
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        String id = extras.getString("trainedge.android_project.id");
        long timestamp = extras.getLong("trainedge.android_project.timestamp");
        getFromDatabase(id);

    }

    private void getFromDatabase(String id) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("location").child(id);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    String address = dataSnapshot.child("address").getValue(String.class);
                    try {
                        String lat = String.valueOf(dataSnapshot.child("lat").getValue(Double.class));
                        String lng = String.valueOf(dataSnapshot.child("log").getValue(Double.class));
                        tvLat.setText(String.valueOf(lat));
                        tvLog.setText(String.valueOf(lng));
                    } catch (Exception e) {

                    }
                    String main_link = dataSnapshot.child("main_link").getValue(String.class);
                    String photo_1 = dataSnapshot.child("photo_1").getValue(String.class);
                    String photo_2 = dataSnapshot.child("photo_2").getValue(String.class);
                    String wiki_link = dataSnapshot.child("wiki_link").getValue(String.class);
                    tvAddress.setText(address);
                    tvWiki.setText(wiki_link);
                    tvMainLink.setText(main_link);
                    Picasso.with(QRLocationDetails.this).load(photo_1).into(ivPhoto1);
                    Picasso.with(QRLocationDetails.this).load(photo_2).into(ivPhoto2);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(QRLocationDetails.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                shareInfo();
                break;
        }
    }

    private void shareInfo() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "i m already ");
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }
}
