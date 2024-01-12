package moviebookingapp.dao;

import moviebookingapp.database.Connector;
import moviebookingapp.entity.FoodDrink;
import moviebookingapp.entity.Seat;
import moviebookingapp.entity.ShowTime;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class FoodDrinkDao implements DAOInterface<FoodDrink>{

    @Override
    public ArrayList<FoodDrink> list() {
        String sql = "select * from food_drink;";
        ArrayList<FoodDrink> ls = new ArrayList<>();
        try {
            Connector conn = Connector.getInstance();
            ResultSet rs = conn.getConn().createStatement().executeQuery(sql);
            while (rs.next()){
                ls.add(new FoodDrink(
                        rs.getInt("id"),
                        rs.getString("path"),
                        rs.getString("product"),
                        rs.getInt("price"),
                        rs.getInt("order_quantity")
                ));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return ls;
    }

    @Override
    public boolean create(FoodDrink o) {
        return false;
    }

    @Override
    public boolean update(FoodDrink s) {

        String sql = "UPDATE food_drink SET order_quantity = ? WHERE id = ?";
        try {
            Connector conn = Connector.getInstance();
            PreparedStatement ps = conn.getConn().prepareStatement(sql);

            // Set the parameters for the update query
            ps.setInt(1, s.getOrder_quantity());
            ps.setInt(2, s.getId());

            int rowsUpdated = ps.executeUpdate();

            // Check if any rows were affected by the update
            if (rowsUpdated > 0) {
                return true; // Update successful
            }
        } catch (Exception e) {
            // Handle exceptions here
            e.printStackTrace(); // Print the exception for debugging
        }
        return false; // Update failed

    }

    @Override
    public boolean delete(int id) {
        return false;
    }

    @Override
    public FoodDrink findOne(int id) {
        return null;
    }
}
