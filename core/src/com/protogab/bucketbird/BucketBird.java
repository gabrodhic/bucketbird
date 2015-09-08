package com.protogab.bucketbird;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import javax.crypto.SealedObject;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Input.Peripheral;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.I18NBundleLoader;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.audio.Music.OnCompletionListener;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.CatmullRomSpline;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.Timer;






public class BucketBird extends ApplicationAdapter implements ApplicationListener,InputProcessor{
	///////////////CONSTANTS//////////////////	
	private FPSLogger fpsLogger;
	private Preferences preferences;
	public static final String PREFS_NAME = "bucketbird";
	private static final String PREF_CURRENT_LEVEL = "currentlevel";
	private static final String PREF_LAUNCH_COUNT = "launchcount";
	public static final String PREF_GAMESERVICES_USED = "gpgsused";
	public static final String PREF_CURRENT_SCORE = "currentscore";	
	
	public static final String LEADERBOARD_ID = "CgkIvvacvrwIEAIQBw";
	
	// constant useful for logging
    public static final String LOG = BucketBird.class.getSimpleName();
    
    // whether we are in development mode
    public static final boolean DEV_MODE = true;
	
    private static final int MAXIMUM_LEVEL_NUMBER = 100;
	
	private static final int GAME_SCREEN_LOADING = 1;
	private static final int GAME_SCREEN_PLAYING = 2;
	private static final int GAME_SCREEN_ENDED = 3;
	private static final int GAME_SCREEN_ENDMENU = 4;
	private static final int GAME_SCREEN_POPUP = 5;

	
	private static final int PATH_TYPE_NONE = 0;
	private static final int PATH_TYPE_LINEAL = 1;
	private static final int PATH_TYPE_SPLINE = 2;
	
	//WARNING CHANGING ORDER MAY AFFECT OTHER VARIABLES
	private static final int MOVE_NAME_ANY = 0;
	private static final int MOVE_NAME_FIGHT = 1;
	private static final int MOVE_NAME_FIGHT2 = 2;
	private static final int MOVE_NAME_PASSBY = 3;
	private static final int MOVE_NAME_FEINT = 4;
	private static final int MOVE_NAME_FREEFALL = 5;
	private static final int MOVE_NAME_FEINT_DOUBLE = 6;
	private static final int MOVE_NAME_FEINT_GAMOVR = 7;
	private static final int MOVE_NAME_JUMP = 8;
	private static final int MOVE_NAME_ESCAPE = 9;
	private static final int MOVE_NAME_BUCKET_MOVE = 10;
	private static final int MOVE_NAME_JUMP_SPRING = 11;
	private static final int MOVE_NAME_BUCKET_MOVEREMOVE = 12;
	private static final int MOVE_NAME_BUCKET_CARRY = 13;
	
	
	private static final String[] MOVE_NAMES = {"MOVE_NAME_ANY","MOVE_NAME_FIGHT","MOVE_NAME_FIGHT2","MOVE_NAME_PASSBY","MOVE_NAME_FEINT","MOVE_NAME_FREEFALL","MOVE_NAME_FEINT_DOUBLE","MOVE_NAME_FEINT_GAMOVR","MOVE_NAME_JUMP","MOVE_NAME_ESCAPE", "MOVE_NAME_BUCKET_MOVE","MOVE_NAME_JUMP_SPRING","MOVE_NAME_BUCKET_MOVEREMOVE"};
		
	private static final int POSITION_TYPE_NORMAL = 1;
	private static final int POSITION_TYPE_FLYBY = 2;
	private static final int POSITION_TYPE_SMILE = 3;
	private static final int POSITION_TYPE_TONGUE= 4;
	private static final int POSITION_TYPE_HURT= 5;
	private static final int POSITION_TYPE_DEAD= 6;
	
	private static final int SEAL_POSITION_TYPE_STAND = 1;
	private static final int SEAL_POSITION_TYPE_WALK = 2;
	private static final int SEAL_POSITION_TYPE_STOP = 3;
	private static final int SEAL_POSITION_TYPE_SKI = 4;
	
	private static final int BOLINDX_RNDMOV_TRUE = 0;
	private static final int BOLINDX_RNDMOV_FALSE = 1;
	private static final int BOLINDX_RNDMOVSUB_TRUE = 2;
	private static final int BOLINDX_RNDMOVSUB_FALSE = 3;
	private static final int BOLINDX_PAUSEMOV_TRUE = 4;
	private static final int BOLINDX_PAUSEMOV_FALSE = 5;
	private static final int BOLINDX_HELPOINT_TRUE = 6;
	private static final int BOLINDX_HELPOINT_FALSE = 7;
	private static final int BOLINDX_FGHTYPEMOV_TRUE = 8;
	private static final int BOLINDX_FGHTYPEMOV_FALSE = 9;
	private static final int BOLINDX_VARIANCECHAN_TRUE = 10;
	private static final int BOLINDX_VARIANCECHAN_FALSE = 11;
	private static final int BOLINDX_VARIANCEHORVERT_TRUE = 12;
	private static final int BOLINDX_VARIANCEHORVERT_FALSE = 13;
	private static final int BOLINDX_SHOWSHIELD_TRUE = 14;
	private static final int BOLINDX_SHOWSHIELD_FALSE = 15;
	private static final int BOLINDX_SHOWADS_TRUE = 16;
	private static final int BOLINDX_SHOWADS_FALSE = 17;
	private static final int BOLINDX_PLAYMUSIC_TRUE = 18;
	private static final int BOLINDX_PLAYMUSIC_FALSE = 19;
	private static final int BOLINDX_SHOWINVISIBLE_TRUE = 20;
	private static final int BOLINDX_SHOWINVISIBLE_FALSE = 21;
	private static final int BOLINDX_SEALMOVENAME_TRUE = 22;
	private static final int BOLINDX_SEALMOVENAME_FALSE = 23;
	private static final int BOLINDX_BOMBSIZE_TRUE = 24;
	private static final int BOLINDX_BOMBSIZE_FALSE = 25;
	private static final int BOLINDX_BIRDWHISTLE_TRUE = 26;
	private static final int BOLINDX_BIRDWHISTLE_FALSE = 27;
	private static final int BOLINDX_FEINTTYPE_TRUE = 28;
	private static final int BOLINDX_FEINTTYPE_FALSE = 29;
	private static final int BOLINDX_SEALMOVENAME2_TRUE = 30;
	private static final int BOLINDX_SEALMOVENAME2_FALSE = 31;

	
	private static final int VARIANCE_TYPE_HIGH = 1;
	private static final int VARIANCE_TYPE_LOW = 2;
	private static final int CARRYBUCKET_TOTAL_MOVES = 12;
	
	private static final float FLAP_ANIMATION_SPEED = 0.05f;
	private static final float WALK_ANIMATION_SPEED = 0.2f;
	
	
	// 1 / 60... lets use a fixed time_step instead of Gdx.graphics.getDeltaTime() for some cases
	// since this was causing some misbehaviour after game init() expecially
	//http://badlogicgames.com/forum/viewtopic.php?f=11&splineTime=13181&start=10
	private static final float TIME_STEP = 0.01666666666666666666666666666667f;
	
	I18NBundle langBundle;//Internationalization
	
	
	BitmapFont 						fontWinFail;
	BitmapFont 						fontpopUp;
	BitmapFont 						fontSocoreBig;
	BitmapFont 						fontTimer;
	
	Texture						loadingTexture;
	Texture						tempTexture;
	
	
	TextureRegion               backgroundTexture;	
	
	TextureRegion                     playButtonTextureRegion;
	TextureRegion                     scoreButtonTextureRegion;
	TextureRegion                     facebookButtonTextureRegion;
	TextureRegion                     shareButtonTextureRegion;
	TextureRegion                     scoreBoardTextureRegion;
	TextureRegion     				  popupTextureRegion;
	TextureRegion     				  OkButtonTextureRegion;
	TextureRegion     				  CancelButtonTextureRegion;
	TextureRegion                     infoTextureRegion;
	TextureRegion                     moreTextureRegion;
	
	TextureRegion                     clockTextureRegion;
	TextureRegion                     tapInstrucTextureRegion;
	TextureRegion                     levelLockedTextureRegion;
	TextureRegion                     levelUnlockedTextureRegion;
	TextureRegion                     levelCompletedTextureRegion;
	TextureRegion                     levelTextureRegion;
	
	
	TextureRegion                     bucketTextureRegion;
	TextureRegion                     bucketWaterTextureRegion;
	TextureRegion                     bucketBottomWaterTextureRegion;
	TextureRegion                     waterExplosionTextureRegion;
	TextureRegion                     splashWaterTextureRegion;
	TextureRegion                     splashObjectTextureRegion;
	TextureRegion                     splashFloorTextureRegion;
	TextureRegion                     waterBalloonTextureRegion;
	TextureRegion                     ballonRedTextureRegion;
	TextureRegion                     ballonYellowTextureRegion;
	TextureRegion                     ballonGreenTextureRegion;
	TextureRegion                     ballonBlueTextureRegion;
	TextureRegion                     ballonPurpleTextureRegion;
	
	
	
	TextureRegion               flybyUpTexture;
	TextureRegion               flybyDownTexture;
	TextureRegion               normalUpTexture;
	TextureRegion               normalDownTexture;
	TextureRegion               hurtUpTexture;
	TextureRegion               hurtDownTexture;
	TextureRegion               smileUpTexture;
	TextureRegion               smileDownTexture;
	TextureRegion               tongueUpTexture;
	TextureRegion               tongueDownTexture;
	TextureRegion               springBoardTexture;
	TextureRegion               faucetTexture;
	
	TextureRegion[][]               sealSpriteTexture;
	TextureRegion[][]               sealSpriteTexture2;
	TextureRegion[][]               birdFlySpriteTexture;
	TextureRegion               flyBombUpTexture;
	TextureRegion               flyBombDownTexture;
	
	
	TextureRegion 				animMosquitoTextureRegion;	
	TextureRegion 				animBombTextureRegion;
	TextureRegion 				animSealTextureRegion;
	
	
	Rectangle 					loadingRectangle;
	
	Rectangle 					scoreBoardRectangle;
	Rectangle 					popUpRectangle;
	Rectangle 					okButtonRectangle;
	Rectangle 					cancelButtonRectangle;
	Rectangle 					playAgainRectangle;
	Rectangle 					shareScoreRectangle;
	Rectangle 					shareFacebookRectangle;
	Rectangle 					shareAppRectangle;
	Rectangle 					infoRectangle;
	Rectangle 					moreRectangle;
	Rectangle 					timerRectangle;
	Rectangle 					shotsRectangle;
	
	Rectangle 					bombIconRectangle;
	Rectangle 					bucketIconRectangle;
	Rectangle 					clockIconRectangle;
	Rectangle 					swatterRectangle;
	Rectangle 					clockRectangle;
	Rectangle 					shieldRectangle;
	
	Rectangle 					mosquitoRectangle;
	Rectangle 					sealRectangle;
	Rectangle 					bombRectangle;
	Rectangle 					bucketRectangle;
	Rectangle 					bucketLevelRectangle;
	Rectangle 					bucketLockRectangle;
	Rectangle 					bucketWaterRectangle;
	Rectangle 					bucketBottomWaterRectangle;
	Rectangle 					waterExplosionRectangle;
	Rectangle 					waterFallingRectangle;
	Rectangle 					waterFloorRectangle;
	Rectangle 					waterBalloonRectangle;
	Rectangle 					springboardRectangle;
	Rectangle 					faucetRectangle;
	
	Animation 						normalAnimation;
	Animation 						hurtAnimation;
	Animation 						tongueAnimation;
	Animation 						smileAnimation;
	Animation 						flybyAnimation;
	
	Animation 						sealWalkAnimation;
	Animation 						sealCarryOnAnimation;
	Animation 						detonationAnimation;
	
	Music splineMusic;	
	Music slowDJMusic;
	
	
	Sound djEndSound;
	Sound splashSound;	
	Sound freefallSound;
	Sound gruntSound;
	
	Sound birdWhistleSound;
	Sound woofingSound;
		
	Sound bamSound;
	Sound bam2Sound;			

	Music walkingMusic;
	Sound warningSound;
	Sound pointSound;
	Sound popBucketSound;
	Sound popFloorSound;	
	Sound winPointSound;
	Sound jumpSound;	
	Sound ouchSound;
	
	AssetManager manager;
	
	////////////////////////////////////VARIABLES////////////////////////////////
	float loadingAlpha = 0; 
	int GAME_DURATION = 80;//seconds
	int FAIL_COUNT_LIMIT = 15;//seconds	
	int TOTAL_MOSQUITOS = 15;
	
	
	Vector2 linealPosition = new Vector2();
	Vector2 linealVelocity = new Vector2();
	Vector2 linealMovement = new Vector2();
	ArrayList<Vector2> linealTouch = new ArrayList<Vector2>();
	//Vector2 linealTouch[] = new Vector2[20];//Although we use two points vectors for target, only one point can be used. It just depends on the move type.
	Vector2 linealDir = new Vector2();
	
	int currentIndexTouchVector = 0;
	int currentSealIndexTouchVector = 0;
	
	
	Vector2 linealSealPosition = new Vector2();
	Vector2 linealSealVelocity = new Vector2();
	Vector2 linealBombMovement = new Vector2();
	ArrayList<Vector2> linealSealTouch = new ArrayList<Vector2>();
	Vector2 linealSealDir = new Vector2();
	
	float linealSpeed;//When traveling linearly
	float linealSealSpeed;//When traveling linearly
    float splineSpeed;//When traveling in spline
    
    float linealSpeedLevelFactor;
    float splineSpeedLevelFactor;
    
    float rotationSpeed = 400f;
    
    float maxSplineSpeed;
    float minSplineSpeed;
    
    boolean fixedAngle = false;
    float mosquitoAngle;
    float sealAngle;
    
    CatmullRomSpline<Vector2> splinePath;
    Vector2 splinePosition = new Vector2();
    float splineTime = 0;
    Vector2 currentPath[];
    
    float maxLastPointX; //This is just a variable indicator to know when we have reached the end
    float minLastPointX; 
    float maxLastPointY;
    float minLastPointY; 
    
    int pathType = PATH_TYPE_NONE;
    int moveName;
    int sealMoveName;
    int positionType;
    int sealPositionType = SEAL_POSITION_TYPE_WALK;
	
	Random randGen;	   

	int screen_width = 0 ;
	int screen_height = 0; 
	
	int currentGameScreen = GAME_SCREEN_LOADING;
	
	SpriteBatch batch;
	OrthographicCamera camera;
	
	TextureAtlas 				atlas; //Generated with PackerTextureDesktop.java https://github.com/libgdx/libgdx/wiki/Texture-packer
	
	float accelX;
	float accelY;
	float accelZ;
	
	long standByTimeStart = 0;
	long standByTimeCount = 0;
	int  standByTimeWait = 1;//Seconds
	
	long sealStandByTimeStart = 0;
	long sealStandByTimeCount = 0;
	int  sealStandByTimeWait = 3;//Seconds
	
	long movePauseTimeStart = 0;
	long movePauseTimeCount = 0;
	long movePauseTimeWait;// 1000000000 = Seconds
	
	long sealMovePauseTimeStart = 0;
	long sealMovePauseTimeCount = 0;
	long sealMovePauseTimeWait;
	
	long feintMovePauseTimeWaitLevelTime;//Use to control movePauseTimeWait depending on the level number
	long fightmeMovePauseTimeWaitLevelTime;//Use to control movePauseTimeWait depending on the level number
	
	long failCountMessageTimeStart = 0;
	long failCountMessageTimeCount = 0;
	long failCountMessageTimeWait = 500000000;// 1000000000 = Seconds
	
	long winCountMessageTimeStart = 0;
	long winCountMessageTimeCount = 0;
	long winCountMessageTimeWait = 500000000;// 1000000000 = Seconds
	
	long clockCountMessageTimeStart = 0;
	long clockCountMessageTimeCount = 0;
	long clockCountMessageTimeWait = 500000000;// 1000000000 = Seconds
	
	long feintOverTimeStart = -1;
	long feintOverTimeCount = 0;
	long feintOverTimeWait = 300000000;// 1000000000 = Seconds
	boolean feintOverUpDown = false;
	
	
	long gameTimeTrack = 0L;
	long gameTimeStart = 0L;
	long gameTimeEnded = 0L;
	
	boolean rotateMosquito = false;
	boolean pauseMovingEnabled = false;
	boolean sealPauseMovingEnabled = false;
	List<Boolean> sealPauseMovingEnabledArr = new ArrayList<Boolean>();
	
	
	boolean touchEnabled = false;

	List<Float> movePauseTimes = new ArrayList<Float>();
	int movePuseCurrentIndex = 0;
	
	String appPackageName;
	
	float currentDeltaTime = 0f;
	float deltaDetonationTime = 0f;
	long detonationTimeTrack = 0L;//Bomb
	long detonationTimeStart = 0L;//Bomb
	
	long splashTimeTrack = 0L;//Seal
	long splashTimeStart = 0L;//Seal
	
	float splashAlphaValue = 1f;
	
	int moveCounts = 0;
	int fightMeCountsIncrementor;
	int playsCount = 0;
	int doubleKillCount = 0;
	
	Integer[] moveNameCounts = new Integer[10];
	Integer[] moveNameAssertionCounts = new Integer[20];
	//List<Integer> moveNameCounts = new ArrayList<Integer>();
	//List<Integer> moveNameAssertionCounts = new ArrayList<Integer>();
	
	int currentScore = 0;
	int currentScoreFails = 0;
	int currentMoveFails = 0; //Holds the number of fail attempts on the current move, either passby, fightme, feint
	
	//This is used to control memory booleans, 
	//each 2 position should be used for true and false, ie: [0] = true, [1] = false, [2] = true, [3] = false, [4] = true, [5] = false...
	int[] memoryBooleans = new int[100];
	int truesesCount = 0;
	int falsesCount = 0;
	
	boolean showTimeout = false;
	
	
	boolean detonateBomb = false;
	boolean stopFlyingBomb = false;
	
	boolean showShield = false;
	boolean showDoubleKill = false;
	boolean showInvisible = false;
	
	boolean featureFightMe2Available = false;
	boolean featureBombAvailable = false;
	boolean featureShieldAvailable = false;
	boolean featureDoubleFeintAvailable = false;
	boolean featureDoubleKillAvailable = false;
	boolean featureInvisibleAvailable = false;
	
	
	boolean showClock = false;
	boolean showSwatter = false;
	
	boolean timeTrackingEnabled = true;
	boolean inputEnabled = true;
	boolean gamingEnabled;
	
	
	int bombScoreValue;
	int currentLevel;
	boolean gameWin = false;
	String gameEndInfo = "";
	int launchcount;
	
	boolean stopDJMusic = false;
	float lastTimeDJMusic = -1f;
	
	float lastTapInstrucDeltaTime = 0f;
	boolean showTapInstruc = true;
	boolean firstTapCountedInstruc = false;
	
	boolean showChallengeInvitation = true;
	
	public boolean showAppFeedback = false;
	
	public ActionResolver actionResolver;
	//public GoogleServicesAnalytics gsAnalytics;
	//public GoogleServicesAds gsa; //GoogleServicesAds
	
	
	//Constructor
	public BucketBird(){

	}

	public BucketBird(ActionResolver ar){
		 this.actionResolver = ar;

	}
	
	@Override
	public void create () {
		fpsLogger = new FPSLogger();
		
		if(DEV_MODE == false) Gdx.app.setLogLevel(Application.LOG_NONE);        
        Gdx.app.log( LOG, "Creating game" );
        Gdx.input.setInputProcessor(this);
        Gdx.input.setCatchBackKey(true);
		
		 appPackageName =  BucketBird.this.getClass().getPackage().getName()+".android";
		 //Texture.setEnforcePotImages(false);
		 
	 	// create the camera and the SpriteBatch
		camera = new OrthographicCamera();
		
		batch = new SpriteBatch();
		
		//Loading image
		loadingTexture = new Texture(Gdx.files.internal("loading.png")); loadingTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		//tempTexture = new Texture(Gdx.files.internal("broken_screen.png")); tempTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		preferences = Gdx.app.getPreferences(PREFS_NAME);
		 launchcount =  preferences.getInteger( PREF_LAUNCH_COUNT, 0);
		launchcount++;
		preferences.putInteger( PREF_LAUNCH_COUNT, launchcount);
		preferences.flush();
				
		 //Only If the user has already used GPGS before in the past then auto login...(like flappy bird style), otherwise manual login when needed
		 if(preferences.getBoolean( PREF_GAMESERVICES_USED, false) == true) {
			 if(actionResolver.getSignedInGPGS()==false) actionResolver.loginGPGS();
		 }

		 
		manager = new AssetManager();
		
		//Textures
		manager.load("mypack.atlas", TextureAtlas.class);		//atlas = new TextureAtlas(Gdx.files.internal("mypack.atlas"));		
		
		//Music and Sounds
		manager.load("spline.ogg", Music.class);				//splineMusic = Gdx.audio.newMusic(Gdx.files.internal("test.mp3"));					
		manager.load("djmusic_slow.ogg", Music.class);
		
		manager.load("djmusic_end.ogg", Sound.class);
		manager.load("splash.ogg", Sound.class);
		
		manager.load("freefall.ogg", Sound.class);
		manager.load("grunt.ogg", Sound.class);		
		manager.load("bird_whistle.ogg", Sound.class);
		manager.load("woofing1.ogg", Sound.class);
				
		manager.load("bam.ogg", Sound.class);
		manager.load("bam2.ogg", Sound.class);					
		manager.load("walking.ogg", Music.class);
		manager.load("warning.ogg", Sound.class);
		manager.load("point.ogg", Sound.class);
		manager.load("pop_bucket.ogg", Sound.class);
		manager.load("pop_floor.ogg", Sound.class);		
		manager.load("win_point.ogg", Sound.class);
		manager.load("jump.ogg", Sound.class);		
		manager.load("ouch.ogg", Sound.class);
		
		 Locale locale = new Locale(Locale.getDefault().getLanguage());							
		manager.load("langBundle", I18NBundle.class , new I18NBundleLoader.I18NBundleParameter(locale));

		 
	}

