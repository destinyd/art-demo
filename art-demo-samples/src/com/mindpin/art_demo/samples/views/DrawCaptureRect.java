package com.mindpin.art_demo.samples.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.view.View;

public class DrawCaptureRect extends View {
	 private int mcolorfill;
	 private int mleft, mtop, mwidth, mheight;
	public DrawCaptureRect(Context context,int left, int top,int width, int height, int colorfill) {
		super(context);
		// TODO Auto-generated constructor stub
		this.mcolorfill = colorfill;
		this.mleft = left;
		this.mtop = top;
		this.mwidth = width;
		this.mheight = height;
	}
	@Override
	protected void onDraw(Canvas canvas) {
		Paint p = new Paint();
        //����
        p.setXfermode(new PorterDuffXfermode(Mode.CLEAR));
        canvas.drawPaint(p);
        p.setXfermode(new PorterDuffXfermode(Mode.SRC));
        
		Paint mpaint = new Paint();
		mpaint.setColor(mcolorfill);
		mpaint.setStyle(Paint.Style.FILL);
		mpaint.setStrokeWidth(2.0f);
		canvas.drawLine(mleft, mtop, mleft+mwidth, mtop, mpaint);
		canvas.drawLine(mleft+mwidth, mtop, mleft+mwidth, mtop+mheight, mpaint);
		canvas.drawLine(mleft, mtop, mleft, mtop+mheight, mpaint);
		canvas.drawLine(mleft, mtop+mheight, mleft+mwidth, mtop+mheight, mpaint);
		super.onDraw(canvas); 
	}

	public int getMleft() {
		return mleft;
	}

	public int getMtop() {
		return mtop;
	}

	public int getMwidth() {
		return mwidth;
	}

	public int getMheight() {
		return mheight;
	}
}
