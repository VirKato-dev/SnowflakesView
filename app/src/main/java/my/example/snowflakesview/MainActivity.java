package my.example.snowflakesview;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.SurfaceView;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import my.example.snowflakesview.surface.Snowflakes;
import my.example.snowflakesview.view.SnowflakesView;


public class MainActivity extends AppCompatActivity {

    private SnowflakesView sfv;
    private Snowflakes sf;

    private LinearLayout l_main;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        l_main = findViewById(R.id.l_main);

        sfv = findViewById(R.id.snowflakes);
//        sfv.setVisibility(View.GONE);

        sf = new Snowflakes(this);
        sf.addPicture(R.drawable.snowflakes2);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1, -1, 1);
        sf.setLayoutParams(lp);
        l_main.addView(sf, 0);

        // сначала падают синие снежинки
        sfv.setColor(Color.BLUE).setCount(50).setMaxSize(50);
        // меняем цвет следующих снежинок
        new Handler().postDelayed(() -> sfv
                .addPicture(R.drawable.snowflakes1)
                .addPicture(R.drawable.snowflakes2)
                .addPicture(R.drawable.snowflakes3)
                .addPicture(R.drawable.snowflakes4)
                .setColor(Color.CYAN).setMaxSize(40), 2000);
    }

}