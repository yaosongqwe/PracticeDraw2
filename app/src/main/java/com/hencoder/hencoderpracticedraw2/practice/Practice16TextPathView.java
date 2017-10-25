package com.hencoder.hencoderpracticedraw2.practice;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Region;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class Practice16TextPathView extends View {
    private static final float UNDERLINE_CLEAR_GAP = 5.5f;
    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    Paint pathPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    Paint mStroke = new Paint();
    Path mUnderline = new Path();
    Path textPath = new Path();
    String text = "just a try for henCoder";
    private float density;

    public Practice16TextPathView(Context context) {
        super(context);
    }

    public Practice16TextPathView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public Practice16TextPathView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    {
        paint.setTextSize(60);

        // 使用 Paint.getTextPath() 来获取文字的 Path
        pathPaint.setTextSize(60);
        pathPaint.getTextPath(text, 0, text.length(), 0, 0, textPath);
        pathPaint.setStyle(Paint.Style.STROKE);

        density = getContext().getResources().getDisplayMetrics().density;
        mStroke.setStyle(Paint.Style.FILL_AND_STROKE);
        mStroke.setStrokeWidth(UNDERLINE_CLEAR_GAP * density);
        mStroke.setStrokeCap(Paint.Cap.BUTT);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
//        canvas.drawText(text, 50, 200, paint);

//        canvas.drawPath(textPath, pathPaint);

        Path strokedOutline = new Path();
        Rect mBounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), mBounds);
        // Create a rectangular region for the underline
        Rect underlineRect = new Rect(
                mBounds.left, (int) (3.0f * density),
                mBounds.right, (int) (3.5f * density));
        Region underlineRegion = new Region(underlineRect);

        // Create a region for the text outline and clip it with the underline
        Region outlineRegion = new Region();
        outlineRegion.setPath(textPath, underlineRegion);

        // Extract the resulting region's path, we now have a clipped version
        // of the text outline
        textPath.rewind();
        outlineRegion.getBoundaryPath(textPath);

        // Stroke the clipped text and get the result as a fill path
        mStroke.getFillPath(textPath, strokedOutline);

        // Create a region from the clipped stroked outline
        outlineRegion = new Region();
        outlineRegion.setPath(strokedOutline, new Region(mBounds));

        // Subtracts the clipped, stroked outline region from the underline
        underlineRegion.op(outlineRegion, Region.Op.DIFFERENCE);

        // Create a path from the underline region
        underlineRegion.getBoundaryPath(mUnderline);
        canvas.translate(
                (int) ((getWidth() - mBounds.width()) / 2.0f),
                (int) ((getHeight() - mBounds.height()) / 2.0f));
        canvas.drawText(text, 0,0,paint);
        canvas.drawPath(mUnderline, paint);
    }
}
