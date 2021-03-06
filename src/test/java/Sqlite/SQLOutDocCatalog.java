package Sqlite;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Connection;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;

import models.DocCatalog;
import models.ExDocCatalog;
import models.OutDocCatalog;
import sqlite.SQLConectionHolder;
import sqlite.SqliteExDocCatalogDAO;
import sqlite.SqliteOutDocCatalogDAO;

public class SQLOutDocCatalog {
SqliteOutDocCatalogDAO exDoccDAO;
	


void initConnection() {
	SQLConectionHolder conectionHolder = new SQLConectionHolder();
	conectionHolder.setConnString("jdbc:mysql://localhost/instrument1?user=root&password=");
	exDoccDAO = new SqliteOutDocCatalogDAO();
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
	ExDocCatalog docc =new ExDocCatalog(2020, 1,"2020-1",  today);
	initConnection();
	exDoccDAO.createExDocCatalog(docc);
	boolean error = exDoccDAO.hasError();
	assertTrue(!error,"must be ok");
}
@Test
void getExDocCatalogById() {
	initConnection();
	ExDocCatalog exdoc =exDoccDAO.getExDocCatalogById(3);
	boolean error = exDoccDAO.hasError();
	assertTrue(!error,"must be ok");
	assertTrue(exdoc!=null,"must not be null");
}
@Test
void getExDocCatalogByDate() {
	initConnection();
	List<DocCatalog> exDocList  = exDoccDAO.getExDocCatalogByDate(LocalDate.now());
	boolean error = exDoccDAO.hasError();
	assertTrue(!error,"must be ok");
	assertTrue(exDocList.size()!=0,"must not be 0");
}
@Test
void getExDocCatalogBySname() {
	initConnection();
	DocCatalog exdoc =exDoccDAO.getExDocCatalogBySnumber("2020-4");
	boolean error = exDoccDAO.hasError();
	assertTrue(!error,"must be ok");
	assertTrue(exdoc!=null,"must not be null");
}
@Test
void getExDocCataloByNumber() {
	initConnection();
	List<DocCatalog> exDocList  = exDoccDAO.getExDocCatalogByNumber(1);
	boolean error = exDoccDAO.hasError();
	assertTrue(!error,"must be ok");
	assertTrue(exDocList.size()!=0,"must not be 0");
}

@Test
void deleteExDocCatalo() {
	initConnection();
	List<DocCatalog> exDocList  = exDoccDAO.getExDocCatalogByDate(LocalDate.now());
	boolean error = exDoccDAO.hasError();
	assertTrue(!error,"must be ok");
	long id= exDocList.get(exDocList.size()-1).getId();
	exDoccDAO.deleteExDocCatalogDoc(id);
	ExDocCatalog exdoc =exDoccDAO.getExDocCatalogById(id);
	assertTrue(exdoc==null,"doc must be null(deletet)");
}
@Test
void getExDocCatalogByYear(){
	initConnection();
	List<DocCatalog> exDocList  = exDoccDAO.getExDocCatalogByYear(2020);
	boolean error = exDoccDAO.hasError();
	assertTrue(!error,"must be ok");
	assertTrue(exDocList.size()!=0,"must not be 0");
	
}
@Test
void getExDocCatalogByYearNumber(){
	initConnection();
	List<Integer> exDocList  = exDoccDAO.getExDocCatalogByYearN(2020);
	boolean error = exDoccDAO.hasError();
	assertTrue(!error,"must be ok");
	assertTrue(exDocList.size()!=0,"must not be 0");
}
@Test
void getAllDoc(){
	initConnection();
	List<DocCatalog> exDocList  = exDoccDAO.getAllDoc();
	System.out.println(exDocList);
	boolean error = exDoccDAO.hasError();
	assertTrue(!error,"must be ok");
	assertTrue(exDocList.size()!=0,"must not be 0");
	
}
}
