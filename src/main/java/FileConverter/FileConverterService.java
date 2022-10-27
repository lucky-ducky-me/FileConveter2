package FileConverter;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

/**
 * Конвертация файлов.
 */
public class FileConverterService {

    /**
     * Из xml в json.
     * @param xmlFile xml файл.
     * @param jsonFile json файл
     */
    public static void xmlToJson(String xmlFile, String jsonFile) {
        var weaponData = XmlWeaponWorker.getWeaponData(xmlFile);

        JsonWeaponWorker.writeWeaponData(jsonFile, weaponData);
    }

    /**
     * Из xml в json.
     * @param jsonFile json файл
     * @param xmlFile xml файл.
     */
    public static void jsonToXml(String jsonFile, String xmlFile) throws ParserConfigurationException, IOException {
        var weaponData = JsonWeaponWorker.getWeaponData(jsonFile);

        XmlWeaponWorker.writeWeaponData(xmlFile, weaponData);
    }
}
