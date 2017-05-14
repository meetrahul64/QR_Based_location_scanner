package trainedge.android_project;

import android.app.ListActivity;
import android.app.NotificationManager;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;


public class Settings_activity extends AppCompatActivity implements  AdapterView.OnItemSelectedListener, View.OnClickListener {

    public static final String TAG = "Settings";
    private Switch notif;
    private SharedPreferences pref;
    private ArrayAdapter<CharSequence> adapter;
    private ArrayAdapter<CharSequence> adapter1;
    private Spinner spinner;
    private Spinner spinner1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_activity);
        /*Add in Oncreate() funtion after setContentView()*/
// initiate a Switch
        Switch simpleSwitch = (Switch) findViewById(R.id.switch1);

//set the current state of a Switch
        simpleSwitch.setChecked(true);
        simpleSwitch.setChecked(false);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        pref = getSharedPreferences("setting_pref", MODE_PRIVATE);
        // initiate a Switch
        //Switch simpleSwitch = (Switch) findViewById(R.id.switch1);

// check current state of a Switch (true or false).
        Boolean switchState = simpleSwitch.isChecked();

        spinner = (Spinner) findViewById(R.id.spn_themes);
// Create an ArrayAdapter using the string array and a default spinner layout
        adapter = ArrayAdapter.createFromResource(this,
                R.array.pref_example_list_values, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);


        updateUI();


    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Toast.makeText(this, "Settings saved", Toast.LENGTH_SHORT).show();
    }

    private void updateUI() {

        spinner.setSelection(pref.getInt("themepos", 0));

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        SharedPreferences.Editor edit = pref.edit();


        edit.putInt("themepos", position);
        edit.putString("theme", String.valueOf(adapter.getItem(position)));


        edit.apply();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {

    }

    //FirebaseAuth.getInstance().signOut();
    //intent to login activity
    //finish();

}



