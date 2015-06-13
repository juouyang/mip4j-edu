package mip4j.tutorial;

import ij.ImagePlus;
import ij.io.FileSaver;
import ij.plugin.FolderOpener;
import ij.plugin.ZProjector;
import ij.process.StackProcessor;

import java.net.URISyntaxException;

public class EXP_ImageJ {

	public static void main(String[] args) throws URISyntaxException {
		if (args.length == 0) {
			System.out.println("Pleas assign a path contains images");
			return;
		}

		FolderOpener fo = new FolderOpener();
		ImagePlus ip = fo.openFolder(args[0]);
		StackProcessor sp = new StackProcessor(ip.getImageStack(),
				ip.getProcessor());
		sp.flipVertical();
		ip.show();

		ZProjector z = new ZProjector(ip);
		z.setMethod(1);
		z.doProjection();
		ImagePlus mip = z.getProjection();
		mip.show();

		FileSaver fs = new FileSaver(mip);
		fs.saveAsJpeg("./mip.jpg");
	}

}
