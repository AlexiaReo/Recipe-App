package repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Repository {
    private Connection conn; // Database connection

    // Constructor: Establishes connection to the SQLite database
    public Repository() {
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:data/database.db");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves all recipes from the database.
     * @return List of recipes, where each recipe is an array: [name, cuisine, description, ingredients].
     */
    public List<String[]> getAllRecipes() {
        List<String[]> recipes = new ArrayList<>();
        String sql = "SELECT name, cuisine, description, ingredients FROM recipes ORDER BY cuisine";

        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                recipes.add(new String[]{
                        rs.getString("name"),
                        rs.getString("cuisine"),
                        rs.getString("description"),
                        rs.getString("ingredients") // Fetches the 4th column correctly
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return recipes;
    }

    /**
     * Updates the description of a recipe.
     * @param name The name of the recipe to update.
     * @param newDescription The new description to set.
     */
    public void updateDescription(String name, String newDescription) {
        String sql = "UPDATE recipes SET description = ? WHERE name = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newDescription);
            stmt.setString(2, name);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**
     * Adds a new recipe to the database.
     * @param name Recipe name.
     * @param cuisine Recipe cuisine type.
     * @param description Recipe description.
     * @param ingredients List of ingredients (comma-separated).
     */
    public void addRecipe(String name, String cuisine, String description, String ingredients) {
        String sql = "INSERT INTO recipes (name, cuisine, description, ingredients) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setString(2, cuisine);
            stmt.setString(3, description);
            stmt.setString(4, ingredients);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Deletes a recipe from the database.
     * @param name The name of the recipe to delete.
     */
    public void deleteRecipe(String name) {
        String sql = "DELETE FROM recipes WHERE name = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
