package com.navy.docman.gui;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.SystemUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.CoolBar;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Sash;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;

import com.navy.docman.entity.Document;
import com.navy.docman.entity.Folder;

public class GuiContainer {

	private static Boolean inited = Boolean.FALSE;

	private static GuiContainer container = new GuiContainer();

	private static final int SASH_LIMIT = 20;

	private static final int SASH_WIDTH = 3;

	public static Color tableRowColor = null;

	private String latestPath = SystemUtils.USER_HOME;

	private Shell shell;

	private CoolBar toolBar;

	private Tree docTree;

	private Composite bodyComp;

	private Composite detailComp;

	private DocumentView docView;

	private Table listTable;

	private Sash vSash;

	private Sash hSash;

	private Menu docTreeMenu;

	private Menu docListMenu;

	public Table getListTable() {
		return listTable;
	}

	private GuiContainer() {

	}

	private GuiContainer(Shell appShell) {
		shell = appShell;
		shell.setText("玲玲专用文档管理工具");
		tableRowColor = appShell.getDisplay().getSystemColor(
				SWT.COLOR_WIDGET_LIGHT_SHADOW);
		initToolBar();
		initDocTreeMenu();
		// initDocListMenu();
		initBodyComp();
	}

	private void initDocListMenu() {
		docListMenu = new Menu(shell, SWT.POP_UP);
		MenuManager.initDocListMenu(docListMenu, listTable, true);
	}

	private void initDocTreeMenu() {
		docTreeMenu = new Menu(shell, SWT.POP_UP);
		MenuManager.initDocTreeMenu(docTreeMenu);

	}

	public static void init(Shell shell) {
		synchronized (inited) {
			if (!inited.booleanValue()) {
				container = new GuiContainer(shell);
				inited = Boolean.TRUE;
			}
		}
	}

	public static GuiContainer getInstance() {
		return container;
	}

	public CoolBar getToolBar() {
		return toolBar;
	}

	public Composite getBodyComp() {
		return bodyComp;
	}

	public Tree getDocTree() {
		return docTree;
	}

	public DocumentView getDocView() {
		return this.docView;
	}

	private void initToolBar() {
		toolBar = new CoolBar(shell, SWT.NONE);
		ToolBarManger toolBarManger = new ToolBarManger(toolBar);
		toolBarManger.init();
		toolBar.addListener(SWT.Resize, new Listener() {
			public void handleEvent(Event event) {
				shell.layout();
			}
		});
		FormData coolData = new FormData();
		coolData.left = new FormAttachment(0);
		coolData.right = new FormAttachment(100);
		coolData.top = new FormAttachment(0);
		toolBar.setLayoutData(coolData);
	}

