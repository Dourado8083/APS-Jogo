package com.ecologia.game.sprites;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.ecologia.game.JogoEcologia;
import com.ecologia.game.screens.Fase1;
import com.ecologia.game.screens.Fases;

public class Caminhao {
	private Fixture fixture;
	
	public Caminhao(World world, BodyDef bdef, TiledMap map, FixtureDef fdef, Rectangle rect) {
		bdef.type = BodyDef.BodyType.StaticBody;
		bdef.position.set((rect.x + rect.width/2) /JogoEcologia.PPM, (rect.y + rect.height /2)/JogoEcologia.PPM);
		
		
		//adiciona o body ao world do box2d
		Body body = world.createBody(bdef);
		
		PolygonShape shape = new PolygonShape();

		shape.setAsBox((rect.getWidth() / 2)/JogoEcologia.PPM, (rect.getHeight() /2)/JogoEcologia.PPM);

		shape.setRadius(2/JogoEcologia.PPM);
		//Define o shape da fixture e adiciona ao body
		fdef.shape = shape;
		fdef.isSensor = false;
		fdef.filter.categoryBits = JogoEcologia.LIXO_BIT;
		fdef.filter.maskBits = JogoEcologia.PLAYER_BIT;
		fixture = body.createFixture(fdef);
		fixture.setUserData(this);
		
	}
	//chamado quando colide com o caminhao
	//se colide com o caminhao e ja possui todos os lixos, passa de fase
	//se nao, é apenas uma plataforma
	public void colisaoCaminhao() {
		if(JogoEcologia.lixoColetado == JogoEcologia.totalLixo) {
			//passa de fase
			Fases.passouFase = true;
			
			JogoEcologia.totalLixo = 0;
			JogoEcologia.lixoColetado = 0;
		}
		
	}
	
}
