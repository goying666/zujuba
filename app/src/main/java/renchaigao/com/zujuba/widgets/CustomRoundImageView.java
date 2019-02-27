package renchaigao.com.zujuba.widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.os.Build;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.View;

public class CustomRoundImageView extends AppCompatImageView {
    float width, height;

    public CustomRoundImageView(Context context) {
        this(context, null);
        init(context, null);
    }

    public CustomRoundImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        init(context, attrs);
    }

    public CustomRoundImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (Build.VERSION.SDK_INT < 18) {
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        width = getWidth();
        height = getHeight();
    }

    private int cutR = 16;
    @Override
    protected void onDraw(Canvas canvas) {
        if (width >= cutR && height > cutR) {
            Path path = new Path();
            //四个圆角
            path.moveTo(cutR, 0);
            path.lineTo(width - cutR, 0);
            path.quadTo(width, 0, width, cutR);
            path.lineTo(width, height - cutR);
            path.quadTo(width, height, width - cutR, height);
            path.lineTo(cutR, height);
            path.quadTo(0, height, 0, height - cutR);
            path.lineTo(0, cutR);
            path.quadTo(0, 0, cutR, 0);

            canvas.clipPath(path);
        }
        super.onDraw(canvas);
    }

}