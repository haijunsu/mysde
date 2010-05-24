package com.navy.docman.gui.menus;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.TreeItem;

import com.navy.docman.gui.GuiContainer;
import com.navy.docman.gui.MenuManager;
import com.navy.docman.gui.TreeManager;

public class DocTreeMenu implements Listener {

	enum TreeMenuActions {
		addFolder, rename, rmFolder, separator, importFolder
	};

	// String[] menuTexts = new String[] { "增加分类", "修改分类", "删除分类", "--",
	// "批量导入", "批量导出", "批量删除", "批量整理分类", "--", "展开", "收缩" };

	private Map<TreeMenuActions, String> treeMenus = new HashMap<TreeMenuActions, String>();

	private Menu parent = null;

	public DocTreeMenu(Menu parent) {
		this.parent = parent;
		treeMenus.put(TreeMenuActions.addFolder, "增加分类");
		treeMenus.put(TreeMenuActions.rename, "重命名");
		treeMenus.put(TreeMenuActions.rmFolder, "删除分类");
		treeMenus.put(TreeMenuActions.importFolder, "批量导入");
		treeMenus.put(TreeMenuActions.separator, MenuManager.MENU_SEPARATOR);
	}

	public void init() {
		TreeMenuActions[] actions = TreeMenuActions.values();
		String text = null;
		for (int i = 0; i < actions.length; i++) {
			text = treeMenus.get(actions[i]);
			MenuItem item = null;
			if (MenuManager.MENU_SEPARATOR.equals(text)) {
				item = new MenuItem(parent, SWT.SEPARATOR);
			} else {
				item = new MenuItem(parent, SWT.PUSH);
				item.setText(text);
				item.addListener(SWT.Selection, this);
			}
			item.setData(actions[i]);
		}
	}

	public void handleEvent(Event event) {
		MenuItem menu = (MenuItem) event.widget;
		TreeMenuActions action = (TreeMenuActions) menu.getData();
		process(action);
	}

	private void process(TreeMenuActions action) {
		TreeItem[] items = GuiContainer.getInstance().getDocTree()
				.getSelection();
		if (items != null && items.length > 0) {
			switch (action) {
			case addFolder:
				TreeManager.addTreeItem(items[0]);

				break;
			case rename:
				TreeManager.renameTreeItem(items[0]);
				break;
			case rmFolder:
				TreeManager.removeItem(items[0]);
				break;
			case importFolder:
				TreeManager.batchImport(items[0]);
				break;

			default:
				System.out.println("Error: " + action);
				break;
			}
		} else {
			System.out.println("No tree item selected");
		}
	}
}
