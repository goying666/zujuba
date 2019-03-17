package renchaigao.com.zujuba.widgets;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.widget.ImageView;
import android.widget.TextView;

import renchaigao.com.zujuba.R;

public class WidgetToolbarNormal extends ConstraintLayout {

    private TextView titleTextView, secondTitleTextView;
    private ImageView goback;

    public WidgetToolbarNormal(Context context) {
        super(context);
        this.titleTextView = (TextView) findViewById(R.id.textView146);
        this.secondTitleTextView = (TextView) findViewById(R.id.textView147);
        this.goback = (ImageView) findViewById(R.id.imageView33);
    }

}
