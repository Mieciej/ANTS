package com.maciej.ants;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.*;

import static com.maciej.ants.Math.haltonSequence;

public class Ants extends ApplicationAdapter {
	SpriteBatch batch;
	private Texture nodeTexture;
	private Texture antTexture;
	private HashMap<Integer,Sprite> verticesSprites;
	OrthographicCamera camera;
	ShapeRenderer shapeRenderer;
	private Stage stage;
	private VerticalGroup descriptionPopup;
	private Label description;
	private Skin skin ;
	Vector2[] edgeSources;
	Vector2[] edgeEnds;
	Ant [] ants;
	Sprite [] antSprites;
	@Override
	public void create () {
		// Load assets
		skin = new Skin(Gdx.files.internal("comic/skin/comic-ui.json"));
		nodeTexture = new Texture("node.png");
		antTexture = new Texture("ant.png");
		// Initialise renderers
		batch = new SpriteBatch();
		shapeRenderer = new ShapeRenderer();
		// Initialise UI elements
		stage = new Stage();
		descriptionPopup = new VerticalGroup();
		description = new Label("Hello World",skin );
		stage.addActor(description);
		// Create Camera
		camera = new OrthographicCamera();
		camera.setToOrtho(false,800,400);
		// Initialise World
		WorldManager.initialiseWorld(100);
		// Generate and store vertices sprites
		verticesSprites = new HashMap<>();
		float[] xSequence = haltonSequence(3,100);
		float[] ySequence = haltonSequence(2,100);
		for (Integer vertex :
				WorldManager.worldManager().getGraph().getVertices()) {
			int i = verticesSprites.size();
			Sprite newVertexSprite= new Sprite(nodeTexture,nodeTexture.getWidth(),nodeTexture.getHeight());
			Vector2 newVertexPos = new Vector2(xSequence[i]*camera.viewportWidth,ySequence[i]*camera.viewportHeight);
			newVertexSprite.setPosition(newVertexPos.x, newVertexPos.y);
			verticesSprites.put(vertex,newVertexSprite);
		}
		// Add edges to the graph
		// Store edge information to speedup drawing
		ArrayList<AbstractMap.SimpleEntry<Integer,Integer>> edges = WorldManager.worldManager().getGraph().getEdges();
		edgeSources = new Vector2[edges.size()];
		edgeEnds = new Vector2[edges.size()];
		for (int j = 0; j < edges.size(); j++) {
			AbstractMap.SimpleEntry<Integer,Integer> edge = edges.get(j);
			Vector2 edgeSource = new Vector2(verticesSprites.get(edge.getKey()).getX() + verticesSprites.get(edge.getKey()).getWidth()/2, verticesSprites.get(edge.getKey()).getY() + verticesSprites.get(edge.getKey()).getHeight()/2);
			Vector2 edgeEnd = new Vector2(verticesSprites.get(edge.getValue()).getX() + verticesSprites.get(edge.getValue()).getWidth()/2, verticesSprites.get(edge.getValue()).getY() + verticesSprites.get(edge.getValue()).getHeight()/2);
			edgeSources[j] = edgeSource;
			edgeEnds[j] = edgeEnd;

		}
		antSprites = new Sprite[200];
		ants = new Ant[200];
		for (int i = 0; i < 200; i++) {
			antSprites[i] =new Sprite(antTexture,antTexture.getWidth(),antTexture.getHeight());
			antSprites[i].setCenter(antTexture.getWidth()/2,antTexture.getHeight()/2);
			ants[i] = new Ant();
			Vector2 antPos = new Vector2(verticesSprites.get(ants[i].sourceVertex).getX(), verticesSprites.get(ants[i].sourceVertex).getY());
			antSprites[i].setPosition(antPos.x,antPos.y);
			ants[i].start();

		}
	}




	@Override
	public void render () {
		ScreenUtils.clear(new Color(new Color(238,232,213,1)));
		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
		camera.update();
		for (int i = 0; i < edgeSources.length; i++) {
			shapeRenderer.setColor(Color.GREEN);
			shapeRenderer.setProjectionMatrix(camera.combined);
			Vector2 source = edgeSources[i];
			Vector2 end = edgeEnds[i];
			shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
			shapeRenderer.rectLine(source,end,3);
			shapeRenderer.end();
		}
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		for (int i = 0; i < ants.length; i++) {
			antSprites[i].draw(batch);
			Vector2 antPosSource = new Vector2(verticesSprites.get(ants[i].sourceVertex).getX(), verticesSprites.get(ants[i].sourceVertex).getY());
			Vector2 antPosTarget = new Vector2(verticesSprites.get(ants[i].targetVertex).getX(), verticesSprites.get(ants[i].targetVertex).getY());
			antSprites[i].setPosition((antPosTarget.x - antPosSource.x)*ants[i].getTravelProgress() + antPosSource.x,(antPosTarget.y - antPosSource.y)*ants[i].getTravelProgress() + antPosSource.y );
		}
		for (Sprite sprite :
				verticesSprites.values()) {
			sprite.draw(batch);
		}
		batch.end();
	}


	@Override
	public void dispose () {
		batch.dispose();
		nodeTexture.dispose();
		stage.dispose();
		for (int i = 0; i < ants.length; i++) {
			ants[i].interrupt();
		}
	}

	private void initialiseGraph(int numberOfVertices, int edgeDensity){
	}
}
