package www.rutvikgandhiassignment2.com;

// Import Android libraries
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

// Import AppCompatActivity from the androidx library
import androidx.appcompat.app.AppCompatActivity;

// Import data binding library
import www.rutvikgandhiassignment2.com.databinding.ActivityNotificationBinding;

// NotificationActivity class extending AppCompatActivity
public class NotificationActivity extends AppCompatActivity {

    // Data binding variable
    ActivityNotificationBinding binding;

    // onCreate method for activity initialization
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate the layout using data binding
        binding = ActivityNotificationBinding.inflate(getLayoutInflater());
        View viewside = binding.getRoot();
        setContentView(viewside);

        // Call initialization method
        init();
    }

    // Initialization method
    private void init() {
        // Get the intent that started this activity
        Intent notificationDataIntent = getIntent();

        // Retrieve taskID and taskName from the intent
        String taskID = notificationDataIntent.getStringExtra("TaskID");
        String taskName = notificationDataIntent.getStringExtra("TaskName");

        // Set the retrieved values to corresponding TextViews in the layout
        binding.taskIdText.setText(taskID);
        binding.taskNameTextView.setText(taskName);
    }
}
