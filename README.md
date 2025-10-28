# AAD_25_26-Acceso-a-datos-

This repository contains exercises, corresponding to AAD 25 and
AAD 26, focused on accessing data. Here you will find some practical examples, code and
resources relatade to access and data management.

## Repository Structure

- `AAD_25_26-Acceso-a-datos-/`: Contains exercises from subject AAD.

- Each branch could have different folders and files structures.
- Branches have descriptive names that indicate the theme or feature implemented (feature/actX_Y).
- `README.md`: This file provides a general context about this repository and its content.

## feature/act1_5

## How It Works

The application is divided into several key components:

- **`LogEvent.java`**  
  Defines the structure of a log entry, such as message, level, and timestamp.

- **`LogRepository.java`** and **`CrudRepository.java`**  
  Handle the storage and retrieval of logs. The CRUD repository provides the basic create, read, update, and delete
  operations.

- **`LogService.java`**  
  Contains the core logic for managing and processing log entries.  
  It validates inputs and interacts with the repository layer.

- **`CustomService.java`**  
  Provides additional service methods that extend the main logic or customize behavior.

- **`InputValidation.java`**  
  Ensures that all log data is valid before being processed.

- **`Constant.java`**  
  Centralizes constant values used across the project, improving code readability and maintenance.

- **`JrcApplication.java`**  
  The main Spring Boot entry point. Running this file starts the entire application.

---

## Requirements

To run the project, you will need:

- **Java 17** or later
- **Maven 3.8+**
- **Spring Boot 3.x**
- A code editor such as IntelliJ IDEA or VS Code with Java support

## How to Run

1. **Clone or download** the project source code.
2. **Open a terminal** in the project folder.
3. Run the following command to build and start the application:

   ```bash
   mvn spring-boot:run
    ```

## Example Usage

Currently, the application runs through the main method and logs actions directly in the console output.

- Menu options are presented to the user for interaction.
- Users can create, read and filter by date log entries through console prompts.
- Users can also change the log encoding (UTF-8 or ISO-8859-1).
- The application validates inputs and provides feedback on operations.
- Logs are stored in memory during the application's runtime.

1. **Creating a Log Entry**:
    - Select the option (1) to create a log entry.
    - Input the log message and level when prompted.
    - The application confirms the creation of the log entry.

2. **Filtering Log Entries by Date**:
    - Select the option (2) to filter log entries by date.
    - Input the desired date.
    - The application displays log entries that are included in the desired date.

3. **Changing Log Encoding**:
    - Select the option (3) to change log encoding.
    - Choose between UTF-8 or ISO-8859-1.
    - The application confirms the change of encoding.

4. **Reading All Log Entries**:
    - Select the option (4) to read log entries.
    - The application displays all stored log entries.

5. **Exiting the Application**:
    - Select the option (5) to exit the application.
    - The application terminates with a message indicating so to the user.

