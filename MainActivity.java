package com.example.davidsyoun.simpletodolist;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ListView;
import android.widget.Toast;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.*;

public class MainActivity extends AppCompatActivity {
    EditText et;
    ListView lv;
    Button btn;
    List<String> toDoArray;
    int indexArray = 0;
    public final static String fileName = "filesimpletoDoList.txt";
    private TextToSpeech tts;
    private boolean speechReady = false;
    ArrayAdapter<String> itemsAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        et = (EditText) findViewById(R.id.addText);
        lv = (ListView) findViewById(R.id.toDoList);
        btn = (Button) findViewById(R.id.addBtn);

        toDoArray = new ArrayList<String>();
        itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,toDoArray);
        lv.setAdapter(itemsAdapter);

        readFromFile();

        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                speechReady = true;
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = et.getText().toString();

                if (!message.isEmpty()) {
                    //saveStringToFile(fileName, message+"\n");
                    itemsAdapter.add(message);
                    et.setText("");
                    itemsAdapter.notifyDataSetChanged();
                    //saveStringToFile(fileName, toDoArray[indexArray]);
                    Toast toast = Toast.makeText(MainActivity.this, message +" is added", Toast.LENGTH_SHORT);
                    toast.show();
                }
                else {
                    Toast toast = Toast.makeText(MainActivity.this, "Please enter your to-do List", Toast.LENGTH_SHORT);
                    toast.show();
                }

            }
        });
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String text = toDoArray.get(position);
                if (speechReady) {
                    tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "myTTSid");
                }

            }
        });

        //remove
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String removed=toDoArray.get(position);
                toDoArray.remove(position);
                itemsAdapter.notifyDataSetChanged();
                Toast toast = Toast.makeText(MainActivity.this, removed +" is deleted", Toast.LENGTH_SHORT);
                toast.show();
                return true;
            }
        });
    }

    private void readFromFile(){
        int i=0;
        try{
            Scanner scan = new Scanner(openFileInput(fileName));
            while (scan.hasNextLine()) {
                String line = scan.nextLine();
                toDoArray.add(i, line);
                i++;
            }
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }
    }

    public void onStop(){
        super.onStop();
        String s="";
        for (int i=0; i<toDoArray.size();i++){
            s=s+toDoArray.get(i)+"\n";
        }
        saveStringToFile(s);

    }

    private void saveStringToFile(String s) {
        try {
            //fos = openFileOutput(fileName, Context.MODE_PRIVATE); //try this
            PrintStream fos = new PrintStream(openFileOutput(fileName, Context.MODE_PRIVATE ));
            fos.write(s.getBytes());
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    public static String getObjectFromFile(Context context, String filename) {
//        try {
//            FileInputStream fis = context.openFileInput(filename);
//             ois = new ObjectInputStream(fis);
//
//            String str = ois.readObject();
//            ois.close();
//
//            return str;
//
//        } catch (FileNotFoundException e) {
//            Log.e(LOG_TAG, "getObjectFromFile FileNotFoundException: " + e.getMessage());
//            return null;
//        } catch (IOException e) {
//            Log.e(LOG_TAG, "getObjectFromFile IOException: " + e.getMessage());
//            return null;
//        } catch (ClassNotFoundException e) {
//            Log.e(LOG_TAG, "getObjectFromFile ClassNotFoundException: " + e.getMessage());
//            return null;
//        } catch (Exception e) {// Catch exception if any
//            Log.e(LOG_TAG, "getBookmarksFromFile Exception: " + e.getMessage());
//            return null;
//        }
//    }
}