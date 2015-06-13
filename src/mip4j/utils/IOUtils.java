package mip4j.utils;

import static java.nio.file.FileVisitResult.CONTINUE;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;

public class IOUtils {
	private IOUtils() { //
	}

	public static ArrayList<File> listFiles(String srcRoot) throws IOException {
		return new FileVisitor(srcRoot).getFiles();
	}

	public static String[] listFilePath(String dir) {
		String[] ret = new File(dir).list(null);

		for (int i = 0; i < ret.length; i++)
			ret[i] = dir + "/" + ret[i];

		return ret;
	}
}

class FileVisitor {
	private String srcRoot;
	ArrayList<File> files;

	public FileVisitor(String root) {
		this.srcRoot = root;
		this.files = new ArrayList<>();
	}

	private void walkFiles() throws IOException {
		Files.walkFileTree(Paths.get(this.srcRoot),
				new SimpleFileVisitor<Path>() {
					@Override
					public FileVisitResult visitFile(Path file,
							BasicFileAttributes attrs) {
						if (file.getFileName() != null) {
							FileVisitor.this.files.add(file.toFile());
						}
						return CONTINUE;
					}
				});
	}

	public ArrayList<File> getFiles() throws IOException {
		walkFiles();
		return this.files;
	}
}