//Exists to hold information for the Area Effects that will happen in the tiles
//Members responsible: Callum, Daniel, Danilo

package sample;

public class AreaEffect extends tileObject{
    private int scenarioNumber;
    private float duration;

    //Sets the default values for the global variables
    public AreaEffect(int scenario){
      duration = 10f;
      super(scenario);
    }

    //Allows for us to use durationin other classes while keeping it private
    public float getDuration(){
      return this.duration;
    }

    public void setDuration(float duration) {
        this.duration = duration;
    }


    public int getScenarioNumber() {
        return scenarioNumber;
    }

    public void setScenarioNumber(int scenarioNumber) {
        this.scenarioNumber = scenarioNumber;
    }
}
