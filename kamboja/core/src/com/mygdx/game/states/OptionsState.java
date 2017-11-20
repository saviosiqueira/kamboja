package com.mygdx.game.states;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.KambojaMain;
import com.mygdx.game.Manager;
import com.mygdx.game.State;
import com.mygdx.game.controllers.Gamecube;
import com.mygdx.game.controllers.GenericController;
import com.mygdx.game.controllers.XBox;
import com.mygdx.game.objects.GameMusic;
import com.mygdx.game.objects.Util;

public class OptionsState extends GenericInterface{
	
	Texture main_sign;
	Texture above_bar;
	Texture options_sign;
	Texture light_sign;
	Texture debug_sign;
	Texture back_cog;
	Texture left_arrow, right_arrow;
	Texture on, off;
	Texture bar, small_cog;
	Texture selection_tex;
	
	float left_light_scale;
	float right_light_scale;
	float left_debug_scale;
	float right_debug_scale;
	
	
	int opt = 0;
	
	Rectangle2D[] bounds = new Rectangle2D[8];
	Rectangle2D currentBound;
	

	float cog_angle;
	float cog_speed;
	
	Body light_body;
	Body debug_body;
	
	float val_x;
	
	//TODO: dar update no volume da music a cada update;
	
	public OptionsState(Manager manager) {
		super(manager);
	}

	public void create() {
		super.create();
		background = KambojaMain.getTexture("menu/options/fundo.jpg");
		main_sign = KambojaMain.getTexture("menu/options/main_sign.png");
		above_bar = KambojaMain.getTexture("menu/options/above_bar.png");
		options_sign = KambojaMain.getTexture("menu/options/options_sign.png");
		
		left_arrow = KambojaMain.getTexture("menu/options/seta esquerda.png");
		right_arrow = KambojaMain.getTexture("menu/options/seta direita.png");
		on = KambojaMain.getTexture("menu/options/on.png");
		off = KambojaMain.getTexture("menu/options/off.png");
		
		bar = KambojaMain.getTexture("menu/options/barra.png");
		small_cog = KambojaMain.getTexture("menu/options/small_cog.png");
		
		selection_tex = KambojaMain.getTexture("menu/player_select/selection.png");
		
		left_light_scale = 1;
		right_light_scale = 1;
		left_debug_scale = 1;
		right_debug_scale = 1;
		
		val_x = 0;
		
		cog_angle = 0;
		cog_speed = 0;
		
		currentBound = new Rectangle2D.Double(0, 0, 0, 0);
		
		light_sign = KambojaMain.getTexture("menu/options/light.png");
		debug_sign = KambojaMain.getTexture("menu/options/debug_mode.png");
		
		back_cog = KambojaMain.getTexture("menu/options/engrenagem.png");
		light_body = createBox(new Vector2(Gdx.graphics.getWidth()/2f + 700*factor, Gdx.graphics.getHeight()*(3/4f)), new Vector2(394*factor/2f, 241*factor/2f), BodyType.DynamicBody, 0.05f);
		
		buildRopeJoint(
				10, light_body, 750*factor, 80);
		
		debug_body = createBox(new Vector2(Gdx.graphics.getWidth()/2f - 700*factor, Gdx.graphics.getHeight()*(3/4f)), new Vector2(394*factor/2f, 241*factor/2f), BodyType.DynamicBody, 0.05f);
		
		buildRopeJoint(
				10, debug_body, -750*factor, 80);
		
		
		bounds[0] = new Rectangle2D.Double(670*factor, Gdx.graphics.getHeight() - 485*factor, 595*factor, 187*factor);
		bounds[1] = new Rectangle2D.Double(632*factor, Gdx.graphics.getHeight() - 663*factor, 708*factor, 185*factor);
		bounds[2] = new Rectangle2D.Double(607*factor, Gdx.graphics.getHeight() - 782*factor, 759*factor, 123*factor);
		bounds[3] = new Rectangle2D.Double(0*factor, Gdx.graphics.getHeight() - 530*factor, 440*factor, 351*factor);
		bounds[4] = new Rectangle2D.Double(1510*factor, Gdx.graphics.getHeight() - 530*factor, 429*factor, 390*factor);
		bounds[5] = new Rectangle2D.Double(750*factor, Gdx.graphics.getHeight() - 1200*factor, 420*factor, 300*factor);
		bounds[6] = new Rectangle2D.Double(100*factor, Gdx.graphics.getHeight() - 100*factor, 100*factor, 100*factor);
		bounds[7] = new Rectangle2D.Double(100*factor, Gdx.graphics.getHeight() - 200*factor, 100*factor, 100*factor);
	}

