#!/usr/bin/env python3
"""
Script to update all remaining screens with smooth navigation
"""

import os
import re

# List of remaining screens to update
remaining_screens = [
    "selectImageScreen.java",
    "showLiveScreen.java", 
    "nearbyScreen.java",
    "ViewProfile.java",
    "lockscreen.java",
    "lockScreen2.java",
    "lockScreen3.java",
    "contact_us.java",
    "privacy_policy.java",
    "policy.java",
    "deleteMyAccount.java",
    "changeNumber.java",
    "Acount.java",
    "sizeActivity.java",
    "payActivity.java",
    "paymentActivity.java",
    "sponsred.java",
    "notinew.java",
    "notificationActivity.java",
    "testSctreen.java",
    "test3.java",
    "companyProfile.java",
    "block_contact_activity.java",
    "forgetScreenOtp.java",
    "otpverifyScreen.java",
    "otpverifyScreenDelete.java",
    "whatsTheCode.java",
    "whatsYourNumber.java",
    "selectLanguage.java",
    "voiceGroupCallList.java",
    "videoGroupCallList.java",
    "forGroupVisible.java",
    "shareExternalDataScreen.java",
    "shareExternalDataCONTACTScreen.java",
    "dummyChattingScreen.java",
    "SplashScreen2.java",
    "SplashScreenMy.java",
    "NativeNotification.java"
]

def update_screen_file(file_path):
    """Update a single screen file with smooth navigation"""
    if not os.path.exists(file_path):
        print(f"File not found: {file_path}")
        return False
    
    try:
        with open(file_path, 'r', encoding='utf-8') as f:
            content = f.read()
        
        # Add import if not already present
        if "SmoothNavigationHelper" not in content:
            content = content.replace(
                "import androidx.appcompat.app.AppCompatActivity;",
                "import androidx.appcompat.app.AppCompatActivity;\nimport com.Appzia.enclosure.Utils.SmoothNavigationHelper;"
            )
        
        # Update onBackPressed methods
        content = re.sub(
            r'super\.onBackPressed\(\);',
            lambda m: f'SmoothNavigationHelper.finishWithSlideToRight({get_class_name(file_path)}.this);',
            content
        )
        
        # Update startActivity calls (basic pattern)
        content = re.sub(
            r'(\s+)startActivity\(([^)]+)\);',
            lambda m: f'{m.group(1)}SmoothNavigationHelper.startActivityWithSlideFromRight({get_class_name(file_path)}.this, {m.group(2)});',
            content
        )
        
        with open(file_path, 'w', encoding='utf-8') as f:
            f.write(content)
        
        print(f"Updated: {file_path}")
        return True
        
    except Exception as e:
        print(f"Error updating {file_path}: {e}")
        return False

def get_class_name(file_path):
    """Extract class name from file path"""
    filename = os.path.basename(file_path)
    return filename.replace('.java', '')

def main():
    """Main function to update all remaining screens"""
    screens_dir = "app/src/main/java/com/Appzia/enclosure/Screens/"
    
    updated_count = 0
    total_count = len(remaining_screens)
    
    print(f"Updating {total_count} remaining screens with smooth navigation...")
    
    for screen in remaining_screens:
        file_path = os.path.join(screens_dir, screen)
        if update_screen_file(file_path):
            updated_count += 1
    
    print(f"\nCompleted! Updated {updated_count}/{total_count} screens.")
    print("Please review the changes and test the build.")

if __name__ == "__main__":
    main()
