package com.navy.docman.gui;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import com.navy.docman.entity.Document;
import com.navy.docman.entity.Folder;
import com.navy.docman.service.FolderManager;
import com.navy.docman.service.ServiceLocator;

public class SearchManager {

	private static FolderManager fmgr = ServiceLocator.getFolderManager();

	private Text text = null;

	private Table listTable;

	public void searchDialog() {
		Shell shell = GuiContainer.getInstance().getShell();
		Display display = shell.getDisplay();
		final String[] rtnText = new String[1];
		final Shell dialog = new Shell(shell, SWT.DIALOG_TRIM
				| SWT.APPLICATION_MODAL);
		dialog.setText("查找文档");
		Label label = new Label(dialog, SWT.NONE);
		label.setText("请输入查询内容：");
		text = new Text(dialog, SWT.BORDER);
		final Button ok = new Button(dialog, SWT.PUSH);
		ok.setText("确定");
		Button cancel = new Button(dialog, SWT.PUSH);
		cancel.setText("关闭");
		FormLayout form = new FormLayout();
		form.marginWidth = form.marginHeight = 8;
		dialog.setLayout(form);
		FormData okData = new FormData();
		// okData.top = new FormAttachment(label, 0, SWT.TOP);
		okData.left = new FormAttachment(text, 8);
		ok.setLayoutData(okData);
		FormData cancelData = new FormData();
		// cancelData.top = new FormAttachment(ok, 0, SWT.TOP);
		cancelData.left = new FormAttachment(ok, 8);
		cancel.setLayoutData(cancelData);
		FormData textData = new FormData();
		textData.left = new FormAttachment(label, 8);
		textData.width = 200;
		text.setLayoutData(textData);
		int style = SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL
				| SWT.FULL_SELECTION;
		listTable = new Table(dialog, style);

		FormData tableData = new FormData();
		tableData.top = new FormAttachment(label, 24);
		tableData.left = new FormAttachment(0);
		tableData.right = new FormAttachment(100);
		tableData.bottom = new FormAttachment(100);
		tableData.height = 400;
		listTable.setLayoutData(tableData);
		Menu docListMenu = new Menu(dialog, SWT.POP_UP);
		MenuManager.initDocListMenu(docListMenu, listTable, false);
		listTable.setMenu(docListMenu);
		initTable();

		Listener listenerTxt = new Listener() {

			public void handleEvent(Event event) {
				rtnText[0] = ((Text) event.widget).getText();

			}
		};
		Listener cancelListener = new Listener() {
			public void handleEvent(Event event) {
				dialog.close();
			}
		};
		Listener okListener = new Listener() {
			public void handleEvent(Event event) {
				query(rtnText[0]);
			}
		};
		text.addListener(SWT.KeyUp, listenerTxt);
		ok.addListener(SWT.Selection, okListener);
		cancel.addListener(SWT.Selection, cancelListener);
		dialog.pack();
		dialog.open();
		text.setFocus();
		while (!dialog.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
	}

	private void query(String keywords) {
		if (StringUtils.isBlank(keywords)) {
			GuiContainer.getInstance().message("请输入查询内容！");
			text.setFocus();
		}
		List<Document> documents = fmgr.findDocByKeywords(keywords);
		TreeManager.refreshTable(listTable, documents);

	}

	private void initTable() {
		listTable.setLinesVisible(true);
		listTable.setHeaderVisible(true);

		String[] titles = { " ", "文档名称", "文档类型", "分类编码", "分类名称", "作者", "审核",
				"成文编号", "手工编号", "发文日期", "发送人员", "修改人", "存档日期", "文件大小" };
		for (int i = 0; i < titles.length; i++) {
			TableColumn column = new TableColumn(listTable, SWT.NONE);
			column.setText(titles[i]);
		}
		listTable.setVisible(false);
		for (int i = 0; i < titles.length; i++) {
			listTable.getColumn(i).pack();
		}
		listTable.setVisible(true);

		listTable.addListener(SWT.MouseDoubleClick, new Listener() {

			public void handleEvent(Event event) {
				TableItem[] items = listTable.getItems();
				if (items == null || items.length == 0) {
					return;
				}
				items = listTable.getSelection();
				if (items == null || items.length == 0) {
					return;
				}

				TableItem item = listTable.getSelection()[0];
				Document doc = (Document) item.getData();
				// Folder folder = fmgr.getDocumentFolder(doc);
				Folder folder = doc.getFolder();
				String parentPath = TreeManager.getParentPath(
						TreeManager.folderMap.get(folder), "");
				GuiContainer.getInstance().openFile(
						parentPath + doc.getLocation(), "." + doc.getDocType());
			}

		});

	}
}
