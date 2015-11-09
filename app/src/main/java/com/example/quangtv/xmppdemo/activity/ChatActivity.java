package com.example.quangtv.xmppdemo.activity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.quangtv.xmppdemo.adapter.ChatArrayAdapter;
import com.example.quangtv.xmppdemo.entity.Connection;
import com.example.quangtv.xmppdemo.entity.MessageInfo;
import com.example.quangtv.xmppdemo.R;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.StanzaListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.filter.MessageTypeFilter;
import org.jivesoftware.smack.filter.StanzaFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Stanza;
import org.jivesoftware.smackx.filetransfer.FileTransfer;
import org.jivesoftware.smackx.filetransfer.FileTransferListener;
import org.jivesoftware.smackx.filetransfer.FileTransferManager;
import org.jivesoftware.smackx.filetransfer.FileTransferNegotiator;
import org.jivesoftware.smackx.filetransfer.FileTransferRequest;
import org.jivesoftware.smackx.filetransfer.IncomingFileTransfer;
import org.jivesoftware.smackx.filetransfer.OutgoingFileTransfer;
import org.jxmpp.util.XmppStringUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by QuangTV on 11/6/15.
 */
public class ChatActivity  extends ActionBarActivity {
    private static final String TAG = "ChatActivity";
    private Handler mHandler = new Handler();
    private ChatArrayAdapter chatArrayAdapter;
    private ListView listView;
    private EditText chatText;
    private ImageButton buttonSend;
    private ImageButton btnImage;
    private String to;
    static boolean isActive = false;
    String username;
    String password;
    int SELECT_PICTURE = 1;
    private String selectedImagePath;
    private String filePath;
    private XMPPConnection connection;
    List<MessageInfo> messages = new ArrayList<>();

    Intent intent;
    private boolean side = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i = getIntent();
        isActive  = true;
        setContentView(R.layout.activity_chatting);

        Toolbar toolbar = (Toolbar) this.findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        to = i.getExtras().getString("username");

        if (!to.contains("@fysh.in")) {
            to += "@fysh.in";
        }
        toolbar.setTitle(to);
        this.setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        String body = i.getExtras().getString("message");
//        if (body != null) {
//            messages.add(new MessageInfo(body, false, "text"));
//        }
        buttonSend = (ImageButton) findViewById(R.id.buttonSend);
        btnImage = (ImageButton) findViewById(R.id.btnImage);
        connection = Connection.getConnection();
        setConnection(connection);

        listView = (ListView) findViewById(R.id.listView1);

