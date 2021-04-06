package rebus.llc.parvoz.others;

import android.annotation.TargetApi;
import android.os.Build;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Encrypt {
    public void main(String[] args)
    {
        System.out.println(md5(args[0]));
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    public String md5(String str)
    {
        if(!str.isEmpty()){
            StringBuilder sb = new StringBuilder();

            for (byte b : md5(str.getBytes()))
                sb.append(Integer.toHexString(0x100 + (b & 0xff)).substring(1));
            return sb.toString();
        }else
            return "";
    }

    public byte[] md5(byte[] data)
    {
        MessageDigest md5 = null;
        try {

            md5 = MessageDigest.getInstance("MD5");

        } catch (NoSuchAlgorithmException e){

            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        md5.update(data);
        return md5.digest();
    }


    private static String convertToHex(byte[] data) {
        StringBuffer buf = new StringBuffer();
        int length = data.length;
        for(int i = 0; i < length; ++i) {
            int halfbyte = (data[i] >>> 4) & 0x0F;
            int two_halfs = 0;
            do {
                if((0 <= halfbyte) && (halfbyte <= 9))
                    buf.append((char) ('0' + halfbyte));
                else
                    buf.append((char) ('a' + (halfbyte - 10)));
                halfbyte = data[i] & 0x0F;
            }
            while(++two_halfs < 1);
        }
        return buf.toString();
    }


    public static String SHA1(String text) {
        String result = "";

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] sha1hash = new byte[40];
            md.update(text.getBytes("iso-8859-1"), 0, text.length());
            sha1hash = md.digest();
            result = convertToHex(sha1hash);

        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return result;
    }

}