	@Override
	public void resize(int width, int height) {
		 
		 screen_width = Gdx.graphics.getWidth();
		 screen_height = Gdx.graphics.getHeight();
		 

		camera.setToOrtho(false, screen_width, screen_height);
		batch = new SpriteBatch();
		
		
		loadingRectangle = new Rectangle();
		loadingRectangle.width = screen_width*0.7f;//Varies
		loadingRectangle.height =  loadingRectangle.width*0.16f;
		loadingRectangle.x = (screen_width*0.5f)-(loadingRectangle.width*0.5f);
		loadingRectangle.y = (screen_height*0.5f)-(loadingRectangle.height*0.5f);
		 
		
		//Mosquito Main
		 mosquitoRectangle = new Rectangle();
		 mosquitoRectangle.width = screen_width*0.30f;  // 96f;  //screen_width*0.3f;//Varies
		 mosquitoRectangle.height =  mosquitoRectangle.width*0.6f;  /// 115f;//mosquitoRectangle.width*1.2f;//varies
		 mosquitoRectangle.x = 0;//Varies
		 mosquitoRectangle.y = 0;//Varies
		 

		 Gdx.app.log( LOG, "mosca widh height " + mosquitoRectangle.width + " / " + mosquitoRectangle.height );
		 
		 
		 maxSplineSpeed = 0.576f; //0.576f  //NOTICE: FIXED SPEED FOR ALL SCREEN SIZES  /// (mosquitoRectangle.height*0.005f);
		 		 
		 minSplineSpeed = maxSplineSpeed*0.5f;	//0.7f  //0.4f
		 splineSpeed = minSplineSpeed;
		 
		 maxLastPointX = screen_width+  (mosquitoRectangle.height*1f);
		 minLastPointX = 0 - (mosquitoRectangle.height*1f);
		 maxLastPointY  = screen_height+  (mosquitoRectangle.height*1f);
		 minLastPointY = 0 - (mosquitoRectangle.height*1f);
		
		 

		 scoreBoardRectangle = new Rectangle();
		 scoreBoardRectangle.width = screen_width*(0.8f);				 
		 scoreBoardRectangle.height = scoreBoardRectangle.width*1.25f;
		 if( scoreBoardRectangle.height > (screen_height*0.8f)) scoreBoardRectangle.height = screen_height*0.6f;
		 scoreBoardRectangle.x = (screen_width*(0.5f))-(scoreBoardRectangle.width*0.5f);
		 scoreBoardRectangle.y = (screen_height*(0.5f))-(scoreBoardRectangle.height*0.5f);
		 
		 //This is the first level bucket, we copy this one for all the other levels
		 bucketLevelRectangle = new Rectangle();
		 bucketLevelRectangle.width = scoreBoardRectangle.width*(0.16f);//20= 100% / 5(levels) = 20 - 4(margin) = 16				 
		 bucketLevelRectangle.height = bucketLevelRectangle.width*1.1f;
		 bucketLevelRectangle.x = scoreBoardRectangle.x + (bucketLevelRectangle.width*0.3f);
		 bucketLevelRectangle.y = ((scoreBoardRectangle.y))+(bucketLevelRectangle.height*1.8f);

		 bucketLockRectangle = new Rectangle();
		 bucketLockRectangle.width = bucketLevelRectangle.width*(0.4f);				 
		 bucketLockRectangle.height = bucketLockRectangle.width*1.3f;
		 bucketLockRectangle.x = (bucketLevelRectangle.x +bucketLevelRectangle.width) -(bucketLockRectangle.width*0.5f);
		 bucketLockRectangle.y = ((bucketLevelRectangle.y+bucketLevelRectangle.height))-(bucketLockRectangle.height*0.5f);
		 
		 
		 
		playAgainRectangle = new Rectangle();
		playAgainRectangle.width = ((float)screen_width*0.27f);
		playAgainRectangle.height = ((float)screen_width*0.27f);
		playAgainRectangle.x = (scoreBoardRectangle.x+ (playAgainRectangle.width*0.25f));//- ((playAgainRectangle.width*0.5f) );
		playAgainRectangle.y = ((scoreBoardRectangle.y))- ((playAgainRectangle.height*0.6f) );
		
		
		shareScoreRectangle = new Rectangle();
		shareScoreRectangle.width =  playAgainRectangle.width;
		shareScoreRectangle.height = playAgainRectangle.height;
		shareScoreRectangle.x = (scoreBoardRectangle.x + scoreBoardRectangle.width) - (shareScoreRectangle.width*1.25f) ;
		shareScoreRectangle.y = playAgainRectangle.y;
		
		
		 popUpRectangle = new Rectangle();
		 popUpRectangle.width = screen_width*(0.8f);				 
		 popUpRectangle.height = popUpRectangle.width*0.6f;
		 popUpRectangle.x = (screen_width*(0.5f))-(popUpRectangle.width*0.5f);
		 popUpRectangle.y = (screen_height*(0.5f))-(popUpRectangle.height*0.5f);
		 
		 
		 okButtonRectangle = new Rectangle();
		okButtonRectangle.width =  playAgainRectangle.width*0.6f;
		okButtonRectangle.height = playAgainRectangle.height*0.6f;
		okButtonRectangle.x = (popUpRectangle.x+ popUpRectangle.width)- ((okButtonRectangle.width*1.25f) );
		okButtonRectangle.y = ((popUpRectangle.y))- ((okButtonRectangle.height*0.5f) );
		 
		cancelButtonRectangle = new Rectangle();
		cancelButtonRectangle.height = popUpRectangle.height*0.12f;
		cancelButtonRectangle.width =  cancelButtonRectangle.height;		
		cancelButtonRectangle.x = popUpRectangle.x+ (cancelButtonRectangle.width*0.2f);
		cancelButtonRectangle.y = ((popUpRectangle.y+popUpRectangle.height))- ((cancelButtonRectangle.height*1.2f) );
		
		/*cancelButtonRectangle = new Rectangle();
		cancelButtonRectangle.width =  playAgainRectangle.width*0.6f;
		cancelButtonRectangle.height = playAgainRectangle.height*0.6f;
		cancelButtonRectangle.x = (popUpRectangle.x+ popUpRectangle.width)- ((cancelButtonRectangle.width*1.2f) );
		cancelButtonRectangle.y = ((popUpRectangle.y))- ((cancelButtonRectangle.height*0.5f) );*/
 
		
		
		shareAppRectangle = new Rectangle();
		shareAppRectangle.width = (playAgainRectangle.width*0.40f);
		shareAppRectangle.height = (playAgainRectangle.height*0.40f);	
		shareAppRectangle.x = shareAppRectangle.width*0.5f;//screen_width*0.2f;// playRectangle.x;//screen_width-(shareAppRectangle.width*1.5f);
		shareAppRectangle.y = (shareAppRectangle.height*0.25f);
		
		

		infoRectangle = new Rectangle();
		infoRectangle.width = (playAgainRectangle.width*0.40f);
		infoRectangle.height = (playAgainRectangle.height*0.40f);
		infoRectangle.x = screen_width - (infoRectangle.width*1.2f); 
		infoRectangle.y = screen_height - (infoRectangle.width*1.2f);
		
		shareFacebookRectangle = new Rectangle();
		shareFacebookRectangle.width = (playAgainRectangle.width*0.40f);
		shareFacebookRectangle.height = (playAgainRectangle.height*0.40f);
		shareFacebookRectangle.x = screen_width - (infoRectangle.width*1.2f); 
		shareFacebookRectangle.y = shareAppRectangle.y;
		
		moreRectangle = new Rectangle();	
		moreRectangle.width = (playAgainRectangle.width*0.40f);
		moreRectangle.height = (playAgainRectangle.height*0.40f);
		moreRectangle.x = shareAppRectangle.x;
		moreRectangle.y = screen_height - (shareAppRectangle.height*1.25f);
		
		
		
		 timerRectangle = new Rectangle();
		 timerRectangle.height = (screen_height*0.1f);
		 timerRectangle.y = screen_height-timerRectangle.height;
		 timerRectangle.width = timerRectangle.height*2.5f;
		 timerRectangle.x = screen_width-timerRectangle.width;
		 
		 bombIconRectangle = new Rectangle();
		 bombIconRectangle.height = (screen_width*0.05f);
		 bombIconRectangle.y = screen_height-(bombIconRectangle.height*1.2f);
		 bombIconRectangle.width = bombIconRectangle.height*0.8f;
		 bombIconRectangle.x = 0+(bombIconRectangle.width*0.2f);

		 clockIconRectangle = new Rectangle();
		 clockIconRectangle.height = bombIconRectangle.height;
		 clockIconRectangle.y = bombIconRectangle.y;
		 clockIconRectangle.width = clockIconRectangle.height;
		 clockIconRectangle.x = screen_width-(clockIconRectangle.width*4.2f);
		
		 
		 bucketIconRectangle = new Rectangle();
		 bucketIconRectangle.height = bombIconRectangle.height;
		 bucketIconRectangle.y = bombIconRectangle.y;
		 bucketIconRectangle.width = bucketIconRectangle.height*0.8f;
		 bucketIconRectangle.x = (screen_width/2) -(bucketIconRectangle.width*0.5f);
		 
		 
		 swatterRectangle = new Rectangle();
		 swatterRectangle.height = mosquitoRectangle.height*0.6f;
		 swatterRectangle.y = 0;
		 swatterRectangle.width = swatterRectangle.height/2;
		 swatterRectangle.x = 0;
		 
		 clockRectangle = new Rectangle();
		 clockRectangle.height = mosquitoRectangle.height*0.4f;
		 clockRectangle.width = clockRectangle.height*0.7f;
		 clockRectangle.y = 0;		 
		 clockRectangle.x = 0;
		
		 
		 shieldRectangle = new Rectangle();
		 shieldRectangle.height = mosquitoRectangle.height*0.58f;
		 shieldRectangle.width = shieldRectangle.height*0.8f;
		 shieldRectangle.y = 0;		 
		 shieldRectangle.x = 0;
		 
		 
		 springboardRectangle = new Rectangle();
		 springboardRectangle.width = screen_width*0.34f;
		 springboardRectangle.height = springboardRectangle.width*0.25f;
		 springboardRectangle.y = screen_height*0.4f;		 
		 springboardRectangle.x = screen_width - springboardRectangle.width;//0;
		 
		 
		 faucetRectangle = new Rectangle();
		 faucetRectangle.width = screen_width*0.15f;
		 faucetRectangle.height = faucetRectangle.width*1.68f;
		 faucetRectangle.y = screen_height*0.27f;		 
		 faucetRectangle.x = screen_width - (faucetRectangle.width*1.3f);//0;
		 
		 waterBalloonRectangle = new Rectangle();
		 waterBalloonRectangle.width = mosquitoRectangle.width*0.37f;
		 waterBalloonRectangle.height = waterBalloonRectangle.width*1.4f;
		 waterBalloonRectangle.y = 0;		 
		 waterBalloonRectangle.x = 0;

		 
		 bombRectangle = new Rectangle();
		 bombRectangle.width = waterBalloonRectangle.width;
		 bombRectangle.height = waterBalloonRectangle.height;
		 bombRectangle.y = 0;		 
		 bombRectangle.x = 0;
		 
		 
		 waterExplosionRectangle = new Rectangle();
		 waterExplosionRectangle.width = waterBalloonRectangle.width*0.5f;//Changeable
		 waterExplosionRectangle.height = waterExplosionRectangle.width*1.0f;//Changeable
		 waterExplosionRectangle.y = 0;		 
		 waterExplosionRectangle.x = 0;
		 
		 waterFallingRectangle= new Rectangle();
		 waterFallingRectangle.width = waterBalloonRectangle.width*0.7f;
		 waterFallingRectangle.height = waterBalloonRectangle.height*2.0f;//Changeable
		 waterFallingRectangle.y = 0;		 
		 waterFallingRectangle.x = 0;
		 
		 bucketRectangle = new Rectangle();
		 bucketRectangle.width = waterBalloonRectangle.width*2.3f;
		 bucketRectangle.height = bucketRectangle.width*1.0f;//Changeable
		 bucketRectangle.y = 0;
		 bucketRectangle.x = 0 - bucketRectangle.width; //(screen_width*0.5f) - (bucketRectangle.width*0.5f);
		 
		 bucketWaterRectangle = new Rectangle();
		 bucketWaterRectangle.width = bucketRectangle.width*0.98f;
		 bucketWaterRectangle.height = 0f;//Changeable
		 bucketWaterRectangle.y = bucketRectangle.height*0.05f;		 
		 bucketWaterRectangle.x = 0;
		 
		 bucketBottomWaterRectangle = new Rectangle();
		 bucketBottomWaterRectangle.width = bucketRectangle.width*0.98f;
		 bucketBottomWaterRectangle.height = bucketRectangle.height * 0.1f;
		 bucketBottomWaterRectangle.y = bucketRectangle.y;		 
		 bucketBottomWaterRectangle.x = 0;//changebale
		 
		 
		 sealRectangle = new Rectangle();
		 sealRectangle.width = bucketRectangle.width*0.9f;//Changeable
		 sealRectangle.height =  sealRectangle.width*1.0f;//Changeable
		 sealRectangle.y = 0;		 
		 sealRectangle.x = 0;
		  
		 
		 waterFloorRectangle = new Rectangle();
		 waterFloorRectangle.width = waterFallingRectangle.width*1.0f;////Changeable
		 waterFloorRectangle.height = waterFloorRectangle.width*0.3f;//Changeable
		 waterFloorRectangle.y = 0;		 
		 waterFloorRectangle.x = 0;
		 
		
	}
	
	/*
	 * Inittializes the game for the first time. ie: Set resources after create().
	 */
	void init() {
		
		 Gdx.app.log( LOG, "call INIT");
		 

		 		
		langBundle =  manager.get("langBundle",  I18NBundle.class);
		actionResolver.setLangBundle(langBundle);
		
		atlas = manager.get("mypack.atlas", TextureAtlas.class);
		
		normalUpTexture = atlas.findRegion("normal_up");
		normalDownTexture = atlas.findRegion("normal_down");
		flybyUpTexture = atlas.findRegion("flyby_up");
		flybyDownTexture = atlas.findRegion("flyby_down");
		smileUpTexture = atlas.findRegion("smile_up");
		smileDownTexture = atlas.findRegion("smile_down");
		hurtUpTexture = atlas.findRegion("hurt2_up");
		hurtDownTexture = atlas.findRegion("hurt2_down");
		tongueUpTexture = atlas.findRegion("tongue_up");
		tongueDownTexture = atlas.findRegion("tongue_down");
		
		springBoardTexture = atlas.findRegion("springboard");
		faucetTexture = atlas.findRegion("faucet3");
	
		backgroundTexture = atlas.findRegion("background");
		
		
		playButtonTextureRegion = atlas.findRegion("play_button");
		scoreButtonTextureRegion = atlas.findRegion("score_button");
		shareButtonTextureRegion = atlas.findRegion("share_icon");
		facebookButtonTextureRegion = atlas.findRegion("facebook_icon");
		scoreBoardTextureRegion = atlas.findRegion("score_board");		
		popupTextureRegion = atlas.findRegion("popup");
		OkButtonTextureRegion = atlas.findRegion("ok_button");
		CancelButtonTextureRegion = atlas.findRegion("cancel_button");		
		infoTextureRegion = atlas.findRegion("info_button");
		moreTextureRegion = atlas.findRegion("more_button");
		
		clockTextureRegion = atlas.findRegion("clock");
		tapInstrucTextureRegion = atlas.findRegion("tap_instruc");
		levelLockedTextureRegion = atlas.findRegion("level_locked");
		levelUnlockedTextureRegion = atlas.findRegion("level_unlocked");
		levelCompletedTextureRegion = atlas.findRegion("level_completed");
		
		bucketTextureRegion = atlas.findRegion("bucket");
		bucketWaterTextureRegion = atlas.findRegion("bucket_water");	
		bucketBottomWaterTextureRegion = atlas.findRegion("bucket_bottom");		
		splashObjectTextureRegion = atlas.findRegion("splash_object");
		splashFloorTextureRegion = atlas.findRegion("splash_floor");
		splashWaterTextureRegion = atlas.findRegion("splash_water");
		waterExplosionTextureRegion = atlas.findRegion("splash_bucket");
		
		//waterBalloonTextureRegion = atlas.findRegion("balloon_yellow");
		ballonRedTextureRegion = atlas.findRegion("balloon_red");
		ballonYellowTextureRegion = atlas.findRegion("balloon_yellow");
		ballonGreenTextureRegion = atlas.findRegion("balloon_green");
		ballonBlueTextureRegion = atlas.findRegion("balloon_blue");
		ballonPurpleTextureRegion = atlas.findRegion("balloon_purple");
		
		
		TextureRegion[] animFramesTMP3 = new TextureRegion[3];
		
		TextureRegion trg3 = atlas.findRegion("bird_sprite");
	    birdFlySpriteTexture = trg3.split(trg3.getRegionWidth() / 3, trg3.getRegionHeight() / 1);

		animFramesTMP3[0]  = birdFlySpriteTexture[0][0];
		animFramesTMP3[1]  = birdFlySpriteTexture[0][1];
		animFramesTMP3[2]  = birdFlySpriteTexture[0][2];
				

	    Array<? extends TextureRegion>  normalAnimFrames = new Array(animFramesTMP3);          
	    normalAnimation = new Animation(FLAP_ANIMATION_SPEED, normalAnimFrames, Animation.PlayMode.LOOP);
	    
	    TextureRegion[] animFramesTMP = new TextureRegion[2];
	    
	    animFramesTMP[0]  = hurtUpTexture;
		animFramesTMP[1]  = hurtDownTexture;
		     
		Array<? extends TextureRegion>  hurtAnimFrames = new Array(animFramesTMP);          
		hurtAnimation = new Animation(FLAP_ANIMATION_SPEED*2, hurtAnimFrames, Animation.PlayMode.LOOP);//notice slower speed
	    
	    animFramesTMP[0]  = flybyUpTexture;
		animFramesTMP[1]  = flybyDownTexture;
		     
		Array<? extends TextureRegion>  flybyAnimFrames = new Array(animFramesTMP);          
		flybyAnimation = new Animation(FLAP_ANIMATION_SPEED, flybyAnimFrames, Animation.PlayMode.LOOP);
		
		animFramesTMP[0]  = smileUpTexture;
		animFramesTMP[1]  = smileDownTexture;
		     
		Array<? extends TextureRegion>  smileAnimFrames = new Array(animFramesTMP);          
		smileAnimation = new Animation(FLAP_ANIMATION_SPEED, smileAnimFrames, Animation.PlayMode.LOOP);
		
		animFramesTMP[0]  = tongueUpTexture;
		animFramesTMP[1]  = tongueDownTexture;
		     
		Array<? extends TextureRegion>  tongueAnimFrames = new Array(animFramesTMP);          
		tongueAnimation = new Animation(FLAP_ANIMATION_SPEED, tongueAnimFrames, Animation.PlayMode.LOOP);
		
		//Seal walking animation
		TextureRegion trg1 = atlas.findRegion("seal_sprite");
	    sealSpriteTexture = trg1.split(trg1.getRegionWidth() / 5, trg1.getRegionHeight() / 1);

		animFramesTMP[0]  = sealSpriteTexture[0][1];
		animFramesTMP[1]  = sealSpriteTexture[0][2];
		
		//animFramesTMP[0]  = flyBombUpTexture;
		//animFramesTMP[1]  = flyBombDownTexture;
		     
		Array<? extends TextureRegion>  sealWalkAnimFrames = new Array(animFramesTMP);          
		sealWalkAnimation = new Animation(WALK_ANIMATION_SPEED, sealWalkAnimFrames, Animation.PlayMode.LOOP);
		
		trg1 = atlas.findRegion("seal_sprite2");
	    sealSpriteTexture2 = trg1.split(trg1.getRegionWidth() / 4, trg1.getRegionHeight() / 1);
		
		animFramesTMP[0]  = sealSpriteTexture2[0][1];
		animFramesTMP[1]  = sealSpriteTexture2[0][2];
		
		//animFramesTMP[0]  = flyBombUpTexture;
		//animFramesTMP[1]  = flyBombDownTexture;
		     
		Array<? extends TextureRegion>  sealCarryOnAnimFrames = new Array(animFramesTMP);          
		sealCarryOnAnimation = new Animation(WALK_ANIMATION_SPEED, sealCarryOnAnimFrames, Animation.PlayMode.LOOP);
		
		
		//Detonation animation
		/*trg1 = atlas.findRegion("detonation_sprite");
	    TextureRegion[][]  detonationSpriteTexture = trg1.split(trg1.getRegionWidth() / 3, trg1.getRegionHeight() / 2);

	    TextureRegion[] animFramesTMP2 = new TextureRegion[6];     
		animFramesTMP2[0]  = detonationSpriteTexture[0][0];
		animFramesTMP2[1]  = detonationSpriteTexture[0][1];
		animFramesTMP2[2]  = detonationSpriteTexture[0][2];
		animFramesTMP2[3]  = detonationSpriteTexture[1][0];
		animFramesTMP2[4]  = detonationSpriteTexture[1][1];
		animFramesTMP2[5]  = detonationSpriteTexture[1][2];
		     
		Array<? extends TextureRegion>  detonationAnimFrames = new Array(animFramesTMP2);          
		detonationAnimation = new Animation(0.2f, detonationAnimFrames, Animation.PlayMode.NORMAL);
		*/
		
		
		
		
		
		//Load sounds and Music
		splineMusic = manager.get("spline.ogg", Music.class);		
		slowDJMusic = manager.get("djmusic_slow.ogg", Music.class);
		
		
		
		djEndSound = manager.get("djmusic_end.ogg", Sound.class);
		splashSound = manager.get("splash.ogg", Sound.class);
		
		freefallSound = manager.get("freefall.ogg", Sound.class);
		gruntSound = manager.get("grunt.ogg", Sound.class);
		
		birdWhistleSound = manager.get("bird_whistle.ogg", Sound.class);
		woofingSound = manager.get("woofing1.ogg", Sound.class);			
		bamSound  = manager.get("bam.ogg", Sound.class);
		bam2Sound  = manager.get("bam2.ogg", Sound.class);						
		
		walkingMusic   = manager.get("walking.ogg", Music.class);
		warningSound  = manager.get("warning.ogg", Sound.class);
		pointSound  = manager.get("point.ogg", Sound.class);
		popBucketSound  = manager.get("pop_bucket.ogg", Sound.class);
		popFloorSound  = manager.get("pop_floor.ogg", Sound.class);		
		winPointSound  = manager.get("win_point.ogg", Sound.class);
		jumpSound = manager.get("jump.ogg", Sound.class);		
		ouchSound = manager.get("ouch.ogg", Sound.class);
		
		
		//Load other fonts
		
		 //Load font
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("arial-roundb.ttf"));
		
