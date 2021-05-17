package com.sapient.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.sapient.entity.Message;
import com.sapient.utils.DbUtil;

public class MessageDaoImpl implements MessageDao {
	
    public Boolean liveMessage(Message message, Integer senderId)
        throws DaoException {
		if (senderId == message.getSenderId()){
			String sql = "INSERT INTO MESSAGE (message_id, sender_id, reciever_id, content, timestamp) VALUES (?,?,?,?,?)";
			//what to do about user id generation?
			try (Connection conn = DbUtil.createConnection(); 
				PreparedStatement stmt = conn.prepareStatement(sql);
				) {
				stmt.setInt(1, message.getMessageId());
				stmt.setInt(2, message.getSenderId());
				stmt.setInt(3, message.getReceiverId());
				stmt.setString(4, message.getContent());
				stmt.setDate(5, (java.sql.Date) message.getTimestamp());
				stmt.executeUpdate();
				System.out.println("message sent");
				return true;

			} catch (Exception e) {
				throw new DaoException(e);
			}
		}
		else{
			throw new DaoException("Forbidden Request");
		}
	}

    public List<Message> returnAllMessagesForAnOrder(Integer orderId) throws DaoException {
		
		List<Message> chats = new ArrayList<Message>();
		
		String sql = "SELECT message_id,sender_id,reciever_id,content,MESSAGE.timestamp FROM MESSAGE JOIN `ORDER` WHERE (MESSAGE.sender_id = `ORDER`.user_id OR MESSAGE.reciever_id = `ORDER`.user_id) AND `ORDER`.order_id = ?";
		try (Connection conn = DbUtil.createConnection(); PreparedStatement stmt = conn.prepareStatement(sql);) 
		{
			stmt.setInt(1, orderId);
			try(ResultSet rs = stmt.executeQuery();)
			{
                
				if(rs.next()) {
					do {
						Message message = getMessageObj(rs);
						chats.add(message);
					} while (rs.next());

				}
				else
				{
					System.out.println("No data found!"); 
				}
			
			}
			catch (Exception e) {
				throw new DaoException(e);
			}
				
		}
		catch (Exception e) {
			throw new DaoException(e);
		}
		return chats;

	}

	private Message getMessageObj(ResultSet rs) throws SQLException {
		Message message = new Message();
		message.setMessageId(rs.getInt("message_id"));
		message.setSenderId(rs.getInt("sender_id"));
		message.setReceiverId(rs.getInt("reciever_id"));
		message.setContent(rs.getString("content"));
		message.setTimestamp(rs.getDate("timestamp"));
		return message;
	}

    public List<Integer> returnChatsOfUser(Integer userId) throws DaoException {
		
		List<Integer> recievers = new ArrayList<Integer>();
		//Costly Operation
		String sql = "SELECT DISTINCT(reciever_id) from MESSAGE JOIN USER ON MESSAGE.reciever_id=USER.user_id  WHERE sender_id=?  ";
		try (Connection conn = DbUtil.createConnection(); PreparedStatement stmt = conn.prepareStatement(sql);) 
		{
			stmt.setInt(1, userId);
			try(ResultSet rs = stmt.executeQuery();)
			{
                
				if(rs.next()) {
					do {
						recievers.add(rs.getInt("reciever_id"));
					} while (rs.next());

				}
				else
				{
					System.out.println("User has made no conversation!"); 
				}
			
			}
			catch (Exception e) {
				throw new DaoException(e);
			}
				
		}
		catch (Exception e) {
			throw new DaoException(e);
		}
		return recievers;

	}

	public List<Message> returnAllMessagesForAReceiver(Integer recieverId) throws DaoException {
		
		List<Message> chats = new ArrayList<Message>();
		
		String sql = "SELECT message_id,sender_id,reciever_id,content,MESSAGE.timestamp FROM MESSAGE WHERE reciever_id = ?";
		try (Connection conn = DbUtil.createConnection(); PreparedStatement stmt = conn.prepareStatement(sql);) 
		{
			stmt.setInt(1, recieverId);
			try(ResultSet rs = stmt.executeQuery();)
			{
                
				if(rs.next()) {
					do {
						Message message = getMessageObj(rs);
						chats.add(message);
					} while (rs.next());

				}
				else
				{
					System.out.println("No data found!"); 
				}
			
			}
			catch (Exception e) {
				throw new DaoException(e);
			}
				
		}
		catch (Exception e) {
			throw new DaoException(e);
		}
		return chats;

	}

}