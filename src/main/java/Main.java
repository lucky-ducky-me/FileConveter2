import FileConverter.*;
import Weapon.*;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {

    static public void main(String[] args) throws ParserConfigurationException, IOException {

        try {

            var path = Paths.get("");

            var sourceFile = path.toAbsolutePath().toString() + "\\src\\main\\resources\\test.xml";

            var destinationFile = path.toAbsolutePath().toString() + "\\src\\main\\resources\\test.json";

            if (args != null && args.length >= 2) {
                sourceFile = args[0];

                destinationFile = args[1];
            }
            
            if (sourceFile.endsWith(".xml") && destinationFile.endsWith(".json")) {
                FileConverterService.xmlToJson(sourceFile, destinationFile);
            }
            else if (sourceFile.endsWith(".json") && destinationFile.endsWith(".xml")) {
                FileConverterService.jsonToXml(sourceFile, destinationFile);
            }
            else {
                throw new Exception("Переданные файлы неверных типов.");
            }
        }
        catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
