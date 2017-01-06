package com.example.jack.pubkid;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pubnub.api.*;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private static final String CHANNEL = "flaherty_family";

    private static final String PUBLISH_KEY = "pub-c-dc529173-c016-4bf1-b4bf-f90416f3e56d";
    private static final String SUBSCRIBE_KEY = "sub-c-3fcb0e72-d3a9-11e6-8b9b-02ee2ddab7fe";

    TextView title;
    Context context;
    RecyclerView recyclerView;
    RelativeLayout relativeLayout;
    RecyclerView.Adapter recyclerViewAdapter;
    RecyclerView.LayoutManager recyclerViewLayoutManager;
    String[] tasks = {
            "Eating breakfast",
            "Getting on bus",
            "In homeroom",
            "At lunch",
            "Getting on bus",
            "Arrived at bus stop",
            "Arrived home",
            "Doing homework",
            "In bed"
    };

    Pubnub pn;
    Callback callback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        title = (TextView) findViewById(R.id.task_title);
        title.setText(R.string.task_list_title);

        context = getApplicationContext();
        relativeLayout = (RelativeLayout) findViewById(R.id.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview1);
        recyclerViewLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(recyclerViewLayoutManager);
        recyclerViewAdapter = new RecyclerViewAdapter(context, tasks, this);
        recyclerView.setAdapter(recyclerViewAdapter);

        pn = new Pubnub(PUBLISH_KEY, SUBSCRIBE_KEY);

        callback = new Callback() {
            public void successCallback(String channel, Object response) {
                System.out.println(response.toString());
            }
            public void errorCallback(String channel, PubnubError error) {
                System.out.println(error.toString());
            }
        };
    }

    public void publishCheck(int position) {
        JSONObject js = new JSONObject();
        try {
            js.put("Position", position);
        } catch (JSONException je) {
            je.printStackTrace();
        }
        pn.publish(CHANNEL, js, callback);
    }
}
