package com.navy.docman.entity;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "tbl_folders")
public class Folder extends IdEntity implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -6313473707762647195L;

	/**
	 * 父结点ID
	 */
	public Long parentId;

	/**
	 * 结点名称
	 */
	public String folderName;

	public Set<Document> docs;

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public String getFolderName() {
		return folderName;
	}

	public void setFolderName(String folderName) {
		this.folderName = folderName;
	}

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "FOLDER_ID")
	public Set<Document> getDocs() {
		return docs;
	}

	public void setDocs(Set<Document> docs) {
		this.docs = docs;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((folderName == null) ? 0 : folderName.hashCode());
		result = prime * result
				+ ((parentId == null) ? 0 : parentId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (!(obj instanceof Folder)) {
			return false;
		}
		Folder other = (Folder) obj;
		if (folderName == null) {
			if (other.folderName != null) {
				return false;
			}
		} else if (!folderName.equals(other.folderName)) {
			return false;
		}
		if (parentId == null) {
			if (other.parentId != null) {
				return false;
			}
		} else if (!parentId.equals(other.parentId)) {
			return false;
		}
		return true;
	}

}
