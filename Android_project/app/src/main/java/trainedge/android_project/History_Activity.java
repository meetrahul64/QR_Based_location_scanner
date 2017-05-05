package trainedge.android_project;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.util.ArrayList;

 class HistoryActivity extends AppCompatActivity {

    private RecyclerView rvHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        manageHistoryRecycler();

    }

    //for history
    private void manageHistoryRecycler() {
        rvHistory = (RecyclerView) findViewById(R.id.rvHistory);
        rvHistory.setLayoutManager(new LinearLayoutManager(this));
        rvHistory.setAdapter(new HistoryAdapter(getApplicationContext()));
        rvHistory.setVisibility(View.GONE);
    }


    private class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.Holder> {

        private final Context context;
        private final ArrayList<HistoryAdapter.HistoryModel> modelList;

        public HistoryAdapter(Context context) {
            this.context = context;
            modelList = new ArrayList<>();
            try {
                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                DatabaseReference history = FirebaseDatabase.getInstance().getReference("history").child(uid);
                history.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        modelList.clear();
                        if (dataSnapshot.hasChildren()) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                modelList.add(new HistoryAdapter.HistoryModel(snapshot));
                                HistoryAdapter.this.notifyDataSetChanged();
                            }
                            if (modelList.size() > 0) {
                                rvHistory.setVisibility(View.VISIBLE);
                            }
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        if (databaseError != null) {
                            Toast.makeText(HistoryActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(context, "could not load history", Toast.LENGTH_SHORT).show();
            }

        }

        @Override
        public HistoryAdapter.Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = ((LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE)).inflate(R.layout.simple_history_item, parent, false);
            return new HistoryAdapter.Holder(view);
        }

        @Override
        public void onBindViewHolder(HistoryAdapter.Holder holder, int position) {
            HistoryAdapter.HistoryModel model = modelList.get(position);
            Picasso.with(context).load(model.getPhoto()).into(holder.ivLocImage);
            holder.tvLocName.setText(model.getName());
        }

        @Override
        public int getItemCount() {
            return modelList.size();
        }

        public class Holder extends RecyclerView.ViewHolder {
            TextView tvLocName;
            ImageView ivLocImage;

            public Holder(View itemView) {
                super(itemView);
                tvLocName = (TextView) itemView.findViewById(R.id.ivLocName);
                ivLocImage = (ImageView) itemView.findViewById(R.id.ivlocImage);
            }
        }

        private class HistoryModel {
            String name;
            String photo;
            String id;
            String key;
            long time;

            public HistoryModel(DataSnapshot snapshot) {
                id = snapshot.child("id").getValue(String.class);
                name = snapshot.child("name").getValue(String.class);
                time = snapshot.child("timestamp").getValue(Long.class);
                photo = snapshot.child("id").getValue(String.class);
                key = snapshot.getKey();
            }

            public String getName() {
                return name;
            }


            public String getPhoto() {
                return photo;
            }

            public String getId() {
                return id;
            }
        }
    }
}