	public void dispose() {
		
	}


	public void update(float delta) {
		super.update(delta);
		cog_angle += cog_speed;
		
		System.out.println(val_x);
		
		if(opt == 6) {
			if(Math.abs(val_x) > 0.1f) {
				GameState.VOLUME += val_x * 0.01f;
				if(GameState.VOLUME > 1) GameState.VOLUME = 1;
				if(GameState.VOLUME < 0) GameState.VOLUME = 0;
			}
		}
		else if(opt == 7) {
			if(Math.abs(val_x) > 0.1f) {
				GameMusic.MUSIC_VOLUME += val_x * 0.01f;
				if(GameMusic.MUSIC_VOLUME > 1) GameMusic.MUSIC_VOLUME = 1;
				if(GameMusic.MUSIC_VOLUME < 0) GameMusic.MUSIC_VOLUME = 0;
			}
		}
		
		currentBound.setFrame(
				currentBound.getX() + (bounds[opt].getX() - currentBound.getX())/10.0f,
				currentBound.getY() + (bounds[opt].getY() - currentBound.getY())/10.0f,
				currentBound.getWidth() + (bounds[opt].getWidth() - currentBound.getWidth())/10.0f,
				currentBound.getHeight() + (bounds[opt].getHeight() - currentBound.getHeight())/10.0f);
		


		bounds[6].setFrame(
				(Gdx.graphics.getWidth() - small_cog.getWidth()*factor)/ 2  + ((GameState.VOLUME - 0.5f) * (bar.getWidth()*factor*0.9f)) - 20*factor,
				600*factor, 100*factor, 100*factor);
		bounds[7].setFrame(
				(Gdx.graphics.getWidth() - small_cog.getWidth()*factor)/ 2  + ((GameMusic.MUSIC_VOLUME - 0.5f) * (bar.getWidth()*factor*0.9f)) - 20*factor,
				420*factor, 100*factor, 100*factor);

		if(outro){
			cog_speed += delta*10;
		}
		
		
		left_light_scale += (1- left_light_scale)/10.0f;
		right_light_scale += (1- right_light_scale)/10.0f;
		left_debug_scale += (1- left_debug_scale)/10.0f;
		right_debug_scale += (1- right_debug_scale)/10.0f;
		
		
		
		
	}

	public void connected(Controller controller) {
		
	}

	public void disconnected(Controller controller) {
		
	}

	public boolean buttonDown(Controller controller, int buttonCode) {
		
		int back_btn = 0;
		int select_btn = 0;
		
		if(controller.getName().equals(Gamecube.getID())){
			back_btn = Gamecube.B;
			select_btn = Gamecube.A;
		}
		else if(controller.getName().toUpperCase().contains("XBOX") && controller.getName().contains("360")){
			back_btn = XBox.BUTTON_B;
			select_btn = XBox.BUTTON_A;
		}
		else {
			back_btn = GenericController.CIRCLE;
			select_btn = GenericController.X;
		}
		
		if(buttonCode == back_btn) {
			if(opt == 6) {
				opt  = 0;
			}
			else if(opt == 7) {
				opt = 1;
			}
			else {
				opt = 5;
			}
		}
		else if(buttonCode == select_btn) {
			if(opt == 0) {
				opt = 6;
			}
			else if(opt == 1) {
				opt = 7;
			}
			else if(opt == 2) {
				GameState.DIFFICULTY ++;
				if(GameState.DIFFICULTY == 5) {
					GameState.DIFFICULTY = 0;
				}
			}
			else if(opt == 3) {
				GameState.DEBUG = !GameState.DEBUG;
			}
			else if(opt == 4) {
				GameState.LIGHTS = !GameState.LIGHTS;
			}
			else if(opt == 5) {
				intro = false;
				outro = true;
			}
			else if(opt == 6) {
				opt = 0;
			}
			else if(opt == 7) {
				opt = 1;
			}
		}
		
		return false;
	}

