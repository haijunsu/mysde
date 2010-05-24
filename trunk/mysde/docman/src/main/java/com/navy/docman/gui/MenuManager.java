package com.navy.docman.gui;

import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Table;

import com.navy.docman.gui.menus.DocListMenu;
import com.navy.docman.gui.menus.DocTreeMenu;

public class MenuManager {

	public static final String MENU_SEPARATOR = "--";

	public static void initDocTreeMenu(Menu docTreeMenu) {

		DocTreeMenu menu = new DocTreeMenu(docTreeMenu);
		menu.init();
	}

	public static void initDocListMenu(Menu docListMenu, Table table,
			boolean supportDeleteFile) {
		DocListMenu listMenu = new DocListMenu(docListMenu, table,
				supportDeleteFile);
		listMenu.init();
		// String[] menuTexts = new String[] { "打开", "另存为...", "删除文件", "--",
		// "调整文件所在分类", "图片显视", "--", "个人设置" };
		// for (int i = 0; i < menuTexts.length; i++) {
		// if (menuTexts[i].equals("--")) {
		// new MenuItem(docListMenu, SWT.SEPARATOR);
		// continue;
		// }
		// MenuItem item = new MenuItem(docListMenu, SWT.RADIO);
		// item.setText(menuTexts[i]);
		// item.addSelectionListener(new SelectionAdapter() {
		// public void widgetSelected(SelectionEvent e) {
		// MenuItem item = (MenuItem) e.widget;
		// if (item.getSelection()) {
		//
		// System.out.println(item + " selected");
		// } else {
		// System.out.println(item + " unselected");
		// }
		// }
		// });
		// }

	}
}