	private void initBodyComp() {
		bodyComp = new Composite(shell, SWT.BORDER | SWT.NONE);
		FormData bodyData = new FormData();
		bodyData.left = new FormAttachment(0);
		bodyData.right = new FormAttachment(100);
		bodyData.top = new FormAttachment(toolBar);
		bodyData.bottom = new FormAttachment(100);
		bodyComp.setLayoutData(bodyData);
		initDetail();
		initTable();
		initDocTree();
		vSash = new Sash(bodyComp, SWT.VERTICAL | SWT.NONE);
		hSash = new Sash(bodyComp, SWT.HORIZONTAL | SWT.NONE);
		/* Add the listeners */
		vSash.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				Rectangle rect = vSash.getParent().getClientArea();
				event.x = Math.min(Math.max(event.x, SASH_LIMIT), rect.width
						- SASH_LIMIT);
				if (event.detail != SWT.DRAG) {
					vSash
							.setBounds(event.x, event.y, event.width,
									event.height);
					layout();
				}
			}
		});
		hSash.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				Rectangle rect = vSash.getParent().getClientArea();
				event.y = Math.min(Math.max(event.y, SASH_LIMIT), rect.height
						- SASH_LIMIT);
				if (event.detail != SWT.DRAG) {
					hSash
							.setBounds(event.x, event.y, event.width,
									event.height);
					layout();
				}
			}
		});
		bodyComp.addControlListener(new ControlAdapter() {
			public void controlResized(ControlEvent event) {
				resized();
			}
		});
	}

	private void layout() {

		Rectangle clientArea = bodyComp.getClientArea();
		Rectangle vSashBounds = vSash.getBounds();
		Rectangle hSashBounds = hSash.getBounds();

		docTree.setBounds(0, 0, vSashBounds.x, clientArea.height);
		detailComp.setBounds(vSashBounds.x + vSashBounds.width, 0,
				clientArea.width - (vSashBounds.x + vSashBounds.width),
				hSashBounds.y);
		listTable.setBounds(vSashBounds.x + vSashBounds.width, hSashBounds.y
				+ hSashBounds.height, clientArea.width
				- (vSashBounds.x + vSashBounds.width), clientArea.width
				- (hSashBounds.y + hSashBounds.height));

		/**
		 * If the vertical sash has been moved then the horizontal sash is
		 * either too long or too short and its size must be adjusted.
		 */
		hSash.setBounds(vSashBounds.x + vSashBounds.width, hSashBounds.y,
				clientArea.width - (vSashBounds.x + vSashBounds.width),
				hSashBounds.height);
	}

	/**
	 * Handle the shell resized event.
	 */
	private void resized() {

		/* Get the client area for the shell */
		Rectangle clientArea = bodyComp.getClientArea();

		Rectangle docTreeArea = docTree.getClientArea();
		Rectangle detailCompArea = detailComp.getClientArea();
		Rectangle listTableArea = listTable.getClientArea();

		int treeWidth = docTreeArea.width == 0 ? 3 : docTreeArea.width;
		int detailWidth = detailCompArea.width == 0 ? 7 : detailCompArea.width;
		int detailHeight = detailCompArea.height == 0 ? 5
				: detailCompArea.height;
		int listTableHeight = listTableArea.height == 0 ? 5
				: listTableArea.height;

		/*
		 * Make Docment tree half the width and half the height of the tab
		 * leaving room for the sash. Place list 1 in the top left quadrant of
		 * the tab.
		 */
		if (100000 * treeWidth / (treeWidth + detailWidth) <= 20000) {
			treeWidth = 2;
			detailWidth = 8;
		}
		if (100000 * treeWidth / (treeWidth + detailWidth) >= 80000) {
			treeWidth = 8;
			detailWidth = 2;
		}
		Rectangle docTreeBounds = new Rectangle(0, 0, clientArea.width
				* treeWidth / (treeWidth + detailWidth), clientArea.height);
		docTree.setBounds(docTreeBounds);

		/*
		 * Make list 2 half the width and half the height of the tab leaving
		 * room for the sash. Place list 2 in the top right quadrant of the tab.
		 */
		if (100000 * detailHeight / (detailHeight + listTableHeight) <= 20000) {
			detailHeight = 3;
			listTableHeight = 7;
		}
		if (100000 * detailHeight / (detailHeight + listTableHeight) >= 80000) {
			detailHeight = 6;
			listTableHeight = 4;
		}
		detailHeight = docTreeBounds.height * detailHeight
				/ (detailHeight + listTableHeight);
		detailWidth = clientArea.width - (docTreeBounds.width + SASH_WIDTH);
		detailComp.setBounds(docTreeBounds.width + SASH_WIDTH, 0, detailWidth,
				detailHeight);

		listTable.setBounds(docTreeBounds.width + SASH_WIDTH, detailHeight
				+ SASH_WIDTH, detailWidth, docTreeBounds.height
				- (detailHeight + SASH_WIDTH));
		/* Position the sashes */
		vSash.setBounds(docTreeBounds.width, 0, SASH_WIDTH,
				docTreeBounds.height);
		hSash.setBounds(docTreeBounds.width + SASH_WIDTH, detailHeight,
				detailWidth, SASH_WIDTH);
	}

	private void initDocTree() {
		int style = SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL;
		docTree = new Tree(bodyComp, style);
		TreeManager.init(docTree);
		docTree.setMenu(docTreeMenu);
	}

	public Menu getDocTreeMenu() {
		return docTreeMenu;
	}

	private void initDetail() {
		// int style = SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL;
		detailComp = new Composite(bodyComp, SWT.BORDER | SWT.NONE);
		detailComp.setLayout(new FillLayout());
		docView = new DocumentView(detailComp, SWT.NONE);
	}

	private void initTable() {
		int style = SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL
				| SWT.FULL_SELECTION;
		listTable = new Table(bodyComp, style);
		initDocListMenu();
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

		listTable.setMenu(docListMenu);
		listTable.addSelectionListener(new SelectionListener() {

			public void widgetDefaultSelected(SelectionEvent event) {
				// TODO Auto-generated method stub

			}

			public void widgetSelected(SelectionEvent event) {
				Table table = (Table) event.getSource();
				TableItem item = table.getSelection()[0];
				docView.setDocument((Document) item.getData());
			}

		});
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
				Folder folder = doc.getFolder();
				String parentPath = TreeManager.getParentPath(
						TreeManager.folderMap.get(folder), "");
				openFile(parentPath + doc.getLocation(), "." + doc.getDocType());
			}

		});
	}

	public Shell getShell() {
		return shell;
	}

	public boolean confirm(String prompt) {
		Display display = shell.getDisplay();
		final boolean[] result = new boolean[1];
		final Shell dialog = new Shell(shell, SWT.DIALOG_TRIM
				| SWT.APPLICATION_MODAL);
		Label label = new Label(dialog, SWT.NONE);
		label.setText(prompt);
		final Button ok = new Button(dialog, SWT.PUSH);
		ok.setText("确定");
		Button cancel = new Button(dialog, SWT.PUSH);
		cancel.setText("取消");
		FormLayout form = new FormLayout();
		form.marginWidth = form.marginHeight = 8;
		dialog.setLayout(form);
		FormData okData = new FormData();
		okData.top = new FormAttachment(label, 8);
		okData.right = new FormAttachment(50);
		ok.setLayoutData(okData);
		FormData cancelData = new FormData();
		// cancelData.left = new FormAttachment(ok, 8);
		cancelData.top = new FormAttachment(ok, 0, SWT.TOP);
		cancelData.left = new FormAttachment(50);
		cancel.setLayoutData(cancelData);

		Listener listener = new Listener() {
			public void handleEvent(Event event) {
				result[0] = event.widget == ok;
				dialog.close();
			}
		};
		ok.addListener(SWT.Selection, listener);
		cancel.addListener(SWT.Selection, listener);
		dialog.pack();
		dialog.open();
		System.out.println("Prompt ...");
		while (!dialog.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		System.out.println("Result: " + result[0]);
		return result[0];
	}

	public void message(String prompt) {
		Display display = shell.getDisplay();
		final Shell dialog = new Shell(shell, SWT.DIALOG_TRIM
				| SWT.APPLICATION_MODAL);
		Label label = new Label(dialog, SWT.NONE);
		label.setText(prompt);
		final Button ok = new Button(dialog, SWT.PUSH);
		ok.setText("确定");
		FormLayout form = new FormLayout();
		form.marginWidth = form.marginHeight = 8;
		dialog.setLayout(form);
		FormData okData = new FormData();
		okData.top = new FormAttachment(label, 8);
		okData.left = new FormAttachment(label, 0, SWT.CENTER);

		ok.setLayoutData(okData);

		Listener listener = new Listener() {
			public void handleEvent(Event event) {
				dialog.close();
			}
		};
		ok.addListener(SWT.Selection, listener);
		dialog.pack();
		dialog.open();
		while (!dialog.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
	}

	/**
	 * 提示用户输入
	 *
	 * @param labelText
	 * @return 取消返回null, 确定返回非null值
	 */
	public String prompt(String labelText) {
		return prompt(labelText, null);
	}

	public String prompt(String labelText, String defaultValue) {
		Display display = shell.getDisplay();
		final boolean[] result = new boolean[1];
		final String[] rtnText = new String[1];
		rtnText[0] = "";
		final Shell dialog = new Shell(shell, SWT.DIALOG_TRIM
				| SWT.APPLICATION_MODAL);
		Label label = new Label(dialog, SWT.NONE);
		label.setText(labelText + ": ");
		Text text = new Text(dialog, SWT.BORDER);
		if (defaultValue != null) {
			text.setText(defaultValue);
		}
		final Button ok = new Button(dialog, SWT.PUSH);
		ok.setText("确定");
		Button cancel = new Button(dialog, SWT.PUSH);
		cancel.setText("取消");
		FormLayout form = new FormLayout();
		form.marginWidth = form.marginHeight = 8;
		dialog.setLayout(form);
		FormData okData = new FormData();
		okData.top = new FormAttachment(label, 8);
		okData.right = new FormAttachment(50);
		ok.setLayoutData(okData);
		FormData cancelData = new FormData();
		cancelData.top = new FormAttachment(ok, 0, SWT.TOP);
		cancelData.left = new FormAttachment(50);
		cancel.setLayoutData(cancelData);
		FormData textData = new FormData();
		textData.left = new FormAttachment(label, 8);
		textData.width = 200;
		text.setLayoutData(textData);

		Listener listenerTxt = new Listener() {

			public void handleEvent(Event event) {
				rtnText[0] = ((Text) event.widget).getText();

			}
		};
		Listener listener = new Listener() {
			public void handleEvent(Event event) {
				result[0] = event.widget == ok;
				// rtnText[0] = text.getText();
				dialog.close();
			}
		};
		text.addListener(SWT.KeyUp, listenerTxt);
		ok.addListener(SWT.Selection, listener);
		cancel.addListener(SWT.Selection, listener);
		dialog.pack();
		dialog.open();
		while (!dialog.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		if (result[0]) {
			return rtnText[0];
		}
		return null;
	}

	public String fileDialog(String[] filterNames, String[] filterExtensions,
			int style, String defaultFileName) {
		FileDialog dialog = new FileDialog(shell, style);
		// String[] filterNames = new String[] { "Image Files", "All Files (*)"
		// };
		// String[] filterExtensions = new String[] {
		// "*.gif;*.png;*.xpm;*.jpg;*.jpeg;*.tiff", "*" };
		dialog.setFilterNames(filterNames);
		dialog.setFilterExtensions(filterExtensions);
		dialog.setFilterPath(SystemUtils.USER_HOME);
		if (defaultFileName != null) {
			dialog.setFileName(defaultFileName);
		}
		String fileName = dialog.open();
		System.out.println("Save to: " + fileName);
		return fileName;

	}

	public String fileOpenDialog() {
		// FileDialog dialog = new FileDialog(shell, SWT.SAVE);
		String[] filterNames = new String[] { "All Files (*)" };
		String[] filterExtensions = new String[] { "*" };

		return fileDialog(filterNames, filterExtensions, SWT.OPEN, null);

	}

	public String fileSaveDialog(String defaultName) {
		// FileDialog dialog = new FileDialog(shell, SWT.SAVE);
		String[] filterNames = new String[] { "All Files (*)" };
		String[] filterExtensions = new String[] { "*" };

		return fileDialog(filterNames, filterExtensions, SWT.SAVE, defaultName);

	}

	public String directoryDialog() {
		DirectoryDialog dialog = new DirectoryDialog(shell);
		dialog.setFilterPath(latestPath);
		String selectPath = dialog.open();
		if (StringUtils.isNotBlank(selectPath)) {
			latestPath = selectPath;
		}
		return selectPath;
	}

	public String calenderDialog(String dateStr) {
		Display display = shell.getDisplay();
		final String[] results = new String[1];
		final Shell dialog = new Shell(shell, SWT.DIALOG_TRIM
				| SWT.APPLICATION_MODAL);
		FormLayout form = new FormLayout();
		form.marginWidth = form.marginHeight = 8;
		dialog.setLayout(form);
		final DateTime calendar = new DateTime(dialog, SWT.CALENDAR
				| SWT.BORDER);
		if (StringUtils.isNotBlank(dateStr)) {
			String[] dates = dateStr.split("-");
			if (dates.length == 3) {
				try {
					calendar.setYear(Integer.parseInt(dates[0]));
					calendar.setMonth(Integer.parseInt(dates[1]) - 1);
					calendar.setDay(Integer.parseInt(dates[2]));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		Button ok = new Button(dialog, SWT.PUSH);
		ok.setText("选择");
		Button cancel = new Button(dialog, SWT.PUSH);
		cancel.setText("取消");

		FormData okData = new FormData();
		okData.top = new FormAttachment(calendar, 8);
		okData.right = new FormAttachment(50);
		ok.setLayoutData(okData);
		ok.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				results[0] = calendar.getYear() + "-"
						+ ((calendar.getMonth() + 1) < 10 ? "0" : "")
						+ (calendar.getMonth() + 1) + "-"
						+ (calendar.getDay() < 10 ? "0" : "")
						+ calendar.getDay();
				dialog.close();
			}
		});

		FormData cancelData = new FormData();
		cancelData.top = new FormAttachment(calendar, 8);
		cancelData.left = new FormAttachment(50);
		cancel.setLayoutData(cancelData);
		cancel.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				dialog.close();
			}
		});

		dialog.setDefaultButton(ok);
		dialog.setText("请选择日期");
		dialog.pack();
		dialog.open();
		while (!dialog.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		return results[0];
	}

	public void openFile(String filename, String fileType) {
		Program program = Program.findProgram(fileType);
		if (program != null) {
			program.execute(filename);
		} else {
			Program.launch(filename);
		}

	}
}
