# QRScannerandGenerator
This App Can Generate QRCode and Scan QRCode from Images or Camera 


-Manifest :


<uses-permission android:name="android.permission.CAMERA" />


<uses-feature android:name="android.hardware.camera" />


<uses-feature android:name="android.hardware.camera.autofocus"/>




build.gradle(Module) :


• View Binding

   android {
   buildFeatures {
    viewBinding true
     }}


• Navigation Component


` implementation 'androidx.navigation:navigation-fragment:2.4.1'`
 
 
` implementation 'androidx.navigation:navigation-ui:2.4.1'`

     
 
 -dependencies:
 
 
• Sdp


    `implementation 'com.intuit.sdp:sdp-android:1.0.6'`



• CardView


    `implementation "androidx.cardview:cardview:1.0.0"`



• QR Generate


    `implementation 'com.journeyapps:zxing-android-embedded:3.4.0'`



• QR Scanner


    `implementation 'com.budiyev.android:code-scanner:2.1.0'`



• QR Image Scanner


    `implementation 'me.dm7.barcodescanner:zxing:1.9'`
    
    
    `implementation 'androidmads.library.qrgenearator:QRGenearator:1.0.3'`




• Gson to add arraylist to SharedPreferences


   ` implementation 'com.google.code.gson:gson:2.8.8'`



Download APk :


https://www.mediafire.com/file/gsl4jzn4911zjjs/QRScannerandGenerator.apk/file




https://user-images.githubusercontent.com/99625111/153978173-c9a54050-34a9-4160-9759-f56a0a6d663b.mp4