		fontWinFail = generator.generateFont((int)((screen_width*0.10f))); //40 font size 25 pixels
		fontWinFail.setColor(Color.BLACK);								
		fontpopUp = generator.generateFont((int)(popUpRectangle.height-(popUpRectangle.height*0.9f)));
	    fontpopUp.setColor(Color.WHITE);
	    fontSocoreBig = generator.generateFont((int)(scoreBoardRectangle.width-(scoreBoardRectangle.width*0.85f)));
	    fontSocoreBig.setColor(Color.WHITE);
	    fontTimer = generator.generateFont((int)(screen_width-(screen_width*0.95f)));
	    fontTimer.setColor(Color.WHITE);	    	   
	    generator.dispose();
		
		 	 
		 currentLevel =  preferences.getInteger( PREF_CURRENT_LEVEL, 20 );
		 randGen = new Random();
		
		
		 restartGame();
	}
	
	
	
	void restartGame(){
		Gdx.app.log( LOG, "call restart");
		
		showTimeout = false;
		currentScoreFails = 0;
		currentScore = 0;
		
		splashTimeTrack = 0L;
		splashTimeStart = 0L;
		
		timeTrackingEnabled = true;
		inputEnabled = true;
		gamingEnabled = true;
		
		
		detonateBomb = false;
		stopFlyingBomb = false;
		
		showShield = false;
		
		linealSpeed = (mosquitoRectangle.height*14.0f); // 1725f;  //(mosquitoRectangle.height*15.0f);
		

		 bombRectangle.width = waterBalloonRectangle.width;
		 bombRectangle.height =  waterBalloonRectangle.width;
		 bombRectangle.x = 0;
		 bombRectangle.y = maxLastPointY;
		 
		 bucketRectangle.y = 0;
		 if(moveCounts > 0) bucketRectangle.x = (screen_width*0.5f) - (bucketRectangle.width*0.5f);
		 bucketBottomWaterRectangle.y = bucketRectangle.y;	
		 
		fightMeCountsIncrementor = 1;
		playsCount++;
		
		//moveNameCounts.clear();
		//moveNameAssertionCounts.clear();
		//moveNameCounts = new ArrayList<Integer>();
		//moveNameAssertionCounts = new ArrayList<Integer>();
		moveNameCounts = new Integer[10];
		moveNameAssertionCounts = new Integer[20];
		
		gameTimeTrack = 0L;//System.nanoTime();
	 	gameTimeStart = 0L;
	 	gameTimeEnded = 0L;
		currentDeltaTime = 0f;
		deltaDetonationTime = 0f;

		
		currentGameScreen = GAME_SCREEN_PLAYING;
		splineMusic.stop();		
		walkingMusic.stop();
				
		setupLevel();
		
		if(moveCounts==0)
			cleanMove();
		else
			setRandomMove(MOVE_NAME_ANY);
		
		setSealRandomMove(MOVE_NAME_BUCKET_MOVE);
		
		
	}
	
	void setupLevel(){
		
		//if(DEV_MODE == true) currentLevel = 5;			//TEMP
		//currentLevel = 20;			//TEMP
		
		GAME_DURATION = 10;//seconds
		FAIL_COUNT_LIMIT = 10;				
		TOTAL_MOSQUITOS = currentLevel;//100;
		feintMovePauseTimeWaitLevelTime =   300000000;			//NOTE: Learning curve is large on level 1 so its separate from more levels
		fightmeMovePauseTimeWaitLevelTime = 450000000;			//NOTE: Learning curve is large on level 1 so its separate from more levels
		linealSpeedLevelFactor = 1f;
		splineSpeedLevelFactor = 1f;
		
		featureFightMe2Available = false;
		featureBombAvailable = false;
		featureShieldAvailable = false;
		featureDoubleFeintAvailable = false;
		featureDoubleKillAvailable = true;	//
		featureInvisibleAvailable = false;
		/*
		switch (currentLevel) {
			case 1:
				
					
				break;
			case 2:
				GAME_DURATION = 60;//seconds
				FAIL_COUNT_LIMIT = 15;				
				TOTAL_MOSQUITOS = 10;
				feintMovePauseTimeWaitLevelTime =   520000000;//390000000
				fightmeMovePauseTimeWaitLevelTime = 350000000;
				linealSpeedLevelFactor = 1.03f;
				splineSpeedLevelFactor = 1.05f;
				
				featureFightMe2Available = true;
				featureBombAvailable = true;
				featureShieldAvailable = false;
				featureDoubleFeintAvailable = false;
				featureDoubleKillAvailable = false;
				featureInvisibleAvailable = false;
				
				
				break;
			case 3:
				GAME_DURATION = 60;//seconds
				FAIL_COUNT_LIMIT = 15;				
				TOTAL_MOSQUITOS = 10;
				feintMovePauseTimeWaitLevelTime =   500000000;//390000000
				fightmeMovePauseTimeWaitLevelTime = 300000000;
				linealSpeedLevelFactor = 1.06f;
				splineSpeedLevelFactor = 1.10f;
				
				featureFightMe2Available = true;
				featureBombAvailable = true;
				featureShieldAvailable = true;
				featureDoubleFeintAvailable = false;
				featureDoubleKillAvailable = false;
				featureInvisibleAvailable = false;
				
				break;
			
			case 4:
				GAME_DURATION = 60;//seconds
				FAIL_COUNT_LIMIT = 15;				
				TOTAL_MOSQUITOS = 10;
				feintMovePauseTimeWaitLevelTime =   480000000;
				fightmeMovePauseTimeWaitLevelTime = 300000000;
				linealSpeedLevelFactor = 1.09f;
				splineSpeedLevelFactor = 1.15f;
				
				featureFightMe2Available = true;
				featureBombAvailable = true;
				featureShieldAvailable = true;
				featureDoubleFeintAvailable = true;
				featureDoubleKillAvailable = true;
				featureInvisibleAvailable = false;
				
				break;
			
			case 5:
				GAME_DURATION = 60;//seconds
				FAIL_COUNT_LIMIT = 15;				
				TOTAL_MOSQUITOS = 10;
				feintMovePauseTimeWaitLevelTime = 	460000000;
				fightmeMovePauseTimeWaitLevelTime = 300000000;
				linealSpeedLevelFactor = 1.10f;
				splineSpeedLevelFactor = 1.20f;
				
				featureFightMe2Available = true;
				featureBombAvailable = true;
				featureShieldAvailable = true;
				featureDoubleFeintAvailable = true;
				featureDoubleKillAvailable = true;
				featureInvisibleAvailable = true;
			
				
				break;
		}
		*/
	}
	
	/*
	 * Re-start stand by time and the mosquito splinePosition to hidden splinePosition
	 */
	void reStartStandBy() {
		Gdx.app.log( LOG, "re Start stangBY");
		standByTimeStart = System.nanoTime();	
 		standByTimeCount = 0;
 		mosquitoRectangle.x = maxLastPointX;
 		mosquitoRectangle.y = maxLastPointY;
 		splineMusic.stop();
 		
	}
	
	
	void reStartStandBySeal() {
		Gdx.app.log( LOG, "re Start stangBY");
		sealStandByTimeStart = System.nanoTime();	
 		sealStandByTimeCount = 0;
 		//sealRectangle.x = maxLastPointX;
 		//sealRectangle.y = maxLastPointY;
 		walkingMusic.stop();
 		//splineSlowMusic.stop();
 		if(moveCounts ==1) {
 			moveCounts++;
 			reStartStandBy();//Only the first time
 		}
	}
	


	/**
	 * Generates a boolean random taking into account history of randoms.
	 * @param posTrue the true position for this item in case on the array. Note a true pos X must be associated with a false pos X.
	 * @param posFalse the false position for this item in case on the array. Note a true pos X must be associated with a false pos X.
	 * @param limitTrues the limit of trues. Ideally must be the same as the limit of falses
	 * @param limitFalses the limit of falses. Ideally must be the same as the limit of trues.
	 * @return
	 */
	boolean memoryRandom(int posTrue, int posFalse, int limitTrues, int limitFalses) {
		boolean value = randGen.nextBoolean();
		
		if(value == true ) {
			
			if(memoryBooleans[posTrue] < limitTrues) {
				memoryBooleans[posTrue]++;
				return value;
			}else {
				if(memoryBooleans[posFalse] < limitFalses) {
					memoryBooleans[posFalse]++;
					return false;
				}
				memoryBooleans[posTrue] = 0;
				memoryBooleans[posFalse] = 0;
				return false;
			}
		}else {
			
			if(memoryBooleans[posFalse] < limitFalses) {
				memoryBooleans[posFalse]++;
				return value;
			}else {
				if(memoryBooleans[posTrue] < limitTrues) {
					memoryBooleans[posTrue]++;
					return true;
				}
					memoryBooleans[posFalse] = 0;
					memoryBooleans[posTrue] = 0;
					return true;
			}
		}
		
	}
	
	
	/* Generates a boolean random taking into account history of randoms */

	boolean memoryRandom() {
		boolean value = randGen.nextBoolean();
		if(value == true ) {
			
			if(truesesCount < 2) {
				truesesCount++;
				return value;
			}else {
				truesesCount = 0;
				return false;
			}
		}else {
			
			if(falsesCount < 2) {
				falsesCount++;
				return value;
			}else {
				falsesCount = 0;
				return true;
			}
		}
	}
	
	
	int getRandom(int min, int max) {
		int value;		
		value =  (min + (int)(Math.random() * (max - min)));
		return value;
	}
	
	/* Generates a random number skipping a specified range */
	int getRandomIntSkipRange(int min, int max, int skipMin, int skipMax) {
		 int value;
		 
		 do{
			 value =  (min + (int)(Math.random() * (max - min)));
			 //Gdx.app.log( LOG, "RANDOM INT" + value);
		 }while((value >= skipMin && value <= skipMax));
		  
		 //if (value >= skipMin && value <= skipMax) return getRandomInt(min, max, skipMin, skipMax);//lets retry
		 return value;
	}
	
	/*
	 * Generates a random int with a high variance respective to the last one or more received values.
	 * NOTE: This takes into account that Screen is dived in 4 quadrants.  TopLeft,  TopRight,  BottomLeft, BottomRIght
	 */
	int[] getRandomPos(int minX, int maxX, int minY, int maxY, int[] point, int variance_type) {
		if(variance_type == VARIANCE_TYPE_HIGH)
			return getRandomPosHighVariance(minX, maxX, minY, maxY, point);
		else
			return getRandomPosLowVariance(minX, maxX, minY, maxY, point);
	}
	
	/*
	 * Generates a random int with a high variance respective to the last one or more received values.
	 * NOTE: This takes into account that Screen is dived in 4 quadrants.  TopLeft,  TopRight,  BottomLeft, BottomRIght
	 */
	int[] getRandomPosHighVariance(int minX, int maxX, int minY, int maxY, int[] point) {
		int[] target = new int[2];
		int[] bounds = new int[2];
		int targetX;
		int targetY;
		
		//Limit the difference length between current and new position
		//minX = getTrimedMinValue(minX, point[0], (int)mosquitoRectangle.width*2);
		//maxX = getTrimedMaxValue(maxX, point[0], (int)mosquitoRectangle.width*2);
		
		int percentTrue;		
		
		if(currentScore > 80)
			percentTrue = 5;
		else if(currentScore > 60)
			percentTrue = 4;
		else if(currentScore > 40)
			percentTrue = 3;
		else if(currentScore > 30)
			percentTrue = 2;
		else
			percentTrue = 1;
		
		if(memoryRandom(BOLINDX_VARIANCEHORVERT_TRUE, BOLINDX_VARIANCEHORVERT_FALSE, percentTrue, 2) == true) {//MORE HORIZONTAL
			int skipRangeSize = (int)(mosquitoRectangle.width*0.5f);
			int skipRangeMin = (int)point[0] - skipRangeSize;
			int skipRangeMax = (int)point[0] + skipRangeSize;
	
			targetX = getRandomIntSkipRange(minX,maxX,skipRangeMin,skipRangeMax);
			
			
			if(currentScore > 70) {//add also vertical factor(same code as bellow)
				minY = getTrimedMinValue(minY, point[1], (int)(mosquitoRectangle.height*2.0f));
				maxY = getTrimedMaxValue(maxY, point[1], (int)(mosquitoRectangle.height*2.0f));
								
				int skipRangeSize2 = (int)mosquitoRectangle.height;
				int skipRangeMin2 = (int)point[1] - skipRangeSize2;
				int skipRangeMax2 = (int)point[1] + skipRangeSize2;
				
				targetY = getRandomIntSkipRange(minY,maxY,skipRangeMin2,skipRangeMax2);
				
			}else {
				minY = getTrimedMinValue(minY, point[1], (int)(mosquitoRectangle.height*0.5f));
				maxY = getTrimedMaxValue(maxY, point[1], (int)(mosquitoRectangle.height*0.5f));
				
				targetY = getRandom(minY, maxY);
			}
			
			//targetY = point[1];
		}else {//MORE VERTIAL
			minY = getTrimedMinValue(minY, point[1], (int)(mosquitoRectangle.height*2.0f));
			maxY = getTrimedMaxValue(maxY, point[1], (int)(mosquitoRectangle.height*2.0f));
			
			targetX = getRandom(minX, maxX);
			Gdx.app.log( LOG, "send: " + minY +" "+ maxY + " | "+ point[0] + " "+ point[1]);
			int skipRangeSize = (int)mosquitoRectangle.height;
			int skipRangeMin = (int)point[1] - skipRangeSize;
			int skipRangeMax = (int)point[1] + skipRangeSize;
			
			targetY = getRandomIntSkipRange(minY,maxY,skipRangeMin,skipRangeMax);
		}
				
		
		
		
			

		target[0] = targetX;
		target[1] = targetY;
		
		Gdx.app.log( LOG, "values: " + target[0] +" "+ target[1] + " | "+ point[0] + " "+ point[1]);
		return target;
	}
	
	/*
	 * Generates a random int with a high variance respective to the last one or more received values.
	 * NOTE: This takes into account that Screen is dived in 4 quadrants.  TopLeft,  TopRight,  BottomLeft, BottomRIght
	 */
	int[] getRandomPosLowVariance(int minX, int maxX, int minY, int maxY, int[] point) {
		int[] target = new int[2];
		int targetX;
		int targetY;
		
	
		
		
		
		if(memoryRandom(BOLINDX_VARIANCEHORVERT_TRUE, BOLINDX_VARIANCEHORVERT_FALSE, 1, 2) == true) {
			//Limit the difference length between current and new position
			minX = getTrimedMinValue(minX, point[0], (int)mosquitoRectangle.width*2);
			maxX = getTrimedMaxValue(maxX, point[0], (int)mosquitoRectangle.width*2);
			minY = getTrimedMinValue(minY, point[1], (int)(mosquitoRectangle.height*0.5f));
			maxY = getTrimedMaxValue(maxY, point[1], (int)(mosquitoRectangle.height*0.5f));
			
			int skipRangeSize = (int)mosquitoRectangle.width/2;
			int skipRangeMin = (int)point[0] - skipRangeSize;
			int skipRangeMax = (int)point[0] + skipRangeSize;
			
			targetX = getRandomIntSkipRange(minX,maxX,skipRangeMin,skipRangeMax);					
			targetY = getRandom(minY, maxY);
		}else {
			//Limit the difference length between current and new position
			minX = getTrimedMinValue(minX, point[0], (int)(mosquitoRectangle.width*0.5f));
			maxX = getTrimedMaxValue(maxX, point[0], (int)(mosquitoRectangle.width*0.5f));
			minY = getTrimedMinValue(minY, point[1], (int)(mosquitoRectangle.height*1.0f));
			maxY = getTrimedMaxValue(maxY, point[1], (int)(mosquitoRectangle.height*1.0f));
			
			
			Gdx.app.log( LOG, "send: " + minY +" "+ maxY + " | "+ point[0] + " "+ point[1]);
			int skipRangeSize = (int)mosquitoRectangle.height/2;
			int skipRangeMin = (int)point[1] - skipRangeSize;
			int skipRangeMax = (int)point[1] + skipRangeSize;
			
			targetX = getRandom(minX, maxX);			
			targetY = getRandomIntSkipRange(minY,maxY,skipRangeMin,skipRangeMax);
			
		}

			
		
		/*
		
	*/
		target[0] = targetX;
		target[1] = targetY;
		
		Gdx.app.log( LOG, "values: " + target[0] +" "+ target[1] + " | "+ point[0] + " "+ point[1]);
		return target;
	}
	
	/*
	 * Generates a random int with a high variance respective to the last one or more received values.
	 * NOTE: This takes into account that Screen is dived in 4 quadrants.  TopLeft,  TopRight,  BottomLeft, BottomRIght
	 * @DEPRECATED
	 */
	int[] getRandomPosRandomVariance(int minX, int maxX, int minY, int maxY, int[] point) {
		int[] target = new int[2];
		int[] bounds = new int[2];
		int targetX;
		int targetY;
		
		//Limit the difference length between current and new position
		minX = getTrimedMinValue(minX, point[0], (int)mosquitoRectangle.width*2);
		maxX = getTrimedMaxValue(maxX, point[0], (int)mosquitoRectangle.width*2);
		
		minY = getTrimedMinValue(minY, point[1], (int)(mosquitoRectangle.height*1.5f));
		maxY = getTrimedMaxValue(maxY, point[1], (int)(mosquitoRectangle.height*1.5f));
		

		if(randGen.nextBoolean()==false && randGen.nextBoolean()==true) {
			bounds = getQuadrantHorizontalBounds(point[0]);
			if(point[0] < (screen_width/2))
				targetX = getRandomIntSkipRange(minX,maxX,(bounds[0]+(int)mosquitoRectangle.width),bounds[1]);
			else
				targetX = getRandomIntSkipRange(minX,maxX,bounds[0],bounds[1]);
			
			targetY = getRandom(minY, maxY);
			
		}else {				
			targetX = getRandom(minX, maxX);
			bounds = getQuadrantVerticalBounds(point[1]);
			Gdx.app.log( LOG, "send: " + minY +" "+ maxY + " | "+ bounds[0] + " "+ bounds[1]);
			int skipRangeSize = (int)mosquitoRectangle.height/2;
			int skipRangeMin = (int)point[1] - skipRangeSize;
			int skipRangeMax = (int)point[1] + skipRangeSize;
			
			targetY = getRandomIntSkipRange(minY,maxY,skipRangeMin,skipRangeMax);
			/*
			targetY = getRandomIntSkipRange(minY,maxY,bounds[0],bounds[1]);
			*/
		}

		target[0] = targetX;
		target[1] = targetY;
		
		Gdx.app.log( LOG, "values: " + target[0] +" "+ target[1] + " | "+ point[0] + " "+ point[1]);
		return target;
	}
	
	/*
	 * Trims a max value if needed according to a maximum  difference
	 * 
	 */
	int getTrimedMaxValue(int maxValue, int currentValue, int maxValueDifference) {
		
		if((maxValue - currentValue) > maxValueDifference)
			return currentValue+maxValueDifference;
		else
			return maxValue;
	}
	
	/*
	 * Trims a min value if needed according to a maximum  difference
	 * 
	 */
	int getTrimedMinValue(int minValue, int currentValue, int maxValueDifference) {
		
		if(Math.abs((minValue - currentValue)) > maxValueDifference)
			return currentValue-maxValueDifference;
		else
			return minValue;
	}
	

	
	/*
	 * Returns the quadrant Horizontal bounds of the current X point.
	 * Screen is dived in 4 quadrants.  TopLeft,  TopRight,  BottomLeft, BottomRIght
	 * 0 = left side
	 * 1 = right side
	 */
	int[] getQuadrantHorizontalBounds(int xPos){
		int[] bounds = new int[2];
		if(xPos < (screen_width/2)) {
			bounds[0] = 0;
			bounds[1] = (screen_width/2);//OPTIONAL: add mosquitorectangle's width to make it more difficult
		}else {
			bounds[0] = (screen_width/2);
			bounds[1] = screen_width;
		}
		return bounds;
	}
	
	/*
	 * Returns the quadrant Horizontal bounds of the current X point.
	 * Screen is dived in 4 quadrants.  TopLeft,  TopRight,  BottomLeft, BottomRIght
	 * 0 = top side
	 * 1 = bottom side
	 */
	int[] getQuadrantVerticalBounds(int yPos){
		int[] bounds = new int[2];
		if(yPos < (screen_height/2)) {
			bounds[0] = (screen_height/2);
			bounds[1] = 0;
		}else {
			bounds[0] = screen_height;
			bounds[1] = (screen_height/2);
		}
		return bounds;
	}
	
	/*Make The mosquito move on x,y axis to the designated coordinates on a Spline splinePath.*/
	void moveSplinePath(){
		
		//Gdx.app.log( LOG, "MOVE SPLINE SPEED " + splineSpeed + " / "+ maxSplineSpeed); 
		
		 if(pauseMovingEnabled == true) {	
			 
			 if(movePauseTimes.size() ==0) {				 				
				 if((bombRectangle.x-(bombRectangle.width*0.1f)) > bucketRectangle.x && (bombRectangle.x+(bombRectangle.width*1.1f)) < (bucketRectangle.x+bucketRectangle.width) ) {					 	
					 int chanceTrue;
					 int chanceFalse;
					 if(currentScore > 50){
						 chanceTrue = 1;
						 chanceFalse = 10;
					 }else if(currentScore > 25){
						 chanceTrue = 1;
						 chanceFalse = 6;
					 }else{
						 chanceTrue = 1;
						 chanceFalse = 4;
					 }
					 
					 if(memoryRandom(BOLINDX_BIRDWHISTLE_TRUE, BOLINDX_BIRDWHISTLE_FALSE, chanceTrue, chanceFalse) == true) {
						movePuseCurrentIndex = 0;
						movePauseTimes.clear();
						movePauseTimes = new ArrayList<Float>();						
						float pausedTime = splineTime;		//pause at current time
						movePauseTimes.add(pausedTime);	
					 }
				 }
				
			 }
				
			 if(movePuseCurrentIndex <= (movePauseTimes.size()-1)){
				 if(splineTime >= movePauseTimes.get(movePuseCurrentIndex)) {
					 if (movePauseTimeStart != 0) {
		
							if(movePauseTimeCount  >= movePauseTimeWait) {
								Gdx.app.log( LOG, "TIME PAUSE " + movePauseTimeCount);
								movePuseCurrentIndex++;
								movePauseTimeCount = 0;
								movePauseTimeStart = 0;
								
								positionType = POSITION_TYPE_NORMAL;
								
								showClock = false;
								//if (showBomb == false) putBomb();
								
								//touchEnabled = false;
							}else {								
								
								
								moveFloatingEffect();
								
								
								movePauseTimeCount = System.nanoTime() - movePauseTimeStart;//we have reached the target, start counting again
								//touchEnabled = true;
								return;
							}							
						}else {
							movePauseTimeStart = System.nanoTime();	
							if(currentMoveFails == 0) { 
								positionType = POSITION_TYPE_TONGUE;
								birdWhistleSound.play();
								showClock = true;
							}else {
								//positionType = POSITION_TYPE_SMILE;
								//hahaSound.play();
								
							}
							/*if(featureShieldAvailable ==true) {
								if(memoryRandom(BOLINDX_SHOWSHIELD_TRUE, BOLINDX_SHOWSHIELD_FALSE, 2, 1) == true ) {							
									shieldSwitchSound.play();
									showShield = true; 
								}
							}*/
						}
				 }else {
					 splineTime = (splineTime + (Gdx.graphics.getDeltaTime()*splineSpeed)) % 1f;
				 }
			 }else {
				 splineTime = (splineTime + (Gdx.graphics.getDeltaTime()*splineSpeed)) % 1f;
			 }
		 }else {
			 splineTime = (splineTime + (Gdx.graphics.getDeltaTime()*splineSpeed)) % 1f;
		 }
		 //touchEnabled = true;/////**********************
		
		//splineTime = (splineTime + (Gdx.graphics.getDeltaTime()*0.2f)) % 1f;   	    
		//splineTime = (splineTime + Gdx.graphics.getDeltaTime()) *1.5f;
   	    splinePath.valueAt(splinePosition, splineTime);
   	    mosquitoRectangle.x = splinePosition.x;
   	    mosquitoRectangle.y = splinePosition.y;
   	    
   	    if(fixedAngle == true) {
   	    	mosquitoAngle = 360f;//SET A FIXED ANGLE for now
   	    }else {
   	    	
   			//linealPosition.set(mosquitoRectangle.x, mosquitoRectangle.y);
   		    //linealDir.set(linealTouch[currentIndexTouchVector]).sub(linealPosition).nor();	    
   		    //Set angle
   		    //mosquitoAngle =  linealDir.angle();
   		    
   	    	
   	    	//If Full rotation its enabled rotate it completly, otherwise only a little bit
   		    if(rotateMosquito == true) {
	   		     mosquitoAngle+=Gdx.graphics.getDeltaTime()*rotationSpeed;
		   		 mosquitoAngle%=360;      // Limits the angle to be <= 360
		   		 while (mosquitoAngle < 0)  // Unfortunally the "modulo" in java gives negative result for negative values.
		   	    	mosquitoAngle+=360;
   		    }else {
   		    	mosquitoAngle = getRandom(360, 380) - 10;
   		    }
   		    
   		    
   		    
   		    
	   		
   	   	    
   	    }
   	    

   	   
   	       	    
   	    //Vector2 lastPoint = currentPath[currentPath.length-2];
   	    //Gdx.app.log( LOG, "POSS SPLINE " + splineTime + " : " + " CP x" + splinePath.controlPoints[splinePath.controlPoints.length-1].x + " ---- "+ mosquitoRectangle.x +" / "+ mosquitoRectangle.y + " / --- " + maxLastPointX +" / "+ minLastPointX  +" / "+ maxLastPointY +" / "+ minLastPointY);
	   	
   	    
   	    if(mosquitoRectangle.x == 200f || mosquitoRectangle.y == 300f )
   	    	Gdx.app.log( LOG, "GO GOG OG");
   	    
   	    //If We have reached the end(the last point of the splinePath)
   	    //Note we use 0.5 instead of 0.99 because catmaull its goes like a bumeran...it goes back to its origin
   	    /*if(splineTime >= 0.7f) {		
   	    	splineTime = 0; //We need to reset splineTime also
   	    	reStartStandBy();				
		}*/
   	    
   	    if(splineTime > 0.2f) {		///at least we are in more than the 30% of the splinePath(since sometime the starting points can have the masLastPoint coordinates just like the ending points)
   	    if( mosquitoRectangle.x > maxLastPointX || mosquitoRectangle.x < minLastPointX || mosquitoRectangle.y > maxLastPointY || mosquitoRectangle.y < minLastPointY) {   //if( mosquitoRectangle.x == lastPoint.x &&  mosquitoRectangle.y == lastPoint.y) {   	 		
   	    	Gdx.app.log( LOG, "STOPPED AT -- " + splineTime);
   	    	splineTime = 0; //We need to reset splineTime also on the splinePath
   	 		
   	    	reStartStandBy();					
		}
   	    }
	
	}
	
	/*Make The mosquito move on x,y axis to the designated coordinates.*/
	void moveLineal(){
		//Gdx.app.log( LOG, "MOVIENG LINEAL MOS " + mosquitoRectangle.x  + " " + mosquitoRectangle.y);
		//Gdx.app.log( LOG, "MOVIENG LINEAL TOC " + linealTouch[currentIndexTouchVector].x  + " " + linealTouch[currentIndexTouchVector].y);
		 
	    //If we have reached the target splinePosition
	    //Gdx.app.log( LOG, "POSS " + mosquitoRectangle.x +" / "+ touch.x +"-"+ mosquitoRectangle.y +"-"+ touch.y);
		if( mosquitoRectangle.x == linealTouch.get(currentIndexTouchVector).x &&  mosquitoRectangle.y == linealTouch.get(currentIndexTouchVector).y) {	
			if(currentIndexTouchVector < (linealTouch.size() -1)) {
				 //if(linealTouch.get(currentIndexTouchVector+1) != null) {
					 if(pauseMovingEnabled == true) {
						 if (movePauseTimeStart != 0) {
							 

								if(movePauseTimeCount  >= movePauseTimeWait) {
									Gdx.app.log( LOG, "TTIME PAUSE " + movePauseTimeCount);
									
									if(moveName == MOVE_NAME_FEINT_DOUBLE && currentIndexTouchVector < 1) {
										 Gdx.app.log( LOG, "ENTER DOUBLE FEINT" );
										 mosquitoRectangle.x =  linealTouch.get(currentIndexTouchVector+2).x;
										 mosquitoRectangle.y =  linealTouch.get(currentIndexTouchVector+2).y;
										 movePauseTimeWait = feintMovePauseTimeWaitLevelTime;//reset to normal from previous modification in setFeintMove()
										 currentIndexTouchVector += 2;
										 return;
									 }else {
										 currentIndexTouchVector++;
									 }
									
									movePauseTimeStart = System.nanoTime(); //reStartStandBy(); //TEMP
									movePauseTimeCount = 0;
								}else {
									
									if(moveName == MOVE_NAME_FEINT_GAMOVR) {
										
										//If is divisible by X 
										if(movePauseTimeCount%400000000 == 0)										
											mosquitoRectangle.y = mosquitoRectangle.y + (mosquitoRectangle.height*0.01f);
										
										if(movePauseTimeCount%200000000 == 0)	
											mosquitoRectangle.y = mosquitoRectangle.y - (mosquitoRectangle.height*0.01f);
									}
									
									//moveFloatingEffect();
									//Gdx.app.log( LOG, "COUNT CRAZY" );
									movePauseTimeCount = System.nanoTime() - movePauseTimeStart;//we have reached the target, start counting again
									return;
								}	
								
							}else {
								movePauseTimeStart = System.nanoTime();	
							}
					 }else {
			
							 currentIndexTouchVector++;
					
					 }
					 					 					
				 //}else {
				//	 Gdx.app.log( LOG, "restart 1" );
				//	 reStartStandBy();
			   	// 	 return;
				// }
			 }else {
				 Gdx.app.log( LOG, "restart 2" );
			 	reStartStandBy();
	   	 		return;
			 }
			 
			 return;
		}
	
		
		linealPosition.set(mosquitoRectangle.x, mosquitoRectangle.y);
	    linealDir.set(linealTouch.get(currentIndexTouchVector)).sub(linealPosition).nor();
	    
	    //Set angle
	    if(currentGameScreen == GAME_SCREEN_ENDED)
	    	mosquitoAngle = 180f;
	    else if(fixedAngle == true) 
   	    	mosquitoAngle = 360f;//SET A FIXED ANGLE for now
	    else
	    	mosquitoAngle =  linealDir.angle()-90f;
	    
	    //Gdx.app.log( LOG, "MOVIENG LINEAL ANGLE" + mosquitoAngle );
	     
	    //Gdx.app.log( LOG, "speed and delta " + linealSpeed + " / "+ Gdx.graphics.getDeltaTime()); 	    
	    linealVelocity.set(linealDir).scl(linealSpeed);
	    linealMovement.set(linealVelocity).scl(TIME_STEP);
	    if (linealPosition.dst2(linealTouch.get(currentIndexTouchVector)) > linealMovement.len2()) {
	        linealPosition.add(linealMovement); 
	    } else {
	        linealPosition.set(linealTouch.get(currentIndexTouchVector));
	    }
	    mosquitoRectangle.x = linealPosition.x;
	    mosquitoRectangle.y = linealPosition.y;


	}
	
	
	/*
	 * Moves the mosquito very little ramdomly giving the effect as if it were floating/hover
	 */
	void moveFloatingEffect() {
		//Let's pause and move the mosquito just few pixels randomly in all directions (to make look the stop more smooth natural)
		if(randGen.nextBoolean()==true)
			mosquitoRectangle.x = mosquitoRectangle.x + (mosquitoRectangle.width*0.01f);
		else
			mosquitoRectangle.x = mosquitoRectangle.x - (mosquitoRectangle.width*0.01f);
	   	
		if(randGen.nextBoolean()==true)
			mosquitoRectangle.y = mosquitoRectangle.y + (mosquitoRectangle.height*0.01f);
		else
			mosquitoRectangle.y = mosquitoRectangle.y - (mosquitoRectangle.height*0.01f);
		
	}
	
	
	/* This is a simple straight(no dir/angle) movement */
	void moveSealLineal() {

		if( sealRectangle.x == linealSealTouch.get(currentSealIndexTouchVector).x &&  sealRectangle.y == linealSealTouch.get(currentSealIndexTouchVector).y) {
			if(currentSealIndexTouchVector < (linealSealTouch.size() -1)) {

					 if(currentSealIndexTouchVector < (sealPauseMovingEnabledArr.size()-1) && sealPauseMovingEnabledArr.get(currentSealIndexTouchVector) ==true) {
						 if(1==1) {
						 if (sealMovePauseTimeStart != 0 ) {

								if(sealMovePauseTimeCount  >= sealMovePauseTimeWait) {
									Gdx.app.log( LOG, "TTTTIME PAUSE " + sealMovePauseTimeCount);
									
									if(sealMoveName == MOVE_NAME_FEINT_DOUBLE && currentSealIndexTouchVector < 1) {
										 Gdx.app.log( LOG, "ENTER DOUBLE FEINT" );
										 sealRectangle.x =  linealSealTouch.get(currentSealIndexTouchVector+2).x;
										 sealRectangle.y =  linealSealTouch.get(currentSealIndexTouchVector+2).y;
										 sealMovePauseTimeWait = feintMovePauseTimeWaitLevelTime;//reset to normal from previous modification in setFeintMove()
										 currentSealIndexTouchVector += 2;
										 return;
									 }else {
										 currentSealIndexTouchVector++;

									 }
									if(sealMoveName == MOVE_NAME_BUCKET_CARRY ) {
										if(walkingMusic.isPlaying()==false) walkingMusic.play();
									}
									sealMovePauseTimeStart = 0;//System.nanoTime(); //reStartStandBy(); //TEMP
									sealMovePauseTimeCount = 0;
								}else {
									Gdx.app.log( LOG, "COUNT CRAZY" );
									sealMovePauseTimeCount = System.nanoTime() - sealMovePauseTimeStart;//we have reached the target, start counting again
									return;
								}	
								
							}else {
								if(sealMoveName == MOVE_NAME_BUCKET_CARRY ) {
									if(walkingMusic.isPlaying()) walkingMusic.pause();
								}
								sealMovePauseTimeStart = System.nanoTime();	
							}
					 }
					 }else {
							 currentSealIndexTouchVector++;	
							 if( sealMoveName == MOVE_NAME_JUMP) {
									if( currentSealIndexTouchVector ==1 ) {
								 
									 walkingMusic.stop();
									 jumpSound.play();
									 touchEnabled=false;
									 //sealRectangle.x = sealRectangle.x + (sealRectangle.height*1f);//NOTE: we need to add the height of the seal to the X coordinate because we are rotating the Seal when we draw it at this point of the vector path
									}
									if( currentSealIndexTouchVector == 2 ) {stopDJMusic = true;  disablePlay();}
									if( currentSealIndexTouchVector ==5 ) {
										splashSound.play();
										
										
										
										 waterExplosionRectangle.width = bucketRectangle.width*1.0f;
										 waterExplosionRectangle.height = bucketRectangle.height*0.1f;
										 waterExplosionRectangle.y = bucketRectangle.y; //bucketWaterRectangle.y + bucketWaterRectangle.height;		 
										 waterExplosionRectangle.x = bucketRectangle.x;//+(bucketRectangle.width*0.5f);
										splashTimeStart = System.nanoTime();
										splashAlphaValue = 1f;
									}
									
							 }
							 if( sealMoveName == MOVE_NAME_JUMP_SPRING) {
								if( currentSealIndexTouchVector ==1 ) {
									stopDJMusic = true; 
								 walkingMusic.stop();
								 jumpSound.play();
								 touchEnabled=false;
								 //sealRectangle.x = sealRectangle.x + (sealRectangle.height*1f);//NOTE: we need to add the height of the seal to the X coordinate because we are rotating the Seal when we draw it at this point of the vector path
								}
								if( currentSealIndexTouchVector == 2 ) { disablePlay();}
								if( currentSealIndexTouchVector == 3 ) {
									splashSound.play();									
									
									 waterExplosionRectangle.width = bucketRectangle.width*1.0f;
									 waterExplosionRectangle.height = bucketRectangle.height*0.1f;
									 waterExplosionRectangle.y = bucketRectangle.y; //bucketWaterRectangle.y + bucketWaterRectangle.height;		 
									 waterExplosionRectangle.x = bucketRectangle.x;//+(bucketRectangle.width*0.5f);
									splashTimeStart = System.nanoTime();
									splashAlphaValue = 1f;
								}
							 }
					 }

			 }else {
				 Gdx.app.log( LOG, "restart 2" );
				 reStartStandBySeal();
				 
				 
				 if( sealMoveName == MOVE_NAME_JUMP || sealMoveName == MOVE_NAME_JUMP_SPRING) {					 
					woofingSound.play();
					
					Timer.schedule(new Timer.Task(){
				 	    @Override
				 	    public void run() {
				 	    	gameEndInfo = "SEAL WIN";
				 	    	raiseGameOver();	 	    
				 	    }
				 	}, 0.5f);					
				}
	   	 		return;
			 }
			 			
			 return;
		}
		
		
		//Gdx.app.log( LOG, "BEFORE CALL SCAPE "+ bombRectangle.y);
	    if(detonationTimeStart==0 && (sealMoveName == MOVE_NAME_BUCKET_MOVE || sealMoveName == MOVE_NAME_BUCKET_MOVEREMOVE || sealMoveName == MOVE_NAME_BUCKET_CARRY) && bombRectangle.y <= (bucketRectangle.y+bucketRectangle.height)) {
	    	Gdx.app.log( LOG, "CALL SCAPE "+ bombRectangle.y);
	    	if(sealMoveName == MOVE_NAME_BUCKET_CARRY)
	    		return;//DONT MOVE THE SEAL
	    	else
	    		setSealEscapeMove();
	    }
		
		 //Gdx.app.log( LOG, "MOVING SEAL " + sealMovePauseTimeCount + " / " + sealMovePauseTimeStart);
		sealAngle =  linealSealDir.angle()-180f;
		sealAngle%=360;      // Limits the angle to be <= 360
  		 while (sealAngle < 0)  // Unfortunally the "modulo" in java gives negative result for negative values.
  			sealAngle+=180; //sealAngle+=360;
		sealAngle = 0;
		
		linealSealPosition.set(sealRectangle.x, sealRectangle.y);
	    linealSealDir.set(linealSealTouch.get(currentSealIndexTouchVector)).sub(linealSealPosition).nor();	    
	     
	    //Gdx.app.log( LOG, "speed and delta " + linealSpeed + " / "+ Gdx.graphics.getDeltaTime()); 
	    
	    float myLinealSealSpeed;
	    if(sealMoveName == MOVE_NAME_JUMP && currentSealIndexTouchVector > 2)
	    	myLinealSealSpeed = linealSealSpeed*2.5f;
	    else if(sealMoveName == MOVE_NAME_JUMP && currentSealIndexTouchVector > 0)
	    	myLinealSealSpeed = linealSealSpeed*2.3f;
	    else
	    	myLinealSealSpeed = linealSealSpeed;
	    
	    if(sealMoveName == MOVE_NAME_JUMP_SPRING && currentSealIndexTouchVector == 1) {
	    	//linealSealSpeed -= linealSealSpeed*0.02f;
	    	myLinealSealSpeed = linealSealSpeed*1.1f;
	    }else if(sealMoveName == MOVE_NAME_JUMP_SPRING && currentSealIndexTouchVector == 2) {
	    	//linealSealSpeed += linealSealSpeed*0.03f;
	    	myLinealSealSpeed = linealSealSpeed*1.7f;
	    }else if(sealMoveName == MOVE_NAME_JUMP_SPRING && currentSealIndexTouchVector > 2) {
	    	myLinealSealSpeed = linealSealSpeed*1.8f;
	    }
	    
	    linealSealVelocity.set(linealSealDir).scl(myLinealSealSpeed);
	    linealBombMovement.set(linealSealVelocity).scl(TIME_STEP);
	    if (linealSealPosition.dst2(linealSealTouch.get(currentSealIndexTouchVector)) > linealBombMovement.len2()) {
	        linealSealPosition.add(linealBombMovement); 
	    } else {
	        linealSealPosition.set(linealSealTouch.get(currentSealIndexTouchVector));
	    }
	    sealRectangle.x = linealSealPosition.x;
	    sealRectangle.y = linealSealPosition.y;
		
	    
	    
	    //Move the bucket by the percentage intersection width
	    if((sealMoveName == MOVE_NAME_BUCKET_MOVE || sealMoveName == MOVE_NAME_BUCKET_MOVEREMOVE || sealMoveName == MOVE_NAME_FEINT || sealMoveName == MOVE_NAME_FEINT_DOUBLE) && sealRectangle.overlaps(bucketRectangle)  ) {
	    	 //Gdx.app.log( LOG, "INTERSECTION " );
	    	Rectangle intersection = new Rectangle();
	    	intersection.x = Math.max(sealRectangle.x, bucketRectangle.x);
	        intersection.width = Math.min(sealRectangle.x + sealRectangle.width, bucketRectangle.x + bucketRectangle.width) - intersection.x;
	        intersection.y = Math.max(sealRectangle.y, bucketRectangle.y);
	        intersection.height = Math.min(sealRectangle.y + sealRectangle.height, bucketRectangle.y + bucketRectangle.height) - intersection.y;
	        
	    	/*if(linealSealDir.angle() == 0.0f ) {
	    		bucketRectangle.x += intersection.width;
	    	}
	    	if(linealSealDir.angle() == 180.0f) {
	    		bucketRectangle.x -= intersection.width;
	    	}*/
	    	
	    	if(sealRectangle.x < bucketRectangle.x ) {
	    		bucketRectangle.x += intersection.width;
	    	}else {
	    	
	    		bucketRectangle.x -= intersection.width;
	    	}
	    	
	    	//Limit it by screen borders in case it passes
	    	if(moveCounts > 1 && sealMoveName != MOVE_NAME_BUCKET_MOVEREMOVE) {
		    	if(bucketRectangle.x < 0 && sealRectangle.x > bucketRectangle.x) bucketRectangle.x = 0;
		    	if((bucketRectangle.x+bucketRectangle.width) > screen_width && sealRectangle.x < bucketRectangle.x) bucketRectangle.x = (screen_width - bucketRectangle.width);
		    }
	    }
		
		//If we have reached the max point, restart
		//if(bombRectangle.x == linealSealTouch.x && bombRectangle.y == linealSealTouch.y)			setBombPassByMove();
	}
	
	void cleanMove() {
		
		pathType = PATH_TYPE_NONE;
		
		currentMoveFails = 0;
		fixedAngle = false;
		standByTimeCount = 0;
		standByTimeStart = 0;
		
		showClock = false;
		showSwatter = false;
		showShield = false;
		showDoubleKill = false;
		showInvisible = false;
		
		movePauseTimeStart = 0;
		movePauseTimeCount = 0;
		pauseMovingEnabled = false;
		
		doubleKillCount = 0;
		
		touchEnabled = false;
		
		rotateMosquito = false;
		
		detonateBomb=false;
		detonationTimeTrack = 0L;
		detonationTimeStart = 0L;
		
		 //mosquitoRectangle.width = screen_width*0.4f;  // 96f;  //screen_width*0.3f;//Varies
		 //mosquitoRectangle.height =  mosquitoRectangle.width*1.0f;  /// 115f;//mosquitoRectangle.width*1.2f;//varies
		 

		 
		 if(currentScore < 20) {
			 bombScoreValue = 3;//bombScoreValue = getRandom(3, 5);
			/* if(memoryRandom(BOLINDX_BOMBSIZE_TRUE, BOLINDX_BOMBSIZE_FALSE, 1, 2) == true)
				bombScoreValue = 10;
			else 
				bombScoreValue = 5;*/
		}else if(currentScore < 40) {
			bombScoreValue = getRandom(3, 4);
			/*if(memoryRandom(BOLINDX_BOMBSIZE_TRUE, BOLINDX_BOMBSIZE_FALSE, 1, 4) == true)
				bombScoreValue = 10;
			else 
				bombScoreValue = 5;*/		
		}else if(currentScore < 60) {
			bombScoreValue = getRandom(3, 5);
		}else if(currentScore < 80) {
			bombScoreValue = getRandom(3, 6);
		}else {
			bombScoreValue = getRandom(3, 7);
			//bombScoreValue = 5;
		}
		 
		 bombRectangle.width = waterBalloonRectangle.width;
		 bombRectangle.height = waterBalloonRectangle.height;
		 
		 
		 float initMinimunPercent = 0.75f;//From which value we are starting to resize the balloon
		 int positionsRange = 5;//Total positions from the lowest to the larges value..... 100 - 75 = 25
		 int maxRangeValue = 7;
		 int myPercent = (bombScoreValue+positionsRange) - maxRangeValue;
		 float percentExtractor = initMinimunPercent + (float)((myPercent*positionsRange)*0.01);
						 
		 //float percentExtractor = (float)bombScoreValue * 0.18f;//0.15f;
		 if(percentExtractor > 1.0f) percentExtractor = 1.0f;
		 bombRectangle.width = bombRectangle.width*percentExtractor;
		 bombRectangle.height = bombRectangle.height*percentExtractor;
		 //bombScoreValue = 7;
		 
		 //this is to avoid invalid call to setSealScape()
		 mosquitoRectangle.y = maxLastPointY;
		 bombRectangle.y = maxLastPointY;
		
	}
	
	
	void cleanMoveSeal() {
		//sealMoveName = MOVE_NAME_ANY;
		
//		animSealTextureRegion = new TextureRegion(sealWalkAnimation.getKeyFrame(currentDeltaTime));
		//animSealTextureRegion.flip(true,false);
		sealStandByTimeCount = 0;
		sealStandByTimeStart = 0;
		
		sealMovePauseTimeStart = 0;
		sealMovePauseTimeCount = 0;
		
		sealWalkAnimation.setFrameDuration(WALK_ANIMATION_SPEED);
	}

	/*
	 * Choose a random move between the available ones.
	 */
	void setRandomMove(int moveType ) {
		Gdx.app.log( LOG, "RANBDOM MOVE ");
		
		cleanMove();
		
		
						
		switch (moveType) {
			case MOVE_NAME_ANY:
				
					//setPassByMove();
					//setFeintMove();
					setFightMeMove();
					//setFightMeMove2();
					
					
					
					/*
					if(memoryRandom(BOLINDX_RNDMOV_TRUE, BOLINDX_RNDMOV_FALSE, 2, 2) == true) {
						Gdx.app.log( LOG, "RAND 1");
						
						if(featureFightMe2Available==true) {
							if(memoryRandom(BOLINDX_FGHTYPEMOV_TRUE, BOLINDX_FGHTYPEMOV_FALSE, 2, 2) == true) 
								setFightMeMove();
							else
								setFightMeMove2();
						}else
						{
							setFightMeMove();
						}
						
					}else {
						Gdx.app.log( LOG, "RAND 2");
						if(memoryRandom(BOLINDX_RNDMOVSUB_TRUE, BOLINDX_RNDMOVSUB_FALSE, 3, 1) == true)	
							setFeintMove();
						else
							setPassByMove();
					}
					*/
			
				break;
			case MOVE_NAME_PASSBY:
				setPassByMove();
				break;
		}
		
		if ( moveNameCounts[ moveName ] == null ) {
			moveNameCounts[ moveName ] = 1;
			moveNameAssertionCounts[ moveName ] = 0;
		}else {
			moveNameCounts[ moveName ]++;
		}
		
		
		

	}
	
	void setSealRandomMove(int moveType) {
		
		
		cleanMoveSeal();	
		
		switch (moveType) {
			case MOVE_NAME_ANY:
				
		
				//setSealFeintMove();
				//setSealJumpMove();
				if(slowDJMusic.isPlaying()==false) {
					if(memoryRandom(BOLINDX_PLAYMUSIC_TRUE, BOLINDX_PLAYMUSIC_FALSE, 2, 2) == true) {				
						slowDJMusic.setLooping(true);
						slowDJMusic.play();		
						slowDJMusic.setOnCompletionListener( new  OnCompletionListener() {			
							@Override
							public void onCompletion(Music music) {
								Gdx.app.log( LOG, "MUSIC ENDED");						
							}
						}					
						);
					}
				}
						
						
				//Change sealStandByTimeWait for next round with a random number to make it feel more real
				if(currentScore >=70 && sealMoveName == MOVE_NAME_FEINT) {
					sealStandByTimeWait = 0; //Don't wait, fire immediately a jump bucket after a simple feint move
					setSealJumpMove();
					return;
				}else if(currentScore >=80) {
					sealStandByTimeWait = getRandom(0, 4);
				}else if(currentScore >=60) {
					sealStandByTimeWait = getRandom(1, 4);
				}else if(currentScore >=40) {
					sealStandByTimeWait = getRandom(2, 4);
				}else {
					sealStandByTimeWait = getRandom(3, 4);
				}
						
				Gdx.app.log( LOG, "PRE SEAL CARRYMOVE" + bucketRectangle.x + " / " + screen_width);
				if( ((bucketRectangle.x+bucketRectangle.width) < 0 || bucketRectangle.x > screen_width)) {
					if(sealMoveName == MOVE_NAME_BUCKET_CARRY)
						setSealBucketMove();
					else
						setSealCarryBucketMove();
								
				}else if(currentScore >= 10) {
					
					int percentTrue;
					int percentFalse;
					
					if(currentScore > 85)
						percentTrue = 3;
					else if(currentScore > 65)
						percentTrue = 2;					
					else
						percentTrue = 1;
					
					//percentTrue = 1;
		
						
					if(memoryRandom(BOLINDX_SEALMOVENAME_TRUE, BOLINDX_SEALMOVENAME_FALSE, percentTrue, 1) == true) {
						if(currentScore > 60) {
							if(memoryRandom()==true)
								setSealJumpMove();
							else
								setSealJumpSpringMove();
						}else {
							setSealJumpSpringMove();
						}
						
					}else {
						if(memoryRandom(BOLINDX_SEALMOVENAME2_TRUE, BOLINDX_SEALMOVENAME2_FALSE, 2, 1) == true) { //if(mosquitoRectangle.x > 0 && mosquitoRectangle.x < screen_width) {
							if(currentScore > 30) {
								if(memoryRandom()==true)
									setSealBucketMove();
								else
									setSealBucketRemoveMove();
							}else {
								setSealBucketMove();
							}
							
							
							
							/*if(currentScore > 80)
								percentTrue = 5;
							else if(currentScore > 60)
								percentTrue = 4;		
							else if(currentScore > 40)
								percentTrue = 3;					
							else
								percentTrue = 2;
							if(memoryRandom(BOLINDX_SEALMOVENAME2_TRUE, BOLINDX_SEALMOVENAME2_FALSE, percentTrue, 1) == true) {  ///if(randGen.nextBoolean()==false) {
								setSealBucketMove();
							}else {
								setSealFeintMove();
							}*/
						}else {
							setSealFeintMove();
						}
						
						
					}
							
				}else {
					//setSealBucketRemoveMove();
					if(memoryRandom(BOLINDX_SEALMOVENAME2_TRUE, BOLINDX_SEALMOVENAME2_FALSE, 1, 1) == true) {
						setSealBucketMove();
					}else {
						setSealFeintMove();
						
					}
					
					
				}
				break;
			case MOVE_NAME_BUCKET_MOVE:
				setSealBucketMove();
				break;
		}
		moveCounts++;
	}
	
	
	/*
	 * Sets a random move for a fight me linealPath.
	 */
	void setFightMeMove2(){	
		
		Gdx.app.log( LOG, "FIGHT ME MOVE 2");
		moveName =  MOVE_NAME_FIGHT2;
		positionType =  POSITION_TYPE_NORMAL;
		touchEnabled = true;
		

		pathType = PATH_TYPE_LINEAL;
		if(featureDoubleKillAvailable==true) showDoubleKill = true;
		if(featureInvisibleAvailable==true) {
			if(memoryRandom(BOLINDX_SHOWINVISIBLE_TRUE, BOLINDX_SHOWINVISIBLE_FALSE, 2, 1) == true) showInvisible = true;
		}
		
			
		int varianceType;
		if(memoryRandom(BOLINDX_VARIANCECHAN_TRUE, BOLINDX_VARIANCECHAN_FALSE, 2, 1) == true) 
			varianceType = VARIANCE_TYPE_LOW;
		else
			varianceType = VARIANCE_TYPE_HIGH;
		
		//varianceType = VARIANCE_TYPE_HIGH;//////
		
		movePauseTimeWait = 200000000;
		if(varianceType == VARIANCE_TYPE_HIGH ) {
			
			linealSpeed = (mosquitoRectangle.height*6.5f) * linealSpeedLevelFactor;
			fixedAngle = false;
		}else {
			linealSpeed = (mosquitoRectangle.height*7.0f) * linealSpeedLevelFactor;
			fixedAngle = true;
		}
		

		

		
		
		
		mosquitoRectangle.x = 0;
		mosquitoRectangle.y = 0;
	
	
		currentIndexTouchVector = 0;
		//pauseMovingEnabled = true;//TEMP
		//if(memoryRandom(BOLINDX_PAUSEMOV_TRUE, BOLINDX_PAUSEMOV_FALSE, 3, 1)==true)
		pauseMovingEnabled = true;
		linealTouch.clear();
		linealTouch = new ArrayList<Vector2>();
		
		int minX;
		int maxX;
		int minY;
		int maxY;
		minX = 0;
		maxX = screen_width-(int)mosquitoRectangle.width;
		minY = 0;
		maxY = screen_height-((int)mosquitoRectangle.height);
		int[] point = new int[2];
		point[0] = getRandom(minX, maxX);//0;//Set initial point
		point[1] = getRandom(minY, maxY);//0;//Set initial point	
		
		point = getRandomPos(minX,maxX,minY,maxY,point,varianceType);
		int i;
		for(i =0; i < 10; i++) {
	
			point = getRandomPos(minX,maxX,minY,maxY,point,varianceType);
			
			linealTouch.add(currentIndexTouchVector+i,  new Vector2(point[0], point[1]));
			
			
		}
		float lastPointX;
		float lastPointY;
		
		if(randGen.nextBoolean()==false) {
			
			lastPointX = minLastPointX + mosquitoRectangle.height;
		}else
		{
			
			lastPointX = maxLastPointX - mosquitoRectangle.height;
		}
		if(randGen.nextBoolean()==true) {
			
			lastPointY = minLastPointY + mosquitoRectangle.height;
		}
		else{
			
			lastPointY = maxLastPointY  - mosquitoRectangle.height;
		}
		
			
			linealTouch.add(currentIndexTouchVector+i, new Vector2(lastPointX, lastPointY));
			
		splineMusic.setLooping(true);
		splineMusic.play();
		
		
	}
	
	/*
	 * Sets a random move for a fight me splinePath.
	 */
	void setFightMeMove(){	
		moveName =  MOVE_NAME_FIGHT;
		positionType =  POSITION_TYPE_NORMAL;
		touchEnabled = true;
		fixedAngle = true;
		splineTime = 0; //We need to reset splineTime also on the splinePath
				
		pathType = PATH_TYPE_SPLINE;
		
		if(featureDoubleKillAvailable==true) showDoubleKill = true;
		if(featureInvisibleAvailable==true) {
			if(memoryRandom(BOLINDX_SHOWINVISIBLE_TRUE, BOLINDX_SHOWINVISIBLE_FALSE, 2, 1) == true) showInvisible = true;
		}
		
		movePauseTimeWait = fightmeMovePauseTimeWaitLevelTime;
		
		//Help points (swatter or clock)
		int secsDuration = (int) ((((long) (GAME_DURATION*1000000000.0)) - gameTimeTrack)/ 1000000000.0);
		
		/*	
		if(currentScoreFails >= (FAIL_COUNT_LIMIT/2)) {
			if(memoryRandom(BOLINDX_HELPOINT_TRUE, BOLINDX_HELPOINT_FALSE, 2, 1)==true) showSwatter = true;
		}
		if(secsDuration < (int)((float)GAME_DURATION*0.4f)) {
			if(memoryRandom(BOLINDX_HELPOINT_TRUE, BOLINDX_HELPOINT_FALSE, 2, 1)==true) showClock = true;
	    	showSwatter = false;
	    }else {
	    	//showSwatter = true;
	    }
*/
		int percentTrue;		
		
		if(currentScore > 80)
			percentTrue = 5;
		else if(currentScore > 60)
			percentTrue = 4;
		else if(currentScore > 40)
			percentTrue = 3;
		else if(currentScore > 30)
			percentTrue = 2;
		else
			percentTrue = 1;
	
		
		int varianceType;
		if(memoryRandom(BOLINDX_VARIANCECHAN_TRUE, BOLINDX_VARIANCECHAN_FALSE, percentTrue, 1) == true) 
			varianceType = VARIANCE_TYPE_HIGH;
		else
			varianceType = VARIANCE_TYPE_LOW;
		
		//varianceType = VARIANCE_TYPE_HIGH;////
		
		if(currentScore > 80)
			splineSpeedLevelFactor = 1.24f;
		else if(currentScore > 60)
			splineSpeedLevelFactor = 1.18f;
		else if(currentScore > 40)
			splineSpeedLevelFactor = 1.12f;
		else if(currentScore > 20)
			splineSpeedLevelFactor = 1.06f;
		else
			splineSpeedLevelFactor = 1.0f;
		
		if(varianceType == VARIANCE_TYPE_HIGH) {
			maxSplineSpeed = 0.15f * splineSpeedLevelFactor;	 			
		}else {			
			maxSplineSpeed = 0.20f * splineSpeedLevelFactor;			
		}
		minSplineSpeed = maxSplineSpeed*0.8f;
		
		
		
		
		//Increase speed on each attempt until the maximum permitted
//		fightMeCountsIncrementor++;
		if(currentScore >= fightMeCountsIncrementor) fightMeCountsIncrementor++;
		int limitIncrementor =  (TOTAL_MOSQUITOS/2);
		float splineIncreaseFactor = ( (maxSplineSpeed-minSplineSpeed) /  limitIncrementor );
		int stepIncrementor;
		if(fightMeCountsIncrementor > limitIncrementor)
			stepIncrementor = limitIncrementor;
		else
			stepIncrementor = fightMeCountsIncrementor;
		splineSpeed = minSplineSpeed + (splineIncreaseFactor*stepIncrementor);
				
		
		
		
		if(memoryRandom(BOLINDX_PAUSEMOV_TRUE, BOLINDX_PAUSEMOV_FALSE, 3, 1)==true)
			pauseMovingEnabled = true;
		
		movePuseCurrentIndex = 0;
		movePauseTimes.clear();
		movePauseTimes = new ArrayList<Float>();
		//movePauseTimes.add(0.6f);
		int numberOfPauses = getRandom(1, 3);
		int minTime = 3;
		int maxTime = 8;
		int pausedTimeInt = getRandom(minTime, maxTime);
		float pausedTime = pausedTimeInt * 0.1f;//Convert it to float
		//if(currentLevel == 1) movePauseTimes.add(pausedTime);		//only add random pauses on level 1 (no bombs)
		
	

		
		//Spline move
		
		
		/*
		int min;
		int max;
		int min2;
		int max2;
		min = 0;
		max = (screen_width/2)-((int)mosquitoRectangle.width);
		min2 = (screen_width/2);
		max2 = screen_width-((int)mosquitoRectangle.width);
		int[] xBounds = new int[2]; 
		int randX1 =  getRandom(min, max);
		int randX2 =  getRandom(min2, max2);
		int randX3 =  getRandom(min, max);
		int randX4 =  getRandom(min2, max2);
		int randX5 =  getRandom(min, max);
		int randX6 =  getRandom(min2, max2);
		int randX7 =  getRandomIntSkipRange(min, max, xBounds[0], xBounds[1]);			xBounds = getQuadrantHorizontalBounds(randX6);
		int randX8 =  (min + (int)(Math.random() * (max - min)));
		min = 0;
		max = screen_height/2-((int)mosquitoRectangle.height);
		min = screen_height/2;
		max = screen_height-((int)mosquitoRectangle.height);
		
		int randY1 =  getRandom(min, max);
		int randY2 =  getRandom(min2, max2);
		int randY3 =  getRandom(min, max);
		int randY4 =  getRandom(min2, max2);
		int randY5 =  getRandom(min, max);
		int randY6 =  getRandom(min2, max2);
		int randY7 =  (min + (int)(Math.random() * (max - min)));
		int randY8 =  (min + (int)(Math.random() * (max - min)));
		*/
		
		int randX1;
		int randX2;
		int randX3;
		int randX4;
		int randX5;
		int randX6;
		int randX7;
		int randX8;
		int randX9;
		int randY1;
		int randY2;
		int randY3;
		int randY4;
		int randY5;
		int randY6;
		int randY7;
		int randY8;
		int randY9;
		
		int minX;
		int maxX;
		int minY;
		int maxY;
		minX = 0;
		maxX = screen_width-(int)mosquitoRectangle.width;
		if(currentScore > 80)
			minY = (int) (screen_height*0.7f);
		else if(currentScore > 60)
			minY = (int) (screen_height*0.6f);
		else if(currentScore > 40)
			minY = (int) (screen_height*0.5f);
		else
			minY = (int) (screen_height*0.4f);
		
		//minY = (int) (bucketRectangle.height*2f);
		
		maxY = screen_height-((int)mosquitoRectangle.height);
		int[] point = new int[2];
		point[0] = getRandom(minX, maxX);//0;//Set initial point
		point[1] = getRandom(minY, maxY);//0;//Set initial point
		
		
		point = getRandomPos(minX,maxX,minY,maxY,point,varianceType);
		randX1 = point[0]; randY1 = point[1];
		point = getRandomPos(minX,maxX,minY,maxY,point,varianceType);
		randX2 = point[0]; randY2 = point[1];
		point = getRandomPos(minX,maxX,minY,maxY,point,varianceType);
		randX3 = point[0]; randY3 = point[1];
		point = getRandomPos(minX,maxX,minY,maxY,point,varianceType);
		randX4 = point[0]; randY4 = point[1];
		point = getRandomPos(minX,maxX,minY,maxY,point,varianceType);
		randX5 = point[0]; randY5 = point[1];
		point = getRandomPos(minX,maxX,minY,maxY,point,varianceType);
		randX6 = point[0]; randY6 = point[1];
		point = getRandomPos(minX,maxX,minY,maxY,point,varianceType);
		randX7 = point[0]; randY7 = point[1];
		point = getRandomPos(minX,maxX,minY,maxY,point,varianceType);
		randX8 = point[0]; randY8 = point[1];
		point = getRandomPos(minX,maxX,minY,maxY,point,varianceType);
		randX9 = point[0]; randY9 = point[1];
		
		
		float firstPointX;
		float firstPointY;
		float lastPointX;
		float lastPointY;
		
		if(randGen.nextBoolean()==false) {
			firstPointX = maxLastPointX - mosquitoRectangle.height;
			lastPointX = minLastPointX + mosquitoRectangle.height;
		}else
		{
			firstPointX = minLastPointX + mosquitoRectangle.height;
			lastPointX = maxLastPointX - mosquitoRectangle.height;
		}
		if(randGen.nextBoolean()==true) {
			firstPointY = maxLastPointY  - mosquitoRectangle.height;
			lastPointY = maxLastPointY + mosquitoRectangle.height;
		}
		else{
			firstPointY = maxLastPointY + mosquitoRectangle.height;
			lastPointY = maxLastPointY  - mosquitoRectangle.height;
		}
		//firstPointX = firstPointX*2;
		//firstPointY = firstPointY*2;
		lastPointX = lastPointX*2;
		lastPointY = lastPointY*2;
		
		
		/*
		currentPath = new Vector2[]{
			    new Vector2(200, 300), new Vector2(200, 350),new Vector2(200, 300),new Vector2(200, 300),new Vector2(200, 350),new Vector2(200, 300),new Vector2(200, 300)
		};
		*/
		
		currentPath = new Vector2[]{
			    new Vector2(firstPointX, firstPointY), 
			    new Vector2(randX1, randY1),new Vector2(randX2, randY2), new Vector2(randX3, randY3),new Vector2(randX4, randY4),
			    new Vector2(randX5, randY5),new Vector2(randX6, randY6),new Vector2(randX7, randY7),new Vector2(randX8, randY8),new Vector2(randX9, randY9),
			    new Vector2(lastPointX, lastPointY)	//We use lastPointY lastPointX to indicate which is the last vector(and have some extra margin)
			};
		
		
		
		/*
		if(randGen.nextBoolean()==true && randGen.nextBoolean()==false) {
			currentPath = new Vector2[]{
			    new Vector2(0, 0), new Vector2(randX1, randY1),new Vector2(randX2, randY2), new Vector2(randX3, randY3),
			    new Vector2(randX4, randY4),new Vector2(randX5, randY5),new Vector2(randX6, randY6),
			    new Vector2(maxLastPointX*2f, randY7)	//We use maxLastPointX to indicate which is the last vector(and have some extra margin)
			};
		}else {
			currentPath = new Vector2[]{
				    new Vector2(0, 0), new Vector2(randX1, randY1),new Vector2(randX2, randY2), new Vector2(randX3, randY3),
				    new Vector2(randX4, randY4),new Vector2(randX5, randY5),new Vector2(randX6, randY6),
				    new Vector2(randX7, maxLastPointY*2f)	//We use maxLastPointX to indicate which is the last vector(and have some extra margin)
				};
		}*/
		splinePath = new CatmullRomSpline<Vector2>(currentPath, true);
		
	
		splineMusic.setLooping(true);
		splineMusic.play();
		
		
		
	}
	


	/*
	 * Sets a random move for a passby for the Bomb
	 */
	void setSealBucketMove(){
		
		sealMoveName = MOVE_NAME_BUCKET_MOVE;
		linealSealSpeed = (sealRectangle.width*2.5f); 
		sealPauseMovingEnabled = false;		
		sealPauseMovingEnabledArr.clear();
		sealPauseMovingEnabledArr = new ArrayList<Boolean>();
		float targetX;
		float targetY;
		
		
	
		//1st STEP - Move to the bucket to move it	
		float bucketCenterX = bucketRectangle.x+(bucketRectangle.width*0.5f);
		float birdCenterX = mosquitoRectangle.x+(mosquitoRectangle.width*0.5f);
		float movePercent;
		if(currentScore > 50) {
			movePercent = ((float)getRandom(5, 7))*0.1f;
			
		}else {
			movePercent = 0.5f;
		}
		
		//Move it to where the bomb is more getting out
		if(birdCenterX > bucketCenterX ) {
			sealRectangle.x = screen_width + sealRectangle.width;
			targetX = (bucketRectangle.x + (bucketRectangle.width*movePercent));
		}else {
			sealRectangle.x = 0 - sealRectangle.width;
			targetX = bucketRectangle.x - (bucketRectangle.width*movePercent); 
		}
		
		//We make an exception if the bucket is on either corner
		if(bucketRectangle.x <  (screen_width*0.2f)) { //if(bucketRectangle.x == 0) {
			sealRectangle.x = 0 - sealRectangle.width;
			targetX = bucketRectangle.x - (bucketRectangle.width*movePercent); 
		}
		if((bucketRectangle.x+ bucketRectangle.width) >  (screen_width*0.8f)) {//if(bucketRectangle.x == (screen_width - bucketRectangle.width)) {
			sealRectangle.x = screen_width + sealRectangle.width;
			targetX = (bucketRectangle.x + (bucketRectangle.width*movePercent));
		}
		if(moveCounts==0 || (bucketRectangle.x < 0 || bucketRectangle.x > screen_width) ) {
			
			sealRectangle.x = 0 - (sealRectangle.width*3);
			bucketRectangle.y = 0;//Reset it
			bucketRectangle.x = sealRectangle.x+sealRectangle.width;//Reset it  0-bucketRectangle.width;//
			targetX = ((screen_width/2) - (bucketRectangle.width*0.5f)) - sealRectangle.width; 
		}
		
		/*
		if(bucketCenterX > (screen_width/2)) {
			sealRectangle.x = screen_width + sealRectangle.width;
			targetX = (bucketRectangle.x + (bucketRectangle.width*movePercent));
		}else {
			sealRectangle.x = 0 - sealRectangle.width;
			targetX = bucketRectangle.x - (bucketRectangle.width*movePercent); 
		}*/
		
		targetY = 0;
		sealRectangle.y = 0;				
		
		currentSealIndexTouchVector = 0;
		linealSealTouch.clear();
		linealSealTouch = new ArrayList<Vector2>();
		linealSealTouch.add(currentSealIndexTouchVector, new Vector2(targetX, targetY));sealPauseMovingEnabledArr.add(false);
	
		//2nd step return back to its position		
		linealSealTouch.add(currentSealIndexTouchVector+1, new Vector2(sealRectangle.x, targetY));sealPauseMovingEnabledArr.add(false);
		
		
		
		
		
		if(randGen.nextBoolean() == true) {
			//if(randGen.nextBoolean() == true) 
				woofingSound.play();
			//else
			//	gruntSound.play();
		}
		
		walkingMusic.setLooping(true);
		walkingMusic.play();
		
	}
	
	/*
	 * Moves the bucket and removes it from the visible screen
	 */
	void setSealBucketRemoveMove(){
		
		sealMoveName = MOVE_NAME_BUCKET_MOVEREMOVE;
		linealSealSpeed = (sealRectangle.width*3.0f); 
		sealPauseMovingEnabled = false;
		sealPauseMovingEnabledArr.clear();
		sealPauseMovingEnabledArr = new ArrayList<Boolean>();
		float targetX;
		float targetY;
		
			
		//1st STEP - Move to the bucket to move it	
		float bucketCenterX = bucketRectangle.x+(bucketRectangle.width*0.5f);

		
		if(bucketCenterX < (screen_width/2)) {
			sealRectangle.x = screen_width + sealRectangle.width;
			targetX = 0 - sealRectangle.width;
		}else {
			sealRectangle.x = 0 - sealRectangle.width;
			targetX = screen_width + sealRectangle.width; 
		}
		
		targetY = 0;
		sealRectangle.y = 0;				
		
		currentSealIndexTouchVector = 0;
		linealSealTouch.clear();
		linealSealTouch = new ArrayList<Vector2>();
		linealSealTouch.add(0, new Vector2(targetX, targetY));	sealPauseMovingEnabledArr.add(false);
		
		
		
		if(randGen.nextBoolean() == true) {
			//if(randGen.nextBoolean() == true) 
				woofingSound.play();
			//else
			//	gruntSound.play();
		}
		
		walkingMusic.setLooping(true);
		walkingMusic.play();
		
	}
	
	
	void setSealCarryBucketMove(){
		
		Gdx.app.log( LOG, "SEAL CARRY MOVE" );
		
		sealMoveName = MOVE_NAME_BUCKET_CARRY;
		linealSealSpeed = (sealRectangle.width*2.0f); 
		sealPauseMovingEnabled = true;
		sealPauseMovingEnabledArr.clear();
		sealPauseMovingEnabledArr = new ArrayList<Boolean>();
		sealMovePauseTimeWait =  1000000000;
		float targetX;
		float targetY;
		

		if(bucketRectangle.x < 0){
			sealRectangle.x = 0 - sealRectangle.width;			
		}else {
			sealRectangle.x = screen_width + sealRectangle.width;			
		}
		
		targetX = (screen_width/2);
		targetY = 0;
		sealRectangle.y = 0;				
		
		currentSealIndexTouchVector = 0;
		linealSealTouch.clear();
		linealSealTouch = new ArrayList<Vector2>();
		linealSealTouch.add(0, new Vector2(targetX, targetY));
	
		//Add random positions		
		for(int i = 0; i < CARRYBUCKET_TOTAL_MOVES; i++) {
			Gdx.app.log( LOG, "i " + i);
			linealSealTouch.add(new Vector2( (float)getRandom(0, (int)(screen_width-bucketRectangle.width)), targetY));
			sealPauseMovingEnabledArr.add(true);
		}
		
		if(memoryRandom()==true)
			linealSealTouch.add(new Vector2( 0 - (sealRectangle.width*1.5f), targetY));
		else
			linealSealTouch.add(new Vector2( screen_width + (sealRectangle.width*1.5f), targetY));
		
		
		
		
		
		if(randGen.nextBoolean() == true) {
			//if(randGen.nextBoolean() == true) 
				woofingSound.play();
			//else
			//	gruntSound.play();
		}
		
		walkingMusic.setLooping(true);
		walkingMusic.play();
		
	}
	
	void setSealCarryBucketJumpMove(){
		
		Gdx.app.log( LOG, "SEAL CARRY MOVE JUMP" );
		
		
		float targetX;
		float targetY;
		
		targetX = sealRectangle.x;
		targetY = sealRectangle.y+(sealRectangle.height*0.3f);
		sealRectangle.y = 0;				
		
		currentSealIndexTouchVector = 0;
		linealSealTouch.clear();
		linealSealTouch = new ArrayList<Vector2>();
		sealPauseMovingEnabledArr.clear();
		sealPauseMovingEnabledArr = new ArrayList<Boolean>();
		//linealSealTouch.add(0, new Vector2(targetX, targetY));
		

		
		
		//Add random positions
		linealSealTouch.add( new Vector2( targetX, targetY));sealPauseMovingEnabledArr.add(false);
		linealSealTouch.add( new Vector2( targetX, 0));sealPauseMovingEnabledArr.add(false);
		
		targetY = 0;
		for(int i = 0; i < CARRYBUCKET_TOTAL_MOVES; i++) {
			Gdx.app.log( LOG, "i " + i);
			linealSealTouch.add(new Vector2( (float)getRandom(0, (int)(screen_width-bucketRectangle.width)), targetY));
			sealPauseMovingEnabledArr.add(true);
		}
			
		if(memoryRandom()==true)
			linealSealTouch.add(new Vector2( 0 - (sealRectangle.width*1.5f), targetY));
		else
			linealSealTouch.add(new Vector2( screen_width + (sealRectangle.width*1.5f), targetY));
		sealPauseMovingEnabledArr.add(false);
		
	}
	
	/*
	 * Sets a random move for a passby for the Bomb
	 */
	void setSealJumpSpringMove(){
		
		sealMoveName = MOVE_NAME_JUMP_SPRING;
		linealSealSpeed = (sealRectangle.width*2.5f); 
		sealPauseMovingEnabled = false;
		sealPauseMovingEnabledArr.clear();
		sealPauseMovingEnabledArr = new ArrayList<Boolean>();
		sealMovePauseTimeWait =  100000000;
		
		float targetX;
		float targetY;
		float bucketCenterX = bucketRectangle.x+(bucketRectangle.width*0.5f);		
	
	
		//1st STEP - Horizontal PATH				
		sealRectangle.x = screen_width + sealRectangle.width;
		//targetX = 0 - sealRectangle.width;
		targetX = springboardRectangle.x;
		
		
		sealRectangle.y = springboardRectangle.y+(springboardRectangle.height*0.4f);
		targetY = sealRectangle.y;
		
		currentSealIndexTouchVector = 0;
		linealSealTouch.clear();
		linealSealTouch = new ArrayList<Vector2>();
		linealSealTouch.add(0, new Vector2(targetX, targetY));sealPauseMovingEnabledArr.add(true);
	
		//2nd step Jump up
		targetX  = bucketCenterX;
		targetY = linealSealTouch.get(0).y + (sealRectangle.height*0.5f);
		linealSealTouch.add(1, new Vector2(targetX, targetY));sealPauseMovingEnabledArr.add(false);
		
		
		//3rd step Drop down	
		targetX  = bucketCenterX-(sealRectangle.width*0.5f);
		targetY = 0f;
		linealSealTouch.add(2, new Vector2(targetX, targetY));sealPauseMovingEnabledArr.add(false);
		
		
		
		//Get the seal out of the bucket			
		targetY = (bucketRectangle.y+bucketRectangle.height);		
		linealSealTouch.add(3, new Vector2(targetX, targetY));sealPauseMovingEnabledArr.add(false);
		
		targetX  = (bucketCenterX < (screen_width/2)) ? (bucketRectangle.x+bucketRectangle.width) : bucketRectangle.x;
		targetY = (bucketRectangle.y+bucketRectangle.height)+(bucketRectangle.height*0.4f);
		linealSealTouch.add(4, new Vector2(targetX, targetY));sealPauseMovingEnabledArr.add(false);
		
		targetX  = (bucketCenterX < (screen_width/2)) ? (bucketRectangle.x+bucketRectangle.width) + (sealRectangle.width*0.2f) : bucketRectangle.x - (sealRectangle.width*1.2f);
		targetY = (bucketRectangle.y+bucketRectangle.height);
		linealSealTouch.add(5, new Vector2(targetX, targetY));sealPauseMovingEnabledArr.add(false);
		
		targetY = 0;
		linealSealTouch.add(6, new Vector2(targetX, targetY));sealPauseMovingEnabledArr.add(false);
		
		
		
		if(slowDJMusic.isPlaying()==false) {
				slowDJMusic.setLooping(true);
				slowDJMusic.play();		
		}
		
		gruntSound.play();
		walkingMusic.setLooping(true);
		walkingMusic.play();
		
		
		
	}

	/*
	 * Sets a random move for a passby for the Bomb
	 */
	void setSealJumpMove(){
		
		sealMoveName = MOVE_NAME_JUMP;
		linealSealSpeed = (sealRectangle.width*2.5f); 
		sealPauseMovingEnabled = false;
		sealPauseMovingEnabledArr.clear();
		sealPauseMovingEnabledArr = new ArrayList<Boolean>();
		
		float targetX;
		float targetY;
		float bucketCenterX = bucketRectangle.x+(bucketRectangle.width*0.5f);
		Gdx.app.log( LOG, "SET BOMB paTh" );
		//Randomly choose between vertical or horizontal paths
	
		//1st STEP - Horizontal PATH		
		if(bucketCenterX < (screen_width/2)) {
			sealRectangle.x = screen_width + sealRectangle.width;
			//targetX = 0 - sealRectangle.width;
			targetX = (bucketRectangle.x+bucketRectangle.width) + (sealRectangle.width*0.1f);// + (bucketRectangle.width*0.5f);
		}else {
			sealRectangle.x = 0 - sealRectangle.width;
			//targetX = screen_width + sealRectangle.width;
			targetX = bucketRectangle.x - (sealRectangle.width*1.1f); 
		}
		targetY = 0;
		sealRectangle.y = 0;				
		
		currentSealIndexTouchVector = 0;
		linealSealTouch.clear();
		linealSealTouch = new ArrayList<Vector2>();
		linealSealTouch.add(currentSealIndexTouchVector, new Vector2(targetX, targetY));sealPauseMovingEnabledArr.add(false);
	
		//2nd step Jump up
		targetY = bucketRectangle.y+(bucketRectangle.height*1.0f);
		linealSealTouch.add(currentSealIndexTouchVector+1, new Vector2(targetX, targetY));sealPauseMovingEnabledArr.add(false);
		
		if(sealRectangle.x <=0 )
			targetX  = bucketRectangle.x; 
		else
			targetX  = bucketRectangle.x + bucketRectangle.width;
		
		targetY = bucketRectangle.y+(bucketRectangle.height*1.8f);

		linealSealTouch.add(currentSealIndexTouchVector+2, new Vector2(targetX, targetY));sealPauseMovingEnabledArr.add(false);
		
		
		//3rd step Drop down
		if(sealRectangle.x <=0 )
			targetX  = bucketCenterX; 
		else
			targetX  = bucketCenterX;
		
		
		targetX  = bucketCenterX-(sealRectangle.width*0.5f);
		targetY = bucketRectangle.y+(bucketRectangle.height*1.0f);		
		linealSealTouch.add(currentSealIndexTouchVector+3, new Vector2(targetX, targetY));sealPauseMovingEnabledArr.add(false);
		
		targetY = 0f;
		linealSealTouch.add(currentSealIndexTouchVector+4, new Vector2(targetX, targetY));sealPauseMovingEnabledArr.add(false);
		
		
		
		//Get the seal out of the bucket
		linealSealTouch.add(currentSealIndexTouchVector+5, linealSealTouch.get(3));sealPauseMovingEnabledArr.add(false);
		linealSealTouch.add(currentSealIndexTouchVector+6, linealSealTouch.get(2));sealPauseMovingEnabledArr.add(false);
		linealSealTouch.add(currentSealIndexTouchVector+7, linealSealTouch.get(1));sealPauseMovingEnabledArr.add(false);
		linealSealTouch.add(currentSealIndexTouchVector+8, linealSealTouch.get(0));sealPauseMovingEnabledArr.add(false);
		
		
		if(slowDJMusic.isPlaying()==false) {

				slowDJMusic.setLooping(true);
				slowDJMusic.play();		

		}
		
		gruntSound.play();
		walkingMusic.setLooping(true);
		walkingMusic.play();
		
		
		
	}
	
	/*
	 * Sets a random move for a passby only (mosquito will not stop on the screen).
	 */
	void setPassByMove(){
		Gdx.app.log( LOG, "PASS BY MOVE");
		moveName =  MOVE_NAME_PASSBY;
		positionType =  POSITION_TYPE_FLYBY;
		
		pathType = PATH_TYPE_LINEAL;
		
		 mosquitoRectangle.width = screen_width*0.3f;
		 mosquitoRectangle.height =  mosquitoRectangle.width*1.5f;
		 
		 linealSpeed = (mosquitoRectangle.height*14.0f);
		
		float targetX;
		float targetY;
		
		//Randomly choose between vertical or horizontal paths
		if(moveCounts==0 || randGen.nextBoolean() == true) {
			Gdx.app.log( LOG, "SET VERTICAL PATH" );
			//Vertical PATH			
			if(moveCounts==0 || randGen.nextBoolean() == true) {
				mosquitoRectangle.y = 0-mosquitoRectangle.height;
				targetY = screen_height + mosquitoRectangle.height;
				
			}else {
				mosquitoRectangle.y = screen_height + mosquitoRectangle.height;
				targetY = 0-mosquitoRectangle.height;
			}
			
			int min = (int) mosquitoRectangle.width;
			int max = (int) (screen_width - mosquitoRectangle.height); 
			int value =  (min + (int)(Math.random() * (max - min)));
			int value2 =  (min + (int)(Math.random() * (max - min)));
			targetX = (float) value;
			mosquitoRectangle.x = value2;//We also need to reset mosquito's X
		}else  { 
			//Horizontal PATH
			
			if(randGen.nextBoolean() == true) {
				mosquitoRectangle.x = screen_width + mosquitoRectangle.height;
				targetX = 0 - mosquitoRectangle.height;
			}else {
				mosquitoRectangle.x = 0 - mosquitoRectangle.height;
				targetX = screen_width + mosquitoRectangle.height;
			}
			int min = (int) 1;
			int max = (int) screen_height; 
			int value =  (min + (int)(Math.random() * (max - min)));
			int value2 =  (min + (int)(Math.random() * (max - min)));
			targetY = value;
			mosquitoRectangle.y = value2;
			
		}
		Gdx.app.log( LOG, "seted actual pos " + mosquitoRectangle.x + " / " + mosquitoRectangle.y);
		Gdx.app.log( LOG, "PATH target " + targetX + " / " +targetY);
		
		currentIndexTouchVector = 0;
		linealTouch.clear();
		linealTouch = new ArrayList<Vector2>();
		linealTouch.add(currentIndexTouchVector, new Vector2(targetX, targetY));
		
		//NOTE: dont raise index(++) FROM HERE BELLOW...only reference by adding 1 directly in any expression
		//linealTouch.add(currentIndexTouchVector+1, null);  
		//linealTouch[currentIndexTouchVector+1] =  new Vector2();
		//linealTouch[currentIndexTouchVector+1].set(200, 400);
		
		
		/*if(randGen.nextBoolean() == true)
			flyby2Sound.play();
		else
			flyby4Sound.play();
		*/
	}
	
	/*
	 * Triguered when the bomb its detonated
	 */
	
	void setPassByeByeMove(){
		Gdx.app.log( LOG, "PASS BYE BYE" );
		//cleanMove();
		moveName =  MOVE_NAME_PASSBY;
		positionType =  POSITION_TYPE_NORMAL;
		
		pathType = PATH_TYPE_LINEAL;
		
		 //mosquitoRectangle.width = screen_width*0.3f;
		 //mosquitoRectangle.height =  mosquitoRectangle.width*1.5f; 
		 
		 //linealSpeed = (mosquitoRectangle.height*14.0f);
		 linealSpeed = (mosquitoRectangle.height*4.0f) * linealSpeedLevelFactor;
		
		float targetX;
		float targetY;
		

		if(randGen.nextBoolean() == true) {			
			targetX = 0 - mosquitoRectangle.height;
		}else {			
			targetX = screen_width + mosquitoRectangle.height;
		}
		int min = (int) screen_height/2;
		int max = (int) screen_height; 
		int value =  (min + (int)(Math.random() * (max - min)));
		
		targetY = value;
		
		//mosquitoRectangle.x = screen_width /2;
		//mosquitoRectangle.y = screen_height /2;
		
		
		currentIndexTouchVector = 0;
		linealTouch.clear();
		linealTouch = new ArrayList<Vector2>();
		linealTouch.add(currentIndexTouchVector, new Vector2(targetX, targetY));
	
	}
	
	
	void setSealEscapeMove(){
		sealPauseMovingEnabled = false;
		sealPauseMovingEnabledArr.clear();
		sealPauseMovingEnabledArr = new ArrayList<Boolean>();
		
		
		
		float targetX;
		float targetXHalf;
		float targetY;
		
		float bucketCenterX = bucketRectangle.x+(bucketRectangle.width*0.5f);
		if( sealRectangle.x < bucketCenterX) {			
			targetX = 0 - sealRectangle.width;
			targetXHalf = sealRectangle.x*0.5f;
		}else {			
			targetX = screen_width + sealRectangle.width;
			targetXHalf = targetX*0.75f;
		}
		//Notice this condition must be BEFORE assgining sealMoveName
		if(sealMoveName == MOVE_NAME_JUMP_SPRING) targetX = screen_width + sealRectangle.width;
			
		sealMoveName =  MOVE_NAME_ESCAPE;
		sealPositionType =  SEAL_POSITION_TYPE_WALK;		
		linealSealSpeed = (sealRectangle.width*10.0f); 

		targetY = sealRectangle.y;///DONT CHANGE HEIGHT
		

		currentSealIndexTouchVector = 0;
		linealSealTouch.clear();
		linealSealTouch = new ArrayList<Vector2>();
		linealSealTouch.add(currentSealIndexTouchVector, new Vector2(targetXHalf, targetY));//add a first ski effect like
		linealSealTouch.add(currentSealIndexTouchVector+1, new Vector2(targetX, targetY));
		
		sealWalkAnimation.setFrameDuration(WALK_ANIMATION_SPEED*0.2f);
		walkingMusic.stop();
		//ouchSound.play();
	
	}
	
	/*
	 * Sets a Feint move on any corner on the screen, and inmediately go back in reverse mode
	 */
	void setSealFeintMove() {
		
		Gdx.app.log( LOG, "FEINT MOVE SEAL");
		sealPositionType =  SEAL_POSITION_TYPE_WALK;
		sealMoveName =  MOVE_NAME_FEINT;
		
		float originX;
		float originY;
		float targetX;
		float targetY;
		float originX2=0.0f;
		float targetX2=0.0f;
		
		
		sealMovePauseTimeWait = feintMovePauseTimeWaitLevelTime;		
		
		linealSealSpeed = (sealRectangle.width*3.0f); 

		sealPauseMovingEnabled = true;
		sealPauseMovingEnabledArr.clear();
		sealPauseMovingEnabledArr = new ArrayList<Boolean>();
		//Horizontal PATH		
		
		touchEnabled = true;
		if(randGen.nextBoolean() == true) {
			originX = screen_width + sealRectangle.height;				 
			targetX = originX - (sealRectangle.height*1.5f);///Note double
			sealRectangle.x = originX;
			
			if(currentScore > 50) {
				if(memoryRandom(BOLINDX_FEINTTYPE_TRUE, BOLINDX_FEINTTYPE_FALSE, 2, 2) == true) {
					sealMoveName =  MOVE_NAME_FEINT_DOUBLE;
					sealMovePauseTimeWait = (feintMovePauseTimeWaitLevelTime/2);
					originX2 = 0 - (sealRectangle.height*1.0f);//a little bit to make it faster
					targetX2 =  originX2 + (sealRectangle.height*0.5f);
				}
			}
		}else {
			originX = 0 - sealRectangle.width;				
			targetX =  originX + (sealRectangle.height*0.5f);
			sealRectangle.x = originX;
			
			if(currentScore > 50) {
				if(memoryRandom(BOLINDX_FEINTTYPE_TRUE, BOLINDX_FEINTTYPE_FALSE, 2, 2) == true) {
					sealMoveName =  MOVE_NAME_FEINT_DOUBLE;
					sealMovePauseTimeWait = (feintMovePauseTimeWaitLevelTime/2);
					originX2 = screen_width + (sealRectangle.height*0.5f);//a little bit to make it faster
					targetX2 =  originX2 - (sealRectangle.height*1.0f);
				}
			}
		}

		targetY = 0f;
		originY = 0f;
		sealRectangle.y = originY;
			

		
		currentSealIndexTouchVector = 0;
		linealSealTouch.clear();
		linealSealTouch = new ArrayList<Vector2>();
		linealSealTouch.add(currentSealIndexTouchVector, new Vector2(targetX, targetY));

		//NOTE: dont raise index(++) FROM HERE BELLOW...only reference by adding 1 directly in any expression
		//linealTouch[currentIndexTouchVector+1] = null;  
		linealSealTouch.add(currentSealIndexTouchVector+1, new Vector2(originX, originY));
		
		
		if(originX2 != 0.0f) {
			linealSealTouch.add(currentSealIndexTouchVector+2, new Vector2(originX2, originY));//this vector is not really used as path/direction, only to take the values
			linealSealTouch.add(currentSealIndexTouchVector+3, new Vector2(targetX2, originY));
			linealSealTouch.add(currentSealIndexTouchVector+4, new Vector2(originX2, originY));
			Timer.schedule(new Timer.Task(){
		 	    @Override
		 	    public void run() { 
		 	    	
		 	    	woofingSound.play(); 		 	    
		 	    }
		 	}, 0.5f);
			
		}else {
			woofingSound.play();
		}
		
		
		
		
		//feintSound.play();
		
	}
	
	void setFeintMoveGameOver() {
		
		//cleanMove();
		
		moveName =  MOVE_NAME_FEINT_GAMOVR;
		pathType = PATH_TYPE_LINEAL;
		positionType =  POSITION_TYPE_SMILE;
		
		linealSpeed = (mosquitoRectangle.height*14.0f);
		//linealSpeed = linealSpeed *0.03f;
		
		touchEnabled = false;
		
		feintOverTimeStart = -1;
		feintOverTimeCount = 0;
		
		float originX;
		float originY;
		float targetX;
		float targetY;
		
		//Randomly choose between vertical or horizontal paths
		
		//Vertical PATH
		
			
		
		movePauseTimeWait = 1000000000;
		
		originY = screen_height + (mosquitoRectangle.height*0.5f);
		targetY = originY-(mosquitoRectangle.height*1.0f); //notice 1.5 instead of 0.5
		mosquitoRectangle.y = originY; 
	
		
		int min = (int) mosquitoRectangle.width;
		int max = (int) (screen_width - mosquitoRectangle.width); 
		int value =  (min + (int)(Math.random() * (max - min)));
		
		targetX = (float) value;
		originX =  value;
		mosquitoRectangle.x = originX;
		
		
		currentIndexTouchVector = 0;
		linealTouch.clear();
		linealTouch = new ArrayList<Vector2>();
		linealTouch.add(currentIndexTouchVector, new Vector2(targetX, targetY));
		
		//NOTE: dont raise index(++) FROM HERE BELLOW...only reference by adding 1 directly in any expression
		//linealTouch[currentIndexTouchVector+1] = null;  		
		linealTouch.add(currentIndexTouchVector+1, new Vector2(originX, originY));
		
		pauseMovingEnabled = true;
		

	}
	
	
	/*
	 * Sets a free fall move usually needed after the mosquito is dead.
	 */
	void setFreefallMove() {
		
		moveName = MOVE_NAME_FREEFALL;
		pathType = PATH_TYPE_SPLINE;
		positionType = POSITION_TYPE_DEAD;
		touchEnabled = false;
		fixedAngle = false;
		pauseMovingEnabled = false;
		splineTime = 0; //We need to reset splineTime also on the splinePath
		
		maxSplineSpeed = 0.576f; 
		splineSpeed = maxSplineSpeed;
		  
		
		//rotateMosquito = rotate;
		
		
		if(rotateMosquito==true) {
			if(mosquitoRectangle.x < 0 )
				mosquitoRectangle.x = 0;
			if(mosquitoRectangle.x > (screen_width-mosquitoRectangle.width) )
				mosquitoRectangle.x = screen_width - mosquitoRectangle.width;
		}
		
		movePauseTimes.clear();
		movePauseTimes = new ArrayList<Float>();
		
		int randX1;
		int randX2;
		int randX3;
		int randX4;
		int randX5;
		int randX6;
		int randY1;
		int randY2;
		int randY3;
		int randY4;
		int randY5;
		int randY6;
		
		int minX;
		int maxX;
		int minY;
		int maxY;
		minX = (int) (mosquitoRectangle.x - (mosquitoRectangle.width*0.5f));
		maxX = (int) (mosquitoRectangle.x + (mosquitoRectangle.width*0.5f));
		minY = 0;
		maxY = screen_height-((int)mosquitoRectangle.height);
		
		if(minX < 0) minX = 0;
		if(maxX > (screen_width-mosquitoRectangle.width)) maxX = (int) (screen_width-mosquitoRectangle.width);
		
		
		
		randX1 = getRandom(minX, maxX);
		randY1 = (int) (mosquitoRectangle.y - (mosquitoRectangle.height * 0.3f));
		
		randX2 = getRandom(minX, maxX);
		randY2 = (int) (mosquitoRectangle.y - (mosquitoRectangle.height * 0.6f));
		
		randX3 = getRandom(minX, maxX);
		randY3 = (int) (mosquitoRectangle.y - (mosquitoRectangle.height * 1.3f));
		
		randX4 = getRandom(minX, maxX);
		randY4 = (int) (mosquitoRectangle.y - (mosquitoRectangle.height * 1.8f));
		
		randX5 = getRandom(minX, maxX);
		randY5 = (int) (mosquitoRectangle.y - (mosquitoRectangle.height * 2.5f));
		
		randX6 = getRandom(minX, maxX);
		randY6 = (int) (mosquitoRectangle.y - (mosquitoRectangle.height * 3.5f));
		
		
		float firstPointX;
		float firstPointY;
		float lastPointX;
		float lastPointY;
		
		firstPointX = mosquitoRectangle.x;
		firstPointY = mosquitoRectangle.y;
		lastPointX = mosquitoRectangle.x;
		lastPointY = minLastPointY;
		
	
		lastPointX = lastPointX*2;
		lastPointY = lastPointY*2;
		
		
		/*
		currentPath = new Vector2[]{
			    new Vector2(0, 0), new Vector2(200, 300),new Vector2(50, 100), new Vector2(150, 640),new Vector2(50, 340)
		};
		*/
		
		currentPath = new Vector2[]{
			    new Vector2(firstPointX, firstPointY), new Vector2(randX1, randY1),new Vector2(randX2, randY2), new Vector2(randX3, randY3),
			    new Vector2(randX4, randY4),new Vector2(randX5, randY5),new Vector2(randX6, randY6),
			    new Vector2(lastPointX, lastPointY)	//We use lastPointY lastPointX to indicate which is the last vector(and have some extra margin)
			};
		

		splinePath = new CatmullRomSpline<Vector2>(currentPath, true);
		
		freefallSound.play();
		
	}
	

	
	
	/*
	 * Detonates the bomb already placed
	 */
	void detonateBomb() {
		
	 	  detonateBomb = true;
	 	  
	 	  freefallSound.play();
	 	
		
		
	}
	
	/*
	 * Disables tracking time of the game and interaction
	 */
	void disablePlay() {
		timeTrackingEnabled = false;
		inputEnabled = false;
		gamingEnabled = false;
		
		//slowDJMusic.stop();			
		
		splineMusic.stop();						
	}
	
	void countFail() {
		bamSound.play();
		//if(1==1)return;
		if(currentMoveFails < 2 ) {
		  if(moveName == MOVE_NAME_FIGHT) {
			  //lets add an extra pause to make the mosquito laugh about the fail just commited by the user...ahahaha
			  float nextPause;
			//First thing is to restrict the range. We dont want a pause when mosquito its almost out of screen or just getting in
			  if(splineTime > 0.2f &&  splineTime < 0.8f) {
				  nextPause = splineTime + 0.15f;
				  Gdx.app.log( LOG, "---ADDED PAUSE " + nextPause );
				  
				  
				  movePauseTimes.clear();
				  movePauseTimes = new ArrayList<Float>();
				  movePauseTimes.add(nextPause);
				  pauseMovingEnabled = true;
			  }	  
		  }
		}
		  
		  
		  
		  if(currentScoreFails == (FAIL_COUNT_LIMIT-3)) warningSound.play(); 
		 
		  currentScoreFails++;
		  currentMoveFails++;
		  
		  failCountMessageTimeStart = System.nanoTime();
		  if(currentScoreFails >= FAIL_COUNT_LIMIT) {
			  gameEndInfo = "SHOT OUT";
			  stopDJMusic=true;
			  raiseGameOver();
		  }
		
	}
	
	void countWin() {
		
		//Notice this should be before setFreefallMove()
		//moveNameAssertionCounts[ moveName ]++;
		currentScore += bombScoreValue;
		

		winCountMessageTimeStart = System.nanoTime();
		pointSound.play();

		
		if(currentScore >= TOTAL_MOSQUITOS) raiseGameWin();		 	
		
	}
	
	void setGameEnded() {
		currentGameScreen = GAME_SCREEN_ENDED;
		
		disablePlay();
				
		
		int secsDuration;
		secsDuration = (int) (gameTimeTrack/ 1000000000.0);
		secsDuration =  Math.round(secsDuration/10) * 10;
		//actionResolver.trackEvent("Score", "Score " + currentScore, "Time "+ secsDuration, 0);
		actionResolver.trackEvent("Score", "Score " + currentScore, gameEndInfo, secsDuration);//Score  35, POINT OUT, 38s
		
		//User for passing it to the GPGS onActivityResult method...
		//WARNING:This is the score as it is saved on GPGS
		preferences.putInteger( PREF_CURRENT_SCORE, currentScore);
		preferences.flush();
		
		//Game Services: Submit achievement if its higher than highest one
		if(actionResolver.getSignedInGPGS()==true) {
			actionResolver.submitScoreGPGS("CgkIvvacvrwIEAIQBw", currentScore);
			if (currentLevel == 100) actionResolver.unlockAchievementGPGS("CgkIvvacvrwIEAIQBQ");
			else if (currentLevel >= 80) actionResolver.unlockAchievementGPGS("CgkIvvacvrwIEAIQBA");
			else if (currentLevel >= 60) actionResolver.unlockAchievementGPGS("CgkIvvacvrwIEAIQAw");
			else if (currentLevel >= 40) actionResolver.unlockAchievementGPGS("CgkIvvacvrwIEAIQAg");
			else if (currentLevel >= 20) actionResolver.unlockAchievementGPGS("CgkIvvacvrwIEAIQAQ");
		}

		
		//TODO:Add specific details
		/*
		for(int i=0; i<moveNameCounts.length; i++) {
            if(moveNameCounts[i] != null) { 
            	//Gdx.app.log( LOG, "POINT: "+ MOVE_NAMES[i] + ":" +moveNameAssertionCounts[ i ] + "/"+moveNameCounts[i]);
            	float value = ((float)moveNameAssertionCounts[ i ] / (float)moveNameCounts[i]) * 100f;
            	Gdx.app.log( LOG, "POINT: "+ MOVE_NAMES[i] + ":" +moveNameAssertionCounts[ i ] + "/"+moveNameCounts[i] +"="+ (int)value);
            	gsAnalytics.trackEvent("Point", "Level " + currentLevel, MOVE_NAMES[i], (int)value);
            }
        }
		*/
		
	
	}
	
	void raiseGameWin() {
		currentLevel+= 20;
		if(currentLevel > MAXIMUM_LEVEL_NUMBER)  currentLevel = MAXIMUM_LEVEL_NUMBER;
		
		if(currentScore > TOTAL_MOSQUITOS) currentScore = TOTAL_MOSQUITOS;
		
		gameWin = true;
		gameEndInfo = "LEFT "+ (FAIL_COUNT_LIMIT - currentScoreFails);
		setGameEnded();
		gameTimeStart = 0;
		
		
		preferences.putInteger( PREF_CURRENT_LEVEL, currentLevel);
		preferences.flush();

		
	}

	
	void raiseGameOver() {
		Gdx.app.log( LOG, "RAISE GEMEOVER" );
		gameWin = false;
		setGameEnded();

		
		//gameOverSound.play();
		gameTimeStart = 0;
		

		
	}
	
	
	private void renderLoadingScreen(){
		
		//Gdx.app.log( LOG, "LOADING SCREEN" );
   	 	/*
		//WinFail Message
		String winFailMessage = "Chikungunya";				    	
    	fontWinFail.draw(batch, winFailMessage, (screen_width*0.5f)-(fontWinFail.getBounds(winFailMessage).width*0.5f), (screen_height*0.6f)+(fontWinFail.getBounds(winFailMessage).height*0.5f));        
   	 	 */
		//Color c = new Color(batch.getColor());
		//float transparency =  0.1f;			
		batch.setColor(loadingAlpha,loadingAlpha,loadingAlpha,loadingAlpha); 		
		//batch.setColor(c);//reset it
		
    	batch.draw(loadingTexture, loadingRectangle.x, loadingRectangle.y, loadingRectangle.width, loadingRectangle.height);
    	
		
	}
	
	
	private void renderPlayingScreen(){ 
		
		//Gdx.app.log( LOG, "PLAYING SCREEN");
		//Gdx.app.log( LOG, "MEMORY RANDOM 8/1 " + Boolean.toString(memoryRandom(BOLINDX_SHOWADS_TRUE, BOLINDX_SHOWADS_FALSE, 8, 1)));
		

		
		if(gameTimeStart == 0L) gameTimeStart = System.nanoTime();	
		if(timeTrackingEnabled == true) gameTimeTrack = System.nanoTime() - gameTimeStart;
		
		if(Gdx.input.justTouched()) {

		    Vector3 touchPos = new Vector3();			      	
      		touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);//get touch splinePosition
      		camera.unproject(touchPos);//transform splinePosition to proper camera dimensions			
      		
      		if(inputEnabled == true) {
      			if (bombRectangle.contains(touchPos.x, touchPos.y) ) {
      				Gdx.app.log( LOG, "TOUCHED BOMB");
      				bam2Sound.play();
      				if(detonateBomb == false) detonateBomb();
      				
      			}else if (mosquitoRectangle.contains(touchPos.x, touchPos.y) && touchEnabled==true) {
      			      						
      					bam2Sound.play();	
      					if(detonateBomb == false) detonateBomb();
      					/*
  						if(pathType == PATH_TYPE_SPLINE){
  							splineSpeed = splineSpeed*0.95f;      							
  						}else{
  							linealSpeed = linealSpeed*0.95f;
  						}
  						  							
						bam2Sound.play();  							  						
						
						positionType = POSITION_TYPE_HURT;
						doubleKillCount++;
  						*/
      					      				
      			}else if (sealRectangle.contains(touchPos.x, touchPos.y) && touchEnabled==true) {
      				
      				if(sealMoveName == MOVE_NAME_BUCKET_CARRY) {
      					bam2Sound.play();
      					setSealCarryBucketJumpMove();
      				}else {
      				
	      				if(firstTapCountedInstruc ==false) firstTapCountedInstruc = true;
	      				if(memoryRandom()==true) ouchSound.play();
	      				bam2Sound.play();
	      				if(memoryRandom()==true) slowDJMusic.stop();
	      				setSealEscapeMove();
      				}
			    }else {
			    	bamSound.play();			    	  
			    }			      	
      		}
		}

		//Move the Bird
		if (standByTimeStart != 0) {
			if(((int) (standByTimeCount / 1000000000.0)) >= standByTimeWait) {
				Gdx.app.log( LOG, "TIME BINGO " + standByTimeCount);
				
				if(gamingEnabled == true && moveCounts>=1) setRandomMove(MOVE_NAME_ANY);
			}else {
				
				standByTimeCount = System.nanoTime() - standByTimeStart;//we have reached the target, start counting again
			}	
			
		}else {
			if(pathType == PATH_TYPE_SPLINE) moveSplinePath();
			else if(pathType == PATH_TYPE_LINEAL) moveLineal();			
		}
		
		//Move the Seal
		if (sealStandByTimeStart != 0) {
			if(((int) (sealStandByTimeCount / 1000000000.0)) >= sealStandByTimeWait) {
				//Gdx.app.log( LOG, "TIME BINGO " + sealStandByTimeCount);
				
				if(gamingEnabled == true && currentScore > 0) setSealRandomMove(MOVE_NAME_ANY);
			}else {				
				sealStandByTimeCount = System.nanoTime() - sealStandByTimeStart;//we have reached the target, start counting again
			}	
			
		}else {			
			moveSealLineal();			
		}
		
		
		//Draw springboard
		if(moveCounts>1) batch.draw(springBoardTexture, springboardRectangle.x, springboardRectangle.y, springboardRectangle.width, springboardRectangle.height);
		
		//Draw faucet
		//if(moveCounts>1) batch.draw(faucetTexture, faucetRectangle.x, faucetRectangle.y, faucetRectangle.width, faucetRectangle.height);
				
		
		//Draw mosquito
		/*if(positionType == POSITION_TYPE_FLYBY)
			animMosquitoTextureRegion = flybyAnimation.getKeyFrame(currentDeltaTime);		
		if(positionType == POSITION_TYPE_SMILE)
			animMosquitoTextureRegion = smileAnimation.getKeyFrame(currentDeltaTime);
		if(positionType == POSITION_TYPE_TONGUE)
			animMosquitoTextureRegion = tongueAnimation.getKeyFrame(currentDeltaTime);
		if(positionType == POSITION_TYPE_NORMAL)			
			animMosquitoTextureRegion = normalAnimation.getKeyFrame(currentDeltaTime);
		if(positionType == POSITION_TYPE_HURT)			
			animMosquitoTextureRegion = hurtAnimation.getKeyFrame(currentDeltaTime);
		if(positionType == POSITION_TYPE_DEAD)			
			animMosquitoTextureRegion = springBoardTexture;
		*/
		animMosquitoTextureRegion = normalAnimation.getKeyFrame(currentDeltaTime);
		    	
		Color c = new Color(batch.getColor());
		/*if(showDoubleKill==true) {
			 batch.setColor(0.8f,1.0f,0.8f,1.0f); 
		}*/
		if( showInvisible == true) {
			float transparency = (doubleKillCount / 2f) * 0.1f;
			if(doubleKillCount==0) transparency = 0.1f;
			if(doubleKillCount==1) transparency = 0.6f;
			batch.setColor(1f,1f,1f,transparency); 
		}
		
		batch.draw(animMosquitoTextureRegion, mosquitoRectangle.x, mosquitoRectangle.y, mosquitoRectangle.width/2, mosquitoRectangle.height/2, mosquitoRectangle.width, mosquitoRectangle.height,1f, 1f, mosquitoAngle);
		batch.setColor(c);//reset it
		
		//Draw bomb
		if(bombScoreValue==3) waterBalloonTextureRegion = ballonYellowTextureRegion;
		if(bombScoreValue==4) waterBalloonTextureRegion = ballonBlueTextureRegion;
		if(bombScoreValue==5) waterBalloonTextureRegion = ballonGreenTextureRegion;
		if(bombScoreValue==6) waterBalloonTextureRegion = ballonRedTextureRegion;
		if(bombScoreValue==7) waterBalloonTextureRegion = ballonPurpleTextureRegion;
		 
		boolean isWin = false;
			if(detonateBomb == true){
				float floorExplodeHeight;
				
				
				if(bombRectangle.x > bucketRectangle.x && (bombRectangle.x+(bombRectangle.width*1.0f)) < (bucketRectangle.x+bucketRectangle.width) ) {
					floorExplodeHeight = bucketWaterRectangle.y + bucketWaterRectangle.height;
					isWin = true;
				
				}else if(bombRectangle.overlaps(bucketRectangle)) {
					floorExplodeHeight = bucketRectangle.y + (bucketRectangle.height*0.8f);
					isWin = false;
				}else {
					floorExplodeHeight = 0f;
					isWin = false;
				}
					
					
				//Gdx.app.log( LOG, " BOMB AND FLOOR " + bombRectangle.y + " | "+ floorExplodeHeight);
				if(bombRectangle.y > floorExplodeHeight && detonationTimeStart == 0) {
					bombRectangle.y -= (bombRectangle.height*0.08f);
					if(bombRectangle.y <= floorExplodeHeight) {
						detonationTimeStart = System.nanoTime();
						splashAlphaValue = 1f;
						if(isWin==true) {
							waterExplosionRectangle.width = bucketRectangle.width;
							waterExplosionRectangle.x = (bucketRectangle.x + (bucketRectangle.width*0.5f) ) - (waterExplosionRectangle.width*0.5f);
							waterExplosionRectangle.height = bucketRectangle.height*0.1f;
						}else {
							waterExplosionRectangle.width = bombRectangle.width;
							waterExplosionRectangle.x = bombRectangle.x;
							waterExplosionRectangle.height = bombRectangle.height*0.7f;
						}
												
						waterExplosionRectangle.y = bombRectangle.y;
						
						//Make the mosquito end its path as soon as possible because user already detonate the bomb
						setPassByeByeMove();
						
						if(isWin==true) {
							popBucketSound.play();
							countWin();
						}else {
							popFloorSound.play();
							countFail();
						}
							
						freefallSound.stop();

					}
				}else {
					detonationTimeTrack = System.nanoTime() - detonationTimeStart;
					
				}
				
			}else {				
					
				//if(stopFlyingBomb ==false) moveBombLineal();
				bombRectangle.x = (mosquitoRectangle.x+(mosquitoRectangle.width/2)) - (  (bombRectangle.width*0.5f));
				bombRectangle.y = (mosquitoRectangle.y) - (bombRectangle.height*0.75f);	
				animBombTextureRegion = waterBalloonTextureRegion;
				
				
			}
