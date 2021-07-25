package com.ecologia.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.ecologia.game.screens.Ending;
import com.ecologia.game.screens.Fase1;

public class JogoEcologia extends Game {
	//SpriteBatch precisa ser publica para ser acessada nas telas das fases
	public SpriteBatch batch;
	//variavel static: inicializada apenas no comeco da classe, pertenca a classe e nao a um objeto
	//variavel final: nao pode ser mudada ou inicializada novamente depois, previne override
	//virtual width e height pra hud/viewport. 
	public static final int V_WIDTH = 470;
	public static final int V_HEIGHT = 250;
	
	//ppm = pixel por metro, escalamento para o body do player se mover melhor
	public static final float PPM = 100;

	
	//valores pros filtros
	//variavel short: valor maximo de 32,767
	//usando 2 pq é mais facil ir elevando a 2
	public static final short DEFAULT_BIT = 1;
	public static final short PLAYER_BIT = 2;
	public static final short LIXO_BIT = 4;
	public static final short COLLECTED_BIT = 8;


	//Contagem de lixo para a HUD.
	public static int totalLixo;
	public static int lixoColetado = 0;
	

	private Music musica;
	
	
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		
		musica = Gdx.audio.newMusic(Gdx.files.internal("data/blinding (90bpm).mp3"));
		musica.setLooping(true);
		musica.play();
		
		
		//Gera uma nova instancia da tela de jogo que foi criada em outra classe.
		//No caso, instancia a tela da fase 1.
		setScreen(new Fase1(this));
	}

	@Override
	public void render () {
	super.render();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
