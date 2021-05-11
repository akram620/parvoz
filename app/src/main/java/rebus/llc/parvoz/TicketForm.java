package rebus.llc.parvoz;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import rebus.llc.parvoz.R;
import rebus.llc.parvoz.adapters.CameraPickAdapter;
import rebus.llc.parvoz.db.DBSample;
import rebus.llc.parvoz.models.MyTicketModel;
import rebus.llc.parvoz.others.MyAsyncTask;
import rebus.llc.parvoz.others.Settings;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static rebus.llc.parvoz.others.Utilities.convertFromatDMYtoYMD;
import static rebus.llc.parvoz.others.Utilities.convertFromatYMDtoDMY;
import static rebus.llc.parvoz.others.Utilities.convertToBase64;
import static rebus.llc.parvoz.others.Utilities.convertToBitmap;
import static rebus.llc.parvoz.others.Utilities.getDate;
import static rebus.llc.parvoz.others.Utilities.getServerUrls;
import static rebus.llc.parvoz.others.Utilities.justConvertToBitmap;

public class TicketForm  extends AppCompatActivity implements MyAsyncTask.ResponseCame {
    Uri picUri;
    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected = new ArrayList<>();

    private final static int ALL_PERMISSIONS_RESULT = 107;

    private static String FILE_NAME = "photo.jpg";
    private  File photoFile;

    public  Context context;
    ImageView imageView;
    Spinner spinnerFrom;
    Spinner spinnerTo;
    String[] list_id_cities;
    String[] list_name_cities;
    ArrayAdapter<String> adapter_from;
    ArrayAdapter<String> adapter_to;
    MyAsyncTask myAsyncTask;

    Calendar cal_date;
    DatePickerDialog picker;
    EditText editTextDateFrom;
    private View mProgressView;
    private View mLoginFormView;
    ObjectMapper objectMapper;
    CheckBox checkBox;



    String base64Document;
    int id_my_flight;
    LinearLayout addNewDocument;
    LinearLayout recvisits;

    LinearLayout layoutFrom;
    LinearLayout layoutTo;
    TextView airoportTo;
    TextView airoportFrom;
    EditText editTextTime;
    TextInputLayout viewPasportNumber;
    TextInputLayout viewBD;
    TextInputLayout viewPasportGender;
    TextInputLayout viewName;
    TextInputLayout viewTime;
    TextInputLayout viewCost;
    TextInputLayout viewNomerRejsa;
    TextInputLayout viewNomerBileta;
    WebView webView;

    EditText editTextName;
    EditText editTextGender;
    EditText editTextBirthDate;
    EditText editTextPasportNumber;
    EditText editTextCost;
    EditText editTextNomerRejsa;
    EditText editTextNomerBileta;
    LinearLayout linearLayoutSave;
    LinearLayout linearLayoutCencelTicket;
    View linearLayoutScreenShot;
    View contentView;

    private int GALLERY = 1, CAMERA = 2;
    private String [] permissions = {"android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.ACCESS_FINE_LOCATION", "android.permission.READ_PHONE_STATE", "android.permission.SYSTEM_ALERT_WINDOW","android.permission.CAMERA"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_form);

        base64Document = "";
        context = this;

        TextView textTo = (TextView) findViewById(R.id.textTo);
        TextView textFrom = (TextView) findViewById(R.id.textFrom);

        airoportTo = (TextView) findViewById(R.id.airoportTo);
        airoportFrom = (TextView) findViewById(R.id.airoportFrom);

