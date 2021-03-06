package savvats;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

import models.InDoc;
import models.Instrument;
import savvats.utils.ControllersCheckWDoc;


public class ControllresCheckWInDocTest {
	
	@Test
	void makeExDoc() {
		ControllersCheckWDoc.initDAO();
		ExDocWEB docW = new ExDocWEB("1", null, 1, 0, "2", 1);
		ExDocTempStore exDocTempStore = ControllersCheckWDoc.makeExDoc(docW, 0,DocType.INDOC);
		String message = exDocTempStore.getErrorString();
		assertTrue(message.equals(""), "no errors");
		assertTrue(exDocTempStore.getDoc().getInstrument()!=null,"must be instrument");

		docW = new ExDocWEB("50", null, 1, 0, "2", 1);
		exDocTempStore = ControllersCheckWDoc.makeExDoc(docW, 0,DocType.INDOC);
		message = exDocTempStore.getErrorString();
		assertTrue(message.equals("<li>неправильное место  видачи в стоке 1</li>"), "wrong outlocation");

		docW = new ExDocWEB("1", null, 50, 2, "2", 1);
		exDocTempStore = ControllersCheckWDoc.makeExDoc(docW, 0,DocType.INDOC);
		message = exDocTempStore.getErrorString();
		assertTrue(message.equals("<li>неправильная видающая ячейка в строке 1</li>"), "wrong inlocation");

		docW = new ExDocWEB("1", null, 1, 2, "50", 1);
		exDocTempStore = ControllersCheckWDoc.makeExDoc(docW, 0,DocType.INDOC);
		message = exDocTempStore.getErrorString();
		assertTrue(message.equals("<li>не правильний инструмент в строке 1 </li>"), "wrong inlocation");

	}

	@Test
	void createExDocUnwrap() {
		ControllersCheckWDoc.initDAO();
		ExDocWEBList docListWrap = new ExDocWEBList();
		List<ExDocWEB> docList = new ArrayList<ExDocWEB>();
		docList.add(new ExDocWEB("1", "1", 1, 2, "2", 1));
		docList.add(new ExDocWEB("50", "1", 1, 2, "2", 1));
		docList.add(new ExDocWEB("1", "1", 50, 1, "2", 1));
		docList.add(new ExDocWEB("1", "1", 1, 2, "1", 1));
		docListWrap.setDocList(docList);
		String message = ControllersCheckWDoc.createExDocUnwrap(docListWrap,DocType.INDOC);
		assertTrue(message.equals("<ul><li>неправильное место  видачи в стоке 2</li>"
				+ "<li>неправильная видающая ячейка в строке 3</li>"
				+ "<li>не правильний инструмент в строке 4 </li></ul>"), "six errors");

	}

	@Test
	void createExDocUnwrapAdd() {
		ControllersCheckWDoc.initDAO();
		ExDocWEBList docListWrap = new ExDocWEBList();
		List<ExDocWEB> docList = new ArrayList<ExDocWEB>();
		docList.add(new ExDocWEB("7", "1", 4, 2, "2", 5));

		docListWrap.setDocList(docList);
		String message = ControllersCheckWDoc.createExDocUnwrap(docListWrap,DocType.INDOC);
		assertFalse(message.equals("2020"), "six errors"); // test mast fali

	}
	@Test
	void cheakStorageUpdate() {
		ControllersCheckWDoc.initDAO();
		ExDocWEBList docListWrap = new ExDocWEBList();
		List<ExDocWEB> docList = new ArrayList<ExDocWEB>();
		docList.add(new ExDocWEB("7", "1", 4, 2, "2", 5));
		docList.add( new ExDocWEB("1", null, 1, 0, "4", 3));
		docListWrap.setDocList(docList);
		String message = ControllersCheckWDoc.createExDocUnwrap(docListWrap,DocType.INDOC);
		assertFalse(message.equals("2020"), "six errors"); // test mast fali

	}
	
	@Test
	void writeInDoc() {
		ControllersCheckWDoc.initDAO();
		ExDocWEB docW = new ExDocWEB("1", "1", 1, 1, "2", 7);
		ExDocTempStore exDocTempStore = ControllersCheckWDoc.makeExDoc(docW, 1,DocType.INDOC);
		InDoc doc = (InDoc) exDocTempStore.getDoc();
		String message = ControllersCheckWDoc.writeExDoc(doc, 3, exDocTempStore.getOutStorageId(),DocType.INDOC);
		System.out.println(message);
		assertTrue(message.equals("документ создан"), "no errors");
	}
	@Test
	void addInstrumentManyWork() {
		ControllersCheckWDoc.initDAO();
		ExDocWEBList docListWrap = new ExDocWEBList();
		List<ExDocWEB> docList = new ArrayList<ExDocWEB>();
		for(int i = 13;i<15;i++) {
		docList.add(new ExDocWEB("10", "1", i, i, String.valueOf(i), 5));
		}
		docListWrap.setDocList(docList);
		String message = ControllersCheckWDoc.createExDocUnwrap(docListWrap,DocType.INDOC);
		assertFalse(message.equals("2020"), "six errors"); // test mast fali
		}
		
}
