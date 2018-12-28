package renchaigao.com.zujuba.widgets;

import android.support.constraint.ConstraintLayout;
import android.widget.TextView;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Administrator on 2018/6/25/025.
 */
@Setter
@Getter
public class WidgetDateAndWeekSelect {
    private ConstraintLayout constraint;
    private TextView date_textView;
    private TextView week_textView;

    public void setDate_textView(TextView date_textView) {
        this.date_textView = date_textView;
    }

    public void setConstraint(ConstraintLayout constraint) {
        this.constraint = constraint;
    }

    public void setWeek_textView(TextView week_textView) {
        this.week_textView = week_textView;
    }

    public TextView getDate_textView() {
        return date_textView;
    }

    public TextView getWeek_textView() {
        return week_textView;
    }

    public ConstraintLayout getConstraint() {
        return constraint;
    }
}
