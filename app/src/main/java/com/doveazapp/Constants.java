package com.doveazapp;


/**
 * Created by Karthik on 2015/12/16.
 */
public class Constants {

    /*// Base URL for the app
    public final static String BASE_URL = "http://dev.appdemo.biz/doveaz/api";*/


    // Base URL for the app
    /*public final static String BASE_URL = "http://doveaz.co.in/api";*/

    public final static String BASE_URL = "http://dealanzer.com/api"; // for testing purpose

    //LOGIN
    public final static String LOGIN_URL = BASE_URL + "/Users/login";

    //OTP PROCESS SEND
    public final static String SEND_OTP = BASE_URL + "/Otp/send";
    //OTP PROCESS VERIFY
    public final static String OTP_VERIFY = BASE_URL + "/Otp/verify";

    //REGISTER
    public final static String REGISTER_URL = BASE_URL + "/Users/register";

    //FORGOT PASSWORD
    public final static String FORGOT_URL = BASE_URL + "/Users/forgot_password";

    //GET_CATEGORIES
    public final static String GET_CATEGORIES = BASE_URL + "/Services/getAllCategories";

    public final static String SAVE_BUYANDDELIVERY = BASE_URL + "/Services/saveService";

    public final static String CALCULATE_CREDITS = BASE_URL + "/Services/calculateCredit";

    public final static String SAVE_CONTACTS = BASE_URL + "/Contacts/save";

    public final static String DISPATCH_COUNTS = BASE_URL + "/Dispatch";

    public final static String GET_LIST_B_PROFILES = BASE_URL + "/Dispatch/listBProfiles";

    public final static String GET_LIST_A_PROFILES = BASE_URL + "/Dispatch/listAProfiles";

    public final static String GET_USERDETAILS_FROM_LIST = BASE_URL + "/Dispatch/getDetailedProfile";

    public final static String GET_ENGAGEMENT = BASE_URL + "/Engage";

    public final static String NEGOTIATE_SERVICE = BASE_URL + "/Negotiate";

    public final static String NEGOTIATE_SERVICE_ACCEPT = BASE_URL + "/Negotiate/accept";

    public final static String NEGOTIATE_SERVICE_DECLINE = BASE_URL + "/Negotiate/decline";

    public final static String NOTIFICATION_RESPONSE = BASE_URL + "/Notification";

    public final static String NOTIFICATION_CHECK_SERVICE_STATUS = BASE_URL + "/Milestone/listHandshakedServices";

    public final static String NOTIFICATION_STATUS = BASE_URL + "/Engage/status";

    public final static String TRANSFER_CREDITS = BASE_URL + "/Credits/transfer";

    public final static String CHECK_USER_CREDITS = BASE_URL + "/Credits";

    public final static String CHECK_ADMIN_BALANCE = BASE_URL + "/Credits/checkMain";

    public final static String PURCHASE_CREDITS = BASE_URL + "/Credits/purchase";

    public final static String MILESTONES = BASE_URL + "/Milestone";

    public final static String MILESTONE_DETAILS = BASE_URL + "/Milestone/details";

    public final static String MILESTONE_APPROVE = BASE_URL + "/Milestone/approve";

    public final static String MILESTONE_DELINE = BASE_URL + "/Milestone/decline";

    public final static String GET_MILESTONE_LIST = BASE_URL + "/Milestone/listFinalAgreements";

    public final static String USER_RATING_API = BASE_URL + "/Rate";

    public final static String WITHDRAW_CREDIT_API = BASE_URL + "/Credits/withdraw";

    public final static String CREATE_GROUP_API = BASE_URL + "/Groups/create";

    public final static String GET_CONTACTS_TO_CREATE_GROUP = BASE_URL + "/Contacts/getVerifiedContacts";

    public final static String GET_PARTNER_CONTACTS_TO_CREATE_GROUP = BASE_URL + "/Contacts/getVerifiedPartners";

    public final static String ADD_MEMBER_TO_GROUP = BASE_URL + "/Groups/addMember";

    public final static String LIST_MY_GROUPS = BASE_URL + "/Groups";

    public final static String DELETE_GROUP = BASE_URL + "/Groups/delete";

    public final static String GROUP_DETAILS_TYPE_A = BASE_URL + "/Groups/details";

    public final static String ADD_SERVICE_TO_GROUP = BASE_URL + "/Groups/addToGroup";

