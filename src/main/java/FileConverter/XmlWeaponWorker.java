package FileConverter;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.javatuples.Pair;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import Weapon.Pistol;
import Weapon.Rifle;
import Weapon.WeaponData;

/**
 * Работа с xml.
 */
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

    /**
     * Записать данные об оружии в xml файл.
     * @param fileLocation расположение файла.
     * @param weaponData данные об оружии.
     */
    public static void writeWeaponData(String fileLocation, WeaponData weaponData) throws ParserConfigurationException, IOException {
        var docFactory = DocumentBuilderFactory.newInstance();
        var docBuilder = docFactory.newDocumentBuilder();

        var doc = docBuilder.newDocument();
        var rootElement = doc.createElement("Firearms");
        doc.appendChild(rootElement);

        var countries = doc.createElement("Countries");

        rootElement.appendChild(countries);

        var transformedWeaponData = getTransformedWeaponData(weaponData);

        for (var key : transformedWeaponData.keySet()) {

            var countryNode = doc.createElement(key);

            var pistols = transformedWeaponData.get(key).getValue0();

            var rifles = transformedWeaponData.get(key).getValue1();

            writePistols(doc, countryNode, pistols);

            writeRifles(doc, countryNode, rifles);

            countries.appendChild(countryNode);
        }

        if (!new File(fileLocation).exists()) {
            new File(fileLocation).createNewFile();
        }

        try (var output = new FileOutputStream(fileLocation)) {

            writeXmlInFile(doc, output);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TransformerConfigurationException e) {
            throw new RuntimeException(e);
        } catch (TransformerException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * Запись в файл "документа".
     * @param doc "документ".
     * @param output файловый поток.
     */
    private static void writeXmlInFile(Document doc, OutputStream output) throws TransformerException {
        var transformerFactory = TransformerFactory.newInstance();
        var transformer = transformerFactory.newTransformer();

        transformer.setOutputProperty(OutputKeys.INDENT, "yes");

        var source = new DOMSource(doc);
        var result = new StreamResult(output);

        transformer.transform(source, result);
    }

    /**
     * Получить трансформированные для записи в xml данные об оружии.
     * @param weaponData данные об оружии.
     * @return трансформированные данные.
     */
    private static HashMap<String, Pair<ArrayList<Pistol>, ArrayList<Rifle>>> getTransformedWeaponData(WeaponData weaponData) {
        var countries = getCountries(weaponData);

        var result = new HashMap<String, Pair<ArrayList<Pistol>, ArrayList<Rifle>>>();

        for (var i = 0; i < countries.size(); i++) {
            var pistols = new ArrayList<Pistol>();

            for (var j = 0; j < weaponData.getPistols().size(); j++) {
                if (weaponData.getPistols().get(j).getCountry().equals(countries.get(i))) {
                    pistols.add(weaponData.getPistols().get(j));
                }
            }

            var rifles = new ArrayList<Rifle>();

            for (var j = 0; j < weaponData.getRifles().size(); j++) {
                if (weaponData.getRifles().get(j).getCountry().equals(countries.get(i))) {
                    rifles.add(weaponData.getRifles().get(j));
                }
            }

            result.put(countries.get(i), Pair.with(pistols, rifles));
        }

        return result;
    }

    /**
     * Получить все страны.
     * @param weaponData данные об оружие.
     * @return Список стран.
     */
    private static ArrayList<String> getCountries(WeaponData weaponData) {
        var countries = new ArrayList<String>();

        for (var i = 0; i < weaponData.getPistols().size(); i++) {
            var country = weaponData.getPistols().get(i).getCountry();

            if (!countries.contains(country)) {
                countries.add(country);
            }
        }

        for (var i = 0; i < weaponData.getRifles().size(); i++) {
            var country = weaponData.getRifles().get(i).getCountry();

            if (!countries.contains(country)) {
                countries.add(country);
            }
        }

        return countries;
    }

    /**
     * Запись пистолетов в xml узел.
     * @param doc xml "документ".
     * @param countryNode узел страны.
     * @param pistols список пистолетов.
     */
    private static void writePistols(Document doc, Element countryNode, ArrayList<Pistol> pistols) {
        var pistolsNode = doc.createElement("Pistols");

        for (var pistol: pistols) {
            var pistolNode = doc.createElement("Pistol");

            var nameNode = doc.createElement("Name");

            nameNode.setTextContent(pistol.getName());

            var calibreNode = doc.createElement("Calibre");

            calibreNode.setTextContent(Double.toString(pistol.getCalibre()));

            var magazineNode = doc.createElement("Magazine");

            magazineNode.setTextContent(Integer.toString(pistol.getMagazine()));

            pistolNode.appendChild(nameNode);
            pistolNode.appendChild(calibreNode);
            pistolNode.appendChild(magazineNode);

            pistolsNode.appendChild(pistolNode);
        }

        countryNode.appendChild(pistolsNode);
    }

    /**
     * Запись винтовок в xml узел.
     * @param doc xml "документ".
     * @param countryNode узел страны.
     * @param rifles список винтовок.
     */
    private static void writeRifles(Document doc, Element countryNode, ArrayList<Rifle> rifles) {
        var riflesNode = doc.createElement("Rifles");

        for (var rifle: rifles) {
            var rifleNode = doc.createElement("Rifle");

            var nameNode = doc.createElement("Name");

            nameNode.setTextContent(rifle.getName());

            var calibreNode = doc.createElement("Calibre");

            calibreNode.setTextContent(Double.toString(rifle.getCalibre()));

            var magazineNode = doc.createElement("Magazine");

            magazineNode.setTextContent(Integer.toString(rifle.getMagazine()));

            rifleNode.appendChild(nameNode);
            rifleNode.appendChild(calibreNode);
            rifleNode.appendChild(magazineNode);

            riflesNode.appendChild(rifleNode);
        }

        countryNode.appendChild(riflesNode);
    }
}