package com.bhola.desiKahaniya;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
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

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAd;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ftab2 extends Fragment {

    List<String> storyURL;
    List<String> storyName;
    AudioStory_Details_Adapter adapter2;
    StorageReference mStorageReference;
    ProgressBar progressBar2;
    RecyclerView recyclerView;
    FirebaseAuth mAuth;

    int currentPositonTime;

    public ftab2() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ftab2, container, false);

        storyURL = new ArrayList<String>();
        storyName = new ArrayList<String>();
        progressBar2 = view.findViewById(R.id.progressBar2);
        mStorageReference = FirebaseStorage.getInstance().getReference().child("audiostories");
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        try {
            loadAudioDatabase(view);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!isInternetAvailable(getContext())) {
            Toast.makeText(getContext(), "Check Internet Connection!", Toast.LENGTH_SHORT).show();
        } else {
        }
        return view;
    }


    private void loadAudioDatabase(View view) {
        if (SplashScreen.Login_Times < 4 || SplashScreen.Sex_Story.equals("inactive")) {
            Cursor cursor2 = new DatabaseHelper(getActivity(), SplashScreen.DB_NAME, SplashScreen.DB_VERSION, "Audio_Story_Fake").readalldata();
            while (cursor2.moveToNext()) {
                storyName.add(cursor2.getString(1));
                storyURL.add(cursor2.getString(2));
            }
        } else {
            Cursor cursor = new DatabaseHelper(getActivity(), SplashScreen.DB_NAME, SplashScreen.DB_VERSION, "Audio_Story").readalldata();
            while (cursor.moveToNext()) {
                if (SplashScreen.Login_Times < 6) {
                    if (cursor.getPosition() < 20) {
                        storyName.add(cursor.getString(1));
                        storyURL.add(cursor.getString(2));
                    }
                } else {
                    storyName.add(cursor.getString(1));
                    storyURL.add(cursor.getString(2));
                }
            }
        }

        ArrayList<Object> collectionData = new ArrayList<Object>();
        for (int i = 0; i < storyName.size(); i++) {
            AudioModel model = new AudioModel(storyName.get(i), storyURL.get(i));
            collectionData.add(model);
        }


        Collections.shuffle(collectionData);
        if (SplashScreen.Login_Times < 4 && SplashScreen.Sex_Story.equals("inactive")) {
            collectionData.clear();
        }

        adapter2 = new AudioStory_Details_Adapter(collectionData, getActivity());
        recyclerView.setAdapter(adapter2);
        progressBar2.setVisibility(View.GONE);
        adapter2.notifyDataSetChanged();


    }


    boolean isInternetAvailable(Context context) {
        if (context == null) return false;


        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager != null) {


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
                if (capabilities != null) {
                    if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                        return true;
                    } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                        return true;
                    } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                        return true;
                    }
                }
            } else {

                try {
                    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                    if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
                        Log.i("update_statut", "Network is available : true");
                        return true;
                    }
                } catch (Exception e) {
                    Log.i("aaaaaaaaaaaaaaaaa", "" + e.getMessage());
                }
            }
        }
        return false;
    }

}


class AudioStory_Details_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    RewardedInterstitialAd mRewardedVideoAd;
    com.facebook.ads.InterstitialAd facebook_IntertitialAds;
    ArrayList<Object> collectionData = new ArrayList<Object>();


    public AudioStory_Details_Adapter(ArrayList<Object> data, FragmentActivity activity) {
        this.context = activity;
        this.collectionData = data;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View Story_ROW_viewHolder = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_layout, parent, false);
        return new Story_ROW_viewHolder(Story_ROW_viewHolder);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int POSITION = position;

        AudioModel model = (AudioModel) collectionData.get(position);
        String filename = model.getName().replace("-", " ").trim();
        ((Story_ROW_viewHolder) holder).title.setText(filename);
        ((Story_ROW_viewHolder) holder).imageview.setImageResource(R.drawable.mp3);
        ((Story_ROW_viewHolder) holder).date.setText("2020-11-09");
        ((Story_ROW_viewHolder) holder).recyclerview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (SplashScreen.Ads_State.equals("active")) {
                    if (SplashScreen.Ad_Network_Name.equals("admob")) {


                        ADS_ADMOB rewarded_ads = new ADS_ADMOB(mRewardedVideoAd, v.getContext(), v.getContext().getString(R.string.Rewarded_ADS_Unit_ID));
                        rewarded_ads.RewardedVideoAds();
                    } else {

                        ADS_FACEBOOK.interstitialAd(v.getContext(), facebook_IntertitialAds, v.getContext().getString(R.string.Facebbok_InterstitialAdUnit));
                    }
                }


                if (isInternetAvailable()) {
                    Intent intent = new Intent(context, AudioPlayer.class);
                    intent.putExtra("storyURL", model.getURL());
                    intent.putExtra("storyName", filename);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    v.getContext().startActivity(intent);
                } else {
                    Toast.makeText(context, "Check Internet Connection!" + System.lineSeparator() +
                            "इंटरनेट कनेक्शन चेक करे", Toast.LENGTH_SHORT).show();
                }

            }

            boolean isInternetAvailable() {
                if (context == null) return false;


                ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                if (connectivityManager != null) {


                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
                        if (capabilities != null) {
                            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                                return true;
                            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                                return true;
                            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                                return true;
                            }
                        }
                    } else {

                        try {
                            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                            if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
                                Log.i("update_statut", "Network is available : true");
                                return true;
                            }
                        } catch (Exception e) {
                            Log.i("update_statut", "" + e.getMessage());
                        }
                    }
                }
                Log.i("update_statut", "Network is available : FALSE ");
                return false;
            }
        });

    }


    @Override
    public int getItemCount() {
        return collectionData.size();
    }


    public class Story_ROW_viewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView  date;

        ImageView imageview;
        LinearLayout recyclerview;


        public Story_ROW_viewHolder(@NonNull View itemView) {
            super(itemView);

            recyclerview = itemView.findViewById(R.id.recyclerviewLayout);
            imageview = itemView.findViewById(R.id.imageview);
            title = itemView.findViewById(R.id.titlee);
            date = itemView.findViewById(R.id.date_recyclerview);

        }
    }
}


