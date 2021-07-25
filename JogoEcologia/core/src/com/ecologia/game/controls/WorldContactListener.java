package com.ecologia.game.controls;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.ecologia.game.sprites.Player;
import com.ecologia.game.sprites.Vidro;
import com.ecologia.game.JogoEcologia;
import com.ecologia.game.screens.Fase1;
import com.ecologia.game.sprites.Caminhao;
import com.ecologia.game.sprites.Lixo;


public class WorldContactListener implements ContactListener{

	@Override
	public void beginContact(Contact contact) {
		// TODO Auto-generated method stub
		Fixture fixtA = contact.getFixtureA();
		Fixture fixtB = contact.getFixtureB();
		
		//Se houver uma colisao com o player
		if (fixtA.getUserData() == Player.class || fixtB.getUserData() == Player.class) {
			Fixture player = fixtA;
			Fixture object = fixtB;	
		
			
			//Organiza as fixtures caso estejam trocadas
			//É necessario descobrir qual fixture é o player e qual é o objeto
			if (fixtA.getUserData() == Player.class) {
				player = fixtA;
				object = fixtB;				
			}
			else if (fixtB.getUserData() == Player.class) {
				player = fixtB;
				object = fixtA;				
			}
			
			if (object.getUserData() != null && object.getUserData() instanceof Lixo) {
				Gdx.app.log("moeda",null);
				//mudar depois
				JogoEcologia.lixoColetado++;
				//Declara que o object pertence a classe Lixo
				((Lixo) object.getUserData()).Coleta();				
			}
			else if (object.getUserData() != null && object.getUserData() instanceof Caminhao) {
				Gdx.app.log("caminhao", null);			
				((Caminhao) object.getUserData()).colisaoCaminhao();
			}
			else if (object.getUserData() != null & object.getUserData() instanceof Vidro) {
				Gdx.app.log("vidro", null);
				((Vidro) object.getUserData()).Dano();
			}
				
		}
	}

	@Override
	public void endContact(Contact contact) {
		// TODO Auto-generated method stub
		Gdx.app.log("colisao ", null);
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		// TODO Auto-generated method stub
		
	}

}
