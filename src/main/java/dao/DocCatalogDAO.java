package dao;

import java.time.LocalDate;
import java.util.List;

import models.DocCatalog;

public interface DocCatalogDAO {

	public boolean createExDocCatalog(DocCatalog DocCatalog);
	public DocCatalog getExDocCatalogById(long id);
	public List<DocCatalog> getExDocCatalogByDate(LocalDate date);
	public List<DocCatalog> getExDocCatalogByNumber(int number);
	public List<DocCatalog> getExDocCatalogByYear(int year);
	public List<DocCatalog> getAllDoc();
	public List<Integer> getExDocCatalogByYearN(int year);
	public DocCatalog getExDocCatalogBySnumber(String numberString);
	public boolean deleteExDocCatalogDoc(long id);
	public boolean hasError();
	public void closeConection();

}
