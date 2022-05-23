package com.app.fixirman.utils;

import com.app.fixirman.BuildConfig;
public class AppConstants {
    public static final String APP_VERSION = BuildConfig.VERSION_NAME;
    //public static String HOST_URL = "http://www.fixirman.com/fixirman/"; //live link
    public static String HOST_URL = "http://10.10.10.155/fixirman/"; //testing link
    //public static String HOST_URL = "https://mcquez-test.000webhostapp.com/fixirman/"; //test link

    public static final String CATEGORY_ID = "categoryId";
    public static final String SUB_CATEGORY_ID = "subCategoryId";
    public static final String CATEGORY_NAME = "categoryName";
    public static final String SUB_CATEGORY_NAME = "subCategoryName";

    public static final int PHONE_ACTIVITY_FORGOT_PASSWORD = 1;
    public static final int PHONE_ACTIVITY_SIGN_UP = 2;
    public static final int PHONE_ACTIVITY_CHECK = 3;
    public static final int PHONE_ACTIVITY_ADD_NEW_NUMBER = 4;

    public static final int UPDATE_PROFILE = 1;
    public static final int COMPLETE_PROFILE = 2;

    public static final String TYPE_USER = "user";

    public static final String HOME_ADDRESS = "home";
    public static final String OFFICE_ADDRESS = "office";
    public static final String OTHER_ADDRESS = "other";

    //user account status
    public static final String USER_ACTIVE = "active";
    public static final String USER_INACTIVE = "inactive";
    public static final String USER_BLOCKED = "blocked";
    public static final String USER_SUSPENDED = "suspend";



    public static final int PENDING = 1;
    public static final int ACCEPT = 2;
    public static final int IN_PROCESS = 3;
    public static final int FINISHED = 4;
    public static final int REJECT = 5;
    public static final int CANCELLED = 6;
    public static final int FAILED = 7;
    public static final String PENDING_STRING = "Pending";
    public static final String ACCEPT_STRING = "Accepted";
    public static final String IN_PROCESS_STRING = "In process";
    public static final String FINISHED_STRING = "Finished";
    public static final String REJECT_STRING = "Rejected";
    public static final String CANCELLED_STRING = "Cancelled";
    public static final String FAILED_STRING = "Failed";

    //notification types
    public static final String NOTIFICATION_APPOINTMENT = "job" ;


    public static final int REQUEST_PERMISSION_TYPE_FRAGMENT = 2;
    public static final int REQUEST_PERMISSION_TYPE_ACTIVITY = 1;