//			batch.draw(animBombTextureRegion, bombRectangle.x, bombRectangle.y, bombRectangle.width/2, bombRectangle.height/2, bombRectangle.width, bombRectangle.height,1f, 1f, 360f);
						

			
			if(detonationTimeStart == 0) batch.draw(animBombTextureRegion, bombRectangle.x, bombRectangle.y, bombRectangle.width, bombRectangle.height);
			
			
		
		//Draw seal tap instruc
		 lastTapInstrucDeltaTime += Gdx.graphics.getDeltaTime();
	      if(lastTapInstrucDeltaTime > 0.8f || lastTapInstrucDeltaTime < 0.5f ){		    	  
	    	  showTapInstruc = true;
	    	  if(lastTapInstrucDeltaTime>0.8f){
	    		  lastTapInstrucDeltaTime = 0f;
	    	  }
	    	  
	      }else{
	    	  showTapInstruc = false;
	      }
	      if(showTapInstruc && firstTapCountedInstruc==false && moveCounts > 1 && sealMoveName != MOVE_NAME_BUCKET_CARRY) 
	    	  batch.draw(tapInstrucTextureRegion, (sealRectangle.x+(sealRectangle.width*0.5f))-((sealRectangle.width*0.5f)*0.5f), sealRectangle.y+(sealRectangle.height*1.07f), sealRectangle.width*0.5f, sealRectangle.height*0.7f);
	      
		
		
		
		//Draw Seal
