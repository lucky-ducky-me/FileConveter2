package Weapon;

import lombok.Data;

import java.util.ArrayList;

/**
 * Данные об оружии.
 */
@Data
public class WeaponData {
    /**
     * Пистолеты.
     */
    ArrayList<Pistol> pistols;

    /**
     * Винтовки.
     */
    ArrayList<Rifle> rifles;

    public void WeaponData(ArrayList<Pistol> pistols, ArrayList<Rifle> rifles) {
        this.pistols = new ArrayList<>(pistols);
        this.rifles = new ArrayList<>(rifles);
    }
}
