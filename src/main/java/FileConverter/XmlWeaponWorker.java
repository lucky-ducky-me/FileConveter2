package FileConverter;

import java.io.*;
import java.util.ArrayList;

import Weapon.Pistol;
import Weapon.Rifle;
import Weapon.WeaponData;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.*;

public class XmlWeaponWorker {

    /**
     * Считывание данных об оружии из Xml файла.
     * @param fileLocation путь до файла.
     * @return данные об оружии.
     */
    public static WeaponData getWeaponData(String fileLocation) {
        var dbf = DocumentBuilderFactory.newInstance();

        try (InputStream is = new FileInputStream(new File(fileLocation))) {
            var db = dbf.newDocumentBuilder();

            var doc = db.parse(is);

            var list = doc.getChildNodes();

            var weaponDatas = new ArrayList<WeaponData>();

            parseXmlWeaponFile(list, weaponDatas);

            var weaponData = new WeaponData();

            for (var i = 0; i < weaponDatas.size(); i++) {
                var pistols = weaponData.getPistols();
                var rifles = weaponData.getRifles();

                if (pistols == null) {
                    pistols = new ArrayList<>();
                }

                if (rifles == null) {
                    rifles = new ArrayList<>();
                }

                for (var j = 0; j < weaponDatas.get(i).getRifles().size(); j++) {
                    rifles.add(weaponDatas.get(i).getRifles().get(j));
                }

                for (var j = 0; j < weaponDatas.get(i).getPistols().size(); j++) {
                    pistols.add(weaponDatas.get(i).getPistols().get(j));
                }

                weaponData.setPistols(pistols);
                weaponData.setRifles(rifles);
            }

            return weaponData;
        }
        catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        }
        catch (SAXException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Парсинг xml файла с оружием.
     * @param nodeList текущий тэг.
     * @param weaponDatas данные об оружии.
     */
    private static void parseXmlWeaponFile(NodeList nodeList, ArrayList<WeaponData> weaponDatas) {
        for (int count = 0; count < nodeList.getLength(); count++) {

            var tempNode = nodeList.item(count);

            if (tempNode.getNodeType() == Node.ELEMENT_NODE) {
                if (tempNode.getNodeName() == "Countries") {
                    var countryNodes = tempNode.getChildNodes();

                    for (var i = 0; i < countryNodes.getLength(); i++) {
                        var country = countryNodes.item(i);

                        if (country.getNodeType() == Node.ELEMENT_NODE) {
                            var weaponData = getWeaponData(country.getNodeName(), country.getChildNodes());

                            weaponDatas.add(weaponData);
                        }
                    }
                }
                else if (tempNode.hasChildNodes()) {
                    parseXmlWeaponFile(tempNode.getChildNodes(), weaponDatas);
                }
            }
        }
    }

    /**
     * Получение данных об оружии для узла страны.
     * @param country страна.
     * @param weapons узел оружий страны.
     * @return данные об оружии.
     */
    static WeaponData getWeaponData(String country, NodeList weapons) {
        var weaponData = new WeaponData();

        for (int count = 0; count < weapons.getLength(); count++) {
            var weaponType = weapons.item(count);

            if (weaponType.getNodeType() == Node.ELEMENT_NODE) {

                var nodes = weaponType.getChildNodes();

                switch (weaponType.getNodeName()) {
                    case "Pistols":
                        var pistolsList = getPistols(nodes)
                                .stream()
                                .map(x -> {
                                    x.setCountry(country);
                                    return x;})
                                .toList();

                        var pistols = new ArrayList<>(pistolsList);

                        weaponData.setPistols(pistols);
                        break;
                    case "Rifles":
                        var riflesList = getRifles(weaponType.getChildNodes())
                                .stream()
                                .map(x -> {
                                    x.setCountry(country);
                                    return x;})
                                .toList();

                        var rifles = new ArrayList<>(riflesList);

                        weaponData.setRifles(rifles);
                        break;
                }
            }

        }

        return weaponData;
    }

    /**
     * Получение пистолетов.
     * @param pistols узел с пистолетами.
     * @return список пистолетов.
     */
    static ArrayList<Pistol> getPistols(NodeList pistols) {
        var resultPistols = new ArrayList<Pistol>();

        for (int count = 0; count < pistols.getLength(); count++) {
            Node pistolNode = pistols.item(count);

            if (pistolNode.getNodeType() == Node.ELEMENT_NODE) {
                var pistolFields = pistolNode.getChildNodes();

                var pistol = new Pistol();

                for (int i = 0; i < pistolFields.getLength(); i++) {
                    var pistolField = pistolFields.item(i);

                    switch (pistolField.getNodeName()) {
                        case "Name":
                            pistol.setName(pistolField.getTextContent());
                            break;
                        case "Calibre":
                            pistol.setCalibre(Double.parseDouble(pistolField.getTextContent()));
                            break;
                        case "Magazine":
                            pistol.setMagazine(Integer.parseInt(pistolField.getTextContent()));
                            break;
                    }
                }

                resultPistols.add(pistol);
            }
        }

        return resultPistols;
    }

    /**
     * Получение винтовок.
     * @param rifles узел с винтовками.
     * @return список винтовок.
     */
    static ArrayList<Rifle> getRifles(NodeList rifles) {
        var resultRifles = new ArrayList<Rifle>();

        for (int count = 0; count < rifles.getLength(); count++) {
            Node rifleNode = rifles.item(count);

            if (rifleNode.getNodeType() == Node.ELEMENT_NODE) {
                var rifleFields = rifleNode.getChildNodes();

                var rifle = new Rifle();

                for (int i = 0; i < rifleFields.getLength(); i++) {
                    var rifleField = rifleFields.item(i);

                    switch (rifleField.getNodeName()) {
                        case "Name":
                            rifle.setName(rifleField.getTextContent());
                            break;
                        case "Calibre":
                            rifle.setCalibre(Double.parseDouble(rifleField.getTextContent()));
                            break;
                        case "Magazine":
                            rifle.setMagazine(Integer.parseInt(rifleField.getTextContent()));
                            break;
                    }
                }

                resultRifles.add(rifle);
            }
        }

        return resultRifles;
    }
}
