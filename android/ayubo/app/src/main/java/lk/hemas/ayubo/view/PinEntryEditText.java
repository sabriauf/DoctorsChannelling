package lk.hemas.ayubo.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.util.AttributeSet;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class PinEntryEditText extends AppCompatEditText {

    //instance
    private OnClickListener listener;
    private Paint mLinesPaint;

    //primary data
    float mSpace = 24; //24 dp by default
    float mCharSize = 0;
    float mNumChars = 6;
    float mLineSpacing = 8; //8dp by default
    private float mLineStroke = 1; //1dp by default
    int[][] mStates = new int[][]{
            new int[]{android.R.attr.state_selected}, // selected
            new int[]{android.R.attr.state_focused}, // focused
            new int[]{-android.R.attr.state_focused}, // unfocused
    };
    int[] mColors = new int[]{
            Color.GREEN,
            Color.BLACK,
            Color.GRAY
    };

    public PinEntryEditText(Context context) {
        super(context);
    }

    public PinEntryEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public PinEntryEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public void setInputNumbers(int no) {
        mNumChars = no;
    }

    private void init(Context context, AttributeSet attrs) {
        setBackgroundResource(0);

        float multi = context.getResources().getDisplayMetrics().density;
        mSpace = multi * mSpace;

        mLineStroke = multi * mLineStroke;
        mLinesPaint = new Paint(getPaint());
        mLinesPaint.setStrokeWidth(mLineStroke);

        mLineSpacing = multi * mLineSpacing; //convert to pixels
        mNumChars = attrs.getAttributeIntValue(null, "maxLength", (int) mNumChars);

        //Disable copy paste
        super.setCustomSelectionActionModeCallback(
                new ActionMode.Callback() {
                    public boolean onPrepareActionMode(ActionMode mode,
                                                       Menu menu) {
                        return false;
                    }

                    public void onDestroyActionMode(ActionMode mode) {
                    }

                    public boolean onCreateActionMode(ActionMode mode,
                                                      Menu menu) {
                        return false;
                    }

                    public boolean onActionItemClicked(ActionMode mode,
                                                       MenuItem item) {
                        return false;
                    }
                });

        //When tapped, move cursor to end of the text
        super.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setSelection(getText().length());
                if (listener != null) {
                    listener.onClick(v);
                }
            }
        });
    }

    @Override
    public void setOnClickListener(OnClickListener l) {
        listener = l;
    }

    @Override
    public void setCustomSelectionActionModeCallback(ActionMode.Callback actionModeCallback) {
        throw new RuntimeException("setCustomSelectionActionModeCallback() not supported.");
    }

    ColorStateList mColorStates = new ColorStateList(mStates, mColors);

    private int getColorForState(int... states) {
        return mColorStates.getColorForState(states, Color.GRAY);
    }

    /* next = is the current char the next character to be input? */
    private void updateColorForLines(boolean next) {
        if (isFocused()) {
            mLinesPaint.setColor(
                    getColorForState(android.R.attr.state_focused));
            if (next) {
                mLinesPaint.setColor(
                        getColorForState(android.R.attr.state_selected));
            }
        } else {
            mLinesPaint.setColor(
                    getColorForState(-android.R.attr.state_focused));
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //super.onDraw(canvas);
        int availableWidth = getWidth() - getPaddingRight() - getPaddingLeft();
        if (mSpace < 0) {
            mCharSize = (availableWidth / (mNumChars * 2 - 1));
        } else {
            mCharSize =
                    (availableWidth - (mSpace * (mNumChars - 1))) / mNumChars;
        }

        Editable text = getText();
        int textLength = text.length();
        float[] textWidths = new float[textLength];
        getPaint().getTextWidths(getText(), 0, textLength, textWidths);

        int startX = getPaddingLeft();
        int bottom = getHeight() - getPaddingBottom();

        for (int i = 0; i < mNumChars; i++) {
            updateColorForLines(i == textLength);
            canvas.drawLine(
                    startX, bottom, startX + mCharSize, bottom, getPaint());

            if (getText().length() > i) {
                float middle = startX + mCharSize / 2;
                canvas.drawText(text,
                        i,
                        i + 1,
                        middle - textWidths[0] / 2,
                        bottom - mLineSpacing,
                        getPaint());
            }

            if (mSpace < 0) {
                startX += mCharSize * 2;
            } else {
                startX += mCharSize + mSpace;
            }
        }
    }
}
