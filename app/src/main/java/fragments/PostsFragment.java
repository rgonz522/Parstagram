package fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.parstagram.Post;
import com.example.parstagram.PostsAdapter;
import com.example.parstagram.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PostsFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class PostsFragment extends Fragment {

    private static final String TAG = "PostsFragment" ;
    private RecyclerView rvposts;
    private PostsAdapter adapter;
    private List<Post> posts;

    public static PostsFragment newInstance(String param1, String param2) {
        PostsFragment fragment = new PostsFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    public PostsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_posts, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        posts = new ArrayList<>();

        rvposts = view.findViewById(R.id.rvPosts);
        adapter = new PostsAdapter(getContext(), posts );

        rvposts.setAdapter(adapter);
        rvposts.setLayoutManager(new LinearLayoutManager(getContext()));
        queryPosts();

    }

    private void queryPosts()
    {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
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
            }
        });
    }
}