        spinnerFrom = (Spinner) findViewById(R.id.spinnerFrom);
        spinnerTo = (Spinner) findViewById(R.id.spinnerTo);
        editTextDateFrom = (EditText) findViewById(R.id.editTextDate);
        mProgressView = findViewById(R.id.login_progress);
        mLoginFormView = findViewById(R.id.login_form);
        editTextTime = (EditText) findViewById(R.id.editTextTime);
        addNewDocument  = (LinearLayout) findViewById(R.id.addNewDocument);
        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextGender = (EditText) findViewById(R.id.editTextGender);
        editTextBirthDate = (EditText) findViewById(R.id.editTextBirthDate);
        editTextPasportNumber = (EditText) findViewById(R.id.editTextPasportNumber);
        editTextCost = (EditText) findViewById(R.id.editTextCost);
        editTextNomerRejsa = (EditText) findViewById(R.id.editTextNomerRejsa);
        editTextNomerBileta = (EditText) findViewById(R.id.editTextNomerBileta);
        viewPasportNumber = (TextInputLayout) findViewById(R.id.viewPasportNumber);
        viewBD = (TextInputLayout) findViewById(R.id.viewBD);
        viewPasportGender = (TextInputLayout) findViewById(R.id.viewPasportGender);
        viewName = (TextInputLayout) findViewById(R.id.viewName);
        viewTime = (TextInputLayout) findViewById(R.id.viewTime);
        viewCost = (TextInputLayout) findViewById(R.id.viewCost);
        viewNomerRejsa = (TextInputLayout) findViewById(R.id.viewNomerRejsa);
        viewNomerBileta = (TextInputLayout) findViewById(R.id.viewNomerBileta);
        webView = (WebView) findViewById(R.id.webView);
        linearLayoutScreenShot = (View) findViewById(R.id.linearLayoutScreenShot);
        layoutFrom = (LinearLayout) findViewById(R.id.layoutFrom);
        layoutTo = (LinearLayout) findViewById(R.id.layoutTo);
        recvisits = (LinearLayout) findViewById(R.id.recvisits);
        checkBox = (CheckBox) findViewById(R.id.checkBox);

        cal_date = Calendar.getInstance();
        editTextDateFrom.setText(getDate("dd-MM-yyyy"));
        editTextDateFrom.setInputType(0);
        editTextDateFrom.setFocusable(false);
        editTextName.setInputType(0);
        editTextGender.setInputType(0);
        editTextBirthDate.setInputType(0);
        editTextPasportNumber.setInputType(0);
        editTextName.setFocusable(false);
        editTextGender.setFocusable(false);
        editTextBirthDate.setFocusable(false);
        editTextPasportNumber.setFocusable(false);
        editTextTime.setInputType(0);
        editTextTime.setFocusable(false);
        editTextCost.setFocusable(false);
        editTextCost.setInputType(0);
        editTextCost.setFocusable(false);
        editTextCost.setInputType(0);
        editTextNomerRejsa.setFocusable(false);
        editTextNomerRejsa.setInputType(0);
        editTextNomerBileta.setFocusable(false);
        editTextNomerBileta.setInputType(0);


        linearLayoutSave = (LinearLayout) findViewById(R.id.linearLayoutSave);
        linearLayoutCencelTicket = (LinearLayout) findViewById(R.id.linearLayoutCencelTicket);
        LinearLayout linearLayoutCencel = (LinearLayout) findViewById(R.id.linearLayoutCencel);
        ArrayList<String[]> arrayFrom = DBSample.getCitiesForSpinner(context);

        list_id_cities =  arrayFrom.get(0);//getResources().getStringArray(R.array.work_types);
        list_name_cities =  arrayFrom.get(1);