    public final static String LIST_GROUP_SERVICES = BASE_URL + "/Groups/listServicesGroup";

    public final static String GROUP_SERVICE_DETAILS = BASE_URL + "/Services/getServiceDetails";

    public final static String GROUP_MEMBERS_LIST = BASE_URL + "/Groups/members";

    public final static String DELETE_GROUP_MEMBER = BASE_URL + "/Groups/deleteMember";

    public final static String AUTHENTICATE_USER = BASE_URL + "/Users/authenticate";

    public final static String GET_TRANSACTION_DETAILS = BASE_URL + "/Groups/getTypeBProfileByTransactionId";

    public final static String GET_DELIVERY_ADDRESS = BASE_URL + "/Extra/getDeliveryAddress";

    public final static String GET_ITEM_DETAILS = BASE_URL + "/Extra/getItemDetails";

    public final static String GET_STATES = BASE_URL + "/Extra/getStatesByCountry";

    public final static String GET_CITIES = BASE_URL + "/Extra/getCitiesByStates";

    public final static String GET_AREA = BASE_URL + "/Store/getArea";

    public final static String GET_LOCALS = BASE_URL + "/Store/getStreets";

    public final static String GET_STORECATEGORIES = BASE_URL + "/Store/getStoreCategories";

    public final static String GET_STORES = BASE_URL + "/Store/listStoreByCategory";

    public final static String GET_PRIME_OR_NOT = BASE_URL + "/Store/getUserByPhone";

    public final static String GET_STORE_PRODUCT_LIST = BASE_URL + "/Store/listProductsByStore";

    public final static String ADD_ORDER = BASE_URL + "/Store/addOrder";

    public final static String CREATE_ORDER = BASE_URL + "/Store/createOrder";

    public final static String DISPATCH_ORDER = BASE_URL + "/Dispatch";

    public final static String AGENT_PICKUP_AREAS = BASE_URL + "/Agents/pickupAreas";

    public final static String AGENT_ORDER_HISTORY = BASE_URL + "/Agents/orderHistory";

    public final static String AGENT_DELIVERY_AREAS = BASE_URL + "/Agents/deliveryAreas";

    public final static String UPDATE_AGENT_AREA = BASE_URL + "/Agents/updateArea";

    public final static String PICKUP_LIST_BY_AREA = BASE_URL + "/Agents/pickupListByArea";

    public final static String DELIVERY_LIST_BY_AREA = BASE_URL + "/Agents/deliveryListByArea";

    public final static String AGENT_UPDATE_ORDER_STATUS = BASE_URL + "/Agents/updateOrderStatus";

    public final static String VIEW_ORDER_TYPE_A = BASE_URL + "/Store/viewOrder";

    public final static String VIEW_MY_ORDER = BASE_URL + "/Store/viewMyOrder";

    public final static String CANCEL_ORDER_TYPE_A = BASE_URL + "/Store/cancelOrder";

    public final static String ORDER_CHECK = BASE_URL + "/Store/orderExist";

    public final static String ORDER_CHECK_TYPE_B = BASE_URL + "/Notification/getDetailsB";

    public final static String AGENT_ACCEPT_ORDER = BASE_URL + "/Dispatch/accept";

    public final static String AGENT_DECLINE_ORDER = BASE_URL + "/Dispatch/reject";

    public final static String VIEW_ORDER_INFO = BASE_URL + "/Store/viewMyOrderDetails";

    public final static String CALCULATE_HASH = BASE_URL + "/Credits/createHash";


    public final static String CALCULATE_HASH_FOR_PAYUMONEY = "http://dealanzer.com/moneyhash.php";



    //Select image from camera Toast message
    public static final String SELECT_IMAGE_TOAST_MESSAGE = "Please select Image from gallery or from camera";


    //Params for Login
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_EMAIL = "email";

    //Params for OTP
    public static final String KEY_PHONE = "phone";
    public static final String KEY_OTP = "otp";
    public static final String KEY_COUNTRY_CODE = "country_code";
    public static final String KEY_COUNTRY_CODE_ISO = "country_code_iso";

    public final static String NUMBER_EXIST = "check_is_exist";
    public final static String KEY_CONFIRM_PASSWORD = "confirm_password";

