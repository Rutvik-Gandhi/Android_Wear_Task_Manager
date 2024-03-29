package www.rutvikgandhiassignment2.com.Task;

// Import Android libraries
import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;

// Import RecyclerView and LinearLayoutManager from the androidx library
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

// Import application-specific R class for resource references
import www.rutvikgandhiassignment2.com.R;

// Import Java utility classes
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

// ListActivity class extending Activity
public class ListActivity extends Activity {

    // RecyclerView and Adapter variables
    private RecyclerView recyclerView;
    private ListAdapter taskListAdapter;

    // onCreate method for activity initialization
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the content view to the layout defined in activity_list.xml
        setContentView(R.layout.activity_list);

        // Initialize RecyclerView and set its layout manager
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Load tasks from SharedPreferences
        List<Task> taskList = loadTasks();

        // Set up the adapter for the RecyclerView
        taskListAdapter = new ListAdapter(this, taskList);
        recyclerView.setAdapter(taskListAdapter);
    }

    // Method to load tasks from SharedPreferences
    private List<Task> loadTasks() {
        // Initialize a List to hold Task objects
        List<Task> taskDataList = new ArrayList<>();

        // Retrieve existing tasks from SharedPreferences
        Set<String> generatedTasks = getSharedPreferences("TaskPreferences", MODE_PRIVATE)
                .getStringSet("tasks", new HashSet<String>());

        // Iterate through existing tasks and create Task objects
        for (String taskString : generatedTasks) {
            String[] taskPartion = taskString.split("\\|");
            if (taskPartion.length == 3) {
                // Extract task details from the split string
                String taskDataId = taskPartion[0];
                String taskDataName = taskPartion[1];
                long taskDataDateTime = Long.parseLong(taskPartion[2]);

                // Create a Task object
                Task task = new Task(taskDataId, taskDataName, taskDataDateTime);

                // Add the task to the list
                taskDataList.add(task);
            }
        }

        // Return the list of tasks
        return taskDataList;
    }
}
