package com.cloud.filesystem.util;

import java.io.FileOutputStream;
import java.io.IOException;

public class FileUtils {

    //This method is rather slower with respect to below one : byteArrayToHexEncode
    public static String byteArrayToHex(byte[] a) {
        StringBuilder sb = new StringBuilder(a.length * 2);
        for(byte b: a)
            sb.append(String.format("%02x", b)).append("-");
        return sb.substring(0,sb.length()-1).toString();
    }

    private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();
    public static String byteArrayToHexEncode(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 3];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 3] = HEX_ARRAY[v >>> 4];
            hexChars[j * 3 + 1] = HEX_ARRAY[v & 0x0F];
            hexChars[j * 3 + 2] = '-';
        }
        String result = new String(hexChars);
        return result.substring(0,result.length()-1);
    }

    public static void writeBytesToFileClassic(byte[] bFile, String fileName) {
        String fileDest = "C:\\temp\\" + fileName;
        FileOutputStream fileOuputStream = null;

        try {
            fileOuputStream = new FileOutputStream(fileDest);
            fileOuputStream.write(bFile);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileOuputStream != null) {
                try {
                    fileOuputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
