package shadust.androidnetworktest.service;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    ListView service_list;
    ArrayList<String> message;
    ArrayAdapter adapter;
    static int SHOW_MESSAGE = 7;

    private DatagramSocket serverSocket;
    private int servicePort = 6666;

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==SHOW_MESSAGE){
                adapter.notifyDataSetChanged();
            }
        }
    };
    Thread check_thread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        try {
            serverSocket = new DatagramSocket(servicePort);
        } catch (SocketException e) {
            e.printStackTrace();
        }
        check_thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    byte[] receiveData = new byte[1024];
                    Log.i("MainActivity","start recieving");
                    DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

                    try {

                        serverSocket.receive(receivePacket);

                        InetAddress sourceIPAddress = receivePacket.getAddress();


                        receiveData = receivePacket.getData();

                        String messageNow = new String(receiveData);
                        Log.i("MainActivity","messageNow="+messageNow);
                        message.add(messageNow);

                        handler.sendEmptyMessage(SHOW_MESSAGE);


                    } catch (Exception e) {


                    }
                }
            }
        });
        check_thread.start();
    }

    private void initView() {
        service_list = findViewById(R.id.service_list);
        message = new ArrayList<>();
        adapter = new ArrayAdapter(this,R.layout.message,message);
        service_list.setAdapter(adapter);
    }

}
