package trainedge.android_project;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Request;

import java.util.HashMap;

import static android.R.attr.name;
import static trainedge.android_project.ScanActivity.LOCATION_ID;

public class ShowInfoActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String PACKAGE_NAME = "trainedge.android_project";
    private Toolbar toolbar;
    private FloatingActionButton fab;
    private ImageView appBarImage;
    private CollapsingToolbarLayout toolbarLayout;
    private TextView tvAddress;
    private TextView tvlat;
    private TextView tvlng;
    private Button btnGoogle;
    private Button btnWiki;
    private Button btnViewImages;
    private Button btnMap;
    private Button btnMainLink;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrlocation_detaills);
        bindViews();
        initViews();
        getIntentInfo();
        setupListeners();
    }

    private void getIntentInfo() {
        if (getIntent() != null) {
            if (getIntent().hasExtra(LOCATION_ID)) {
                String locationId = getIntent().getStringExtra(LOCATION_ID);
                loadDataFromFirebase(locationId);
            }
        } else {

        }
    }

    private void loadDataFromFirebase(String locationId) {
        dialog = new ProgressDialog(this);
        dialog.setTitle("Loading");
        dialog.setCancelable(false);
        dialog.show();
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("location").child(locationId);
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    String address = dataSnapshot.child("address").getValue(String.class);
                    String latitude = String.valueOf(dataSnapshot.child("lat").getValue(Double.class));
                    String longitude = String.valueOf(dataSnapshot.child("log").getValue(Double.class));
                    String main = dataSnapshot.child("main_link").getValue(String.class);
                    String photo1 = dataSnapshot.child("photo_1").getValue(String.class);
                    String photo2 = dataSnapshot.child("photo_2").getValue(String.class);
                    String web = dataSnapshot.child("wiki_link").getValue(String.class);
                    String id = dataSnapshot.getKey();
                    updateUI(address, latitude, longitude, main, photo1, photo2, web,id);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                if (databaseError != null) {

                    dialog.dismiss();
                    Toast.makeText(ShowInfoActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void updateUI(String address, String latitude, String longitude, String name, String photo1, String photo2, String web, String id) {
        dialog.dismiss();
        try {
            tvAddress.setText(address);
            tvlat.setText(latitude);
            tvlng.setText(longitude);
            Picasso.with(this).setIndicatorsEnabled(true);

            Picasso.with(this).load(photo1).error(R.drawable.backgrnd).into(appBarImage);
            btnWiki.setTag(web);
            btnMainLink.setTag(name);
            try {
                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                DatabaseReference history = FirebaseDatabase.getInstance().getReference("history").child(uid);
                HashMap<String, Object> historyInfo = new HashMap<String, Object>();
                historyInfo.put("id", id);
                historyInfo.put("name", name);
                historyInfo.put("timestamp", System.currentTimeMillis());
                historyInfo.put("photo", photo2);
                historyInfo.put("address", address);
                history.push().setValue(historyInfo);
            } catch (NullPointerException e) {
                Toast.makeText(this, "user details could not be fetched", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(this, "could not save in history", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }


        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    private void setupListeners() {
        fab.setOnClickListener(this);
        btnGoogle.setOnClickListener(this);
        btnMap.setOnClickListener(this);
        btnViewImages.setOnClickListener(this);
        btnWiki.setOnClickListener(this);
        btnMainLink.setOnClickListener(this);
    }

    private void initViews() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void bindViews() {
        appBarImage = (ImageView) findViewById(R.id.app_bar_image);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        toolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbarLayout);
        tvAddress = (TextView) findViewById(R.id.tvAddress);
        tvlat = (TextView) findViewById(R.id.tvlongitude);
        tvlng = (TextView) findViewById(R.id.tvlatitude);
        btnGoogle = (Button) findViewById(R.id.btnGoogle);
        btnWiki = (Button) findViewById(R.id.btnWiki);
        btnViewImages = (Button) findViewById(R.id.btnViewImages);
        btnMap = (Button) findViewById(R.id.btnMap);
        btnMainLink = (Button) findViewById(R.id.btnMainLink);
    }

    @Override
    public void onClick(View v) {
        String lng = tvlng.getText().toString();
        String lat = tvlat.getText().toString();
        switch (v.getId()) {
            case R.id.btnGoogle:
                launchIntent("https://www.google.co.in/search?q=" + tvAddress.getText().toString());
                break;
            case R.id.btnMap:

                Uri gmmIntentUri = Uri.parse("geo:" + lat + "," + lng);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                if (mapIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(mapIntent);
                } else {
                    Toast.makeText(this, "this mobile sucks", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btnViewImages:
                launchIntent("https://www.google.co.in/search?q=" + tvAddress.getText().toString() + "&tbm=isch");

                break;
            case R.id.btnWiki:
                launchIntent(btnWiki.getTag().toString());
                break;
            case R.id.fab:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "hey my location is"+tvAddress+"and "+lat+","+lng+".");
                sendIntent.setType("text/plain");
                startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.send_to)));
                break;
            case R.id.btnMainLink:
                launchIntent(btnMainLink.getTag().toString());
                break;
        }
    }

    private void launchIntent(String url) {
        Uri webpage = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        if (intent.resolveActivity(getPackageManager()) != null) ;
        {
            startActivity(intent);

        }
    }
}