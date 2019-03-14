package com.akihitoq.osmqtt;

import android.content.Context;
import android.util.Log;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.DisconnectedBufferOptions;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MqttHelper {
    public MqttAndroidClient mqttAndroidClient;

    final String serverUri = "tcp://m10.cloudmqtt.com:16636";

    final String clientId = "ExampleAndroidClient";
    final String subscriptionTopic = "sensor/A";

    final String username = "hivukmbp";
    final String password = "dJYz_mGHsqGP";

    public MqttHelper(Context context){
        mqttAndroidClient = new MqttAndroidClient(context, serverUri, clientId);
        mqttAndroidClient.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean reconnect, String serverURI) {
                Log.w("OSMQTT", serverURI);
            }

            @Override
            public void connectionLost(Throwable cause) {

            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                Log.w("OSMQTT",message.toString());
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });
        connect();
    }

    public void setCallback(MqttCallbackExtended callback) {
        mqttAndroidClient.setCallback(callback);
    }

    public void connect(){
        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setAutomaticReconnect(true);
        mqttConnectOptions.setCleanSession(false);
        mqttConnectOptions.setUserName(username);
        mqttConnectOptions.setPassword(password.toCharArray());

        try {
          mqttAndroidClient.connect(mqttConnectOptions, null, new IMqttActionListener() {
              @Override
              public void onSuccess(IMqttToken asyncActionToken) {
                  DisconnectedBufferOptions disconnectedBufferOptions = new DisconnectedBufferOptions();
                  disconnectedBufferOptions.setBufferEnabled(true);
                  disconnectedBufferOptions.setBufferSize(100);
                  disconnectedBufferOptions.setPersistBuffer(false);
                  disconnectedBufferOptions.setDeleteOldestMessages(false);
                  mqttAndroidClient.setBufferOpts(disconnectedBufferOptions);
                  subscribeToTopic();
              }

              @Override
              public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                  Log.w("OSMQTT", "Failed to connect to: " + serverUri + exception.toString());
              }
          });
        } catch (MqttException ex) {
            ex.printStackTrace();
        }
    }

    private void subscribeToTopic() {
        try{
            mqttAndroidClient.subscribe(subscriptionTopic, 0, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.w("OSMQTT","Subscribed!");
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.w("OSMQTT", "Subscribed fail!");
                }
            });
        } catch (MqttException ex) {
            System.err.println("Exception whilst subscribing");
            ex.printStackTrace();
        }
    }
}


