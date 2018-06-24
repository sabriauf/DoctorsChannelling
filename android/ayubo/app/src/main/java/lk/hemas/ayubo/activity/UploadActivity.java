package lk.hemas.ayubo.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import lk.hemas.ayubo.R;
import lk.hemas.ayubo.adapter.ImageAdapter;
import lk.hemas.ayubo.config.AppConfig;
import lk.hemas.ayubo.model.DownloadDataBuilder;
import lk.hemas.ayubo.model.Expert;
import lk.hemas.ayubo.model.SoapBasicParams;
import lk.hemas.ayubo.util.AppHandler;
import lk.hemas.ayubo.util.DownloadData;
import lk.hemas.ayubo.util.DownloadManager;
import lk.hemas.ayubo.util.TimeFormatter;

public class UploadActivity extends AppCompatActivity {

    //constants
    public static final String EXTRA_EXPERT_OBJECT = "expert_object";
    private static final int MY_CAMERA_PERMISSION_CODE = 101;
    private static final int CAMERA_REQUEST = 102;
    private static final int BROWSE_REQUEST = 103;

    //instances
    private Expert expert;
    private ImageAdapter adapter;

    //primary data
    private List<Uri> images = new ArrayList<>();

    //views
    private ProgressBar progressBar;
    private Button btnNext;
    private EditText edtNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        progressBar = findViewById(R.id.prg_upload_progress);
        btnNext = findViewById(R.id.btn_upload_next);
        edtNote = findViewById(R.id.edt_upload_note);

