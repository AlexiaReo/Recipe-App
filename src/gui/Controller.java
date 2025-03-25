package gui;

import service.Service;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;
import java.util.stream.Collectors;

public class Controller {
    private Service service;
    private List<String[]> allRecipes; // Stores all recipes for filtering
    private String[] previousRecipe; // Stores last modified recipe (for undo)

    @FXML private TableView<String[]> recipeTable;
    @FXML private TableColumn<String[], String> nameColumn;
    @FXML private TableColumn<String[], String> cuisineColumn;
    @FXML private TableColumn<String[], String> descriptionColumn;
    @FXML private TableColumn<String[], String> ingredientsColumn;
    @FXML private TextField descriptionField;
    @FXML private ComboBox<String> cuisineFilter;
    @FXML private TextField searchField;
    @FXML private TextField ingredientsField; // New field for ingredient search

    public Controller() {}

    public void setService(Service service) {
        this.service = service;
        loadRecipes();
        loadCuisines();
    }

    @FXML
    public void initialize() {
        // Configure columns
        nameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()[0]));
        cuisineColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()[1]));
        descriptionColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()[2]));
        ingredientsColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()[3]));
    }

    private void loadRecipes() {
        if (service != null) {
            allRecipes = service.getAllRecipes();
            recipeTable.setItems(FXCollections.observableArrayList(allRecipes));
        }
    }

    private void loadCuisines() {
        if (service != null) {
            List<String> cuisines = service.getAllCuisines();
            cuisineFilter.setItems(FXCollections.observableArrayList(cuisines));
        }
    }

    @FXML
    private void filterRecipes() {
        String selectedCuisine = cuisineFilter.getValue();
        String searchText = searchField.getText().toLowerCase();

        List<String[]> filteredRecipes = allRecipes.stream()
                .filter(recipe -> (selectedCuisine == null || recipe[1].equals(selectedCuisine))) // Filter by cuisine
                .filter(recipe -> recipe[0].toLowerCase().contains(searchText) ||
                        recipe[2].toLowerCase().contains(searchText)) // Filter by name/description
                .collect(Collectors.toList());

        recipeTable.setItems(FXCollections.observableArrayList(filteredRecipes));
    }

    @FXML
    private void updateDescription() {
        String[] selected = recipeTable.getSelectionModel().getSelectedItem();
        String newDescription = descriptionField.getText();

        if (selected != null && !newDescription.isEmpty()) {
            String name = selected[0]; // Get recipe name

            // Store the current state for undo
            previousRecipe = new String[]{selected[0], selected[1], selected[2], selected[3]};

            // Update in the database
            service.updateRecipeDescription(name, newDescription);

            // Update UI
            selected[2] = newDescription; // Update description in local list
            recipeTable.refresh(); // Refresh TableView
            descriptionField.clear(); // Clear text field
        }
    }


    @FXML
    private void showAllRecipes() {
        loadRecipes(); // Reload all recipes
    }

    @FXML
    private void findRecipeByIngredients() {
        String input = ingredientsField.getText().toLowerCase().trim();

        if (input.isEmpty()) {
            showAlert("Please enter ingredients to search.");
            return;
        }

        List<String> inputIngredients = List.of(input.split("\\s*,\\s*")); // Split input by commas and trim spaces

        List<String[]> matchingRecipes = allRecipes.stream()
                .filter(recipe -> {
                    List<String> recipeIngredients = List.of(recipe[3].toLowerCase().split("\\s*,\\s*"));
                    return recipeIngredients.containsAll(inputIngredients);
                })
                .collect(Collectors.toList());

        if (matchingRecipes.isEmpty()) {
            showAlert("No recipes found with the given ingredients.");
        } else {
            recipeTable.setItems(FXCollections.observableArrayList(matchingRecipes));
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("No Recipes Found");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML private TextField newNameField;
    @FXML private TextField newCuisineField;
    @FXML private TextField newDescriptionField;
    @FXML private TextField newIngredientsField;

    @FXML
    private void addRecipe() {
        String name = newNameField.getText();
        String cuisine = newCuisineField.getText();
        String description = newDescriptionField.getText();
        String ingredients = newIngredientsField.getText();

        if (name.isEmpty() || cuisine.isEmpty() || description.isEmpty() || ingredients.isEmpty()) {
            showAlert("All fields must be filled to add a recipe.");
            return;
        }

        service.addRecipe(name, cuisine, description, ingredients);
        loadRecipes(); // Refresh the table
    }

    @FXML
    private void deleteRecipe() {
        String[] selected = recipeTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            service.deleteRecipe(selected[0]);
            loadRecipes();
        }
    }


}
