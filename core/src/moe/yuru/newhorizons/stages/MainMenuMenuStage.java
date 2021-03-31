package moe.yuru.newhorizons.stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kotcrab.vis.ui.widget.VisTextButton;

import moe.yuru.newhorizons.YuruNewHorizons;
import moe.yuru.newhorizons.screens.GameScreen;
import moe.yuru.newhorizons.screens.MainMenuScreen;

/**
 * Main menu right buttons menu {@link Stage}.
 */
public class MainMenuMenuStage extends Stage {

    /**
     * @param game the game instance
     */
    public MainMenuMenuStage(YuruNewHorizons game) {
        super(game.getViewport(), game.getBatch());

        Button startButton = new VisTextButton("New Game");
        startButton.setSize(400, 75);
        startButton.setPosition(690, 450);
        addActor(startButton);

        Button ResumeButton = new VisTextButton("Resume Game");
        ResumeButton.setSize(400, 75);
        ResumeButton.setPosition(690, 325);
        addActor(ResumeButton);

        Button OptionsButton = new VisTextButton("Options");
        OptionsButton.setSize(400, 75);
        OptionsButton.setPosition(690, 200);
        addActor(OptionsButton);

        Button exitButton = new VisTextButton("Quit");
        exitButton.setSize(400, 75);
        exitButton.setPosition(690, 75);
        addActor(exitButton);

        startButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.getScreen().dispose();
                game.setScreen(new GameScreen(game));
            }
        });

        ResumeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.getScreen().dispose();
                game.setScreen(new MainMenuScreen(game)); // A changer évidemment...
            }
        });

        OptionsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.getScreen().dispose();
                game.setScreen(new MainMenuScreen(game)); // A changer évidemment...
            }
        });

        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });
    }

}