    public static String termsAndUse="<h1 style=\"font-size:150%;\"> PRIVACY POLICY AND TERMS OF USE (EULA)</font></h1>\n" +
            "<p style=\"font-size:110%;\">By using this app you agree that you are a registered member of the Fixirman Community. Parts of the registration information that was provided by you is visible to attendees, Event organizers and/or partners of the Event. It may be used to help us improve the Event, understand which sessions are most attended, and statistical information provided to Fixirman App may be used to help us evaluate the Event, the interest it generates and the attendees.</p>\n" +
            "<h2 style=\"font-size:110%;\">Fixirman App Privacy Policy</h2>\n" +
            "<p style=\"font-size:110%;\">The Fixirman App is provided by the Fixirman Community and is designed to facilitate communication among Community members for personal and Community-related purposes of Fixirman App. To safeguard the operation of Fixirman, Fixirman Community has adopted the following guidelines and policies.</p>\n" +
            "<h3 style=\"font-size:110%;\">Guidelines & Policies for proper use</h3>\n" +
            "<p style=\"font-size:110%;\">Information available on the Fixirman App is intended for the private use of Fixirman Community and for Community-related purposes. Users of the Fixirman App must abide by the following rules:</p>\n" +
            "<ol type=\"A\">\n" +
            "\n" +
            "<li style=\"font-size:110%;\"> Use of communications available through the Fixirman App for any commercial, public, or political purposes is strictly prohibited. Prohibited activities include, but are not limited to, solicitations for commercial services and mass mailings for commercial purposes.</li><br />\n" +
            "\n" +
            "<li style=\"font-size:110%;\"> Members may download or copy any downloadable materials displayed on the Fixirman App for home, non-commercial, and personal use only and must maintain all copyright, trademark, and other notices contained in such material and they agree to abide by all additional copyright notices or restrictions contained in any material accessed through theFixirman App.</li><br />\n" +
            "\n" +
            "<li style=\"font-size:110%;\"> Members shall not restrict or inhibit any other user from enjoying any service offered through the Fixirman App, and shall not post obscene materials or use abusive, defamatory, profane, or threatening language of any kind. Additionally, users shall not upload, transmit, distribute, or otherwise publish any materials containing a virus or any other harmful components.</li><br />\n" +
            "\n" +
            "<li style=\"font-size:110%;\"> All aliases adopted by Fixirman App members are subject to approval by the Viion Technology.</li>\n" +
            "</ol>\n" +
            "<p style=\"font-size:110%;\">Fixirman Community is not responsible for the content of the information available on, and have no obligation to monitor the use of, the Fixirman App. Fixirman Community  makes no representations relating to member use of the Fixirman App or concerning the accuracy, completeness, or timeliness of any information found on the Fixirman App. Fixirman Community is not responsible for screening communications and will not actively monitor the use of the Fixirman App. For this reason, it is essential that the Community users of the Fixirman App  App report any abuse or misuse of the Fixirman App by emailing us immediately. Participation in the Fixirman App is a privilege. Fixirman App reserve the right to suspend or terminate the accounts of any individuals who misuse the information contained in the Fixirman App or otherwise violate this user agreement.</p>\n" +
            "<h3 style=\"font-size:110%;\">ELIGIBILITY</h3>\n" +
            "<p style=\"font-size:110%;\">If you live in the Fixirman App area, you are automatically a member of Fixirman Community, along with nearly 10000+ of your fellow community members. All community members are eligible to register for Fixirman App membership. Use of the Fixirman App indicates that you accept the Terms herein and agree to abide by them.</p>\n" +
            "<h3 style=\"font-size:110%;\">PRIVACY</h3>\n" +
            "<p style=\"font-size:110%;\">Your privacy is a top priority to the Fixirman Community. Fixirman Community does not share, sell or trade community member mailing lists (including community member email addresses) with outside corporations or organizations, except for those that have business or contractual relationships with the community. Any third parties who receive Community member information are prohibited from using or sharing community member information for any purpose other than offering or providing approved services to the community. Fixirman Community takes reasonable precautions to assure overall system security by monitoring security issues and industry trends, to protect the privacy of Fixirman Community members and secure the personal information available through Fixirman Community. All areas of\n" +
            "Fixirman Community that contain private information are housed on a secure server.</p>\n" +
            "<p style=\"font-size:110%;\">The Fixirman App is also password-protected to allow access only to registered Fixirman Community members. Although these precautions should help to protect any personal information available through Fixirman App from abuse or outside interference, a certain degree of privacy risk is faced any time information is shared over the Internet or through a downloaded app. Therefore, Fixirman Community members have the ability to selectively hide the personal information that is listed in the Fixirman App to be viewed by other Fixirman Community members.</p>\n" +
            "<p style=\"font-size:110%;\">This is easily accomplished by clicking on the Privacy Preferences link on your My Profile page.Fixirman Community invites you to explore this page, review your record, and select any and all privacy settings you wish to apply.</p>\n" +
            "<p style=\"font-size:110%;\">The information we maintain about the user in Fixirman App is available only under the following restricted circumstances: To Fixirman Community members who have registered with Fixirman App (App).</p>\n" +
            "<h3 style=\"font-size:110%;\">Data Policy</h3>\n" +
            "\n" +
            "<p style=\"font-size:110%;\">What kinds of information do we collect? To provide you with the best Fixirman App experience we must process information about you:</p>\n" +
            "   <ul type=\"disc\">\n" +
            "\n" +
            "        <li style=\"font-size:110%;\">Email address</li><br />\n" +
            "\n" +
            "        <li style=\"font-size:110%;\">First name and last name</li><br />\n" +
            "\n" +
            "        <li style=\"font-size:110%;\">Phone number</li><br />\n" +
            "\n" +
            "        <li style=\"font-size:110%;\">Address, State, Province, ZIP/Postal code, City</li><br />\n" +
            "\n" +
            "        <li style=\"font-size:110%;\">Cookies and Usage Data</li><br />\n" +
            "\n" +
            "        <li style=\"font-size:110%;\">Usage Data</li><br />\n" +
            "\n" +
            "       <li style=\"font-size:110%;\"> We may also collect information that your browser sends whenever you visit our App or when you access the App by or through a mobile device</li>\n" +
            "       </ul>\n" +
            "      <h3 style=\"font-size:110%;\"> Things That You and Others Do and Provide </h3>\n" +
            "\n" +
            "<p style=\"font-size:110%;\">Information and content, you provide. We collect the content, communications and other information you provide when you use our Fixirman App, including when you sign up for an account, create or share content and message or communicate with others. This can include information on or about the content that you provide (e.g. metadata), such as the location of a photo or the date the file was created. Our systems automatically process content and communications that you and others provide to analyze context and what's in them for the purposes described below.</p>\n" +
            "<ul type=\"disc\">\n" +
            "      <li style=\"font-size:110%;\"> Networks and connections. We collect information about the people, accounts, you are connected to and how you interact with them across our Products, such as people you communicate with the most or groups that you are part of.</li><br />\n" +
            "\n" +
            "      <li style=\"font-size:110%;\">  Your usage. We collect information about how you use our App, such as the types of content that you view or engage with, the features you use, the actions you take, the people or accounts you interact with and the time, frequency and duration of your activities. We also collect information about how you use features such as our camera.</li><br />\n" +
            "\n" +
            "<li style=\"font-size:110%;\">     Things others do and information that they provide about you. We also receive and analyze content, communications and information that other people provide when they use our Fixirman App.</li>\n" +
            "</ul>\n" +
            "\n" +
            "<p style=\"font-size:110%;\">Fixirman Community App uses the collected data for various purposes:</p>\n" +
            "<ul type=\"disc\">\n" +
            "<li style=\"font-size:110%;\">  To provide and maintain the Service.</li><br />\n" +
            "\n" +
            "<li style=\"font-size:110%;\">     To notify you about changes to our service.</li><br />\n" +
            "\n" +
            "<li style=\"font-size:110%;\">    To allow you to participate in interactive features of our Service when you choose to do so.</li><br />\n" +
            "\n" +
            "<li style=\"font-size:110%;\">       To provide customer care and support.</li><br />\n" +
            "\n" +
            "<li style=\"font-size:110%;\">       To provide analysis or valuable information so that we can improve the service.</li><br />\n" +
            "\n" +
            "<li style=\"font-size:110%;\">        To monitor the usage of the Service.</li><br />\n" +
            "\n" +
            "<li style=\"font-size:110%;\">       To detect, prevent and address technical issues.</li>\n" +
            "</ul>\n" +
            "\n" +
            "<p style=\"font-size:110%;\">Device information as described below, we collect information collected through Fixirman App. Information that we obtain from these devices includes:</p>\n" +
            "<ul type=\"disc\">\n" +
            "<li style=\"font-size:110%;\">        Data from device settings</li><br />\n" +
            "\n" +
            "<li style=\"font-size:110%;\">        Information that you allow us to receive through device settings that you turn on, such as access to your GPS location, camera or photos.</li><br />\n" +
            "\n" +
            "<li style=\"font-size:110%;\">         Network and connections: information such as the mobile phone number, information about other devices that are nearby or on your network, so we can do things such as help you. Cookie data: data from cookies stored on your device, including cookie IDs and settings.</li>\n" +
            "</ul>\n" +
            "<h3 style=\"font-size:110%;\"> How do we use this information?</h3>\n" +
            "\n" +
            "<p style=\"font-size:110%;\">We use the information that we have (subject to choices you make) as described below, and to provide and support Fixirman App and related services described in the Fixirman App terms and conditions.</p>\n" +
            "\n" +
            "<h3 style=\"font-size:110%;\">Here's how:</h3>\n" +
            "<ul type=\"disc\">\n" +
            "\n" +
            "    <li style=\"font-size:110%;\">    Provide, Personalize and Improve Our Services. We use the information we have to deliver our Products, and make suggestions for you (such as groups or events you may be interested in or topics you may want to follow) on and off our Products. To create personalized services that are unique and relevant to you, we use your connections, preferences, interests and activities based on the data that we collect and learn from you and others (including any data with special protections you choose to provide); how you use and interact with our Products; and the people, places or things that you're connected to and interested in on and off our services.</li><br />\n" +
            "\n" +
            "<li style=\"font-size:110%;\">        Information Across Fixirman App: We connect information about your activities on Fixirman App to provide a more customized and consistent experience. We can also make your experience more seamless, for example, by automatically filling in your registration information (such as your phone number) as well as automatically syncing your profile with LinkedIn and Facebook.</li><br />\n" +
            "\n" +
            "<li style=\"font-size:110%;\">        Location-Related Information: We use location related information - such as your current location, where you live, the places you like to go, and the businesses and people you're near - to provide, personalize and improve our Products, for you and others. Location-related information can be based on things such as precise device location (if you've allowed us to collect it), Information from your and others use of Fixirman App (such as check-ins or events you attend).</li><br />\n" +
            "\n" +
            "<li style=\"font-size:110%;\">        Product Research and Development, we use the information we have to develop, test and improve our Products, including by conducting surveys and research, and testing and troubleshooting new products and features.</li>\n" +
            "\n" +
            "</ul>\n" +
            "<h3 style=\"font-size:110%;\">Transfer of Data</h3>\n" +
            "\n" +
            "<p style=\"font-size:110%;\">Your information, including Personal Data, may be transferred to - and maintained on - computers located outside of your state, province, country or other governmental jurisdiction where the data protection laws may differ than those from your jurisdiction.If you are located outside Pakistan and choose to provide information to us, please note that we transfer the data, including Personal Data, to Pakistan and process it there.</p>\n" +
            "\n" +
            "<h3 style=\"font-size:110%;\">Promote Safety, Integrity and security</h3>\n" +
            "\n" +
            "<p style=\"font-size:110%;\">We use the information that we have to verify accounts and activity, combat harmful conduct, detect and prevent spam and other bad experiences, maintain the integrity of our Products, and promote safety and security on and off Fixirman App services. For example, we use data that we have to investigate suspicious activity or breaches of our security.</p>\n" +
            "\n" +
            "<h3 style=\"font-size:110%;\">Communicate with You</h3>\n" +
            "\n" +
            "<p style=\"font-size:110%;\">We use the information that we have to send you marketing communications, communicate with you about our services and let you know about our Policies and Terms. We also use your information to respond to you when you contact us.</p>\n" +
            "\n" +
            "<h3 style=\"font-size:110%;\">Email</h3>\n" +
            "\n" +
            "<p style=\"font-size:110%;\">By registering for the Fixirman App, members consent to receive emails from Fixirman Community. Fixirman App will send periodic news and information to Fixirman App affiliates through email.</p>\n" +
            "\n" +
            "<h3 style=\"font-size:110%;\">Password Confidentiality</h3>\n" +
            "\n" +
            "<p style=\"font-size:110%;\">Please do not give your \\\"Fixirman App\\\" App password to anyone. Together with your login name, your password is your key to managing your Fixirman App information. Your login name and password provide easy access to your directory profile, Email Forwarding for Life, career tools, and other confidential services.</p>\n" +
            "\n" +
            "<h3 style=\"font-size:110%;\">Limitation of Liability</h3>\n" +
            "\n" +
            "<p style=\"font-size:110%;\">In no event shall Fixirman Community Members, be liable for any direct, indirect, punitive, incidental, special, consequential, or other damages arising out of or in any way connected with the use of the Fixirman App or with the delay or in ability to use Fixirman App, or for any information, software, products, and services obtained through the Fixirman App, or otherwise arising out of the use of the Fixirman App. Whether based on contract, tort, strict liability, or otherwise. The user hereby specifically acknowledges and agrees that neither Fixirman Community shall not be liable for any defamatory, offensive, or illegal conduct of any user of the “Fixirman App” (App).</p>\n" +
            "\n" +
            "<p style=\"font-size:110%;\">Please note that Fixirman Community reserve the right to change this user agreement at any time. Your continued use of Fixirman App (App) indicates your acceptance of such changes.</p>\n" +
            "<h3 style=\"font-size:110%;\">Contact Information</h3>\n" +
            "\n" +
            "<p style=\"font-size:110%;\">Information when not needed will be deleted by us and we will \"also make sure that information when not required is deleted by our 3rd Party\"Partners. If you wish to delete your information you can send us a request to \"delete your information. We will need some additional information from you to\"delete your information. Send us your queries regarding deletion of your\"information and in case you have any questions at<b> info@viiontech.com</b></p>\n" +
            "\n" +
            "<b>© 2020 Copyright: Fixirman Community</b>";
}
