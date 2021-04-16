package com.proj.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.proj.post.Post;
import com.proj.user.User;

public class PostDAO {


	public static boolean insert(Post post)
	{
		Connection conn = null;
		PreparedStatement ps = null;
		String sql = "INSERT INTO posts (author, title, content, time) VALUES (?,?,?,?)";

		try
		{
			conn = DbConnection.getInstance();
			ps = conn.prepareStatement(sql); 
			ps.setInt(1, post.getAuthor());
			ps.setString(2, post.getTitle());
			ps.setString(3, post.getContent());
			ps.setLong(4, post.getTime());
			ps.executeUpdate();
		} catch (SQLException e) {
			System.out.println("[INSERT POST] "+e.getMessage());
			return false;
		} finally {
			if(ps != null){
				try{
					ps.close();
				} catch(Exception e){
					e.printStackTrace();
				}
			}
		}
		return true;
	}

	public static List<Post> getRelationsPostsOf(User user)
	{
		Connection conn = null;
		PreparedStatement ps = null;

		List<Post> posts = new ArrayList<>();

		try
		{
			conn = DbConnection.getInstance();
			ps = conn.prepareStatement("SELECT * FROM relations INNER JOIN posts ON target_user_id=author WHERE main_user_id=?)");
			ps.setInt(1, user.getId());

			ResultSet rs = ps.executeQuery();

			if (rs.next())
			{
				int id = rs.getInt("id_post");
				int author = rs.getInt("author");
				String title = rs.getString("title");
				String content = rs.getString("content");
				long time = rs.getLong("time");

				posts.add(new Post(id, author, title ,content, time));

			}
		}
		catch (SQLException ex)
		{
			System.out.println("[getRelationsPostsOf] " + ex.getMessage());
			return posts;
		}
		finally
		{
			DbConnection.close();
		}
		return posts;
	}
}