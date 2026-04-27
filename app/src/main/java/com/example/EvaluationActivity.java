package com.example.myapplication;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class EvaluationActivity extends AppCompatActivity {

    EditText etCourse, etRating, etFeedback;
    Button btnSubmitEval, btnViewGrade;
    TextView tvGradeResult;

    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluation);

        etCourse = findViewById(R.id.etCourse);
        etRating = findViewById(R.id.etRating);
        etFeedback = findViewById(R.id.etFeedback);
        btnSubmitEval = findViewById(R.id.btnSubmitEval);
        btnViewGrade = findViewById(R.id.btnViewGrade);
        tvGradeResult = findViewById(R.id.tvGradeResult);

        userId = getIntent().getStringExtra("user_id");

        btnSubmitEval.setOnClickListener(v -> submitEvaluation());
        btnViewGrade.setOnClickListener(v -> viewGrade());
    }

    private void submitEvaluation() {
        String course = etCourse.getText().toString().trim();
        String rating = etRating.getText().toString().trim();
        String feedback = etFeedback.getText().toString().trim();

        if (course.isEmpty() || rating.isEmpty()) {
            Toast.makeText(this, "Enter course ID and rating", Toast.LENGTH_LONG).show();
            return;
        }

        String url = "http://10.0.2.2/phase2/api/api_submit_evaluation.php";

        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> {
                    try {
                        JSONObject obj = new JSONObject(response);
                        Toast.makeText(this, obj.getString("message"), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(this, "Submit response error", Toast.LENGTH_LONG).show();
                    }
                },
                error -> Toast.makeText(this, "Network error submitting evaluation", Toast.LENGTH_LONG).show()
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("student_id", userId);
                params.put("course_id", course);
                params.put("rating", rating);
                params.put("feedback", feedback);
                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }

    private void viewGrade() {
        String course = etCourse.getText().toString().trim();

        if (course.isEmpty()) {
            Toast.makeText(this, "Enter course ID first", Toast.LENGTH_LONG).show();
            return;
        }

        String url = "http://10.0.2.2/phase2/api/api_view_grade_after_eval.php?student_id="
                + userId + "&course_id=" + course;

        StringRequest request = new StringRequest(Request.Method.GET, url,
                response -> {
                    try {
                        JSONObject obj = new JSONObject(response);

                        if (obj.getBoolean("success")) {
                            String result = obj.getString("course_id")
                                    + " - " + obj.getString("title")
                                    + "\nGrade: " + obj.getString("grade");
                            tvGradeResult.setText(result);
                        } else {
                            tvGradeResult.setText(obj.getString("message"));
                        }

                    } catch (Exception e) {
                        Toast.makeText(this, "Grade response error", Toast.LENGTH_LONG).show();
                    }
                },
                error -> Toast.makeText(this, "Network error loading grade", Toast.LENGTH_LONG).show()
        );

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }
}