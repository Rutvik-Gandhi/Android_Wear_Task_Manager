package www.rutvikgandhiassignment2.com.Task;

// Import necessary Java utility classes
import java.util.UUID;

// Task class representing a task in the application
public class Task {

    // Private member variables
    private String taskId;          // Unique identifier for the task
    private String taskName;        // Name of the task
    private long taskDateTime;      // Date and time of the task

    // Constructor for creating a task with specified task ID, name, and date/time
    public Task(String taskDataId, String taskDataName, long taskDataDateTime) {
        this.taskId = taskDataId;
        this.taskName = taskDataName;
        this.taskDateTime = taskDataDateTime;
    }

    // Constructor for creating a task with a generated task ID, specified name, and date/time
    public Task(String taskDataName, long taskDataDateTime) {
        this.taskId = generateUniqueId();
        this.taskName = taskDataName;
        this.taskDateTime = taskDataDateTime;
    }

    // Getter method for retrieving the task ID
    public String getTaskId() {
        return taskId;
    }

    // Getter method for retrieving the task name
    public String getTaskName() {
        return taskName;
    }

    // Getter method for retrieving the task date and time
    public long getTaskDateTime() {
        return taskDateTime;
    }

    // Private method to generate a unique task ID
    private String generateUniqueId() {
        // Generate a UUID and extract the first 4 characters
        String uuid = UUID.randomUUID().toString();
        return uuid.substring(0, 4);
    }
}
