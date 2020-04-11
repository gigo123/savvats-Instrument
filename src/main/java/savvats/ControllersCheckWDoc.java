package savvats;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import dao.BoxDAO;
import dao.ExDocCatalogDAO;
import dao.ExDocDAO;
import dao.InstrumentDAO;
import dao.LocationDAO;
import dao.StorageDAO;
import models.Box;
import models.ExDoc;
import models.ExDocCatalog;
import models.Instrument;
import models.Location;
import models.Storage;

public class ControllersCheckWDoc {
	static ApplicationContext context = new ClassPathXmlApplicationContext("application-context.xml");
	
	public static String createExDocUnwrap(ExDocWEBList docListWrap) {
		List<ExDocWEB> docList = docListWrap.getDocList();
		StringBuilder errorText = new StringBuilder("<ul>");
		LocationDAO locDAO = (LocationDAO) context.getBean("LocationDAO");
		InstrumentDAO instDAO = (InstrumentDAO) context.getBean("InstrumentDAO");
		BoxDAO boxDAO = (BoxDAO) context.getBean("BoxDAO");
		StorageDAO storageDAO = (StorageDAO) context.getBean("StorageDAO");
		ExDocDAO exDocDAO = (ExDocDAO) context.getBean("ExDocDAO");

		List<ExDocTempStore> docTempList = new ArrayList<ExDocTempStore>();
		for (int i = 0; i < docList.size(); i++) {
			ExDocTempStore tempDoc = makeExDoc(docList.get(i), i, locDAO, instDAO, boxDAO, storageDAO);
			errorText.append(tempDoc.getErrorString());
			docTempList.add(makeExDoc(docList.get(i), i, locDAO, instDAO, boxDAO, storageDAO));
		}
		errorText.append("</ul>");
		String errString = errorText.toString();
		if (errString.equals("<ul></ul>")) {
			for (ExDocTempStore exDocTempStore : docTempList) {
				writeExDoc(exDocTempStore.getDoc(), storageDAO, exDocDAO, exDocTempStore.getOutStorageId());
			}
			boxDAO.closeConection();
			locDAO.closeConection();
			instDAO.closeConection();
			storageDAO.closeConection();
			exDocDAO.closeConection();
			return writeExDocCatolog();
		} else {
			boxDAO.closeConection();
			locDAO.closeConection();
			instDAO.closeConection();
			storageDAO.closeConection();
			exDocDAO.closeConection();
			return errString;
		}

	}

	public static ExDocTempStore makeExDoc(ExDocWEB docW, int number, LocationDAO locDAO, InstrumentDAO instDAO,
			BoxDAO boxDAO, StorageDAO storageDAO) {
		StringBuilder errorText = new StringBuilder("");
		number++;
		ExDoc doc = new ExDoc();
		Box box = null;
		boolean error = false;
		long storageId = 0;
		Location location = locDAO.getLocById(Long.parseLong(docW.getInLocation()));
		if (location != null) {
			doc.setInLocation(location);
			box = boxDAO.getBoxByNumber(docW.getInBox(), location.getId());
			if (box == null) {
				error = true;
				errorText.append("<li>неправильная принимающая ячейка в строке " + number + "</li>");
			} else {
				doc.setInBox(box);
			}
		} else {
			error = true;
			errorText.append("<li>неправильное место приема в стоке " + number + "</li>");
		}
		location = locDAO.getLocById(Long.parseLong(docW.getOutLocation()));
		if (location != null) {
			doc.setOutLocation(location);
			box = boxDAO.getBoxByNumber(docW.getOutBox(), location.getId());
			if (box == null) {
				error = true;
				errorText.append("<li>неправильная видающая ячейка в строке " + number + "</li>");
			} else {
				doc.setOutBox(box);
			}
		} else {
			error = true;
			errorText.append("<li>неправильное место  видачи в стоке " + number + "</li>");
		}
		if (!error) {
			Instrument instrument = instDAO.getInstrumentByID(Long.parseLong(docW.getInstrument()));
			if (instrument == null) {
				errorText.append("<li>не правильний инструмент в строке " + number + " </li>");
			} else {

				List<Storage> storeList = storageDAO.getStorageByBox(box);
				boolean hasInstrument = false;
				for (int i = 0; i < storeList.size(); i++) {
					Instrument tempInst = storeList.get(i).getInstrument();
					if (tempInst != null) {
						if (tempInst.getId() == instrument.getId()) {
							hasInstrument = true;
							storageId = storeList.get(i).getId();
						}
					}
				}
				if (hasInstrument) {

					Storage storage = storageDAO.getStorageByID(storageId);
					if (storage.getAmount() >= docW.getAmount()) {
						doc.setInstrument(instrument);
					} else {
						errorText.append("<li>недостачно инструмента для видачи  в строке " + number + "</li>");
					}

				} else {
					errorText.append("<li>нет инструмента в ячеке видачи  в строке " + number + "</li>");
				}
			}
		}
		doc.setAmount(docW.getAmount());

		String errString = errorText.toString();
		if (errString == null) {
			errString = "";
		}
		return new ExDocTempStore(errString, doc, storageId);

	}

	public static String writeExDoc(ExDoc doc, StorageDAO storageDAO, ExDocDAO exDocDAO, long outStorageId) {
		try {
			exDocDAO.createExDoc(doc);
			long inStorageId = 0;
			Storage storage = storageDAO.getStorageByID(outStorageId);
			float amount = storage.getAmount() - doc.getAmount();
			storage.setAmount(amount);
			storageDAO.updateStorage(outStorageId, storage);

			List<Storage> storeList = storageDAO.getStorageByBox(doc.getInBox());
			Instrument instrument = doc.getInstrument();
			boolean hasInstrument = false;
			for (int i = 0; i < storeList.size(); i++) {
				Instrument tempInst = storeList.get(i).getInstrument();
				if (tempInst != null) {
					if (tempInst.getId() == instrument.getId()) {
						hasInstrument = true;
						inStorageId = storeList.get(i).getId();
					}
				}
			}
			if (hasInstrument) {
				storage = storageDAO.getStorageByID(inStorageId);
				amount = storage.getAmount() + doc.getAmount();
				storage.setAmount(amount);
				storageDAO.updateStorage(inStorageId, storage);
			} else {
				Storage newInStorage = new Storage(doc.getInBox(), instrument, doc.getAmount());
				storageDAO.createStorage(newInStorage);
			}
		} catch (Exception e) {
			return "<li>ошыбка бази данних </li>";
		}
		return "";

	}

	public static String writeExDocCatolog() {
		StringBuilder errorText = new StringBuilder("<ul>");
		ExDocCatalogDAO exDocCatalogDAO = (ExDocCatalogDAO) context.getBean("ExDocCatalogDAO");
		LocalDate date = LocalDate.now();
		int year = date.getYear();
		List<Integer> numberList = exDocCatalogDAO.getExDocCatalogByYearN(year);
		Collections.sort(numberList);
		int lastNumber = numberList.get(numberList.size() - 1);
		lastNumber++;
		String numberString = "" + year + "-" + lastNumber;
		System.out.println(numberString);
		ExDocCatalog exCat = new ExDocCatalog(year, lastNumber, numberString, date);
		if (!exDocCatalogDAO.createExDocCatalog(exCat)) {
			errorText.append("<li>ошыбка бази данних </li>");
		}

		errorText.append("</ul>");
		String errString = errorText.toString();
		exDocCatalogDAO.closeConection();
		if (errString.equals("<ul></ul>")) {
			return "документ успешно создан";
		} else {
			return errString;
		}

	}
}