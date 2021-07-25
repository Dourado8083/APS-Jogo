package com.ecologia.game.scenes;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;

import javax.imageio.ImageIO;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import  com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.BaseDrawable;
import com.badlogic.gdx.utils.viewport.*;
import com.ecologia.game.JogoEcologia;
import com.ecologia.game.screens.Fase1;
import com.ecologia.game.sprites.Player;
import com.ecologia.game.sprites.Player.State;

public class Hud {
	//Stage: Elemento de GUI do jogo, sobrepoe nossa tela principal
	//Classe de HUD do jogo
	public Stage stage;
	private Viewport viewport;
	private Label contaLixo;
	private Label faseAtual;
	private Label tempoJogo;
	private String fase;
	private Table table;
	public Hud(SpriteBatch batch, String fase, float gameTime) {
		//O stage será renderizado por uma camera diferente, pois ficará travado no mesmo lugar na tela sempre, e a câmera do jogo acompanha o jogador
		//utiliza um Viewport do mesmo tipo (Fit) e dimensoes que a camera do nosso jogo
		viewport = new FitViewport(JogoEcologia.V_WIDTH, JogoEcologia.V_HEIGHT, new OrthographicCamera());
		stage = new Stage(viewport, batch);
		
		//Organiza os elementos de interface em uma tabela para adicioná-la ao stage
		this.table = new Table();
		this.contaLixo = new Label(String.valueOf(JogoEcologia.lixoColetado) +" / " +  String.valueOf(JogoEcologia.totalLixo) + " lixos coletados \n", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
		this.faseAtual = new Label("Fase " + fase, new Label.LabelStyle(new BitmapFont(), Color.WHITE));
		this.tempoJogo = new Label("Tempo Restante: " + String.format("%2f", gameTime), new Label.LabelStyle(new BitmapFont(), Color.WHITE)); 
		
		
		
		this.fase = fase;
		//fica no canto superior esquerdo
		table.top();
		table.left();
		//preenche todo o viewport
		table.setFillParent(true);

		table.add(faseAtual);
		table.row();
		table.add(contaLixo);
		table.row();
		table.add(tempoJogo);
		
		//Atribui a table ao stage
		stage.addActor(table);
		
	}
	//atualiza a hud com os lixos coletados corretos
	public void update(boolean ganhou, Player player, float gameTime) {
		DecimalFormat df = new DecimalFormat("#");
		this.tempoJogo.setText("Tempo Restante: " + df.format(gameTime));
		
		if (JogoEcologia.lixoColetado >= JogoEcologia.totalLixo && !ganhou) {
			this.contaLixo.setText("Todos os lixos coletados! Entregue-os ao caminhão de lixo!");
		}
		else if (JogoEcologia.lixoColetado != JogoEcologia.totalLixo){
			this.contaLixo.setText(String.valueOf(JogoEcologia.lixoColetado) + " / " + String.valueOf(JogoEcologia.totalLixo) + " lixos coletados!");		
			this.faseAtual.setOrigin(contaLixo.getOriginX(), contaLixo.getOriginY() - 40);
		}
		else {
			this.contaLixo.setText(null);
			this.faseAtual.setText("FASE "+ fase + " CONCLUÍDA!");
		}
		
		if (player.getState() == State.HURT) {
			if (gameTime > 0 ) {
			table.clear();
			Label gameover = new Label("Fim de Jogo! Aperte R para tentar este nível de novo." , new Label.LabelStyle(new BitmapFont(), Color.WHITE));
			table.add(gameover);
			
			table.center();		
			table.top();
		}
			else if (gameTime <= 0) {
			table.clear();
			Label gameover = new Label("Tempo Esgotado! Você não alcançou o caminhão a tempo!\n Aperte R para tentar este nível de novo.", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
			table.add(gameover);
			table.center();		
			table.top();
			}
		}
	}
	public void ending() {
		table.clear();
		table.center();
		table.setColor(new Color (255,255,255, 0));
		
		
		Image ending = new Image(new Texture(Gdx.files.internal("ending.png")));
		table.add(ending);
		table.act(0);
		
	}
}
