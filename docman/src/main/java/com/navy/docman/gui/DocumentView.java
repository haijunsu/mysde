package com.navy.docman.gui;

import java.io.InputStream;
import java.util.Calendar;

import org.apache.commons.lang.StringUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.navy.docman.entity.Document;

public class DocumentView extends Composite {

	private static final int LABEL_WIDTH = 60;

	private static final int TEXT_WIDTH = 120;

	private static Image defaultIcon = null;

	private static Image calenderIcon = null;

	private Document document = null;

	private Text docName = null;

	/**
	 * 文件类型 (分为电子和纸质，电子可以显示文件大小
	 */
	public Text docType;

	/**
	 * 文件大小 (电子文档显示文件大小，纸质显示输入页数)
	 */
	public Text fileSize;
	/**
	 * 分类编码
	 */
	public Text categoryCode;

	/**
	 * 分类名称
	 */
	public Text categoryName;

	/**
	 * 作者
	 */
	public Text author;

	/**
	 * 审核
	 */
	public Text auditor;

	/**
	 * 成文编号
	 */
	public Text finalCode;

	/**
	 * 手工编号
	 */
	public Text manualCode;

	/**
	 * 发文日期
	 */
	public Text dispatchDate;

	/**
	 * 发送人员
	 */
	public Text sendPerson;

	/**
	 * 存档位置 (电子文档显示文件全名，纸质显示存储文件柜)
	 */
	public Text docLocation;

	/**
	 * 文档的注释
	 */
	public Text notes;

	public Text createDate;

	public Text lastUpdate;

	public Text loginId;

	private Label progIcon;

