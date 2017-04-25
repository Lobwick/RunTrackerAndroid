package fr.twomoulins.moulin_f.runtracker;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import java.util.ArrayList;

/**
 * Created by moulin_f on 24/04/2017.
 */

public class CustomView  extends View {

    private ArrayList<Double> values;
    private int width;
    private int height;
    private int length;
    private int inc;
    protected ShapeDrawable[] square;
    private DisplayHistory context;
    private int i;

    public CustomView(Context c) {
        super(c);
        context = (DisplayHistory)c;
        length = 1;
    }

    public CustomView(Context c, AttributeSet as) {
        super(c, as);
        context = (DisplayHistory) c;
        length = 1;
    }

    public CustomView(Context c, AttributeSet as, int default_style) {
        super(c, as, default_style);
        length = 1;
    }

    public void getMetrics(){
        DisplayMetrics metrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(metrics);
        width  = metrics.widthPixels;
        height = metrics.heightPixels;
        width = width / length;
        height = height - 575;
    }


    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpect)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpect);
        getMetrics();
    }

    public void init(int length, ArrayList<Double> listDouble) {
        this.length = length;
        values = listDouble;
        square = new ShapeDrawable[length];
        for ( inc = 0; inc < length; inc += 1){
                square[inc] = new ShapeDrawable(new RectShape());
                square[inc].getPaint().setColor(Color.DKGRAY);
        }
        getMetrics();
        invalidate();
    }

    private double getMaxValue(){
        int i = 0;
        double res = 0;
        while (i < values.size()){
            if (values.get(i) >= res){
                res = values.get(i);
            }
            i++;
        }
        return res;
    }

    private int doubleToInt(double d){
        return Math.round((int)(d*100));
    }

    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int x = 0;
        int y = 0;
        int max;
        if (values != null) {
            max = doubleToInt(getMaxValue());
            for (inc = 0; inc < length; inc += 1) {
                int tmp = height - (height * (doubleToInt(values.get(inc)))) / max;
                square[inc].setBounds(x, tmp, width + x - 1, height);
                square[inc].draw(canvas);
                x = width + x + 1;
            }
        }

    }
}
