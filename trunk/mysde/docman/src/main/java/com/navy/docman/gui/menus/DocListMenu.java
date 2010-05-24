package com.navy.docman.gui.menus;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

import com.navy.docman.entity.Document;
import com.navy.docman.gui.GuiContainer;
import com.navy.docman.gui.MenuManager;
import com.navy.docman.gui.TreeManager;
import com.navy.docman.util.FileUtil;

public class DocListMenu implements Listener {

	enum DocListMenuActions {
		open, saveAs, delete, separator, openFolder
	};

	// String[] menuTexts = new String[] { "打开", "另存为...", "删除文件", "--",
	// "调整文件所在分类", "图片显视", "--", "个人设置" };

	private Map<DocListMenuActions, String> listMenus = new HashMap<DocListMenuActions, String>();

	private Menu parent = null;

	private Table table = null;

	private boolean supportDeleteFile = false;

	public DocListMenu(Menu parent, Table table, boolean supportDeleteFile) {
		this.parent = parent;
		this.table = table;
		this.supportDeleteFile = supportDeleteFile;
		listMenus.put(DocListMenuActions.open, "打开");
		listMenus.put(DocListMenuActions.saveAs, "另存为...");
		listMenus.put(DocListMenuActions.delete, "删除");
		listMenus.put(DocListMenuActions.separator, MenuManager.MENU_SEPARATOR);
		listMenus.put(DocListMenuActions.openFolder, "打开文件所在目录");
	}

	public void init() {
		DocListMenuActions[] actions = DocListMenuActions.values();
		String text = null;
		for (int i = 0; i < actions.length; i++) {
			if (actions[i].toString().startsWith(
					DocListMenuActions.separator.toString())) {
				actions[i] = DocListMenuActions.separator;
			}
			text = listMenus.get(actions[i]);
			MenuItem item = null;
			if (MenuManager.MENU_SEPARATOR.equals(text)) {
				item = new MenuItem(parent, SWT.SEPARATOR);
			} else {
				if (!supportDeleteFile
						&& DocListMenuActions.delete == actions[i]) {
					continue;
				}
				item = new MenuItem(parent, SWT.PUSH);
				item.setText(text);
				item.addListener(SWT.Selection, this);
			}
			item.setData(actions[i]);
		}
	}

	public void handleEvent(Event event) {
		MenuItem menu = (MenuItem) event.widget;
		DocListMenuActions action = (DocListMenuActions) menu.getData();
		process(action);
	}

	private void process(DocListMenuActions action) {
		TableItem[] items = table.getItems();
		if (items == null || items.length == 0) {
			return;
		}
		items = table.getSelection();
		if (items == null || items.length == 0) {
			return;
		}
		switch (action) {
		case open:
			openFile();

			break;
		case saveAs:
			saveAsFile();
			break;
		case delete:
			deleteFile();
			break;

		case openFolder:
			TableItem item = table.getSelection()[0];
			Document doc = (Document) item.getData();
			String parentPath = TreeManager.getParentPath(TreeManager.folderMap
					.get(doc.getFolder()), "");
			Program.launch(parentPath);
			break;

		default:
			System.out.println("Error: " + action);
			break;
		}
	}

	private void openFile() {
		TableItem item = table.getSelection()[0];
		Document doc = (Document) item.getData();
		String parentPath = TreeManager.getParentPath(TreeManager.folderMap
				.get(doc.getFolder()), "");
		String path = parentPath + doc.getLocation();
		GuiContainer.getInstance().openFile(path, doc.getDocType());
	}

	private void deleteFile() {
		TableItem item = table.getSelection()[0];
		Document doc = (Document) item.getData();
		if (GuiContainer.getInstance().confirm(
				"确认要删除文件\"" + doc.getDocName() + "\"吗？删除后不可恢复！！！")) {
			File srcFile = new File(TreeManager.getParentPath(
					TreeManager.folderMap.get(doc.getFolder()), "")
					+ doc.getLocation());
			TreeManager.removeDocument(doc);
			FileUtil.removeDir(srcFile);
			TreeManager.refreshTable(GuiContainer.getInstance().getDocTree()
					.getSelection()[0]);
		}

	}

	private void saveAsFile() {
		TableItem item = table.getSelection()[0];
		Document doc = (Document) item.getData();
		String dist = GuiContainer.getInstance().fileSaveDialog(
				doc.getLocation());
		if (StringUtils.isNotBlank(dist)) {
			File distFile = new File(dist);
			File srcFile = new File(TreeManager.getParentPath(
					TreeManager.folderMap.get(doc.getFolder()), "")
					+ doc.getLocation());
			if (distFile.isFile() && distFile.exists()) {
				if (GuiContainer.getInstance().confirm("目标文件已经存在，确认要覆盖吗？")) {
					FileUtil.copy(srcFile, distFile, true);
				}
			} else {
				FileUtil.copy(srcFile, distFile, true);
			}
		}

	}
}
