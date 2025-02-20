package de.glowman554.search;

import de.glowman554.search.utils.Migration;

import java.sql.Connection;
import java.sql.SQLException;

public class Migrations {
    public static void apply(Connection connection) throws SQLException {
        new Migration("crawler_queue", (_, statement) -> {
            statement.execute("create table `crawlerQueue` (`link` varchar(512) NOT NULL, `timestamp` timestamp NULL DEFAULT CURRENT_TIMESTAMP, PRIMARY KEY (`link`))");
        }, connection);

        new Migration("web_pages", (_, statement) -> {
            statement.execute("create table `webPages` (`link` varchar(512) NOT NULL, `title` text, `content` longtext, `description` text, `keywords` text, `shortText` text, `timestamp` timestamp NULL DEFAULT CURRENT_TIMESTAMP, PRIMARY KEY (`link`), FULLTEXT KEY `content` (`title`, `link`, `content`, `description`, `keywords`))");
        }, connection);

        new Migration("user_tables", (_, statement) -> {
            statement.execute("""
                    create table `users` (
                      `username` varchar(100) NOT NULL,
                      `createdAt` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                      `profilePictureUrl` text NOT NULL,
                      `passwordHash` varchar(100) NOT NULL,
                      `isAdministrator` tinyint(1) NOT NULL DEFAULT '0',
                      `isPremiumUser` tinyint(1) NOT NULL DEFAULT '0',
                      `shouldKeepHistory` tinyint(1) NOT NULL DEFAULT '1',
                      PRIMARY KEY (`username`)
                    )
                    """);

            statement.execute("""
                    create table `userSessions` (
                      `username` varchar(100) NOT NULL,
                      `token` varchar(100) NOT NULL,
                      `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                      PRIMARY KEY (`username`,`token`),
                      CONSTRAINT `userSessions_users_FK` FOREIGN KEY (`username`) REFERENCES `users` (`username`) ON DELETE CASCADE ON UPDATE CASCADE
                    )
                    """);
        }, connection);

        new Migration("user_history", (_, statement) -> {
            statement.execute("""
                    create table `searchHistory` (
                      `username` varchar(100) NOT NULL,
                      `id` int NOT NULL AUTO_INCREMENT,
                      `query` text NOT NULL,
                      `timestamp` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
                      PRIMARY KEY (`id`),
                      KEY `searchHistory_users_FK` (`username`),
                      CONSTRAINT `searchHistory_users_FK` FOREIGN KEY (`username`) REFERENCES `users` (`username`) ON DELETE CASCADE ON UPDATE CASCADE
                    )
                    """);

            statement.execute("""
                    create table `visitHistory` (
                      `username` varchar(100) NOT NULL,
                      `id` int NOT NULL AUTO_INCREMENT,
                      `link` varchar(512) NOT NULL,
                      `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                      PRIMARY KEY (`id`),
                      KEY `visitHistory_users_FK` (`username`),
                      KEY `visitHistory_webPages_FK` (`link`),
                      CONSTRAINT `visitHistory_users_FK` FOREIGN KEY (`username`) REFERENCES `users` (`username`) ON DELETE CASCADE ON UPDATE CASCADE,
                      CONSTRAINT `visitHistory_webPages_FK` FOREIGN KEY (`link`) REFERENCES `webPages` (`link`) ON DELETE CASCADE ON UPDATE CASCADE
                    )
                    """);

            statement.execute("""
                    create table `crawlRequests` (
                      `username` varchar(100) NOT NULL,
                      `id` int NOT NULL AUTO_INCREMENT,
                      `url` text NOT NULL,
                      `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                      PRIMARY KEY (`id`),
                      KEY `NewTable_users_FK` (`username`),
                      CONSTRAINT `NewTable_users_FK` FOREIGN KEY (`username`) REFERENCES `users` (`username`) ON DELETE CASCADE ON UPDATE CASCADE
                    )
                    """);
        }, connection);
    }
}