    //Params for Register(SignUp)
    public static final String KEY_FULLNAME = "fullname";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_GENDER = "gender";
    public static final String KEY_DOB = "dob";
    public static final String KEY_STATE = "state";
    public static final String KEY_CITY = "city";
    public static final String KEY_AREA = "area";
    public static final String KEY_STREET = "streetaddress";
    public static final String KEY_PARTNER = "partner";
    public static final String KEY_NATIONALITY = "nationality";
    public static final String KEY_EDUCATION = "education";
    public static final String KEY_PROFESSION = "profession";
    public static final String KEY_PRESENTADDRESS = "presentaddress";
    public static final String KEY_ID1 = "idproof1";
    public static final String KEY_ID2 = "idproof2";
    public static final String KEY_POSTALCODE = "postalcode";
    public static final String KEY_COUNTRYCODE = "country_code";
    public static final String KEY_PROFILEPIC = "profilepic";
    public static final String KEY_CONTACTS = "contacts";
    public static final String KEY_TYPE = "type";

    public static final String KEY_API_TOKEN = "Api-Token";


    public static final String KEY_SERVICETYPE = "service_type";
    public static final String KEY_CATEGORY = "category_id";
    public static final String KEY_ITEM_SHORT_DESC = "item_short_description";
    public static final String KEY_IMAGE = "image";
    public static final String KEY_REGION = "where_to_buy";
    public static final String KEY_PICK_ADDRESS = "pick_up_address";
    public static final String KEY_PICK_CITY = "pick_up_city";
    public static final String KEY_PICK_STATE = "pick_up_state";
    public static final String KEY_PICK_COUNTRY = "pick_up_country";
    public static final String KEY_PICK_POSTALCODE = "pick_up_postalcode";
    public static final String KEY_PACKAGE_TYPE = "type";
    public static final String KEY_DELIVERY_COUNTRY = "delivery_country";
    public static final String KEY_DELIVERY_ADDRESS = "delivery_address";
    public static final String KEY_DELIVERY_ADDRESS_TYPE = "delivery_address_type";
    public static final String KEY_DELIVERY_STREET = "delivery_street";
    public static final String KEY_DELIVERY_AREA = "delivery_area";
    public static final String KEY_DELIVERY_CITY = "delivery_city";
    public static final String KEY_DELIVERY_PHONE = "delivery_phone";
    public static final String KEY_DELIVERY_STATE = "delivery_state";
    public static final String KEY_DELIVERY_POSTAL = "delivery_postalcode";
    public static final String KEY_DELIVERY_DATE = "date";
    public static final String KEY_IKNOW_VALUE = "value_of_the_item";
    public static final String KEY_KNOWN_VALUE = "value";
    public static final String KEY_TIP_FEE = "fee";
    public static final String KEY_CREDITS = "credits";
    public static final String KEY_TOTAL_PRICE = "total_price";


    public static final String KEY_SENDER_ID = "sender_id";
    public static final String KEY_COLLECTION_ADDRESS = "collection_address";
    public static final String KEY_COLLECTION_CITY = "collection_city";
    public static final String KEY_COLLECTION_STATE = "collection_state";
    public static final String KEY_COLLECTION_COUNTRY = "collection_country";
    public static final String KEY_COLLECTION_POSTAL = "collection_postal";

    public static final String KEY_PARTNER_REGION = "where_you_based";
    public static final String KEY_TRAVEL_DATE = "travel_date";
    public static final String KEY_DELIVER_DATE = "delivery_date";
    public static final String KEY_CATEGORY_NAME = "category_name";
    public static final String KEY_RETURN_DATE = "return_date";
    public static final String KEY_ADDRESS = "address";
    public static final String KEY_COUNTRY = "country";
    public static final String KEY_OFFER = "offer";
    public static final String KEY_DATE = "date";

    public static final String KEY_DESTINATION_ADDRESS = "destination_address";
    public static final String KEY_DESTINATION_CITY = "destination_city";
    public static final String KEY_DESTINATION_STATE = "destination_state";
    public static final String KEY_DESTINATION_COUNTRY = "destination_country";
    public static final String KEY_DESTINATION_POSTAL = "destination_postalcode";
    public static final String KEY_TRAVEL_TYPE = "travel_type";


