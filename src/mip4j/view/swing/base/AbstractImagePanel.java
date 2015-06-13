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
package mip4j.view.swing.base;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import mip4j.data.image.Image;

public abstract class AbstractImagePanel<T extends Image> extends JPanel {
	private static final long serialVersionUID = 1L;
	protected BufferedImage buf = new BufferedImage(1, 1,
			BufferedImage.TYPE_BYTE_GRAY);
	private T img = null;
	private String msg = new String();

	protected abstract BufferedImage newBufferedImage(int w, int h);

	protected abstract void convertImageToBufferedImage();

	public void setImage(T i) {
		if (i == null) {
			return;
		}

		this.img = i;

		final int w = i.getWidth();
		final int h = i.getHeight();

		if ((this.buf == null) || (this.buf.getWidth() != w)
				|| (this.buf.getHeight() != h)) {
			this.buf = newBufferedImage(w, h);
		}

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				setSize(w, h);
				setPreferredSize(new Dimension(w, h));
				invalidate();
				repaint();
			}
		});
	}

	public T getImage() {
		return this.img;
	}

	public void setMessage(String msg) {
		this.msg = msg;

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				invalidate();
				repaint();
			}
		});
	}

	@Override
	public void paint(Graphics g) {
		if (this.img != null) {
			convertImageToBufferedImage();
			g.drawImage(this.buf, 0, 0, this);
			g.setColor(Color.WHITE);
			g.drawString(this.msg, 0, 14);
		} else {
			super.paint(g);
		}
	}

	@Override
	public Dimension getPreferredSize() {
		if (this.img != null) {
			return new Dimension(this.img.getWidth(), this.img.getHeight());
		}

		return new Dimension(1, 1);
	}
}
