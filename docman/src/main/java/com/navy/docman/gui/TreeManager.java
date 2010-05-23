package com.navy.docman.gui;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.SystemUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.eclipse.swt.SWT;
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
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import com.navy.docman.entity.Document;
import com.navy.docman.entity.Folder;
import com.navy.docman.service.FolderManager;
import com.navy.docman.service.ServiceLocator;
import com.navy.docman.util.FileUtil;

public class TreeManager {

	static final Long rootParent = -1L;
	static Image imagesOpen = null;
	static Image imagesClose = null;
	static Image[] images = null;
	static FolderManager fmgr = ServiceLocator.getFolderManager();
	static Color color = null;

	public static Map<Folder, TreeItem> folderMap = new HashMap<Folder, TreeItem>();

	public static void init(Tree tree) {
		final Class<TreeManager> clazz = TreeManager.class;

		try {
			InputStream sourceStream = clazz
					.getResourceAsStream("closedFolder.gif");
			ImageData source = new ImageData(sourceStream);
			ImageData mask = source.getTransparencyMask();
			imagesClose = new Image(null, source, mask);
			sourceStream = clazz.getResourceAsStream("openFolder.gif");
			source = new ImageData(sourceStream);
			mask = source.getTransparencyMask();
			imagesOpen = new Image(null, source, mask);
			sourceStream.close();
			images = new Image[] { imagesClose, imagesOpen };
		} catch (IOException e) {
			e.printStackTrace();
		}

		List<Folder> flds = fmgr.getByParentIdOrderByName(rootParent);
		if (flds == null || flds.size() == 0) {
			// init root directory
			Folder folder = new Folder();
			folder.setFolderName("文档管理");
			folder.setParentId(-1L);
			fmgr.saveFolder(folder);
			if (flds == null) {
				flds = new ArrayList<Folder>();
			}
			flds.add(folder);
		}
		for (Folder folder : flds) {
			TreeItem item = new TreeItem(tree, SWT.NONE);
			item.setData(folder);
			item.setText(folder.getFolderName());
			item.setImage(imagesOpen); // only use imagesClose
			tree.select(item);
			folderMap.put(folder, item);
			initTreeSubItems(item);
			// refreshTable(item);
		}
		// addDragListener(tree);
		addTreeEditorListener(tree);
		addCommonListener(tree);

	}

	private static void initTreeSubItems(final TreeItem item) {
		Folder parent = (Folder) item.getData();
		List<Folder> flds = fmgr.getByParentIdOrderByName(parent.getId());
		for (Folder folder : flds) {
			TreeItem subItem = new TreeItem(item, SWT.NONE);
			subItem.setData(folder);
			subItem.setText(folder.getFolderName());
			subItem.setImage(imagesClose);
			folderMap.put(folder, subItem);
			initTreeSubItems(subItem);
		}
	}

	public static TreeItem addTreeItem(TreeItem parentItem) {
		String name = GuiContainer.getInstance().prompt("请输入分类名称");
		if (StringUtils.isBlank(name)) {
			return null;
		}
		// refreshTable();
		return addTreeItem(parentItem, name.trim());
	}

	public static void renameTreeItem(TreeItem item) {
		String name = GuiContainer.getInstance().prompt("请输入分类名称",
				item.getText());
		if (StringUtils.isBlank(name)) {
			return;
		}
		updateItem(item, name.trim());
		refreshTable(item);
	}

	private static TreeItem addTreeItem(TreeItem parentItem, String name) {
		Folder parent = (Folder) parentItem.getData();
		if (!fmgr.isValid(parent.getId(), name)) {
			GuiContainer.getInstance().message("分类 “" + name + "” 已经存在，创建失败！");
			return null;
		}
		Folder folder = new Folder();
		folder.setFolderName(name);
		folder.setParentId(parent.getId());
		fmgr.saveFolder(folder);
		TreeItem subItem = new TreeItem(parentItem, SWT.NONE);
		subItem.setData(folder);
		subItem.setText(folder.getFolderName());
		subItem.setImage(images);
		FileUtil.createDir(getParentPath(parentItem, name));
		if (!parentItem.getExpanded()) {
			parentItem.setExpanded(true);
			parentItem.setImage(imagesOpen);
		}
		GuiContainer.getInstance().getDocTree().setSelection(subItem);
		refreshTable(subItem);
		folderMap.put((Folder) subItem.getData(), subItem);
		return subItem;
	}

