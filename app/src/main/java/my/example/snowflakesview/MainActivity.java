package my.example.snowflakesview;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;

import my.example.snowflakesview.view.SnowflakesView;


public class MainActivity extends AppCompatActivity {

    private SnowflakesView sfv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sfv = findViewById(R.id.snowflakes);

        // сначала падают синие снежинки
        sfv.setColor(Color.BLUE).setCount(100).setMaxSize(50);
        // меняем цвет следующих снежинок
        new Handler().postDelayed(() -> sfv.setColor(Color.CYAN).setMaxSize(20), 2000);
    }



}