	public boolean buttonUp(Controller controller, int buttonCode) {
		return false;
	}
	
	/*	0 = SFX unselected
	 * 	1 = music unselected
	 *  2 = difficulty
	 *  3 = debug
	 *  4 = lights
	 *  5 = back
	 *  6 = sfx selected
	 *  7 = music selected
	 *  
	 */
	
	public void changeSelectionX(float value) {
		if(value > 0) {
			if(opt == 0 || opt == 1 || opt == 2) {
				opt = 4;
			}
			else if(opt == 3) {
				opt = 0;
			}
		}
		else {
			if(opt == 0 || opt == 1 || opt == 2) {
				opt = 3;
			}
			else if(opt == 4) {
				opt = 0;
			}
		}
	}
	
	public void changeSelectionY(float value) {
		if(value < 0) {
			if(opt == 1 || opt == 2) {
				opt --;
			}
			else if(opt == 5) {
				opt = 2;
			}
		}
		else {
			if(opt == 3 || opt == 4 ||opt == 2) {
				opt = 5;
			}
			else if(opt == 0 || opt == 1) {
				opt ++;
			}
		}
	}

	boolean xMoved = false;
	boolean yMoved = false;
	
	public boolean axisMoved(Controller controller, int axisCode, float value) {
			if(controller.getName().equals(Gamecube.getID())){
				if(axisCode == Gamecube.MAIN_X) {
					if(Math.abs(value) > 0.5f) {
						if(!xMoved) {
							xMoved = true;
							changeSelectionX(value);
						}
					}
					else {
						xMoved = false;
					}
					
					val_x = value;
				}
				if(axisCode == Gamecube.MAIN_Y) {
					if(Math.abs(value) > 0.5f) {
						if(!yMoved) {
							yMoved = true;
							changeSelectionY(value);
						}
					}
					else {
						yMoved = false;
					}
				}
			}
			else if(controller.getName().toUpperCase().contains("XBOX") && controller.getName().contains("360")){
				if(axisCode == XBox.AXIS_LEFT_X) {
					if(Math.abs(value) > 0.5f) {
						if(!xMoved) {
							xMoved = true;
							changeSelectionX(value);
						}
					}
					else {
						xMoved = false;
					}
					val_x = value;
				}
				if(axisCode == Gamecube.MAIN_Y) {
					if(Math.abs(value) > 0.5f) {
						if(!yMoved) {
							yMoved = true;
							changeSelectionY(value);
						}
					}
					else {
						yMoved = false;
					}
				}
			}
			else {
				if(axisCode == GenericController.LEFT_X) {
					if(Math.abs(value) > 0.5f) {
						if(!xMoved) {
							xMoved = true;
							changeSelectionX(value);
							
						}
					}
					else {
						xMoved = false;
					}
					val_x = value;
				}
				if(axisCode == Gamecube.MAIN_Y) {
					if(Math.abs(value) > 0.5f) {
						if(!yMoved) {
							yMoved = true;
							changeSelectionY(value);
						}
					}
					else {
						yMoved = false;
					}
				}
			}
		
		return false;
	}

	public void doSelection() {
			if(opt == 0) {
				opt = 6;
			}
			else if(opt == 1) {
				opt = 7;
			}
			else if(opt == 2) {
				GameState.DIFFICULTY ++;
				if(GameState.DIFFICULTY == 5) {
					GameState.DIFFICULTY = 0;
				}
			}
			else if(opt == 3) {
				GameState.DEBUG = !GameState.DEBUG;
			}
			else if(opt == 4) {
				GameState.LIGHTS = !GameState.LIGHTS;
			}
			else if(opt == 5) {
				intro = false;
				outro = true;
			}
			else if(opt == 6) {
				opt = 0;
			}
			else if(opt == 7) {
				opt = 1;
			}
		
	}
	
