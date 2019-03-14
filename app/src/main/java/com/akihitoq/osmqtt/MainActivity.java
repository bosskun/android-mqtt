package com.akihitoq.osmqtt;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MainActivity extends AppCompatActivity {
    MqttHelper mqttHelper;
    TextView dataReceived;
    EditText message_send;
    Button btn_send;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dataReceived = (TextView) findViewById(R.id.tv_sub);
        message_send = (EditText) findViewById(R.id.message_txt);
        btn_send = (Button) findViewById(R.id.btn_send);
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    sendMessage();
                } catch (MqttException e) {
                    e.printStackTrace();
                }
            }
        });

        startMqtt();
    }

    private void startMqtt() {
        mqttHelper = new MqttHelper(getApplicationContext());
        mqttHelper.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean reconnect, String serverURI) {

            }

            @Override
            public void connectionLost(Throwable cause) {

            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                String[] str3 = message.toString().split((" "));
                if(str3[1].equals("+"))
                    dataReceived.setText(String.valueOf(Integer.parseInt(str3[0]) + Integer.parseInt(str3[2])));
                else if(str3[1].equals("-")){
                    dataReceived.setText(String.valueOf(Integer.parseInt(str3[0]) - Integer.parseInt(str3[2])));
                } else if(str3[1].equals("*")){
                    dataReceived.setText(String.valueOf(Integer.parseInt(str3[0]) * Integer.parseInt(str3[2])));
                }else if(str3[1].equals("/")){
                    dataReceived.setText(String.valueOf(Integer.parseInt(str3[0]) / Integer.parseInt(str3[2])));
                }
                Log.w("OSMQTT", message.toString());
                //dataReceived.setText(message.toString());
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });
    }
    private void sendMessage() throws MqttException {
        MqttMessage message = new MqttMessage(message_send.getText().toString().getBytes());
        mqttHelper.mqttAndroidClient.publish(mqttHelper.subscriptionTopic,message);
    }
}