    public static final String KEY_USERID = "userid";
    public static final String KEY_SERVICEID = "service_id";
    public static final String KEY_RISKSCORE = "risk_score";
    public static final String KEY_SERVICE_B_ID = "service_b_id";
    public static final String KEY_SERVICE_A_ID = "service_a_id";
    public static final String KEY_TYPE_A_SERVICE_ID = "type_a_service_id";
    public static final String KEY_TYPE_B_SERVICE_ID = "type_b_service_id";
    public static final String KEY_NEGOTIATED_COL_ADDRESS = "negotiated_collection_address";
    public static final String KEY_NEGOTIATED_COL_CITY = "negotiated_collection_city";
    public static final String KEY_NEGOTIATED_COL_STATE = "negotiated_collection_state";
    public static final String KEY_NEGOTIATED_COL_COUNTRY = "negotiated_collection_country";
    public static final String KEY_NEGOTIATED_POSTAL = "negotiated_collection_postalcode";
    public static final String KEY_NEGOTIATED_D_ADDRESS = "negotiated_delivery_address";
    public static final String KEY_NEGOTIATED_D_CITY = "negotiated_delivery_city";
    public static final String KEY_NEGOTIATED_D_STATE = "negotiated_delivery_state";
    public static final String KEY_NEGOTIATED_D_COUNTRY = "negotiated_delivery_country";
    public static final String KEY_NEGOTIATED_D_POSTAL = "negotiated_delivery_postalcode";
    public static final String KEY_NEGOTIATED_FEE = "negotiated_fee";
    public static final String KEY_INITIATED_BY = "initiated_by";


    public static final String KEY_CONTACT_VERIFIED = "CONTACT_VERIFIED";
    public static final String KEY_CONTACTS_NOT_VERIFIED = "CONTACT_NOT_VERIFIED";
    public static final String KEY_FIRST_CONTACT_VERIFIED = "FIRST_CONTACT_VERIFIED";
    public static final String KEY_FIRST_CONTACT_NOT_VERIFIED = "FIRST_CONTACT_NOT_VERIFIED";
    public static final String KEY_UNKNOWN_VERIFIED = "UNKNOWN_CONTACT_VERIFIED";
    public static final String KEY_UNKNOWN_NOT_VERIFIED = "UNKNOWN_CONTACT_NOT_VERIFIED";


    //User Types
    public static final String KEY_TYPE_PARTNER = "1";
    public static final String KEY_TYPE_DELIVER = "0";
    public static final String KEY_TOTAL_CREDITS = "credits";
    public static final String KEY_REFERENCE = "reference_id";
    public static final String KEY_LOW_CREDIT = "low_credit";
    public static final String KEY_TOTAL_CREDIT = "total_credits";
    public static final String KEY_AVAILABLE_CREDIT = "available_credits";
    public static final String KEY_INTERNATIONAL = "International";
    public static final String KEY_LOCAL = "Local";
    public static final String KEY_DOMESTIC = "Domestic";
    public static final String KEY_ENVELOPE = "ENVELOPE";
    public static final String KEY_BAG = "BAG";
    public static final String KEY_SMALL_BOX = "SMALL BOX";
    public static final String KEY_LARGE_BOX = "LARGE BOX";
    public static final String KEY_EXTRA_LARGE = "EXTRA LARGE";


    //Button change for user type
    public static final String KEY_AVAILABE_JOBS = "Available Jobs";
    public static final String KEY_AVAILABE_PARTNERS = "Available Partners";


    // FOR MILESTONE
    public static final String KEY_STAGE = "stage";
    public static final String KEY_ITEM_UPLOAD = "image";
    public static final String KEY_BILL_UPLOAD = "bill";
    public static final String KEY_MILESTONE_THREE = "milestone_3_message";
    public static final String KEY_SUCCESS = "SUCCESS";
    public static final String KEY_PAYMENT_STATUS = "payment_status";
    public static final String KEY_STATUS = "status";
    public static final String KEY_MILESTONE1_PAYMENT_STATUS = "milestone_1_payment_status";
    public static final String KEY_MILESTONE1_STATUS = "milestone_1_status";
    public static final String KEY_MILESTONE2_IMAGE_URL = "milestone_2_image_url";
    public static final String KEY_MILESTONE2_STATUS = "milestone_2_status";
    public static final String KEY_MILESTONE3 = "milestone_3";
    public static final String KEY_MILESTONE3_STATUS = "milestone_3_status";
    public static final String KEY_MILESTONE1_DATE = "milestone_1_date";
    public static final String KEY_MILESTONE2_DATE = "milestone_2_date";
    public static final String KEY_MILESTONE3_DATE = "milestone_3_date";
    public static final String KEY_RATING = "rating";
    public static final String LOGIN_PARTNER = "partner_loggedin";
    public static final String LOGIN_DELIVER = "deliver_loggedin";
    public static final String KEY_DELIVER = "deliver";
    public static final String KEY_VALUE = "value";
    public static final String KEY_MILESTONE_DETAILS = "milestone_details";

