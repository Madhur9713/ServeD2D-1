package com.sapient.dao;

import java.sql.SQLException;
import com.sapient.dto.LoginUser;

import com.sapient.entity.*;

public interface UserDao {
	
	public Boolean verifyUserCreds(LoginUser user) throws DaoException;
	public Boolean addNewUser(User user) throws DaoException ;
	public Double getBalance(Integer userId) throws DaoException, ClassNotFoundException, SQLException ;
	public Boolean addToWallet(Integer userId, Double amount) throws DaoException ;
	public Boolean withdrawFromWallet(Integer userId, Double amount) throws DaoException;
	public User getUserInfo(Integer userId) throws DaoException;
	public User getUser(String email) throws DaoException;

}
