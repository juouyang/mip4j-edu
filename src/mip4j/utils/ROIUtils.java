package mip4j.utils;

import ij.gui.Roi;
import ij.io.RoiDecoder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ROIUtils {

	private ROIUtils() { //
	}

	public static ArrayList<Roi> getRoisFromZip(File zipFile) {
		ArrayList<Roi> rois = new ArrayList<>();

		try (ZipInputStream zin = new ZipInputStream(new FileInputStream(
				zipFile))) {
			while (true) {
				ZipEntry entry = zin.getNextEntry();
				if (entry == null)
					break;

				String name = entry.getName();

				if (!name.endsWith(".roi"))
					continue;

				try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
					byte[] buf = new byte[1024];
					int len;

					while ((len = zin.read(buf)) > 0)
						out.write(buf, 0, len);
					out.close();

					Roi roi = new RoiDecoder(out.toByteArray(), name).getRoi();

					if (roi != null)
						rois.add(roi);
				}

			}

			zin.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (rois.size() == 0)
			throw new IllegalArgumentException(
					"This ZIP archive does not appear to contain \".roi\" files");

		return rois;
	}

	public static ArrayList<Roi> getRoisByPosition(ArrayList<Roi> rois,
			int position) {
		ArrayList<Roi> ret = new ArrayList<>();

		for (Roi r : rois)
			if (r.getPosition() == position)
				ret.add(r);

		return ret;
	}

	public static Roi getRoiByPoint(ArrayList<Roi> rois, int x, int y) {
		for (Roi r : rois)
			if (r.contains(x, y))
				return r;
		return null;
	}

	public static boolean isWithinRoi(ArrayList<Roi> rois, int x, int y) {
		for (Roi r : rois)
			if (r.contains(x, y))
				return true;
		return false;
	}
}
