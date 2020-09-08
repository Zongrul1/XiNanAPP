package com.example.xinan.View;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Xfermode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import com.example.xinan.R;
import android.widget.ImageView;

import androidx.annotation.Nullable;
//https://www.jianshu.com/p/a5be5d3a0a05
public class CircleImageView extends androidx.appcompat.widget.AppCompatImageView {
    private int mSize;
    private Paint mPaint;
    private Xfermode mPorterDuffXfermode;
    private boolean first;

    public CircleImageView(Context context) {
        this(context,null);
    }

    public CircleImageView(Context context,AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CircleImageView(Context context,AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();

    }

    private void init(){
        mPaint = new Paint();
        mPaint.setDither(true);
        mPaint.setAntiAlias(true);
        mPorterDuffXfermode = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);
        first = false;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        mSize = Math.min(width,height);  //取宽高的最小值
        setMeasuredDimension(mSize,mSize);    //设置CircleImageView为等宽高
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //获取sourceBitmap，即通过xml或者java设置进来的图片
        Drawable drawable = getDrawable();
        if (drawable == null) return;
        Bitmap sourceBitmap = ((BitmapDrawable)getDrawable()).getBitmap();
        if (sourceBitmap != null){
            //对图片进行缩放，以适应控件的大小
            Bitmap bitmap = resizeBitmap(sourceBitmap,getWidth(),getHeight());
            //drawCircleBitmapByXfermode(canvas,bitmap);    //(1)利用PorterDuffXfermode实现
            drawCircleBitmapByShader(canvas,bitmap);    //(2)利用BitmapShader实现
        }
    }

    private Bitmap resizeBitmap(Bitmap sourceBitmap,int dstWidth,int dstHeight){
        int width = sourceBitmap.getWidth();
        int height = sourceBitmap.getHeight();

        float widthScale = ((float)dstWidth) / width;
        float heightScale = ((float)dstHeight) / height;

        //取最大缩放比
        float scale = Math.max(widthScale,heightScale);
        Matrix matrix = new Matrix();
        matrix.postScale(scale,scale);

        return Bitmap.createBitmap(sourceBitmap,0,0,width,height,matrix,true);
    }

    private void drawCircleBitmapByXfermode(Canvas canvas,Bitmap bitmap){
        int sc;
        if(!first) {
            sc = canvas.saveLayer(0, 0, getWidth(), getHeight(), null, Canvas.ALL_SAVE_FLAG);
        }
        else{
            sc = canvas.save();
        }
        //绘制dst层
        canvas.drawCircle(mSize / 2,mSize / 2,mSize / 2,mPaint);
        //设置图层混合模式为SRC_IN
        mPaint.setXfermode(mPorterDuffXfermode);
        //绘制src层
        canvas.drawBitmap(bitmap, 0, 0, mPaint);
        canvas.restoreToCount(sc);
        first = true;
    }

    private void drawCircleBitmapByShader(Canvas canvas,Bitmap bitmap){
        BitmapShader shader = new BitmapShader(bitmap,BitmapShader.TileMode.CLAMP,BitmapShader.TileMode.CLAMP);
        mPaint.setShader(shader);
        canvas.drawCircle(mSize / 2,mSize /2 ,mSize / 2,mPaint);
    }
}
