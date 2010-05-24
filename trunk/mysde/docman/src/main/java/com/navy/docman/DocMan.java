package com.navy.docman;

import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.navy.docman.gui.GuiContainer;
import com.navy.docman.gui.TreeManager;

public class DocMan {

	public static void main(String[] args) {
		Display display = new Display();
		final Shell shell = new Shell(display);
		FormLayout layout = new FormLayout();
		shell.setLayout(layout);
		GuiContainer.init(shell);
		shell.open();
		TreeManager.refreshTable(GuiContainer.getInstance().getDocTree()
				.getSelection()[0]);
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}

}
