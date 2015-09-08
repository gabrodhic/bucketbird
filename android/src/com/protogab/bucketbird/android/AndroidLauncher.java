package com.protogab.bucketbird.android;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.RelativeLayout.LayoutParams;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.Timer;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.games.Games;
import com.google.example.games.basegameutils.GameHelper;
import com.google.example.games.basegameutils.GameHelper.GameHelperListener;
import com.protogab.bucketbird.ActionResolver;
import com.protogab.bucketbird.BucketBird;


public class AndroidLauncher extends AndroidApplication implements ActionResolver, GameHelperListener  {
	
	String developerPublisherId = "PROTOGAB";
    int backKeyCount = 0;
    I18NBundle langBundle;//Internationalization
    int optionSelected = -1;
    String comment = "";
    GameHelper gameHelper;
    Preferences preferences;
    
	
	protected View gameView;
	private InterstitialAd interstitialAd;
	private String AD_UNIT_ID_INTERSTITIAL; 
	
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	    // Do the stuff that initialize() would do for you
	    //https://github.com/TheInvader360/tutorial-libgdx-google-ads/blob/0a5ea376d4eb92b8e87c13a03245adb40b53e811/tutorial-libgdx-google-ads-android/src/com/theinvader360/tutorial/libgdx/google/ads/MainActivity.java
	    requestWindowFeature(Window.FEATURE_NO_TITLE);
	    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
	    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
		
	    RelativeLayout layout = new RelativeLayout(this);
	    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
	    layout.setLayoutParams(params);
	    
	    AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();		
	    
	    //Create GameView
	    View gameView;
	    gameView = initializeForView(new BucketBird(this), config);
	    RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
	    params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
	    params.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
	    params.addRule(RelativeLayout.BELOW, 12345); // this is an arbitrary id, allows for relative positioning in createGameView()
	    gameView.setLayoutParams(params2);
	    	    
	    layout.addView(gameView);
	    
	    setContentView(layout);
	    
	    
	    //Init Google Analytics
	     //Nothing needed here so far
		
		//Init Google Ads
	    AD_UNIT_ID_INTERSTITIAL = getResources().getString(R.string.interstitial_ad_id);
		interstitialAd = new InterstitialAd(this);
	    interstitialAd.setAdUnitId(AD_UNIT_ID_INTERSTITIAL);
	    interstitialAd.setAdListener(new AdListener() {
	      @Override
	      public void onAdLoaded() {
	        //Toast.makeText(getApplicationContext(), "Finished Loading Interstitial", Toast.LENGTH_SHORT).show();
	      }
	      @Override
	      public void onAdClosed() {
	        //Toast.makeText(getApplicationContext(), "Closed Interstitial", Toast.LENGTH_SHORT).show();
	      }
	    });
	    
	    
	    
