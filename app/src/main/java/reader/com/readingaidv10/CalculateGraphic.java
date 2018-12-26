package reader.com.readingaidv10;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.vision.text.Text;
import com.google.android.gms.vision.text.TextBlock;
import java.util.List;
import reader.com.readingaidv10.sampledata.GraphicOverlay;

/**
 * Graphic instance for rendering TextBlock position, size, and ID within an associated graphic
 * overlay view.
 */
public class CalculateGraphic extends GraphicOverlay.Graphic {

    private int mId;

    private static final int TEXT_COLOR = Color.WHITE;

    private static Paint sRectPaint;
    private static Paint sTextPaint;
    private static Paint resRectPaint;
    private static Paint resTextPaint;
    private final TextBlock mText;
    private String testString;

    CalculateGraphic(GraphicOverlay overlay, TextBlock text) {
        super(overlay);

        testString = "4-7";
        mText = text;
        resRectPaint = new Paint();
        resRectPaint.setColor(Color.YELLOW);
        resRectPaint.setStyle(Paint.Style.STROKE);
        resRectPaint.setStrokeWidth(4.0f);

        resTextPaint = new Paint();
        resTextPaint.setColor(Color.YELLOW);
        resTextPaint.setTextSize(54.0f);

        if (sRectPaint == null) {
            sRectPaint = new Paint();
            sRectPaint.setColor(TEXT_COLOR);
            sRectPaint.setStyle(Paint.Style.STROKE);
            sRectPaint.setStrokeWidth(4.0f);
        }

        if (sTextPaint == null) {
            sTextPaint = new Paint();
            sTextPaint.setColor(TEXT_COLOR);
            sTextPaint.setTextSize(54.0f);
        }
        // Redraw the overlay, as this graphic has been added.
        postInvalidate();
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        this.mId = id;
    }

    public TextBlock getTextBlock() {
        return mText;
    }

    /**
     * Checks whether a point is within the bounding box of this graphic.
     * The provided point should be relative to this graphic's containing overlay.
     * @param x An x parameter in the relative context of the canvas.
     * @param y A y parameter in the relative context of the canvas.
     * @return True if the provided point is contained within this graphic's bounding box.
     */
    public boolean contains(float x, float y) {
        TextBlock text = mText;
        if (text == null) {
            return false;
        }
        RectF rect = new RectF(text.getBoundingBox());
        rect.left = translateX(rect.left);
        rect.top = translateY(rect.top);
        rect.right = translateX(rect.right);
        rect.bottom = translateY(rect.bottom);
        return (rect.left < x && rect.right > x && rect.top < y && rect.bottom > y);
    }

    /**
     * Draws the text block annotations for position, size, and raw value on the supplied canvas.
     */
    @Override
    public void draw(Canvas canvas) {
        TextBlock text = mText;
        if (text == null) {
            return;
        }

        // Draws the bounding box around the TextBlock.
        RectF rect = new RectF(text.getBoundingBox());
        rect.left = translateX(rect.left);
        rect.top = translateY(rect.top);
        rect.right = translateX(rect.right);
        rect.bottom = translateY(rect.bottom);
        canvas.drawRect(rect, sRectPaint);

        RectF resultRect = new RectF(text.getBoundingBox());
        resultRect.left = translateX(resultRect.left + resultRect.left);
        resultRect.top = translateX(resultRect.top);
        resultRect.right = translateX(resultRect.right + resultRect.right);
        resultRect.bottom = translateX(resultRect.bottom );
        //canvas.drawRect(resultRect,resRectPaint);

        // Break the text into multiple lines and draw each one according to its own bounding box.
        List<? extends Text> textComponents = text.getComponents();
        for(Text currentText : textComponents) {
            float left = translateX(currentText.getBoundingBox().left);
            float bottom = translateY(currentText.getBoundingBox().bottom);
            canvas.drawText(currentText.getValue(), left, bottom, sTextPaint);
            if (!(CalculateOcrText(currentText.getValue()) == null && SortWrongText(currentText.getValue()) == null)) {
                try {
                    canvas.drawText(CalculateOcrText(SortWrongText(currentText.getValue())), resultRect.left, resultRect.bottom, resTextPaint);
                }
                catch (Exception nullEx)
                {
                    Log.e("127","Error");
                }
            }
            else canvas.drawText("null",resultRect.left,resultRect.bottom,resTextPaint);

        }
    }
    public int Add(int x, int y)    {
        return x + y;
    }
    public int Sub(int x, int y)    {
        return x - y;
    }
    public float Div(int x, int y) {
        if (y == 0)
        {
            return 0.0f;
        }
        else {
            return x / y;
        }
    }
    public int Mult(int x, int y)   {
        return x * y;
    }

