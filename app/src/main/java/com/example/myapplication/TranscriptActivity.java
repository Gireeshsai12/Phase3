package com.example.myapplication;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class TranscriptActivity extends AppCompatActivity {

    ListView listView;
    TextView tvGPA;
    ArrayList<String> courseList;

    String studentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transcript);

        listView = findViewById(R.id.listViewTranscript);
        tvGPA = findViewById(R.id.tvGPA);

        studentId = getIntent().getStringExtra("user_id");

        courseList = new ArrayList<>();

        loadTranscript();
    }

    private void loadTranscript() {

        String url = "http://10.0.2.2/phase2/api/api_transcript.php?student_id=" + studentId;

        StringRequest request = new StringRequest(Request.Method.GET, url,
                response -> {
                    try {
                        JSONObject obj = new JSONObject(response);

                        JSONArray courses = obj.getJSONArray("courses");

                        courseList.clear();

                        for (int i = 0; i < courses.length(); i++) {
                            JSONObject c = courses.getJSONObject(i);

                            String course = c.getString("course_id")
                                    + " - " + c.getString("title")
                                    + " | Grade: " + c.getString("grade");

                            courseList.add(course);
                        }

                        double gpa = obj.getDouble("gpa");

                        tvGPA.setText("GPA: " + gpa);

                        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                                this,
                                android.R.layout.simple_list_item_1,
                                courseList
                        );

                        listView.setAdapter(adapter);

                    } catch (Exception e) {
                        Toast.makeText(this, "JSON Error", Toast.LENGTH_LONG).show();
                    }
                },
                error -> Toast.makeText(this, "Error loading transcript", Toast.LENGTH_LONG).show()
        );

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }
}