    /*
    * App Variables used in parsing, progress bar, etc.,
    * */
    public static final String KEY_MAILID_VALIDATE = "Please enter valid mail id";
    public static final String KEY_PASSWORD_VALIDATE = "Minimum 6 characters required";
    public static final String KEY_BUSINESS = "business";
    public static final String KEY_STOREID = "store_id";
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_OWNER_ID = "owner_id";
    public static final String KEY_ORDER_QTY = "max_order_qty";
    public static final String KEY_UNIT_PRICE = "unit_price";
    public static final String KEY_ADDRESS_TYPE = "address_type";
    public static final String KEY_DELIVERY_ZIP = "delivery_zip";
    public static final String KEY_ORDER_DETAILS = "order_details";
    public static final String KEY_PICKUP_PHONE = "pickup_phone";
    public static final String KEY_PICKUP_ADDR_TYPE = "pickup_address_type";
    public static final String KEY_PICKUP_STREET = "pickup_street";
    public static final String KEY_PICKUP_AREA = "pickup_area";
    public static final String KEY_PICKUP_CITY = "pickup_city";
    public static final String KEY_PICKUP_STATE = "pickup_state";
    public static final String KEY_PICKUP_COUNTRY = "pickup_country";
    public static final String KEY_PICKUP_ZIPCODE = "pickup_zip";
    public static final String KEY_FLOOR_NUMBER = "floor_number";
    public static final String KEY_PICKUP_NAME = "pickup_name";
    public static final String KEY_DELIVERY_NAME = "delivery_name";
    public static final String KEY_PICKUP_FLOOR_NO = "pickup_floor_no";
    public static final String KEY_DELIVERY_FLOOR_NO = "delivery_floor_no";
    public static final String KEY_ORDER_ID = "order_id";
    public static final String KEY_ORDER_ITEMS = "order_items";
    public static final String KEY_CREDIT_TYPE = "credit_type";
    public static final String KEY_PRICE_STORE = "price_store";
    public static final String KEY_ORDERS = "orders";
    public static final String KEY_ORDER_STATUS = "delivery_status";
    public static final String KEY_PRICE = "price";
    public static final String KEY_QUANTITY = "qty";
    public static final String KEY_DELIVERY_STATUS = "delivery_status";
    public static final String KEY_ORDER_TYPE = "order_type";
    public static final String KEY_RESULT = "result";
    public static final String KEY_PRODUCT_ID = "product_id";
    public static final String KEY_PRODUCT_NAME = "product_name";
    public static final String KEY_PRODUCT_QUANTITY = "quantity";
    public static final String KEY_PAYMENT_TYPE = "payment_type";
    public static final String KEY_PICKUP_ADDRESS = "pickup_address";
    public static final String KEY_COLLECTION_AMOUNT = "collection_amount";

    //Smarty
    String SMARTY_STREETS = "https://api.smartystreets.com/street-address?auth-id=1aefa793-57b5-c233-4f5e-1f564bc981b9&auth-token=vA78RIZHOI36nbmQJFh2" +
            "&street=1600,amphitheatrepkwy&city=mountainview&state=CA&candidates=10";

    public static final String SMARTY_STREETS_FOR_VALIDATION = "https://api.smartystreets.com/street-address?auth-id=1aefa793-57b5-c233-4f5e-1f564bc981b9&auth-token=vA78RIZHOI36nbmQJFh2";

