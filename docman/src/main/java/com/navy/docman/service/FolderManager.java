package com.navy.docman.service;

import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.navy.docman.entity.Document;
import com.navy.docman.entity.Folder;
import com.navy.docman.hibernate.Page;
import com.navy.docman.hibernate.SimpleHibernateTemplate;

@Service("folderManager")
@Transactional(readOnly = true)
public class FolderManager {
	private SimpleHibernateTemplate<Folder, Long> folderDao;

	private SimpleHibernateTemplate<Document, Long> documentDao;

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		folderDao = new SimpleHibernateTemplate<Folder, Long>(sessionFactory,
				Folder.class);
		documentDao = new SimpleHibernateTemplate<Document, Long>(
				sessionFactory, Document.class);
	}

	@Transactional
	public void saveFolder(Folder folder) {
		folderDao.save(folder);
	}

	@Transactional
	public void saveDocument(Document doc) {
		documentDao.save(doc);
	}

	public List<Document> findDocByKeywords(String keyWords) {
		keyWords = "%" + keyWords + "%";
		Page<Document> page = new Page<Document>();
		page.setOrder(Page.DESC);
		page.setOrderBy("id");
		documentDao.findByCriteria(page, Restrictions.or(Restrictions.or(
				Restrictions.ilike("author", keyWords), Restrictions.ilike(
						"docName", keyWords)), Restrictions.ilike("notes",
				keyWords)));
		return page.getResult();
	}

	public List<Document> findDocByFolder(Folder folder) {
		Page<Document> page = new Page<Document>();
		page.setOrder(Page.DESC);
		page.setOrderBy("id");
		documentDao.findByCriteria(page, Restrictions.eq("folder", folder));
		return page.getResult();
	}

	public List<Folder> getAll() {
		return folderDao.findAll();
	}

	public Folder getById(Long id) {
		return folderDao.get(id);
	}

	public Folder getDocumentFolder(Document document) {
		return document.getFolder();
	}

	public List<Folder> getByParentIdOrderByName(Long id) {
		// System.out.println(id);
		Page<Folder> page = new Page<Folder>();
		page.setOrder(Page.ASC);
		page.setOrderBy("folderName");
		folderDao.findByCriteria(page, Restrictions.eq("parentId", id));
		return page.getResult();
	}

	@Transactional
	public void removeDocument(Document doc) {
		documentDao.delete(doc);
	}

	@Transactional
	public void removeFolder(Folder folder) {
		System.out.println("removing folder: " + folder.getFolderName());
		List<Folder> subFolders = folderDao.findByCriteria(Restrictions.eq(
				"parentId", folder.getId()));
		for (Folder subFolder : subFolders) {
			removeFolder(subFolder);
		}
		folderDao.delete(folder);
	}

	public boolean isValid(Long parentId, String name) {
		List<Folder> flds = folderDao.findByCriteria(Restrictions.eq(
				"parentId", parentId), Restrictions.eq("folderName", name));
		if (flds == null || flds.size() == 0) {
			return true;
		}
		return false;
	}
}
