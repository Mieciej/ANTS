package com.maciej.ants;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.ScreenUtils;

import java.security.Key;
import java.util.*;

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
	Ant [] ants;
	Sprite [] antSprites;
	private float screenBoundX;
	private float screenBoundY;
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
		camera.setToOrtho(false,800,400);
		screenBoundX = camera.viewportWidth;
		screenBoundY = camera.viewportHeight -   100;
		// Initialise UI elements
		stage = new Stage();
		descriptionPopup = new Table(skin);
		descriptionPopup.setFillParent(true);
		descriptionPopup.top().left();
		stage.addActor(descriptionPopup);
		description = new Label("Hello World",skin);
		description.setFontScale(0.75f);
		descriptionPopup.add(description).padTop(5).padLeft(5).width(screenBoundX-5);

		// Initialise World
		WorldManager.initialiseWorld(100);
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
				}
			}
			Vector2 newVertexPos = new Vector2(WorldManager.worldManager().getGraph().getAbstractPosition(vertex).x*screenBoundX,WorldManager.worldManager().getGraph().getAbstractPosition(vertex).y*screenBoundY);
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
		int numberOfAnts = 40;
		antSprites = new Sprite[numberOfAnts];
		ants = new Ant[numberOfAnts];
		for (int i = 0; i < numberOfAnts; i++) {
			antSprites[i] =new Sprite(antTexture,antTexture.getWidth(),antTexture.getHeight());
			if(i<numberOfAnts/2)
				ants[i] = AntFactory.getWorker(WorldManager.worldManager().getAnthills().get("RED"));
			else
				ants[i]  = AntFactory.getWorker(WorldManager.worldManager().getAnthills().get("BLUE"));
			switch (ants[i].getTeam()){
				case "RED":
					antSprites[i].setColor(Color.RED);
					break;
				case "BLUE":
					antSprites[i].setColor(Color.BLUE);
					break;
			}
			antSprites[i].setPosition(ants[i].getAbstractPosition().x*screenBoundX + antSprites[i].getWidth()/2,ants[i].getAbstractPosition().y*screenBoundY + antSprites[i].getHeight()/2);
			ants[i].start();

		}
	}
	private Object selected = null;
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
		for (int i = 0; i < ants.length; i++) {
			antSprites[i].draw(batch);
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
		for (int i = 0; i < ants.length; i++) {
			if(clicked){
				Vector3 clickPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(),0);
				Rectangle rect = antSprites[i].getBoundingRectangle();
				camera.unproject(clickPos);
				if(rect.contains(clickPos.x,clickPos.y)){
					selected = ants[i];
					s = true;
				}
			}

			antSprites[i].setPosition(ants[i].getAbstractPosition().x*screenBoundX + antSprites[i].getWidth()/2,ants[i].getAbstractPosition().y*screenBoundY + antSprites[i].getHeight()/2);
		}
		if(selected!=null)
			description.setText(selected.toString());
		if(!s&&clicked){
			description.setText("");
			selected = null;
		}
	}


	@Override
	public void dispose () {
		batch.dispose();
		nodeTexture.dispose();
		antTexture.dispose();
		stage.dispose();
		for (Ant ant : ants) {
			ant.interrupt();
		}
	}

}