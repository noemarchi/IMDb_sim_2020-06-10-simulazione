package it.polito.tdp.imdb.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.polito.tdp.imdb.model.Actor;
import it.polito.tdp.imdb.model.Adiacenza;
import it.polito.tdp.imdb.model.Director;
import it.polito.tdp.imdb.model.Movie;

public class ImdbDAO {
	
	public Map<Integer, Actor> mapAllActors()
	{
		String sql = "SELECT * FROM actors";
		Map<Integer, Actor> result = new HashMap<Integer, Actor>();
		Connection conn = DBConnect.getConnection();

		try 
		{
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			
			while (res.next()) 
			{

				Actor actor = new Actor(res.getInt("id"), res.getString("first_name"), res.getString("last_name"),
						res.getString("gender"));
				
				result.put(actor.getId(), actor);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Movie> listAllMovies()
	{
		String sql = "SELECT * FROM movies";
		List<Movie> result = new ArrayList<Movie>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Movie movie = new Movie(res.getInt("id"), res.getString("name"), 
						res.getInt("year"), res.getDouble("rank"));
				
				result.add(movie);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Director> listAllDirectors()
	{
		String sql = "SELECT * FROM directors";
		List<Director> result = new ArrayList<Director>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Director director = new Director(res.getInt("id"), res.getString("first_name"), res.getString("last_name"));
				
				result.add(director);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<String> listAllGenres()
	{
		String sql = "SELECT DISTINCT genre "
				+ "FROM movies_genres";
		
		List<String> result = new ArrayList<String>();
		Connection conn = DBConnect.getConnection();

		try 
		{
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			
			while (res.next()) 
			{
				result.add(res.getString("genre"));
			}
			
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Actor> getVertici(String genere, Map<Integer, Actor> idMap)
	{
		String sql = "SELECT DISTINCT r.actor_id AS id "
				+ "FROM roles r, movies m, movies_genres g "
				+ "WHERE r.movie_id = m.id "
				+ "AND m.id = g.movie_id "
				+ "AND g.genre = ?";
		
		List<Actor> result = new ArrayList<Actor>();
		Connection conn = DBConnect.getConnection();

		try 
		{
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, genere);
			ResultSet res = st.executeQuery();
			
			while (res.next()) 
			{
				int id = res.getInt("id");
				
				Actor a = idMap.get(id);
				
				result.add(a);
			}
			
			conn.close();
			return result;
			
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Adiacenza> getAdiacenze(String genere, Map<Integer, Actor> idMap)
	{
		String sql = "SELECT r1.actor_id AS a1, r2.actor_id AS a2, COUNT(DISTINCT r1.movie_id) AS peso "
				+ "FROM roles r1, movies_genres g, roles r2 "
				+ "WHERE r1.movie_id = g.movie_id "
				+ "AND r1.movie_id = r2.movie_id "
				+ "AND g.genre = ? "
				+ "AND r1.actor_id > r2.actor_id "
				+ "GROUP BY r1.actor_id, r2.actor_id";
		
		List<Adiacenza> result = new ArrayList<Adiacenza>();
		Connection conn = DBConnect.getConnection();

		try 
		{
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, genere);
			ResultSet res = st.executeQuery();
			
			while (res.next()) 
			{
				int id1 = res.getInt("a1");
				int id2 = res.getInt("a2");
				
				Actor a1 = idMap.get(id1);
				Actor a2 = idMap.get(id2);
				
				int peso = res.getInt("peso");
				
				result.add(new Adiacenza(a1, a2, peso));
			}
			
			conn.close();
			return result;
			
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
			return null;
		}
	}
	
	
}
