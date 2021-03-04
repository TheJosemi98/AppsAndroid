package com.example.podometroapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.podometroapp.Registros.RegistrosCuartosDeHora;
import com.example.podometroapp.Registros.RegistrosMinutos;
import com.example.podometroapp.progreso.Progreso;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.Calendar;
import java.util.FormatFlagsConversionMismatchException;

public class PodometroActivity extends AppCompatActivity {

    private Progreso progreso;
    private RegistrosCuartosDeHora registroCuarto;
    private Calendar CalendarCuartos;
    private int ind_minutosCuarto;
    private int ind_comienzoCuarto;
    private long tiempoEventoAnterior;
    private int counterStepAnterior;
    private boolean unlistener;
    private boolean actividadCreada;

    // State of application, used to register for sensors when app is restored
    public static final int STATE_OTHER = 0;
    public static final int STATE_COUNTER = 1;
    public static final int STATE_DETECTOR = 2;

    // Number of events to keep in queue and display on card
    private static final int EVENT_QUEUE_LENGTH = 10;

    // max batch latency is specified in microseconds
    private static final int BATCH_LATENCY_0 = 0; // no batching
    private static final int BATCH_LATENCY_10s = 10000000;
    private static final int BATCH_LATENCY_5s = 5000000;

    // Steps counted in current session
    private int mSteps = 0;
    // Value of the step counter sensor when the listener was registered.
    // (Total steps are calculated from this value.)
    private int mCounterSteps = 0;
    // State of the app (STATE_OTHER, STATE_COUNTER or STATE_DETECTOR)
    private int mState = STATE_OTHER;
    // When a listener is registered, the batch sensor delay in microseconds
    private int mMaxDelay = 0;

    @Override
    protected void onPause() {
        this.guardarCambios();
        unregisterListeners();
        super.onPause();
    }

    @Override
    protected void onStop(){
        this.guardarCambios();
        unregisterListeners();
        super.onStop();
    }

    @Override
    protected void onResume(){
        super.onResume();
        if(isKitkatWithStepSensor())
            registerEventListener(BATCH_LATENCY_0, Sensor.TYPE_STEP_COUNTER);
    }

    @Override
    protected void onDestroy(){
        unregisterListeners();
        this.guardarCambios();
        super.onDestroy();
    }


