package moe.yuru.newhorizons.views;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.ObjectMap;

import moe.yuru.newhorizons.YuruNewHorizons;
import moe.yuru.newhorizons.models.BuildingInstance;
import moe.yuru.newhorizons.utils.AssetHelper;
import moe.yuru.newhorizons.utils.Event;
import moe.yuru.newhorizons.utils.EventType;
import moe.yuru.newhorizons.utils.Listener;

/**
 * Contains all the town buildings buttons. Listens to the game model for new
 * buildings.
 * 
 * @author NextFire
 * @author DinoGurnari
 */
public class MapStage extends Stage implements Listener {

    private YuruNewHorizons game;
    private Sound sound;
    private ObjectMap<BuildingInstance, BuildingButton> buttons;

    /**
     * Creates a new MapStage and initializes already built buildings. It also
     * registers itself to the game model.
     * 
     * @param game the game instance
     */
    public MapStage(YuruNewHorizons game) {
        super(game.getViewport(), game.getBatch());
        this.game = game;

        // Initialization
        buttons = new ObjectMap<>();
        for (BuildingInstance instance : game.getGameModel().getTownBuildings()) {
            addInstanceActor(instance);
        }
        sound = null;

        // Registering
        game.getGameModel().addListener(this);
    }

    @Override
    public void dispose() {
        super.dispose();
        if (sound != null) {
            sound.dispose();
        }
        BuildingButton.disposeAll();
    }

    @Override
    public void processEvent(Event event) {
        if (event.getSource() == game.getGameModel()) {
            // If a new building has been registered and validated by the model, call
            // addInstanceActor to add the newly missing button on stage.
            if (event.getType() == EventType.Construction.VALIDATED) {
                BuildingInstance instance = (BuildingInstance) event.getValue();
                addInstanceActor(instance);
                playInstanceSound(instance);
            }

            // If a building has been leveled up and validated by the model
            // modify the button on stage.
            else if (event.getType() == EventType.Construction.LEVELED_UP) {
                BuildingInstance instance = (BuildingInstance) event.getValue();
                buttons.get(instance).updateLevel();
                playInstanceSound(instance);
            }
        }
    }

    /**
     * @param instance
     */
    private void playInstanceSound(BuildingInstance instance) {
        if (sound != null) {
            // dispose the old one
            sound.dispose();
        }
        sound = AssetHelper.getCharaSound(instance.getModel());
        sound.play(game.getSoundVolume());
    }

    /**
     * Adds the actor for the given {@link BuildingInstance} to the map.
     * 
     * @param instance a town {@link BuildingInstance}
     */
    private void addInstanceActor(BuildingInstance instance) {
        // Place a new button where it must be placed
        BuildingButton button = new BuildingButton(instance);
        buttons.put(instance, button);
        addActor(button);

        // Add a controller to switch to the building screen when clicking on the button
        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (sound != null) {
                    sound.stop();
                }
                game.setScreen(new BuildingScreen(game, instance));
            }
        });
    }

}
