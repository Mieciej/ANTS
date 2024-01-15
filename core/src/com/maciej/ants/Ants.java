package com.maciej.ants;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;

import java.security.Key;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Main class which is responsible for showing GUI of the program.
 */
public class Ants extends ApplicationAdapter {
	SpriteBatch batch;
	private Texture nodeTexture;
	private Texture antTexture;
	private HashMap<Node,Sprite> verticesSprites;
	OrthographicCamera camera;
	ShapeRenderer shapeRenderer;
	private Stage stage;
	private Table descriptionPopup;
	private Label description;
	private Skin skin ;
	Vector2[] edgeSources;
	Vector2[] edgeEnds;
	private float screenBoundX;
	private float screenBoundY;
	private TextButton buttonWorker;
	private TextButton buttonDrone;
	private TextButton buttonSoldier;
	private TextButton buttonCollector;
	private TextButton buttonBlunderer;
	private Sprite antSprite;
	private TextButton buttonRemoveAnt;
	private Ant selectedAnt;

	/**
	 * Run at the start of the game.
	 */
	@Override
	public void create () {
		// Load assets
		skin = new Skin(Gdx.files.internal("commodore64/skin/uiskin.json"));
		nodeTexture = new Texture("node.png");
		antTexture = new Texture("ant.png");
		// Initialise renderers
		batch = new SpriteBatch();
		shapeRenderer = new ShapeRenderer();
		// Create Camera
		camera = new OrthographicCamera();
		camera.setToOrtho(false,960,480);
		screenBoundX = camera.viewportWidth;
		screenBoundY = camera.viewportHeight -   120;
		// Initialise UI elements
		stage = new Stage();
		descriptionPopup = new Table(skin);
		descriptionPopup.setFillParent(true);
		descriptionPopup.top().left();
		stage.addActor(descriptionPopup);
		description = new Label("CLICK ON ANT/LOCATION TO DISPLAY INFORMATION.",skin);
		buttonWorker = new TextButton("W",skin);
		buttonWorker.setColor(Color.RED);
		buttonWorker.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent changeEvent, Actor actor) {
				WorldManager.worldManager().createAnt("WORKER",WorldManager.worldManager().getAnthills().get("RED"));
			}
		});
		buttonDrone = new TextButton("D",skin);
		buttonDrone.setColor(Color.RED);
		buttonDrone.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent changeEvent, Actor actor) {
				WorldManager.worldManager().createAnt("DRONE",WorldManager.worldManager().getAnthills().get("RED"));
			}
		});
		buttonSoldier = new TextButton("S",skin);
		buttonSoldier.setColor(Color.BLUE);
		buttonSoldier.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent changeEvent, Actor actor) {
				WorldManager.worldManager().createAnt("SOLDIER",WorldManager.worldManager().getAnthills().get("BLUE"));
			}
		});
		buttonCollector = new TextButton("C",skin);
		buttonCollector.setColor(Color.BLUE);
		buttonCollector.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent changeEvent, Actor actor) {
				WorldManager.worldManager().createAnt("COLLECTOR",WorldManager.worldManager().getAnthills().get("BLUE"));
			}
		});
		buttonBlunderer = new TextButton("B",skin);
		buttonBlunderer.setColor(Color.BLUE);
		buttonBlunderer.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent changeEvent, Actor actor) {
				WorldManager.worldManager().createAnt("BLUNDERER",WorldManager.worldManager().getAnthills().get("BLUE"));
			}
		});

		description.setFontScale(0.7f);
		buttonRemoveAnt = new TextButton("X",skin);
		buttonRemoveAnt.setVisible(false);
		buttonRemoveAnt.setColor(Color.BLACK);
		buttonRemoveAnt.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent changeEvent, Actor actor) {
				if(selectedAnt!=null)
					selectedAnt.setExit(true);
			}
		});
		descriptionPopup.add(description).padTop(5).padLeft(5).padRight(75).width(screenBoundX-475);
		descriptionPopup.add(buttonRemoveAnt).top().right().padTop(5);
		descriptionPopup.add(buttonWorker).top().right().padTop(5);
		descriptionPopup.add(buttonDrone).top().right().padTop(5);
		descriptionPopup.add(buttonSoldier).top().right().padTop(5);
		descriptionPopup.add(buttonCollector).top().right().padTop(5);
		descriptionPopup.add(buttonBlunderer).top().right().padTop(5);
		Gdx.input.setInputProcessor(stage);
		// Initialise World
		WorldManager.initialiseWorld(50,0);
		// Generate and store vertices sprites
		verticesSprites = new HashMap<>();
		for (Node vertex :
				WorldManager.worldManager().getGraph().getVertices()) {
			int i = verticesSprites.size();
			Sprite newVertexSprite= new Sprite(nodeTexture,nodeTexture.getWidth(),nodeTexture.getHeight());
			if(vertex == WorldManager.worldManager().getAnthills().get("RED"))
				newVertexSprite.setColor(Color.RED);
			else if(vertex == WorldManager.worldManager().getAnthills().get("BLUE"))
				newVertexSprite.setColor(Color.BLUE);
			else
			{
				switch (vertex.getNodeType())	{
					case "STONE":
						newVertexSprite.setColor(Color.LIGHT_GRAY);
						break;
					case "LEAF":
						newVertexSprite.setColor(Color.GREEN);
						break;
				}
			}
			Vector2 newVertexPos = new Vector2(vertex.getAbstractPosition().x*screenBoundX,vertex.getAbstractPosition().y*screenBoundY);
			newVertexSprite.setPosition(newVertexPos.x, newVertexPos.y);
			verticesSprites.put(vertex,newVertexSprite);
		}
		// Add edges to the graph
		ArrayList<AbstractMap.SimpleEntry<Node,Node>> edges = WorldManager.worldManager().getGraph().getEdges();
		edgeSources = new Vector2[edges.size()];
		edgeEnds = new Vector2[edges.size()];
		for (int j = 0; j < edges.size(); j++) {
			AbstractMap.SimpleEntry<Node,Node> edge = edges.get(j);
			Vector2 edgeSource = new Vector2(verticesSprites.get(edge.getKey()).getX() + verticesSprites.get(edge.getKey()).getWidth()/2, verticesSprites.get(edge.getKey()).getY() + verticesSprites.get(edge.getKey()).getHeight()/2);
			Vector2 edgeEnd = new Vector2(verticesSprites.get(edge.getValue()).getX() + verticesSprites.get(edge.getValue()).getWidth()/2, verticesSprites.get(edge.getValue()).getY() + verticesSprites.get(edge.getValue()).getHeight()/2);
			edgeSources[j] = edgeSource;
			edgeEnds[j] = edgeEnd;
		}
		antSprite = new Sprite(antTexture,antTexture.getWidth(),antTexture.getHeight());
	}
	private Object selected = null;

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width,height,true);
	}

	/**
	 * Executed every fame.
	 */
	@Override
	public void render () {
		ScreenUtils.clear(Color.DARK_GRAY);
		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
		camera.update();
		for (int i = 0; i < edgeSources.length; i++) {
			shapeRenderer.setColor(Color.BROWN);
			shapeRenderer.setProjectionMatrix(camera.combined);
			Vector2 source = edgeSources[i];
			Vector2 end = edgeEnds[i];
			shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
			shapeRenderer.rectLine(source,end,3);
			shapeRenderer.end();
		}
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		for (Sprite sprite :
				verticesSprites.values()) {
			sprite.draw(batch);
		}
		for (Ant ant : WorldManager.worldManager().getAnts()) {
			switch (ant.getTeam()){
				case "RED":
					antSprite.setColor(Color.RED);
					break;
				case "BLUE":
					antSprite.setColor(Color.BLUE);
					break;
			}
			antSprite.setPosition(ant.getAbstractPosition().x*screenBoundX + antSprite.getWidth()/2,ant.getAbstractPosition().y*screenBoundY + antSprite.getHeight()/2);
			antSprite.draw(batch);
		}
		batch.end();
		for (Node vertex : WorldManager.worldManager().getGraph().getVertices()) {
			vertex.update();
		}
		boolean clicked = Gdx.input.isButtonJustPressed(Input.Buttons.LEFT) ;
		boolean s = false;
		for(Node vertex: verticesSprites.keySet()){
			if(clicked){
				Vector3 clickPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(),0);
				Rectangle rect = verticesSprites.get(vertex).getBoundingRectangle();
				camera.unproject(clickPos);
				if(rect.contains(clickPos.x,clickPos.y)){
					selected = vertex;
					s = true;
				}
			}
		}
		for (Ant ant: WorldManager.worldManager().getAnts()) {
			if(clicked){
				Vector3 clickPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(),0);
				antSprite.setPosition(ant.getAbstractPosition().x*screenBoundX + antSprite.getWidth()/2,ant.getAbstractPosition().y*screenBoundY + antSprite.getHeight()/2);
				Rectangle rect = antSprite.getBoundingRectangle();
				camera.unproject(clickPos);
				if(rect.contains(clickPos.x,clickPos.y)){
					selected = ant;
					selectedAnt = ant;
					s = true;
				}
			}
		}
		if(selected!=null)
		{
			description.setText(selected.toString());
			if(selected.getClass().toString().equals(Ant.class.toString()) ){
				buttonRemoveAnt.setVisible(true);
			}
			else buttonRemoveAnt.setVisible(false);
		}
		if(!s&&clicked){
			description.setText("");
			selected = null;
			buttonRemoveAnt.setVisible(false);
		}
	}


	@Override
	public void dispose () {
		batch.dispose();
		nodeTexture.dispose();
		antTexture.dispose();
		stage.dispose();
		for (Ant ant : WorldManager.worldManager().getAnts()) {
			ant.interrupt();
		}
	}

}