        setButtons();
        readExtra(getIntent());
        setRecyclerView();
        createAnAppointment();

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnNext.setEnabled(false);
                uploadImages();
            }
        });
    }

    private void setButtons() {
        View cameraButton = findViewById(R.id.layout_button_upload_camera);
        setButton(cameraButton, getString(R.string.take_snap), ContextCompat.getDrawable(this,
                android.R.drawable.ic_menu_camera));
        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA},
                            MY_CAMERA_PERMISSION_CODE);
                } else {
                    startCameraActivity();
                }
            }
        });

        View browseButton = findViewById(R.id.layout_button_upload_browse);
        setButton(browseButton, getString(R.string.from_device), ContextCompat.getDrawable(this,
                android.R.drawable.ic_menu_agenda));
        browseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, BROWSE_REQUEST);
            }
        });
    }

    private void setButton(View view, String action, Drawable drawable) {
        ImageView image = view.findViewById(R.id.img_image_button);
        TextView txtAction = view.findViewById(R.id.txt_image_button_action);

        txtAction.setText(action);
        image.setImageDrawable(drawable);
    }

    private void readExtra(Intent intent) {
        if (intent.getExtras() != null && intent.getExtras().containsKey(EXTRA_EXPERT_OBJECT)) {
            expert = (Expert) intent.getExtras().getSerializable(EXTRA_EXPERT_OBJECT);
            setExpertDetails();
        }
    }

    private void setExpertDetails() {
        CircleImageView imgExpert = findViewById(R.id.img_upload_doc);
        TextView txtExpertName = findViewById(R.id.txt_upload_name);
        TextView txtExpertSpeciality = findViewById(R.id.txt_upload_speciality);
        TextView txtTime = findViewById(R.id.txt_upload_time);
        TextView txtPrice = findViewById(R.id.txt_upload__price);

        Date time = TimeFormatter.stringToDate(expert.getLocations().get(0).getNext_available(), "yyyy-MM-dd hh:mm:ss");

        Glide.with(this).load(expert.getPicture()).into(imgExpert);
        txtExpertName.setText(expert.getName());
        txtExpertSpeciality.setText(expert.getSpeciality());
        txtTime.setText(TimeFormatter.millisecondsToString(time.getTime(), "dd-MM-yyyy hh:mm aa"));
        txtPrice.setText(expert.getLocations().get(0).getFee());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCameraActivity();
            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void startCameraActivity() {
        Intent cameraIntent = new
                Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

        File photoFile = null;
        try {
            photoFile = createImageFile();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        // Continue only if the File was successfully created
        if (photoFile != null) {
            Uri photoURI = FileProvider.getUriForFile(this,
                    "com.example.android.fileprovider",
                    photoFile);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            startActivityForResult(cameraIntent, CAMERA_REQUEST);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST) {
            if (resultCode != Activity.RESULT_OK)
                images.remove(images.size() - 1);
            adapter.notifyDataSetChanged();
        } else if (requestCode == BROWSE_REQUEST) {
            if (resultCode == RESULT_OK) {
                try {
                    Uri imageUri = data.getData();
                    images.add(imageUri);
                    adapter.notifyDataSetChanged();
                } catch (NullPointerException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = TimeFormatter.millisecondsToString(Calendar.getInstance().getTime().getTime(), "yyyyMMdd_HHmmss");
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        images.add(Uri.fromFile(image));

        return image;
    }

    private void setRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recycler_upload_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        adapter = new ImageAdapter(this, images);
        recyclerView.setAdapter(adapter);
    }

    private void createAnAppointment() {
        progressBar.setVisibility(View.VISIBLE);
        new DownloadData(this, new DownloadDataBuilder().init(AppConfig.URL_AYUBO_SOAP_REQUEST, 0, DownloadManager.POST_REQUEST).
                setParams(AppHandler.getSoapRequestParams(AppConfig.METHOD_SOAP_VIDEO_APPIONTMENT, new AppointmentParams().getSearchParams())).
                setType(AppConfig.SERVER_REQUEST_CONTENT_TYPE).setTimeout(AppConfig.SERVER_REQUEST_TIMEOUT)).
                setOnDownloadListener(DashboardActivity.class.getSimpleName(), new DownloadData.DownloadListener() {
                    @Override
                    public void onDownloadSuccess(String response, int what, int code) {
                        progressBar.setVisibility(View.GONE);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if(jsonObject.getInt("result") == 0) {
                                finish();
                                return;
                            } else {
                                Toast.makeText(UploadActivity.this, jsonObject.getString("error"), Toast.LENGTH_LONG).show();
                                finish();
                                return;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Toast.makeText(UploadActivity.this, "Failed to create an appointment", Toast.LENGTH_LONG).show();
                        finish();
                    }

                    @Override
                    public void onDownloadFailed(String errorMessage, int what, int code) {
                        Toast.makeText(UploadActivity.this, "Failed to create an appointment", Toast.LENGTH_LONG).show();
                        finish();
                    }
                }).execute();
    }

    private void uploadImages() {
        progressBar.setVisibility(View.VISIBLE);
        new DownloadData(this, new DownloadDataBuilder().init(AppConfig.URL_AYUBO_SOAP_REQUEST, 0, DownloadManager.POST_REQUEST).
                setParams(AppHandler.getSoapRequestParams(AppConfig.METHOD_SOAP_UPLOAD_IMAGE, new UploadParams().getSearchParams())).
                setType(AppConfig.SERVER_REQUEST_CONTENT_TYPE).setTimeout(AppConfig.SERVER_REQUEST_TIMEOUT)).
                setOnDownloadListener(DashboardActivity.class.getSimpleName(), new DownloadData.DownloadListener() {
                    @Override
                    public void onDownloadSuccess(String response, int what, int code) {
                        progressBar.setVisibility(View.GONE);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if(jsonObject.getInt("result") == 0) {
                                //TODO goto next
                                return;
                            } else {
                                Toast.makeText(UploadActivity.this, jsonObject.getString("error"), Toast.LENGTH_LONG).show();
                                return;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Toast.makeText(UploadActivity.this, getString(R.string.server_error), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onDownloadFailed(String errorMessage, int what, int code) {
                        progressBar.setVisibility(View.GONE);
                        btnNext.setEnabled(true);
                        Toast.makeText(UploadActivity.this, getString(R.string.server_error), Toast.LENGTH_LONG).show();
                    }
                }).execute();
    }

    class AppointmentParams extends SoapBasicParams {

        private String locationID;
        private String doctorID;
        private String start;
        private String patientID;

        AppointmentParams() {
            locationID = expert.getLocations().get(0).getId();
            doctorID = expert.getId();
            start = expert.getLocations().get(0).getNext_available();
            patientID = "";
        }

        public String getSearchParams(String query) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("user_id", user_id);
                jsonObject.put("locationID", locationID);
                jsonObject.put("doctorID", doctorID);
                jsonObject.put("start", start);
                jsonObject.put("patientID", patientID);
                jsonObject.put("token_key", token_key);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return jsonObject.toString();
        }
    }

    class UploadParams extends SoapBasicParams {
        private String note;
        private File[] picture;

        UploadParams() {
            note = edtNote.getText().toString();

            List<File> files = new ArrayList<>();
            for (Uri uri: images) {
                files.add(new File(uri.toString()));
            }

            picture = files.toArray(new File[files.size()]);
        }

        public String getSearchParams(String query) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("user_id", user_id);
                jsonObject.put("note", note);
                jsonObject.put("picture", picture);
                jsonObject.put("token_key", token_key);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return jsonObject.toString();
        }
    }
}
