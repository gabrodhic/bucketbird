package com.protogab.bucketbird.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.utils.I18NBundle;
import com.protogab.bucketbird.ActionResolver;
import com.protogab.bucketbird.BucketBird;


public class DesktopLauncher implements ActionResolver {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 384;//384;//keeps same proportion to 384x640, 480x800, 720x1280
		config.height = 640;//640;//keeps same proportion to 480x800
		
		ActionResolver ar = new DesktopLauncher();///NOTICE HERE THE implicit casting by heritance implementation of interface
		//GoogleServicesAnalyticsDesktop gsa = new GoogleServicesAnalyticsDesktop();
		//GoogleServicesAdsDesktop gsad = new GoogleServicesAdsDesktop();
		
		new LwjglApplication(new BucketBird(ar), config);
		//new LwjglApplication(new BucketBird(), config);
	}

	@Override
	public void setLangBundle(I18NBundle langBundle) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void showToast(CharSequence toastMessage, int toastDuration) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void showAlertBoxNeutral(String alertBoxTitle, String alertBoxMessage,
			String alertBoxButtonText) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public boolean showAlertBoxYesNo(String alertBoxTitle,
			String alertBoxMessage) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void openUri(String uri) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void appStoreInfo() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void appStoreCatalog() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean shareContent(String subject, String message,
			String chooserTitle) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void showCloseFeedbackDialog() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initialize() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void trackPageView(String path) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void trackEvent(String category, String subCategory) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void trackEvent(String category, String subCategory, String label,
			int value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void showAds(boolean show) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void displayInterstitial() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void showOrLoadInterstital() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean getSignedInGPGS() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void loginGPGS() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void submitScoreGPGS(String leaderboardid, int score) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void unlockAchievementGPGS(String achievementId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getLeaderboardGPGS(String leaderboardid) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getAchievementsGPGS() {
		// TODO Auto-generated method stub
		
	}


}