        setListAdapter();



        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        chatText = (EditText) findViewById(R.id.chatText);
        chatText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    return sendChatMessage();
                }
                return false;
            }
        });


        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                sendChatMessage();
            }
        });

        listView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);

        btnImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_PICK);
                startActivityForResult(Intent.createChooser(intent,
                        "Select Picture"), SELECT_PICTURE);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MessageInfo info = messages.get(position);
                if (info.getType().equalsIgnoreCase("image")) {
                    Intent intent = new Intent();
                    intent.setAction(android.content.Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.parse("file://" + info.getBody()), "image/*");
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        isActive = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        isActive = false;
    }

    private void setListAdapter() {
        ChatArrayAdapter chatArrayAdapter = new ChatArrayAdapter(getApplicationContext(), messages);
        listView.setAdapter(chatArrayAdapter);
    }

    private boolean sendChatMessage(){


        String text = chatText.getText().toString();
        Log.i("XMPPClient", "Sending text [" + text + "] to [" + to + "]");
        Message msg = new Message(to, Message.Type.chat);

        msg.setBody(text);
        try {
            connection.sendStanza(msg);
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        }



        messages.add(new MessageInfo(chatText.getText().toString(), true, "text"));
        chatText.setText("");
        side = !side;
        return true;
    }



    public void setConnection
            (XMPPConnection
                     connection) {

        this.connection = connection;
        if (connection != null) {
            // Add a packet listener to get messages sent to us


            StanzaFilter filter1 = MessageTypeFilter.CHAT;


            //PacketFilter filter = (PacketFilter) MessageTypeFilter.CHAT;
            connection.addAsyncStanzaListener(new StanzaListener() {
                @Override
                public void processPacket(Stanza packet) throws SmackException.NotConnectedException {
                    Message message = (Message) packet;
                    if (message.getBody() != null) {
                        String fromName = XmppStringUtils.parseBareJid(message.getFrom());
                        Log.i("XMPPClient", "Got text [" + message.getBody() + "] from [" + fromName + "]");
                        messages.add(new MessageInfo(message.getBody(), false, "text"));
                        // Add the incoming message to the list view
                        mHandler.post(new Runnable() {
                            public void run() {
                                setListAdapter();
                            }
                        });

                    }
                }
            }, filter1);
            receiveFile();


        }
    }

    public void receiveFile() {
        System.out.println("in ReceiveFile");
        Thread thread = new Thread() {
            public void run() {
                System.out.println("in Thread");
                // Create the file transfer manager
                final FileTransferManager managerListner = FileTransferManager.getInstanceFor(connection);
                FileTransferNegotiator.isServiceEnabled(connection);

                Log.i("File transfere manager", "created");

                // Create the listener
                managerListner
                        .addFileTransferListener(new FileTransferListener() {
                            public void fileTransferRequest(
                                    final FileTransferRequest request) {

                                Log.i("Recieve File",
                                        "new file transfere request");

                                Log.i("file request",
                                        "from" + request.getRequestor());

                                IncomingFileTransfer transfer = request
                                        .accept();

                                Log.i("Recieve File", "accepted");
                                try {

                                    File fileReceive = new File("/sdcard/" + request.getFileName());
                                    transfer.recieveFile(fileReceive);
                                    Log.d("Create File", fileReceive.getAbsolutePath());
                                    while (!transfer.isDone()
                                            || (transfer.getProgress() < 1)) {

                                        Thread.sleep(1000);
                                        Log.i("Recieve File: ",(transfer.getProgress() + " status "  + transfer.getStatus()));

                                        if (transfer.getStatus().equals(
                                                FileTransfer.Status.error)) {
                                            // Log.i("Error file",
                                            // transfer.getError().getMessage());
                                            Log.i("Recieve File ",
                                                    "cancelling still receiving : "
                                                            + (transfer
                                                            .getProgress())
                                                            + " status "
                                                            + transfer
                                                            .getStatus());
                                            transfer.cancel();

                                            break;
                                        }

                                    }
                                    messages.add(new MessageInfo(fileReceive.getAbsolutePath(), false, "image"));
                                    mHandler.post(new Runnable() {
                                        public void run() {
                                            setListAdapter();
                                        }
                                    });
                                } catch (InterruptedException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                } catch (SmackException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });

            }
        };
        thread.start();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();
                selectedImagePath = getPath(selectedImageUri);
                filePath = selectedImageUri.toString();
                Log.d("Parse Image", "Selected Path: " + (selectedImagePath != null ? selectedImagePath : filePath));
                try {
                    messages.add(new MessageInfo((selectedImagePath != null ? selectedImagePath : filePath), true, "image"));
                    // Add the incoming message to the list view
                    mHandler.post(new Runnable() {
                        public void run() {
                            setListAdapter();
                        }
                    });
                    sendImage(to, (selectedImagePath != null ? selectedImagePath : filePath));
                } catch (SmackException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public String getPath(Uri uri) {
        String res = null;
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(uri, proj, null, null, null);
        if(cursor.moveToFirst()){
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }

    private void sendImage(String to, String directory) throws SmackException {
        Log.d("Send Image", directory);
        FileTransferManager manager = FileTransferManager.getInstanceFor(connection);
        OutgoingFileTransfer transfer = manager.createOutgoingFileTransfer(to + "/Smack");
        transfer.sendFile(new File(directory), "File for operator");
    }

    @Override
    public void onBackPressed() {
        if (messages.size() > 0) {
            MessageInfo info = messages.get(messages.size() - 1);

            Intent intent = new Intent();
            if (info.getType().equalsIgnoreCase("image")) {
                intent.putExtra("message","*image");
            } else {
                intent.putExtra("message",info.getBody());
            }

            intent.putExtra("to", to);
            setResult(RESULT_OK, intent);
            finish();
        } else {
            finish();
        }

    }
}
