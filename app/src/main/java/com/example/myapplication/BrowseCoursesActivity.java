package com.example.myapplication;

import android.os.Bundle;
import android.widget.ArrayAdapter;
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

public class BrowseCoursesActivity extends AppCompatActivity {

    ListView listView;
    ArrayList<String> courseList;
    JSONArray sectionsArray;

    String studentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_courses);

        listView = findViewById(R.id.listViewCourses);
        courseList = new ArrayList<>();

        // Get student ID from previous screen
        studentId = getIntent().getStringExtra("user_id");

        loadCourses();

        // Click to register
        listView.setOnItemClickListener((parent, view, position, id) -> {
            try {
                JSONObject sec = sectionsArray.getJSONObject(position);

                String course_id = sec.getString("course_id");
                String section_id = sec.getString("section_id");
                String semester = sec.getString("semester");
                String year = sec.getString("year");

                registerCourse(studentId, course_id, section_id, semester, year);

            } catch (Exception e) {
                Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void loadCourses() {

        String url = "http://10.0.2.2/phase2/api/api_get_sections.php";

        StringRequest request = new StringRequest(Request.Method.GET, url,
                response -> {
                    try {
                        JSONObject obj = new JSONObject(response);
                        sectionsArray = obj.getJSONArray("sections");

                        courseList.clear();

                        for (int i = 0; i < sectionsArray.length(); i++) {
                            JSONObject sec = sectionsArray.getJSONObject(i);

                            String course = sec.getString("course_id")
                                    + " - " + sec.getString("title")
                                    + " | Seats: " + sec.getString("enrolled")
                                    + "/" + sec.getString("capacity");

                            courseList.add(course);
                        }

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
                error -> Toast.makeText(this, "Error loading courses", Toast.LENGTH_LONG).show()
        );

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }

    private void registerCourse(String student_id, String course_id, String section_id, String semester, String year) {

        String url = "http://10.0.2.2/phase2/api/api_register_section.php";

        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> {
                    try {
                        JSONObject obj = new JSONObject(response);
                        boolean success = obj.getBoolean("success");

                        if (success) {
                            Toast.makeText(this, "Registered Successfully", Toast.LENGTH_LONG).show();
                            loadCourses(); // refresh seats
                        } else {
                            Toast.makeText(this, obj.getString("message"), Toast.LENGTH_LONG).show();
                        }

                    } catch (Exception e) {
                        Toast.makeText(this, "Error parsing response", Toast.LENGTH_LONG).show();
                    }
                },
                error -> Toast.makeText(this, "Registration failed", Toast.LENGTH_LONG).show()
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("student_id", student_id);
                params.put("course_id", course_id);
                params.put("section_id", section_id);
                params.put("semester", semester);
                params.put("year", year);
                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }
}