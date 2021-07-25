package com.ecologia.game.sprites;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.ecologia.game.JogoEcologia;
import com.ecologia.game.screens.Fase1;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.math.Ellipse;
import com.badlogic.gdx.physics.box2d.CircleShape;


public class Walls {

	public Walls(World world, Fase1 screen, TiledMap map, BodyDef bdef, FixtureDef fdef) {
		
		for (MapObject object : map.getLayers().get(9).getObjects().getByType(RectangleMapObject.class)) {
			
			Rectangle rect = ((RectangleMapObject) object).getRectangle();
			
			//Dinamico: Se move
			//Estatico: nao se move
			//Kinematic: nao é afetado por forcas como gravidade, mas é afetado por forcas como velocidade
			bdef.type = BodyDef.BodyType.KinematicBody;
			bdef.position.set((rect.x + rect.width/2) /JogoEcologia.PPM, (rect.y + rect.height /2)/JogoEcologia.PPM);
			
			//adiciona o body ao world do box2d
			Body body = world.createBody(bdef);
			
			//Define o shape da fixture e adiciona ao body
			fdef.shape = new PolygonShape();
			fdef.shape.setRadius(2/JogoEcologia.PPM);
			body.createFixture(fdef);
			
		}
	
	}
}