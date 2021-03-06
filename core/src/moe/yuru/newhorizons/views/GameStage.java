package moe.yuru.newhorizons.views;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ObjectMap;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;

import moe.yuru.newhorizons.YuruNewHorizons;
import moe.yuru.newhorizons.models.Faction;
import moe.yuru.newhorizons.utils.AssetHelper;
import moe.yuru.newhorizons.utils.Event;
import moe.yuru.newhorizons.utils.EventType;
import moe.yuru.newhorizons.utils.GameModelSave;
import moe.yuru.newhorizons.utils.Listener;

/**
 * Contains the town map layout, town stats at the upper right and menus on the
 * bottom right. It listens to the game model to dynamically modify menu
 * buttons.
 * 
 * @author NextFire
 * @author DinoGurnari
 */
public class GameStage extends Stage implements Listener {

    private YuruNewHorizons game;

    private Texture mapTexture;

    private VisLabel coinsLabel;
    private VisLabel coinsPerSecondLabel;

    private ObjectMap<Faction, VisLabel> resourcesLabels;
    private ObjectMap<Faction, VisLabel> perSecondLabels;

    private VisLabel populationLabel;
    private VisLabel populationPerSecondLabel;

    private VisLabel happinessLabel;

    private VisLabel coinsLabelVs;
    private VisLabel scienceLabelVs;
    private VisLabel cultureLabelVs;
    private VisLabel industryLabelVs;
    private VisLabel politicsLabelVs;

    private VisTable menuTable;

