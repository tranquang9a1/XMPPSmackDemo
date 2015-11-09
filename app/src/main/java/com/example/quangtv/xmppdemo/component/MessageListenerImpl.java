package com.example.quangtv.xmppdemo.component;

import android.util.Log;

import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smackx.chatstates.ChatState;
import org.jivesoftware.smackx.chatstates.ChatStateListener;

/**
 * Created by QuangTV on 11/5/15.
 */
public class MessageListenerImpl implements ChatStateListener, MessageListener {

    private String LOG_TAG = "MessageListenerImp";


    @Override
    public void processMessage(Message message) {
        Log.d(LOG_TAG, "ProcessMessage" + message);
    }

    @Override
    public void processMessage(Chat chat, Message message) {
        Log.d(LOG_TAG, "Receive Message " + message);
    }

    @Override
    public void stateChanged(Chat chat, ChatState state) {
        if(ChatState.composing.equals(state)) {
            Log.d("Chat State",chat.getParticipant() + " is typing..");
        } else if (ChatState.gone.equals(state)) {
            Log.d("Chat State",chat.getParticipant() + " has left the conversation.");
        } else {
            Log.d("Chat State",chat.getParticipant() + ": " + state.name());
        }
    }
}
