package fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.example.parstagram.MainActivity;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;


public class ChangeUserPicFragment extends ComposeFragment {

    private static final String TAG = "ChangeUserPicFragment";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etDescription.setText(" ");
        etDescription.setVisibility(View.INVISIBLE);
        //Compose Fragment's Description not needed.
    }


    @Override
    protected void savePost(String description, ParseUser currentUser, File photoFile) {
        currentUser.put("profile_pic", new ParseFile(photoFile));

        currentUser.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issues  changing profile pic ", e);
                    return;
                }
                Glide.with(getContext()).clear(ivPostImage);
                pbLoading.setVisibility(ProgressBar.INVISIBLE);
                //after posting go to MainActivity
                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }
}