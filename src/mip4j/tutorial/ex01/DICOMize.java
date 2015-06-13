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
package mip4j.tutorial.ex01;

import ij.ImagePlus;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import lu.tudor.santec.dicom.exporter.DicomExporter;

import org.dcm4che2.data.DicomObject;

public class DICOMize {

	public static void main(String[] args) throws IOException {
		BufferedImage bi = ImageIO.read(DICOMize.class.getResourceAsStream("../skin.jpg"));
		ImagePlus ip = new ImagePlus("", bi);

		DicomExporter exporter = new DicomExporter();
		DicomObject dObj = exporter.createHeader(ip, true, true, true);

		File dcmFile = new File("skin.dcm");
		exporter.write(dObj, ip, dcmFile, true);

		System.out.println(dObj);

	}
}
