package service;

import repository.Repository;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The Service class acts as a middle layer between the GUI (Controller) and the database (Repository).
 * It processes business logic before passing data between the UI and the database.
 */
public class Service {
    private Repository repo; // Repository instance to handle database interactions

    /**
     * Constructor to initialize the Service with a Repository instance.
     * @param repo The repository that interacts with the database.
     */
    public Service(Repository repo) {
        this.repo = repo;
    }

    /**
     * Retrieves all recipes from the database.
     * @return A list of recipes, where each recipe is represented as an array: [name, cuisine, description, ingredients].
     */
    public List<String[]> getAllRecipes() {
        return repo.getAllRecipes();
    }

    /**
     * Retrieves all unique cuisines from the database.
     * @return A list of unique cuisine names.
     */
    public List<String> getAllCuisines() {
        return repo.getAllRecipes().stream()
                .map(recipe -> recipe[1]) // Extracts the cuisine column
                .distinct() // Removes duplicate cuisines
                .collect(Collectors.toList());
    }

    /**
     * Updates the description of a specific recipe.
     * @param name The name of the recipe to update.
     * @param newDescription The new description to set.
     */
    public void updateRecipeDescription(String name, String newDescription) {
        repo.updateDescription(name, newDescription);
    }

    /**
     * Adds a new recipe to the database.
     * @param name Recipe name.
     * @param cuisine Recipe cuisine type.
     * @param description Recipe description.
     * @param ingredients List of ingredients (comma-separated).
     */
    public void addRecipe(String name, String cuisine, String description, String ingredients) {
        repo.addRecipe(name, cuisine, description, ingredients);
    }

    /**
     * Deletes a recipe from the database.
     * @param name The name of the recipe to delete.
     */
    public void deleteRecipe(String name) {
        repo.deleteRecipe(name);
    }
}
