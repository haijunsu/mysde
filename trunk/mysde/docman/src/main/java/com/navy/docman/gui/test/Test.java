package com.navy.docman.gui.test;

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.time.DateFormatUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.custom.TreeEditor;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Sash;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;

import com.navy.docman.gui.GuiContainer;

public class Test {
	static String[][] files = {
			{ "ver.txt", "1 KB", "Text Document", "28/09/2005 9:57 AM",
					"admin", },
			{ "Thumbs.db", "76 KB", "Data Base file", "13/03/2006 3:56 PM",
					"john", },
			{ "daddy.bmp", "148 MB", "Bitmap", "27/10/2008 1:34 PM", "bill", },
			{ "io.sys", "48 KB", "File System", "16/12/2008 6:14 AM", "admin", },
			{ "Programs", "0 KB", "File Folder", "04/02/2009 12:18 PM", "anne", },
			{ "test.rnd", "55 MB", "RND File", "19/02/2009 5:49 PM", "john", },
			{ "arial.ttf", "94 KB", "True Type Font", "25/08/2008 1:25 PM",
					"john", }, };

	public static void main(String[] args) {
		try {
			testIdentifier();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	static void testIdentifier(){
		final Display display = new Display();
		final Shell shell = new Shell(display);
		shell.setLayout(new GridLayout());
		final Text text = new Text(shell, SWT.MULTI | SWT.BORDER | SWT.V_SCROLL);
		text.setLayoutData(new GridData(GridData.FILL_BOTH));
		final int[] nextId = new int[1];
		Button b = new Button(shell, SWT.PUSH);
		b.setText("invoke long running job");
		b.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				Runnable longJob = new Runnable() {
					boolean done = false;
					int id;
					public void run() {
						Thread thread = new Thread(new Runnable() {
							public void run() {
								id = nextId[0]++;
								display.syncExec(new Runnable() {
									public void run() {
										if (text.isDisposed()) return;
										text.append("\nStart long running task "+id);
									}
								});
								for (int i = 0; i < 100000; i++) {
									if (display.isDisposed()) return;
									System.out.println("do task that takes a long time in a separate thread "+id);
								}
								if (display.isDisposed()) return;
								display.syncExec(new Runnable() {
									public void run() {
										if (text.isDisposed()) return;
										text.append("\nCompleted long running task "+id);
									}
								});
								done = true;
								display.wake();
							}
						});
						thread.start();
						while (!done && !shell.isDisposed()) {
							if (!display.readAndDispatch())
								display.sleep();
						}
					}
				};
				BusyIndicator.showWhile(display, longJob);
			}
		});
		shell.setSize(250, 150);
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}


	static void testDateTime3() {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setLayout(new GridLayout());
		final Text text = new Text(shell, SWT.BORDER);

		text.setText(DateFormatUtils.format(new Date(), "yyyy-MM-dd"));
		final Calendar calendar = Calendar.getInstance();
		text.addListener(SWT.Verify, new Listener() {
			boolean ignore;

			public void handleEvent(Event e) {
				if (ignore)
					return;
				e.doit = false;
				StringBuffer buffer = new StringBuffer(e.text);
				char[] chars = new char[buffer.length()];
				buffer.getChars(0, chars.length, chars, 0);
				if (e.character == '\b') {
					for (int i = e.start; i < e.end; i++) {
						switch (i) {
						case 0: /* [Y]YYY */
						case 1: /* Y[Y]YY */
						case 2: /* YY[Y]Y */
						case 3: /* YYY[Y] */{
							buffer.append('Y');
							break;
						}
						case 5: /* [M]M */
						case 6: /* M[M] */{
							buffer.append('M');
							break;
						}
						case 8: /* [D]D */
						case 9: /* D[D] */{
							buffer.append('D');
							break;
						}
						case 4: /* YYYY[/]MM */
						case 7: /* MM[/]DD */{
							buffer.append('/');
							break;
						}
						default:
							return;
						}
					}
					text.setSelection(e.start, e.start + buffer.length());
					ignore = true;
					text.insert(buffer.toString());
					ignore = false;
					text.setSelection(e.start, e.start);
					return;
				}

				int start = e.start;
				if (start > 9)
					return;
				int index = 0;
				for (int i = 0; i < chars.length; i++) {
					if (start + index == 4 || start + index == 7) {
						if (chars[i] == '-') {
							index++;
							continue;
						}
						buffer.insert(index++, '-');
					}
					if (chars[i] < '0' || '9' < chars[i])
						return;
					if (start + index == 5 && '1' < chars[i])
						return; /* [M]M */
					if (start + index == 8 && '3' < chars[i])
						return; /* [D]D */
					index++;
				}
				String newText = buffer.toString();
				int length = newText.length();
				StringBuffer date = new StringBuffer(text.getText());
				date.replace(e.start, e.start + length, newText);
				calendar.set(Calendar.YEAR, 1901);
				calendar.set(Calendar.MONTH, Calendar.JANUARY);
				calendar.set(Calendar.DATE, 1);
				String yyyy = date.substring(0, 4);
				if (yyyy.indexOf('Y') == -1) {
					int year = Integer.parseInt(yyyy);
					calendar.set(Calendar.YEAR, year);
				}
				String mm = date.substring(5, 7);
				if (mm.indexOf('M') == -1) {
					int month = Integer.parseInt(mm) - 1;
					int maxMonth = calendar.getActualMaximum(Calendar.MONTH);
					if (0 > month || month > maxMonth)
						return;
					calendar.set(Calendar.MONTH, month);
				}
				String dd = date.substring(8, 10);
				if (dd.indexOf('D') == -1) {
					int day = Integer.parseInt(dd);
					int maxDay = calendar.getActualMaximum(Calendar.DATE);
					if (1 > day || day > maxDay)
						return;
					calendar.set(Calendar.DATE, day);
				} else {
					if (calendar.get(Calendar.MONTH) == Calendar.FEBRUARY) {
						char firstChar = date.charAt(8);
						if (firstChar != 'D' && '2' < firstChar)
							return;
					}
				}
				text.setSelection(e.start, e.start + length);
				ignore = true;
				text.insert(newText);
				ignore = false;
			}
		});
		shell.pack();
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();

	}

	static void testDateTime2() {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setLayout(new RowLayout());

		DateTime calendar = new DateTime(shell, SWT.CALENDAR);
		calendar.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				System.out.println("calendar date changed");
			}
		});

		DateTime time = new DateTime(shell, SWT.TIME);
		time.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				System.out.println("time changed");
			}
		});

		shell.pack();
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();

	}

	static void testDateTime() {
		Display display = new Display();
		final Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());

		Button open = new Button(shell, SWT.PUSH);
		open.setText("Open Dialog");
		open.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				final Shell dialog = new Shell(shell, SWT.DIALOG_TRIM);
				dialog.setLayout(new GridLayout(3, false));

				final DateTime calendar = new DateTime(dialog, SWT.CALENDAR
						| SWT.BORDER);
				final DateTime date = new DateTime(dialog, SWT.DATE | SWT.SHORT);
				final DateTime time = new DateTime(dialog, SWT.TIME | SWT.SHORT);

				new Label(dialog, SWT.NONE);
				new Label(dialog, SWT.NONE);
				Button ok = new Button(dialog, SWT.PUSH);
				ok.setText("OK");
				ok.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false,
						false));
				ok.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(SelectionEvent e) {
						System.out
								.println("Calendar date selected (MM/DD/YYYY) = "
										+ (calendar.getMonth() + 1)
										+ "/"
										+ calendar.getDay()
										+ "/"
										+ calendar.getYear());
						System.out.println("Date selected (MM/YYYY) = "
								+ (date.getMonth() + 1) + "/" + date.getYear());
						System.out.println("Time selected (HH:MM) = "
								+ time.getHours() + ":"
								+ (time.getMinutes() < 10 ? "0" : "")
								+ time.getMinutes());
						dialog.close();
					}
				});
				dialog.setDefaultButton(ok);
				dialog.pack();
				dialog.open();
			}
		});
		shell.pack();
		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();

	}

	static void testDialog() {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.pack();
		shell.open();
		final boolean[] result = new boolean[1];
		final Shell dialog = new Shell(shell, SWT.DIALOG_TRIM
				| SWT.APPLICATION_MODAL);
		dialog.setLayout(new RowLayout());
		final Button ok = new Button(dialog, SWT.PUSH);
		ok.setText("OK");
		Button cancel = new Button(dialog, SWT.PUSH);
		cancel.setText("Cancel");
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
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}

	static void createMenuItem(Menu parent, final TreeColumn column) {
		final MenuItem itemName = new MenuItem(parent, SWT.CHECK);
		itemName.setText(column.getText());
		itemName.setSelection(column.getResizable());
		itemName.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				if (itemName.getSelection()) {
					column.setWidth(150);
					column.setResizable(true);
				} else {
					column.setWidth(0);
					column.setResizable(false);
				}
			}
		});
	}

	static void testTreeMenu() {
		final Display display = new Display();
		Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());

		final Tree tree = new Tree(shell, SWT.V_SCROLL | SWT.H_SCROLL
				| SWT.BORDER);
		tree.setHeaderVisible(true);
		final Menu headerMenu = new Menu(shell, SWT.POP_UP);
		final TreeColumn columnName = new TreeColumn(tree, SWT.NONE);
		columnName.setText("Name");
		columnName.setWidth(150);
		createMenuItem(headerMenu, columnName);
		final TreeColumn columnSize = new TreeColumn(tree, SWT.NONE);
		columnSize.setText("Size");
		columnSize.setWidth(150);
		createMenuItem(headerMenu, columnSize);
		final TreeColumn columnType = new TreeColumn(tree, SWT.NONE);
		columnType.setText("Type");
		columnType.setWidth(150);
		createMenuItem(headerMenu, columnType);
		final TreeColumn columnDate = new TreeColumn(tree, SWT.NONE);
		columnDate.setText("Date");
		columnDate.setWidth(150);
		createMenuItem(headerMenu, columnDate);
		final TreeColumn columnOwner = new TreeColumn(tree, SWT.NONE);
		columnOwner.setText("Owner");
		columnOwner.setWidth(0);
		columnOwner.setResizable(false);
		createMenuItem(headerMenu, columnOwner);

		for (int i = 0; i < files.length; i++) {
			TreeItem item = new TreeItem(tree, SWT.NONE);
			item.setText(files[i]);
			TreeItem subItem = new TreeItem(item, SWT.NONE);
			subItem.setText("node");
		}

		final Menu treeMenu = new Menu(shell, SWT.POP_UP);
		MenuItem item = new MenuItem(treeMenu, SWT.PUSH);
		item.setText("Open");
		item = new MenuItem(treeMenu, SWT.PUSH);
		item.setText("Open With");
		new MenuItem(treeMenu, SWT.SEPARATOR);
		item = new MenuItem(treeMenu, SWT.PUSH);
		item.setText("Cut");
		item = new MenuItem(treeMenu, SWT.PUSH);
		item.setText("Copy");
		item = new MenuItem(treeMenu, SWT.PUSH);
		item.setText("Paste");
		new MenuItem(treeMenu, SWT.SEPARATOR);
		item = new MenuItem(treeMenu, SWT.PUSH);
		item.setText("Delete");

		tree.addListener(SWT.MenuDetect, new Listener() {
			public void handleEvent(Event event) {
				Point pt = display.map(null, tree, new Point(event.x, event.y));
				Rectangle clientArea = tree.getClientArea();
				boolean header = clientArea.y <= pt.y
						&& pt.y < (clientArea.y + tree.getHeaderHeight());
				tree.setMenu(header ? headerMenu : treeMenu);
			}
		});

		/*
		 * IMPORTANT: Dispose the menus (only the current menu, set with
		 * setMenu(), will be automatically disposed)
		 */
		tree.addListener(SWT.Dispose, new Listener() {
			public void handleEvent(Event event) {
				headerMenu.dispose();
				treeMenu.dispose();
			}
		});

		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();

	}

	static void testSach() {

		Display display = new Display();
		final Shell shell = new Shell(display);
		Button button1 = new Button(shell, SWT.PUSH);
		button1.setText("Button 1");
		final Sash sash = new Sash(shell, SWT.VERTICAL);
		Button button2 = new Button(shell, SWT.PUSH);
		button2.setText("Button 2");

		final FormLayout form = new FormLayout();
		shell.setLayout(form);

		FormData button1Data = new FormData();
		button1Data.left = new FormAttachment(0, 0);
		button1Data.right = new FormAttachment(sash, 0);
		button1Data.top = new FormAttachment(0, 0);
		button1Data.bottom = new FormAttachment(100, 0);
		button1.setLayoutData(button1Data);

		final int limit = 20, percent = 50;
		final FormData sashData = new FormData();
		sashData.left = new FormAttachment(percent, 0);
		sashData.top = new FormAttachment(0, 0);
		sashData.bottom = new FormAttachment(100, 0);
		sash.setLayoutData(sashData);
		sash.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				Rectangle sashRect = sash.getBounds();
				Rectangle shellRect = shell.getClientArea();
				int right = shellRect.width - sashRect.width - limit;
				e.x = Math.max(Math.min(e.x, right), limit);
				if (e.x != sashRect.x) {
					sashData.left = new FormAttachment(0, e.x);
					shell.layout();
				}
			}
		});

		FormData button2Data = new FormData();
		button2Data.left = new FormAttachment(sash, 0);
		button2Data.right = new FormAttachment(100, 0);
		button2Data.top = new FormAttachment(0, 0);
		button2Data.bottom = new FormAttachment(100, 0);
		button2.setLayoutData(button2Data);

		shell.pack();
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}

	static void formLayoutTest() {
		Display display = new Display();
		final Shell shell = new Shell(display);
		shell.setLayout(new FormLayout());

		final Button button = new Button(shell, SWT.PUSH);
		button.setText(">>");
		FormData data = new FormData();
		data.top = new FormAttachment(0, 4);
		data.right = new FormAttachment(100, -4);
		button.setLayoutData(data);

		final Composite composite = new Composite(shell, SWT.BORDER);
		final FormData down = new FormData();
		down.top = new FormAttachment(button, 4, SWT.BOTTOM);
		down.bottom = new FormAttachment(100, -4);
		down.left = new FormAttachment(0, 4);
		down.right = new FormAttachment(100, -4);
		final FormData up = new FormData();
		up.top = new FormAttachment(0);
		up.bottom = new FormAttachment(0);
		up.left = new FormAttachment(0);
		up.right = new FormAttachment(0);
		composite.setLayoutData(up);

		button.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (composite.getLayoutData() == up) {
					composite.setLayoutData(down);
					button.setText("<<");
				} else {
					composite.setLayoutData(up);
					button.setText(">>");
				}
				shell.pack();
			}
		});

		shell.pack();
		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}

	static void tabFolderTest() {
		Display display = new Display();
		final Shell shell = new Shell(display);
		final TabFolder tabFolder = new TabFolder(shell, SWT.BORDER);
		for (int i = 0; i < 6; i++) {
			TabItem item = new TabItem(tabFolder, SWT.NONE);
			item.setText("TabItem " + i);
			Button button = new Button(tabFolder, SWT.PUSH);
			button.setText("Page " + i);
			item.setControl(button);
		}
		tabFolder.pack();
		shell.pack();
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();

	}

	static void dragTree2() {
		Display display = new Display();
		final Shell shell = new Shell(display);
		GuiContainer.init(shell);
		FormLayout layout = new FormLayout();
		shell.setLayout(layout);
		// coolbar
		FormData coolData = new FormData();
		coolData.left = new FormAttachment(0);
		coolData.right = new FormAttachment(100);
		coolData.top = new FormAttachment(0);
		GuiContainer.getInstance().getDocTree().setLayoutData(coolData);
		// shell.setLayout(new FillLayout());
		shell.setSize(400, 400);
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}

	static void dragTree() {

		final Display display = new Display();
		final Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());
		final Tree tree = new Tree(shell, SWT.BORDER);
		for (int i = 0; i < 3; i++) {
			TreeItem item = new TreeItem(tree, SWT.NONE);
			item.setText("item " + i);
			for (int j = 0; j < 3; j++) {
				TreeItem subItem = new TreeItem(item, SWT.NONE);
				subItem.setText("item " + i + " " + j);
				for (int k = 0; k < 3; k++) {
					TreeItem subsubItem = new TreeItem(subItem, SWT.NONE);
					subsubItem.setText("item " + i + " " + j + " " + k);
				}
			}
		}

		Transfer[] types = new Transfer[] { TextTransfer.getInstance() };
		int operations = DND.DROP_MOVE | DND.DROP_COPY | DND.DROP_LINK;

		final DragSource source = new DragSource(tree, operations);
		source.setTransfer(types);
		final TreeItem[] dragSourceItem = new TreeItem[1];
		source.addDragListener(new DragSourceListener() {
			public void dragStart(DragSourceEvent event) {
				TreeItem[] selection = tree.getSelection();
				if (selection.length > 0 && selection[0].getItemCount() == 0) {
					event.doit = true;
					dragSourceItem[0] = selection[0];
				} else {
					event.doit = false;
				}
			};

			public void dragSetData(DragSourceEvent event) {
				event.data = dragSourceItem[0].getText();
			}

			public void dragFinished(DragSourceEvent event) {
				if (event.detail == DND.DROP_MOVE)
					dragSourceItem[0].dispose();
				dragSourceItem[0] = null;
			}
		});

		DropTarget target = new DropTarget(tree, operations);
		target.setTransfer(types);
		target.addDropListener(new DropTargetAdapter() {
			public void dragOver(DropTargetEvent event) {
				event.feedback = DND.FEEDBACK_EXPAND | DND.FEEDBACK_SCROLL;
				if (event.item != null) {
					TreeItem item = (TreeItem) event.item;
					Point pt = display.map(null, tree, event.x, event.y);
					Rectangle bounds = item.getBounds();
					if (pt.y < bounds.y + bounds.height / 3) {
						event.feedback |= DND.FEEDBACK_INSERT_BEFORE;
					} else if (pt.y > bounds.y + 2 * bounds.height / 3) {
						event.feedback |= DND.FEEDBACK_INSERT_AFTER;
					} else {
						event.feedback |= DND.FEEDBACK_SELECT;
					}
				}
			}

			public void drop(DropTargetEvent event) {
				if (event.data == null) {
					event.detail = DND.DROP_NONE;
					return;
				}
				String text = (String) event.data;
				if (event.item == null) {
					TreeItem item = new TreeItem(tree, SWT.NONE);
					item.setText(text);
				} else {
					TreeItem item = (TreeItem) event.item;
					Point pt = display.map(null, tree, event.x, event.y);
					Rectangle bounds = item.getBounds();
					TreeItem parent = item.getParentItem();
					if (parent != null) {
						TreeItem[] items = parent.getItems();
						int index = 0;
						for (int i = 0; i < items.length; i++) {
							if (items[i] == item) {
								index = i;
								break;
							}
						}
						if (pt.y < bounds.y + bounds.height / 3) {
							TreeItem newItem = new TreeItem(parent, SWT.NONE,
									index);
							newItem.setText(text);
						} else if (pt.y > bounds.y + 2 * bounds.height / 3) {
							TreeItem newItem = new TreeItem(parent, SWT.NONE,
									index + 1);
							newItem.setText(text);
						} else {
							TreeItem newItem = new TreeItem(item, SWT.NONE);
							newItem.setText(text);
						}

					} else {
						TreeItem[] items = tree.getItems();
						int index = 0;
						for (int i = 0; i < items.length; i++) {
							if (items[i] == item) {
								index = i;
								break;
							}
						}
						if (pt.y < bounds.y + bounds.height / 3) {
							TreeItem newItem = new TreeItem(tree, SWT.NONE,
									index);
							newItem.setText(text);
						} else if (pt.y > bounds.y + 2 * bounds.height / 3) {
							TreeItem newItem = new TreeItem(tree, SWT.NONE,
									index + 1);
							newItem.setText(text);
						} else {
							TreeItem newItem = new TreeItem(item, SWT.NONE);
							newItem.setText(text);
						}
					}

				}
			}
		});

		shell.setSize(400, 400);
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}

	static void dialogTest() {
		Display display = new Display();
		final Shell shell = new Shell(display);
		shell.setText("Shell");
		FillLayout fillLayout = new FillLayout();
		fillLayout.marginWidth = 10;
		fillLayout.marginHeight = 10;
		shell.setLayout(fillLayout);

		Button open = new Button(shell, SWT.PUSH);
		open.setText("Prompt for a String");
		open.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				final Shell dialog = new Shell(shell, SWT.DIALOG_TRIM
						| SWT.APPLICATION_MODAL);
				dialog.setText("Dialog Shell");
				FormLayout formLayout = new FormLayout();
				formLayout.marginWidth = 10;
				formLayout.marginHeight = 10;
				formLayout.spacing = 10;
				dialog.setLayout(formLayout);

				Label label = new Label(dialog, SWT.NONE);
				label.setText("Type a String:");
				FormData data = new FormData();
				label.setLayoutData(data);

				Button cancel = new Button(dialog, SWT.PUSH);
				cancel.setText("Cancel");
				data = new FormData();
				data.width = 60;
				data.right = new FormAttachment(100, 0);
				data.bottom = new FormAttachment(100, 0);
				cancel.setLayoutData(data);
				cancel.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(SelectionEvent e) {
						System.out.println("User cancelled dialog");
						dialog.close();
					}
				});

				final Text text = new Text(dialog, SWT.BORDER);
				data = new FormData();
				data.width = 200;
				data.left = new FormAttachment(label, 0, SWT.DEFAULT);
				data.right = new FormAttachment(100, 0);
				data.top = new FormAttachment(label, 0, SWT.CENTER);
				data.bottom = new FormAttachment(cancel, 0, SWT.DEFAULT);
				text.setLayoutData(data);

				Button ok = new Button(dialog, SWT.PUSH);
				ok.setText("OK");
				data = new FormData();
				data.width = 60;
				data.right = new FormAttachment(cancel, 0, SWT.DEFAULT);
				data.bottom = new FormAttachment(100, 0);
				ok.setLayoutData(data);
				ok.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(SelectionEvent e) {
						System.out.println("User typed: " + text.getText());
						dialog.close();
					}
				});

				dialog.setDefaultButton(ok);
				dialog.pack();
				dialog.open();
			}
		});
		shell.pack();
		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}

	private static void testTreeEditor() {
		final Display display = new Display();
		final Color black = display.getSystemColor(SWT.COLOR_BLACK);
		Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());
		final Tree tree = new Tree(shell, SWT.BORDER);
		for (int i = 0; i < 16; i++) {
			TreeItem itemI = new TreeItem(tree, SWT.NONE);
			itemI.setText("Item " + i);
			for (int j = 0; j < 16; j++) {
				TreeItem itemJ = new TreeItem(itemI, SWT.NONE);
				itemJ.setText("Item " + j);
			}
		}
		final TreeItem[] lastItem = new TreeItem[1];
		final TreeEditor editor = new TreeEditor(tree);
		tree.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				final TreeItem item = (TreeItem) event.item;
				if (item != null && item == lastItem[0]) {
					boolean showBorder = true;
					final Composite composite = new Composite(tree, SWT.NONE);
					if (showBorder)
						composite.setBackground(black);
					final Text text = new Text(composite, SWT.NONE);
					final int inset = showBorder ? 1 : 0;
					composite.addListener(SWT.Resize, new Listener() {
						public void handleEvent(Event e) {
							Rectangle rect = composite.getClientArea();
							text.setBounds(rect.x + inset, rect.y + inset,
									rect.width - inset * 2, rect.height - inset
											* 2);
						}
					});
					Listener textListener = new Listener() {
						public void handleEvent(final Event e) {
							switch (e.type) {
							case SWT.FocusOut:
								item.setText(text.getText());
								composite.dispose();
								break;
							case SWT.Verify:
								String newText = text.getText();
								String leftText = newText.substring(0, e.start);
								String rightText = newText.substring(e.end,
										newText.length());
								GC gc = new GC(text);
								Point size = gc.textExtent(leftText + e.text
										+ rightText);
								gc.dispose();
								size = text.computeSize(size.x, SWT.DEFAULT);
								editor.horizontalAlignment = SWT.LEFT;
								Rectangle itemRect = item.getBounds(),
								rect = tree.getClientArea();
								editor.minimumWidth = Math.max(size.x,
										itemRect.width)
										+ inset * 2;
								int left = itemRect.x,
								right = rect.x + rect.width;
								editor.minimumWidth = Math.min(
										editor.minimumWidth, right - left);
								editor.minimumHeight = size.y + inset * 2;
								editor.layout();
								break;
							case SWT.Traverse:
								switch (e.detail) {
								case SWT.TRAVERSE_RETURN:
									item.setText(text.getText());
									// FALL THROUGH
								case SWT.TRAVERSE_ESCAPE:
									composite.dispose();
									e.doit = false;
								}
								break;
							}
						}
					};
					text.addListener(SWT.FocusOut, textListener);
					text.addListener(SWT.Traverse, textListener);
					text.addListener(SWT.Verify, textListener);
					editor.setEditor(composite, item);
					text.setText(item.getText());
					text.selectAll();
					text.setFocus();
				}
				lastItem[0] = item;
			}
		});
		shell.pack();
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}

}