        adapter_from = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, list_name_cities );
        adapter_from.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFrom.setAdapter(adapter_from);

        adapter_to = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, list_name_cities );
        adapter_to.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTo.setAdapter(adapter_to);

        imageView = findViewById(R.id.IdProf);

        linearLayoutCencelTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmDialog();
            }
        });


        addNewDocument.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPictureDialog();
            }
        });

         contentView = (View) findViewById(R.id.contentView);

        linearLayoutScreenShot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap b = getBitmap(contentView);

                String root = Environment.getExternalStorageDirectory().toString();
                File myDir = new File(root + "/tickets_images");
                myDir.mkdirs();
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String fname = "ticket_"+ timeStamp +".jpg";
                File file = new File(myDir, fname);
                if (file.exists()) file.delete ();

                try {
                    FileOutputStream out = new FileOutputStream(file);
                    b.compress(Bitmap.CompressFormat.JPEG, 100, out);
                    out.flush();
                    out.close();
                    Toast.makeText(context, "Билет сохранен на телефоне в папке tickets_images!", Toast.LENGTH_SHORT).show();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        // requestMultiplePermissions(); // check permission
        int requestCode = 200;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, requestCode);
        }

        editTextDateFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Calendar cal = Calendar.getInstance();
                int year  = cal_date.get(Calendar.YEAR);
                int month = cal_date.get(Calendar.MONTH);
                int day   = cal_date.get(Calendar.DAY_OF_MONTH);

                picker = new DatePickerDialog(TicketForm.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                cal_date.set(Calendar.YEAR, year);
                                cal_date.set(Calendar.MONTH, monthOfYear);
                                cal_date.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                                SimpleDateFormat dateformat_yyyyMMdd = new SimpleDateFormat("yyyy-MM-dd");
                                SimpleDateFormat dateformat_ddMMyyyy = new SimpleDateFormat("dd-MM-yyyy");
                                String date_to_string_ddMMyyyy = dateformat_ddMMyyyy.format(cal_date.getTime());
                                editTextDateFrom.setText(date_to_string_ddMMyyyy);
                            }
                        }, year, month, day);
                picker.show();
            }
        });

        linearLayoutCencel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               TicketForm.this.finish();
            }
        });

        linearLayoutSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    attemptSave();
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
        });

        Intent intent = getIntent();

        if(intent.hasExtra("mode")){
            if(intent.getStringExtra("mode").equals("view")){//Если это режим просмотра
                id_my_flight = intent.getIntExtra("id_my_flight", 0);
                setData(id_my_flight);
                //viewMode();
                setTitle("Просмотр билета");
            }else if(intent.getStringExtra("mode").equals("edit")){//Если это режим редактирования
                id_my_flight = intent.getIntExtra("id_my_flight", 0);
                setData(id_my_flight);
                setTitle("Редактирование билета");
            }else if(intent.getStringExtra("mode").equals("new")){//Если это режим созодания нового
                if(intent.hasExtra("new_flight")){
                    for(int i = 0; i < list_id_cities.length; i++){
                        if(list_id_cities[i].equals(""+intent.getIntExtra("gorod_vyleta_id", 0))){
                            spinnerFrom.setSelection(i);
                        }
                        if(list_id_cities[i].equals(""+intent.getIntExtra("gorod_prileta_id", 0))){
                            spinnerTo.setSelection(i);
                        }
                        editTextDateFrom.setText(convertFromatYMDtoDMY(intent.getStringExtra("data_vyleta")));

                    }
                }
                id_my_flight = 0;
                setTitle("Заказ билета");
            }
        }
    }

    public Bitmap getBitmap(View view) {
        //view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache(true);
        Bitmap b = Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false);

