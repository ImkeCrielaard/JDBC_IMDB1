package tudelft.wis.idm_tasks;

import tudelft.wis.idm_tasks.basicJDBC.interfaces.JDBCTask2Interface;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;

public class JDBC_IMDB {
    public static void main(String[] args) {
        JDBCTask2Interface jdbcTask2Interface = new JDBCTask2Interface() {

            private static final String URL = "jdbc:postgresql://localhost:5432/imdb";
            private static final String USERNAME = "postgres";
            private static final String PASSWORD = "Computer2808!";

            @Override
            public Connection getConnection() throws ClassNotFoundException, SQLException {
                Class.forName("org.postgresql.Driver");
                return DriverManager.getConnection(URL, USERNAME, PASSWORD);
            }

            @Override
            public Collection<String> getTitlesPerYear(int year) throws SQLException, ClassNotFoundException {
                Connection connection = getConnection();
                Collection<String> titles = new ArrayList<>();
                PreparedStatement changeNameStmt = connection.prepareStatement("SELECT primary_title FROM titles WHERE start_year = ? LIMIT 20 ");
                changeNameStmt.setInt(1, year);
                ResultSet resultSet = changeNameStmt.executeQuery();
                while(resultSet.next()){
                titles.add(resultSet.getString("primary_title"));}
                return titles;
            }

            @Override
            public Collection<String> getJobCategoriesFromTitles(String searchString) throws SQLException, ClassNotFoundException {
                Connection connection = getConnection();
                Collection<String> jobCategories = new ArrayList<>();
                                            PreparedStatement getJob = connection.prepareStatement("SELECT DISTINCT ci.job_category FROM titles t " +
                                                    "JOIN cast_info ci on t.title_id = ci.title_id WHERE primary_title LIKE ? LIMIT 20");
                getJob.setString(1, "%" + searchString + "%");
                ResultSet resultSet = getJob.executeQuery();
                while(resultSet.next()){
                    jobCategories.add(resultSet.getString("job_category"));}
                return jobCategories;

            }

            @Override
            public double getAverageRuntimeOfGenre(String genre) throws SQLException, ClassNotFoundException {
                Connection connection = getConnection();
                PreparedStatement changeNameStmt = connection.prepareStatement("SELECT AVG(runtime) AS average_runtime FROM titles JOIN titles_genres " +
                        " ON titles.title_id = titles_genres.title_id  WHERE titles_genres.genre = ?; ");
                changeNameStmt.setString(1, genre);
                ResultSet resultSet = changeNameStmt.executeQuery();
                resultSet.next();
                return resultSet.getDouble("average_runtime");
            }

            @Override
            public Collection<String> getPlayedCharacters(String actorFullname) throws SQLException, ClassNotFoundException {
                Connection connection = getConnection();
                Collection<String> chara = new ArrayList<String>();
                PreparedStatement playedchar  = connection.prepareStatement("SELECT character_name FROM title_person_character c JOIN persons p " +
                        "ON p.person_id = c.person_id  WHERE p.full_name = ? LIMIT 20 ");
                playedchar.setString(1, actorFullname);
                ResultSet resultSet = playedchar.executeQuery();
                while (resultSet.next()){
                    chara.add(resultSet.getString("character_name"));
                }
                return chara;
            }

        };

        try {
            Connection connection = jdbcTask2Interface.getConnection();
            System.out.println("Connected to the database.");
            Collection<String> query1 = jdbcTask2Interface.getTitlesPerYear(2022);
            System.out.println("Query 1 - First 20 titles that were released in 2022: ");
            printResults(query1);
            Collection<String> query2 = jdbcTask2Interface.getJobCategoriesFromTitles("mario");
            System.out.println("Query 2 - Categories that are related to the word mario: ");
             printResults(query2);
            double query3 = jdbcTask2Interface.getAverageRuntimeOfGenre("Action");
            System.out.println("Query 3 - Average runtime for the genre action: ");
            System.out.println(String.format("%.2f", query3) + " minutes.");
            Collection<String> query4 = jdbcTask2Interface.getPlayedCharacters("Brad Pitt");
            System.out.println("Query 4 - All characters that are played by Brad Pitt: ");
            printResults(query4);
            connection.close();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
    private static void printResults
            (Collection<String> query1) {System.out.println(String.join(", \n", query1));
    }

}
