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
package mip4j.tutorial.ex03;

import ij.ImagePlus;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

import javax.imageio.ImageIO;

import lu.tudor.santec.dicom.exporter.DicomExporter;

import org.dcm4che2.data.DicomObject;
import org.dcm4che2.data.Tag;
import org.dcm4che2.data.TransferSyntax;
import org.dcm4che2.data.UID;
import org.dcm4che2.data.VR;
import org.dcm4che2.util.UIDUtils;

public class DICOMize {

	public static void main(String[] args) throws IOException, ParseException {
		BufferedImage bi = ImageIO.read(DICOMize.class.getResourceAsStream("../skin.jpg"));
		ImagePlus ip = new ImagePlus("", bi);

		DicomExporter exporter = new DicomExporter();
		DicomObject dObj = exporter.createHeader(ip, true, true, true);

		// SOP Class
		String sopClass = UID.MRImageStorage;
		dObj.putString(Tag.SOPClassUID, VR.UI, sopClass);
		dObj.putString(Tag.SOPInstanceUID, VR.UI, UIDUtils.createUID());
		dObj.putString(Tag.SpecificCharacterSet, VR.CS, "ISO_IR 100");
		// dObj.putString(Tag.TransferSyntaxUID, VR.UI,
		// TransferSyntax.ImplicitVRBigEndian.uid()); // ImageJ not support
		// dObj.putString(Tag.TransferSyntaxUID, VR.UI,
		// TransferSyntax.ImplicitVRLittleEndian.uid());
		// dObj.putString(Tag.TransferSyntaxUID, VR.UI,
		// TransferSyntax.ExplicitVRBigEndian.uid());
		dObj.putString(Tag.TransferSyntaxUID, VR.UI,
				TransferSyntax.ExplicitVRLittleEndian.uid());
		// Patient
		dObj.putString(Tag.PatientName, VR.PN, "Ju Ouyang");
		dObj.putString(Tag.PatientID, VR.LO, "A123456789");
		DateFormat df = DateFormat.getDateInstance();
		Date date = df.parse("1983/4/15");
		dObj.putDate(Tag.PatientBirthDate, VR.DA, date);
		dObj.putString(Tag.PatientSex, VR.CS, "M");
		// Study
		dObj.putString(Tag.StudyInstanceUID, VR.UI, UIDUtils.createUID());
		Date now = new Date();
		dObj.putDate(Tag.StudyDate, VR.DA, now);
		dObj.putDate(Tag.StudyTime, VR.TM, now);
		dObj.putString(Tag.ReferringPhysicianName, VR.PN, "Dr. Who");
		dObj.putString(Tag.StudyID, VR.SH, "9527");
		// Series
		dObj.putString(Tag.Modality, VR.CS, "CS");
		dObj.putString(Tag.SeriesInstanceUID, VR.UI, UIDUtils.createUID());
		dObj.putInt(Tag.SeriesNumber, VR.IS, 1);
		// Image
		dObj.putInt(Tag.InstanceNumber, VR.IS, 1);
		dObj.putDate(Tag.InstanceCreationDate, VR.DA, now);
		dObj.putDate(Tag.InstanceCreationTime, VR.TM, now);

		File out = new File("skin.dcm");
		exporter.write(dObj, ip, out, true);

		System.out.println(dObj);

	}
}
