package com.example.alexi.examen_2_u4;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class Principal extends AppCompatActivity implements SensorEventListener {
    Button btnComenzar;
    EditText edtTiempo;
    TextView txtX,txtY,txtZ;
    EditText edtResultado;
    SensorManager manager;
    Sensor giroscopio;
    Timer timer;
    TimerTask task;
     int Tiempo =0,suma=0,contador=0;
    double sumX=0,sumY=0,sumZ=0;
    boolean activa = true, bolSensor=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        btnComenzar = (Button)findViewById(R.id.btnComenzar);
        edtTiempo = (EditText)findViewById(R.id.edtTiempo);
        edtResultado = (EditText)findViewById(R.id.edtResultado);
        txtX = (TextView)findViewById(R.id.txtX);
        txtY = (TextView)findViewById(R.id.txtY);
        txtZ = (TextView)findViewById(R.id.txtZ);
        manager = (SensorManager) getSystemService(SENSOR_SERVICE);
        giroscopio = manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        btnComenzar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Tiempo= 1000*Integer.parseInt(edtTiempo.getText().toString());

                if (!activa){
                    task.cancel();
                    timer.cancel();
                    Toast.makeText(Principal.this, "Desactivo", Toast.LENGTH_SHORT).show();
                }else{
                    activa= false;
                    timer= new Timer();
                    task = new TimerTask() {
                        @Override
                        public void run() {
                            Principal.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    bolSensor=true;
                                    double promX=0,promY=0,promZ=0;
                                    //Accion
                                    Toast.makeText(Principal.this, "Activo", Toast.LENGTH_SHORT).show();

                                    promX=sumX/contador;
                                    promY=sumY/contador;
                                    promZ=sumZ/contador;
                                    txtX.setText(promX+"");
                                    txtY.setText(promY+"");
                                    txtZ.setText(promZ+"");


                                    fileList();
                                }
                            });
                        }
                    };

                    timer.schedule(task, 0, Tiempo);
                    activa=false;
                }

                }
        });

    }

    @Override
    public void onSensorChanged(final SensorEvent sensorEvent) {
        if(bolSensor==true){
            suma++;
            contador++;
            sumX=sensorEvent.values[0]+sumX;
            sumY=sensorEvent.values[1]+sumY;
            sumZ=sensorEvent.values[2]+sumZ;

            edtResultado.setText("X: "+sensorEvent.values[0]+"\n"+
                    "Y: "+sensorEvent.values[1]+"\n"+
                    "Z: "+sensorEvent.values[2]);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
    @Override
    protected void onResume() {
        super.onResume();
        manager.registerListener(this,giroscopio,SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        manager.unregisterListener(this);
    }
}
