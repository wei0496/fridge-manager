package com.androidtutorialpoint.firebasegrocerylistapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.androidtutorialpoint.firebasegrocerylistapp.Item;
import com.googlecode.leptonica.android.AdaptiveMap;
import com.googlecode.leptonica.android.Binarize;
import com.googlecode.leptonica.android.Convert;
import com.googlecode.leptonica.android.Enhance;
import com.googlecode.leptonica.android.Pix;
import com.googlecode.leptonica.android.ReadFile;
import com.googlecode.leptonica.android.Rotate;
import com.googlecode.leptonica.android.Skew;
import com.googlecode.leptonica.android.WriteFile;
import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ListActivity extends AppCompatActivity {
    List<Item> list;
    MyAdapter adapter;
    String[] tag1s = {"meat", "veggies", "dairy", "ice-cream"};
    String[] tag2s = {"refrigerator", "freezer"};
    ListView lv;
    TextView tv[];//bottom


    /********** used for camera and OCR --start *************/
    String[] ocrResult;

    Button btnUseCamera,btnUseGallery;
    ImageView imVShown;

    static final int PICK_IMAGE = 100; //constants for intent code
    static final int REQUEST_TAKE_PHOTO = 1; //constants for intent code
    String mCurrentPhotoPath; //absolute path for new taken photo by camera
    private TessBaseAPI mTess; //Tess API reference
    String datapath = ""; //path to folder containing language data file
    private void copyFiles() {
        try {
            //location we want the file to be at
            String filepath = datapath + "/tessdata/eng.traineddata";

            //get access to AssetManager
            AssetManager assetManager = getAssets();
            //open byte streams for reading/writing
            //InputStream instream = assetManager.open("tessdata/eng.traineddata");
            InputStream instream = assetManager.open("tessdata/eng.traineddata");
            OutputStream outstream = new FileOutputStream(filepath);

            //copy the file to the location specified by filepath
            byte[] buffer = new byte[1024];
            int read;
            while ((read = instream.read(buffer)) != -1) {
                outstream.write(buffer, 0, read);
            }
            outstream.flush();
            outstream.close();
            instream.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    } //copy trained data to the phone
    private void checkFile(File dir) {
        //directory does not exist, but we can successfully create it
        if (!dir.exists()&& dir.mkdirs()){
            copyFiles();
        }
        //The directory exists, but there is no data file in it
        if(dir.exists()) {
            String datafilepath = datapath+ "/tessdata/eng.traineddata";
            File datafile = new File(datafilepath);
            if (!datafile.exists()) {
                copyFiles();
            }
        }
    } //check if trained data is on the phone, if not, copy one
    public void processImage(Bitmap image){

        /******* preprocess, don't change if its not needed.******/
        ReadFile rdf = new ReadFile();
        WriteFile wtf = new WriteFile();
        Pix pixtest = rdf.readBitmap(image);
        Convert cvt = new Convert();
        Binarize binarization = new Binarize();
        Enhance ehc = new Enhance();
        AdaptiveMap adaptiveMap = new AdaptiveMap();
        pixtest = cvt.convertTo8(pixtest);
        pixtest = ehc.unsharpMasking(pixtest);
        pixtest = binarization.sauvolaBinarizeTiled(pixtest);
        Skew skw = new Skew();
        float angle = skw.findSkew(pixtest);
        Log.i("angle: ",Float.toString(angle));
        Rotate rot = new Rotate();
        pixtest = rot.rotate(pixtest,angle);
        Bitmap bm2 = wtf.writeBitmap(pixtest);
        /******* preprocess, don't change if its not needed.******/

        imVShown.setImageBitmap(bm2); //show on the image view(for testing), delete it and remove the ImageView when test is over.

        mTess.setImage(bm2);
        String ocrRawData = mTess.getUTF8Text();
        String[] result = split(ocrRawData); //get the final OCR result;
    } //process the taken image
    private String[] split(String raw)  //split the raw result
    {
        List<String> resList = new ArrayList<>();
        String[] tempRaw = raw.split("\n");
        ArrayList<String> tempL = new ArrayList<>();
        for(String str:tempRaw)
        {
            if(str.length()>0)
                tempL.add(str);
        }
        String[] temp = new String[tempL.size()];
        temp = (String[]) tempL.toArray(temp);

        //whole food:
        boolean start = false;
        for(int i = 0; i<temp.length;i++)
        {
            Log.w("test::",temp[i]);

            //starmarket filter
            if(temp[i].matches("^=>.*-[0-9]*\\.[0-9][0-9] F$"))
                continue;

            // whole food/ starmarket /hmarket/
            if(temp[i].matches(".*\\bF$")){
                //WHOLE FOOD:
                Log.w("@::",Boolean.toString(temp[i].contains("@")));
//                Log.w("temp i-1::",temp[i-1]);
                if(temp[i].contains("@")) {
                    Log.w("temp2 i-1::",temp[i-1]);
                    resList.add(temp[i-1]);
                }
                else {

                    String[] group = temp[i].split(" ");
                    if(group.length==0||group[0]=="==>")
                    {
                        continue;
                    }
                    if(group.length ==0||group[0]=="==>")
                        continue;
                    int end = group.length-2;
                    if(group.length>3 && group[group.length-3].matches("\\d*"))
                        end = group.length-3;
                    StringBuilder sb =new StringBuilder();
                    for(int j = 0; j < end;j++)
                    {
                        sb.append(group[j]);
                        sb.append(" ");
                    }
                    resList.add(sb.toString());
                    Log.w("split::",sb.toString());




//                    Matcher m = Pattern.compile("^.*?(?=(\\$?[0-9]*[^A-Za-z0-9][0-9][0-9][ $| F]))").matcher(temp[i]);
//                    if(m.find()) {
//                        resList.add(m.group(0));

//                    }
                }
            }
            //target:
            if(temp[i].matches(".*\\bFN\\b .*")) {
                Matcher m = Pattern.compile("(?<=[0-9]\\b)(.*)\\b(?=FN)").matcher(temp[i]);

                if(m.find())
                    resList.add(m.group(0));

            }

        }

        return resList.toArray(new String[resList.size()]);
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    } //create camera intent and start camera activity
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    } //create file for saving image(created by camera activity)

    /**************** used for OCR and camera --end***************/




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_list);
        initView();
        initData();
        adapter = new MyAdapter();
        lv.setAdapter(adapter);

        /*********** OCR and camera --start ************/
        mTess = new TessBaseAPI();
        datapath = getFilesDir()+ "/tesseract/";
        checkFile(new File(datapath + "tessdata/"));
        mTess.init(datapath, "eng");
        btnUseCamera = (Button) findViewById(R.id.btnUseCamera);
        btnUseGallery = (Button) findViewById(R.id.btnUseGallery);
        imVShown = (ImageView) findViewById(R.id.imVShown);
        btnUseCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });
        btnUseGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(gallery, PICK_IMAGE);
            }
        });
        /*********** OCR and camera --end***************/
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {

        if(resultCode != RESULT_CANCELED){

            /*********** used for OCR and Camera --start **************/
            if (requestCode == PICK_IMAGE) {
                Uri IMAGE_URI = data.getData();
                try {
                    InputStream image_stream = getContentResolver().openInputStream(IMAGE_URI);
                    Bitmap bitmap= BitmapFactory.decodeStream(getContentResolver().openInputStream(IMAGE_URI));
                    processImage(bitmap);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
            else if (requestCode == REQUEST_TAKE_PHOTO) {
                Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath);
                processImage(bitmap);
            }
            /*********** used for OCR and Camera --end **************/
        }
    }
    private void initView(){
        lv = (ListView) findViewById(R.id.lv);
        tv = new TextView[4];
        tv[0] = (TextView) findViewById(R.id.home);
        tv[0].setTag(0);
        tv[0].setSelected(true);
        tv[1] = (TextView) findViewById(R.id.explore);
        tv[1].setTag(1);
        tv[2] = (TextView) findViewById(R.id.shopping);
        tv[2].setTag(2);
        tv[3] = (TextView) findViewById(R.id.account);
        tv[3].setTag(3);
        for (int i = 0;i<tv.length;i++){
            tv[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int p = (int) v.getTag();
                    //clear other focus
                    for (int i = 0;i<tv.length;i++){
                        tv[i].setTextColor(getResources().getColor(R.color.text_color));
                        tv[i].setSelected(false);
                    }
                    tv[p].setTextColor(getResources().getColor(R.color.blue_color));
                    tv[p].setSelected(true);
                }
            });
        }
    }
    private int random(int max){
        Random r = new Random();
        return r.nextInt(max)%max;
    }
    private void initData() {
        list = new ArrayList<>();
        Item item = new Item();
        item.id = 1;
        item.name = "Beef";
        item.tag1 = tag1s[random(tag1s.length)];//random
        item.tag2 = tag2s[random(tag2s.length)];//random
        item.date = "7 Days";
        list.add(item);
        item = new Item();
        item.id = 2;
        item.name = "Milk";
        item.tag1 = tag1s[random(tag1s.length)];//random
        item.tag2 = tag2s[random(tag2s.length)];//random
        item.date = "7 Days";
        list.add(item);
        item = new Item();
        item.id = 3;
        item.name = "Apple";
        item.tag1 = tag1s[random(tag1s.length)];//random
        item.tag2 = tag2s[random(tag2s.length)];//random
        item.date = "10 Days";
        list.add(item);
        item = new Item();
        item.id = 4;
        item.name = "Haagen-Dazs";
        item.tag1 = tag1s[random(tag1s.length)];//random
        item.tag2 = tag2s[random(tag2s.length)];//random
        item.date = "2 Days";
        list.add(item);
        item = new Item();
        item.id = 5;
        item.name = "Orange";
        item.tag1 = tag1s[random(tag1s.length)];//random
        item.tag2 = tag2s[random(tag2s.length)];//random
        item.date = "10 Days";
        list.add(item);
        item = new Item();
        item.id = 6;
        item.name = "Chicken";
        item.tag1 = tag1s[random(tag1s.length)];//random
        item.tag2 = tag2s[random(tag2s.length)];//random
        item.date = "5 Days";
        list.add(item);
    }

    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = LayoutInflater.from(ListActivity.this).inflate(R.layout.item_list, null);
            TextView id = (TextView) convertView.findViewById(R.id.id);
            id.setText(list.get(position).id+"");
            TextView name = (TextView) convertView.findViewById(R.id.name);
            name.setText(list.get(position).name);
            TextView tag1 = (TextView) convertView.findViewById(R.id.tag1);
            tag1.setText(list.get(position).tag1);
            tag1.setTag(position);
            TextView tag2 = (TextView) convertView.findViewById(R.id.tag2);
            tag2.setText(list.get(position).tag2);
            tag2.setTag(position);
            EditText et = (EditText) convertView.findViewById(R.id.date);
            et.setText(list.get(position).date);
            tag1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = (int) v.getTag();
                    ShowTag1Dialog(pos);
                }
            });
            tag2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = (int) v.getTag();
                    ShowTag2Dialog(pos);
                }
            });
            return convertView;
        }

    }

    private void ShowTag1Dialog(final int pos) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this, android.R.style.Theme_Holo_Light_Dialog);
        builder.setTitle("Please Choose:");
        builder.setItems(tag1s, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                list.get(pos).tag1 = tag1s[which];
                adapter.notifyDataSetChanged();
            }
        });
        builder.show();
    }

    private void ShowTag2Dialog(final int pos) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this, android.R.style.Theme_Holo_Light_Dialog);
        builder.setTitle("Please Choose:");
        builder.setItems(tag2s, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                list.get(pos).tag2 = tag2s[which];
                adapter.notifyDataSetChanged();
            }
        });
        builder.show();
    }
}
