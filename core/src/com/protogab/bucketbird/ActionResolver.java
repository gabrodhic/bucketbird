package com.protogab.bucketbird;

import com.badlogic.gdx.utils.I18NBundle;

public interface ActionResolver {
	
	//Base functions
	public void setLangBundle(I18NBundle langBundle);
    public void showToast(CharSequence toastMessage, int toastDuration);
    public void showAlertBoxNeutral(String alertBoxTitle, String alertBoxMessage, String alertBoxButtonText);
    public boolean showAlertBoxYesNo(String alertBoxTitle, String alertBoxMessage);
    public void openUri(String uri);
    public void appStoreInfo();
    public void appStoreCatalog();
    public boolean shareContent(String subject, String message, String chooserTitle);//Return is used to know in case of desktop version if there was a call or not
    public void showCloseFeedbackDialog();
    
    //Google Analytics
    void initialize();    
    void trackPageView(String path);
    void trackEvent(String category, String subCategory);
    void trackEvent(String category, String subCategory, String label, int value);
    void stop();
    
    //Google Ads
    void showAds(boolean show);
	void displayInterstitial();
	void showOrLoadInterstital();
    
    //Google Game Services
    public boolean getSignedInGPGS();
    public void loginGPGS();
    public void submitScoreGPGS(String leaderboard, int score);
    public void unlockAchievementGPGS(String achievementId);
    public void getLeaderboardGPGS(String leaderboard);
    public void getAchievementsGPGS();
}

