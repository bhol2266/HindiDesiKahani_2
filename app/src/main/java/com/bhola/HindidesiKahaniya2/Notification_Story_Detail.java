package com.bhola.HindidesiKahaniya2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bhola.HindidesiKahaniya2.FirebaseData;
import com.bhola.HindidesiKahaniya2.admin_panel;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Notification_Story_Detail extends AppCompatActivity {
    String TAG="taga";
    List<FirebaseData> collectonData;
    NotificationAdapter adapter2;
    DatabaseReference mref;
    String title1;
    Context context;
    ImageView back,share_ap;
    ProgressBar progressBar2;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection_detail);


        actionBar();
        progressBar2 = findViewById(R.id.progressBar2);
        mref = FirebaseDatabase.getInstance().getReference().child("Notification");
        final RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        collectonData = new ArrayList<FirebaseData>();

        mref.keepSynced(true);
        mref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    FirebaseData firebaseData = ds.getValue(FirebaseData.class);
                    collectonData.add(firebaseData);
                }
                if (!SplashScreen.Sex_Story.equals("active") && !SplashScreen.Sex_Story_Switch_Open.equals("active")) {
                    collectonData.clear();
                }
                adapter2 = new NotificationAdapter(collectonData,context,title1);
                recyclerView.setAdapter(adapter2);
                adapter2.notifyDataSetChanged();
                progressBar2.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(),Collection_GridView.class));
    }

    private void actionBar() {
        TextView title=findViewById(R.id.title_collection);
        title.setText("Notifications");

        back=findViewById(R.id.back_arrow);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Collection_GridView.class));
            }
        });
        share_ap=findViewById(R.id.share_app);
        share_ap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                share_ap.setImageResource(R.drawable.favourite_active);
                startActivity(new Intent(getApplicationContext(),Download_Detail.class));
            }
        });
    }

}

class NotificationAdapter  extends RecyclerView.Adapter<NotificationAdapter.viewholder> {

   List<FirebaseData> collectonData;
   Context context;
   String Collection_Number;


   public NotificationAdapter(List<FirebaseData> collectonData, Context context, String message) {

       this.collectonData = collectonData;
       this.context=context;
       this.Collection_Number=message;
   }




   @NonNull
   @Override
   public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
       View view = layoutInflater.inflate(R.layout.grid_layout, parent, false);
       return new viewholder(view);
   }

   @Override
   public void onBindViewHolder(@NonNull viewholder holder, int position) {

       FirebaseData firebaseData = collectonData.get(position);
       holder.title.setText(firebaseData.getTitle());
       holder.date.setText(firebaseData.getDate());

       holder.recyclerview.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent=new Intent(v.getContext(), Notification_StoryPage.class);
               intent.putExtra("Story",firebaseData.getHeading());
               intent.putExtra("Collection",Collection_Number);
               intent.putExtra("title",firebaseData.getTitle());
               intent.putExtra("Collection2","Notification");
               intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
               v.getContext().startActivity(intent);

           }
       });


   }




   public class viewholder extends RecyclerView.ViewHolder {
       TextView title;
       TextView index,heading,date;
       LinearLayout recyclerview;


       public viewholder(@NonNull View itemView) {
           super(itemView);
           recyclerview=itemView.findViewById(R.id.recyclerviewLayout);
           title=itemView.findViewById(R.id.titlee);
           date=itemView.findViewById(R.id.date_recyclerview);

       }


   }

   @Override
   public int getItemCount() {
       return collectonData.size();
   }
}