	static {
		try {
			final Class<DocumentView> clazz = DocumentView.class;
			InputStream sourceStream = clazz
					.getResourceAsStream("icon_File.gif");
			ImageData source = new ImageData(sourceStream);
			ImageData mask = source.getTransparencyMask();
			defaultIcon = new Image(null, source, mask);
			sourceStream = clazz.getResourceAsStream("icon_calender.gif");
			source = new ImageData(sourceStream);
			mask = source.getTransparencyMask();
			calenderIcon = new Image(null, source, mask);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public DocumentView(Composite composite, int style) {
		super(composite, style);
		// int style = SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL;
		setLayout(new FormLayout());
		// mainTxt = new Text(detailComp, style);

		init();

	}

	private void init() {
		// docName
		Label docLabel = new Label(this, SWT.NONE);
		docLabel.setText("文档名称：");
		FormData docLabelFormData = new FormData();
		docLabelFormData.left = new FormAttachment(0);
		docLabelFormData.width = LABEL_WIDTH;
		docLabel.setLayoutData(docLabelFormData);
		docLabel.setAlignment(SWT.RIGHT);

		docName = new Text(this, SWT.BORDER);
		FormData docNameFormData = new FormData();
		docNameFormData.left = new FormAttachment(docLabel, 8);
		docNameFormData.width = TEXT_WIDTH;
		docName.setLayoutData(docNameFormData);

		// author
		Label authorLabel = new Label(this, SWT.NONE);
		authorLabel.setText("作    者：");
		FormData authorLabelFormData = new FormData();
		authorLabelFormData.left = new FormAttachment(docName, 8);
		// authorLabelFormData.top = new FormAttachment(docLabel, 8,
		// SWT.BOTTOM);
		authorLabelFormData.width = LABEL_WIDTH;
		authorLabel.setLayoutData(authorLabelFormData);
		authorLabel.setAlignment(SWT.RIGHT);

		author = new Text(this, SWT.BORDER);
		FormData authorFormData = new FormData();
		authorFormData.left = new FormAttachment(authorLabel, 8);
		// authorFormData.top = new FormAttachment(docLabel, 8, SWT.BOTTOM);
		authorFormData.width = TEXT_WIDTH;
		author.setLayoutData(authorFormData);

		// autitor
		Label auditorLabel = new Label(this, SWT.NONE);
		auditorLabel.setText("审    核：");
		FormData auditorLabelFormData = new FormData();
		auditorLabelFormData.left = new FormAttachment(author, 8);
		// auditorLabelFormData.top = new FormAttachment(cateCodeLabel, 8,
		// SWT.BOTTOM);
		auditorLabelFormData.width = LABEL_WIDTH;
		auditorLabel.setLayoutData(auditorLabelFormData);
		auditorLabel.setAlignment(SWT.RIGHT);

		auditor = new Text(this, SWT.BORDER);
		FormData auditorFormData = new FormData();
		auditorFormData.left = new FormAttachment(auditorLabel, 8);
		// auditorFormData.top = new FormAttachment(cateCodeLabel, 8,
		// SWT.BOTTOM);
		auditorFormData.width = TEXT_WIDTH;
		auditor.setLayoutData(auditorFormData);

		// categoryCode
		Label cateCodeLabel = new Label(this, SWT.NONE);
		cateCodeLabel.setText("分类编码：");
		FormData cateCodeLabelFormData = new FormData();
		cateCodeLabelFormData.left = new FormAttachment(0);
		cateCodeLabelFormData.top = new FormAttachment(docLabel, 8, SWT.BOTTOM);
		cateCodeLabelFormData.width = LABEL_WIDTH;
		cateCodeLabel.setLayoutData(cateCodeLabelFormData);
		cateCodeLabel.setAlignment(SWT.RIGHT);

		categoryCode = new Text(this, SWT.BORDER);
		FormData categoryCodeFormData = new FormData();
		categoryCodeFormData.left = new FormAttachment(cateCodeLabel, 8);
		categoryCodeFormData.top = new FormAttachment(docLabel, 8, SWT.BOTTOM);
		categoryCodeFormData.width = TEXT_WIDTH;
		categoryCode.setLayoutData(categoryCodeFormData);

		// categroyName
		Label codeNameLabel = new Label(this, SWT.NONE);
		codeNameLabel.setText("分类名称：");
		FormData codeNameLabelFormData = new FormData();
		codeNameLabelFormData.left = new FormAttachment(categoryCode, 8);
		codeNameLabelFormData.top = new FormAttachment(docLabel, 8, SWT.BOTTOM);
		codeNameLabelFormData.width = LABEL_WIDTH;
		codeNameLabel.setLayoutData(codeNameLabelFormData);
		codeNameLabel.setAlignment(SWT.RIGHT);

		categoryName = new Text(this, SWT.BORDER);
		FormData categoryNameFormData = new FormData();
		categoryNameFormData.left = new FormAttachment(codeNameLabel, 8);
		categoryNameFormData.top = new FormAttachment(docLabel, 8, SWT.BOTTOM);
		categoryNameFormData.width = TEXT_WIDTH;
		categoryName.setLayoutData(categoryNameFormData);

		// docType
		Label docTypeLabel = new Label(this, SWT.NONE);
		docTypeLabel.setText("文档类型：");
		FormData docTypeLabelFormData = new FormData();
		docTypeLabelFormData.left = new FormAttachment(categoryName, 8);
		docTypeLabelFormData.top = new FormAttachment(docLabel, 8, SWT.BOTTOM);
		docTypeLabelFormData.width = LABEL_WIDTH;
		docTypeLabel.setLayoutData(docTypeLabelFormData);
		docTypeLabel.setAlignment(SWT.RIGHT);

		docType = new Text(this, SWT.BORDER);
		FormData docTypeFormData = new FormData();
		docTypeFormData.left = new FormAttachment(docTypeLabel, 8);
		docTypeFormData.top = new FormAttachment(docLabel, 8, SWT.BOTTOM);
		docTypeFormData.width = TEXT_WIDTH;
		docType.setLayoutData(docTypeFormData);
		docType.setEditable(false);

		// finalCode
		Label finalCodeLabel = new Label(this, SWT.NONE);
		finalCodeLabel.setText("成文编号：");
		FormData finalCodeLabelFormData = new FormData();
		finalCodeLabelFormData.left = new FormAttachment(0);
		finalCodeLabelFormData.top = new FormAttachment(cateCodeLabel, 8,
				SWT.BOTTOM);
		finalCodeLabelFormData.width = LABEL_WIDTH;
		finalCodeLabel.setLayoutData(finalCodeLabelFormData);
		finalCodeLabel.setAlignment(SWT.RIGHT);

		finalCode = new Text(this, SWT.BORDER);
		FormData finalCodeFormData = new FormData();
		finalCodeFormData.left = new FormAttachment(finalCodeLabel, 8);
		finalCodeFormData.top = new FormAttachment(cateCodeLabel, 8, SWT.BOTTOM);
		finalCodeFormData.width = TEXT_WIDTH;
		finalCode.setLayoutData(finalCodeFormData);

		// manualCode
		Label manualCodeLabel = new Label(this, SWT.NONE);
		manualCodeLabel.setText("手工编号：");
		FormData manualCodeLabelFormData = new FormData();
		manualCodeLabelFormData.left = new FormAttachment(finalCode, 8);
		manualCodeLabelFormData.top = new FormAttachment(cateCodeLabel, 8,
				SWT.BOTTOM);
		manualCodeLabelFormData.width = LABEL_WIDTH;
		manualCodeLabel.setLayoutData(manualCodeLabelFormData);
		manualCodeLabel.setAlignment(SWT.RIGHT);

		manualCode = new Text(this, SWT.BORDER);
		FormData manualCodeFormData = new FormData();
		manualCodeFormData.left = new FormAttachment(manualCodeLabel, 8);
		manualCodeFormData.top = new FormAttachment(cateCodeLabel, 8,
				SWT.BOTTOM);
		manualCodeFormData.width = TEXT_WIDTH;
		manualCode.setLayoutData(manualCodeFormData);

		// fileSize
		Label fileSizeLabel = new Label(this, SWT.NONE);
		fileSizeLabel.setText("文件大小：");
		FormData fileSizeLabelFormData = new FormData();
		fileSizeLabelFormData.left = new FormAttachment(manualCode, 8);
		fileSizeLabelFormData.top = new FormAttachment(cateCodeLabel, 8,
				SWT.BOTTOM);
		fileSizeLabelFormData.width = LABEL_WIDTH;
		fileSizeLabel.setLayoutData(fileSizeLabelFormData);
		fileSizeLabel.setAlignment(SWT.RIGHT);

		fileSize = new Text(this, SWT.BORDER);
		FormData fileSizeFormData = new FormData();
		fileSizeFormData.left = new FormAttachment(fileSizeLabel, 8);
		fileSizeFormData.top = new FormAttachment(cateCodeLabel, 8, SWT.BOTTOM);
		fileSizeFormData.width = TEXT_WIDTH;
		fileSize.setLayoutData(fileSizeFormData);
		fileSize.setEditable(false);

		// dispatch Date
		Label dispatchDateLabel = new Label(this, SWT.NONE);
		dispatchDateLabel.setText("发文日期：");
		FormData dispatchDateLabelFormData = new FormData();
		dispatchDateLabelFormData.left = new FormAttachment(0);
		dispatchDateLabelFormData.top = new FormAttachment(finalCodeLabel, 8,
				SWT.BOTTOM);
		dispatchDateLabelFormData.width = LABEL_WIDTH;
		dispatchDateLabel.setLayoutData(dispatchDateLabelFormData);
		dispatchDateLabel.setAlignment(SWT.RIGHT);

		dispatchDate = new Text(this, SWT.BORDER);
		FormData dispatchDateFormData = new FormData();
		dispatchDateFormData.left = new FormAttachment(dispatchDateLabel, 8);
		dispatchDateFormData.top = new FormAttachment(finalCodeLabel, 8,
				SWT.BOTTOM);
		dispatchDateFormData.width = TEXT_WIDTH - 20;
		dispatchDate.setLayoutData(dispatchDateFormData);
		dispatchDate.setToolTipText("点击按钮弹出日期选择窗口");

		Label calenderLabel = new Label(this, SWT.PUSH);
		calenderLabel.setImage(calenderIcon);
		FormData calenderLabelData = new FormData();
		calenderLabelData.left = new FormAttachment(dispatchDate, 2);
		calenderLabelData.top = new FormAttachment(finalCodeLabel, 8,
				SWT.BOTTOM);
		calenderLabelData.right = new FormAttachment(finalCode, 0, SWT.RIGHT);
		calenderLabel.setLayoutData(calenderLabelData);
		calenderLabel.addMouseListener(new MouseListener() {

			public void mouseUp(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			public void mouseDown(MouseEvent arg0) {
				String selectDate = GuiContainer.getInstance().calenderDialog(
						getDispatchDate().getText());
				if (selectDate != null) {
					getDispatchDate().setText(selectDate);
				}

			}

			public void mouseDoubleClick(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}
		});

		// sendperson
		Label senderLabel = new Label(this, SWT.NONE);
		senderLabel.setText("发送人员：");
		FormData senderLabelFormData = new FormData();
		senderLabelFormData.left = new FormAttachment(calenderLabel, 8);
		senderLabelFormData.top = new FormAttachment(finalCodeLabel, 8,
				SWT.BOTTOM);
		senderLabelFormData.width = LABEL_WIDTH;
		senderLabel.setLayoutData(senderLabelFormData);
		senderLabel.setAlignment(SWT.RIGHT);

		sendPerson = new Text(this, SWT.BORDER);
		FormData sendPersonFormData = new FormData();
		sendPersonFormData.left = new FormAttachment(senderLabel, 8);
		sendPersonFormData.top = new FormAttachment(finalCodeLabel, 8,
				SWT.BOTTOM);
		sendPersonFormData.width = TEXT_WIDTH;
		sendPerson.setLayoutData(sendPersonFormData);

		// loginId
		Label loginIdLabel = new Label(this, SWT.NONE);
		loginIdLabel.setText("修 改 人：");
		FormData loginIdLabelFormData = new FormData();
		loginIdLabelFormData.left = new FormAttachment(sendPerson, 8);
		loginIdLabelFormData.top = new FormAttachment(finalCodeLabel, 8,
				SWT.BOTTOM);
		loginIdLabelFormData.width = LABEL_WIDTH;
		loginIdLabel.setLayoutData(loginIdLabelFormData);
		loginIdLabel.setAlignment(SWT.RIGHT);

		FormData loginIdFormData = new FormData();
		loginId = new Text(this, SWT.BORDER);
		loginIdFormData.left = new FormAttachment(loginIdLabel, 8);
		loginIdFormData.top = new FormAttachment(finalCodeLabel, 8, SWT.BOTTOM);
		loginIdFormData.width = TEXT_WIDTH;
		loginId.setLayoutData(loginIdFormData);
		loginId.setEditable(false);

		// create Date
		Label createLabel = new Label(this, SWT.NONE);
		createLabel.setText("存档日期：");
		FormData createLabelFormData = new FormData();
		createLabelFormData.left = new FormAttachment(0);
		createLabelFormData.top = new FormAttachment(dispatchDateLabel, 8,
				SWT.BOTTOM);
		createLabelFormData.width = LABEL_WIDTH;
		createLabel.setLayoutData(createLabelFormData);
		createLabel.setAlignment(SWT.RIGHT);

		createDate = new Text(this, SWT.BORDER);
		FormData createDateFormData = new FormData();
		createDateFormData.left = new FormAttachment(createLabel, 8);
		createDateFormData.top = new FormAttachment(dispatchDateLabel, 8,
				SWT.BOTTOM);
		createDateFormData.width = TEXT_WIDTH;
		createDate.setLayoutData(createDateFormData);
		createDate.setEditable(false);

		// location
		Label locationLabel = new Label(this, SWT.NONE);
		locationLabel.setText("存档位置：");
		FormData locationLabelFormData = new FormData();
		locationLabelFormData.left = new FormAttachment(createDate, 8);
		locationLabelFormData.top = new FormAttachment(dispatchDateLabel, 8,
				SWT.BOTTOM);
		locationLabelFormData.width = LABEL_WIDTH;
		locationLabel.setLayoutData(locationLabelFormData);
		locationLabel.setAlignment(SWT.RIGHT);

		docLocation = new Text(this, SWT.BORDER);
		FormData docLocationFormData = new FormData();
		docLocationFormData.left = new FormAttachment(locationLabel, 8);
		docLocationFormData.top = new FormAttachment(dispatchDateLabel, 8,
				SWT.BOTTOM);
		docLocationFormData.right = new FormAttachment(loginId, 0, SWT.RIGHT);
		docLocation.setLayoutData(docLocationFormData);
		docLocation.setEditable(false);
		// Label lastUpdateLabel = new Label(this, SWT.NONE);
		// lastUpdate = new Text(this, SWT.BORDER);

		notes = new Text(this, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		FormData notesFormData = new FormData();
		notesFormData.left = new FormAttachment(0);
		notesFormData.right = new FormAttachment(100);
		notesFormData.top = new FormAttachment(createLabel, 8, SWT.BOTTOM);
		notesFormData.bottom = new FormAttachment(100);
		notes.setLayoutData(notesFormData);

		// program icon
		progIcon = new Label(this, SWT.NONE);
		if (defaultIcon != null) {
			progIcon.setImage(defaultIcon);
		}
		FormData progIconFormData = new FormData();
		progIconFormData.left = new FormAttachment(fileSize, 8);
		progIconFormData.top = new FormAttachment(0);
		progIconFormData.right = new FormAttachment(100);
		progIconFormData.bottom = new FormAttachment(notes, 8, SWT.TOP);
		progIcon.setLayoutData(progIconFormData);
		progIcon.setAlignment(SWT.CENTER);
		progIcon.setToolTipText("双击此处打开文档");

		progIcon.addMouseListener(new MouseListener() {

			public void mouseUp(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			public void mouseDown(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			public void mouseDoubleClick(MouseEvent arg0) {
				GuiContainer.getInstance().openFile(docLocation.getText(),
						"." + docType);

			}
		});
	}

	public Document getDocument() {
		return document;
	}

	private boolean verifyDate(String dateStr, boolean canEmpty) {
		if (StringUtils.isBlank(dateStr)) {
			return canEmpty ? true : false;
		}
		String[] dates = dateStr.split("-");
		if (dates.length == 3) {
			for (int i = 0; i < dates.length; i++) {
				if (!StringUtils.isNumeric(dates[i])) {
					return false;
				}
			}
		} else {
			return false;
		}
		return true;
	}

	public Document apply() {
		// TODO time issue
		// document.setDispatchDate(dispatchDate.getText());
		String dateStr = dispatchDate.getText();
		if (verifyDate(dateStr, true)) {
			if (StringUtils.isBlank(dateStr)) {
				document.setDispatchDate(null);
			} else {
				String[] dates = dateStr.split("-");
				Calendar calender = Calendar.getInstance();
				calender.set(Integer.parseInt(dates[0]), Integer
						.parseInt(dates[1]) - 1, Integer.parseInt(dates[2]));
				document.setDispatchDate(calender.getTime());
			}
		} else {
			return null;
		}
		document.setDocName(docName.getText());
		document.setAuthor(author.getText());
		document.setAuditor(auditor.getText());
		document.setCategoryCode(categoryCode.getText());
		document.setCategoryName(categoryName.getText());
		document.setFinalCode(finalCode.getText());
		document.setManualCode(manualCode.getText());
		document.setSendPerson(sendPerson.getText());
		document.setNotes(notes.getText());
		return document;
	}

	public void setDocument(Document document) {
		this.document = document;
		this.docName.setText(document.getDocName() == null ? "" : document
				.getDocName());
		this.author.setText(document.getAuthor() == null ? "" : document
				.getAuthor());
		this.auditor.setText(document.getAuditor() == null ? "" : document
				.getAuditor());
		this.categoryCode.setText(document.getCategoryCode() == null ? ""
				: document.getCategoryCode());
		this.categoryName.setText(document.getCategoryName() == null ? ""
				: document.getCategoryName());
		this.docType.setText(document.getDocType() == null ? "" : document
				.getDocType());
		this.finalCode.setText(document.getFinalCode() == null ? "" : document
				.getFinalCode());
		this.manualCode.setText(document.getManualCode() == null ? ""
				: document.getManualCode());
		this.fileSize.setText(document.getFileSize() == null ? "" : document
				.getFileSize());
		this.sendPerson.setText(document.getSendPerson() == null ? ""
				: document.getSendPerson());
		this.dispatchDate.setText(TreeManager.getDateString(document
				.getDispatchDate()));
		this.loginId.setText(document.getLoginId() == null ? "" : document
				.getLoginId());
		this.docLocation.setText(document.getLocation() == null ? ""
				: TreeManager.getCurrentPath() + document.getLocation());
		this.docLocation.setToolTipText(this.docLocation.getText());
		this.createDate.setText(TreeManager.getDateString(document
				.getCreateDate()));
		this.notes.setText(document.getNotes() == null ? "" : document
				.getNotes());
		setDocImage();
	}

	public Text getDocName() {
		return docName;
	}

	public void setDocName(Text docName) {
		this.docName = docName;
	}

	public Text getDocType() {
		return docType;
	}

	public void setDocType(Text docType) {
		this.docType = docType;
	}

	public Text getFileSize() {
		return fileSize;
	}

	public void setFileSize(Text fileSize) {
		this.fileSize = fileSize;
	}

	public Text getCategoryCode() {
		return categoryCode;
	}

	public void setCategoryCode(Text categoryCode) {
		this.categoryCode = categoryCode;
	}

	public Text getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(Text categoryName) {
		this.categoryName = categoryName;
	}

	public Text getAuthor() {
		return author;
	}

	public void setAuthor(Text author) {
		this.author = author;
	}

	public Text getAuditor() {
		return auditor;
	}

	public void setAuditor(Text auditor) {
		this.auditor = auditor;
	}

	public Text getFinalCode() {
		return finalCode;
	}

	public void setFinalCode(Text finalCode) {
		this.finalCode = finalCode;
	}

	public Text getManualCode() {
		return manualCode;
	}

	public void setManualCode(Text manualCode) {
		this.manualCode = manualCode;
	}

	public Text getDispatchDate() {
		return dispatchDate;
	}

	public void setDispatchDate(Text dispatchDate) {
		this.dispatchDate = dispatchDate;
	}

	public Text getSendPerson() {
		return sendPerson;
	}

	public void setSendPerson(Text sendPerson) {
		this.sendPerson = sendPerson;
	}

	public Text getDocLocation() {
		return this.docLocation;
	}

	public void setDocLocation(Text location) {
		this.docLocation = location;
	}

	public Text getNotes() {
		return notes;
	}

	public void setNotes(Text notes) {
		this.notes = notes;
	}

	public Text getCreateDate() {
		return createDate;
	}

	public void setCreatDate(Text createDate) {
		this.createDate = createDate;
	}

	public Text getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(Text lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public Text getLoginId() {
		return loginId;
	}

	public void setLoginId(Text loginId) {
		this.loginId = loginId;
	}

	private void setDocImage() {
		Image image = null;
		Program program = Program.findProgram("." + document.getDocType());
		if (program != null) {
			ImageData data = program.getImageData();
			if (data != null) {
				image = new Image(GuiContainer.getInstance().getShell()
						.getDisplay(), data);

			}
		}
		progIcon.setImage(image == null ? defaultIcon : image);
	}
}
