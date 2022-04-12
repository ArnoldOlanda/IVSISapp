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

public class RegistroActivity extends AppCompatActivity {
    private RequestQueue queue;
    private EditText txtNombre;
    private EditText txtApellidos;
    private EditText txtDireccion;
    private EditText txtUsuario;
    private EditText txtPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        queue = Volley.newRequestQueue(this);
        txtNombre = (EditText) findViewById(R.id.txtNombre);
        txtApellidos = (EditText) findViewById(R.id.txtApellidos);
        txtDireccion = (EditText) findViewById(R.id.txtDireccion);
        txtUsuario = (EditText) findViewById(R.id.txtUsuarioRegister);
        txtPassword = (EditText) findViewById(R.id.txtPasswordRegister);
    }

    public void registerUser(View view){
        String nombre = txtNombre.getText().toString();
        String apellidos = txtApellidos.getText().toString();
        String direccion = txtDireccion.getText().toString();
        String usuario = txtUsuario.getText().toString();
        String password = txtPassword.getText().toString();
        if(nombre.length() < 1 || apellidos.length() < 1 || direccion.length() < 1 || usuario.length() < 1 || password.length() < 1){
            Toast.makeText(this, "Llene todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }
        getDataTest();


    }

    private void getDataTest(){
        String url = "https://ivsis-api.herokuapp.com/api/usuarios";
        String nombreCompleto = txtNombre.getText().toString() + " " + txtApellidos.getText().toString();
        String usuario = txtUsuario.getText().toString();
        String password = txtPassword.getText().toString();
        String direccion = txtDireccion.getText().toString();

        String stringBody = "{" +
                "\"nombre\":\"" + nombreCompleto + "\"," +
                "\"usuario\":\"" + usuario + "\"," +
                "\"password\":\"" + password + "\"," +
                "\"direccion\":\"" + direccion + "\"" +
                "}";

        try {
            final JSONObject jsonBody= new JSONObject(stringBody);
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonBody, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        //JSONArray mJsonArray = response.getJSONArray("msg");
                        //JSONObject mJsonObject = response.getJSONObject("msg");
                        String msg = response.getString("msg");
                        Toast.makeText(RegistroActivity.this, "Registro existoso por favor inicie sesion", Toast.LENGTH_SHORT).show();
                        finish();
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
                            // Now you can use any deserializer to make sense of data

                            JSONObject obj = new JSONObject(res);
                            System.out.println(obj.toString());
                            String mensaje = obj.getString("err");
                            Toast.makeText(RegistroActivity.this, mensaje, Toast.LENGTH_SHORT).show();
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
        txtNombre.setText("");
        txtApellidos.setText("");
        txtDireccion.setText("");
        txtUsuario.setText("");
        txtPassword.setText("");
    }
}