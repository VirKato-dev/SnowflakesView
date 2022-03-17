package my.example.snowflakesview.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.Random;


public class Snowflakes extends View {

    /***
     * Снежинка
     */
    class Flake {
        public int x, y;
        public int size;
        public int speed;
        public boolean isAlive = false;
    }


    private Random random = new Random();
    private Paint paint = new Paint();
    private int color = Color.WHITE;
    private int count = 20;

    /***
     * Количество снежинок
     */
    private ArrayList<Flake> flakes = new ArrayList<>();


    public Snowflakes(Context context) {
        super(context);
    }

    public Snowflakes(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (attrs != null) {
            for (int i = 0; i < attrs.getAttributeCount(); i++) {
                switch (attrs.getAttributeName(i)) {
                    case "colorFlakes":
                        color = attrs.getAttributeIntValue(i, Color.WHITE);
                        break;
                    case "countFlakes":
                        count = attrs.getAttributeIntValue(i, 20);
                }
            }
        }
        init();
    }

    public Snowflakes(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public Snowflakes(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawFlakes(canvas);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int w = MeasureSpec.getSize(widthMeasureSpec);
        int h = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(w, h);
    }


    private void init() {
        paint.setColor(color);
        paint.setShadowLayer(5, 0, 0, Color.BLACK);
        paint.setStrokeWidth(5);
        paint.setStyle(Paint.Style.FILL);

        for (int i = 0; i < count; i++) flakes.add(new Flake());
    }


    private void drawFlakes(Canvas canvas) {
        for (Flake f : flakes) {
            if (f.isAlive) {
                canvas.drawCircle(f.x, f.y, f.size, paint);
            } else {
                f.size = random.nextInt(19) + 2;
                f.speed = random.nextInt(f.size / 2) + 2;
                f.x = random.nextInt(getWidth());
                f.y = random.nextInt(50) - 50;
                f.isAlive = true;
            }
            f.y += f.speed;
            if (f.y > getHeight() + f.size) f.isAlive = false;
        }
        invalidate();
    }

}