	public static void batchImport(TreeItem item) {
		String selectPath = GuiContainer.getInstance().directoryDialog();
		if (StringUtils.isNotBlank(selectPath)) {
			if (selectPath.length() < 3) {
				GuiContainer.getInstance().message("不能选择磁盘根目录！");
				return;
			}
			String node = selectPath.substring(selectPath
					.lastIndexOf(SystemUtils.FILE_SEPARATOR) + 1);
			String distPath = getParentPath(item, node);
			FileUtil.createDir(distPath);
			boolean result = FileUtil.xcopy(new File(selectPath), new File(
					distPath));
			// add documents
			System.out.println(result);
			addDocuments(item, node);

		}
	}

	private static void addDocuments(TreeItem parnentItem, String node) {
		String path = getParentPath(parnentItem, node);
		TreeItem item = addTreeItem(parnentItem, node);
		File dir = new File(path);
		File[] docs = dir.listFiles();
		for (int i = 0; i < docs.length; i++) {
			if (docs[i].isHidden()) {
				System.out.println("ignored hidden file: "
						+ docs[i].getAbsolutePath());
			} else if (docs[i].isDirectory()) {
				addDocuments(item, docs[i].getName());
			} else {
				addDocument(item, docs[i]);
			}
		}

	}

	public static void addDocument(TreeItem item, File file) {
		Document doc = new Document();
		doc.setFolder((Folder) item.getData());
		doc.setCreateDate(new Date(file.lastModified()));
		doc.setDocName(file.getName().lastIndexOf(".") == -1 ? file.getName()
				: file.getName().substring(0, file.getName().lastIndexOf(".")));
		doc.setDocType(FileUtil.getSuffix(file.getName()));
		doc.setFileSize(FileUtil.getPrintFileSize(file));
		doc.setLocation(file.getName());
		doc.setLoginId(SystemUtils.USER_NAME);
		fmgr.saveDocument(doc);
		System.out.println("Save doc: " + doc.getLocation());

	}

	public static String getParentPath(TreeItem item, String childPath) {
		Folder folder = (Folder) item.getData();
		String parentPath = folder.getFolderName() + SystemUtils.FILE_SEPARATOR
				+ childPath;
		if (rootParent.equals(folder.getParentId())) {
			parentPath = SystemUtils.USER_DIR + SystemUtils.FILE_SEPARATOR
					+ "data" + SystemUtils.FILE_SEPARATOR + parentPath;
		} else {
			parentPath = getParentPath(item.getParentItem(), parentPath);
		}
		return parentPath;
	}

