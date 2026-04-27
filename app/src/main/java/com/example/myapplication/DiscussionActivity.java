package com.example.myapplication;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DiscussionActivity extends AppCompatActivity {

    EditText etMessage;
    Button btnPost;
    ListView listViewPosts;

    ArrayList<String> postList;
    JSONArray postsArray;

    String userId;
    String role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discussion);

        etMessage = findViewById(R.id.etMessage);
        btnPost = findViewById(R.id.btnPost);
        listViewPosts = findViewById(R.id.listViewPosts);

        userId = getIntent().getStringExtra("user_id");
        role = getIntent().getStringExtra("role");

        postList = new ArrayList<>();

        loadPosts();

        btnPost.setOnClickListener(v -> addPost());

        listViewPosts.setOnItemClickListener((parent, view, position, id) -> {
            if (role != null && (role.equalsIgnoreCase("admin") || role.equalsIgnoreCase("ta") || role.equalsIgnoreCase("grader"))) {
                try {
                    JSONObject post = postsArray.getJSONObject(position);
                    deletePost(post.getString("post_id"));
                } catch (Exception e) {
                    Toast.makeText(this, "Delete error", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(this, "Only TA/Grader/Admin can delete posts", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void loadPosts() {
        String url = "http://10.0.2.2/phase2/api/api_get_posts.php";

        StringRequest request = new StringRequest(Request.Method.GET, url,
                response -> {
                    try {
                        JSONObject obj = new JSONObject(response);
                        postsArray = obj.getJSONArray("posts");

                        postList.clear();

                        for (int i = 0; i < postsArray.length(); i++) {
                            JSONObject p = postsArray.getJSONObject(i);

                            String post = p.getString("student_id")
                                    + " (" + p.getString("course_id") + "): "
                                    + p.getString("content");

                            postList.add(post);
                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                                this,
                                android.R.layout.simple_list_item_1,
                                postList
                        );

                        listViewPosts.setAdapter(adapter);

                    } catch (Exception e) {
                        Toast.makeText(this, "JSON Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                },
                error -> Toast.makeText(this, "Error loading posts", Toast.LENGTH_LONG).show()
        );

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }

    private void addPost() {
        String message = etMessage.getText().toString().trim();

        if (message.isEmpty()) {
            Toast.makeText(this, "Enter a message", Toast.LENGTH_LONG).show();
            return;
        }

        String url = "http://10.0.2.2/phase2/api/api_add_post.php";

        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> {
                    try {
                        JSONObject obj = new JSONObject(response);

                        if (obj.getBoolean("success")) {
                            Toast.makeText(this, "Post added", Toast.LENGTH_LONG).show();
                            etMessage.setText("");
                            loadPosts();
                        } else {
                            Toast.makeText(this, obj.getString("message"), Toast.LENGTH_LONG).show();
                        }

                    } catch (Exception e) {
                        Toast.makeText(this, "Post error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                },
                error -> Toast.makeText(this, "Error adding post", Toast.LENGTH_LONG).show()
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("student_id", userId);
                params.put("message", message);
                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }

    private void deletePost(String postId) {
        String url = "http://10.0.2.2/phase2/api/api_delete_post.php";

        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> {
                    try {
                        JSONObject obj = new JSONObject(response);

                        if (obj.getBoolean("success")) {
                            Toast.makeText(this, "Post deleted", Toast.LENGTH_LONG).show();
                            loadPosts();
                        } else {
                            Toast.makeText(this, obj.getString("message"), Toast.LENGTH_LONG).show();
                        }

                    } catch (Exception e) {
                        Toast.makeText(this, "Delete error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                },
                error -> Toast.makeText(this, "Error deleting post", Toast.LENGTH_LONG).show()
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id", postId);
                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }
}