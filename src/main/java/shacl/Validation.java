package shacl;

import org.apache.jena.util.FileManager;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.topbraid.shacl.validation.ValidationUtil;
import org.topbraid.shacl.vocabulary.SH;

public class Validation {

	public static void main(String[] args) {
		try {
			Path path = Paths.get(".").toAbsolutePath().normalize();

			String data = path.toFile().getAbsolutePath() + "/src/main/resources/temperature.ttl";
			String shape = path.toFile().getAbsolutePath() + "/src/main/resources/temperatureShape.ttl";

			InputStream inData = FileManager.get().open(data);
			if (inData == null) {
				throw new IllegalArgumentException("File: " + data + " not found");
			}

			InputStream inShape = FileManager.get().open(data);
			if (inShape == null) {
				throw new IllegalArgumentException("File: " + inShape + " not found");
			}

			Model dataModel = ModelFactory.createDefaultModel();
			Model shapeModel = ModelFactory.createDefaultModel();
			
			dataModel.read(data, null);
			shapeModel.read(shape, null);
			Resource reportResource = ValidationUtil.validateModel(dataModel, shapeModel, true);
			boolean conforms = reportResource.getProperty(SH.conforms).getBoolean();
			System.out.println("Conforms = " + conforms);
			if (!conforms) {
				String report = path.toFile().getAbsolutePath() + "/src/main/resources/temperatureReport.ttl";
				reportResource.getModel().write(new FileOutputStream(new File(report)), "TURTLE");
			}
		} catch (Exception e) {
			System.out.println("Exception = " + e.getLocalizedMessage());
		}
	}
}