	private static void addCommonListener(final Tree tree) {
		tree.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				TreeItem item = (TreeItem) e.item;
				refreshTable(item);

			}
		});
		tree.addListener(SWT.DefaultSelection, new Listener() {
			public void handleEvent(Event e) {
				String string = "";
				TreeItem[] selection = tree.getSelection();
				for (int i = 0; i < selection.length; i++)
					string += selection[i] + " ";
				System.out.println("DefaultSelection={" + string + "}");
			}
		});
		tree.addListener(SWT.Expand, new Listener() {
			public void handleEvent(Event e) {
				System.out.println("Expand={" + e.item + "}");
				TreeItem item = (TreeItem) e.item;
				item.setImage(imagesOpen);
			}
		});
		tree.addListener(SWT.Collapse, new Listener() {
			public void handleEvent(Event e) {
				System.out.println("Collapse={" + e.item + "}");
				TreeItem item = (TreeItem) e.item;
				item.setImage(imagesClose);
			}
		});
		tree.getItems()[0].setExpanded(true);

	}

	@SuppressWarnings("unused")
	private static void addDragListener(final Tree tree) {
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
					Point pt = tree.getShell().getDisplay().map(null, tree,
							event.x, event.y);
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
					item.setImage(images);
				} else {
					TreeItem item = (TreeItem) event.item;
					Point pt = tree.getShell().getDisplay().map(null, tree,
							event.x, event.y);
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
							newItem.setImage(images);
						} else if (pt.y > bounds.y + 2 * bounds.height / 3) {
							TreeItem newItem = new TreeItem(parent, SWT.NONE,
									index + 1);
							newItem.setText(text);
							newItem.setImage(images);
						} else {
							TreeItem newItem = new TreeItem(item, SWT.NONE);
							newItem.setText(text);
							newItem.setImage(images);
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
							newItem.setImage(images);
						} else if (pt.y > bounds.y + 2 * bounds.height / 3) {
							TreeItem newItem = new TreeItem(tree, SWT.NONE,
									index + 1);
							newItem.setText(text);
							newItem.setImage(images);
						} else {
							TreeItem newItem = new TreeItem(item, SWT.NONE);
							newItem.setText(text);
							newItem.setImage(images);
						}
					}

				}
			}
		});
	}

	private static void addTreeEditorListener(final Tree tree) {
		final TreeItem[] lastItem = new TreeItem[1];
		final TreeEditor editor = new TreeEditor(tree);
		final Color black = tree.getDisplay().getSystemColor(SWT.COLOR_BLACK);

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
							String oldText = item.getText();
							switch (e.type) {
							case SWT.FocusOut:
								if (!oldText.equals(text.getText().trim())) {
									updateItem(item, text.getText());
								}
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
									if (!oldText.equals(text.getText().trim())) {
										updateItem(item, text.getText());
									}
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

	}

	private static void updateItem(TreeItem item, String name) {
		name = name.trim();
		if (item.getText().equals(name) || StringUtils.isBlank(name)) {
			return;
		}
		Folder folder = (Folder) item.getData();
		if (!folder.getParentId().equals(rootParent)) {
			TreeItem parentItem = item.getParentItem();
			Folder parent = (Folder) parentItem.getData();
			if (!fmgr.isValid(parent.getId(), name)) {
				GuiContainer.getInstance().message(
						"分类 “" + name + "” 已经存在，修改失败！");
				return;
			}
		}
		folderMap.remove(folder);
		String srcPath = getParentPath(item, "");
		item.setText(name);
		folder.setFolderName(name);
		fmgr.saveFolder(folder);
		folderMap.put(folder, item);
		String destPath = getParentPath(item, "");
		FileUtil.renameTo(srcPath, destPath);
		// Folder fd = fmgr.getById(folder.getId());
		System.out.println("update name: " + folder.getFolderName() + " /id: "
				+ folder.getId());

	}

	public static void removeItem(TreeItem item) {
		if (GuiContainer.getInstance().confirm(
				"你确定要删除分类“" + item.getText() + "”吗？")) {
			if (item.getItemCount() > 0) {
				if (GuiContainer.getInstance().confirm(
						"分类“" + item.getText() + "”还包含子分类，删除后不可恢复，要继续吗？")) {
					remove(item);
				}
			} else {
				remove(item);
			}
		}
	}

	public static void removeDocument(Document doc) {
		fmgr.removeDocument(doc);
	}

	private static void remove(TreeItem item) {
		Folder folder = (Folder) item.getData();
		fmgr.removeFolder(folder);
		folderMap.remove(folder);
		FileUtil.removeDir(getParentPath(item, ""));
		TreeItem parentItem = item.getParentItem();
		parentItem.removeAll();
		initTreeSubItems(parentItem);
	}

	public static void refreshTable(TreeItem treeItem) {
		Folder folder = (Folder) treeItem.getData();
		List<Document> documents = fmgr.findDocByFolder(folder);
		Table table = GuiContainer.getInstance().getListTable();
		refreshTable(table, documents);
		if (table.getItemCount() > 0) {
			table.select(0);
			TableItem item = table.getSelection()[0];
			GuiContainer.getInstance().getDocView().setDocument(
					(Document) item.getData());
		} else {
			Document doc = new Document();
			GuiContainer.getInstance().getDocView().setDocument(doc);
		}
	}

	public static void refreshTable(Table table, List<Document> documents) {
		if (color == null) {
			color = GuiContainer.tableRowColor;
		}
		table.removeAll();
		int index = 1;
		for (Document doc : documents) {
			TableItem item = new TableItem(table, SWT.NONE);
			item.setData(doc);
			item.setText(0, Integer.toString(index++));
			item.setBackground(0, color);
			item.setText(1, getStringValue(doc.getDocName()));
			item.setText(2, getStringValue(doc.getDocType()));
			item.setText(3, getStringValue(doc.getCategoryCode()));
			item.setText(4, getStringValue(doc.getCategoryName()));
			item.setText(5, getStringValue(doc.getAuthor()));
			item.setText(6, getStringValue(doc.getAuditor()));
			item.setText(7, getStringValue(doc.getFinalCode()));
			item.setText(8, getStringValue(doc.getManualCode()));
			item.setText(9, getDateString(doc.getDispatchDate()));
			item.setText(10, getStringValue(doc.getSendPerson()));
			item.setText(11, getStringValue(doc.getLoginId()));
			item.setText(12, getDateString(doc.getCreateDate()));
			item.setText(13, getStringValue(doc.getFileSize()));
		}
		table.setVisible(false);
		for (int i = 0; i < table.getColumnCount(); i++) {
			table.getColumn(i).pack();
		}
		table.setVisible(true);
	}

	public static String getStringValue(String str) {
		return str == null ? "" : str;
	}

	public static String getDateString(Date date) {
		if (date == null) {
			return "";
		}
		return DateFormatUtils.format(date, "yyyy-MM-dd");
	}

	public static void saveDocument(Document doc) {
		fmgr.saveDocument(doc);
		refreshTable(GuiContainer.getInstance().getDocTree().getSelection()[0]);
	}

	public static String getCurrentPath() {
		return getParentPath(GuiContainer.getInstance().getDocTree()
				.getSelection()[0], "");
	}
}
