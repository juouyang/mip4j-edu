/*******************************************************************************
 * Copyright (c) 2014 Ju Ouyang (juouyang@gmail.com).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     Ju Ouyang (juouyang@gmail.com) - initial API and implementation
 ******************************************************************************/
package mip4j.view.swing;

import java.awt.BorderLayout;
import java.io.FileNotFoundException;

import javax.swing.JFrame;
import javax.swing.JScrollPane;

import mip4j.data.image.MR;
import mip4j.data.image.ShortImage;

public class ShortImageFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	ShortImagePanel imgPanel = null;

	public ShortImageFrame() {
		this.imgPanel = new ShortImagePanel();
		JScrollPane scrollPane = new JScrollPane();

		scrollPane.getViewport().add(this.imgPanel);
		add(scrollPane, BorderLayout.CENTER);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public void setImage(ShortImage img) {
		this.imgPanel.setImage(img);
		pack();
		setVisible(true);
	}

	public static void main(String[] args) throws FileNotFoundException {
		MR mr = new MR(MR.class.getResourceAsStream("mr.dcm"));
		new ShortImageFrame().setImage(mr);
	}
}
