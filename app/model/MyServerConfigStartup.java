//package model;
//
//import com.avaje.ebean.config.ServerConfig;
//import com.avaje.ebean.config.dbplatform.DbIdentity;
//import com.avaje.ebean.config.dbplatform.IdType;
//import com.avaje.ebean.config.dbplatform.PostgresPlatform;
//import com.avaje.ebean.event.ServerConfigStartup;
//
///**
// * Created by dima on 12/11/15.
// */
//public class MyServerConfigStartup implements ServerConfigStartup {
//    @Override
//    public void onStart(ServerConfig serverConfig) {
//        PostgresPlatform postgresPlatform = new PostgresPlatform();
//        DbIdentity dbIdentity = postgresPlatform.getDbIdentity();
//        dbIdentity.setSupportsGetGeneratedKeys(false);
//        dbIdentity.setSupportsSequence(true);
//        dbIdentity.setIdType(IdType.GENERATOR);
//        serverConfig.setDatabasePlatform(postgresPlatform);
//    }
//}
