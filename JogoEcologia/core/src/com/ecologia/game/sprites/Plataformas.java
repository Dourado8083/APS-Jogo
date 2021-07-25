package com.ecologia.game.sprites;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Ellipse;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.ecologia.game.JogoEcologia;
import com.ecologia.game.controls.WorldContactListener;
import com.ecologia.game.screens.Fase1;


public class Plataformas {

	public Plataformas (World world, TiledMap map, BodyDef bdef, FixtureDef fdef, Rectangle rect, Body body, PolygonShape shape) {
			
			//Dinamico: Se move
			//Estatico: nao se move
			//Kinematic: nao é afetado por forcas como gravidade, mas é afetado por forcas como velocidade
			bdef.type = BodyDef.BodyType.StaticBody;
			bdef.position.set((rect.getX() + rect.getWidth()/2) /JogoEcologia.PPM, (rect.getY()+ rect.getHeight() /2)/JogoEcologia.PPM);
			
			//adiciona o body ao world do box2d
			body = world.createBody(bdef);
			shape.setRadius(2/JogoEcologia.PPM);
			shape.setAsBox((rect.getWidth() / 2)/JogoEcologia.PPM, (rect.getHeight() /2)/JogoEcologia.PPM);
			//Define o shape da fixture e adiciona ao body
			
			fdef.shape = shape;
			body.createFixture(fdef);
			world.setContactListener(new WorldContactListener());
			
		
	}

}
