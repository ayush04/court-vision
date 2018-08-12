/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.cv.utils;

import com.cv.events.EventsData;
import com.cv.events.EventsService;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.pubnub.api.PNConfiguration;
import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.SubscribeCallback;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult;
import java.util.Arrays;

/**
 *
 * @author ayush
 */
public class PubNubListener {
    private static PubNub pubnub;
    private static final SubscribeCallback subscribeCallback = new SubscribeCallback() {
        @Override
        public void status(PubNub pubnub, PNStatus status) {
            /* Handling disconnects */
            if (null == status.getCategory()) {
                System.err.println(status);
            } else {
                switch (status.getCategory()) {
                    case PNUnexpectedDisconnectCategory:
                        /* Try to reconnect */
                        pubnub.reconnect();
                        break;
                    case PNTimeoutCategory:
                        /* Try to reconnect */
                        pubnub.reconnect();
                        break;
                    default:
                        System.err.println(status);
                        break;
                }
            }
        }

        @Override
        public void message(PubNub pubnub, PNMessageResult message) {
            /* Message received from RasPi Camera */
            if (message.getPublisher().equals(Constants.RASPI_CAMERA_CLIENT)) {
                JsonElement payload = message.getMessage();
                JsonArray array = payload.getAsJsonArray();
                Double x = array.get(0).getAsDouble();
                Double y = array.get(1).getAsDouble();
                Long gameId = array.get(2).getAsLong();
                EventsData eventData = new EventsData();
                eventData.setEventTime(message.getTimetoken());
                eventData.setEventType(Constants.BALL_POSITION_EVENT_TYPE);
                eventData.setEventValue(String.valueOf(x));
                eventData.setEventValue2(String.valueOf(y));
                eventData.setGameId(gameId);
                EventsService.createEvent(eventData);
            }
        }

        @Override
        public void presence(PubNub pubnub, PNPresenceEventResult pnper) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
    };
    public static void initialize() {
        PNConfiguration pnConfiguration = new PNConfiguration();
        pnConfiguration.setSubscribeKey(Constants.PUBNUB_SUBSCRIBE_KEY);
        pnConfiguration.setPublishKey(Constants.PUBNUB_PUBLISH_KEY);
        pnConfiguration.setSecure(false);
        pubnub = new PubNub(pnConfiguration);
        pubnub.addListener(subscribeCallback);
        
        /* Subscribe to channel */
        pubnub.subscribe().channels(Arrays.asList(Constants.PUBNUB_CHANNELS)).execute();
    }
    
    public static void disconnect() {
        if(pubnub != null) {
            pubnub.removeListener(subscribeCallback);
        }
    }
}