    public String CalculateOcrText(String OcrText)    {
        //This method analyzes the text and "maths" it up as follows
        ////////////////////////////////////////////////////////////
        //PART 1 : Split text into an array using SubStrings
        //PART 2 : Locate .Math operator in array
        //PART 3 : Pass arguments to respective .Math function
        //PART 4 : Cast every result as String variable
        //PART 5 : Return result
        //PART 6 : Show result in canvas drwText method

        String result = null;
        OcrText.trim();
        String[] TextArray = new String[99];
        try {
            TextArray[0] = OcrText.substring(0, 1);
            TextArray[1] = OcrText.substring(1, 2);
            TextArray[2] = OcrText.substring(2, 3);
        }
        catch (Exception stopGivingMeProblems) {
            result = "null";
        }
        int A = 0;
        int B = 0;
            try {
                    A = Integer.valueOf(TextArray[0]);
                    B = Integer.valueOf(TextArray[2]);
            }
            catch (Exception e)
            {
               result = "";
            }
            if (TextArray[1] != null)
            {
                if (TextArray[1].equals("+")) {
                    result = Integer.toString(Add(A, B));
                }
                else if (TextArray[1].equals("-"))
                {
                    result = Integer.toString(Sub(A, B));
                }
                else if (TextArray[1].equals("*"))
                {
                    result = Integer.toString(Mult(A, B));
                }
                else if (TextArray[1].equals("x") || TextArray[1].equals("X"))
                {
                    result = Integer.toString(Mult(A, B));
                }
                else if (TextArray[1].equals("/"))
                {
                    if (Div(A, B) == 0.0f )
                    {
                        result = "Undefined";
                    }
                    else
                    result = Float.toString(Div(A, B));
                }
            }
            else result = null;

        return result;
    }
    public String SortWrongText(String wrongText)
    {
        //
        //This function changes text which might hve been scanned wrongly to what they should have been
        //
        String num1, num2, opt;
        try {
            num1 = wrongText.substring(0,1);
            opt = wrongText.substring(1,2);
            if (!(wrongText.substring(2,3) == null)) {
                num2 = wrongText.substring(2, 3);
            }
            else num2 = "";
            //
            //Check the various Strings for mistakes and correct them
            //
            checkError(num1);
            checkError(opt);
            checkError(num2);
            return num1 + opt + num2;
        }
        catch (Exception e){
            Log.e("Eception","check check error");
        }
        return " ";



    }
    public String checkError(String errorString)
    {
        //Mistake Strings
        String mis_T = "T";
        String mis_t= "t";
        String mis_X = "X";
        String mis_x = "x";
        String mis_I= "I";
        String mis_i = "i";
        String mis_S = "S";
        String mis_s = "s";
        String mis_O = "O";
        String mis_o = "o";

        if (errorString.equals(mis_T) || errorString.equals(mis_t))
        {
            errorString = "+";
        }
        else if (errorString.equals(mis_I) || errorString.equals(mis_i))
        {
            errorString = "1";
        }
        else if (errorString.equals(mis_S) || errorString.equals(mis_s))
        {
            errorString = "5";
        }
        else if (errorString.equals(mis_X) || errorString.equals(mis_x))
        {
            errorString = "*";
        }
        else if (errorString.equals(mis_O) || errorString.equals(mis_o))
        {
            errorString = "0";
        }
        return errorString;
    }
}
