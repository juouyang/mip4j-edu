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

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

import mip4j.data.image.ShortImage;
import mip4j.view.swing.base.AbstractImagePanel;

public class ShortImagePanel extends AbstractImagePanel<ShortImage> {
	private static final long serialVersionUID = 1L;
	int preX = 0;
	int preY = 0;
	int winCenter = 0;
	int winWidth = 0;
	short pixelMax;
	short pixelMin;

	public ShortImagePanel() {
		addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				int level = (e.getX() > ShortImagePanel.this.preX) ? 1 : -1;
				if (e.getX() == ShortImagePanel.this.preX)
					level = 0;
				level = (int) (ShortImagePanel.this.winCenter - level
						* (ShortImagePanel.this.pixelMax - ShortImagePanel.this.pixelMin)
						* 0.001);

				int width = (e.getY() > ShortImagePanel.this.preY) ? 1 : -1;
				if (e.getY() == ShortImagePanel.this.preY)
					width = 0;
				width = ShortImagePanel.this.winWidth - width
						* Math.abs(ShortImagePanel.this.preY - e.getY()) * 10;

				setWinLevelWidth(level, width);
				ShortImagePanel.this.preX = e.getX();
				ShortImagePanel.this.preY = e.getY();
			}

			@Override
			public void mouseMoved(MouseEvent e) {
				setPixelValue(e.getX(), e.getY());
			}
		});

		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					setWinLevelWidth(getImage().getWindowCenter(), getImage()
							.getWindowWidth());
				}
			}

			@Override
			public void mousePressed(MouseEvent e) {
				ShortImagePanel.this.preX = e.getX();
				ShortImagePanel.this.preY = e.getY();
			}
		});
	}

	@Override
	protected BufferedImage newBufferedImage(int w, int h) {
		if ((this.buf == null) || (this.buf.getWidth() != w)
				|| (this.buf.getHeight() != h)) {
			return new BufferedImage(w, h, BufferedImage.TYPE_BYTE_GRAY);
		}
		return this.buf;
	}

	@Override
	protected void convertImageToBufferedImage() {
		short[] pixels = this.getImage().getPixelArray();
		int w = this.getImage().getWidth();
		int h = this.getImage().getHeight();

		if (pixels.length != (w * h)) {
			throw new IllegalArgumentException();
		}

		byte[] data = ((DataBufferByte) this.buf.getRaster().getDataBuffer())
				.getData();
		convertShortsToBytes(pixels, this.winCenter - (this.winWidth / 2),
				this.winCenter + (this.winWidth / 2), data);
	}

	private void convertShortsToBytes(short[] inShortBuf, int windowMin,
			int windowMax, byte[] outByteBuf) {
		final int displayMin = 0;
		final int displayMax = 255;
		final int imgMin = (windowMin >= this.pixelMin) ? windowMin
				: this.pixelMin;
		final int imgMax = (windowMax <= this.pixelMax) ? windowMax
				: this.pixelMax;
		float displayRatio = (float) (displayMax - displayMin)
				/ (imgMax - imgMin);

		for (int i = 0; i < inShortBuf.length; ++i) {
			int in = inShortBuf[i];
			int out;

			if (in < imgMin) {
				out = displayMin;
			} else if (in > imgMax) {
				out = displayMax;
			} else {
				out = (int) ((in - imgMin) * displayRatio);
			}

			outByteBuf[i] = (byte) out;
		}
	}

	@SuppressWarnings({ "nls", "boxing" })
	void setPixelValue(int x, int y) {
		if (getImage() == null || (x < 0) || (x >= getImage().getWidth())
				|| (y < 0) || (y >= getImage().getHeight())) {
			return;
		}

		setMessage(String.format("(%04d,%04d)=%04d", x, y,
				getImage().getPixel(x, y)));
	}

	@SuppressWarnings({ "nls", "boxing" })
	void setWinLevelWidth(int windowCenter, int windowWidth) {
		if ((windowWidth < 5))
			this.winWidth = 5;
		else if (windowWidth > (this.pixelMax - this.pixelMin) * 2)
			this.winWidth = (this.pixelMax - this.pixelMin) * 2;
		else
			this.winWidth = windowWidth;

		if (windowCenter <= this.pixelMin)
			this.winCenter = this.pixelMin + 1;
		else if (windowCenter >= this.pixelMax)
			this.winCenter = this.pixelMax - 1;
		else
			this.winCenter = windowCenter;

		setMessage(String.format("[%04d:%04d]", this.winCenter, this.winWidth));
	}

	@Override
	public void setImage(ShortImage img) {
		this.winCenter = img.getWindowCenter();
		this.winWidth = img.getWindowWidth();
		this.pixelMax = img.getMax();
		this.pixelMin = img.getMin();
		super.setImage(img);
	}
}
