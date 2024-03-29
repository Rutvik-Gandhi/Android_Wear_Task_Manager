package www.rutvikgandhiassignment2.com;

// Import Android libraries

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

// Import androidx libraries

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

// Import data binding library

import www.rutvikgandhiassignment2.com.databinding.ActivityMainBinding;

// Import necessary classes from the application

import www.rutvikgandhiassignment2.com.Task.Task;
import www.rutvikgandhiassignment2.com.Task.ListActivity;

// Import Java libraries

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

// Main activity class
public class MainActivity extends Activity {

    // Constants
    private static final int SPEECH_START_CODE = 1234;
    private static final String CHANNEL_ID_NUMBER = "my_channel";

    // UI elements and variables
    private TextView taskNameTextViewSide;
    private TextView TBDTextView;
    private Calendar optdatetime;
    private SharedPreferences sharedData;

    // Activity lifecycle method
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set up data binding

        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        // Initialize UI components and SharedPreferences

        taskNameTextViewSide = binding.textViewTaskID;
        Button btnVoiceInput = binding.btnVoiceInput;
        Button btnPickDateTime = binding.btnchooseDateTime;
        TBDTextView = binding.textViewTBD;
        Button btnAddTask = binding.btnAddMoreTask;

        sharedData = getSharedPreferences("TaskPreferences", MODE_PRIVATE);
        optdatetime = Calendar.getInstance();

        // Call this method to create the notification channel

        createNotificationChannel();

        // Set up event listeners

        btnVoiceInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startVoiceInput();
            }
        });

        btnPickDateTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickDateTime();
            }
        });

        btnAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTask();
            }
        });
    }

    // Method to initiate voice input
    public void startVoiceInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Voice the task name");

        startActivityForResult(intent, SPEECH_START_CODE);
    }

    // Handle the result of the voice input

    @Override
    protected void onActivityResult(int requestsendingsideCode, int resultoutputDataCode, Intent data) {
        super.onActivityResult(requestsendingsideCode, resultoutputDataCode, data);

        if (requestsendingsideCode == SPEECH_START_CODE && resultoutputDataCode == RESULT_OK) {
            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (matches != null && matches.size() > 0) {
                String spokenText = matches.get(0);

                // Save the voice input to SharedPreferences

                saveVoiceInput(spokenText);

                // Optionally, you can provide a confirmation to the user

                Toast.makeText(this, "Voice input saved: " + spokenText, Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Save the voice input to SharedPreferences
    private void saveVoiceInput(String input) {
        SharedPreferences.Editor editor = sharedData.edit();
        editor.putString("VoiceInput", input);
        editor.apply();
    }

    // Launch the date picker
    public void pickDateTime() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                optdatetime.set(Calendar.YEAR, year);
                optdatetime.set(Calendar.MONTH, month);
                optdatetime.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                // Launch time picker after date is selected

                launchTimePicker();
            }
        }, optdatetime.get(Calendar.YEAR), optdatetime.get(Calendar.MONTH), optdatetime.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
    }

    // Launch the time picker
    private void launchTimePicker() {
        TimePickerDialog timepickingDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                optdatetime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                optdatetime.set(Calendar.MINUTE, minute);

                // Update UI with selected date and time

                updateDueDateText();

                // Save the selected date and time to SharedPreferences

                saveDateTime(optdatetime.getTimeInMillis());
            }
        }, optdatetime.get(Calendar.HOUR_OF_DAY), optdatetime.get(Calendar.MINUTE), true);

        timepickingDialog.show();
    }

    // Update the UI with the selected date and time
    private void updateDueDateText() {
        SimpleDateFormat datesetup = new SimpleDateFormat("EEE, MMM d, yyyy h:mm a", Locale.getDefault());
        String formatDate = datesetup.format(optdatetime.getTime());
        TBDTextView.setText("Due Date & Time: " + formatDate);
    }

    // Save the selected date and time to SharedPreferences
    private void saveDateTime(long dateTimeInMillis) {
        SharedPreferences.Editor creator = sharedData.edit();
        creator.putLong("selectedDateTime", dateTimeInMillis);
        creator.apply();
    }

    // Add a task to the list and trigger notifications
    public void addTask() {
        String taskacceptName = sharedData.getString("voiceInput", "");
        long dateTimeInMillis = sharedData.getLong("selectedDateTime", 0);

        if (!taskacceptName.trim().isEmpty() && dateTimeInMillis != 0) {
            Task task = new Task(taskacceptName, dateTimeInMillis);

            // Persistently store the task

            saveTask(task);

            // Navigate to TaskListActivity

            Intent intent = new Intent(this, ListActivity.class);
            startActivity(intent);

            // Save taskId to SharedPreferences

            SharedPreferences.Editor creator = sharedData.edit();
            creator.putString("TaskID", task.getTaskId());
            creator.apply();

            // Trigger notifications

            sendNotification();

            // Optionally, you can show a confirmation message

            Toast.makeText(this, getString(R.string.task_added), Toast.LENGTH_SHORT).show();
            finish();
        } else {
            // Show a Toast message indicating that data is empty

            Toast.makeText(this, getString(R.string.task_is_empty), Toast.LENGTH_SHORT).show();
        }
    }

    // Save the task to SharedPreferences
    private void saveTask(Task task) {
        Set<String> existTasks = sharedData.getStringSet("tasks", new HashSet<String>());
        existTasks.add(task.getTaskId() + "|" + task.getTaskName() + "|" + task.getTaskDateTime());

        SharedPreferences.Editor creator = sharedData.edit();
        creator.putStringSet("tasks", existTasks);
        creator.apply();
    }

    // Create the notification channel
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String MessageName = "taskMessage";
            String channelInformation = "Channel for taskMessage";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID_NUMBER, MessageName, importance);
            channel.setDescription(channelInformation);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    // Send a notification
    private void sendNotification() {
        long randomNumber = System.currentTimeMillis();
        int askingCode = (int) randomNumber;

        String taskName = sharedData.getString(getString(R.string.voiceinput_shared), "");
        long dateTimeInMillis = sharedData.getLong(getString(R.string.selecteddatetime_shared), 0);
        String taskId = sharedData.getString(getString(R.string.taskid_shared), "");
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID_NUMBER);

        if (!taskName.trim().isEmpty() && dateTimeInMillis != 0) {
            // Calculate time difference

            long presentTimeMillis = System.currentTimeMillis();
            long timeofDifference = dateTimeInMillis - presentTimeMillis;

            // Schedule notification if the task is due within the next one hour

            if (timeofDifference > 0 && timeofDifference <= 60 * 60 * 1000) {
                // Create an intent for the NotificationReceiver
                Intent intent = new Intent(this, ListActivity.class);
                intent.putExtra(getString(R.string.tasknameIntent), taskName);
                intent.putExtra(getString(R.string.taskidIntent), taskId);

                // Create a PendingIntent for the notification

                PendingIntent remainIntent = PendingIntent.getActivity(
                        this,
                        askingCode,
                        intent,
                        PendingIntent.FLAG_IMMUTABLE
                );

                // Create a NotificationCompat.Builder

                builder.setSmallIcon(R.drawable.ic_notification)
                        .setContentTitle("Task Reminder: " + taskName)
                        .setContentText("TaskID: " + taskId)
                        .setContentIntent(remainIntent)
                        .setAutoCancel(true);

                // Get the NotificationManagerCompat instance

                NotificationManagerCompat manageCompat = NotificationManagerCompat.from(this);
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 5);
                    return;
                }
                manageCompat.notify(2, builder.build());
            }
        }
    }
}