    @SuppressLint("SourceLockedOrientationActivity")
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_progreso_de_hoy, R.id.navigation_registros, R.id.navigation_metas)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
        actividadCreada = true;

        SharedPreferences sharedPreferences = getSharedPreferences("ProgresoYRegistros", Context.MODE_PRIVATE);
        String progresoRegistrosJSON = sharedPreferences.getString("Progreso","");
        this.checkPermisos();

        if(!progresoRegistrosJSON.equals("")){
            Log.i("Progreso","No es nulo");
            Gson gson = new Gson();
            progreso = gson.fromJson(progresoRegistrosJSON, Progreso.class);
            Log.i("Progreso",progresoRegistrosJSON);
            //this.eliminarDatos();
        }
        else{
            Log.i("Progreso","Es nulo");
            progreso = new Progreso();
            this.guardarCambios();
            Toast.makeText(this,"Introduzca sus datos en el apartado de metas",Toast.LENGTH_LONG).show();

        }

        if(isKitkatWithStepSensor())
            registerEventListener(BATCH_LATENCY_0, Sensor.TYPE_STEP_COUNTER);

        /*
        AlarmManager scheduler = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent  = new Intent(getApplicationContext(),ContadorPasosService.class);
        PendingIntent scheduledIntent = PendingIntent.getService(getApplicationContext(),0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        scheduler.setInexactRepeating(AlarmManager.RTC_WAKEUP,System.currentTimeMillis(),AlarmManager.INTERVAL_FIFTEEN_MINUTES,scheduledIntent);*/


    }


    //Función sacada de BatchStepSensor
    private boolean isKitkatWithStepSensor() {
        // BEGIN_INCLUDE(iskitkatsensor)
        // Require at least Android KitKat
        int currentApiVersion = android.os.Build.VERSION.SDK_INT;
        // Check that the device supports the step counter and detector sensors
        PackageManager packageManager = getPackageManager();
        return currentApiVersion >= android.os.Build.VERSION_CODES.KITKAT
                && packageManager.hasSystemFeature(PackageManager.FEATURE_SENSOR_STEP_COUNTER)
                && packageManager.hasSystemFeature(PackageManager.FEATURE_SENSOR_STEP_DETECTOR);
        // END_INCLUDE(iskitkatsensor)
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void registerEventListener(int maxdelay, int sensorType){
        mMaxDelay = maxdelay;
        if (sensorType == Sensor.TYPE_STEP_COUNTER) {
            mState = STATE_COUNTER;
        } else {
            mState = STATE_DETECTOR;
        }


        // Obtiene sensor por defecto
        SensorManager sensorManager = (SensorManager) getSystemService(Activity.SENSOR_SERVICE);
        // Sensor puede ser tanto step detector como counter detector
        Sensor sensor = sensorManager.getDefaultSensor(sensorType);

        //Registrar el listener según un delay
        final boolean batchMode = sensorManager.registerListener(mListener, sensor, SensorManager.SENSOR_DELAY_NORMAL, maxdelay);

        if (!batchMode) {
            Log.w("StepSensorSample", "Could not register sensor listener in batch mode, " +
                    "falling back to continuous mode.");
        }

    }

    private void unregisterListeners() {
        // BEGIN_INCLUDE(unregister)
        unlistener = true;
        SensorManager sensorManager = (SensorManager) getSystemService(Activity.SENSOR_SERVICE);
        sensorManager.unregisterListener(mListener);
        Log.i("StepSensorSample", "Sensor listener unregistered.");

    }

    /**
     * Listener that handles step sensor events for step detector and step counter sensors.
     */
    private final SensorEventListener mListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            Log.i("ActividadCreada",actividadCreada+"");
            long tiempoRegistroMilis;
            long difDelayMilis;

            //Calculo del tiempo transcurrido
            tiempoRegistroMilis = (event.timestamp / 1000000L);
            difDelayMilis = tiempoRegistroMilis - tiempoEventoAnterior;
            Log.i("tiempoEventoAnterior",tiempoEventoAnterior+"");
            Log.i("mCounterSteps",mCounterSteps+"");
            Log.i("events",event.values[0]+"");

            if(actividadCreada){
                mCounterSteps = 0;
                tiempoEventoAnterior = (event.timestamp / 1000000L);
                difDelayMilis = 0;
                actividadCreada = false;
                CalendarCuartos = Calendar.getInstance();
                registroCuarto = new RegistrosCuartosDeHora(CalendarCuartos.getTime(),progreso.getEdad(),progreso.getSexo(),progreso.getPeso(),progreso.getAltura());
                progreso.addRegistro(registroCuarto);
            }
            else
                tiempoEventoAnterior = tiempoRegistroMilis;


            //Calculo de los pasos
            if (mCounterSteps < 1) {
                // initial value
                mCounterSteps = (int) event.values[0];
            }

            Log.i("unlistener:",""+unlistener);
            if(unlistener){
                mCounterSteps = counterStepAnterior;
                unlistener = false;
            }


            counterStepAnterior = (int) event.values[0];
            Log.i("counterStepAnterior:",""+counterStepAnterior);
            mSteps = (int) event.values[0] - mCounterSteps;

            Log.i("Numero Pasos:",""+mSteps);
            Log.i("Tiempo:",""+difDelayMilis);

            registroCuarto.addPasos(mSteps,difDelayMilis);
            progreso.setLastRegistro(registroCuarto);
            CalendarCuartos = Calendar.getInstance();
            registroCuarto = new RegistrosCuartosDeHora(CalendarCuartos.getTime(),progreso.getEdad(),progreso.getSexo(),progreso.getPeso(),progreso.getAltura());
            progreso.addRegistro(registroCuarto);
            this.guardarRegistros();
            mSteps = 0;

            Log.i("StepSensorSample", "New step detected by STEP_COUNTER sensor. Total step count: " + mSteps);

        }

        public void guardarRegistros(){
            Gson gson = new Gson();
            String progresoRegistroJSON = gson.toJson(progreso);
            Log.i("progresoJSON",progresoRegistroJSON);
            SharedPreferences sharedPreferences = getSharedPreferences("ProgresoYRegistros", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("Progreso",progresoRegistroJSON);
            editor.commit();
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    public void guardarCambios(){
        Gson gson = new Gson();
        String progresoRegistroJSON = gson.toJson(progreso);
        Log.i("progresoJSON",progresoRegistroJSON);

        SharedPreferences sharedPreferences = getSharedPreferences("ProgresoYRegistros", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("Progreso",progresoRegistroJSON);
        editor.commit();

    }

    public void aplicarCambios(View view){

        if(view.getId() == R.id.boton_aplicar_metas){

            EditText pasosObjetivo = findViewById(R.id.pasos_metas_edit);
            EditText caloriasObjetivo = findViewById(R.id.calorias_meta_edit);
            EditText pesoText = findViewById(R.id.input_peso);
            EditText edadText = findViewById(R.id.input_edad);
            EditText alturaText = findViewById(R.id.input_altura);
            RadioButton masculinoRadioButton = findViewById(R.id.radioButtonMasculino);
            RadioButton femeninoRadioButton = findViewById(R.id.radioButtonFemenino);

            Log.i("pasosobjetivo",pasosObjetivo.getText().toString());

            if(pasosObjetivo.getText().toString().isEmpty() || caloriasObjetivo.getText().toString().isEmpty() || pesoText.getText().toString().isEmpty() || edadText.getText().toString().isEmpty() || alturaText.toString().isEmpty() )
                Toast.makeText(this,"Debe rellenar todos los apartados",Toast.LENGTH_LONG).show();
            else{
                progreso.setAltura(Double.parseDouble(alturaText.getText().toString()));
                progreso.setEdad(Integer.parseInt(edadText.getText().toString()));
                progreso.setPeso(Double.parseDouble(pesoText.getText().toString()));
                progreso.setPasosObjetivo(Integer.parseInt(pasosObjetivo.getText().toString()));
                progreso.setCaloriasObjetivo(Integer.parseInt(caloriasObjetivo.getText().toString()));
                Toast.makeText(this,"¡¡Cambios guardados!!",Toast.LENGTH_LONG).show();
            }

            if(masculinoRadioButton.isChecked())
                progreso.setSexo(1);
            else if(femeninoRadioButton.isChecked())
                progreso.setSexo(0);
            else
                progreso.setSexo(1);

            this.guardarCambios();

        }

    }

    public void checkPermisos(){

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            int permissionCheckEscritura  = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            int permissionCheckLectura = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
            int permissionCheckCalendario = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALENDAR);
            if(permissionCheckEscritura != PackageManager.PERMISSION_GRANTED && permissionCheckLectura != PackageManager.PERMISSION_GRANTED){
                Log.i("Mensaje_info_Storage_W","No hay permisos de escritura");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 225);
                Log.i("Mensaje_info_Storage_R","No hay permisos de lectura");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 225);
            }
            else if(permissionCheckEscritura == PackageManager.PERMISSION_GRANTED && permissionCheckLectura != PackageManager.PERMISSION_GRANTED){
                Log.i("Mensaje_info_Storage_W","Hay permisos de escritura");
                Log.i("Mensaje_info_Storage_R","No hay permisos de lectura");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 225);
            }
            else if(permissionCheckEscritura != PackageManager.PERMISSION_GRANTED && permissionCheckLectura == PackageManager.PERMISSION_GRANTED){
                Log.i("Mensaje_info_Storage_R","Hay permisos de lectura");
                Log.i("Mensaje_info_Storage_W","No hay permisos de escritura");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 225);
            }
            else{
                Log.i("Mensaje_info_Storage_R","Hay permisos de lectura");
                Log.i("Mensaje_info_Storage_W","Hay permisos de escritura");
            }

            if(permissionCheckCalendario != PackageManager.PERMISSION_GRANTED)
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CALENDAR}, 225);


        }

    }

    public void setProgreso(){
        SharedPreferences sharedPreferences = getSharedPreferences("ProgresoYRegistros", Context.MODE_PRIVATE);
        String progresoRegistrosJSON = sharedPreferences.getString("Progreso","");
        Gson gson = new Gson();
        progreso = gson.fromJson(progresoRegistrosJSON, Progreso.class);
    }

    public void eliminarDatos(){
        SharedPreferences sharedPreferences = getSharedPreferences("ProgresoYRegistros", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();

    }



}
