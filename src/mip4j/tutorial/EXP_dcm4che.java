package mip4j.tutorial;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import mip4j.data.image.MR;

import org.dcm4che2.data.DicomObject;
import org.dcm4che2.io.DicomInputStream;
import org.dcm4che2.io.DicomOutputStream;

public class EXP_dcm4che {

	public static void main(String[] args) {
		DicomObject dcmObj = null;
		try (DicomInputStream din = new DicomInputStream(MR.class.getResourceAsStream("mr.dcm"))) {
			dcmObj = din.readDicomObject();
		} catch (Exception e) {
		}

		try (DicomOutputStream dos = new DicomOutputStream(
				new BufferedOutputStream(new FileOutputStream(new File(
						"newFile.dcm"))))) {
			dos.writeDicomFile(dcmObj);
		} catch (Exception e) {
		}
	}
}
