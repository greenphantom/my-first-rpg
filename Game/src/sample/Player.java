package sample;

import java.lang.Math;
import java.awt.Point;
import javafx.scene.image.Image;

public class Player extends GameObject {
    private int Health;
    private int AttackPoints;
    private int DefensePoints;
    private int Experience;
    private int ExpToNextLvl;
    private int Level;
    private boolean isDead;
    private Image playerSprite;
    private String name;
    private Inventory bag;
    private Equipment[] gear;
    private final int EQUIPMENT_SLOTS = 3;
    private Point pos;
    public Occupy occupy;
    public Player(String name, int x, int y){
        this.name = name;
        Level = 1;
        ExpToNextLvl = 100;
        Experience = 0;
        Health = 100;
        AttackPoints = 5;
        DefensePoints = 5;
        bag = new Inventory();
        pos = new Point(x,y);
        gear = new Equipment[]{new Armor(1, 100, 0), new Weapon(2, 200, 0), new Ring(3, 300, 0)};
    }
    public Player(){
        this.name = "H Y P E - B O Y";
        Level = 1;
        ExpToNextLvl = 100;
        Experience = 0;
        Health = 100;
        AttackPoints = 5;
        DefensePoints = 5;
        bag = new Inventory();
        pos = new Point(0,0);
        gear = new Equipment[]{new Armor(1, 100, 0), new Weapon(2, 200, 0), new Ring(3, 300, 0)};
    }

    public Equipment[] getEquipment(){
        return gear;
    }

    public int getHealth() {
        return Health;
    }

    public int getExperience(){
        return Experience;
    }

    public void setHealth(int health) {
        this.Health = health;
    }

    // May be unnecessary
    public void setExperience(int experience) {
        this.Experience = experience;
    }

    public void gainExp(int exp) {
        if (exp >= ExpToNextLvl)
        {
            LevelUp();
            exp -= ExpToNextLvl;
            ExpToNextLvl = Level * 100;
            this.gainExp(exp);
        }
        else {
            ExpToNextLvl -= exp;
            Experience = exp;
        }
    }

    public void acquireItem(Item i) {
        bag.addItem(i);
    }

    public void takeDamage(int damage) {
        int dmg = (DefensePoints - damage);
        if (dmg <= 0) { Health -= 1; }
        else Health -= dmg;
    }

    public void heal(int hpIncrease){
        if (hpIncrease > 0) {
            if (Level == 1){
                if (hpIncrease + Health <= 100) {
                    Health += hpIncrease;
                }
                else {
                    Health = 100;
                }
            }
            else if (hpIncrease + Health <= (int)Math.log10((double)(Level*100)) + (int)1.247*(Level * 100)) {
                Health += hpIncrease;
            }
            else {
                Health = (int)Math.log10((double)(Level*100)) + (int)1.247*(Level * 100);
            }
        }
    }

    // Slot 0 = Armor, Slot 1 = Weapon, Slot 2 = Ring
    public void equipGear(Equipment swag) {
        int EquipSlot = swag.getEquipmentID()/100 - 1;
        if (EquipSlot == 0) { // Meaning the player equipped an Armor Piece
            DefensePoints -= gear[EquipSlot].supplyBenefit();
            DefensePoints += swag.supplyBenefit();
        }
        else if (EquipSlot == 1) { // Meaning the player equipped a Weapon
            AttackPoints -= gear[EquipSlot].supplyBenefit();
            AttackPoints += swag.supplyBenefit();
        }
        else if (EquipSlot == 2) { // Meaning the player equipped a Ring
            Health -= gear[EquipSlot].supplyBenefit();
            Health += swag.supplyBenefit();
        }
        bag.addItem(gear[EquipSlot]);
        gear[EquipSlot] = swag;
    }

    private void LevelUp()
    {
        Level++;
        Health = (int)Math.log10((double)(Level*100)) + (int)1.247*(Level * 100);
        AttackPoints = (int)(Math.log10((double)(Level))*5)+AttackPoints;
        DefensePoints = (int)(Math.log10((double)(Level))*2.5)+DefensePoints;
        // Increase other stats here as needed.
    }

    public int getLevel() {
        return Level;
    }

    public void setLevel(int Level){
        this.Level = Level;
    }

    public Inventory getInventory(){
        return bag;
    }

    public Point getPosition()
    {
        return pos;
    }

    public void setPosition(int x, int y) { pos.setLocation(x,y); }

    public int getExpToNextLvl() {
        return ExpToNextLvl;
    }

    public void setExpToNextLvl(int expToNextLvl) {
        ExpToNextLvl = expToNextLvl;
    }

    public int getAttackPoints() {
        return AttackPoints;
    }

    public void setAttackPoints(int attackPoints) {
        AttackPoints = attackPoints;
    }

    public int getDefensePoints() {
        return DefensePoints;
    }

    public void setDefensePoints(int defensePoints) {
        DefensePoints = defensePoints;
    }

    public String getName() {
        return name;
    }

    public void setName(String newName) {
        name = newName;
    }

    public Image getPlayerSprite() { return playerSprite; }


    public int getMaxHealth() {
        if (getLevel() == 1) {
            return 100;
        } else {
            return (int)Math.log10((double)(Level*100)) + (int)1.247*(Level * 100);
        }
    }
    public void setPlayerSprite(Image newSprite) { playerSprite = newSprite; }

    public void setIsDead(boolean bool) { isDead = bool;}

    public boolean getIsDead() { return isDead; }
}
