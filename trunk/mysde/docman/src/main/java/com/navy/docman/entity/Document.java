package com.navy.docman.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Entity
@Table(name = "tbl_docs")
public class Document extends IdEntity implements Serializable {
	/**
	 *
	 */
	@Transient
	private static final long serialVersionUID = -452215758630292091L;

	/**
	 * 文档名称
	 */
	public String docName;

	/**
	 * 文件类型 (分为电子和纸质，电子可以显示文件大小，电子文档显示扩展名)
	 */
	public String docType;

	/**
	 * 文件大小 (电子文档显示文件大小，纸质显示输入页数)
	 */
	public String fileSize;
	/**
	 * 分类编码
	 */
	public String categoryCode;

	/**
	 * 分类名称
	 */
	public String categoryName;

	/**
	 * 作者
	 */
	public String author;

	/**
	 * 审核
	 */
	public String auditor;

	/**
	 * 成文编号
	 */
	public String finalCode;

	/**
	 * 手工编号
	 */
	public String manualCode;

	/**
	 * 发文日期
	 */
	public Date dispatchDate;

	/**
	 * 发送人员
	 */
	public String sendPerson;

	/**
	 * 存档位置 (电子文档显示文件全名，纸质显示存储文件柜)
	 */
	public String location;

	/**
	 * 文档的注释
	 */
	public String notes;

	public Date createDate;

	public Date lastUpdate;

	public String loginId;

	/**
	 * Folder
	 */
	public Folder folder;

	@ManyToOne
	@JoinColumn(name = "FOLDER_ID")
	public Folder getFolder() {
		return folder;
	}

	public void setFolder(Folder folder) {
		this.folder = folder;
	}

	@Column(length = 1024)
	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getDocName() {
		return docName;
	}

	public void setDocName(String docName) {
		this.docName = docName;
	}

	public String getDocType() {
		return docType;
	}

	public void setDocType(String docType) {
		this.docType = docType;
	}

	public String getFileSize() {
		return fileSize;
	}

	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}

	public String getCategoryCode() {
		return categoryCode;
	}

	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getAuditor() {
		return auditor;
	}

	public void setAuditor(String auditor) {
		this.auditor = auditor;
	}

	public String getFinalCode() {
		return finalCode;
	}

	public void setFinalCode(String finalCode) {
		this.finalCode = finalCode;
	}

	public String getManualCode() {
		return manualCode;
	}

	public void setManualCode(String manualCode) {
		this.manualCode = manualCode;
	}

	@Temporal(TemporalType.TIMESTAMP)
	public Date getDispatchDate() {
		return dispatchDate;
	}

	public void setDispatchDate(Date dispatchDate) {
		this.dispatchDate = dispatchDate;
	}

	public String getSendPerson() {
		return sendPerson;
	}

	public void setSendPerson(String sendPerson) {
		this.sendPerson = sendPerson;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	@Temporal(TemporalType.TIMESTAMP)
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	public Date getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public String getLoginId() {
		return loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((docName == null) ? 0 : docName.hashCode());
		result = prime * result + ((docType == null) ? 0 : docType.hashCode());
		result = prime * result
				+ ((finalCode == null) ? 0 : finalCode.hashCode());
		result = prime * result
				+ ((manualCode == null) ? 0 : manualCode.hashCode());
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
		if (!(obj instanceof Document)) {
			return false;
		}
		Document other = (Document) obj;
		if (docName == null) {
			if (other.docName != null) {
				return false;
			}
		} else if (!docName.equals(other.docName)) {
			return false;
		}
		if (docType == null) {
			if (other.docType != null) {
				return false;
			}
		} else if (!docType.equals(other.docType)) {
			return false;
		}
		if (finalCode == null) {
			if (other.finalCode != null) {
				return false;
			}
		} else if (!finalCode.equals(other.finalCode)) {
			return false;
		}
		if (manualCode == null) {
			if (other.manualCode != null) {
				return false;
			}
		} else if (!manualCode.equals(other.manualCode)) {
			return false;
		}
		return true;
	}

}
