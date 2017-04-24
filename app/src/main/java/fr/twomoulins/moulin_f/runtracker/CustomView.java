package fr.twomoulins.moulin_f.runtracker;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;

import static java.security.AccessController.getContext;

/**
 * Created by moulin_f on 24/04/2017.
 */

public class CustomView  extends View {

    private int width;
    private int height;
    private int inc;
    protected ShapeDrawable test;
    private MainActivity context;
    private int i;

    public CustomView(Context c) {
        super(c);
        context = (MainActivity)c;
        init();
    }

    public CustomView(Context c, AttributeSet as) {
        super(c, as);
        context = (MainActivity) c;
        init();
    }

    public CustomView(Context c, AttributeSet as, int default_style) {
        super(c, as, default_style);
        init();
    }

    public void resetGame(){
        DisplayMetrics metrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(metrics);
        width  = metrics.widthPixels;
        height = metrics.heightPixels;

        width = width / 8;
        height = (height -270) / 8;
        if (width < height)
            height = width;
        else
            width = height;
    }


    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpect)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpect);
        resetGame();
    }

    public void init() {

        /*square = new ShapeDrawable[8][8];
        for ( inc = 0; inc < 8; inc += 1){
            for (i = 0; i < 8; i += 1){
                square[inc][i] = new ShapeDrawable(new RectShape());
                square[inc][i].getPaint().setStyle(Paint.Style.STROKE);
                square[inc][i].getPaint().setStrokeWidth(2);
                square[inc][i].getPaint().setColor(Color.GRAY);
            }
        }

        pions = new ShapeDrawable[8][8];
        for ( inc = 0; inc < 8; inc += 1){
            for (i = 0; i < 8; i += 1){
                pions[inc][i] = new ShapeDrawable(new OvalShape());
                pions[inc][i].getPaint().setColor(Color.GRAY);
            }
        }
        boardGame = null;*/
    }

    public void drawPiece(int x, int y, int color){
        /*if (color == PIECE.BLACK.ordinal()){
            pions[x][y].getPaint().setColor(Color.BLACK);
        }
        else if (color == PIECE.WHITE.ordinal())
            pions[x][y].getPaint().setColor(Color.WHITE);
        if (color != PIECE.BLACK.ordinal() && color != PIECE.WHITE.ordinal()){
            pions[x][y].getPaint().setColor(Color.TRANSPARENT);
        }
        pions[x][y].setBounds(new Rect(((x) * width) + 10, ((y) * width) + 10,((x +1) * width) +10 , ((y +1) * width) + 10));*/
        invalidate();
    }

    public void onDraw(Canvas canvas) {
       /* super.onDraw(canvas);
        int x = 10;
        int y = 10;

        for (inc = 0; inc < 8; inc += 1){
            for (i = 0; i < 8; i += 1){

                square[inc][i].setBounds(x, y, width +x, height + y);
                square[inc][i].draw(canvas);
                x = width +x;
            }
            x = 10;
            y = height + y;
        }

        for (inc = 0; inc < 8; inc += 1){
            for (i = 0; i < 8; i += 1){
                if (pions[inc][i].getPaint().getColor() == Color.BLACK || pions[inc][i].getPaint().getColor() == Color.WHITE ) {
                    pions[inc][i].draw(canvas);
                }
            }
        }*/
    }

    public boolean onTouchEvent(MotionEvent event) {
        /*float x = event.getX();
        float y = event.getY();

        int tmp2 = height + 10;
        int tmp = width + 10;
        int myX = 0;
        int myY = 0;
        if (x > 10.0 && y > 10.0){
            while (x > tmp && myX < 8){
                tmp = tmp + width;
                myX += 1;
            }
            myY  = 0;
            while (y > tmp2 && myY < 8){
                tmp2 = tmp2 + height;
                myY +=1;
            }
        }
        if (myX != 8 && myY != 8 ){
            try {
                boardGame.checkIfCanPlay((int)myX, (int)myY);
            }catch (Exception e){
                pop(e.getMessage());
            }
            context.updateDisplayScore();
        }*/
        return super.onTouchEvent(event);
    }

    private void pop(final String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message);
        builder.setPositiveButton("New Game", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                init();
                resetGame();
                //context.updateDisplayScore();
            }
        });
        builder.setNegativeButton("Hide", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
