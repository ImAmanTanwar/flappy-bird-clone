package com.aman.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;

public class FlappyBird extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;
	Texture[] birds;
	Texture topTube;
	Texture bottomTube;
	float birdY = 0;
	float velocity = 0;
	int flapState = 0;
	float gravity = 2.5f;
	int gameState = 0;
	int score = 0;
	int passingTube = 0;
	float gap = 400;
	Random random;
	float tubeVelocity = 4;
	BitmapFont bitmapFont;
//	float maxTubeOffset;
	int numberOfTubes = 4;
	float[] tubeX = new float[numberOfTubes];
	float[] tubeOffset = new float[numberOfTubes];
	float distanceBetweenTubes;
	Circle birdBody;
	Rectangle[] upperTubeBody = new Rectangle[numberOfTubes];
	Rectangle[] lowerTubeBody = new Rectangle[numberOfTubes];

	@Override
	public void create () {
		batch = new SpriteBatch();
		background = new Texture("bg.png");
		birds = new Texture[2];
		birds[0] = new Texture("bird.png");
		birds[1] = new Texture("bird2.png");
		topTube = new Texture("toptube.png");
		bottomTube = new Texture("bottomtube.png");
		birdBody = new Circle();
		birdY = Gdx.graphics.getHeight()/2 - birds[0].getHeight()/2;
		bitmapFont = new BitmapFont();
		bitmapFont.setColor(Color.RED);
		bitmapFont.getData().setScale(10);
	//	maxTubeOffset = Gdx.graphics.getHeight() /2 - gap/2 - 100;
		random = new Random();
		distanceBetweenTubes = Gdx.graphics.getWidth() * 0.62f;

		for(int i=0;i<numberOfTubes;i++) {
			tubeX[i] = Gdx.graphics.getWidth() / 2 - bottomTube.getWidth() /2 + i * distanceBetweenTubes;
			tubeOffset[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - 200);
			upperTubeBody[i] = new Rectangle();
			lowerTubeBody[i] = new Rectangle();
		}
	}

	@Override
	public void render () {

		batch.begin();
		batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		if (gameState != 0) {

			if(tubeX[passingTube] < Gdx.graphics.getWidth()/2 - topTube.getWidth()) {
				score++;
				Gdx.app.log("score",  ""+score);
				passingTube = (passingTube + 1) % numberOfTubes;
			}
			if(Gdx.input.justTouched()) {
				velocity = -40;
			}
			for(int i=0;i<numberOfTubes;i++) {
				if(tubeX[i] < -bottomTube.getWidth()) {
					tubeX[i] += numberOfTubes * distanceBetweenTubes;
					tubeOffset[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - 200);
				}
				else {
					tubeX[i] = tubeX[i] - tubeVelocity;

				}
				batch.draw(topTube, tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i]);
				batch.draw(bottomTube, tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() + tubeOffset[i]);
				upperTubeBody[i] = new Rectangle(tubeX[i],Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i],topTube.getWidth(),topTube.getHeight());
				lowerTubeBody[i] = new Rectangle(tubeX[i],Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() + tubeOffset[i],bottomTube.getWidth(),bottomTube.getHeight());
			}


			if(birdY > 0 || velocity < 0) {
				velocity = velocity + gravity;
				birdY -= velocity;
			}
		}
		else {
			if(Gdx.input.justTouched()) {
				gameState = 1;

			}
		}
		flapState = (flapState + 1) % 2;
		batch.draw(birds[flapState], Gdx.graphics.getWidth() / 2 - birds[flapState].getWidth() / 2, birdY);
		bitmapFont.draw(batch,String.valueOf(score),100,Gdx.graphics.getHeight()-100);
		batch.end();
		birdBody.set(Gdx.graphics.getWidth()/2,birdY + birds[flapState].getHeight() / 2,birds[flapState].getWidth()/2);
		for(int i = 0;i< numberOfTubes; i++) {
			if(Intersector.overlaps(birdBody,upperTubeBody[i]) || Intersector.overlaps(birdBody,lowerTubeBody[i])) {

			}
		}
	}
}
