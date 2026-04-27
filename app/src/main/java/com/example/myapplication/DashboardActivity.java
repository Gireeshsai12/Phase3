package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class DashboardActivity extends AppCompatActivity {

    TextView tvWelcome;
    Button btnBrowse, btnTranscript, btnDiscussion, btnEval;

    String userId;
    String role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        tvWelcome = findViewById(R.id.tvWelcome);
        btnBrowse = findViewById(R.id.btnBrowse);
        btnTranscript = findViewById(R.id.btnTranscript);
        btnDiscussion = findViewById(R.id.btnDiscussion);
        btnEval = findViewById(R.id.btnEval);

        userId = getIntent().getStringExtra("user_id");
        role = getIntent().getStringExtra("role");

        tvWelcome.setText("Welcome " + userId + " (" + role + ")");

        // Browse Courses
        btnBrowse.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, BrowseCoursesActivity.class);
            intent.putExtra("user_id", userId);
            intent.putExtra("role", role);
            startActivity(intent);
        });

        // Transcript
        btnTranscript.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, TranscriptActivity.class);
            intent.putExtra("user_id", userId);
            intent.putExtra("role", role);
            startActivity(intent);
        });

        // Discussion Board
        btnDiscussion.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, DiscussionActivity.class);
            intent.putExtra("user_id", userId);
            intent.putExtra("role", role);
            startActivity(intent);
        });

        // Course Evaluation (THIS IS STEP 5)
        btnEval.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, EvaluationActivity.class);
            intent.putExtra("user_id", userId);
            startActivity(intent);
        });
    }
}