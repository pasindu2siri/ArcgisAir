package com.example.arcgisair.models;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class DummyDataReader {
    Context context;

    public DummyDataReader(Context context) {
        this.context = context;
    }

    public String ReadTextFromFile(String file) {
        String data = "";
        AssetManager assetManager = context.getResources().getAssets();
        InputStream inputStream = null;
        try {
            inputStream = assetManager.open(file);
            StringBuilder buf = new StringBuilder();
            BufferedReader in =
                    new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            String str;
            while ((str = in.readLine()) != null) {
                buf.append(str);
            }
            in.close();
            data = buf.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }


    public String WriteTextToFile(String city){
        FileOutputStream outputStream = null;
        try {
            System.out.print("1:" + city);

            outputStream = context.openFileOutput("cities.txt", Context.MODE_PRIVATE);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.print(":" + city);

        }
        try {
            outputStream.write(city.getBytes());
        } catch (IOException e) {
            System.out.print("3:" + city);

            e.printStackTrace();
        }
        try {
            outputStream.close();
        } catch (IOException e) {
            System.out.print("4:" + city);

            e.printStackTrace();
        }
        System.out.print("1:" + city);

        return city;
    }

}
