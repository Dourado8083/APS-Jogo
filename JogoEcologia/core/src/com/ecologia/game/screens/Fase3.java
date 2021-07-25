package com.ecologia.game.screens;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.ecologia.game.JogoEcologia;
import com.ecologia.game.controls.LevelRenderer;

//usa a interface Screen 
public class Fase3 extends Fases implements Screen{
	
	private TextureAtlas tAtlas;
	private LevelRenderer level3;

	private JogoEcologia game;
	
	
	public Fase3(JogoEcologia game) {
		this.game = game;
		this.playerMorto = false;
		this.passouFase = false;
		//A classe LevelRenderer que cuida do processamento e renderização do jogo
		//Essa clase é aproveitada em todas as fases, e é instanciada apenas com o nome do mapa desejado e a classe de JogoEcologia
		level3 = new LevelRenderer("3", game);

	}
	
	@Override
	public void fasePassada() {
		if (this.passouFase) {
			game.setScreen(new Ending(game));
		}
		
	}	
	
	
	public void update(float deltaTime) {
		playerMorreu();
		fasePassada();
		if (level3.player.playerHurt) {
			this.playerMorto = true;
		}
		
	}
	
		
	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(float delta) {
		level3.render(delta, passouFase);
		update(delta);
		}

	@Override
	public void resize(int width, int height) {
		level3.resize(width, height);
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void playerMorreu() {
		// TODO Auto-generated method stub
		if (this.playerMorto) {
			if (Gdx.input.isKeyPressed(Input.Keys.R)){
				Gdx.app.log("reset", null);
				game.setScreen(new Fase3(game));
				pause();
				JogoEcologia.lixoColetado = 0;
			}
			
		}		
	}

	

}
