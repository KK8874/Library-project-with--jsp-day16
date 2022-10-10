package com.infinite.Library;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LibraryDAO {
	
	public List<TranBook> issueBooks(String user) throws ClassNotFoundException, SQLException {
		Connection connection = ConnectionHelper.getConnection();
		String sql = "select * from TranBook where UserName=?";
		PreparedStatement pst = connection.prepareStatement(sql);
		pst.setString(1, user);
		ResultSet rs = pst.executeQuery();
		TranBook tranBook = null;
		List<TranBook> tranBookList = new ArrayList<TranBook>();
		while(rs.next()) {
			tranBook = new TranBook();
			tranBook.setBookId(rs.getInt("BookId"));
			tranBook.setUserName(user);
			tranBook.setFromDate(rs.getDate("FromDate"));
			tranBookList.add(tranBook);
		}
		return tranBookList;
	}
	
	
	public TranBook searchTranBook(String user,int bookId)throws ClassNotFoundException, SQLException {
		Connection connection = ConnectionHelper.getConnection();
		String sql = "select * from TranBook where UserName=? and bookid=?";
		PreparedStatement pst = connection.prepareStatement(sql);
		pst.setString(1, user);
		pst.setInt(2, bookId);
		ResultSet rs = pst.executeQuery();
		TranBook tranBook = null;
		if(rs.next()){
			tranBook=new TranBook();
			tranBook.setBookId(rs.getInt("BookId"));
			tranBook.setUserName(user);
			tranBook.setFromDate(rs.getDate("FromDate"));
		}
		return tranBook;
	}
	public String returnBooks(String user,int bookId)throws ClassNotFoundException, SQLException {
		Connection connection = ConnectionHelper.getConnection();
		TranBook tranBook=searchTranBook(user,bookId);
		String sql = "Insert into TransReturn(userName,BookId,FromDate) value(?,?,?)";
		PreparedStatement pst = connection.prepareStatement(sql);
		pst.setString(1, user);
		pst.setInt(2, bookId);
		pst.setDate(3, tranBook.getFromDate());
		pst.executeUpdate();
		sql="Update Books set TotalBooks=TotalBooks+1 where id =?";
		pst = connection.prepareStatement(sql);
		pst.setInt(1, bookId);
		pst.executeUpdate();
		sql="Delete from TranBook where UserName=? AND BookId =?";
		pst = connection.prepareStatement(sql);
		pst.setString(1, user);
		pst.setInt(2, bookId);
		pst.executeUpdate();
		return "your Book " +bookId+"Returend SuccessFully";
	}
		
	
	public int issueOrNotBook(String userName, int bookId) throws ClassNotFoundException, SQLException{
	Connection connection=ConnectionHelper.getConnection();
	String sql="select count(*) cnt from tranbook where UserName=? and BookId=?";
	PreparedStatement pst = connection.prepareStatement(sql);
	pst.setString(1,userName);
	pst.setInt(2,bookId);
	ResultSet rs = pst.executeQuery();
	rs.next();
	int count = rs.getInt("cnt");
	return count;
	
	}
	
	public String issueBook(String userName, int bookId) throws ClassNotFoundException, SQLException{
	int count = issueOrNotBook(userName, bookId);
	if(count==0){
		Connection connection=ConnectionHelper.getConnection();
	String sql="Insert into TranBook(UserName,BookId) values(?,?)";
	PreparedStatement pst = connection.prepareStatement(sql);

	pst.setString(1,userName);
	pst.setInt(2,bookId);
	pst.executeUpdate();
	sql="Update Books set TotalBooks=TotalBooks-1 where id=?";
	pst= connection.prepareStatement(sql);
	pst.setInt(1,bookId);
	pst.executeUpdate();
	return " Book with ID of "+bookId+" issue raised successfully";
	}else{
		return "Book Id "+bookId+" for User"+userName+"already Issued..";
	}
	}
	public List<Books> searchBooks(String searchType, String searchValue) throws ClassNotFoundException, SQLException {
		String sql;
		boolean isValid=true;
		if(searchType.equals("id")) {
			sql = " SELECT * FROM Books WHERE Id = ? " ;
		} else if(searchType.equals("bookname")) {
			sql = " SELECT * FROM Books WHERE Name = ? " ;
		} else if(searchType.equals("authorname")) {
			sql = " SELECT * FROM Books WHERE Author = ? " ;
		} else if(searchType.equals("dept")) {
			sql = " SELECT * FROM Books WHERE Dept = ? " ;
		}
		else {
			isValid=false;
			sql = " SELECT * FROM Books" ;
		}
		Connection connection = ConnectionHelper.getConnection();
		PreparedStatement pst = connection.prepareStatement(sql);
		if (isValid==true) {
			pst.setString(1, searchValue);
		} 
		ResultSet rs = pst.executeQuery();
		Books books = null;
		List<Books> booksList = new ArrayList<Books>();
		while(rs.next()) {
			books = new Books();
			books.setId(rs.getInt("id"));
			books.setName(rs.getString("name"));
			books.setAuthor(rs.getString("author"));
			books.setEdition(rs.getString("edition"));
			books.setDept(rs.getString("dept"));
			books.setNoOfBooks(rs.getInt("TotalBooks"));
			booksList.add(books);
		}
		return booksList;
	}
	
	public int authenticate(String user,String password) throws ClassNotFoundException,SQLException{
	      Connection connection=ConnectionHelper.getConnection();
	       String cmd="select count(*) cnt from libusers where UserName=? and Password=?";
	       PreparedStatement pst=connection.prepareStatement(cmd);
	       pst.setString(1, user);
	       pst.setString(2, password);
	       ResultSet rs=pst.executeQuery();
	       rs.next();
	       int count=rs.getInt("cnt");
	       return count;

}
	public List<TranBook> returnHistory(String user) throws ClassNotFoundException, SQLException {
		Connection connection = ConnectionHelper.getConnection();
		PreparedStatement preparedStatement = connection.prepareStatement("select * from transreturn where username=?");
		preparedStatement.setString(1, user);
		ResultSet resultSet = preparedStatement.executeQuery();
		
		TranBook tranBook = null;
		List<TranBook> tranHistoryList = new ArrayList<TranBook>();
		while(resultSet.next()) {
			tranBook = new TranBook();
			tranBook.setBookId(resultSet.getInt("bookid"));
			tranBook.setUserName(user);
			tranBook.setFromDate(resultSet.getDate("fromdate"));
			
			tranHistoryList.add(tranBook);
		}
		return tranHistoryList;
	}
}
