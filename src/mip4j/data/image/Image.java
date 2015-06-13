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
package mip4j.data.image;

public abstract class Image {
	protected int height;
	protected int width;

	public Image(int width, int height) {
		this.width = width;
		this.height = height;
	}

	public Image() {
		this.width = 1;
		this.height = 1;
	}

	public int getHeight() {
		return this.height;
	}

	public int getWidth() {
		return this.width;
	}

	protected void setHeight(int height) {
		this.height = height;
	}

	protected void setWidth(int width) {
		this.width = width;
	}

	@Override
	public String toString() {
		String toString = "";

		toString += (String.format("%30s", "Width ") + this.width + "\n");
		toString += (String.format("%30s", "Height ") + this.height + "\n");

		return toString;
	}
}
