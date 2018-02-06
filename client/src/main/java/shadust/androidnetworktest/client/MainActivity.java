package shadust.androidnetworktest.client;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{


    private DatagramSocket clientSocket;
    private int clientPort = 6666;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        try {
            clientSocket = new DatagramSocket(clientPort);
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    private void initView() {
        Button send_btn = findViewById(R.id.client_send_btn);
        send_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        EditText client_host_input = findViewById(R.id.client_host_input);
        EditText client_port_input = findViewById(R.id.client_port_input);
        EditText client_content_input = findViewById(R.id.client_content_input);


        String host = client_host_input.getText().toString();
        int port = Integer.parseInt(client_port_input.getText().toString());
        byte[] content = client_content_input.getText().toString().getBytes(StandardCharsets.UTF_8);

        try {
            InetAddress address = InetAddress.getByName(host);
            DatagramPacket sendPacket = new DatagramPacket(content,
                    content.length, address, port);
            clientSocket.send(sendPacket);

            client_content_input.setText("");

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
