package com.franciscocalaca.led;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;



@RestController
@RequestMapping(value="/led")
public class LedController {
    
    private ObjectMapper mapper = new ObjectMapper();
    private String topic        = "hello/world";
    private int qos             = 2;
    private String broker       = "tcp://iot.franciscocalaca.com:1883";
    private String clientId     = "Java Client";
    private MemoryPersistence persistence = new MemoryPersistence();


    private String getJsonLed(int v) throws JsonProcessingException{
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("sensor", "clock");
        data.put("number", v);
        data.put("time", new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()));
        String json = mapper.writeValueAsString(data);
        return json;
        
    }
    
    private void changeLed(int v) throws MqttException, JsonProcessingException{
        MqttClient sampleClient = new MqttClient(broker, clientId, persistence);
        MqttConnectOptions connOpts = new MqttConnectOptions();
        connOpts.setCleanSession(true);
        System.out.println("Connecting to broker: "+broker);
        sampleClient.connect(connOpts);
        System.out.println("Connected");
        String content = getJsonLed(v);
        System.out.println("Publishing message: "+content);
        MqttMessage message = new MqttMessage(content.getBytes());
        message.setQos(qos);
        sampleClient.publish(topic, message);
        System.out.println("Message published");
        sampleClient.disconnect();
        System.out.println("Disconnected");        
    }
    
    @RequestMapping(value="/on", method = RequestMethod.GET)
    public ResponseEntity<Void> on() {
        try {
            changeLed(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok(null);
    }
    
    @RequestMapping(value="/off", method = RequestMethod.GET)
    public ResponseEntity<Void> off() {
        try {
            changeLed(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok(null);
    }


}
