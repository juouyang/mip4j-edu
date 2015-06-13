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

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.dcm4che2.data.BasicDicomObject;
import org.dcm4che2.data.DicomObject;
import org.dcm4che2.data.Tag;
import org.dcm4che2.data.TransferSyntax;
import org.dcm4che2.io.DicomInputStream;
import org.dcm4che2.io.DicomOutputStream;

public class MR extends ShortImage {
	public String patientID;
	public String studyInstanceUID;
	public String seriesInstanceUID;
	public String sopInstanceUID;

	public MR(int width, int height) {
		super(width, height);
	}

	public MR(String dcmFile) throws FileNotFoundException {
		this(new FileInputStream(dcmFile));
	}

	public MR(InputStream in) {
		DicomObject dcmObj = null;
		try (DicomInputStream din = new DicomInputStream(in)) {
			dcmObj = din.readDicomObject();
		} catch (IOException e) {
			e.printStackTrace();
		}

		this.patientID = dcmObj.getString(Tag.PatientID);
		this.studyInstanceUID = dcmObj.getString(Tag.StudyInstanceUID);
		this.seriesInstanceUID = dcmObj.getString(Tag.SeriesInstanceUID);
		this.sopInstanceUID = dcmObj.getString(Tag.SOPInstanceUID);
		this.windowCenter = (int) dcmObj.getDouble(Tag.WindowCenter);
		this.windowWidth = (int) dcmObj.getDouble(Tag.WindowWidth);
		this.width = Integer.parseInt(dcmObj.getString(Tag.Columns));
		this.height = Integer.parseInt(dcmObj.getString(Tag.Rows));
		this.pixelArray = dcmObj.getShorts(Tag.PixelData);
		this.rescaleIntercept = (int) dcmObj.getDouble(Tag.RescaleIntercept);

		for (int i = 0; i < this.pixelArray.length; i++) {
			this.pixelArray[i] += this.rescaleIntercept;
		}

		flipVertically();
	}

	public void writeDICOM(String dcmFile) {
		DicomObject dcmObj = new BasicDicomObject();

		dcmObj.putString(Tag.TransferSyntaxUID,
				dcmObj.vrOf(Tag.TransferSyntaxUID),
				TransferSyntax.ImplicitVRLittleEndian.uid());
		dcmObj.putString(Tag.PatientID, dcmObj.vrOf(Tag.PatientID), "12345");
		dcmObj.putString(Tag.StudyInstanceUID,
				dcmObj.vrOf(Tag.StudyInstanceUID), this.studyInstanceUID);
		dcmObj.putString(Tag.SeriesInstanceUID,
				dcmObj.vrOf(Tag.SeriesInstanceUID), this.seriesInstanceUID);
		dcmObj.putString(Tag.SOPInstanceUID, dcmObj.vrOf(Tag.SOPInstanceUID),
				this.sopInstanceUID);
		dcmObj.putInt(Tag.BitsAllocated, dcmObj.vrOf(Tag.BitsAllocated), 16);
		dcmObj.putInt(Tag.BitsStored, dcmObj.vrOf(Tag.BitsStored), 16);
		dcmObj.putInt(Tag.HighBit, dcmObj.vrOf(Tag.HighBit), 15);
		dcmObj.putInt(Tag.SamplesPerPixel, dcmObj.vrOf(Tag.SamplesPerPixel), 1);
		dcmObj.putString(Tag.PhotometricInterpretation,
				dcmObj.vrOf(Tag.PhotometricInterpretation), "MONOCHROME2");
		dcmObj.putDouble(Tag.WindowCenter, dcmObj.vrOf(Tag.WindowCenter),
				this.windowCenter);
		dcmObj.putDouble(Tag.WindowWidth, dcmObj.vrOf(Tag.WindowWidth),
				this.windowWidth);
		dcmObj.putInt(Tag.Columns, dcmObj.vrOf(Tag.Columns), this.width);
		dcmObj.putInt(Tag.Rows, dcmObj.vrOf(Tag.Rows), this.height);
		for (int i = 0; i < this.pixelArray.length; i++) {
			this.pixelArray[i] -= this.rescaleIntercept;
		}
		dcmObj.putShorts(Tag.PixelData, dcmObj.vrOf(Tag.PixelData),
				this.pixelArray);
		dcmObj.putDouble(Tag.RescaleIntercept,
				dcmObj.vrOf(Tag.RescaleIntercept), 0);

		File destFile = new File(dcmFile);

		try (DicomOutputStream dos = new DicomOutputStream(
				new BufferedOutputStream(new FileOutputStream(destFile)))) {
			dos.writeDicomFile(dcmObj);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws IOException {
		MR mr = new MR(MR.class.getResourceAsStream("mr.dcm"));
		ImageIO.write(mr.getBufferedImage(1000, 4000), "JPG",
				new File("mr.jpg"));
	}
}
