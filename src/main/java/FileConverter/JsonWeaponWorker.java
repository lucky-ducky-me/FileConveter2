package FileConverter;

import Weapon.Pistol;
import Weapon.Rifle;
import Weapon.WeaponData;
import com.google.gson.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Iterator;

public class JsonWeaponWorker {

    /**
     * Получение данных об оружии из json файла.
     * @param fileLocation местоположение файла.
     * @return данные об оружии.
     */
    public static WeaponData getWeaponData(String fileLocation) {
        var parser = new JSONParser();

        try (Reader reader = new FileReader(fileLocation)) {
            var weaponData = new WeaponData();

            var jsonObject = (JSONObject) parser.parse(reader);

            var firearms = (JSONObject) jsonObject.get("firearms");

            var pistolsJson = (JSONArray) firearms.get("pistols");

            var riflesJson = (JSONArray) firearms.get("rifles");

            var pistols = getPistols(pistolsJson);

            var rifles = getRifles(riflesJson);

            weaponData.setPistols(pistols);
            weaponData.setRifles(rifles);

            return weaponData;

        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Получение пистолетов.
     * @param pistolsJson json объект с пистолетами.
     * @return список пистолетов.
     */
    private static ArrayList<Pistol> getPistols(JSONArray pistolsJson) {
        var pistols = new ArrayList<Pistol>();

        Iterator<JSONObject> iterator = pistolsJson.iterator();

        while (iterator.hasNext()) {
            var pistolJson = iterator.next();

            var name = (String) pistolJson.get("name");
            var caliber = Double.parseDouble((String) pistolJson.get("calibre"));
            var country = (String) pistolJson.get("country");
            var magazine = Integer.parseInt((String) pistolJson.get("magazine"));

            var pistol = new Pistol();

            pistol.setName(name);
            pistol.setCalibre(caliber);
            pistol.setCountry(country);
            pistol.setMagazine(magazine);

            pistols.add(pistol);
        }

        return pistols;
    }

    /**
     * Получение винтовок.
     * @param riflesJson json объект с винтовками.
     * @return список винтовок.
     */
    private static ArrayList<Rifle> getRifles(JSONArray riflesJson) {
        var rifles = new ArrayList<Rifle>();

        Iterator<JSONObject> iterator = riflesJson.iterator();

        while (iterator.hasNext()) {
            var rifleJson = iterator.next();

            var name = (String) rifleJson.get("name");
            var caliber = Double.parseDouble((String) rifleJson.get("calibre"));
            var country = (String) rifleJson.get("country");
            var magazine = Integer.parseInt((String) rifleJson.get("magazine"));

            var rifle = new Rifle();

            rifle.setName(name);
            rifle.setCalibre(caliber);
            rifle.setCountry(country);
            rifle.setMagazine(magazine);

            rifles.add(rifle);
        }

        return rifles;
    }

    /**
     * Запись данных об оружии в json файл.
     * @param fileLocation местоположение файла.
     * @param weaponData данные об оружии.
     */
    public static void writeWeaponData(String fileLocation, WeaponData weaponData) {
        var obj = new JSONObject();

        var firearms = new JSONObject();

        var pistols = new JSONArray();

        for (var pistol: weaponData.getPistols()) {
            var pistolJson = new JSONObject();

            pistolJson.put("name", pistol.getName());
            pistolJson.put("calibre", Double.toString(pistol.getCalibre()));
            pistolJson.put("magazine", Integer.toString(pistol.getMagazine()));
            pistolJson.put("country", pistol.getCountry());

            pistols.add(pistolJson);
        }

        var rifles = new JSONArray();

        for (var rifle : weaponData.getRifles()) {
            var rifleJson = new JSONObject();

            rifleJson.put("name", rifle.getName());
            rifleJson.put("calibre", Double.toString(rifle.getCalibre()));
            rifleJson.put("magazine", Integer.toString(rifle.getMagazine()));
            rifleJson.put("country", rifle.getCountry());

            rifles.add(rifleJson);
        }

        firearms.put("pistols", pistols);

        firearms.put("rifles", rifles);

        obj.put("firearms", firearms);

        try (FileWriter file = new FileWriter(fileLocation)) {
            var jsonWeaponUnpretty = obj.toJSONString();

            var gson = new GsonBuilder().setPrettyPrinting().create();
            var je = JsonParser.parseString(jsonWeaponUnpretty);
            var jsonWeaponPretty = gson.toJson(je);

            file.write(jsonWeaponPretty);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