//		animSealTextureRegion = sealWalkAnimation.getKeyFrame(currentDeltaTime);//Default...stopped?
//		if(sealPositionType == SEAL_POSITION_TYPE_WALK)
//			animSealTextureRegion = sealWalkAnimation.getKeyFrame(currentDeltaTime);
		
		//Gdx.app.log( LOG, "1 FLIPX:" +animSealTextureRegion.isFlipX());
		animSealTextureRegion = new TextureRegion(sealWalkAnimation.getKeyFrame(currentDeltaTime));
		//animSealTextureRegion = sealWalkAnimation.getKeyFrame(currentDeltaTime);
		//animSealTextureRegion.flip(true, false);
		//Gdx.app.log( LOG, "2 FLIPX:" +animSealTextureRegion.isFlipX());
		
		
		if(sealMoveName == MOVE_NAME_BUCKET_CARRY) {
			if(sealMovePauseTimeCount > 0)
				animSealTextureRegion = sealSpriteTexture2[0][0];
			else
				animSealTextureRegion = sealCarryOnAnimation.getKeyFrame(currentDeltaTime);		
			
			if(sealRectangle.y > 0 ) animSealTextureRegion = sealSpriteTexture2[0][3];
			
		}else if(linealSealDir.angle() >= 280f || linealSealDir.angle() < 90f) {
			//animSealTextureRegion = new TextureRegion();
			animSealTextureRegion = sealWalkAnimation.getKeyFrame(currentDeltaTime);
		}else {
			if(animSealTextureRegion.isFlipX() == false) animSealTextureRegion.flip(true, false); //So that we dont flip on each render() event			
		}				