//        final CountDownLatch signal = new CountDownLatch(1);
//        final Bitmap b = Bitmap.createBitmap(containerWidth, containerHeight, Bitmap.Config.ARGB_8888);
//        final AtomicBoolean ready = new AtomicBoolean(false);
//        w.post(new Runnable() {
//
//            @Override
//            public void run() {
//                w.setWebViewClient(new WebViewClient() {
//                    @Override
//                    public void onPageFinished(WebView view, String url) {
//                        ready.set(true);
//                    }
//                });
//                w.setPictureListener(new WebView.PictureListener() {
//                    @Override
//                    public void onNewPicture(WebView view, Picture picture) {
//                        if (ready.get()) {
//                            final Canvas c = new Canvas(b);
//                            view.draw(c);
//                            w.setPictureListener(null);
//                            signal.countDown();
//                        }
//                    }
//                });
//                w.layout(0, 0,webView.getWidth(),webView.getHeight());
//                w.loadDataWithBaseURL(baseURL, content, "text/html", "UTF-8", null);
//            }});
//        try {
//            signal.await();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        return b;
    }

    private void showPictureDialog() {
        final String [] items = new String[] {"Галерея", "Камера"};
        final Integer[] icons = new Integer[] { R.drawable.ic_photo_library, R.drawable.ic_camera};
        ListAdapter adapter = new CameraPickAdapter(this, items, icons);
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Выберите").setAdapter(adapter,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                choosePhotoFromGallary();
                                break;
                            case 1:
                                takePhotoFromCamera();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }

    public void choosePhotoFromGallary() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, GALLERY);
    }
    private void takePhotoFromCamera() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE); photoFile = getPhotoFile(FILE_NAME);
        Uri fileProvider = FileProvider.getUriForFile(this, "rebus.llc.parvoz.fileprovider", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);
        if (intent.resolveActivity(this.getPackageManager()) != null) {
            startActivityForResult(intent, CAMERA);
        } else {
            Toast.makeText(this, "Unable to open camera", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == this.RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERY) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                    base64Document = convertToBase64(bitmap);
                    Picasso.with(context).load(contentURI).into(imageView);
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        } else if (requestCode == CAMERA) {
            Bitmap selectedImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
            base64Document = convertToBase64(selectedImage);
            Picasso.with(context).load(photoFile.getAbsoluteFile()).into(imageView);
        }
    }

    private File getPhotoFile(String filaName) {
        File getImage = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        try {
            return File.createTempFile(filaName, ".jpg", getImage);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


//    public Intent getPickImageChooserIntent() {
//
//        Uri outputFileUri = getCaptureImageOutputUri();
//
//        List<Intent> allIntents = new ArrayList<>();
//        PackageManager packageManager = getPackageManager();
//
//        Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//        List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
//        for (ResolveInfo res : listCam) {
//            Intent intent = new Intent(captureIntent);
//            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
//            intent.setPackage(res.activityInfo.packageName);
//            if (outputFileUri != null) {
//                intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
//            }
//            allIntents.add(intent);
//        }
//
//        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
//        galleryIntent.setType("image/*");
//        List<ResolveInfo> listGallery = packageManager.queryIntentActivities(galleryIntent, 0);
//        for (ResolveInfo res : listGallery) {
//            Intent intent = new Intent(galleryIntent);
//            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
//            intent.setPackage(res.activityInfo.packageName);
//            allIntents.add(intent);
//        }
//
//        Intent mainIntent = allIntents.get(allIntents.size() - 1);
//        for (Intent intent : allIntents) {
//            if (intent.getComponent().getClassName().equals("com.android.documentsui.DocumentsActivity")) {
//                mainIntent = intent;
//                break;
//            }
//        }
//        allIntents.remove(mainIntent);
//
//        Intent chooserIntent = Intent.createChooser(mainIntent, "Select source");
//        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, allIntents.toArray(new Parcelable[allIntents.size()]));
//
//        return chooserIntent;
//    }


    private Uri getCaptureImageOutputUri() {
        Uri outputFileUri = null;
        File getImage = getExternalFilesDir("");
        if (getImage != null) {
            outputFileUri = Uri.fromFile(new File(getImage.getPath(), "profile.png"));
        }
            return outputFileUri;
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//
//        if (resultCode == Activity.RESULT_OK) {
//            if (requestCode == IMAGE_RESULT) {
//                String filePath = getImageFilePath(data);
//                if (filePath != null) {
//                    Bitmap selectedImage = BitmapFactory.decodeFile(filePath);
//                    //imageView.setImageBitmap(selectedImage);
//                    Log.d("Connection",selectedImage.toString());
//                    base64Document = convertToBase64(selectedImage);
//                    long length = base64Document.length();
//
//                    Log.d("Connection","LENGTH = "+length);
//                    //convertvToImage(base64Document);
//                    Bitmap bitmap = convertToBitmap(base64Document);
//                    imageView.setImageBitmap(bitmap);
//
//                }
//            }
//        }
//    }


    private String getImageFromFilePath(Intent data) {
        boolean isCamera = data == null || data.getData() == null;

        if (isCamera) return getCaptureImageOutputUri().getPath();
        else return getPathFromURI(data.getData());

    }

    public String getImageFilePath(Intent data) {
        return getImageFromFilePath(data);
    }

    private String getPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Audio.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable("pic_uri", picUri);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // get the file url
        picUri = savedInstanceState.getParcelable("pic_uri");
    }

    private ArrayList<String> findUnAskedPermissions(ArrayList<String> wanted) {
        ArrayList<String> result = new ArrayList<String>();

        for (String perm : wanted) {
            if (!hasPermission(perm)) {
                result.add(perm);
            }
        }

        return result;
    }

    private boolean hasPermission(String permission) {
        if (canMakeSmores()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
            }
        }
        return true;
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    private boolean canMakeSmores() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch (requestCode) {

            case ALL_PERMISSIONS_RESULT:
                for (String perms : permissionsToRequest) {
                    if (!hasPermission(perms)) {
                        permissionsRejected.add(perms);
                    }
                }

                if (permissionsRejected.size() > 0) {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(permissionsRejected.get(0))) {
                            showMessageOKCancel("These permissions are mandatory for the application. Please allow access.",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                                                requestPermissions(permissionsRejected.toArray(new String[permissionsRejected.size()]), ALL_PERMISSIONS_RESULT);
                                            }
                                        }
                                    });
                            return;
                        }
                    }
                }
                break;
        }

    }



    private void convertvToImage(String base64){
        // encode base64 from image
        base64 = base64.split(",")[1];
        byte[] data = Base64.decode(base64, Base64.DEFAULT);
        Bitmap decodeByte = BitmapFactory.decodeByteArray(data, 0, data.length);

        imageView.setImageBitmap(decodeByte);

    }

    private void attemptSave() throws JsonProcessingException {
        if (myAsyncTask != null) {
            return;
        }
        // Reset errors.
        editTextDateFrom.setError(null);

        String dataVyleta   = editTextDateFrom.getText().toString();

        boolean cancel = false;
        View focusView = null;

        List<NameValuePair> params = new ArrayList<NameValuePair>();

        if(spinnerFrom.getSelectedItemPosition()  == spinnerTo.getSelectedItemPosition()){
            Toast.makeText(context, "Вы выбрали два одинаковых города!", Toast.LENGTH_SHORT).show();
            cancel = true;
            focusView = spinnerFrom;
        }

        params.add(new BasicNameValuePair("gorod_vyleta_id", list_id_cities[spinnerFrom.getSelectedItemPosition()]));
        params.add(new BasicNameValuePair("gorod_prileta_id", list_id_cities[spinnerTo.getSelectedItemPosition()]));


        if (TextUtils.isEmpty(dataVyleta)) {
            editTextDateFrom.setError("Укажите дату вылета");
            focusView = editTextDateFrom;
            cancel = true;
        }else{
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

            try {

                Date date1 = format.parse(dataVyleta);
                Calendar calendar = Calendar.getInstance();
                Calendar calendar1 = Calendar.getInstance();
                calendar1.setTime(date1);

                String today_s = getDate("yyyy-MM-dd");
                Date today_date = format.parse(today_s);

                Log.d("Connection", convertFromatDMYtoYMD(dataVyleta));
                Log.d("Connection",  convertFromatDMYtoYMD(today_s));

                if(calendar.before(calendar1)){
                    editTextDateFrom.setError("Дата вылета должна быть не раньше сегодняшней");
                    focusView = editTextDateFrom;
                    cancel = true;
                }else {
                    params.add(new BasicNameValuePair("data_vyleta", convertFromatDMYtoYMD(dataVyleta)));
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }

        }

        try {
            if(checkBox.isChecked()) {
                if (base64Document.equals("")) {
                    Toast.makeText(context, "Добавьте документ!", Toast.LENGTH_SHORT).show();
                    cancel = true;
                    focusView = imageView;
                } else {
                    params.add(new BasicNameValuePair("base64_encoded_image_data", base64Document));
                }
            }
        }catch (NullPointerException msg){
            Toast.makeText(context, "Добавьте документ!", Toast.LENGTH_SHORT).show();
            cancel = true;
            focusView = imageView;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.

            showProgress(true);

            Log.d("Connection", new Settings(context).getToken());
            params.add(new BasicNameValuePair("login", new Settings(context).getLogin()));
            params.add(new BasicNameValuePair("user_id", new Settings(context).getIdUser()));
            params.add(new BasicNameValuePair("token", new Settings(context).getToken()));

            String url = getServerUrls()+"zakazy/sozdat";
            myAsyncTask = new MyAsyncTask(context, params, url, "post");
            myAsyncTask.setResponseListener(this);
            myAsyncTask.execute();
        }
    }

    @Override
    public void responseCame(boolean res, String message, JSONObject jObj) {

        if(res){
            MyTicketModel signInData = null;
            try {

                if(myAsyncTask.comandUrl.equals(getServerUrls()+"zakazy/otmenit")){
                    DBSample.cencelMyFlightData(context, id_my_flight);
                    Intent intent = new Intent();
                    intent.putExtra("activity", "TicketForm");
                    setResult(RESULT_OK, intent);
                    finish();
                }else{
                    objectMapper = new ObjectMapper();
                    signInData = objectMapper.readValue(jObj.toString(), MyTicketModel.class);

                    DBSample.addFlight(context, signInData);
                    //cleanTicketDocs(context, id_my_flight);
                    DBSample.addFDocument(context, signInData.getId(), base64Document);
                    Intent intent = new Intent();
                    intent.putExtra("activity", "TicketForm");
                    setResult(RESULT_OK, intent);
                    finish();
                }
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("Connection", e.getMessage());
            }
        }else {
            myAsyncTask = null;
            showProgress(false);
        }
    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    public void cancelTickets(){

        if( myAsyncTask != null) return;

        String url = getServerUrls()+"zakazy/otmenit";
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("login", new Settings(context).getLogin()));
        params.add(new BasicNameValuePair("user_id", new Settings(context).getIdUser()));
        params.add(new BasicNameValuePair("token", new Settings(context).getToken()));
        params.add(new BasicNameValuePair("order_id", ""+id_my_flight));

        myAsyncTask = new MyAsyncTask(context, params, url, "post");
        myAsyncTask.setResponseListener(this);
        myAsyncTask.execute();
    }

    public void confirmDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(TicketForm.this);
        builder.setCancelable(true);
        builder.setTitle("Подтверждение");
        builder.setMessage("Вы действительно хотите отменить заказ?");
        builder.setPositiveButton("Да",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        cancelTickets();
                        dialog.dismiss();
                    }
                });
        builder.setNegativeButton("Нет", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.FROYO)
    private void setData(int id){

        spinnerTo.setEnabled(false);
        spinnerFrom.setEnabled(false);

        editTextDateFrom.setOnClickListener(null);
        linearLayoutSave.setVisibility(View.GONE);
        addNewDocument.setVisibility(View.GONE);
        //checkBox.setVisibility(View.GONE);

        MyTicketModel myTicketModel = DBSample.getMyTicket(context, id);

        if(!myTicketModel.getStatus_id().equals("novyj") && !myTicketModel.getStatus_id().equals("otmenjon")){
            layoutFrom.setVisibility(View.VISIBLE);
            layoutTo.setVisibility(View.VISIBLE);
            viewPasportNumber.setVisibility(View.VISIBLE);
            viewBD.setVisibility(View.VISIBLE);
            viewPasportGender.setVisibility(View.VISIBLE);
            viewName.setVisibility(View.VISIBLE);
            viewTime.setVisibility(View.VISIBLE);
            viewCost.setVisibility(View.VISIBLE);
            viewNomerRejsa.setVisibility(View.VISIBLE);
            viewNomerBileta.setVisibility(View.VISIBLE);

            editTextName.setText(myTicketModel.getSurname()+" "+myTicketModel.getFirst_name());
            if(myTicketModel.getGender_id() == 1){
                editTextGender.setText("Мужской");
            }else if(myTicketModel.getGender_id() == 2){
                editTextGender.setText("Женский");
            }

            editTextPasportNumber.setText(myTicketModel.getPassport_number()+' '+myTicketModel.getPassport_series());
            if (myTicketModel.getValjuta() != null) {
                editTextCost.setText("" + myTicketModel.getStoimost() + " " + myTicketModel.getValjuta());
            }
            editTextNomerRejsa.setText(myTicketModel.getNomer_rejsa());
            editTextNomerBileta.setText(myTicketModel.getNomer_bileta());
            airoportTo.setText(myTicketModel.getAeroport_prileta_name());
            airoportFrom.setText(myTicketModel.getAeroport_vyleta_name());
            editTextTime.setText(myTicketModel.getVremja_vyleta());
        }else{
            viewPasportNumber.setVisibility(View.GONE);
            viewBD.setVisibility(View.GONE);
            viewPasportGender.setVisibility(View.GONE);
            viewName.setVisibility(View.GONE);
            viewTime.setVisibility(View.GONE);
            viewCost.setVisibility(View.GONE);
            viewNomerRejsa.setVisibility(View.GONE);
            viewNomerBileta.setVisibility(View.GONE);


        }


        if(myTicketModel.getStatus_id().equals("novyj") || myTicketModel.getStatus_id().equals("obrabatyvaetsja") || myTicketModel.getStatus_id().equals("ozhidaet-oplatu")){
            linearLayoutCencelTicket.setVisibility(View.VISIBLE);
            if(myTicketModel.getStatus_id().equals("obrabatyvaetsja") ){
                recvisits.setVisibility(View.VISIBLE);
            }
        }else{
            linearLayoutCencelTicket.setVisibility(View.GONE);
        }

        if(myTicketModel.getStatus_id().equals("ozhidaet-oplatu") ||  myTicketModel.getStatus_id().equals("oplachen")){
            editTextBirthDate.setText(convertFromatYMDtoDMY(myTicketModel.getBirthday()));
        }

        if(myTicketModel.getStatus_id().equals("oplachen")){

            linearLayoutScreenShot.setVisibility(View.VISIBLE);
            String html = getHtml(myTicketModel);
            String encodedHtml = Base64.encodeToString(html.getBytes(),
                    Base64.NO_PADDING);

            webView.loadDataWithBaseURL(null, html, "text/html", "utf-8",null);


//            WebSettings webSettings = webView.getSettings();
//            webSettings.setJavaScriptEnabled(true);
        }

        for(int i = 0; i < list_id_cities.length; i++){
            if(myTicketModel.getGorod_prileta_id() == Integer.valueOf(list_id_cities[i])){
                spinnerTo.setSelection(i);
            }
        }

        for(int i = 0; i < list_id_cities.length; i++){
            if(myTicketModel.getGorod_vyleta_id() == Integer.valueOf(list_id_cities[i])){
                spinnerFrom.setSelection(i);
            }
        }

        editTextDateFrom.setText(convertFromatYMDtoDMY(myTicketModel.getData_vyleta()));

        base64Document = DBSample.MyDocument(context, id);

        if(base64Document != null ){
            if(base64Document != "" ) {
                Bitmap bitmap = justConvertToBitmap(base64Document);
                imageView.setImageBitmap(bitmap);
            }
        }
    }

    public String getHtml(MyTicketModel model){
//        if(model.getNomer_bileta()!=null){
//            model.setNomer_bileta("");
//        }
//        if(model.getNomer_rejsa()!=null){
//            model.setNomer_rejsa("");
//        }
//        if(model.getValjuta()!=null){
//            model.setValjuta("");
//        }
//        if(model.getBagazh()!=null){
//            model.setBagazh("");
//        }
        return "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=<device-width>, initial-scale=1.0\">\n" +
                "    <meta http-equiv=\"X-UA-Compatible\" content=\"ie=edge\">\n" +
                "    <title>Document</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <div class=\"row\" style=\"display: table; width: 100%; height: 100px; table-layout: fixed; margin-bottom: 20px; \">\n" +
                        "<img width='100%' src=\"file:///android_asset/bg_parvoz.svg\">"+
                "        <!-- <div class=\"col\" style=\"display: table-cell;    \"></div>\n" +
                "        <div class=\"col\" style=\"display: table-cell; \">ЭЛЕКТРОННЫЙ БИЛЕТ <br>\n" +
                "        МАРШРУТНАЯ КВИТАНЦИЯ</div> -->\n" +
                "    </div>"+
                "    <div style=\"background: linear-gradient(to right, #387493, #d8eafa); width: 100%; height: 10px; margin-bottom: 10px;\"></div>\n" +
                "\n" +
                "\n" +
                "    <div class=\"row\" style=\"display: table; width: 100%; table-layout: fixed; font-size: small; border-spacing: 5px; background: linear-gradient(to right, #387493, #d8eafa);\">\n" +
                "            <div class=\"col\" style=\"display: table-cell; color: #ffffff; height: 100%;  width: 30%; \">Фамилия/Имя</div>\n" +
                "            <div class=\"col\" style=\"display: table-cell; color: #ffffff; height: 100%; width: 30%; \">Паспорт</div>\n" +
                "            <div class=\"col\" style=\"display: table-cell; color: #ffffff; height: 100%; \">Номер билета</div>\n" +
                "    </div>\n" +
                "    <div class=\"row\" style=\"display: table; width: 100%; table-layout: fixed; border-spacing: 5px; font-size: small; \">\n" +
                "            <div class=\"col\" style=\"display: table-cell; color: #000; height: 100%; width: 30%; \">"+model.getSurname()+" "+model.getFirst_name()+"</div>\n" +
                "            <div class=\"col\" style=\"display: table-cell; color: #000; height: 100%; width: 30%; \">"+model.getPassport_series()+" "+model.getPassport_number()+"</div>\n" +
                "            <div class=\"col\" style=\"display: table-cell; color: #000; height: 100%; \">"+ model.getNomer_bileta()+"</div>\n" +
                "    </div>\n" +
                "    <div style=\"display: table; width: 100%; table-layout: fixed; border-spacing: 5px; font-size: large; color: #387493;\">Данные полета</div>\n" +
                "    <div class=\"row\" style=\"display: table; width: 100%; table-layout: fixed; border-spacing: 5px; font-size: small; background: linear-gradient(to right, #387493, #d8eafa);\">\n" +
                "            \n" +
                "            <div class=\"col\" style=\"display: table-cell; color: #ffffff; height: 100%; width: 20%; \">Рейс</div>\n" +
                "            <div class=\"col\" style=\"display: table-cell; color: #ffffff; height: 100%; width: 20%; \">Дата</div>\n" +
                "            <div class=\"col\" style=\"display: table-cell; color: #ffffff; height: 100%; width: 20%; \">Направление</div>\n" +
                "    </div>\n" +
                "    <div class=\"row\" style=\"display: table; width: 100%; table-layout: fixed; border-spacing: 5px;  font-size: small; \">\n" +
                "            <div class=\"col\" style=\"display: table-cell; color: #000; height: 100%; width: 20%;\">"+model.getNomer_rejsa()+"</div>\n" +
                "            <div class=\"col\" style=\"display: table-cell; color: #000; height: 100%; width: 20%;\">"+convertFromatYMDtoDMY(model.getData_vyleta())+"</div>\n" +
                "            <div class=\"col\" style=\"display: table-cell; color: #000; height: 100%; width: 20%;\">"+model.getGorod_vyleta_name()+" "+model.getGorod_prileta_name()+"</div>\n" +
                "    </div>\n" +
                "\n" +
                "    <div class=\"row\" style=\"display: table; width: 100%; table-layout: fixed; border-spacing: 5px;  font-size: small; \">\n" +
                "        <div class=\"col\" style=\"display: table-cell; color: #000; height: 100%; width: 20%;\"></div>\n" +
                "        <div class=\"col\" style=\"display: table-cell; color: #000; height: 100%; width: 20%;\"></div>\n" +
                "    </div>\n" +
                "\n" +
                "    <div class=\"row\" style=\"display: table; width: 100%; table-layout: fixed; border-spacing: 5px; font-size: small; background: linear-gradient(to right, #387493, #d8eafa);\">\n" +
                "\n" +
                "        <div class=\"col\" style=\"display: table-cell; color: #ffffff; height: 100%; width: 20%; \">Время</div>\n" +
                "        <div class=\"col\" style=\"display: table-cell; color: #ffffff; height: 100%; width: 20%; \">Стоимость</div>\n" +
                "        <div class=\"col\" style=\"display: table-cell; color: #ffffff; height: 100%; width: 20%; \">Багаж</div>\n" +
                "    </div>\n" +
                "    <div class=\"row\" style=\"display: table; width: 100%; table-layout: fixed; border-spacing: 5px;  font-size: small; \">\n" +
                "        <div class=\"col\" style=\"display: table-cell; color: #000; height: 100%; width: 20%;\">"+model.getVremja_vyleta()+"</div>\n" +
                "        <div class=\"col\" style=\"display: table-cell; color: #000; height: 100%; width: 20%;\">"+model.getStoimost()+" "+model.getValjuta()+"</div>\n" +
                "        <div class=\"col\" style=\"display: table-cell; color: #000; height: 100%; width: 20%;\">"+model.getBagazh()+"</div>\n" +
                "    </div>\n" +



                "</body>\n" +
                "</html>";
    }
}