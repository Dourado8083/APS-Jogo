package com.ecologia.game.sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.ecologia.game.JogoEcologia;
import com.ecologia.game.screens.Fase1;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.ecologia.game.controls.LevelRenderer;


public class Player extends Sprite{
	
	public World world;
	public Body b2body;
	private TextureRegion playerStand;
	//ENUM PARA OS ESTADOS DO PLAYER:
	//ENUM: constant final static, fixa e imutavel, deve ser maisculo
	//Running, Jumping, etc
	public enum State {FALLING, JUMPING, STANDING, RUNNING, HURT};
	public State currentState;
	public State previousState;
	
	public boolean playerHurt = false;
	
	private Animation<TextureRegion> playerRun;
	private Animation<TextureRegion> playerJump;
	private Animation<TextureRegion> playerFalling;
	private Animation<TextureRegion> playerHurting;
	//pra saber que direcao player esta olhando
	private boolean runningRight;
	//para contar o tempo das animacoes com precisao
	private float stateTimer;
	
	public Fixture fixture;
	
	public boolean isDamaged = false;
	
	public Player(World world, LevelRenderer screen) {
		//Encontra a region "hoodie" do texture pack hoodie.pack
		//Hoodie é o personagem jogavel
		super(screen.getAtlas().findRegion("hoodie"));
		this.world = world;
		definePlayer();
		
		currentState = State.STANDING;
		previousState = State.STANDING;
		stateTimer = 0;
		runningRight = true;
		
		
		//encontra o X e Y, width/height da texture que queremos na texture pack
		playerStand = new TextureRegion(getTexture(), 8, 3, 16, 28 );
		setBounds(8, 3, 16/JogoEcologia.PPM, 28/JogoEcologia.PPM);
		//associa texture region ao sprite
		setRegion(playerStand);
	
		
		//animacoes: criar um array de textureRegion para passar o constructor para as animacoes
		Array<TextureRegion> animation = new Array<TextureRegion>();
		//for caso as animacoes estejam arrumadas na sprite sheet
		
		
		//fazendo animacao de correr
		//adiciona os sprites das coordenadas da imagem hoodie
		//O número em float se refere ao tempo de cada frame na animação.
		animation.add(new TextureRegion(getTexture(), 5, 72, 16, 28 ));
		animation.add(new TextureRegion(getTexture(), 8, 105, 16, 29 ));
		animation.add(new TextureRegion(getTexture(), 6, 141, 16, 29 ));
		animation.add(new TextureRegion(getTexture(), 8, 174, 16, 29 ));
		
		animation.add(playerStand);
		
		playerRun = new Animation<TextureRegion>(0.08f, animation);
		animation.clear();
		
		//fazendo animacao de pular
		animation.add(new TextureRegion(getTexture(), 43, 141, 16, 29));
		playerJump = new Animation<TextureRegion>(0.1f, animation);
		animation.clear();
		
			
		//animacao caindo
		animation.add(new TextureRegion(getTexture(), 43, 175, 16, 29));
		playerFalling = new Animation<TextureRegion>(0.1f, animation);
		animation.clear();
		
		//animação ferido(game over)
		animation.add(new TextureRegion(getTexture(), 7, 206, 16, 28));
		playerHurting = new Animation<TextureRegion>(0.1f, animation);
	}
	
	public void update(float deltaTime) {
		setPosition(b2body.getPosition().x - getWidth()/2, b2body.getPosition().y - getHeight()/3);
		setRegion(getFrame(deltaTime));
		
		if (isDamaged == true){
			//super.gameOver();
			
		}
		
	}
	
	public TextureRegion getFrame(float deltaTime) {
		currentState = getState();
		TextureRegion region;
		switch(currentState) {
		case JUMPING:
			region = playerJump.getKeyFrame(stateTimer);
			break;
		case RUNNING:
			//parametro boolean pq é loopavel
			region = playerRun.getKeyFrame(stateTimer, true);
			break;
		case FALLING:
			region = playerFalling.getKeyFrame(stateTimer);
			break;
		case HURT:
			region = playerHurting.getKeyFrame(stateTimer);
			break;
		case STANDING:
		default:
			region = playerStand;
			break;
		
		}
		if ((b2body.getLinearVelocity().x < -0.1 || !runningRight) && !region.isFlipX()) {
			//flipar o X e dps o Y
			region.flip(true, false);
			runningRight = false;
		}
		else if ((b2body.getLinearVelocity().x > 0.1  || runningRight) && region.isFlipX()) {
			region.flip(true, false);
			runningRight = true;
		}
		stateTimer = currentState == previousState ? stateTimer + deltaTime:0;
		previousState = currentState;
		return region;
	}
	
	public State getState() {
		if (this.playerHurt) {
			return State.HURT;
		}
		if (b2body.getLinearVelocity().y > 0) {
			return State.JUMPING;
		}
		else if (b2body.getLinearVelocity().y < 0) {
			return State.FALLING;			
		}
		else if(b2body.getLinearVelocity().x != 0) {
			return State.RUNNING;
		}
		else {
			return State.STANDING;
		}
		
		
	}
	
	public void definePlayer() {
		//Começando a definir o body do player
		//Aonde começa, tipo de corpo,
		BodyDef bdef = new BodyDef();
		bdef.position.set(300/JogoEcologia.PPM,150/JogoEcologia.PPM);
		bdef.type = BodyDef.BodyType.DynamicBody;
		
		//Criando o player no mundo
		b2body = world.createBody(bdef);
		
		//fixture: formatos e propriedades do corpo
		//definimos um polígono em formato de caixa
		FixtureDef fdef = new FixtureDef();
		//CircleShape shape = new CircleShape();
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(4/JogoEcologia.PPM, 9/JogoEcologia.PPM);
		//shape.setRadius(5 /JogoEcologia.PPM);
		
		//Filtros:
		//Toda fixture de body2d tem um filtro
		//todo filtro tem 2 partes
		//categoria: seta o que é a fixture: player,lixo, obstaculo etc
		//mask: com o que aquela fixture pode colidir
		fdef.filter.categoryBits = JogoEcologia.PLAYER_BIT;
		fdef.filter.maskBits = JogoEcologia.DEFAULT_BIT | JogoEcologia.LIXO_BIT;
		
		
		fdef.shape = shape;
		//Cria a fixture do player
		fixture = b2body.createFixture(fdef);
		//Atribui dados "Player" a fixture do jogador, vai ajudar a identificar que é o jogador no controlador de colisões (classe WorldContactListener)
		fixture.setUserData(this.getClass());
		//linha entre dois pontos
		/**EdgeShape head = new EdgeShape();
		head.set(new Vector2(-2/JogoEcologia.PPM, 10/JogoEcologia.PPM), new Vector2(2/JogoEcologia.PPM, 10/JogoEcologia.PPM));
		fdef.shape = head;
		//sfdef sensor nao colide com nada, apenas verifica se há dados ou algo semelhante
		fdef.isSensor = true;
		
		//configura a fixture como cabeca, para conferir dps
		b2body.createFixture(fdef).setUserData("head");
		*/
	}
	
//public abstract void onHeadHit();
public void setCategoryFilter(short filterBit) {
	Filter filter = new Filter();
	filter.categoryBits = filterBit;
	filter.set(filter);
}
	
}