	@Override
	public boolean keyUp(int keycode) {
		if(keycode == Keys.LEFT || keycode == Keys.A) {
			val_x = 0;
		}
		if(keycode == Keys.RIGHT || keycode == Keys.D) {
			val_x = 0;
		}
		return false;
	}
	
	public boolean keyDown(int keycode) {
		
			if(keycode == Keys.ENTER) {
				doSelection();
			}
			if(keycode == Keys.DOWN || keycode == Keys.S) {
				changeSelectionY(1);
			}
			if(keycode == Keys.UP || keycode == Keys.W) {
				changeSelectionY(-1);
			}
			if(keycode == Keys.LEFT || keycode == Keys.A) {
				changeSelectionX(-1);
				val_x = -1;
			}
			if(keycode == Keys.RIGHT || keycode == Keys.D) {
				changeSelectionX(1);
				val_x = 1;
			}
		return false;
	}

	public boolean povMoved(Controller controller, int povCode, PovDirection value) {
		return false;
	}

	public boolean xSliderMoved(Controller controller, int sliderCode, boolean value) {
		return false;
	}

	public boolean ySliderMoved(Controller controller, int sliderCode, boolean value) {
		return false;
	}

	public boolean accelerometerMoved(Controller controller, int accelerometerCode, Vector3 value) {
		return false;
	}

	public void resize(int width, int height) {
		
	}

