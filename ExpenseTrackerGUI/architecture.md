# Expense Tracker GUI - Complete Architecture & Documentation

## Table of Contents
1. [Project Overview](#project-overview)
2. [Architecture & Data Flow](#architecture--data-flow)
3. [Project Structure](#project-structure)
4. [Java Concepts Used](#java-concepts-used)
5. [Backend Code Explanation](#backend-code-explanation)
6. [How It Works - Detailed Walkthrough](#how-it-works---detailed-walkthrough)
7. [CRUD Operations Explained](#crud-operations-explained)

---

## Project Overview

**Expense Tracker GUI** is a JavaFX desktop application that helps users manage personal expenses. It provides a modern graphical interface for:
- Adding new expenses with date, description, amount, and category
- Updating existing expenses
- Deleting expenses
- Searching expenses by category
- Viewing total expenses and transaction count
- Persisting all data to CSV file

The application follows the **MVC (Model-View-Controller)** pattern with clean separation between UI and business logic.

---

## Architecture & Data Flow

### Overall Flow Diagram
```
User Interface (JavaFX FXML)
        ↓
   Controller (ExpenseController)
        ↓
Business Logic (ExpenseManager)
        ↓
Data Model (Expense)
        ↓
Persistent Storage (CSV File)
```

### Data Flow for Each Operation

**Adding an Expense:**
1. User enters data in FXML UI fields (description, amount, category, date)
2. Click "New Expense" button → triggers addExpense() in Controller
3. Controller validates input and calls manager.addExpense()
4. Manager creates new Expense object, assigns auto-incremented ID
5. Manager saves to CSV via saveToFile()
6. Manager returns to Controller, which refreshes TableView to display new expense

**Updating an Expense:**
1. User selects expense from table
2. Modifies fields in UI input area
3. Click "Update" button → triggers updateExpense() in Controller
4. Controller gets selected expense, updates its fields
5. Manager saves to CSV
6. Controller calls tableView.refresh() AND set(index, object) to update display

**Deleting an Expense:**
1. User selects expense from table
2. Click "Delete" button → triggers deleteExpense() in Controller
3. Controller gets expense ID from selected row
4. Manager calls deleteExpenseById(id) and saves to CSV
5. Controller refreshes table to remove deleted row

---

## Project Structure

```
C:\ExpenseGUI\ETGUI\
│
├── pom.xml                                    # Maven build configuration
├── module-info.java                           # Java 9+ module declarations
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/expensetrackergui/etgui/
│   │   │       ├── App.java                   # JavaFX Application launcher
│   │   │       ├── Expense.java               # Data model class
│   │   │       ├── ExpenseManager.java        # Business logic & CSV I/O
│   │   │       └── controller/
│   │   │           └── ExpenseController.java # FXML controller (UI handler)
│   │   │
│   │   └── resources/
│   │       └── com/expensetrackergui/etgui/
│   │           └── app.fxml                   # UI layout definition (XML)
│   │
│   └── test/java/                             # Unit tests (if any)
│
├── data/
│   └── expenses.csv                           # CSV data file (persistent storage)
│
└── target/                                    # Compiled output (ignored in version control)
```

### Folder Functions

**src/main/java/com/expensetrackergui/etgui/**
- Contains all Java source code for the application
- **App.java**: Entry point; initializes JavaFX stage and loads FXML
- **Expense.java**: Pure data model (no UI dependencies)
- **ExpenseManager.java**: Business logic and CSV file handling
- **controller/ExpenseController.java**: Links FXML UI to Java code

**src/main/resources/com/expensetrackergui/etgui/**
- Contains FXML file (XML-based UI layout)
- **app.fxml**: Defines all buttons, text fields, tables, labels - the entire GUI

**data/**
- **expenses.csv**: Stores all expense records; created/updated by ExpenseManager
- Format: `id,date,description,amount,category` (with header row)

---

## Java Concepts Used

### 1. **MVC Pattern (Model-View-Controller)**
- **Model**: Expense class (represents data)
- **View**: app.fxml (defines UI layout)
- **Controller**: ExpenseController (handles user interactions)
- Benefit: Separation of concerns; UI can change without affecting business logic

### 2. **JavaFX Framework**
- Modern Java UI framework for desktop applications
- Uses Stage (window), Scene (content), and Nodes (components like buttons, tables)
- **FXML**: XML-based UI language (similar to HTML) that makes UI definition declarative

### 3. **FXML & FXMLLoader**
- FXML is an XML markup language for JavaFX UIs
- FXMLLoader parses app.fxml and creates Java objects for each UI element
- Reflection is used to bind FXML elements to Java @FXML-annotated fields
- Controller attribute in FXML specifies which Java class handles events

### 4. **Controller & Event Binding**
- @FXML annotation marks fields that FXMLLoader should inject from FXML
- onAction="#methodName" in FXML binds button clicks to controller methods
- Example: `<Button onAction="#addExpense"/>` calls the addExpense() method in controller

### 5. **Observable Collections**
- FXCollections.observableArrayList() wraps Java List for JavaFX
- Allows TableView to automatically reflect changes (when items are added/removed)
- Note: Property value changes require manual refresh (Expense class doesn't use ObservableProperties)

### 6. **TableView & TableColumn**
- TableView displays data in table format
- TableColumn.setCellValueFactory() uses PropertyValueFactory to map object fields to columns
- Example: PropertyValueFactory("amount") displays Expense.getAmount() in that column

### 7. **Java Module System (Java 9+)**
- module-info.java declares module dependencies
- `requires javafx.controls;` says this module depends on javafx.controls
- `opens com.expensetrackergui.etgui.controller to javafx.fxml;` allows FXMLLoader reflection access
- Stricter visibility control than older Java versions

### 8. **Exception Handling**
- try-catch blocks handle NumberFormatException for invalid amount input
- DatePickerException if invalid dates selected (automatically handled by DatePicker)
- No explicit file I/O exception handling - assumes CSV file operations succeed

### 9. **CSV File I/O**
- Manual parsing of CSV format (no external library)
- Uses File, FileWriter, BufferedReader, BufferedWriter for I/O
- Each line split by comma to extract fields
- No CSV validation - assumes well-formed data

### 10. **Lambda Expressions & Functional Programming**
- Used in filtering expenses by category
- Example: `stream().filter().collect()` for searching expenses

---

## Backend Code Explanation

### 1. App.java - JavaFX Application Bootstrap

**Purpose**: Entry point for the JavaFX application; initializes the Stage and loads FXML

```java
public class App extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        // Line 11: Load app.fxml from resources
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("app.fxml"));
        
        // Line 12: Create Scene (container for all UI nodes)
        Scene scene = new Scene(fxmlLoader.load());
        
        // Line 14: Set window title
        stage.setTitle("Expense Tracker");
        
        // Line 15: Add scene to stage and display
        stage.setScene(scene);
        stage.show();
    }
}
```

**Key Points**:
- Extends Application (JavaFX's Application class)
- start() is called automatically by JavaFX runtime
- FXMLLoader.load() reads app.fxml, creates all UI components, and instantiates ExpenseController
- Scene is a container holding all UI nodes (buttons, fields, tables, labels)
- Stage is the window itself

**Flow**:
1. JavaFX calls start(Stage stage)
2. FXMLLoader reads app.fxml file
3. For each UI element in FXML, JavaFX creates corresponding Java object
4. @FXML-annotated fields in ExpenseController are populated via reflection
5. Scene containing all UI nodes is added to Stage
6. Stage.show() displays window to user

---

### 2. Expense.java - Data Model

**Purpose**: Represents a single expense record with immutable ID/date and mutable other fields

```java
public class Expense {
    private int id;              // Unique identifier (auto-incremented)
    private String date;         // Format: "dd-MM-yyyy"
    private String description;  // What was purchased
    private double amount;       // Cost in rupees
    private String category;     // Type (e.g., "Food", "Transport", "Entertainment")
    
    // Constructor (Line 11-17)
    public Expense(int id, String date, String description, double amount, String category) {
        this.id = id;
        this.date = date;
        this.description = description;
        this.amount = amount;
        this.category = category;
    }
    
    // Getters (Line 20-24)
    public int getId() { return id; }
    public String getDate() { return date; }
    public String getDescription() { return description; }
    public double getAmount() { return amount; }
    public String getCategory() { return category; }
    
    // Setters for mutable fields (Line 27-29)
    public void setDescription(String description) { this.description = description; }
    public void setAmount(double amount) { this.amount = amount; }
    public void setCategory(String category) { this.category = category; }
    // Note: setId() and setDate() not provided (immutable after creation)
    
    // toString() for display (Line 31-35)
    @Override
    public String toString() {
        return String.format("ID: %d | Date: %s | %s | Rs. %.2f | %s", 
                           id, date, description, amount, category);
    }
}
```

**Key Points**:
- Pure data container (no business logic or UI code)
- Can be used by CLI or GUI without modification
- Getters/setters follow JavaBean convention
- ID and date are effectively immutable (no setters)
- toString() used for debugging and CLI display

**Why This Design**:
- Separation of concerns: data model doesn't know about CSV or UI
- Reusability: same class works for CLI and GUI backends
- Simplicity: no JavaFX Property bindings (kept for backward compatibility with CLI)

---

### 3. ExpenseManager.java - Business Logic

**Purpose**: Handles all CRUD operations and CSV persistence

```java
public class ExpenseManager {
    private List<Expense> expenses;
    private int nextId = 1000;  // Auto-increment starts at 1000
    
    // Constructor (Line 11-13)
    public ExpenseManager() {
        expenses = new ArrayList<>();
        loadFromFile();  // Load existing data from CSV on startup
    }
    
    // CREATE: Add new expense (Line 15-18)
    public void addExpense(String description, double amount, String category, String date) {
        Expense expense = new Expense(++nextId, date, description, amount, category);
        expenses.add(expense);
        saveToFile();  // Persist to CSV immediately
    }
    
    // DELETE: Remove expense by ID (Line 20-23)
    public void deleteExpenseById(int id) {
        expenses.removeIf(e -> e.getId() == id);
        saveToFile();  // Persist to CSV immediately
    }
    
    // SAVE: Write all expenses to CSV (Line 25-35)
    public void saveToFile() {
        try (FileWriter writer = new FileWriter("data/expenses.csv")) {
            // Write header row
            writer.write("id,date,description,amount,category\n");
            
            // Write each expense as CSV line
            for (Expense e : expenses) {
                writer.write(String.format("%d,%s,%s,%.2f,%s\n",
                    e.getId(), e.getDate(), e.getDescription(), e.getAmount(), e.getCategory()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    // LOAD: Read expenses from CSV (Line 37-55)
    private void loadFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader("data/expenses.csv"))) {
            String line;
            reader.readLine();  // Skip header row
            
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 5) {
                    int id = Integer.parseInt(parts[0]);
                    String date = parts[1];
                    String description = parts[2];
                    double amount = Double.parseDouble(parts[3]);
                    String category = parts[4];
                    
                    expenses.add(new Expense(id, date, description, amount, category));
                    if (id >= nextId) nextId = id + 1;  // Update nextId to avoid ID conflicts
                }
            }
        } catch (FileNotFoundException e) {
            // File doesn't exist yet (first run) - silently skip
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    // READ: Get all expenses (Line 57-59)
    public List<Expense> getExpenses() {
        return new ArrayList<>(expenses);  // Return copy to prevent external modification
    }
}
```

**Key Methods Explained**:

1. **Constructor**: Initializes empty list and loads existing expenses from CSV
   - Called once when application starts
   - If CSV doesn't exist, starts with empty list

2. **addExpense()**: Creates and adds new Expense
   - Increments nextId before creating Expense (ensures unique IDs)
   - Adds to list and immediately saves to CSV
   - Called from controller when user clicks "New Expense"

3. **deleteExpenseById()**: Removes expense matching given ID
   - Uses removeIf() with lambda to remove expense where id matches
   - Saves to CSV after removal

4. **saveToFile()**: Writes all expenses to CSV
   - Creates/overwrites data/expenses.csv
   - Writes header row first: "id,date,description,amount,category"
   - Each expense becomes one CSV line with comma-separated values
   - Called after every CRUD operation to persist changes

5. **loadFromFile()**: Reads CSV and reconstructs Expense objects
   - Runs on startup to load previously saved expenses
   - Skips header row (readLine() discards first line)
   - Splits each line by comma to extract fields
   - Parses data types: id (int), date (string), description (string), amount (double), category (string)
   - Updates nextId to prevent ID conflicts

---

### 4. ExpenseController.java - FXML Controller & UI Logic

**Purpose**: Connects FXML UI to backend; handles all user interactions

**Key @FXML Fields** (Lines 17-39):
- descriptionField, amountField: TextFields for input
- categoryCombo: ComboBox dropdown for categories
- datePicker: Calendar widget for date selection
- expenseTable: TableView displaying all expenses
- idColumn, dateColumn, descriptionColumn, amountColumn, categoryColumn: TableColumns
- totalExpensesLabel, transactionCountLabel: Labels showing summary statistics
- searchField: TextField for category filtering

**initialize() Method** (Lines 45-50):
- Called automatically by FXMLLoader after all @FXML fields injected
- Creates ExpenseManager instance
- Calls setupTable() to configure column bindings
- Loads categories into dropdown
- Loads existing expenses from CSV
- Calculates and displays initial summary

**addExpense() Method** (Lines 74-94):
```
User clicks "New Expense" button
    ↓
Get values from: descriptionField, amountField, categoryCombo, datePicker
    ↓
Validate all fields not empty
    ↓
Call manager.addExpense(description, amount, category, date)
    ↓
Manager creates Expense with ++nextId, saves to CSV
    ↓
Controller: loadExpenses() → refreshes table with new data
Controller: clearFields() → clears input fields
Controller: refreshSummary() → updates total and count labels
    ↓
New expense visible in table, saved to CSV
```

**updateExpense() Method - CRITICAL BUG FIX** (Lines 97-132):
```
User selects row in table, edits fields, clicks "Update"
    ↓
Get selected Expense object from TableView
    ↓
Get new values from UI input fields
    ↓
Call selected.setDescription(), setAmount(), setCategory()
    ↓
Call manager.saveToFile() to write CSV
    ↓
*** CRITICAL PART (Lines 122-123):
    int selectedIndex = expenseTable.getSelectionModel().getSelectedIndex();
    expenseTable.getItems().set(selectedIndex, selected);  // Force table to re-render
    expenseTable.refresh();  // Force visual refresh
    
    This is NECESSARY because:
    - Expense class uses plain Java fields (not JavaFX Properties)
    - TableView doesn't auto-detect property changes
    - Without manual refresh, user sees old values despite data being updated
***
    ↓
Clear fields, update summary labels
    ↓
Updated expense visible in table, saved to CSV
```

**deleteExpense() Method** (Lines 135-144):
```
User selects row, clicks "Delete"
    ↓
Get selected Expense ID
    ↓
Call manager.deleteExpenseById(id) which removes from list and saves CSV
    ↓
Controller: loadExpenses() → refreshes table
Controller: clearFields() and refreshSummary()
    ↓
Row removed from table and CSV
```

**refreshSummary() Method** (Lines 159-174):
```
Check if search field has text
    ↓
If empty: show ALL expenses from manager.getExpenses()
If filled: filter by category using Stream API
    ↓
Update TableView with filtered list
    ↓
Calculate total amount: stream().mapToDouble(Expense::getAmount).sum()
Calculate count: filtered list size
    ↓
Update labels: totalExpensesLabel and transactionCountLabel
```

---

## How It Works - Detailed Walkthrough

### Scenario: User Adds New Expense

1. **UI Level**: User enters:
   - Description: "Lunch at restaurant"
   - Amount: "450"
   - Category: "Food" (selected from dropdown)
   - Date: "15-Jan-2025" (selected from date picker)

2. **User clicks "New Expense" button**

3. **Controller Level** (ExpenseController.addExpense() is triggered):
   - Line 79: `descriptionField.getText()` → "Lunch at restaurant"
   - Line 80: `Double.parseDouble(amountField.getText())` → 450.0
   - Line 81: `categoryCombo.getValue()` → "Food"
   - Line 82: Date converted to "15-01-2025" format
   - Lines 87-88: Validation passes (all fields filled)
   - Line 91: Calls `manager.addExpense("Lunch at restaurant", 450.0, "Food", "15-01-2025")`

4. **Business Logic Level** (ExpenseManager.addExpense()):
   - Line 16: `++nextId` increments from 1000 to 1001
   - Line 16: Creates new Expense(1001, "15-01-2025", "Lunch at restaurant", 450.0, "Food")
   - Line 17: Adds to expenses list
   - Line 18: Calls `saveToFile()` immediately

5. **Persistence Level** (ExpenseManager.saveToFile()):
   - Opens data/expenses.csv for writing
   - Line 27: Writes header: "id,date,description,amount,category"
   - Lines 30-31: For each Expense, writes line: "1001,15-01-2025,Lunch at restaurant,450.00,Food"
   - File is updated on disk

6. **Back to Controller** (ExpenseController.addExpense() continues):
   - Line 93: `loadExpenses()` - reloads all expenses from manager
   - Table is populated with new ObservableList including the new expense
   - Line 94: `clearFields()` - empties input fields
   - Line 95: `refreshSummary()` - recalculates total and count
   - User sees new expense appear in table immediately

---

## CRUD Operations Explained

### CREATE: Adding Expense
- **User Action**: Fill fields → Click "New Expense"
- **Controller**: Validates input, calls manager.addExpense()
- **Manager**: Creates Expense with ++nextId, adds to list, saves to CSV
- **UI Update**: Table refreshed, fields cleared, totals recalculated
- **Result**: New row appears in table, data saved to CSV

### READ: Viewing Expenses
- **Initial**: On app startup, initialize() calls manager.getExpenses()
- **Display**: TableView populated with ObservableList of Expenses
- **TableColumn**: Each column bound to Expense getter (PropertyValueFactory)
- **Search Filter**: refreshSummary() filters by category if search field filled
- **Result**: User sees filtered or full expense list in table

### UPDATE: Modifying Expense
- **User Action**: Select row → Edit fields → Click "Update"
- **Controller**: Gets selected Expense, modifies its fields, saves via manager
- **Manager**: Saves updated list to CSV
- **UI Update**: **CRITICAL PART** - Must call set(index, object) + refresh() manually
- **Why Manual?**: Expense class doesn't use JavaFX Properties; changes not auto-detected
- **Result**: Updated row visible in table, data saved to CSV

### DELETE: Removing Expense
- **User Action**: Select row → Click "Delete"
- **Controller**: Gets selected Expense ID, calls manager.deleteExpenseById()
- **Manager**: Removes matching Expense, saves to CSV
- **UI Update**: Table refreshed, fields cleared, totals recalculated
- **Result**: Row removed from table, data deleted from CSV

---

## Key Design Patterns

### MVC Separation
```
Expense.java (Model) - Just data, no business logic or UI
    ↑
    └─── ExpenseManager.java (Business Logic) - CRUD, CSV I/O, no UI code
        ↑
        └─── ExpenseController.java (View/Controller) - UI interactions, JavaFX components
            ↑
            └─── app.fxml (View) - UI layout and structure
```

### Why This Separation Matters
- **Backend is reusable**: ExpenseManager works with CLI or GUI without changes
- **UI is replaceable**: Could switch to web UI (Spring Boot) using same backend
- **Testing is easier**: Can test manager without UI components
- **Maintenance is simpler**: Bugs in CSV logic don't affect UI code and vice versa

---

## Technical Notes

### Java Version & Module System
- **Java 15**: Required for text blocks (triple-quoted strings) in ConsoleUI
- **module-info.java**: Declares dependencies and opens packages for reflection
- **Key**: `opens com.expensetrackergui.etgui.controller to javafx.fxml;` allows FXMLLoader to use reflection on controller

### CSV vs Database
- **Current**: Pure CSV file storage (no external database)
- **Pros**: Simple, no setup required, easy to inspect data (text file)
- **Cons**: No transaction safety, no query optimization, manual parsing needed
- **Future**: Could replace with SQLite for better data integrity

### Observable Collections
- `FXCollections.observableArrayList()` wraps Java List for JavaFX
- TableView notified when items added/removed from list
- For property changes: Need manual refresh (or use JavaFX Properties in Expense class)

### Thread Safety
- No multithreading in this application
- All operations on UI thread (safe for Swing/JavaFX)
- If added background tasks, would need Platform.runLater() to update UI from other threads

---

## Summary

This application demonstrates how to build a complete desktop application with:
- Clean MVC architecture separating data, business logic, and UI
- JavaFX for modern UI with FXML for declarative layout
- CSV-based persistence for simple data storage
- Event handling and user interaction through controller pattern
- TableView with dynamic data updates and filtering

The design allows easy expansion (categories, report generation, data export) and code reuse (same backend can power CLI or web UI).
