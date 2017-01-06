package com.example.jack.pubparent;

import android.content.Context;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.CheckBox;
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
    static Context context;
    static RecyclerView recyclerView;
    static int position;
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
        recyclerViewAdapter = new RecyclerViewAdapter(context, tasks);
        recyclerView.setAdapter(recyclerViewAdapter);

        Pubnub pn = subscribeToChannel();

    }

    private static void toggle(int posGiven) {
        position = posGiven;

        Handler mainHandler = new Handler(context.getMainLooper());
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                CheckBox cBox = (CheckBox) recyclerView.getChildAt(position).findViewById(R.id.taskCheckBox);
                cBox.callOnClick();
            }
        });
    }

    private static Pubnub subscribeToChannel() {
        Pubnub pn = new Pubnub(PUBLISH_KEY, SUBSCRIBE_KEY);

        try {
            pn.subscribe(CHANNEL, new Callback() {

                        @Override
                        public void connectCallback(String channel, Object message) {
                            System.out.println("SUBSCRIBE : CONNECT on channel:" + channel
                                    + " : " + message.getClass() + " : "
                                    + message.toString());
                        }

                        @Override
                        public void disconnectCallback(String channel, Object message) {
                            System.out.println("SUBSCRIBE : DISCONNECT on channel:" + channel
                                    + " : " + message.getClass() + " : "
                                    + message.toString());
                        }

                        public void reconnectCallback(String channel, Object message) {
                            System.out.println("SUBSCRIBE : RECONNECT on channel:" + channel
                                    + " : " + message.getClass() + " : "
                                    + message.toString());
                        }

                        @Override
                        public void successCallback(String channel, Object message) {
                            System.out.println("SUBSCRIBE : " + channel + " : "
                                    + message.getClass() + " : " + message.toString());
                            JSONObject msg = (JSONObject) message;
                            try {
                                int pos = msg.getInt("Position");
                                MainActivity.toggle(pos);
                            } catch (JSONException je) {
                                je.printStackTrace();
                            }
                        }

                        @Override
                        public void errorCallback(String channel, PubnubError error) {
                            System.out.println("SUBSCRIBE : ERROR on channel " + channel
                                    + " : " + error.toString());
                        }
                    }
            );
        }
        catch (PubnubException pnE) {
            pnE.printStackTrace();
        }

        return pn;
    }
}
