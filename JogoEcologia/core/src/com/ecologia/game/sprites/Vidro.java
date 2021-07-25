package com.ecologia.game.sprites;
import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.Map;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Ellipse;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.ecologia.game.JogoEcologia;
import com.ecologia.game.controls.WorldContactListener;
import com.ecologia.game.screens.Fase1;


public class Vidro {

	JogoEcologia game;
	//Fase1 fase1 = new Fase1(game);
	protected Fixture fixture;
	
	Sound vidro = Gdx.audio.newSound(Gdx.files.internal("data/vidro.ogg"));
	private Player player;
	
	private Body body;
	public Vidro (World world, BodyDef bdef, TiledMap map, FixtureDef fdef, Rectangle rect, Player player) {
		PolygonShape shape = new PolygonShape();
		//Dinamico: Se move
		//Estatico: nao se move
		//Kinematic: nao é afetado por forcas como gravidade, mas é afetado por forcas como velocidade
		bdef.type = BodyDef.BodyType.StaticBody;
		bdef.position.set((rect.getX() + rect.getWidth()/2) /JogoEcologia.PPM, (rect.getY()+ rect.getHeight() /2)/JogoEcologia.PPM);
		
		//adiciona o body ao world do box2d
		body = world.createBody(bdef);
		shape.setRadius(2/JogoEcologia.PPM);
		shape.setAsBox(rect.getWidth()/2 /JogoEcologia.PPM, rect.getHeight()/2 /JogoEcologia.PPM);
		//Define o shape da fixture e adiciona ao body
		
		fdef.shape = shape;

		fdef.filter.categoryBits = JogoEcologia.DEFAULT_BIT;
		fdef.filter.maskBits = JogoEcologia.PLAYER_BIT;
		fixture = body.createFixture(fdef);
		fixture.setUserData(this);
		
		
		body.createFixture(fdef);
		
		world.setContactListener(new WorldContactListener());
		
		this.player = player;
		
	}
	
	public void Dano() {
		player.playerHurt = true;
		vidro.play();
		
		
	}
	
	
}
