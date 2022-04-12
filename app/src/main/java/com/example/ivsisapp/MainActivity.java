package com.example.ivsisapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class MainActivity extends AppCompatActivity {
    private RequestQueue queue;
    private EditText txtUsuario;
    private EditText txtPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        queue = Volley.newRequestQueue(this);
        txtUsuario = (EditText) findViewById(R.id.txtUsuario);
        txtPassword = (EditText) findViewById(R.id.txtApellidos);
    }
    
    public void login(View view){
        String usuario = txtUsuario.getText().toString();
        String password = txtPassword.getText().toString();
        
        if(usuario.length() < 1 || password.length() < 1 ){
            Toast.makeText(this, "Por favor llene todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }
        requestLogin();
        txtUsuario.setText("");
        txtPassword.setText("");
    }
    
    public void openRegisterform(View view){
        startActivity(new Intent("com.example.RegistroActivity"));
    }

    private void requestLogin(){
        String url = "https://ivsis-api.herokuapp.com/api/auth/login";
        String usuario = txtUsuario.getText().toString();
        String password = txtPassword.getText().toString();
        String stringBody = "{\"usuario\":\"" + usuario + "\",\"password\":\"" + password + "\"}";
        try {
            final JSONObject jsonBody= new JSONObject(stringBody);
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url,jsonBody, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        JSONObject mJsonObject = response.getJSONObject("usuario");

                        String nombre = mJsonObject.getString("nombre_completo");
                        Toast.makeText(MainActivity.this, "Loggin as " + nombre, Toast.LENGTH_SHORT).show();
                        Intent i1= new Intent(MainActivity.this,MenuPrincipal.class);
                        i1.putExtra("userName",nombre);
                        startActivity(i1);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    NetworkResponse response = error.networkResponse;
                    if (error instanceof ServerError && response != null) {
                        try {
                            String res = new String(response.data, HttpHeaderParser.parseCharset(response.headers, "utf-8"));

                            JSONObject obj = new JSONObject(res);
                            String mensaje = obj.getString("msg");
                            Toast.makeText(MainActivity.this, mensaje, Toast.LENGTH_SHORT).show();
                        } catch (UnsupportedEncodingException e1) {
                            // Couldn't properly decode data to string
                            e1.printStackTrace();
                        } catch (JSONException e2) {
                            // returned data is not JSONObject?
                            e2.printStackTrace();
                        }
                    }
                }
            });
            queue.add(request);
        } catch (JSONException e) {
            e.printStackTrace();
        }



    }
}