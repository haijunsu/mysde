package com.navy.docman.gui;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.CoolBar;
import org.eclipse.swt.widgets.CoolItem;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.TreeItem;

import com.navy.docman.entity.Document;
import com.navy.docman.util.FileUtil;

public class ToolBarManger implements Listener {

	enum ToolBarActions {
		add, separator, save, separator1, search, separator2, about, exit
	}

	private Map<ToolBarActions, String> listMenus = new HashMap<ToolBarActions, String>();

	private CoolBar parent = null;

	public ToolBarManger(CoolBar coolBar) {
		this.parent = coolBar;
		listMenus.put(ToolBarActions.separator, MenuManager.MENU_SEPARATOR);
		listMenus.put(ToolBarActions.add, "添加");
		listMenus.put(ToolBarActions.save, "保存");
		listMenus.put(ToolBarActions.search, "查找");
		listMenus.put(ToolBarActions.about, "关于");
		listMenus.put(ToolBarActions.exit, "退出");

	}

	public void init() {
		ToolBarActions[] actions = ToolBarActions.values();
		ToolBar toolBar = new ToolBar(parent, SWT.FLAT);
		String text = null;
		for (int i = 0; i < actions.length; i++) {
			if (actions[i].toString().startsWith(
					ToolBarActions.separator.toString())) {
				actions[i] = ToolBarActions.separator;
			}
			text = listMenus.get(actions[i]);
			ToolItem item = null;
			if (MenuManager.MENU_SEPARATOR.equals(text)) {
				item = createSeparateItem(toolBar);
			} else {
				item = createItem(toolBar, text);
				item.setText(text);
				item.addListener(SWT.Selection, this);
			}
			item.setData(actions[i]);
		}
		toolBar.pack();
		createCoolItem(parent, toolBar);
	}

	// public static void init(CoolBar coolBar) {
	// createEditBar(coolBar);
	// }

	private static CoolItem createCoolItem(CoolBar coolBar, ToolBar toolBar) {
		Point size = toolBar.getSize();
		CoolItem item = new CoolItem(coolBar, SWT.NONE);
		item.setControl(toolBar);
		Point preferred = item.computeSize(size.x, size.y);
		item.setPreferredSize(preferred);
		return item;
	}

	private static ToolItem createItem(ToolBar toolBar, String name) {
		final ToolItem item = new ToolItem(toolBar, SWT.PUSH);
		item.setText(name);
		// item.addSelectionListener(new SelectionListener() {
		// public void widgetSelected(SelectionEvent event) {
		// ToolItem source = (ToolItem) event.getSource();
		// // Text textArea =
		// // GuiContainer.getInstance().getDocView().getNotes();
		// // textArea
		// // .setText(textArea.getText() + "\r\n" + source.getText());
		// // String value = GuiContainer.getInstance().prompt(
		// // source.getText());
		// // if (value == null) {
		// // System.out.println("No input!!!");
		// // } else {
		// // System.out.println(value);
		// // }
		// // String fileName =
		// // GuiContainer.getInstance().fileOpenDialog();
		// // System.out.println(source.getText() + ": " + fileName);
		// Document doc = GuiContainer.getInstance().getDocView().apply();
		// if (doc != null) {
		// TreeManager.saveDocument(doc);
		// }
		//
		// }
		//
		// public void widgetDefaultSelected(SelectionEvent event) {
		// widgetSelected(event);
		// }
		// });
		return item;
	}

	private static ToolItem createSeparateItem(ToolBar toolBar) {
		final ToolItem item = new ToolItem(toolBar, SWT.PUSH);
		item.setText(" | ");
		item.setEnabled(false);
		return item;
	}

	public void handleEvent(Event event) {
		ToolItem item = (ToolItem) event.widget;
		ToolBarActions action = (ToolBarActions) item.getData();
		process(action);
	}

	private void process(ToolBarActions action) {
		TreeItem[] items = GuiContainer.getInstance().getDocTree()
				.getSelection();
		if (items != null && items.length > 0) {
			switch (action) {
			case add:
				String filename = GuiContainer.getInstance().fileOpenDialog();
				if (StringUtils.isNotBlank(filename)) {
					File src = new File(filename);
					File dist = new File(TreeManager.getCurrentPath()
							+ src.getName());
					if (dist.isDirectory()) {
						GuiContainer.getInstance().message(
								"添加失败！文件名与已经存在分类名冲突，请更改要导入的文件名后再重新添加！");
						return;
					}
					if (dist.isFile() && dist.exists()) {
						if (GuiContainer.getInstance().confirm(
								"目标文件已经存在，确认要覆盖吗？")) {
							FileUtil.copy(src, dist, true);
						}
					} else {
						FileUtil.copy(src, dist, true);
					}
					TreeManager.addDocument(GuiContainer.getInstance()
							.getDocTree().getSelection()[0], src);
					TreeManager.refreshTable(GuiContainer.getInstance()
							.getDocTree().getSelection()[0]);
				}
				break;
			case save:
				if (GuiContainer.getInstance().getDocView().getDocument() != null) {
					Document doc = GuiContainer.getInstance().getDocView()
							.apply();
					if (doc != null) {
						TreeManager.saveDocument(doc);
					}
				}
				break;
			case search:
				//GuiContainer.getInstance().message("正在努力实现中……");
				SearchManager searchMgr = new SearchManager();
				searchMgr.searchDialog();
				break;

			case about:
				GuiContainer.getInstance().message("玲玲专用文档管理工具 v0.1");
				break;

			case exit:
				System.exit(0);
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
