package com.example.lab1;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor gravity;
    private Sensor acceleration;
    private Sensor trueGravity;
    private Sensor linAcc;
    private float[] gravityArr = null;
    private float[] accelerationArr = null;
    private float[] trueGravityArr = null;
    private float[] linAccArr = null;
    private float[] rotationMatrix = new float[9];
    private float[] orientation = new float[3];

    private ImageView compassImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        gravity = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        acceleration = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        trueGravity = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        linAcc = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        compassImg = findViewById(R.id.kompas);
    }

    @Override
    protected void onResume(){
        super.onResume();
        sensorManager.registerListener(this, gravity, sensorManager.SENSOR_DELAY_GAME);
        sensorManager.registerListener(this, acceleration, sensorManager.SENSOR_DELAY_GAME);
        sensorManager.registerListener(this, trueGravity, sensorManager.SENSOR_DELAY_GAME);
        sensorManager.registerListener(this, linAcc, sensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onPause(){
        super.onPause();
        sensorManager.unregisterListener(this, gravity);
        sensorManager.unregisterListener(this, acceleration);
        sensorManager.unregisterListener(this, trueGravity);
        sensorManager.unregisterListener(this, linAcc);
    }

    @Override
    public void onSensorChanged(SensorEvent event){
        switch (event.sensor.getType()){
            case Sensor.TYPE_MAGNETIC_FIELD:
                gravityArr = event.values.clone();
                break;
            case Sensor.TYPE_ACCELEROMETER:
                accelerationArr = event.values.clone();
                break;
            case Sensor.TYPE_GRAVITY:
                trueGravityArr = event.values.clone();
                break;
            case Sensor.TYPE_LINEAR_ACCELERATION:
                linAccArr = event.values.clone();
                break;
        }
        if (gravityArr != null && accelerationArr != null && trueGravityArr!= null && linAccArr != null){
            SensorManager.getRotationMatrix(rotationMatrix, null, accelerationArr, gravityArr);
            SensorManager.getOrientation(rotationMatrix, orientation);

            TextView accTxt = (TextView) findViewById(R.id.acc);
            accTxt.setText(String.valueOf(accelerationArr[0])+"\n"+String.valueOf(accelerationArr[1])+"\n"+String.valueOf(accelerationArr[2]));

            TextView trueGravTxt = (TextView) findViewById(R.id.trueGrav);
            trueGravTxt.setText(String.valueOf(trueGravityArr[0])+"\n"+String.valueOf(trueGravityArr[1])+"\n"+String.valueOf(trueGravityArr[2]));

            TextView linAccTxt = (TextView) findViewById(R.id.linAcc);
            linAccTxt.setText(String.valueOf(linAccArr[0])+"\n"+String.valueOf(linAccArr[1])+"\n"+String.valueOf(linAccArr[2]));

            TextView azymutTxt = (TextView) findViewById(R.id.azymuth);
            float degrees = (float)(Math.toDegrees(orientation[0]) + 360) % 360;
            azymutTxt.setText(String.valueOf(degrees));

            compassImg.setRotation(-degrees);

        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy){
    }
}