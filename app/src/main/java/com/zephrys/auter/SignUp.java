package com.zephrys.auter;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;

import javax.net.ssl.HostnameVerifier;


public class SignUp extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sign_up, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            final View rootView = inflater.inflate(R.layout.fragment_sign_up, container, false);


            Button b1 = (Button) rootView.findViewById(R.id.sign);

            b1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("I am here", "YE");
                    //get the various required fields
                    String aadhar = ((EditText) rootView.findViewById(R.id.aadhar)).getText().toString();
                    TextView t1 = (TextView) rootView.findViewById(R.id.op);
                    String first_name = ((EditText) rootView.findViewById(R.id.fname)).getText().toString();
                    String gender = ((EditText) rootView.findViewById(R.id.gender)).getText().toString();
                    String dob = ((EditText) rootView.findViewById(R.id.dob)).getText().toString();
                    //chages to true if logged in succesfully
                    Boolean details_correct = false;
                    // basic json handles name, age, gender. name matching is fuzzy ;)
                    String son1 = "{ \"aadhaar-id\": \""+aadhar+"\", \"device-id\": \"192.168.0.101\", \"modality\": \"demo\", \"certificate-type\": \"preprod\", \"demographics\": { \"name\": { \"matching-strategy\": \"P\",\"matching-value\": \"60\", \"name-value\": \""+first_name+"\" },\"gender\": \""+gender+"\", \"age\": \""+dob+"\"}, \"location\": { \"type\": \"gps\", \"latitude\": \"22.3\", \"longitude\": \"73.2\", \"altitude\": \"0\" } }";
                    //a default http client (request object form python)
                    DefaultHttpClient client = new DefaultHttpClient();
                    HostnameVerifier hostnameVerifier = org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;

                    //adding ssl capabilities
                    SchemeRegistry registry = new SchemeRegistry();
                    SSLSocketFactory socketFactory = SSLSocketFactory.getSocketFactory();
                    socketFactory.setHostnameVerifier((X509HostnameVerifier) hostnameVerifier);
                    registry.register(new Scheme("https", socketFactory, 443));
                    SingleClientConnManager mgr = new SingleClientConnManager(client.getParams(), registry);

                    DefaultHttpClient httpClient = new DefaultHttpClient(mgr, client.getParams());


                    HttpConnectionParams.setConnectionTimeout(httpClient.getParams(), 10000); //Timeout Limit



                    HttpResponse response;
                    //JSONObject json = new JSONObject();
//                    Log.d("I am here", "YE");
                    try {
                        HttpPost post = new HttpPost("https://ac.khoslalabs.com/hackgate/hackathon/auth/raw");
                        StringEntity se = new StringEntity(son1);
                        se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
                        post.setEntity(se);
                        response = httpClient.execute(post);

                    /*Checking response */
                        if(response!=null){
                            Log.d("RESPONSE", "FOUnD");
                            ResponseHandler<String> handler = new BasicResponseHandler();
                            String body = handler.handleResponse(response);
                            Log.d("stream", body);
                            if(body.indexOf("success\":true") > 0) {
                                details_correct = true;
                            }
                            t1.setText("Details are correct");
                        }

                    } catch(Exception e) {
                        e.printStackTrace();
                    }


                }
            });

            return rootView;
        }
    }
}