	@Override
	public void insideRender(SpriteBatch sb) {
		sb.begin();
		sb.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		bolinha.draw(sb);
		fogo.draw(sb);
		
		sb.draw(above_bar,
				(Gdx.graphics.getWidth() - above_bar.getWidth()*factor)/ 2, Gdx.graphics.getHeight() - above_bar.getHeight()*factor,
				above_bar.getWidth() * factor, above_bar.getHeight() * factor);
		
		sb.draw(main_sign,
				(Gdx.graphics.getWidth() - main_sign.getWidth()*factor)/ 2, 0,
				main_sign.getWidth() * factor, main_sign.getHeight() * factor);
		
		sb.draw(options_sign,
				(Gdx.graphics.getWidth() - options_sign.getWidth()*factor)/ 2, Gdx.graphics.getHeight() - options_sign.getHeight()*factor,
				options_sign.getWidth() * factor, options_sign.getHeight() * factor);
		
		//Light sign
		sb.draw(light_sign,
				light_body.getWorldCenter().x * 100f - light_sign.getWidth()/2f*factor,
				light_body.getWorldCenter().y * 100f - light_sign.getHeight()/2f*factor,
				light_sign.getWidth()/2f*factor,
				light_sign.getHeight()/2f*factor,
				light_sign.getWidth()*factor,
				light_sign.getHeight()*factor,
				1, 1,
				(float)Math.toDegrees(light_body.getAngle()),
				0, 0,
				light_sign.getWidth(),
				light_sign.getHeight(),
				false, false);
		
		sb.draw(left_arrow,
				light_body.getWorldCenter().x * 100f - left_arrow.getWidth()/2f*factor,
				light_body.getWorldCenter().y * 100f - left_arrow.getHeight()/2f*factor,
				left_arrow.getWidth()/2f*factor,
				left_arrow.getHeight()/2f*factor,
				left_arrow.getWidth()*factor,
				left_arrow.getHeight()*factor,
				left_light_scale, left_light_scale,
				(float)Math.toDegrees(light_body.getAngle()),
				0, 0,
				left_arrow.getWidth(),
				left_arrow.getHeight(),
				false, false);
		
		sb.draw(right_arrow,
				light_body.getWorldCenter().x * 100f - right_arrow.getWidth()/2f*factor,
				light_body.getWorldCenter().y * 100f - right_arrow.getHeight()/2f*factor,
				right_arrow.getWidth()/2f*factor,
				right_arrow.getHeight()/2f*factor,
				right_arrow.getWidth()*factor,
				right_arrow.getHeight()*factor,
				right_light_scale, right_light_scale,
				(float)Math.toDegrees(light_body.getAngle()),
				0, 0,
				right_arrow.getWidth(),
				right_arrow.getHeight(),
				false, false);
		
		Texture light_state = GameState.LIGHTS ? on : off;
		
		sb.draw(light_state,
				light_body.getWorldCenter().x * 100f - light_state.getWidth()/2f*factor,
				light_body.getWorldCenter().y * 100f - light_state.getHeight()/2f*factor,
				light_state.getWidth()/2f*factor,
				light_state.getHeight()/2f*factor,
				light_state.getWidth()*factor,
				light_state.getHeight()*factor,
				1, 1,
				(float)Math.toDegrees(light_body.getAngle()),
				0, 0,
				light_state.getWidth(),
				light_state.getHeight(),
				false, false);
		
		//Debug sign
		sb.draw(debug_sign,
				debug_body.getWorldCenter().x * 100f - debug_sign.getWidth()/2f*factor,
				debug_body.getWorldCenter().y * 100f - debug_sign.getHeight()/2f*factor,
				debug_sign.getWidth()/2f*factor,
				debug_sign.getHeight()/2f*factor,
				debug_sign.getWidth()*factor,
				debug_sign.getHeight()*factor,
				1, 1,
				(float)Math.toDegrees(debug_body.getAngle()),
				0, 0,
				debug_sign.getWidth(),
				debug_sign.getHeight(),
				false, false);
		
		sb.draw(left_arrow,
				debug_body.getWorldCenter().x * 100f - left_arrow.getWidth()/2f*factor,
				debug_body.getWorldCenter().y * 100f - left_arrow.getHeight()/2f*factor,
				left_arrow.getWidth()/2f*factor,
				left_arrow.getHeight()/2f*factor,
				left_arrow.getWidth()*factor,
				left_arrow.getHeight()*factor,
				left_debug_scale, left_debug_scale,
				(float)Math.toDegrees(debug_body.getAngle()),
				0, 0,
				left_arrow.getWidth(),
				left_arrow.getHeight(),
				false, false);
		
		sb.draw(right_arrow,
				debug_body.getWorldCenter().x * 100f - right_arrow.getWidth()/2f*factor,
				debug_body.getWorldCenter().y * 100f - right_arrow.getHeight()/2f*factor,
				right_arrow.getWidth()/2f*factor,
				right_arrow.getHeight()/2f*factor,
				right_arrow.getWidth()*factor,
				right_arrow.getHeight()*factor,
				right_debug_scale, right_debug_scale,
				(float)Math.toDegrees(debug_body.getAngle()),
				0, 0,
				right_arrow.getWidth(),
				right_arrow.getHeight(),
				false, false);
		
		Texture debug_state = GameState.DEBUG ? on : off;
		
		sb.draw(debug_state,
				debug_body.getWorldCenter().x * 100f - debug_state.getWidth()/2f*factor,
				debug_body.getWorldCenter().y * 100f - debug_state.getHeight()/2f*factor,
				debug_state.getWidth()/2f*factor,
				debug_state.getHeight()/2f*factor,
				debug_state.getWidth()*factor,
				debug_state.getHeight()*factor,
				1, 1,
				(float)Math.toDegrees(debug_body.getAngle()),
				0, 0,
				debug_state.getWidth(),
				debug_state.getHeight(),
				false, false);
		
		//desenha as barras de volume de SFX e musica
		
		sb.draw(bar,
				(Gdx.graphics.getWidth() - bar.getWidth()*factor)/ 2, 630*factor,
				bar.getWidth() * factor, bar.getHeight() * factor);
		
		sb.draw(small_cog,
				(Gdx.graphics.getWidth() - small_cog.getWidth()*factor)/ 2  + ((GameState.VOLUME - 0.5f) * (bar.getWidth()*factor*0.9f)),
				618*factor,
				small_cog.getWidth()/2f*factor,
				small_cog.getHeight()/2f*factor,
				small_cog.getWidth()*factor,
				small_cog.getHeight()*factor,
				1, 1,
				-((GameState.VOLUME - 0.5f) * (bar.getWidth()*factor)) * 10,
				0, 0,
				small_cog.getWidth(),
				small_cog.getHeight(),
				false, false);
		
		sb.draw(bar,
				(Gdx.graphics.getWidth() - bar.getWidth()*factor)/ 2, 450*factor,
				bar.getWidth() * factor, bar.getHeight() * factor);
		
		sb.draw(small_cog,
				(Gdx.graphics.getWidth() - small_cog.getWidth()*factor)/ 2  + ((GameMusic.MUSIC_VOLUME - 0.5f) * (bar.getWidth()*factor*0.9f)),
				438*factor,
				small_cog.getWidth()/2f*factor,
				small_cog.getHeight()/2f*factor,
				small_cog.getWidth()*factor,
				small_cog.getHeight()*factor,
				1, 1,
				-((GameMusic.MUSIC_VOLUME - 0.5f) * (bar.getWidth()*factor)) * 10,
				0, 0,
				small_cog.getWidth(),
				small_cog.getHeight(),
				false, false);
		
		//desenha as correntes
		drawChains(sb);
		
		sb.draw(back_cog,
				Gdx.graphics.getWidth()/2f - back_cog.getWidth()/2f*factor,
				-back_cog.getHeight()*factor/2f,
				back_cog.getWidth()/2f*factor,
				back_cog.getHeight()/2f*factor,
				back_cog.getWidth()*factor,
				back_cog.getHeight()*factor,
				1, 1,
				cog_angle,
				0, 0,
				back_cog.getWidth(),
				back_cog.getHeight(),
				false, false);
		//SELE��O
		
		sb.setColor(1, 0, 0, 1);
		
		//UPPER LEFT
		sb.draw(
				selection_tex,
				(float)currentBound.getX(),
				(float)(currentBound.getY() + currentBound.getHeight()) - selection_tex.getHeight()*factor,
				(float)currentBound.getWidth()/2f,
				-(float)currentBound.getHeight()/2f,
				selection_tex.getWidth()*factor,
				selection_tex.getHeight()*factor,
				1,
				1,
				0,
				0,
				0,
				selection_tex.getWidth(),
				selection_tex.getHeight(),
				true,
				false);
		
		//UPPER RIGHT
		sb.draw(
				selection_tex,
				(float)(currentBound.getX() + currentBound.getWidth()) - selection_tex.getWidth()*factor,
				(float)(currentBound.getY() + currentBound.getHeight()) - selection_tex.getHeight()*factor,
				-(float)currentBound.getWidth()/2f,
				-(float)currentBound.getHeight()/2f,
				selection_tex.getWidth()*factor,
				selection_tex.getHeight()*factor,
				1,
				1,
				0,
				0,
				0,
				selection_tex.getWidth(),
				selection_tex.getHeight(),
				false,
				false);
		
		//BOTTOM LEFT
		sb.draw(
				selection_tex,
				(float)currentBound.getX(),
				(float)currentBound.getY(),
				(float)currentBound.getWidth()/2f,
				(float)currentBound.getHeight()/2f,
				selection_tex.getWidth()*factor,
				selection_tex.getHeight()*factor,
				1,
				1,
				0,
				0,
				0,
				selection_tex.getWidth(),
				selection_tex.getHeight(),
				true,
				true);
		
		//BOTTOM RIGHT
		sb.draw(
				selection_tex,
				(float)(currentBound.getX() + currentBound.getWidth()) - selection_tex.getWidth()*factor,
				(float)currentBound.getY(),
				-(float)currentBound.getWidth()/2f,
				(float)currentBound.getHeight()/2f,
				selection_tex.getWidth()*factor,
				selection_tex.getHeight()*factor,
				1,
				1,
				0,
				0,
				0,
				selection_tex.getWidth(),
				selection_tex.getHeight(),
				false,
				true);
		
		sb.setColor(1, 1, 1, 1);
		
		sb.end();
		
		//b2dr.render(world, camera.combined);
		
	}

	public void changeScreen() {
		manager.changeState(5);
		
	}

}