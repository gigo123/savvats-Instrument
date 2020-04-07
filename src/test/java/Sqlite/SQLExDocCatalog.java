package Sqlite;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import models.ExDocCatalog;
import sqlite.SQLConectionHolder;
import sqlite.SqliteExDocCatolgDAO;


class SQLExDocCatalog {
	SqliteExDocCatolgDAO exDoccDAO;
	
	
	void initConnection() {
		SQLConectionHolder conectionHolder = new SQLConectionHolder();
		conectionHolder.setConnString("jdbc:mysql://localhost/instrument1?user=root&password=");
		exDoccDAO = new SqliteExDocCatolgDAO();
		exDoccDAO.setConectionHolder(conectionHolder);
	}
	@Test
	void setConnection() {
		initConnection();
		SQLConectionHolder conectHolder2  = exDoccDAO.getConectionHolder();
		Connection conn =conectHolder2.getConnection();
		assertTrue(conn!=null,"connetion must not be null");
	}
	@Test
	void createExDocCatalog() {
		LocalDate today = LocalDate.now();
		ExDocCatalog docc =new ExDocCatalog("1", 1,  today);
		initConnection();
		exDoccDAO.createExDocCatalog(docc);
		boolean error = exDoccDAO.hasError();
		assertTrue(!error,"must be ok");
	}
	@Test
	void getExDocCatalogById() {
		initConnection();
		ExDocCatalog exdoc =exDoccDAO.getExDocCatalogById(1);
		boolean error = exDoccDAO.hasError();
		assertTrue(!error,"must be ok");
		assertTrue(exdoc!=null,"must not be null");
	}
	@Test
	void getExDocCatalogByDate() {
		initConnection();
		List<ExDocCatalog> exDocList  = exDoccDAO.getExDocCatalogByDate(LocalDate.now());
		boolean error = exDoccDAO.hasError();
		assertTrue(!error,"must be ok");
		assertTrue(exDocList.size()!=0,"must not be 0");
	}
	@Test
	void getExDocCatalogBySname() {
		initConnection();
		ExDocCatalog exdoc =exDoccDAO.getExDocCatalogBySnumber("1");
		boolean error = exDoccDAO.hasError();
		assertTrue(!error,"must be ok");
		assertTrue(exdoc!=null,"must not be null");
	}
	@Test
	void getExDocCataloByNumber() {
		initConnection();
		List<ExDocCatalog> exDocList  = exDoccDAO.getExDocCatalogByNumber(1);
		boolean error = exDoccDAO.hasError();
		assertTrue(!error,"must be ok");
		assertTrue(exDocList.size()!=0,"must not be 0");
	}
	
	@Test
	void deleteInDoc() {
		initConnection();
		List<ExDocCatalog> exDocList  = exDoccDAO.getExDocCatalogByDate(LocalDate.now());
		boolean error = exDoccDAO.hasError();
		assertTrue(!error,"must be ok");
		long id= exDocList.get(exDocList.size()-1).getId();
		exDoccDAO.deleteExDocCatalogDoc(id);
		ExDocCatalog exdoc =exDoccDAO.getExDocCatalogById(id);
		assertTrue(exdoc==null,"doc must be null(deletet)");
	}
}