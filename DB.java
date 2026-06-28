import java.sql.*;

public class DB {

    public static Connection getConnection() {
        try {
            Class.forName("org.postgresql.Driver");

            String url = "jdbc:postgresql://aws-1-ap-northeast-1.pooler.supabase.com:5432/postgres?user=postgres.vvkybcoacfabwuxgueze&password=FoodOrdering123@";

            return DriverManager.getConnection(url);

        } catch (ClassNotFoundException e) {
            System.out.println("PostgreSQL driver not found");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Database connection failed");
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("Something went wrong");
            e.printStackTrace();
        }

        return null;
    }
}