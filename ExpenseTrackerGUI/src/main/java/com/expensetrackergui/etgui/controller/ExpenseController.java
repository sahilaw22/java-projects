package com.expensetrackergui.etgui.controller;

import com.expensetrackergui.etgui.Expense;
import com.expensetrackergui.etgui.ExpenseManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class ExpenseController implements Initializable {
    
    @FXML private TextField descriptionField;
    @FXML private TextField amountField;
    @FXML private ComboBox<String> categoryBox;
    @FXML private DatePicker datePicker;
    @FXML private TextField searchCategoryBox;
    
    @FXML private Button addButton;
    @FXML private Button updateButton;
    @FXML private Button deleteButton;
    @FXML private Button clearButton;
    @FXML private Button summaryButton;
    
    @FXML private TableView<Expense> expenseTable;
    @FXML private TableColumn<Expense, Integer> idColumn;
    @FXML private TableColumn<Expense, String> dateColumn;
    @FXML private TableColumn<Expense, String> descriptionColumn;
    @FXML private TableColumn<Expense, Double> amountColumn;
    @FXML private TableColumn<Expense, String> categoryColumn;
    
    @FXML private Label totalLabel;
    @FXML private Label transactionLabel;
    
    private ExpenseManager expenseManager;
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        expenseManager = new ExpenseManager();
        setupTable();
        setupCategories();
        loadExpenses();
    }

    private void setupTable() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
    }

    private void setupCategories() {
        ObservableList<String> categories = FXCollections.observableArrayList(
            "Food", "Transport", "Rent", "Bills", "Entertainment", "Others"
        );
        categoryBox.setItems(categories);
    }

    private void loadExpenses() {
        ObservableList<Expense> data = FXCollections.observableArrayList(expenseManager.getExpenses());
        expenseTable.setItems(data);
        updateSummary();
    }

    @FXML
    public void addExpense() {
        if (descriptionField.getText().isEmpty() || amountField.getText().isEmpty() || 
            categoryBox.getValue() == null || datePicker.getValue() == null) {
            showAlert("Error", "Please fill all fields!");
            return;
        }

        try {
            String desc = descriptionField.getText();
            double amount = Double.parseDouble(amountField.getText());
            String category = categoryBox.getValue();
            String date = datePicker.getValue().format(dateFormatter);

            expenseManager.addExpense(desc, amount, category, date);
            loadExpenses();
            clearFields();
            showAlert("Success", "Expense added successfully!");
        } catch (NumberFormatException e) {
            showAlert("Error", "Invalid amount!");
        }
    }

    @FXML
    public void updateExpense() {
        Expense selected = expenseTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Error", "Select an expense to update!");
            return;
        }

        if (descriptionField.getText().isEmpty() && amountField.getText().isEmpty() && 
            categoryBox.getValue() == null) {
            showAlert("Error", "Enter new description, amount, or category!");
            return;
        }

        try {
            if (!descriptionField.getText().isEmpty()) {
                selected.setDescription(descriptionField.getText());
            }
            if (!amountField.getText().isEmpty()) {
                selected.setAmount(Double.parseDouble(amountField.getText()));
            }
            if (categoryBox.getValue() != null) {
                selected.setCategory(categoryBox.getValue());
            }

            expenseManager.saveToFile();
            
            int selectedIndex = expenseTable.getSelectionModel().getSelectedIndex();
            expenseTable.getItems().set(selectedIndex, selected);
            expenseTable.refresh();
            
            clearFields();
            showAlert("Success", "Expense updated successfully!");
        } catch (NumberFormatException e) {
            showAlert("Error", "Invalid amount!");
        }
    }

    @FXML
    public void deleteExpense() {
        Expense selected = expenseTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Error", "Select an expense to delete!");
            return;
        }

        expenseManager.deleteExpenseById(selected.getId());
        loadExpenses();
        clearFields();
        showAlert("Success", "Expense deleted successfully!");
    }

    @FXML
    public void clearFields() {
        descriptionField.clear();
        amountField.clear();
        categoryBox.setValue(null);
        datePicker.setValue(null);
        searchCategoryBox.clear();
        expenseTable.getSelectionModel().clearSelection();
    }

    @FXML
    public void refreshSummary() {
        String searchText = searchCategoryBox.getText().trim();
        
        if (searchText.isEmpty()) {
            loadExpenses();
        } else {
            ObservableList<Expense> filtered = FXCollections.observableArrayList();
            for (Expense e : expenseManager.getExpenses()) {
                if (e.getCategory().equalsIgnoreCase(searchText)) {
                    filtered.add(e);
                }
            }
            expenseTable.setItems(filtered);
        }
        updateSummary();
    }

    private void updateSummary() {
        double total = 0;
        int count = expenseTable.getItems().size();
        
        for (Expense e : expenseTable.getItems()) {
            total += e.getAmount();
        }
        
        totalLabel.setText(String.format("Total Expenses : Rs.%.2f", total));
        transactionLabel.setText("Transactions : " + count);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
