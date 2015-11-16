package com.example.quangtv.xmppdemo.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quangtv.xmppdemo.entity.Connection;
import com.example.quangtv.xmppdemo.utils.Constant;
import com.example.quangtv.xmppdemo.utils.DialogUtils;
import com.example.quangtv.xmppdemo.R;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.SASLAuthentication;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smackx.iqregister.AccountManager;

import java.io.IOException;
import java.util.Collection;

import javax.net.ssl.SSLContext;

/**
 * Created by QuangTV on 11/6/15.
 */
public class RegisterActivity extends Activity {

    private ChatActivity xmppClient;
    TextView txtUsername;
    TextView txtPassword;
    Button btnRegis;
    Button btnLogin;
    String LOG_TAG = "RegisterActivity";
    String username;
    String password;
    Connection con = new Connection();
    private Handler mHandler = new Handler();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        txtPassword = (TextView) findViewById(R.id.password);
        txtUsername = (TextView) findViewById(R.id.username);
        btnRegis = (Button) findViewById(R.id.btnRegis);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnRegis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username = txtUsername.getText().toString();
                password = txtPassword.getText().toString();
                if (username == null || password == null || username.isEmpty() || password.isEmpty()) {
                    DialogUtils.showAlert(RegisterActivity.this, "Enter username and password");
                } else {
                    registration();
                }
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username = txtUsername.getText().toString();
                password = txtPassword.getText().toString();
                if (username == null || password == null || username.isEmpty() || password.isEmpty()) {
                    DialogUtils.showAlert(RegisterActivity.this, "Enter username and password");
                } else {
                    login();
                }

            }
        });




    }

    public void login() {
        final ProgressDialog dialog = ProgressDialog.show(this,
                "Connecting...", "Please wait...", false);

        Thread t = new Thread(new Runnable() {

            @Override
            public void run() {
                // Create a connection

                XMPPTCPConnection connection;

                XMPPTCPConnectionConfiguration.Builder config = XMPPTCPConnectionConfiguration.builder();
                config.setSecurityMode(ConnectionConfiguration.SecurityMode.required);
                SASLAuthentication.unBlacklistSASLMechanism("PLAIN");
                SASLAuthentication.blacklistSASLMechanism("DIGEST-MD5");
                config.setServiceName(Constant.HOST);
                config.setHost(Constant.HOST);
                config.setPort(Constant.PORT);
                config.setDebuggerEnabled(true);
                connection = new XMPPTCPConnection(config.build());

                try {
                    connection.connect();
                    Log.i(LOG_TAG,
                            "Connected to " + connection.getHost());
                    connection.login(username, password);
                    Log.d("Login", "Login as " + username + "@" + Constant.HOST + " Password: " + password);
                    Presence presence = new Presence(Presence.Type.available);
                    connection.sendStanza(presence);
                    con.setConnection(connection);
                    Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
                    intent.putExtra("username", username);
                    intent.putExtra("password", password);
                    startActivity(intent);
                    finish();
                    dialog.dismiss();
                } catch (SmackException | IOException  | XMPPException e) {
                    dialog.dismiss();
                    e.printStackTrace();
                    mHandler.post(new Runnable() {
                        public void run() {
                            Toast.makeText(RegisterActivity.this, "Something went wrong, please check!", Toast.LENGTH_SHORT ).show();
                        }
                    });
                }



                return;
            }
        });
        t.start();
    }


    public void registration() {
        final ProgressDialog dialog = ProgressDialog.show(this,
                "Connecting...", "Please wait...", false);

        Thread t = new Thread(new Runnable() {

            @Override
            public void run() {
                // Create a connection

                XMPPTCPConnection connection;

                XMPPTCPConnectionConfiguration.Builder config = XMPPTCPConnectionConfiguration.builder();
                config.setSecurityMode(ConnectionConfiguration.SecurityMode.required);
                SSLContext sc = null;
                SASLAuthentication.unBlacklistSASLMechanism("PLAIN");
                SASLAuthentication.blacklistSASLMechanism("DIGEST-MD5");
                //config.setUsernameAndPassword(username, password);
                config.setServiceName(Constant.HOST);
                config.setHost(Constant.HOST);
                config.setPort(Constant.PORT);
                config.setDebuggerEnabled(true);

                //config.setSocketFactory(SSLSocketFactory.getDefault());
                connection = new XMPPTCPConnection(config.build());

                try {
                    connection.connect();
                    Log.i(LOG_TAG,
                            "Connected to " + connection.getHost());
                    AccountManager accountManager = AccountManager.getInstance(connection);
                    accountManager.createAccount(username, password);
                    Log.d(LOG_TAG, "Create account " + username + " with password " + password + " successful!");

                    connection.login(username, password);
                    Log.d("Login", "Login as " + username + "@" + Constant.HOST + " Password: " + password);
                    Presence presence = new Presence(Presence.Type.available);
                    connection.sendStanza(presence);
                    con.setConnection(connection);
                    Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
                    intent.putExtra("username", username);
                    intent.putExtra("password", password);
                    startActivity(intent);
                    finish();
                    dialog.dismiss();

                } catch (SmackException | IOException e) {
                    dialog.dismiss();
                    e.printStackTrace();
                    mHandler.post(new Runnable() {
                        public void run() {
                            Toast.makeText(RegisterActivity.this, "Error when executing, try again!", Toast.LENGTH_SHORT ).show();
                        }
                    });
                } catch (XMPPException e) {
                    Log.d("Rerr", e.getMessage());
                    dialog.dismiss();

                    e.printStackTrace();
                    mHandler.post(new Runnable() {
                        public void run() {
                            DialogUtils.showAlert(RegisterActivity.this, "Username already registered!");
                        }
                    });

                }



                return;
            }
        });
        t.start();

    }
}
