package fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.parstagram.LogInActivity;
import com.example.parstagram.Post;
import com.example.parstagram.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class ProfileFragment extends PostsFragment {

    private static final String TAG = "ProfileFragment";
    public static final String KEY_PROFILE_IMG = "profile_pic";

    private ParseUser profileUser;
    private ImageView ivProfilePic;
    private TextView tvUserName;
    private Button btnLogOut;
    private boolean currentUser;


    public ProfileFragment(ParseUser profileUser, boolean currentUser) {
        this.profileUser = profileUser;
        this.currentUser = currentUser;
    }


    public ProfileFragment(boolean currentUser) {

        profileUser = ParseUser.getCurrentUser();
        this.currentUser = currentUser;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);


    }

    protected void queryPosts(int page) {

        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        query.whereEqualTo(Post.KEY_USER, profileUser);
        query.setLimit(POSTS_PER_PAGE);
        query.setSkip(page * POSTS_PER_PAGE);
        query.addDescendingOrder(Post.KEY_CREATEDAT);
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issues getting posts", e);
                    return;
                }
                allposts.addAll(posts);
                adapter.notifyDataSetChanged();
            }
        });

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        tvUserName = view.findViewById(R.id.tvUsername);
        ivProfilePic = view.findViewById(R.id.ivProfilePic);
        btnLogOut = view.findViewById(R.id.btnLogOut);


        tvUserName.setText(profileUser.getUsername());

        ParseFile profile_pic = profileUser.getParseFile(KEY_PROFILE_IMG);
        String img_url = profile_pic.getUrl();

        if (img_url != null) {

            Glide.with(getContext()).load(img_url).circleCrop().into(ivProfilePic);
        }


        if (currentUser) {
            btnLogOut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ParseUser.logOut();
                    getActivity().finish();
                    Intent login_intent = new Intent(getContext(), LogInActivity.class);
                    startActivity(login_intent);
                }
            });
        }

    }


}
