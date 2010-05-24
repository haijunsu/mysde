package com.navy.docman.service;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.navy.docman.entity.Document;
import com.navy.docman.entity.Folder;

/**
 * 初始化Spring
 *
 * @author xling
 *
 */
public class ServiceLocator {

	private static ApplicationContext ctx = null;

	static {
		try {
			ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static FolderManager getFolderManager() {
		return (FolderManager) ctx.getBean("folderManager");
	}

	public static void main(String[] args) {
		// initTestData();
		// List<Folder> flds = getFolderManager().getyParentIdOrderByName(-1L);
		List<Folder> flds = getFolderManager().getAll();
		for (Folder folder : flds) {
			System.out.println(folder.getId() + ", " + folder.getParentId()
					+ ", " + folder.getFolderName());
		}

		testTree(-1L);
	}

	static void testTree(Long id) {
		List<Folder> flds = getFolderManager().getByParentIdOrderByName(id);
		for (Folder folder : flds) {
			System.out.println(folder.getFolderName());
			testTree(folder.getId());
		}
	}

	public static void initTestData() {
		try {
			FolderManager fmgr = ServiceLocator.getFolderManager();
			Folder folder = new Folder();
			folder.setFolderName("文档管理");
			folder.setParentId(-1L);
			fmgr.saveFolder(folder);
			for (int i = 0; i < 3; i++) {
				Folder fd = new Folder();
				fd.setFolderName("第一层 " + (4 - i));
				fd.setParentId(folder.getId());
				fmgr.saveFolder(fd);
				for (int j = 0; j < 3; j++) {
					Folder subfd = new Folder();
					subfd.setFolderName("第二层_" + i + " " + (9 - j));
					subfd.setParentId(fd.getId());
					fmgr.saveFolder(subfd);
				}
			}

		} catch (Exception e) {
			System.out.println(e);
			e.printStackTrace();
		}
	}

	static void test() {
		FolderManager fm = getFolderManager();
		Folder folder = new Folder();
		folder.setFolderName("test");
		folder.setParentId(1L);
		folder.setDocs(new HashSet<Document>());
		// fm.saveFolder(folder);
		// folder = fm.getById(1L);
		Document doc = new Document();
		doc.setDocName("docName");
		fm.saveDocument(doc);
		System.out.println("doc id:" + doc.getId());
		folder.getDocs().add(doc);
		Document doc2 = new Document();
		doc2.setDocName("docName2");
		fm.saveDocument(doc2);
		folder.getDocs().add(doc2);
		fm.saveFolder(folder);

		// Set<Document> saveeddocs = folder.getDocs();
		// for (Document document : saveeddocs) {
		// System.out.println(document.getId() + ", " + document.getDocName());
		// }
		List<Folder> folders = fm.getAll();
		for (Iterator<Folder> iterator = folders.iterator(); iterator.hasNext();) {
			Folder fd = iterator.next();
			System.out.println(fd.getId() + ", " + fd.getParentId() + ","
					+ fd.getFolderName());
			List<Document> saveeddocs = fm.findDocByFolder(fd);
			for (Document document : saveeddocs) {
				System.out.println(document.getId() + ", "
						+ document.getDocName());
			}

		}
		folder = fm.getById(1L);
		System.out.println(folder.getId() + ", " + folder.getFolderName());
		// Set<Document> docs = folder.getDocs();
		// for (Document document : docs) {
		// System.out.println(document.getDocName());
		// }

	}

}
