package com.example.sivagami.pedometer;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.TextView;

public class MainActivity extends Activity implements SensorEventListener {
    private SensorManager sensorManager;

    TextView t;
    Vibrator v;
    ObjectAnimator animation;
   private int count=0;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        t = (TextView)findViewById(R.id.textView);
       /* rotation = AnimationUtils.loadAnimation(this, R.anim.rotation);
        rotation.setRepeatCount(3);*/
        animation = ObjectAnimator.ofFloat(t, "rotationY", 0.0f, 360f);
        animation.setDuration(500);
        animation.setRepeatCount(0);
        animation.setInterpolator(new AccelerateDecelerateInterpolator());


        v = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            getAccelerometer(event);
        }

    }

    private void getAccelerometer(SensorEvent event) {
        float[] values = event.values;
        // Movement
        float x = values[0];
        float y = values[1];
        float z = values[2];

        float accelationSquareRoot = (x*x+y*y+z*z)
                / (SensorManager.GRAVITY_EARTH * SensorManager.GRAVITY_EARTH);



        if (accelationSquareRoot >= 2)
        {
            count++;
            t.setText(count+" ");
            animation.start();
            v.vibrate(100);

        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {


    }

    @Override
    protected void onResume() {
        super.onResume();
        // register this class as a listener for the orientation and
        // accelerometer sensors
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        // unregister listener
        super.onPause();
        sensorManager.unregisterListener(this);
    }
}