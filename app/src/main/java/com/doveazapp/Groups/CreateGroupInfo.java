package com.doveazapp.Groups;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.doveazapp.Activities.BaseActivity;
import com.doveazapp.Activities.ServiceCalls;
import com.doveazapp.Constants;
import com.doveazapp.Interface.OnRequestCompletedListener;
import com.doveazapp.R;
import com.doveazapp.Utils.MenuVisibility;
import com.doveazapp.Utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

/**
 * Created by Karthik on 2016/02/16.
 */
public class CreateGroupInfo extends AppCompatActivity implements View.OnClickListener, TextWatcher {

    private final static String TAG = CreateGroupInfo.class.getName();

    EditText edit_groupname, edit_groupslogan;

    TextInputLayout input_groupname, input_groupslogan;

    ImageButton upload_img;

    ImageView img_group_view;

    final int CAMERA_CAPTURE = 1;

    Bitmap item_img;

    Button btn_next;

    String group_name, group_slogan;
    // captured picture uri
    Uri picUri;

    SessionManager session;

    //progress dialog
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_group_info);

        menuvisibilityinAlldevices();

        // Session Manager
        session = new SessionManager(getApplicationContext());
        // get user data from session
        HashMap<String, String> user = session.getUserDetails();
        // token
        String name = user.get(SessionManager.KEY_APITOKEN);
        // email
        String email = user.get(SessionManager.KEY_EMAIL);

        // edittexts...
        edit_groupname = (EditText) findViewById(R.id.edit_groupname);
        edit_groupslogan = (EditText) findViewById(R.id.edit_groupslogan);

        // inputs...
        input_groupname = (TextInputLayout) findViewById(R.id.input_groupname);
        input_groupslogan = (TextInputLayout) findViewById(R.id.input_groupslogan);

        // img view...
        img_group_view = (ImageView) findViewById(R.id.upload_img);
        upload_img = (ImageButton) findViewById(R.id.upload_img);

        //buttons
        btn_next = (Button) findViewById(R.id.btn_next);

        // button listeners
        btn_next.setOnClickListener(this);
        upload_img.setOnClickListener(this);
        edit_groupname.addTextChangedListener(this);
        edit_groupslogan.addTextChangedListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == btn_next) {
            validateTextboxes();
        }
        if (view == upload_img) {
            cameraActionToCapture();
        }
    }

    private void validateTextboxes() {
        group_name = edit_groupname.getText().toString();
        group_slogan = edit_groupslogan.getText().toString();

        if (group_name.equals("")) {
            input_groupname.setError("Please enter the group name");
        } else if (group_slogan.equals("")) {
            input_groupslogan.setError("Please enter the slogan");
        } else {
            callAPI_tocreategroup();
        }
    }

    private void cameraActionToCapture() {
        try {
            // use standard intent to capture an image
            Intent captureIntent = new Intent(
                    MediaStore.ACTION_IMAGE_CAPTURE);
            // we will handle the returned data in onActivityResult
            startActivityForResult(captureIntent, CAMERA_CAPTURE);
        } catch (ActivityNotFoundException anfe) {
            // display an error message
            String errorMessage = "Whoops - your device doesn't support capturing images!";
            Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    /**
     * Handle user returning from both capturing and cropping the image
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == CAMERA_CAPTURE) {
                picUri = data.getData();
                Bundle extras = data.getExtras();
                item_img = extras.getParcelable("data");
                img_group_view.setImageBitmap(item_img);
            }
        }
    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void callAPI_tocreategroup() {
        String img_item_pick = null;
        if (item_img != null) {
            img_item_pick = getStringImage(item_img);
        } else {
            Toast.makeText(getApplicationContext(), Constants.SELECT_IMAGE_TOAST_MESSAGE, Toast.LENGTH_LONG).show();
            return;
        }

        group_name = edit_groupname.getText().toString();
        group_slogan = edit_groupslogan.getText().toString();

        progressDialog = ProgressDialog.show(CreateGroupInfo.this, "Please wait ...", "Creating group...", true);
        progressDialog.setCancelable(false);
        OnRequestCompletedListener listener = new OnRequestCompletedListener() {
            @Override
            public void onRequestCompleted(String response) {
                Log.v("--output create group--", response.toString());
                progressDialog.dismiss();
                //Toast.makeText(EditDescription.this, response, Toast.LENGTH_LONG).show();
                System.out.println(response.toString());
                try {
                    JSONObject obj = new JSONObject(response);
                    final String status = obj.getString("status");
                    final String value = obj.getString("value");
                    JSONObject value_obj = obj.getJSONObject("value");
                    String message = value_obj.getString("message");
                    String group_id = value_obj.getString("group_id");

                    if (status.equals("false")) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), value, Toast.LENGTH_SHORT).show();
                    } else if (status.equals("true")) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                        if (message.equals(Constants.KEY_GROUP_CREATED)) {
                            Intent to_addMembers = new Intent(getApplicationContext(), AddGroupMembersActivity.class);
                            to_addMembers.putExtra(Constants.KEY_GROUP_ID, group_id);
                            startActivity(to_addMembers);
                        }
                    }
                } catch (JSONException exception) {
                    Log.e("--JSON EXCEPTION--", exception.toString());
                    progressDialog.dismiss();
                }
            }
        };
        HashMap<String, String> user = session.getUserDetails();
        // token
        String api_token = user.get(SessionManager.KEY_APITOKEN);
        Log.v("Calling Api:", Constants.CREATE_GROUP_API);
        ServiceCalls.CallAPI_tocreateGroup(this, Request.Method.POST, Constants.CREATE_GROUP_API, listener, group_name, group_slogan, img_item_pick, api_token);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (editable == edit_groupname.getEditableText()) {
            input_groupname.setError(null);
        } else if (editable == edit_groupslogan.getEditableText()) {
            input_groupslogan.setError(null);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // get user data from session
        HashMap<String, String> user = session.getUserDetails();
        String user_type = user.get(SessionManager.KEY_USER_TYPE);
        menu.clear();
        if (user_type.equals(Constants.KEY_TYPE_PARTNER)) {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.items, menu);
        } else {
            getMenuInflater().inflate(R.menu.menu_deliver, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return BaseActivity.CommonClass.HandleMenu(this, item.getItemId());
    }

    private void menuvisibilityinAlldevices() {
        MenuVisibility.menuVisible(CreateGroupInfo.this);
    }
}
