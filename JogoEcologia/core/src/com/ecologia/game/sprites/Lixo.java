package com.ecologia.game.sprites;
import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.Map;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Ellipse;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.ecologia.game.JogoEcologia;
import com.ecologia.game.screens.Fase1;


public class Lixo {

	JogoEcologia game;
	//Fase1 fase1 = new Fase1(game);
	protected Fixture fixture;

	Sound sound = Gdx.audio.newSound(Gdx.files.internal("data/lixo222.ogg"));
	
	private TiledMap map;
	
	private Body body;
	public Lixo (World world, BodyDef bdef, TiledMap map, FixtureDef fdef, Ellipse el) {
			//Dinamico: Se move
			//Estatico: nao se move
			//Kinematic: nao é afetado por forcas como gravidade, mas é afetado por forcas como velocidade
			bdef.type = BodyDef.BodyType.KinematicBody;
			bdef.position.set((el.x + el.width/2) /JogoEcologia.PPM, (el.y + el.height /2)/JogoEcologia.PPM);
			
			//adiciona o body ao world do box2d
			Body body = world.createBody(bdef);
			
			
			//Define o shape da fixture e adiciona ao body
			fdef.shape = new CircleShape();
			
			fdef.isSensor = true;
			
			fdef.shape.setRadius(10/JogoEcologia.PPM);
			fdef.filter.categoryBits = JogoEcologia.DEFAULT_BIT;
			fdef.filter.maskBits = JogoEcologia.PLAYER_BIT;
			fixture = body.createFixture(fdef);
			fixture.setUserData(this);
			
			
			this.fixture = fixture;
			this.body = body;
			
			this.map = map;
			
		
	}
	public void Coleta() {
		//Pega a posição do corpo, escala pelo PPM, divide por 16 (mapa feito em 16x16 pixels)
		ArrayList<TiledMapTileLayer.Cell> cells = getCell(this.map, this.body);
		Filter filter = new Filter();
		filter.categoryBits = JogoEcologia.COLLECTED_BIT;
		fixture.setFilterData(filter);
		//Random random = new Random();
		//String somrandom = "data/lixo" + String.valueOf(random.nextInt(2));
		
		for(int i = 0; i < cells.size(); i++) {
			cells.get(i).setTile(null);
		sound.play();
		}
	}
	
	public ArrayList<TiledMapTileLayer.Cell> getCell(TiledMap map, Body body){
		ArrayList<TiledMapTileLayer.Cell> tiles = new ArrayList();
		//pega a cell para remover do jogo (foi coletado)
		TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(8);
		//(int) converte de float para int
		//é necessario multiplicar pelo ppm por que o jogo esta escalado e precisa voltar aos valores normais para o Tiled
		
		if (layer.getCell((int)((body.getPosition().x * JogoEcologia.PPM/16)), (int) ((body.getPosition().y  * JogoEcologia.PPM/16))) != null) {
			tiles.add(layer.getCell((int)((body.getPosition().x * JogoEcologia.PPM/16)), (int) ((body.getPosition().y  * JogoEcologia.PPM/16))));
		}
		if (layer.getCell((int)((body.getPosition().x * JogoEcologia.PPM/16)), (int) ((body.getPosition().y  * JogoEcologia.PPM/16)-1)) != null) {
			tiles.add(layer.getCell((int)((body.getPosition().x * JogoEcologia.PPM/16)), (int) ((body.getPosition().y  * JogoEcologia.PPM/16)-1)));
		}
		if (layer.getCell((int)((body.getPosition().x * JogoEcologia.PPM/16)), (int) ((body.getPosition().y  * JogoEcologia.PPM/16)+1)) != null) {
			tiles.add(layer.getCell((int)((body.getPosition().x * JogoEcologia.PPM/16)), (int) ((body.getPosition().y  * JogoEcologia.PPM/16)+1)));
		}
			
		
		return tiles;
	}
}