    public static final String KEY_NAME = "name";
    public static final String KEY_PROFILE_PIC = "profile_pic";
    public static final String KEY_ID = "id";
    public static final String KEY_RECEIVER_ID = "receiver_id";
    public static final String KEY_GOTO_MILESTONE = "goto_milestone";
    public static final String KEY_REFERENCE_ID = "reference_id";
    public static final String KEY_USER_ID = "userid";
    public static final String KEY_FEE = "fee";
    public static final String KEY_VALUE_ITEM = "value";
    public static final String KEY_SERVICE_TYPE = "service_type";
    public static final String KEY_MILESTONE1_IMAGE = "milestone_1_image_url";
    public static final String KEY_MILESTONE2_IMAGE = "milestone_2_bill_url";
    public static final String DATE_OF_ACCEPTANCE = "date_of_acceptance";
    public static final String TYPE_A_USERID = "type_a_userid";
    public static final String TYPE_B_USERID = "type_b_userid";
    public static final String TYPE_A_SERVICEID = "type_a_service_id";
    public static final String TYPE_B_SERVICEID = "type_b_service_id";
    public static final String KEY_TRANSACTION_ID = "transaction_id";


    //For contacts response
    public static final String KEY_CONTACTS_VERIFIED = "contacts_verfied";
    public static final String KEY_FIRST_CONTACT = "first_contact_verified";
    public static final String KEY_UNKNOWN = "unknown_contacts_verified";

    //
    public static final String KEY_TOKEN = "api_token";


    //** GROUPING **//
    public static final String KEY_GROUP_NAME = "group_name";
    public static final String KEY_GROUP_SLOGAN = "group_slogan";
    public static final String KEY_GROUP_CREATED = "Group created";
    public static final String KEY_GROUP_ID = "group_id";
    public static final String KEY_PROFILE_PICTURE = "profile_pic_url";
    public static final String KEY_GROUP_COUNT = "member_count";
    public static final String KEY_ADMIN = "admin";
    public static final String KEY_VERIFIED_PROFILES = "verified_profiles";
    public static final String KEY_ITEM_DETAILS = "item_description";

    // paypal variables

    // note that these credentials will differ between live & sandbox environments.
    public static final String CONFIG_CLIENT_ID = "AYv2OPwnZivuOeC6y3rbTWcZiyYDDQPa4CwfgK_r8kcgrOc_wXuI2Mu7ip78PjSCODQHu55HA2LD9oZ2";

    public static final int REQUEST_CODE_PAYMENT = 1;
    public static final int REQUEST_CODE_FUTURE_PAYMENT = 2;
    public static final int REQUEST_CODE_PROFILE_SHARING = 3;


    // GCM VARIABLES

    public static final String GCM_TOKEN = "gcm_token";
    public static final String USER_IDS = "userids";
    public static final String MESSAGE = "message";
    public static final String KEY_NOTIFY_USERID = "notify_userid";


    public static final String KEY_USER_DETAILS = "user_details";

    public static final String KEY_USER = "user";
    public static final String KEY_TRUE = "true";
    public static final String KEY_FALSE = "false";
    public static final String KEY_HOLDER_VALUE = "holder_details";

    /*shared Preference values*/
    public static final String PREFS_NAME = "preferences";
    public static final String PREF_UNAME = "Username";
    public static final String KEY_ONE = "1";
    public static final String KEY_ZERO = "0";
    public static final String KEY_FLAG = "flag";
    public static final String KEY_ORDER = "order";

    /*Delivery status*/
    public static final String ACCEPTED = "Accepted";
    public static final String PICKED = "Picked";
    public static final String DELIVERED = "Delivered";
    public static final String NOT_AVAILABLE = "N/A";
    public static final String PLEASE_WAIT = "Please wait...";
    public static final String LOADING = "Loading...";
    public static final String REQUESTING = "Requesting...";
    public static final String CITIES = "cities";
    public static final String CITY_NAME = "city_name";
    public static final String CATEGORIES = "categories";
    public static final String STORES = "stores";
    public static final String MOBILE_NUMBER = "Mobile Number";
    public static final String KEY_USER_TYPE = "user_type";

    //PayUmoney Vriables
    public static final String PAY_U_MONEY_KEY = "ZAcZtRp6";
    public static final String PAY_U_MONEY_MERCHANT_ID = "5599247";
    public static final String PAY_U_MONEY_SALT_KEY = "tZn9EQT2cM";
    public static final String PAY_U_TXN_ID = "txnid";
    public static final String PAY_U_AMOUNT = "amount";
    public static final String PAY_U_PROD_INFO = "productinfo";
    public static final String PAY_U_KEY = "key";
    public static final String PAY_U_SALT = "salt";

}
