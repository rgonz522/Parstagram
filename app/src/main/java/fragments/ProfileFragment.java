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
import com.example.parstagram.LogInActivity;
import com.example.parstagram.Post;
import com.example.parstagram.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class ProfileFragment extends PostsFragment
{

    private static final String TAG = "ProfileFragment" ;
    private static final String KEY_USER = "user" ;

    private ParseUser profileUser;
    private ImageView ivProfilePic;
    private TextView tvUserName;
    private Button btnLogOut;
    private boolean currentUser;


    public ProfileFragment(ParseUser profileUser, boolean currentUser)
    {
        this.profileUser = profileUser;
        this.currentUser = currentUser;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        profileUser = ParseUser.getCurrentUser();

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);


    }

    protected void queryPosts()
    {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        query.whereEqualTo(Post.KEY_USER, ParseUser.getCurrentUser());
        query.setLimit(20);
        query.addDescendingOrder(Post.KEY_CREATEDAT);
        query.findInBackground(new FindCallback<Post>()
        {
            @Override
            public void done(List<Post> posts, ParseException e)
            {
                if(e != null)
                {
                    Log.e(TAG, "Issues getting posts", e );
                }
                for (Post post: posts)
                {
                    Log.i(TAG, "post: " + post.getDescription() + " " + post.getUser().getUsername());
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

        ParseFile profilepic = profileUser.getParseFile("profile_pic");

        if (profilepic != null) Glide.with(getContext()).load(profilepic.getUrl()).into(ivProfilePic);

        if(currentUser)
        {
            btnLogOut.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    ParseUser.logOut();
                    getActivity().finish();
                    Intent login_intent = new Intent(getContext(), LogInActivity.class);
                    startActivity(login_intent);
                }
            });
        }

    }



}
