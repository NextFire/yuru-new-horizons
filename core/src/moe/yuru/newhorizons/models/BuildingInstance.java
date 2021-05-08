package moe.yuru.newhorizons.models;

/**
 * A {@link Building} and supplemental attributes once it has been built by the
 * player.
 * 
 * @author NextFire
 */
public class BuildingInstance {

    private Building model;
    private int level;

    private float posX;
    private float posY;

    public BuildingInstance() {
    }

    /**
     * @param model of the instance
     * @param posX  on screen (horizontally)
     * @param posY  on screen (vertically)
     */
    public BuildingInstance(Building model, float posX, float posY) {
        this.model = model;
        level = 1;
        this.posX = posX;
        this.posY = posY;
    }

    /**
     * Shortcut to {@code this.getModel().getStats(this.getLevel())}.
     * 
     * @return the building stats for this instance current level
     */
    public BuildingStats getStats() {
        return model.getStats(level);
    }

    /**
     * @return this instance {@link Building} model
     */
    public Building getModel() {
        return model;
    }

    /**
     * @return this instance current level
     */
    public int getLevel() {
        return level;
    }

    /**
     * Level up this building.
     */
    public void levelUp() {
        level++;
    }

    /**
     * @return this instance horizontal position
     */
    public float getPosX() {
        return posX;
    }

    /**
     * @param posX a new horizontal position
     */
    public void setPosX(float posX) {
        this.posX = posX;
    }

    /**
     * @return this instance vertical position
     */
    public float getPosY() {
        return posY;
    }

    /**
     * @param posY a new vertical position
     */
    public void setPosY(float posY) {
        this.posY = posY;
    }

}
