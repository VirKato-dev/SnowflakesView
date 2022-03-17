package my.example.snowflakesview.view;

import android.content.Context;
import android.graphics.BlendMode;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;


public class SnowflakesView extends View {

    /***
     * Снежинка
     */
    private static class Flake {
        public int x, y;
        public int size = 1;
        public int speed = 1;
        public float alfa = 1;
        public float alfaSpeed = 0;
    }


    private Random random = new Random();
    private Paint paint = new Paint();
    private int color = Color.MAGENTA;
    private int count = 20;

    /***
     * Количество снежинок
     */
    private ArrayList<Flake> flakes = new ArrayList<>();


    public SnowflakesView(Context context) {
        super(context);
    }

    public SnowflakesView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (attrs != null) {
            for (int i = 0; i < attrs.getAttributeCount(); i++) {
                switch (attrs.getAttributeName(i)) {
                    case "colorFlakes":
                        color = attrs.getAttributeIntValue(i, Color.MAGENTA);
                        break;
                    case "countFlakes":
                        count = attrs.getAttributeIntValue(i, 20);
                }
            }
        }
        init();
    }

    public SnowflakesView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public SnowflakesView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    /***
     * Задать максимальное количество снежинок
     * @param c количество
     * @return
     */
    public SnowflakesView setCount(int c) {
        count = c;
        return this;
    }

    /***
     * Задать цвет снежинок
     * @param c цвет
     * @return
     */
    public SnowflakesView setColor(int c) {
        color = c;
        return this;
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
//        paint.setShadowLayer(5, 0, 0, Color.BLACK);
        paint.setStrokeWidth(5);
        paint.setStyle(Paint.Style.FILL);
    }

    /***
     * Рисуем все снежинки
     * @param canvas полотно
     */
    private void drawFlakes(Canvas canvas) {
        while (flakes.size() < count) {
            flakes.add(genFlake());
        }
        // используем итератор, чтобы легко избавляться от лишних снежинок во время цикла
        Iterator<Flake> it = flakes.iterator();
        while (it.hasNext()) {
            Flake f = it.next();
            paint.setAlpha((int) (f.alfa * 255f));
            canvas.drawCircle(f.x, f.y, f.size, paint);

            f.y += f.speed;
            f.alfa -= f.alfaSpeed;

            if ((f.y > getHeight() + f.size) || f.alfa <= 0) {
                it.remove(); // ушла, удалили
            }
        }
        invalidate();
    }

    /***
     * Создать новую снежинку
     * @return
     */
    private Flake genFlake() {
        Flake f = new Flake();
        f.size = random.nextInt(19) + 2;
        f.speed = random.nextInt(f.size / 2) + 2;
        f.alfaSpeed = random.nextFloat() * 0.005f;
        f.x = random.nextInt(getWidth());
        f.y = random.nextInt(50) - 50;
        return f;
    }

}