//		if(linealSealDir.angle() != 0.0f && linealSealDir.angle() != 180.0f)
//			sealAngle = 90f;
		if(splashTimeStart > 0) //if( sealMoveName == MOVE_NAME_JUMP && currentSealIndexTouchVector > 6) 
			animSealTextureRegion = sealSpriteTexture[0][4];
		//Gdx.app.log( LOG, "CURRENT SEAL ANGLE" + linealSealDir.angle() + "POSITION "+ sealRectangle.x + "/" + sealRectangle.y + " PROPERtIES " + sealRectangle.width + "/" + sealRectangle.height);
		batch.draw(animSealTextureRegion, sealRectangle.x, sealRectangle.y, 0, 0, sealRectangle.width, sealRectangle.height,1f, 1f, sealAngle);
			
		
		//Draw Bucket
		if(sealMoveName == MOVE_NAME_BUCKET_CARRY) {  //Carry on bucket effect
			bucketRectangle.x = sealRectangle.x;// + (sealRectangle.width*0.5f);
			bucketRectangle.y = sealRectangle.y + sealRectangle.height;
		}		
		batch.draw(bucketTextureRegion, bucketRectangle.x, bucketRectangle.y, bucketRectangle.width, bucketRectangle.height);
				
			
		//Draw bucket water percentage
		bucketWaterRectangle.x = ((bucketRectangle.x+bucketRectangle.width) - ((bucketWaterRectangle.width*1.0f) + ((bucketRectangle.width -bucketWaterRectangle.width)*0.5f) ));
		bucketBottomWaterRectangle.x = bucketWaterRectangle.x;
		bucketBottomWaterRectangle.y = bucketRectangle.y;
		bucketWaterRectangle.y = bucketBottomWaterRectangle.y+bucketBottomWaterRectangle.height;
		if(splashTimeStart == 0) {			
			bucketWaterRectangle.height =  ((float)((float)currentScore/(float)TOTAL_MOSQUITOS) ) * (bucketRectangle.height*0.7f);//*current score			
		}else {
			bucketWaterRectangle.height = bucketWaterRectangle.height;
		}
		if(currentScore > 0 && splashTimeStart ==0) batch.draw(bucketBottomWaterTextureRegion, bucketBottomWaterRectangle.x, bucketBottomWaterRectangle.y, bucketBottomWaterRectangle.width, bucketBottomWaterRectangle.height);
		batch.draw(bucketWaterTextureRegion, bucketWaterRectangle.x, bucketWaterRectangle.y, bucketWaterRectangle.width, bucketWaterRectangle.height);
		
		
		//Draw water splash explosion (SEAL ONLY) 
		if(splashTimeStart != 0) {			
			splashTimeTrack = System.nanoTime() - splashTimeStart;
			if(splashTimeTrack < 500000000) {
				float incrementSizePercent = 0.12f;
				//waterExplosionRectangle.width += (waterExplosionRectangle.width*incrementSizePercent);
				if(waterExplosionRectangle.height < ( bucketRectangle.height*2f))
					waterExplosionRectangle.height += (waterExplosionRectangle.height*incrementSizePercent);		
				//waterExplosionRectangle.x -= (waterExplosionRectangle.width*incrementSizePercent)/2;
				//waterExplosionRectangle.y -= (waterExplosionRectangle.height*incrementSizePercent)/2;
				if(bucketWaterRectangle.height>0)
					bucketWaterRectangle.height -= bucketWaterRectangle.height*(incrementSizePercent*3.0f);
				else
					bucketWaterRectangle.height = 0f;
	 			
			}else if(splashTimeTrack < 3000000000l) {
				
			}else {
				
			}
			
			if(splashAlphaValue > 0.01) 
				splashAlphaValue -= splashAlphaValue * 0.02f;
			else
				splashAlphaValue = 0f;
			//Gdx.app.log( LOG, "Enter SPLASHHH"+"POSITION "+ waterExplosionRectangle.x + "/" + waterExplosionRectangle.y + " PROPERtIES " + waterExplosionRectangle.width + "/" + waterExplosionRectangle.height);
				
			c = new Color(batch.getColor());
			batch.setColor(1f,1f,1f,splashAlphaValue); 			
			batch.draw(waterExplosionTextureRegion, waterExplosionRectangle.x, waterExplosionRectangle.y, waterExplosionRectangle.width, waterExplosionRectangle.height);
			batch.setColor(c);//reset it
		}
		
		//Draw pop bomb water explosion
		if(detonationTimeTrack > 0) {
			float incrementSizePercent;
			float incrementSizePercent2;
						
			if(isWin == true) {
				if(detonationTimeTrack < 400000000) {
					incrementSizePercent = 0.07f;
					
		 			//Lets keep it within the bucket if its win, otherwise expand in all directions					
		 				if(waterExplosionRectangle.width < bucketRectangle.width) {
		 					waterExplosionRectangle.width += (bombRectangle.width*incrementSizePercent);
							waterExplosionRectangle.x -= (bombRectangle.width*incrementSizePercent)/2;
		 				}
		 				
		 				if(waterExplosionRectangle.height < (bucketRectangle.height*0.4f)) {
		 					waterExplosionRectangle.height += (bombRectangle.height*incrementSizePercent);					 			
		 					waterExplosionRectangle.y -= (bombRectangle.height*incrementSizePercent)/2;
		 				}
		 			
				}else {
					incrementSizePercent = 0.03f;	 			
	 				if(waterExplosionRectangle.height > (bucketRectangle.height*0.25f)) {
	 					waterExplosionRectangle.height -= (bombRectangle.height*incrementSizePercent);					 			
	 		 			//waterExplosionRectangle.y += (bombRectangle.height*incrementSizePercent)*0.35f;///notice here this difference
	 		 			
	 				}
	 				//bombRectangle.width = waterExplosionRectangle.width;
		 			//bombRectangle.x = bucketRectangle.x;
	 				
	 				splashAlphaValue -= splashAlphaValue * incrementSizePercent;
				}
				
				waterExplosionRectangle.x = bucketRectangle.x;
				waterExplosionTextureRegion = splashWaterTextureRegion;
			}else {
				if(detonationTimeTrack < 300000000) {
					incrementSizePercent = 0.09f;
					incrementSizePercent2 = incrementSizePercent*2.5f;
							 							 		
					
						waterExplosionRectangle.width += (bombRectangle.width*incrementSizePercent2);
						waterExplosionRectangle.x -= (bombRectangle.width*incrementSizePercent2)/2;						
						waterExplosionRectangle.height += (bombRectangle.height*incrementSizePercent);					 			
						if(bombRectangle.y > 0) waterExplosionRectangle.y -= (bombRectangle.height*incrementSizePercent)/2;
					
				}else {
					incrementSizePercent = 0.03f;
					incrementSizePercent2 = incrementSizePercent*2.5f;
					if(waterExplosionRectangle.height > (bucketRectangle.height*0.25f)) {			 							 		
		 				waterExplosionRectangle.width -= (bombRectangle.width*incrementSizePercent2);
		 				waterExplosionRectangle.x += (bombRectangle.width*incrementSizePercent2)/2;
		 				
		 				if(bombRectangle.y > 0) {
		 					waterExplosionRectangle.height -= (bombRectangle.height*incrementSizePercent);
		 					waterExplosionRectangle.y += (bombRectangle.height*incrementSizePercent)*0.35f;///notice here this difference
		 				}else {
		 					waterExplosionRectangle.height -= (bombRectangle.height*incrementSizePercent);
		 					//waterExplosionRectangle.y += (bombRectangle.height*incrementSizePercent)*0.35f;///notice here this difference
		 				}
		 				
		 				//bombRectangle.width = waterExplosionRectangle.width;
			 			//bombRectangle.x = bucketRectangle.x;	
					}
					
					splashAlphaValue -= splashAlphaValue * incrementSizePercent;
				}
				
				if(bombRectangle.y <= 0)
					waterExplosionTextureRegion = splashFloorTextureRegion;
				else
					waterExplosionTextureRegion = splashObjectTextureRegion;
			}
			
			//Gdx.app.log( LOG, " RECTANGLE "+ waterExplosionRectangle.x + "/" + waterExplosionRectangle.y + " PROPERtIES " + waterExplosionRectangle.width + "/" + waterExplosionRectangle.height);
			c = new Color(batch.getColor());
			batch.setColor(1f,1f,1f,splashAlphaValue); 			
			batch.draw(waterExplosionTextureRegion, waterExplosionRectangle.x, waterExplosionRectangle.y, waterExplosionRectangle.width, waterExplosionRectangle.height);
			batch.setColor(c);//reset it
			
			
			
		}
		


		
		
		
		//Draw clock, swatter, etc on top of mosquito if appropiate
		//showClock = true;
		if(showClock == true) {
			if(positionType != POSITION_TYPE_DEAD) {
				clockRectangle.x = (mosquitoRectangle.x+(mosquitoRectangle.width/2)) - (  (clockRectangle.width*0.5f));
				clockRectangle.y = (mosquitoRectangle.y+mosquitoRectangle.height) - (clockRectangle.height*0.5f);
			}
			batch.draw(clockTextureRegion, clockRectangle.x, clockRectangle.y, clockRectangle.width, clockRectangle.height);
		}		
		
		
		/*
		//Draw Water Balloon
		waterBalloonRectangle.x = (mosquitoRectangle.x+(mosquitoRectangle.width/2)) - (  (waterBalloonRectangle.width*0.5f));
		waterBalloonRectangle.y = (mosquitoRectangle.y) - (waterBalloonRectangle.height*0.5f);	
											
		batch.draw(waterBalloonTextureRegion, waterBalloonRectangle.x, waterBalloonRectangle.y, waterBalloonRectangle.width, waterBalloonRectangle.height);
		*/
		
		
		
		
		
		//Draw timer
		float clockY = screen_height - (fontTimer.getBounds("test").height*0.5f);
		/*int secsDuration;
	    if(gameTimeEnded == 0)
	    	secsDuration = (int) ((((long) (GAME_DURATION*1000000000.0)) - gameTimeTrack)/ 1000000000.0);
	    else
	    	secsDuration = (int) (GAME_DURATION/ 1000000000.0);
	    
	    if(secsDuration < 0) secsDuration = 0;    
	    //String durationTxt = String.format("%02d", secsDuration)+"s";//"00:"+String.format("%02d", secsDuration);  
	    String durationTxtFixed = "00:00";
	    String durationTxt;
	    if(secsDuration >= 10) 
	    	durationTxt = "00:"+secsDuration;
	    else
	    	durationTxt = "00:0"+secsDuration;
	    if(durationTxt.length() < 3) durationTxt = " "+ durationTxt;
	    //Notice values are converted to its equivalent percent from a 255 value on Paint
	    //fontTimer.setColor(  0.054f, 0.458f, 0.717f, 0.9f );//notice we use transparent color here
	    if(secsDuration < 4) 
	    	fontTimer.setColor(Color.RED);
	    else
	    	fontTimer.setColor(0.58f, 0.58f, 0.08f, 1f); //fontTimer.setColor(Color.BLUE);
	    //float clockX  = screen_width - (fontTimer.getBounds(durationTxtFixed).width*1.2f);
	    float clockX  =  (clockIconRectangle.x + clockIconRectangle.width) ;//+ (fontTimer.getBounds(durationTxtFixed).width*1.2f);
	    float clockY = screen_height - (fontTimer.getBounds(durationTxtFixed).height*0.5f);
	    batch.draw(clockTextureRegion, clockIconRectangle.x, clockIconRectangle.y, clockIconRectangle.width, clockIconRectangle.height);
	    if (clockCountMessageTimeStart != 0) {
			if(clockCountMessageTimeCount  >= clockCountMessageTimeWait) {
				Gdx.app.log( LOG, "TIME HITMESG " + clockCountMessageTimeCount);				
				clockCountMessageTimeCount = 0;
				clockCountMessageTimeStart = 0;
			}else {												
				clockCountMessageTimeCount = System.nanoTime() - clockCountMessageTimeStart;//we have reached the target, start counting again
				float scaleTarget =  2f;//3 - (failCountMessageTimeCount / 1000000000);
				fontTimer.setScale(scaleTarget);				
				durationTxt = "+10";
			}
		}
	    
	    fontTimer.draw(batch, durationTxt, clockX , clockY);
	    fontTimer.setScale(1f);//reset it to normal
	    if(showTimeout == false && secsDuration < 10) {
	    	showTimeout = true;
	    	timerMusic.setLooping(true);
	    	timerMusic.play();
	    }
	    if(secsDuration == 0) raiseGameOver();*/
	    
	    //Remaining shots
	    String remainingShots;
	    remainingShots = ""+(FAIL_COUNT_LIMIT - currentScoreFails);
	    if((FAIL_COUNT_LIMIT - currentScoreFails) < 3) 
	    	fontTimer.setColor(Color.RED);
	    else
	    	fontTimer.setColor(0.58f, 0.58f, 0.08f, 1f); //fontTimer.setColor(Color.BLUE);
	    //float shotsX  = 0 + (fontTimer.getBounds(remainingShots).width*0.8f);
	    float shotsX  = bombIconRectangle.x + (bombIconRectangle.width*1.7f);// + (fontTimer.getBounds(remainingShots).width*0.4f);
	    float shotsY = clockY;
	    if (failCountMessageTimeStart != 0) {
			if(failCountMessageTimeCount  >= failCountMessageTimeWait) {
				Gdx.app.log( LOG, "TIME HITMESG " + failCountMessageTimeCount);				
				failCountMessageTimeCount = 0;
				failCountMessageTimeStart = 0;
				//if(showClock==true ) showClock = false;
				//if(showSwatter==true) showSwatter = false;
			}else {												
				failCountMessageTimeCount = System.nanoTime() - failCountMessageTimeStart;//we have reached the target, start counting again
				float scaleTarget =  1.5f;//3 - (failCountMessageTimeCount / 1000000000);
				fontTimer.setScale(scaleTarget);
				
				
			}
		}
	    if(moveCounts>1) batch.draw(waterBalloonTextureRegion, bombIconRectangle.x, bombIconRectangle.y, bombIconRectangle.width, bombIconRectangle.height);
	    if(showSwatter==true && winCountMessageTimeStart != 0) {	//An extra condition if we are winning an extra point from a Swatter
			float scaleTarget =  2f;
			fontTimer.setScale(scaleTarget);
			remainingShots = "+1";
		}
	    if(moveCounts>1) fontTimer.draw(batch, remainingShots, shotsX , clockY);
	    fontTimer.setScale(1f);//reset it to normal
	    
	    //Assertions shots
	    int percent; 
  		if(currentScore==0)
  			percent = 0;
  		else
  			percent = (currentScore  * 100) / TOTAL_MOSQUITOS;
  		String currScoreTxt = String.valueOf(percent)+"%";
	    String winsMsg = ""+currScoreTxt;
	    //float winsX  = (screen_width/2) - (fontTimer.getBounds(winsMsg).width*0.5f);
	    float winsX  = bucketIconRectangle.x + (bucketIconRectangle.width*1.7f); //+ (fontTimer.getBounds(winsMsg).width*1.6f);	    
	    float winsY = clockY;
	    if (winCountMessageTimeStart != 0) {		
			if(winCountMessageTimeCount  >= winCountMessageTimeWait) {				
				winCountMessageTimeCount = 0;
				winCountMessageTimeStart = 0;
				//if(showClock==true ) showClock = false;
				//if(showSwatter==true) showSwatter = false;
			}else {												
				winCountMessageTimeCount = System.nanoTime() - winCountMessageTimeStart;//we have reached the target, start counting again
				float scaleTarget =  1.5f;//3 - (failCountMessageTimeCount / 1000000000);
				fontTimer.setScale(scaleTarget);

			}
		}
	    if(moveCounts>1) batch.draw(bucketTextureRegion, bucketIconRectangle.x, bucketIconRectangle.y, bucketIconRectangle.width, bucketIconRectangle.height);
	    fontTimer.setColor(0.58f, 0.58f, 0.08f, 1f);//fontTimer.setColor(Color.BLUE);
	    if(moveCounts>1) fontTimer.draw(batch, winsMsg, winsX , winsY);
	    //Draw the amount gained on the bucket it self also
	    if (winCountMessageTimeStart != 0) {
		    winsMsg = "+"+bombScoreValue;
		    winsX = (bucketRectangle.x + (bucketRectangle.width)*0.5f) - (fontTimer.getBounds(winsMsg).width*0.5f);
		    winsY = (bucketRectangle.height * 0.8f);
		    fontTimer.draw(batch, winsMsg, winsX , winsY);
	    }
	    fontTimer.setScale(1f);//reset it to normal
	    
		

    	//Print the hit Label...either assertion or failure
    	/*if (failCountMessageTimeStart != 0) {		
			if(failCountMessageTimeCount  >= failCountMessageTimeWait) {
				Gdx.app.log( LOG, "TIME HITMESG " + failCountMessageTimeCount);				
				failCountMessageTimeCount = 0;
				failCountMessageTimeStart = 0;															
			}else {												
				failCountMessageTimeCount = System.nanoTime() - failCountMessageTimeStart;//we have reached the target, start counting again				
			}
			
			String winFailMessage = "FAIL";				    
	    	fontWinFail.draw(batch, winFailMessage, (screen_width*0.5f)-(fontWinFail.getBounds(winFailMessage).width*0.5f), (screen_height*0.6f)+(fontWinFail.getBounds(winFailMessage).height*0.5f));        
	   	  
		}*/
    	
    	
    	
    	 /*
    	 updateAccelReadings(); 		
 		//WinFail Message
 		String winFailMessage = "";		
 		winFailMessage = "X " + accelX + "Y " + accelY + "Z " + accelZ;
     	
     	fontWinFail.draw(batch, winFailMessage, (screen_width*0.5f)-(fontWinFail.getBounds(winFailMessage).width*0.5f), (screen_height*0.6f)+(fontWinFail.getBounds(winFailMessage).height*0.5f));        
    	  */
	}
	
	private void renderEndScreen(){
		
		if(gameTimeStart == 0L) gameTimeStart = System.nanoTime();	
		gameTimeTrack = System.nanoTime() - gameTimeStart;
		
		if(gameTimeTrack <  1500000000.0 ) {
			String strGameWinOver;
			if(gameWin == true) 
				strGameWinOver = "You win!";
			else
				strGameWinOver = "Game Over";
			//fontWinFail.setColor(Color.BLACK);
			fontWinFail.draw(batch, strGameWinOver, (screen_width/2)-(fontWinFail.getBounds(strGameWinOver).width/2), screen_height*(0.6f));
		}else {
			
			if(gameWin == true) {
				currentGameScreen = GAME_SCREEN_POPUP;
			}else {
				//TODO: implement better control for displaying ads when fire is triggered		
				if(memoryRandom(BOLINDX_SHOWADS_TRUE, BOLINDX_SHOWADS_FALSE, 8, 1) == true) {
					float timeOutAds;
					
					timeOutAds = 1.3f;
					timeOutAds = 0.1f * (float)getRandom(1, 3);
					//Show Interstital ad
					Timer.schedule(new Timer.Task(){
				 	    @Override
				 	    public void run() {
				 	    	
				 	    	//Request it always, google does not impress it always anyway...Requests are always greater than impressions
				 	    	actionResolver.showOrLoadInterstital();
				 	    }
				 	}, timeOutAds);
					
					currentGameScreen = GAME_SCREEN_ENDMENU;
				}/*else {
					 if(showChallengeInvitation==true && playsCount > 1) 
						 currentGameScreen = GAME_SCREEN_POPUP;
					 else
						 currentGameScreen = GAME_SCREEN_ENDMENU;
				}*/
				//if(DEV_MODE ==true) currentGameScreen = GAME_SCREEN_POPUP;
			}	
		}
		
	}
	
	
	private void renderEndMenuScreen(){
	
			int percent; 
	  		if(currentScore==0)
	  			percent = 0;
	  		else
	  			percent = (currentScore  * 100) / TOTAL_MOSQUITOS;
		
	  		String currScoreTxt = String.valueOf(percent)+"%";
	  		
	  		int currentLevelPrint;
	  		if(gameWin==true)
	  			currentLevelPrint = (currentLevel-20) ;//String.valueOf(percent)+"%"; //String.valueOf(totalScore) + " / " +TOTAL_MOSQUITOS ;
	  		else
	  			currentLevelPrint = currentLevel ;
	  		
	  		  batch.draw(scoreBoardTextureRegion, scoreBoardRectangle.x, scoreBoardRectangle.y, scoreBoardRectangle.width, scoreBoardRectangle.height);		  		  

	  		 //Draw bucket levels
	  		  fontTimer.setColor(Color.WHITE);
	  		  String level_capacity;
	  		level_capacity = "20L";
	  		if(currentLevel > 20) 
	  			levelTextureRegion = levelCompletedTextureRegion;
	  		else
	  			levelTextureRegion = levelUnlockedTextureRegion;
	  		batch.draw(levelTextureRegion, bucketLevelRectangle.x, bucketLevelRectangle.y, bucketLevelRectangle.width, bucketLevelRectangle.height);
	  		fontTimer.draw(batch, level_capacity, ((bucketLevelRectangle.x+ (bucketLevelRectangle.width/2))-(fontTimer.getBounds(level_capacity).width/2)), bucketLevelRectangle.y - (fontTimer.getBounds(level_capacity).height*0.5f) );
	  		level_capacity = "40L";
	  		if(currentLevel > 40) 
	  			levelTextureRegion = levelCompletedTextureRegion;
	  		else if(currentLevel > 20)
	  			levelTextureRegion = levelUnlockedTextureRegion;
	  		else
	  			levelTextureRegion = levelLockedTextureRegion;
	  		batch.draw(levelTextureRegion, bucketLevelRectangle.x*2.0f, bucketLevelRectangle.y, bucketLevelRectangle.width, bucketLevelRectangle.height);
	  		fontTimer.draw(batch, level_capacity, (((bucketLevelRectangle.x*2.0f)+ (bucketLevelRectangle.width/2))-(fontTimer.getBounds(level_capacity).width/2)), bucketLevelRectangle.y - (fontTimer.getBounds(level_capacity).height*0.5f) );
	  		level_capacity = "60L";
	  		if(currentLevel > 60) 
	  			levelTextureRegion = levelCompletedTextureRegion;
	  		else if(currentLevel > 40)
	  			levelTextureRegion = levelUnlockedTextureRegion;
	  		else
	  			levelTextureRegion = levelLockedTextureRegion;
	  		batch.draw(levelTextureRegion, bucketLevelRectangle.x*3.0f, bucketLevelRectangle.y, bucketLevelRectangle.width, bucketLevelRectangle.height);
	  		fontTimer.draw(batch, level_capacity, (((bucketLevelRectangle.x*3.0f)+ (bucketLevelRectangle.width/2))-(fontTimer.getBounds(level_capacity).width/2)), bucketLevelRectangle.y - (fontTimer.getBounds(level_capacity).height*0.5f) );
	  		level_capacity = "80L";
	  		if(currentLevel > 80) 
	  			levelTextureRegion = levelCompletedTextureRegion;
	  		else if(currentLevel > 60)
	  			levelTextureRegion = levelUnlockedTextureRegion;
	  		else
	  			levelTextureRegion = levelLockedTextureRegion;
	  		batch.draw(levelTextureRegion, bucketLevelRectangle.x*4.0f, bucketLevelRectangle.y, bucketLevelRectangle.width, bucketLevelRectangle.height);
	  		fontTimer.draw(batch, level_capacity, (((bucketLevelRectangle.x*4.0f)+ (bucketLevelRectangle.width/2))-(fontTimer.getBounds(level_capacity).width/2)), bucketLevelRectangle.y - (fontTimer.getBounds(level_capacity).height*0.5f) );
	  		level_capacity = "100L";
	  		if(currentLevel == 100) 
	  			levelTextureRegion = levelCompletedTextureRegion;
	  		else if(currentLevel > 80)
	  			levelTextureRegion = levelUnlockedTextureRegion;
	  		else
	  			levelTextureRegion = levelLockedTextureRegion;
	  		batch.draw(levelTextureRegion, bucketLevelRectangle.x*5.0f, bucketLevelRectangle.y, bucketLevelRectangle.width, bucketLevelRectangle.height);
	  		fontTimer.draw(batch, level_capacity, (((bucketLevelRectangle.x*5.0f)+ (bucketLevelRectangle.width/2))-(fontTimer.getBounds(level_capacity).width/2)), bucketLevelRectangle.y - (fontTimer.getBounds(level_capacity).height*0.5f) );
	  		//batch.draw(levelLockedTextureRegion, bucketLockRectangle.x*, bucketLockRectangle.y, bucketLockRectangle.width, bucketLockRectangle.height);
	  		  
	  		//Draw Bucket
	  		bucketRectangle.x = ((scoreBoardRectangle.x+scoreBoardRectangle.width) - ((bucketRectangle.width*1.0f) + ((scoreBoardRectangle.width -bucketRectangle.width)*0.5f) ));
	  		bucketRectangle.y = (scoreBoardRectangle.y +scoreBoardRectangle.height) - (bucketRectangle.height*1.9f);
	  		bucketBottomWaterRectangle.y = bucketRectangle.y;
	  		batch.draw(bucketTextureRegion, bucketRectangle.x, bucketRectangle.y, bucketRectangle.width, bucketRectangle.height);
	  					  			
	  		//Draw bucket water percentage	  		
	  		bucketWaterRectangle.x = ((bucketRectangle.x+bucketRectangle.width) - ((bucketWaterRectangle.width*1.0f) + ((bucketRectangle.width -bucketWaterRectangle.width)*0.5f) ));
	  		bucketBottomWaterRectangle.x = bucketWaterRectangle.x;
	  		bucketWaterRectangle.y = bucketBottomWaterRectangle.y+bucketBottomWaterRectangle.height;
		
	  		bucketWaterRectangle.height =  ((float)((float)currentScore/(float)TOTAL_MOSQUITOS)) * (bucketRectangle.height*0.7f);//*current score			
	 
	  		if(currentScore > 0) batch.draw(bucketBottomWaterTextureRegion, bucketBottomWaterRectangle.x, bucketBottomWaterRectangle.y, bucketBottomWaterRectangle.width, bucketBottomWaterRectangle.height);
	  		batch.draw(bucketWaterTextureRegion, bucketWaterRectangle.x, bucketWaterRectangle.y, bucketWaterRectangle.width, bucketWaterRectangle.height);

	  		//Draw score
	  		  fontSocoreBig.setColor(Color.WHITE);
	  		  fontSocoreBig.draw(batch, currScoreTxt, ((scoreBoardRectangle.x+ (scoreBoardRectangle.width/2))-(fontSocoreBig.getBounds(currScoreTxt).width/2)), bucketRectangle.y + (bucketRectangle.height*1.45f) );//(screen_width/2)-(fontSocoreBig.getBounds(currScoreTxt).width/2), screen_height*(0.6f));
  
	  		level_capacity = currentLevelPrint+"L";	  		
	  		fontTimer.draw(batch, level_capacity, (((bucketRectangle.x)+ (bucketRectangle.width/2))-(fontTimer.getBounds(level_capacity).width/2)), bucketRectangle.y - (fontTimer.getBounds(level_capacity).height*0.5f) );
	  		

	  		
	  		  //fontpopUp.draw(batch, "CURRENT SCORE", playAgainRectangle.x , screen_height*(0.7f));
	    	  //fontpopUp.draw(batch, "HIGHEST SCORE", playAgainRectangle.x , screen_height*(0.6f));
	    	  //fontpopUp.draw(batch, String.valueOf(currentScore), playAgainRectangle.x+(playAgainRectangle.width) , screen_height*(0.7f));
	    	  //fontpopUp.draw(batch, String.valueOf(totalScore), playAgainRectangle.x+(playAgainRectangle.width) , screen_height*(0.6f));
	    	  batch.draw(playButtonTextureRegion, playAgainRectangle.x, playAgainRectangle.y,playAgainRectangle.width, playAgainRectangle.height);
	    	  batch.draw(scoreButtonTextureRegion, shareScoreRectangle.x, shareScoreRectangle.y,shareScoreRectangle.width, shareScoreRectangle.height);
	    	  batch.draw(facebookButtonTextureRegion, shareFacebookRectangle.x, shareFacebookRectangle.y,shareFacebookRectangle.width, shareFacebookRectangle.height);
	    	  batch.draw(shareButtonTextureRegion, shareAppRectangle.x, shareAppRectangle.y,shareAppRectangle.width, shareAppRectangle.height);
	    	  batch.draw(infoTextureRegion, infoRectangle.x, infoRectangle.y,infoRectangle.width, infoRectangle.height);
	    	  batch.draw(moreTextureRegion, moreRectangle.x, moreRectangle.y,moreRectangle.width, moreRectangle.height);
	    	  
	    	  
	    	  
		      if(Gdx.input.justTouched()) {
			         Vector3 touchPos = new Vector3();
			         touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);//get touch splinePosition
			         camera.unproject(touchPos);//transform splinePosition to proper camera dimensions
	
			         //Game end buttons
			         if (playAgainRectangle.contains(touchPos.x, touchPos.y)) {
			        	 Gdx.app.log( LOG, "CLICKED");
			        	 //gsAnalytics.trackEvent("Game", "Play Again");
			        	 actionResolver.trackEvent("Game", "Play Again");
			        	 restartGame();
			        	 
			         }
			         
			         if (shareScoreRectangle.contains(touchPos.x, touchPos.y)) {
			        	 Gdx.app.log( LOG, "CLICKED SHRE SCORE");	
			        	 actionResolver.trackEvent("Social", "Share Score");
			        	//actionResolver.shareContent("Seal Bucket Challenge", "My score: "+ currScoreTxt +" - I challenge you the Seal Bucket Challenge: https://play.google.com/store/apps/details?id=" + appPackageName, "Share to");			        	
			        	 actionResolver.getLeaderboardGPGS("CgkIvvacvrwIEAIQBw");
			        	
			         }
			         
			         if (shareFacebookRectangle.contains(touchPos.x, touchPos.y)) {
			      	   if(DEV_MODE==true) {
			      		 actionResolver.showAds(true);
			    	   }else {
				      	 Gdx.app.log( LOG, "CLICKED SHRE FB");	
				      	actionResolver.trackEvent("Social", "Facebook Share");
				      	 actionResolver.openUri("https://www.facebook.com/pages/Seal-Bucket-Challenge/740719129307804");
			    	  }
				      	 
				       }
			       if (shareAppRectangle.contains(touchPos.x, touchPos.y)) {
			    	   if(DEV_MODE==true) {
			    		   actionResolver.showAlertBoxNeutral("test", "testing class action resolver", "okk");
			    	   }else {
			    		   		Gdx.app.log( LOG, "CLICKED SHRE APP");	
			    		   		actionResolver.trackEvent("Social", "App Share");
			    		   		actionResolver.shareContent("Seal Bucket Challenge", "Check out Seal Bucket Challenge: " + "https://play.google.com/store/apps/details?id=" + appPackageName, "Share to");
			    	   }
			      	 
			       }
			       
			         if (infoRectangle.contains(touchPos.x, touchPos.y)) {
				      	 Gdx.app.log( LOG, "CLICKED SHRE FB");	
				      	actionResolver.trackEvent("Social", "View Info");
				      	 actionResolver.appStoreInfo();
				      	 
				     }
			         
			         if (moreRectangle.contains(touchPos.x, touchPos.y)) {
				      	 Gdx.app.log( LOG, "CLICKED SHRE FB");	
				      	actionResolver.trackEvent("Social", "View Catalog");
				      	 actionResolver.appStoreCatalog();
				      	 
				      	 
				     }
			      
			 }
		
		
	}
	
	
	private void renderPopUpChallengeScreen(){
		 //batch.draw(bckgndMenuTextureRegion, 0, 0, screen_width, screen_height);
		 
		 batch.draw(popupTextureRegion, popUpRectangle.x, popUpRectangle.y,popUpRectangle.width, popUpRectangle.height);
		 String playTitleMsg = "Challenge a Friend"; 
		 fontTimer.setColor(  1f, 1f, 1f, 1.0f );//notice we use transparent color here
		 fontTimer.draw(batch, playTitleMsg, (popUpRectangle.x+(popUpRectangle.width*0.5f))-(fontTimer.getBounds(playTitleMsg).width/2), (popUpRectangle.y+popUpRectangle.height) - (fontTimer.getBounds(playTitleMsg).height/2));
		 batch.draw(OkButtonTextureRegion, okButtonRectangle.x, okButtonRectangle.y, okButtonRectangle.width, okButtonRectangle.height);
		 batch.draw(CancelButtonTextureRegion, cancelButtonRectangle.x, cancelButtonRectangle.y, cancelButtonRectangle.width, cancelButtonRectangle.height);
		 
		 String playDescMsg1 = "Please select a friend from:";
		 String playDescMsg2 = "Facebook, Twitter, WhatsApp...";
		 
		 fontTimer.draw(batch, playDescMsg1, (popUpRectangle.x+(popUpRectangle.width*0.5f))-(fontTimer.getBounds(playDescMsg1).width/2), popUpRectangle.y+(popUpRectangle.height*0.6f));
		 fontTimer.draw(batch, playDescMsg2, (popUpRectangle.x+(popUpRectangle.width*0.5f))-(fontTimer.getBounds(playDescMsg2).width/2), (popUpRectangle.y+(popUpRectangle.height*0.6f))-(fontTimer.getBounds(playDescMsg2).height*1.3f)); 

		 showChallengeInvitation = false;///Dont show it anymore on this session(dont waste ads chances and also dont anoy user)
		 
		 
		 if(Gdx.input.justTouched()) {
			 Vector3 touchPos = new Vector3();
		       touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);//get touch position
		       camera.unproject(touchPos);//transform position to proper camera dimensions
		       
		       
		       if (okButtonRectangle.contains(touchPos.x, touchPos.y)) {		       
		    	   actionResolver.trackEvent("Social", "Challenge Friend");
		    	   actionResolver.shareContent("Seal Bucket Challenge", "I challenge you the Seal Bucket Challenge: https://play.google.com/store/apps/details?id=" + appPackageName, "With");
		    	   currentGameScreen = GAME_SCREEN_ENDMENU;
		       }
		       
		       if (cancelButtonRectangle.contains(touchPos.x, touchPos.y)) {		       
		    	   actionResolver.trackEvent("Social", "DONT Challenge Friend");
		    	   currentGameScreen = GAME_SCREEN_ENDMENU;
		       }

		 }
		 
		 
		 
		 
		 
	 }
	
	@Override
	public void render () {
		
		if(moveCounts==0 && manager.update()) {
			init();
			//currentGameScreen=GAME_SCREEN_LOA;
	    }
		
		/*if(moveCounts==0)
			Gdx.gl.glClearColor(0.6f, 0.58f, 0.52f, 0.9f);
		else
			Gdx.gl.glClearColor(1, 1, 1, 1);
		*/
		
		//Gdx.gl.glClearColor(0.981f, 0.991f, 1, 0.7f);//Gdx.gl.glClearColor(0.78f, 0.76f, 0.77f, 1f);
		if(loadingAlpha < 0.98) loadingAlpha += 0.025f;
		if(loadingAlpha > 0.98) loadingAlpha = 0.98f;
		Gdx.gl.glClearColor(loadingAlpha, loadingAlpha, loadingAlpha, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		
		
		currentDeltaTime += Gdx.graphics.getDeltaTime();
		batch.begin();				
		
		if(moveCounts<=1) {
			if(linealTouch.isEmpty()) //if(linealTouch.get(currentIndexTouchVector) == null)
				renderLoadingScreen();
			else if(mosquitoRectangle.y < (screen_height/2))
				renderLoadingScreen();
						
		}
		
		//Background
		//Gdx.app.log( LOG, "SEAL X " + sealRectangle.x + " .... " + bucketRectangle.x + " -- " + ( (float)(screen_width/2)-bucketRectangle.width));	  
		if(moveCounts >= 2   )	{		
			batch.draw(backgroundTexture, 0, 0, screen_width, screen_height);	
		}
		
		if (stopDJMusic == true) {
			Gdx.app.log( LOG, "MUSIC POS " + slowDJMusic.getPosition());
			//Stop it when we reach the end...we need to know this value manually by knowing the lengh of the audio track
			
			if (slowDJMusic.getPosition() < lastTimeDJMusic) { 		//if (slowDJMusic.getPosition() > 1.09f) {
				djEndSound.play();
				slowDJMusic.stop();				
				stopDJMusic = false;
				lastTimeDJMusic = -1;
			}else {
				lastTimeDJMusic = slowDJMusic.getPosition();
			}
		}
		
		if(currentGameScreen == GAME_SCREEN_PLAYING) renderPlayingScreen();
		if(currentGameScreen == GAME_SCREEN_ENDED) renderEndScreen();
		if(currentGameScreen == GAME_SCREEN_ENDMENU) renderEndMenuScreen();
		if(currentGameScreen == GAME_SCREEN_POPUP) renderPopUpChallengeScreen();
		
		//batch.draw(tempTexture, 0, 0, screen_width, screen_height);
		
		
		batch.end();
	}
	
	@Override
	public void resume() {		

	}
	
   @Override
   public void pause() {
	   
   }
   
   

	@Override
	public boolean keyDown(int keycode) {
		/*if(keycode == Keys.BACK && launchcount == 1 && showAppFeedback == false){
				//Do your optional back button handling (show pause menu?)
				showAppFeedback= true;
				actionResolver.showCloseFeedbackDialog();
				return true;
	     }else if(keycode == Keys.BACK) {
	    	 Gdx.app.exit();
	     }*/		
		if(keycode == Keys.BACK) Gdx.app.exit();
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}
}