    /**
     * @param game the game instance
     */
    public GameStage(YuruNewHorizons game) {
        super(game.getViewport(), game.getBatch());
        this.game = game;

        // Register to the game model
        game.getGameModel().addListener(this);

        // Add map actor to the stage
        mapTexture = AssetHelper.getTownMapTexture(game.getGameModel());
        TextureRegion mapTextureRegion = new TextureRegion(mapTexture, 480, 375, 1680, 1375);
        Image mapImage = new Image(mapTextureRegion);
        mapImage.setBounds(0, 144, 768, 576);
        addActor(mapImage);

        // A full height table just at the right of the map
        VisTable rightTable = new VisTable();
        addActor(rightTable);
        rightTable.setSize(512, 720);
        rightTable.setPosition(768, 0);
        rightTable.defaults().grow().pad(10);

        // Stats table at the top of rightTable
        VisTable statsTable = new VisTable(true);
        rightTable.add(statsTable);
        statsTable.top();
        statsTable.columnDefaults(0).left();
        statsTable.columnDefaults(1).expandX();

        // Labels whose going to display town balance
        statsTable.add(new VisLabel("Coins"));
        coinsLabel = new VisLabel("");
        statsTable.add(coinsLabel);
        coinsPerSecondLabel = new VisLabel("");
        statsTable.add(coinsPerSecondLabel);

        resourcesLabels = new ObjectMap<>();
        perSecondLabels = new ObjectMap<>();
        for (Faction faction : Faction.values()) {
            statsTable.row();
            statsTable.add(new VisLabel(faction.toString()));

            resourcesLabels.put(faction, new VisLabel(""));
            statsTable.add(resourcesLabels.get(faction));

            perSecondLabels.put(faction, new VisLabel(""));
            statsTable.add(perSecondLabels.get(faction));
        }

        // Population
        statsTable.row();
        statsTable.add(new VisLabel("Population"));
        populationLabel = new VisLabel("");
        statsTable.add(populationLabel);
        populationPerSecondLabel = new VisLabel("");
        statsTable.add(populationPerSecondLabel);

        // Happiness
        statsTable.row();
        statsTable.add(new VisLabel("Happiness"));
        happinessLabel = new VisLabel("");
        statsTable.add(happinessLabel);

        if (!game.getGameModel().isSolo()) {
            // Enemy Stats table at the top of rightTable

            rightTable.row();
            VisTable statsTableVs = new VisTable(true);
            rightTable.add(statsTableVs);
            statsTableVs.top();
            statsTableVs.columnDefaults(0).left();
            statsTableVs.columnDefaults(1).expandX();

            statsTableVs.add(new VisLabel("Enemy's Stats"));
            statsTableVs.row();

            // Labels whose going to display enemy's town balance
            statsTableVs.add(new VisLabel("Coins"));
            coinsLabelVs = new VisLabel("");
            statsTableVs.add(coinsLabelVs);
            statsTableVs.row();
            statsTableVs.add(new VisLabel("Science"));
            scienceLabelVs = new VisLabel("");
            statsTableVs.add(scienceLabelVs);
            statsTableVs.row();
            statsTableVs.add(new VisLabel("Culture"));
            cultureLabelVs = new VisLabel("");
            statsTableVs.add(cultureLabelVs);
            statsTableVs.row();
            statsTableVs.add(new VisLabel("Industry"));
            industryLabelVs = new VisLabel("");
            statsTableVs.add(industryLabelVs);
            statsTableVs.row();
            statsTableVs.add(new VisLabel("Politics"));
            politicsLabelVs = new VisLabel("");
            statsTableVs.add(politicsLabelVs);
        }

        // Menu table at the bottom of the right table
        rightTable.row();
        menuTable = new VisTable(true);
        rightTable.add(menuTable);
        menuTable.bottom();
        menuTable.defaults().growX().height(30);

        // Adding buttons to the menu table
        VisTextButton constructButton = new VisTextButton("Construct");
        menuTable.add(constructButton);

        constructButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                game.setScreen(new StockScreen(game)); // new construction screen
            }
        });

        menuTable.row();
        VisTextButton saveButton = new VisTextButton("Save");
        menuTable.add(saveButton);

        saveButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                GameModelSave.save(game.getGameModel());
            }
        });

        menuTable.row();
        VisTextButton exitButton = new VisTextButton("Exit");
        menuTable.add(exitButton);

        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                // kill game screen and model then return to main menu
                game.getScreen().dispose();
                game.setGameModel(null);
                game.setGameScreen(null);
                game.setScreen(new MainMenuScreen(game));
            }
        });
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        // Update labels according to the town balance
        // Coins
        coinsLabel.setText(String.valueOf((int) game.getGameModel().getTownCoins()));

        if (!game.getGameModel().isSolo()) {
            // Update labels according to the opponent balance
            coinsLabelVs.setText(String.valueOf((int) game.getGameModel().getOpponentCoins()));
            scienceLabelVs.setText(String.valueOf((int) game.getGameModel().getOpponentResources(Faction.SCIENCE)));
            cultureLabelVs.setText(String.valueOf((int) game.getGameModel().getOpponentResources(Faction.CULTURE)));
            industryLabelVs.setText(String.valueOf((int) game.getGameModel().getOpponentResources(Faction.INDUSTRY)));
            politicsLabelVs.setText(String.valueOf((int) game.getGameModel().getOpponentResources(Faction.POLITICS)));
        }

        coinsPerSecondLabel.setText("(+" + game.getGameModel().getCoinsPerSecond() + ")");

        // Faction resources
        for (Faction faction : Faction.values()) {
            resourcesLabels.get(faction).setText(String.valueOf((int) game.getGameModel().getTownResources(faction)));
            perSecondLabels.get(faction).setText("(+" + game.getGameModel().getResourcesPerSecond(faction) + ")");
        }

        // Population
        populationLabel.setText((int) game.getGameModel().getPopulation() + "/" + game.getGameModel().getHouses());
        populationPerSecondLabel
                .setText("(+" + (int) (game.getGameModel().getPopulationPerSecond() * 100) / 100f + ")");
        // (Would have used String.format but it does not work on html)

        // Happiness
        happinessLabel.setText(String.valueOf(game.getGameModel().getHappiness()));
    }

    @Override
    public void dispose() {
        super.dispose();
        mapTexture.dispose();
    }

    @Override
    public void processEvent(Event event) {
        // Hide menuTable if a building is currently placed
        if (event.getSource() == game.getGameModel() && event.getType() == EventType.Construction.TO_PLACE) {
            if (event.getValue() != null) {
                menuTable.setVisible(false);
            } else {
                menuTable.setVisible(true);
            }
        }
    }

}
