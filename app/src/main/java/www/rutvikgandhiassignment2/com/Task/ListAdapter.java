package www.rutvikgandhiassignment2.com.Task;

// Import Android libraries
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

// Import RecyclerView and its supporting classes from the androidx library
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

// Import application-specific R class for resource references
import www.rutvikgandhiassignment2.com.R;

// Import Java utility classes
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

// ListAdapter class extending RecyclerView.Adapter
public class ListAdapter extends RecyclerView.Adapter<ListAdapter.TaskViewHolder> {

    // List to hold Task objects and Context variable
    private List<Task> taskList;
    private Context context;

    // Constructor to initialize the adapter with a Context and a List of tasks
    public ListAdapter(Context contextData, List<Task> taskListinfo) {
        this.context = contextData;
        this.taskList = taskListinfo;
    }

    // onCreateViewHolder method to inflate the layout for each item in the RecyclerView
    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewmode = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_task, parent, false);
        return new TaskViewHolder(viewmode);
    }

    // onBindViewHolder method to bind data to the views within each ViewHolder
    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holderdata, int positions) {
        // Get the Task object at the specified position
        Task task = taskList.get(positions);

        // Set the data to the corresponding TextViews in the ViewHolder
        holderdata.taskIdTextView.setText("Task ID: " + task.getTaskId());
        holderdata.taskNameTextView.setText("Task Name: " + task.getTaskName());

        // Format the due date and time using SimpleDateFormat
        SimpleDateFormat properdateFormat = new SimpleDateFormat("EEE, MMM d, yyyy h:mm a", Locale.getDefault());
        String formatDate = properdateFormat.format(task.getTaskDateTime());
        holderdata.dueDateTimeTextView.setText("Due Date & Time: " + formatDate);
    }

    // getItemCount method to determine the number of items in the RecyclerView
    @Override
    public int getItemCount() {
        return taskList.size();
    }

    // TaskViewHolder class representing each item's views in the RecyclerView
    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        // TextViews to display task information
        TextView taskIdTextView;
        TextView taskNameTextView;
        TextView dueDateTimeTextView;

        // Constructor to initialize the ViewHolder with its views
        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            taskIdTextView = itemView.findViewById(R.id.taskIdTextView);
            taskNameTextView = itemView.findViewById(R.id.taskNameTextView);
            dueDateTimeTextView = itemView.findViewById(R.id.TBDTextView);
        }
    }
}
