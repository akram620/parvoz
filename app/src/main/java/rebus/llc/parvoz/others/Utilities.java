package rebus.llc.parvoz.others;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utilities {
    
    public static String getDate(String format){
        DateFormat dateFormat = new SimpleDateFormat(format);
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static String convertFromatYMDtoDMY(String date){

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat format_2 = new SimpleDateFormat("dd-MM-yyyy");

        try {
            Date date1 = format.parse(date);
            return format_2.format(date1);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }

    public static String convertFromatDMYtoYMD(String date){

        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat format_2 = new SimpleDateFormat("yyyy-MM-dd");

        try {
            Date date1 = format.parse(date);
            return format_2.format(date1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static Bitmap convertToBitmap(String base64Str) throws IllegalArgumentException
    {
        base64Str = base64Str.replace("data:image/jpeg;base64,", "");
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 8;

        byte[] decodedBytes = Base64.decode(
                base64Str.substring(base64Str.indexOf(",")  + 1),
                Base64.DEFAULT
        );

        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length, options);
    }
    public static Bitmap justConvertToBitmap(String base64Str) throws IllegalArgumentException
    {
        base64Str = base64Str.replace("data:image/jpeg;base64,", "");

        byte[] decodedBytes = Base64.decode(
                base64Str.substring(base64Str.indexOf(",")  + 1),
                Base64.DEFAULT
        );

        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }


    public static String convertToBase64(Bitmap bitmap)
    {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, outputStream);

        return "data:image/jpeg;base64,"+Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT);
    }

    public  static String getServerUrls(){
        return "http://parvoz24.ru/api/";
    }
}