	    //Init Google Game Services	    
	    preferences = Gdx.app.getPreferences(BucketBird.PREFS_NAME);//THIS MUST BE AFTER THE INITIALIZATION START
	    if (gameHelper == null) {
	        gameHelper = new GameHelper(this, GameHelper.CLIENT_GAMES);
	        gameHelper.enableDebugLog(true);
	     }
	     gameHelper.setConnectOnStart(false);///FORCED NOT AUTOCONNECTION
	     gameHelper.setup(this);
	   
		
	}
	
	
	@Override
	public void onStart(){
		super.onStart();
		gameHelper.onStart(this);
	}

	@Override
	public void onStop(){
		super.onStop();
		gameHelper.onStop();
	}

	@Override
	public void onActivityResult(int request, int response, Intent data) {
		super.onActivityResult(request, response, data);
		gameHelper.onActivityResult(request, response, data);
		
		
		
		
	}
	
	////////////////////////////////////////////////////BASE SYSTEM FUNCTIONS BEGIN////////////////////////////////////////////////
	
	  @Override
      public void setLangBundle( I18NBundle langBundle) {          
          this.langBundle = langBundle;
      }


      
       public void showShortToast(final CharSequence toastMessage) {
    	
    	   runOnUiThread(new Runnable() {
                       public void run() {
                               Toast.makeText(AndroidLauncher.this, toastMessage, Toast.LENGTH_SHORT)
                                               .show();
                       }
               });
       }


      
       public void showLongToast(final CharSequence toastMessage) {
    	   runOnUiThread(new Runnable() {
                       public void run() {
                               Toast.makeText(AndroidLauncher.this, toastMessage, Toast.LENGTH_LONG)
                                               .show();
                       }
               });
       }


      
       public void showAlertBoxNeutral(final String alertBoxTitle, final String alertBoxMessage, final String alertBoxButtonText) {
    	   	runOnUiThread(new Runnable() {
                       public void run() {
                               new AlertDialog.Builder(AndroidLauncher.this)
                                               .setTitle(alertBoxTitle)
                                               .setMessage(alertBoxMessage)
                                               .setNeutralButton(alertBoxButtonText,
                                                               new DialogInterface.OnClickListener() {
                                                                       public void onClick(DialogInterface dialog,
                                                                                       int whichButton) {
                                                                       }
                                                               }).create().show();
                       }
               });
       }
       
       public boolean showAlertBoxYesNo(final String alertBoxTitle, final String alertBoxMessage) {
   	   	runOnUiThread(new Runnable() {
                      public void run() {
                              new AlertDialog.Builder(AndroidLauncher.this)
                                              .setTitle(alertBoxTitle)
                                              .setMessage(alertBoxMessage)
                                              .setNeutralButton(android.R.string.ok,
                                                               new DialogInterface.OnClickListener() {
                                                                       public void onClick(DialogInterface dialog, int whichButton) {
                                                                       }
                                                               })
                                              .create().show();
                              
                      }
                      
              });
   	   		return true;
      }


      
       public void openUri(String uri) {

               if(uri.toLowerCase().contains("https://facebook.com")){
            	   try {
		   	            
		   	          String fburi = uri.replace("https:", "facebook:");
		   	          Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(fburi));
		   	          AndroidLauncher.this.startActivity(intent);
		   	         } catch (Exception e) {
		   	        	Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
		   	        	AndroidLauncher.this.startActivity(intent);
		   	         }
               }else{
            	   
                   Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                   AndroidLauncher.this.startActivity(intent);
               }
       }
       
       public boolean shareContent(String subject, String message, String chooserTitle) {

       	Intent intent = new Intent(Intent.ACTION_SEND);
           intent.setType("text/plain");
           intent.putExtra(Intent.EXTRA_SUBJECT, subject);
           intent.putExtra(Intent.EXTRA_TEXT, message);
           
           AndroidLauncher.this.startActivity(Intent.createChooser(intent, chooserTitle));
           return true;
       }

	
	public void showToast(CharSequence toastMessage, int toastDuration) {
		showShortToast(toastMessage);
		
	}



	@Override
	public void appStoreInfo() {
		
		try {
			AndroidLauncher.this.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + AndroidLauncher.this.getPackageName())));
		} catch (ActivityNotFoundException e) {
			String str ="https://play.google.com/store/apps/details?id=" + AndroidLauncher.this.getPackageName();
		 	Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(str));
		 	AndroidLauncher.this.startActivity(browserIntent);        	    	    	
		}
		
	}



	@Override
	public void appStoreCatalog() {
		try {
			AndroidLauncher.this.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://developer?id=" + developerPublisherId)));
		} catch (ActivityNotFoundException e) {
			String str ="https://play.google.com/store/apps/developer?id=" + developerPublisherId;
		 	Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(str));
		 	AndroidLauncher.this.startActivity(browserIntent);        	    	    	
		}
		
	}
	
	@Override
	 public void showCloseFeedbackDialog(){
	    	//Show the feedback close dialog only once
	    	  	
		//FileHandle baseFileHandle = Gdx.files.internal("i18n/langBundle");
		//Locale locale = new Locale(Locale.getDefault().getLanguage());
		//langBundle = I18NBundle.createBundle(baseFileHandle, locale);
	    		  
	    		  
		//Activity activity = (Activity) appContext; 
		runOnUiThread(new Runnable() {
			  public void run() {
		
				CharSequence[] options = new CharSequence[2];
				options[0] = langBundle.get("option_bad_app");
				options[1] = langBundle.get("option_good_app");
	    	      AlertDialog.Builder feedbackDialog = new AlertDialog.Builder(AndroidLauncher.this);
	    	      feedbackDialog.setTitle(langBundle.get("close_app_feedback")).setItems(options, 
	    	    		  new DialogInterface.OnClickListener() {
	    		          public void onClick(DialogInterface dialog, int which) {
	    		        	  //final String[] mTestArray = options;    		        	  
	    		        	  optionSelected = which;
	    		        	  //Log.d(MainActivity.DBG_TAG, "CALL CLICK ON:");
	    		        	  //If there might be a explination add a comment
	    		        	  if(which==1){
	    		        		  showCloseFeedbackDialogRateUs();
	    		        		  
	    		        	  }else if(which==0){
	    		        		  showCloseFeedbackDialogComment();    		        		  
	    		        	  }else{
	    		        		  sendFeedback();
	    		        	  }
	    		        	  
	    		          }
	    	          });
	    	      
	    	      /*feedbackDialog.setOnDismissListener(
	    	    		  new DialogInterface.OnDismissListener(){
	    	    			  public void onDismiss(DialogInterface dialogInterface) {
	    	    				  if(optionSelected == -1) sendFeedback();//If the user just close de dialog without any option selected then send it
	    	    		  	  }
	    	    		  });*/
	    	      //feedbackDialog.setCancelable(false);
	    	      
	    	      feedbackDialog.setOnKeyListener(new Dialog.OnKeyListener() {
	    	            @Override
	    	            public boolean onKey(DialogInterface arg0, int keyCode, KeyEvent event) {
	    	                // TODO Auto-generated method stub
	    	                if (keyCode == KeyEvent.KEYCODE_BACK) {
	    	                	backKeyCount++;
	    	                	//Prevent closing this activity until at least 2 attempts
	    	                	if(backKeyCount >3 ) {
	    	                		sendFeedback();
	    	                		return false;
	    	                	}else{
	    	                		return true;
	    	                	}
	    	                    //finish();
	    	                    //dialog.dismiss();
	    	                }
	    	                return true;
	    	            }
	    	        });
	    	      feedbackDialog.show();
			  }
		});
	  }

	private void showCloseFeedbackDialogRateUs(){
		
		//Activity activity = (Activity) appContext; 
		runOnUiThread(new Runnable() {
			  public void run() {
    	
    	AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AndroidLauncher.this);
    	 
		// set title
		alertDialogBuilder.setTitle(langBundle.get("close_app_rateus"));

		// set dialog message
		alertDialogBuilder
			.setMessage(langBundle.get("close_app_rateus_desc") + "\n")
			.setCancelable(false)
			.setPositiveButton(langBundle.get("ok"),new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {					
					sendFeedback();
					dialog.dismiss();
					appStoreInfo();           	
	            	
				}
			  })							  
			.setNegativeButton(langBundle.get("cancel"),new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					     
					sendFeedback();
					dialog.cancel();
				}
			});

			// create alert dialog
			AlertDialog alertDialog = alertDialogBuilder.create();

			// show it
			alertDialog.show();
		}
		});
    }

	 public void showCloseFeedbackDialogComment(){
	    	
			//Activity activity = (Activity) appContext; 
			runOnUiThread(new Runnable() {
				  public void run() {
					//Log.d(MainActivity.DBG_TAG, "CALL COMMENT:");
				    	 final AlertDialog.Builder alert = new AlertDialog.Builder(AndroidLauncher.this);
						    final EditText input = new EditText(AndroidLauncher.this);			    
						    alert.setTitle(langBundle.get("close_app_comment"));
						    
						    alert.setView(input);
						    
						    alert.setPositiveButton(langBundle.get("close_app_send"), new DialogInterface.OnClickListener() {
						        public void onClick(DialogInterface dialog, int whichButton) {
						            comment = input.getText().toString().trim();
						            
						            //final  android.widget.TextView thanksTxt = new TextView(appContext);
						            //thanksTxt.setText("Closing, Please wait...");
						            //alert.setView(thanksTxt);
						            
						            sendFeedback( );
						        }
						    });
						    alert.show();
						    //input.setFocusableInTouchMode(true);
						    input.requestFocus();
						    final InputMethodManager imm = (InputMethodManager) AndroidLauncher.this.getSystemService(Context.INPUT_METHOD_SERVICE);
						    //imm.showSoftInput(input, InputMethodManager.SHOW_FORCED);
						 	Timer.schedule(new Timer.Task(){
						 	    @Override
						 	    public void run() {
						 	    	imm.toggleSoftInput(0, 0);
						 	    }
						 	}, 1f);
						    
						    
					  }
					});
	    	
			  
	    }
	  
	  
	  private void sendFeedback(){
	    		    	
	    	 System.out.println("CALL SEND FEEDBACK:");
	    	 Toast.makeText(AndroidLauncher.this, langBundle.get("thanks_closing") , Toast.LENGTH_SHORT).show();
	    	 //final String comment = mycomment;
	    	Thread t = new Thread(new Runnable() { public void run() {
	  		  try{
	  			  
	  			  String url = "http://protogab.com"+"/appfeedback";
	  			  HttpPost httpPost = new HttpPost(url);
					  List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
					  
					  nameValuePairs.add(new BasicNameValuePair("value", String.valueOf(optionSelected)));
					  nameValuePairs.add(new BasicNameValuePair("name", AndroidLauncher.this.getPackageName()));
					  nameValuePairs.add(new BasicNameValuePair("model", Build.MODEL));
					  nameValuePairs.add(new BasicNameValuePair("comment", comment));
				      httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				      
					  HttpParams httpParameters = new BasicHttpParams();
					  // Set the timeout in milliseconds until a connection is established.
					  // The default value is zero, that means the timeout is not used. 
					  int timeoutConnection = 3000;
					  HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
					  // Set the default socket timeout (SO_TIMEOUT) 
					  // in milliseconds which is the timeout for waiting for data.
					  int timeoutSocket = 100;//No need to wait
					  HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
					  
					  
					  
					  //Set retry handler to not retry (wether there is a timeout, error, or whatever)
					  //(By default httpclient retries if the connection timed out)
					  HttpRequestRetryHandler myRetryHandler = new HttpRequestRetryHandler() {
							@Override
							public boolean retryRequest(IOException exception,int executionCount, HttpContext context) {
								
								return false; ///Returning false makes it to NOT retry anymore
							}

				        };
					  
					  

					  DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);
					  httpClient.setHttpRequestRetryHandler(myRetryHandler);//No need to wait
					  
					  //Log.d(MainActivity.DBG_TAG, "SEND BEFORE POST:");
					  HttpResponse response = httpClient.execute(httpPost);
					  //Log.d(MainActivity.DBG_TAG, "SEND AFTER POST:");

		   		  }catch(Exception ise){
		   			     
		   			  	/*
		   			  //TODO:If user closed it by mistake lets reopen it for him
			   			if(optionSelected == 8){
			   				PendingIntent intent = PendingIntent.getActivity(CloseFeedbackActivity.this.getBaseContext(), 0, new Intent(getIntent()), getIntent().getFlags());
						    AlarmManager manager = (AlarmManager) CloseFeedbackActivity.this.getSystemService(Context.ALARM_SERVICE);
						    manager.set(AlarmManager.RTC, System.currentTimeMillis() + 1000, intent);					    
			   			}*/
		   			  	System.exit(0); //Kills all threads also
		   			     //return;
		   		  }
		   		  
	  		  //Toast.makeText(MainActivity.this, getString(R.string.thanks_bye), Toast.LENGTH_SHORT).show();	
	  		/*
				  //TODO:If user closed it by mistake lets reopen it for him
	  		  if(optionSelected == 8){
	 				PendingIntent intent = PendingIntent.getActivity(CloseFeedbackActivity.this.getBaseContext(), 0, new Intent(getIntent()), getIntent().getFlags());
				    AlarmManager manager = (AlarmManager) CloseFeedbackActivity.this.getSystemService(Context.ALARM_SERVICE);
				    manager.set(AlarmManager.RTC, System.currentTimeMillis() + 1000, intent);					    
	  		  }
	  		  */
	      	  System.exit(0); //Kills all threads also
	  		  
		   	  }});
		   	  t.start();
	    }
	
	
	////////////////////////////////////////////////////BASE SYSTEM FUNCTIONS END////////////////////////////////////////////////
	
	  
	  
	//////////////////////////////////////////////////GOOGLE ANALYTICS BEGIN////////////////////////////////////////////////
	  
	  @Override
		public void initialize() {
			 //Google Analytics. Get tracker.
			
			GoogleAnalytics analytics = GoogleAnalytics.getInstance(this.getContext());
			Tracker t = analytics.newTracker(R.xml.app_tracker);
			
		    //Tracker splineTime = ((MyApplication) this.getApplication()).getTracker(TrackerName.APP_TRACKER);
		    
		    //http://stackoverflow.com/questions/22611295/android-google-analytics-availability-in-google-play-services
		    //THIS IS THE REAL: http://developer.android.com/reference/com/google/android/gms/analytics/GoogleAnalytics.html
		    //It reads on AndroidManifest.xml:  <meta-data android:name="com.google.android.gms.analytics.globalConfigResource" android:resource="@xml/app_tracker" /> 
		    
		    //OLD EasyTracker.getInstance(this).activityStart(this);  // Google Analytics
			Activity a = (Activity) this.getContext();
		    GoogleAnalytics.getInstance(this.getContext()).reportActivityStart(a); 
		    
		    //GoogleAnalytics.getInstance(this).getLogger().setLogLevel(LogLevel.VERBOSE);//**************************
		    //GoogleAnalytics.getInstance(this).setDispatchPeriod(5); //GoogleAnalytics.getInstance(this).setLocalDispatchPeriod(5);
		    //GoogleAnalytics.getInstance(this).setLocalDispatchPeriod(5);
		    
		    // Set screen name.
		    // Where splinePath is a String representing the screen name.
		    t.setScreenName(null);
		    // Send a screen view. (And session)
		    //https://developers.google.com/analytics/devguides/collection/android/v4/sessions
		    //http://stackoverflow.com/questions/19254023/short-session-lengths-in-google-analytics-for-android
		    //splineTime.send(new HitBuilders.AppViewBuilder().setNewSession().build());
		    t.send(new HitBuilders.AppViewBuilder().build());
			
		}


		@Override
		public void trackPageView(String path) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void trackEvent(String category, String subCategory) {
			//Report Event to Google Analytics
			GoogleAnalytics analytics = GoogleAnalytics.getInstance(this.getContext());
			Tracker t = analytics.newTracker(R.xml.app_tracker);
		  	  	
			t.send(new HitBuilders.EventBuilder()
		        .setCategory(category)
		        .setAction(subCategory)	        
		        .build());
			
		}
		
		@Override
		public void trackEvent(String category, String subCategory, String label, int value) {
			//Report Event to Google Analytics
			GoogleAnalytics analytics = GoogleAnalytics.getInstance(this.getContext());
			Tracker t = analytics.newTracker(R.xml.app_tracker);
		  	  	
			t.send(new HitBuilders.EventBuilder()
		        .setCategory(category)
		        .setAction(subCategory)
		        .setLabel(label)
		        .setValue(value)
		        .build());
			
		}

		
		@Override
		public void stop() {
			
			Activity a = (Activity) this.getContext();
			GoogleAnalytics.getInstance(a).reportActivityStop(a);
			
		}
	//////////////////////////////////////////////////GOOGLE ANALYTICS END////////////////////////////////////////////////
	
	//////////////////////////////////////////////////GOOGLE ADS SERVICES BEGIN////////////////////////////////////////////////
	  
	@Override
	public void showAds(boolean show) {
		
		      runOnUiThread(new Runnable() {
		        public void run() {
		new AlertDialog.Builder(AndroidLauncher.this)
        .setTitle("test")
        .setMessage("testing")
        .setNeutralButton("okkk",
                        new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                }
                        }).create().show();
		        }
		      });
		    
	}

	@Override
	public void displayInterstitial() {
		// TODO Auto-generated method stub
		
	}

	@Override
	  public void showOrLoadInterstital() {
	    try {
	      runOnUiThread(new Runnable() {
	        public void run() {
	        	//Toast.makeText(getApplicationContext(), "Interstitial Invoked", Toast.LENGTH_SHORT).show();
	        	
	          if (interstitialAd.isLoaded()) {
	            interstitialAd.show();
	            //Toast.makeText(getApplicationContext(), "Showing Interstitial", Toast.LENGTH_SHORT).show();
	          }
	          else {
	            AdRequest interstitialRequest = new AdRequest.Builder().build();
	            interstitialAd.loadAd(interstitialRequest);
	            //Toast.makeText(getApplicationContext(), "Loading Interstitial", Toast.LENGTH_SHORT).show();
	          }
	        }
	      });
	    } catch (Exception e) {
	    }
	  }
	
	
	//////////////////////////////////////////////////GOOGLE ADS SERVICES END////////////////////////////////////////////////	
	
	
	
	////////////////////////////////////////////////////GOOGLE GAME SERVICES BEGIN////////////////////////////////////////////////
	//http://theinvader360.blogspot.co.uk/2013/10/google-play-game-services-tutorial-example.html
	@Override
	public boolean getSignedInGPGS() {
		return gameHelper.isSignedIn();
	}
	
	@Override
	public void loginGPGS() {
		try {
			runOnUiThread(new Runnable(){
				public void run() {
					gameHelper.beginUserInitiatedSignIn();
				}
			});
		} catch (final Exception ex) {
		}
	
	}
	
	@Override
	public void submitScoreGPGS(String leaderboardid, int score) {
		Games.Leaderboards.submitScore(gameHelper.getApiClient(), leaderboardid, score);
	
	}
	
	@Override
	public void unlockAchievementGPGS(String achievementId) {
		Games.Achievements.unlock(gameHelper.getApiClient(), achievementId);
	
	}
	
	@Override
	public void getLeaderboardGPGS(String leaderboardid) {
		if (gameHelper.isSignedIn()) {
		    startActivityForResult(Games.Leaderboards.getLeaderboardIntent(gameHelper.getApiClient(), leaderboardid), 100);
		  }
		  else if (!gameHelper.isConnecting()) {
		    loginGPGS();
		  }
	
	}
	
	@Override
	public void getAchievementsGPGS() {
		if (gameHelper.isSignedIn()) {
		    startActivityForResult(Games.Achievements.getAchievementsIntent(gameHelper.getApiClient()), 101);
		  }
		  else if (!gameHelper.isConnecting()) {
		    loginGPGS();
		  }
	
	}
	
	@Override
	public void onSignInFailed() {
	// TODO Auto-generated method stub
	
	}
	
	@Override
	public void onSignInSucceeded() {
				//Record that the user uses GPGS and also publish its first score after login
				if(preferences.getBoolean(BucketBird.PREF_GAMESERVICES_USED) == false) {
					
										
						//Publish first user score
						submitScoreGPGS( BucketBird.LEADERBOARD_ID, preferences.getInteger(BucketBird.PREF_CURRENT_SCORE, 0));
						
						//Open the leaderboard again(Google Game Services does not opens it af first login(register))
						getLeaderboardGPGS(BucketBird.LEADERBOARD_ID);
						
						preferences.putBoolean( BucketBird.PREF_GAMESERVICES_USED, true);
						preferences.flush();
						
					
				}
	
	}
	
	////////////////////////////////////////////////////GOOGLE GAME SERVICES END////////////////////////////////////////////////
	
		
}
