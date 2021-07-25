package com.ecologia.game.screens;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.ecologia.game.JogoEcologia;
import com.ecologia.game.controls.LevelRenderer;


//usa a interface Screen 
public class Fase1 extends Fases implements Screen {
	

	
	private LevelRenderer level1;
	private JogoEcologia game;
	
	
	public Fase1(JogoEcologia game) {
		this.game = game;
		this.playerMorto = false;
		this.passouFase = false;
		//A classe LevelRenderer que cuida do processamento e renderização do jogo
		//Essa clase é aproveitada em todas as fases, e é instanciada apenas com o nome do mapa desejado e a classe de JogoEcologia
		level1 = new LevelRenderer("1", game);
	

	} 
	
	@Override
	public void fasePassada() {
		if (passouFase) {
			game.setScreen(new Fase2(game));
			pause();
			}
		
	}
	
	@Override
	public void playerMorreu() {
		if (this.playerMorto) {
			if (Gdx.input.isKeyPressed(Input.Keys.R)){
				Gdx.app.log("reset", null);
				game.setScreen(new Fase1(game));
				pause();
				JogoEcologia.lixoColetado = 0;
			}
			
		}		
	}
	
	public void update(float deltaTime) {
		fasePassada();
		playerMorreu();
		if (level1.player.playerHurt) {
			this.playerMorto = true;
		}
	}
	
	
	
	
	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(float delta) {
		level1.render(delta, passouFase);
		update(delta);
		if(Gdx.input.isKeyPressed(Input.Keys.G)) {
			level1.hud.ending();
		}
	}

	@Override
	public void resize(int width, int height) {
		level1.resize(width, height);
		
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

	

}
