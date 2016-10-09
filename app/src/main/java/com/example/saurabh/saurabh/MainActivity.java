package com.example.saurabh.saurabh;

import android.Manifest;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.util.Util;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;
import pl.tajchert.nammu.Nammu;
import pl.tajchert.nammu.PermissionCallback;

public class MainActivity extends AppCompatActivity {
    public static final int MAX_SERVES = 10;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.food_image)
    ImageView foodImage;
    @BindView(R.id.serves)
    Spinner serves;
    @BindView(R.id.recipe_type)
    EditText recipeType;
    @BindView(R.id.master)
    Button master;
    @BindView(R.id.sous)
    Button sous;
    @BindView(R.id.beginner)
    Button beginner;
    @BindView(R.id.upload_button)
    ImageButton uploadButton;
    @BindView(R.id.duration)
    TextView duration;
    String[] recipeTypes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        recipeTypes = getResources().getStringArray(R.array.recipe_types);

        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.window_bg));
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.recipe_upload);
        }

        Glide.with(this).load(R.drawable.food).into(foodImage);

        setupServesAdapter();
    }

    @OnClick(R.id.recipe_type)
    void openRecipeTypes() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.recipe_type)
                .setSingleChoiceItems(recipeTypes,
                        Utils.searchString(recipeTypes,recipeType.getText().toString()),
                        new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        recipeType.setText(recipeTypes[which]);
                        dialog.dismiss();
                    }
                })
                .create()
                .show();
    }

    @OnClick({R.id.beginner, R.id.sous, R.id.master})
    void selectRecipeLevel(Button button) {
        beginner.setSelected(button == beginner);
        sous.setSelected(button == sous);
        master.setSelected(button == master);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Nammu.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void setupServesAdapter() {
        List<String> serveOptions = new ArrayList<>();
        for (int i = 0; i <= MAX_SERVES; i++) {
            serveOptions.add(String.valueOf(i));
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, serveOptions);
        serves.setAdapter(arrayAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==R.id.action_done){
            Toast.makeText(this,R.string.done, Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick({R.id.food_image,R.id.upload_button})
    void showImagePicker() {
        Nammu.askForPermission(this, Manifest.permission.CAMERA, new PermissionCallback() {
            @Override
            public void permissionGranted() {
                EasyImage.openChooserWithGallery(MainActivity.this, getString(R.string.upload_image), 1);
            }

            @Override
            public void permissionRefused() {
                Toast.makeText(MainActivity.this, R.string.cam_perm_required, Toast.LENGTH_SHORT).show();
                EasyImage.openGallery(MainActivity.this, 1);
            }
        });
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        EasyImage.handleActivityResult(requestCode, resultCode, data, this, new DefaultCallback() {
            @Override
            public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {
                Toast.makeText(MainActivity.this, R.string.error_msg, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onImagePicked(File imageFile, EasyImage.ImageSource source, int type) {
                //Handle the image
                onPhotoReturned(imageFile);
            }
        });
    }

    private void onPhotoReturned(File imageFile) {
        Glide.with(this).load(imageFile).into(foodImage);
        uploadButton.setVisibility(View.GONE);
    }

    @OnClick(R.id.duration)
    void openDurationPicker(){
//        Toast.makeText(this, "Open picker", Toast.LENGTH_SHORT).show();
        TimePickerFragment newFragment = new TimePickerFragment();
        newFragment.setTextView(duration);
        newFragment.setFallBacktext(getString(R.string.duration));
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }
    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {
        private TextView textView;
        //in case 0:0 is selected.
        private String fallBacktext;

        public TimePickerFragment() {
        }

        public void setFallBacktext(String fallBacktext) {
            this.fallBacktext = fallBacktext;
        }

        public void setTextView(TextView textView) {
            this.textView = textView;
        }

        @NonNull
        @Override
        public Dialog onCreateDialog( Bundle savedInstanceState) {
            return new TimePickerDialog(getActivity(), this, 0, 1,true);
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // Set the duration
            if(textView!=null){
                if (hourOfDay==0&&minute==0){
                    textView.setText(fallBacktext);
                    return;
                }
                textView.setText(Utils.getFormattedTime(hourOfDay,minute));
            }

        }
    }
}
