package de.glowman554.search;

import de.glowman554.config.auto.AutoSavable;
import de.glowman554.crawler.core.Crawler;
import de.glowman554.crawler.core.CrawlerDatabaseConnection;
import de.glowman554.crawler.core.IPageInserter;
import de.glowman554.crawler.core.queue.AbstractQueue;
import de.glowman554.search.api.v1.V1;
import de.glowman554.search.user.UserManager;
import de.glowman554.search.utils.Logger;
import de.glowman554.search.utils.MutedException;
import de.glowman554.search.utils.config.DateProcessor;
import de.glowman554.search.utils.config.FileProcessor;
import de.glowman554.search.utils.filething.FileThingApi;
import de.glowman554.search.utils.unsplash.UnsplashApi;
import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import net.shadew.json.Json;
import net.shadew.json.JsonNode;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.util.ssl.SslContextFactory;

import java.io.File;
import java.io.FileInputStream;
import java.sql.SQLException;
import java.util.Date;

public class Main {
    private static BackendDatabaseConnection databaseConnection;
    private static UserManager userManager;
    private static Crawler crawler;
    private static FileThingApi fileThingApi;
    private static UnsplashApi unsplashApi;

    public static void main(String[] args) throws SQLException {
        loadCustomTypes();
        Config config = new Config();
        config.load();
        Logger.log("Config loaded");

        DatabaseConfig databaseConfig = config.getDatabase();
        databaseConnection = new BackendDatabaseConnection(databaseConfig);

        userManager = new UserManager(databaseConnection);
        fileThingApi = new FileThingApi(config.getFileThing().getUploadServer(), config.getFileThing().getAuthToken());
        unsplashApi = new UnsplashApi(config.getUnsplashToken());

        initCrawler(databaseConnection);
        initApiServer(config);
    }

    private static void loadCustomTypes() {
        AutoSavable.register(File.class, new FileProcessor());
        AutoSavable.register(Date.class, new DateProcessor());
    }

    private static void initCrawler(BaseDatabaseConnection base) {
        CrawlerDatabaseConnection databaseConnection = new CrawlerDatabaseConnection(base);

        AbstractQueue queue = databaseConnection.getQueue();
        IPageInserter inserter = databaseConnection.getInserter();

        crawler = new Crawler(queue, inserter);
        Logger.log("Crawler initialized");
    }

    private static void initApiServer(Config config) {
        Javalin app = Javalin.create(javalinConfig -> {
            Config.WebserverConfig webserverConfig = config.getWebserver();

            File frontend = webserverConfig.getFrontendPath();
            if (frontend.exists()) {
                javalinConfig.staticFiles.add(staticFileConfig -> {
                    staticFileConfig.hostedPath = "/";
                    staticFileConfig.directory = frontend.getAbsolutePath();
                    staticFileConfig.location = Location.EXTERNAL;
                });
                Logger.log("Frontend path: %s", frontend.getAbsolutePath());
            } else {
                Logger.log("[WARNING] Frontend path not found!");
            }

            if (webserverConfig.isSSL()) {
                javalinConfig.jetty.addConnector((server, httpConfiguration) -> {
                    ServerConnector sslConnector = new ServerConnector(server, getSslContextFactory(webserverConfig));
                    sslConnector.setPort(webserverConfig.getPort());
                    return sslConnector;
                });
            } else {
                javalinConfig.jetty.addConnector((server, httpConfiguration) -> {
                    ServerConnector connector = new ServerConnector(server);
                    connector.setPort(webserverConfig.getPort());
                    return connector;
                });
            }
        });

        app.exception(Exception.class, (exception, context) -> {
            if (!(exception instanceof MutedException)) {
                Logger.exception(exception);
            }

            context.status(500);
            context.header("Content-Type", "application/json");

            JsonNode error = JsonNode.object()
                    .set("status", "error")
                    .set("message", exception.getMessage());
            context.result(Json.json().serialize(error));
        });

        app.error(404, context -> {
            context.header("Content-Type", "text/html");
            context.result(new FileInputStream(new File(config.getWebserver().getFrontendPath(), "404.html")));
        });

        app.before(context -> context.header("Access-Control-Allow-Origin", "*"));

        V1.register(app);

        app.start();
        Logger.log("Listening on port %d", config.getWebserver().getPort());
    }

    private static SslContextFactory.Server getSslContextFactory(Config.WebserverConfig config) {
        SslContextFactory.Server sslContextFactory = new SslContextFactory.Server();
        sslContextFactory.setKeyStorePath(config.getKeystoreFile());
        sslContextFactory.setKeyStorePassword(config.getKeystorePassword());
        return sslContextFactory;
    }

    public static UserManager getUserManager() {
        return userManager;
    }

    public static BackendDatabaseConnection getDatabaseConnection() {
        return databaseConnection;
    }

    public static FileThingApi getFileThingApi() {
        return fileThingApi;
    }

    public static UnsplashApi getUnsplashApi() {
        return unsplashApi;
    }

    public static Crawler getCrawler() {
        return crawler;
    }
}
