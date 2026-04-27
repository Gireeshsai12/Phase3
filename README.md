# Phase 3: Android Student Information System (SIS)

## Overview
This project is the Phase 3 implementation of a Student Information System (SIS), where an Android application connects to a MySQL database (developed in Phase 2) through PHP APIs. The app allows students, instructors, and admins to interact with the database in real time.

The system supports login, course registration, transcript viewing, discussion boards, and course evaluations.

## Team Members
- Sreeja Kaleru
- Pragna sri Bandi
- Gireeshsai Kalluri

## Environment Information

### System
- Operating System: Windows 10
- Development Machine: Laptop/Desktop

### Android
- Android Studio Version: Latest Stable Version
- Emulator: Pixel (API Level 30 or above)
- Minimum SDK: API 21+

### Backend
- XAMPP Version: Latest Version
- PHP Version: Included with XAMPP
- MySQL: phpMyAdmin (localhost)

## Project Structure

phase3/
├── android-app/
│   ├── java/ (Activities and API handling)
│   ├── res/layout/ (XML UI files)
├── backend/
│   ├── login.php
│   ├── register.php
│   ├── transcript.php
│   ├── discussion.php
│   ├── evaluation.php
└── README.md

## Features Implemented

### 1. Accounts and Login
- Pre-created accounts for students, instructors, and admin
- Login using ID and password
- Password reset functionality
- Fixed SID/IID and email

### 2. Browse and Register
- Students can view current semester courses
- Register for available sections
- Section capacity limited to 15 students

### 3. Student Transcript
- View completed courses
- View currently enrolled courses
- Calculate total credits
- Display cumulative GPA

### 4. Discussion Board
- Students can post and reply
- TA/Grader can delete posts
- Threaded discussion supported

### 5. Course Evaluation
- Students must submit evaluation after semester ends
- Grade access is restricted until evaluation is submitted

## How Android Connects to Backend

The Android application communicates with the MySQL database through PHP APIs hosted on XAMPP.

Flow:
1. Android sends HTTP request (using Retrofit or HttpURLConnection)
2. PHP API processes request
3. PHP interacts with MySQL database
4. JSON response is sent back to Android
5. Android displays the data

## Setup Instructions

### Step 1: Setup Backend (XAMPP)
1. Install XAMPP
2. Start Apache and MySQL
3. Open phpMyAdmin
4. Import your Phase 2 database
5. Place PHP files in:
C:\xampp\htdocs\phase2\

### Step 2: Update API URLs
Replace localhost with your system IP in Android code:
http://YOUR_IP_ADDRESS/phase2/login.php

### Step 3: Run Android App
1. Open project in Android Studio
2. Connect emulator or physical device
3. Click Run
4. Login using test credentials

## API Endpoints

- login.php → User authentication
- register.php → Course registration
- transcript.php → Fetch student transcript
- discussion.php → Handle posts and replies
- evaluation.php → Submit evaluation and check grade access

## Changes from Phase 2
- Added PHP APIs for Android connectivity
- Modified database queries for JSON responses
- Enabled remote access via HTTP requests



## Notes
- All functionalities are implemented with database connectivity
- No hardcoded data is used
- System follows project requirements strictly

## Conclusion
This project demonstrates integration of an Android frontend with a MySQL backend using PHP APIs, successfully implementing all required features of a Student Information System.
