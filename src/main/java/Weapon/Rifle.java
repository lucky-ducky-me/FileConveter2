package Weapon;

import lombok.Data;

/**
 * Винтовка.
 */
@Data
public class Rifle {
    /**
     * Название.
     */
    String name;

    /**
     * Калибр.
     */
    double calibre;

    /**
     * Страна-создатель.
     */
    String country;

    /**
     * Ёмкость магазина.
     */
    int magazine;
}
